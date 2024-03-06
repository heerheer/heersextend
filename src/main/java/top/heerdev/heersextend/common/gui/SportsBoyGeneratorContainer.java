package top.heerdev.heersextend.common.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.heerdev.heersextend.common.block.entity.SportsBoyGeneratorEntity;
import top.heerdev.heersextend.common.gui.util.ContainerMenuWithEnergy;

public class SportsBoyGeneratorContainer extends ContainerMenuWithEnergy {

    private SportsBoyGeneratorEntity blockEntity;
    private final BlockPos pos;

    public SportsBoyGeneratorContainer(int windowId, Inventory inv, BlockPos pos) {
        super(ModMenus.SPORTS_BOY_GENERATOR_CONTAINER.get(), windowId, inv);
        this.pos = pos;
        if (inv.player.level().getBlockEntity(pos) instanceof SportsBoyGeneratorEntity be) {
            blockEntity = be;
        }

        // 36
        addSingleItemSlot(this.blockEntity.getItemHandler(), 0, 80, 36);
    }

    @Override
    public int getPower() {
        return blockEntity.getEnergyStorage().getEnergyStored();
    }

    @Override
    public int getMaxPower() {
        return blockEntity.getEnergyStorage().getMaxEnergyStored();
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        System.out.println("[You clicked on slot " + pIndex + " in the container]");
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {

        return true;
        //return stillValid(ContainerLevelAccess.create(player.level(), pos), player, ModBlocks.SPORTS_BOY_GENERATOR.get());
    }

    public SportsBoyGeneratorEntity getSportsBoyGeneratorEntity() {
        return blockEntity;
    }
}
