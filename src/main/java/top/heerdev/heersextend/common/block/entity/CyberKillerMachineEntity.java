package top.heerdev.heersextend.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import top.heerdev.heersextend.common.block.ModBlocks;
import top.heerdev.heersextend.common.block.entity.util.DatableBlockEntity;
import top.heerdev.heersextend.common.block.entity.util.WrappedEnergyStorage;
import top.heerdev.heersextend.common.block.entity.util.WrappedItemHandler;
import top.heerdev.heersextend.common.gui.CyberKillerMachineContainer;
import top.heerdev.heersextend.common.util.CyberFakePlayer;
import top.heerdev.heersextend.common.util.TagUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class CyberKillerMachineEntity extends DatableBlockEntity implements MenuProvider {


    public static final String ITEMS_INPUT_TAG = "Input_Inventory";
    public static final String ITEMS_OUTPUT_TAG = "Output_Inventory";
    public static final String ENERGY_STORAGE_TAG = "Energy_Storage";


    public static final int SLOT_INPUT = 0;
    public static final int SLOT_INPUT_COUNT = 1;

    public static final int SLOT_OUTPUT = 0;
    public static final int SLOT_OUTPUT_COUNT = 0;

    public static final int SLOT_COUNT = SLOT_INPUT_COUNT + SLOT_OUTPUT_COUNT;

    private final ItemStackHandler inputItems = new ItemStackHandler(SLOT_INPUT_COUNT) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            return super.insertItem(slot, stack, simulate);

        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            var tag = TagUtil.INSTANCE.getItemTag("forge:tools");

            if (stack.is(tag.getKey())) {
                return true;
            } else {
                return false;
            }
        }
    };
    ;
    private final ItemStackHandler outputItems = createItemHandler(SLOT_OUTPUT_COUNT);


    private final EnergyStorage energy = new EnergyStorage(500000, 2000, 2000) {
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            return super.extractEnergy(maxExtract, simulate);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            return super.receiveEnergy(maxReceive, simulate);
        }
    };

    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> new WrappedEnergyStorage(energy) {
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return true;
        }
    });


    // 如果想要女的方块可以被输入,那么需要实现
    private final LazyOptional<IItemHandler> inputItemHandler = LazyOptional.of(() -> new WrappedItemHandler(inputItems, (i) -> false, (i, stack) -> i == 0));

    private final LazyOptional<IItemHandler> outputItemHandler = LazyOptional.of(() -> new WrappedItemHandler(outputItems, (i) -> i == 0, (i, stack) -> false));

    public CyberKillerMachineEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.CYBER_KILLER_ENTITY.get(), pos, state);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inputItemHandler.invalidate();
        outputItemHandler.invalidate();
    }


    public ItemStackHandler getInputItems() {
        return inputItems;
    }

    public ItemStackHandler getOutputItems() {
        return outputItems;
    }

    @Nonnull
    private ItemStackHandler createItemHandler(int size) {
        return new ItemStackHandler(size) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        };
    }

    private CyberFakePlayer fakePlayer;

    /**
     * 可以被升级
     * 默认4s自动执行，使用内置的武器斩杀一次附近所有的生物
     * 每斩杀一个生物需要消耗「2k」能量，可以被升级卡减少能量消耗
     */
    public void tickServer() {

        //boolean powered = energy.getEnergyStored() > 0;

        //level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, powered));


        //TODO 升级卡系统
        if (level.getGameTime() % 80 == 0) {

            if (inputItems.getStackInSlot(0) == ItemStack.EMPTY) {
                return;
            }

            if (fakePlayer == null) {
                var server = level.getServer();
                fakePlayer = CyberFakePlayer.getFakePlayer(server.getLevel(server.getLevel(Level.OVERWORLD).dimension()));
            }
            //level.getServer().sendSystemMessage(Component.literal("Energy: " + energy.getEnergyStored()));


            var pos = this.getBlockPos();
            // get entities around
            fakePlayer.setPos(new Vec3(pos.getX(), pos.getY(), pos.getZ()));

            var range = new AABB(pos).inflate(3);
            List<Mob> mobs = this.level.getEntitiesOfClass(Mob.class, range).stream().filter(mobEntity -> !(mobEntity instanceof Animal &&
                    mobEntity.isBaby()) && !mobEntity.isInvulnerable() && !(mobEntity instanceof WitherBoss && ((WitherBoss) mobEntity).getInvulnerableTicks() > 0)).filter(LivingEntity::isAlive).collect(Collectors.toList());
            if (!mobs.isEmpty()) {

                fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, inputItems.getStackInSlot(0));

                DamageSource source = fakePlayer.damageSources().playerAttack(fakePlayer);
                for (Mob mobEntity : mobs) {

                    //level.getServer().sendSystemMessage(Component.literal("Extract Energy: " + energy.extractEnergy(2000, true)));


                    if (energy.extractEnergy(2000, true) == 2000) {
                        energy.extractEnergy(2000, false);
                        mobEntity.setHealth(1);
                        mobEntity.hurt(source, 10);
                    } else {
                        break;
                    }

                }
            }
        }
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyHandler.cast();
        }
        return super.getCapability(cap, side);
    }


    public int getPower() {
        //System.out.println("getPower: " + energy.getEnergyStored());
        return energy.getEnergyStored();
    }

    public int getMaxPower() {
        //System.out.println("getPower: " + energy.getEnergyStored());
        return energy.getMaxEnergyStored();
    }

    @Override
    protected void saveClientData(CompoundTag tag) {
        tag.put(ITEMS_INPUT_TAG, inputItems.serializeNBT());
        tag.put(ITEMS_OUTPUT_TAG, outputItems.serializeNBT());
        tag.put(ENERGY_STORAGE_TAG, energy.serializeNBT());
    }

    @Override
    protected void loadClientData(CompoundTag tag) {
        if (tag.contains(ITEMS_INPUT_TAG)) {
            inputItems.deserializeNBT(tag.getCompound(ITEMS_INPUT_TAG));
        }
        if (tag.contains(ITEMS_OUTPUT_TAG)) {
            outputItems.deserializeNBT(tag.getCompound(ITEMS_OUTPUT_TAG));
        }
        if (tag.contains(ENERGY_STORAGE_TAG)) {
            energy.deserializeNBT(tag.get(ENERGY_STORAGE_TAG));
        }
    }


    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        // This is called client side
        CompoundTag tag = pkt.getTag();
        // This will call loadClientData()
        if (tag != null) {
            handleUpdateTag(tag);
        }
    }


    /**
     * 获取容器的名字
     *
     * @return
     */

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.heersextend.cyber_killer.title");
    }

    /**
     * 创建容器
     *
     * @param pContainerId
     * @param pPlayerInventory
     * @param pPlayer
     * @return
     */
    @org.jetbrains.annotations.Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory inv, Player pPlayer) {
        return new CyberKillerMachineContainer(pContainerId, inv, worldPosition);
    }

    public void drops() {
        //drop items in inventory
        if (!level.isClientSide) {


            SimpleContainer container = new SimpleContainer(inputItems.getSlots());
            for (int i = 0; i < inputItems.getSlots(); i++) {
                container.setItem(i, inputItems.getStackInSlot(i));
            }
            Containers.dropContents(level, worldPosition, container);
        }
    }
}