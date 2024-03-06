package top.heerdev.heersextend.common.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.heerdev.heersextend.common.block.entity.SportsBoyGeneratorEntity;
import top.heerdev.heersextend.common.gui.util.ContainerMenuWithEnergy;

public class TesterContainer extends ContainerMenuWithEnergy {


    private SportsBoyGeneratorEntity entity;

    //BlockPos pos;

    public TesterContainer(int pContainerId, Inventory inv, BlockPos pos) {
        super(ModMenus.TESTER_CONTAINER.get(), pContainerId, inv);
        if (inv.player.level().getBlockEntity(pos) instanceof SportsBoyGeneratorEntity be) {
            this.entity = be;
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        System.out.println("[You clicked on slot " + pIndex + " in the container]");
        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public int getPower() {
        return 1000;
    }

    @Override
    public int getMaxPower() {
        return 5000;
    }
}
