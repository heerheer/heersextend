package top.heerdev.heersextend.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import top.heerdev.heersextend.common.block.ModBlocks;
import top.heerdev.heersextend.common.block.entity.util.DatableBlockEntity;
import top.heerdev.heersextend.common.item.ModItems;

public class SportsBoyGeneratorEntity extends DatableBlockEntity {
    private final BlockPos pos;
    private final BlockState blockState;


    private final ItemStackHandler itemStackHandler = new ItemStackHandler(1) {

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack insertStack) {
            return insertStack.getItem() == ModItems.WHITE_SOCKS.get();
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            super.onContentsChanged(slot);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_ALL);
            return super.insertItem(slot, stack, simulate);
        }
    };


    private final int MAX_EXTRACT = 4000;

    private final EnergyStorage energyStorage = new EnergyStorage(600000, 20 * 99, MAX_EXTRACT) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_ALL);
            return super.receiveEnergy(maxReceive, simulate);

        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int extracted = super.extractEnergy(maxExtract, simulate);
            level.sendBlockUpdated(pos, blockState, blockState, 3);
            return extracted;
        }
    };

    private int LEVEL = 1;
    public final double[] MultiplyMap = {1.0, 1.2, 1.5, 2.0, 3.0, 4.2, 6.0, 8.0, 10.5, 99};
    private long tickCounts = 0;

    //用于暴露给侧面的~
    private LazyOptional<EnergyStorage> lazyEnergyStorage = LazyOptional.of(() -> new EnergyStorage(600000, 0, MAX_EXTRACT) {
        @Override
        public boolean canExtract() {
            return energyStorage.canExtract();
        }

        @Override
        public boolean canReceive() {
            return false;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return energyStorage.extractEnergy(maxExtract, simulate);
        }
    });

    public SportsBoyGeneratorEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlocks.SPORTS_BOY_GENERATOR_ENTITY.get(), pPos, pBlockState);
        pos = pPos;
        blockState = pBlockState;

    }

    @Override
    protected void saveClientData(CompoundTag tag) {
        tag.put("INVENTORY".toUpperCase(), itemStackHandler.serializeNBT());
        tag.put("ENERGY".toUpperCase(), energyStorage.serializeNBT());
        tag.putInt("LEVEL", LEVEL);
        tag.putLong("TICK_COUNTS", tickCounts);
    }

    @Override
    protected void loadClientData(CompoundTag tag) {
        if (tag.contains("INVENTORY")) {
            itemStackHandler.deserializeNBT(tag.getCompound("INVENTORY"));
        }
        if (tag.contains("ENERGY")) {
            energyStorage.deserializeNBT(tag.get("ENERGY"));
        }
        if (tag.contains("LEVEL")) {
            LEVEL = tag.getInt("LEVEL");
        }
        if (tag.contains("TICK_COUNTS")) {
            tickCounts = tag.getLong("TICK_COUNTS");
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (side == this.getBlockState().getValue(DirectionalBlock.FACING)) {
            return super.getCapability(cap, side);
        } else {
            if (cap == ForgeCapabilities.ENERGY) {
                return lazyEnergyStorage.cast();
            }
            return super.getCapability(cap, side);
        }
    }

    public void tickServer() {

        if (level.getGameTime() % 20 != 0) {
            return;
        }

        distributeEnergy();

        this.tickCounts += 20;

        if (tickCounts > 20 * 60 * 60 * 24) {
            setChanged();
            this.tickCounts = 0;
            if (this.LEVEL < MultiplyMap.length)
                this.LEVEL++;
        }

        this.energyStorage.receiveEnergy(this.getCurrentEnergyPerTick(), false);
    }


    public IItemHandler getItemHandler() {
        return this.itemStackHandler;
    }

    public int getGeneratorLevel() {
        return LEVEL;
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public long getTickCounts() {
        return tickCounts;
    }

    private void distributeEnergy() {
        if (this.energyStorage.getEnergyStored() == 0) return;

        for (Direction direction : Direction.values()) {
            BlockEntity be = level.getBlockEntity(getBlockPos().relative(direction));
            if (be == null)
                continue;
            be.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent(
                    e -> {
                        if (e.canReceive()) {
                            int received = e.receiveEnergy(MAX_EXTRACT, false);
                            this.energyStorage.extractEnergy(received, false);
                            setChanged();
                        }
                    }
            );
        }
    }

    /**
     * 获取当前每秒可以产生的电量
     *
     * @return 电量每秒
     */
    public int getCurrentEnergyPerTick() {
        var stack = itemStackHandler.getStackInSlot(0);

        if (stack.isEmpty()) return 1;

        if (stack.is(ModItems.WHITE_SOCKS.get())) {
            return (int) Math.floor(20 * this.MultiplyMap[this.LEVEL - 1]);
        }
        return 0;
    }

}
