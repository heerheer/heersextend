package top.heerdev.heersextend.common.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import top.heerdev.heersextend.common.block.ModBlocks;
import top.heerdev.heersextend.common.block.entity.NbtFilterEntity;

public class NbtFilterContainer extends AbstractContainerMenu {

    NbtFilterEntity entity;
    BlockPos pos;


    public NbtFilterContainer(int windowId, Inventory inv, BlockPos pos) {
        super(ModMenus.NBT_FILTER_CONTAINER.get(), windowId);
        this.pos = pos;
        if (inv.player.level().getBlockEntity(pos) instanceof NbtFilterEntity be) {
            entity = be;
        }

    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, ModBlocks.NBT_FILTER.get());
    }

    public String GetFilterString() {
        if (entity == null)
            return "";
        return entity.FilterString;
    }

    public BlockPos GetEntityPos() {
        if (entity == null)
            return null;
        return entity.getBlockPos();
    }

}
