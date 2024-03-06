package top.heerdev.heersextend.common.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import top.heerdev.heersextend.common.block.entity.CyberKillerMachineEntity;
import top.heerdev.heersextend.common.gui.util.ContainerMenuWithEnergy;

public class CyberKillerMachineContainer extends ContainerMenuWithEnergy {


    private static final int SLOT_COUNT = CyberKillerMachineEntity.SLOT_COUNT;

    private CyberKillerMachineEntity blockEntity;
    protected BlockPos pos;

    public CyberKillerMachineContainer(int windowId, Inventory inv, BlockPos pos) {
        super(ModMenus.CYBER_KILLER.get(), windowId, inv);

        if (inv.player.level().getBlockEntity(pos) instanceof CyberKillerMachineEntity blockEntity) {
            this.blockEntity = blockEntity;
            this.pos = pos;
            addSlot(new SlotItemHandler(blockEntity.getInputItems(), 0, 80, 36));
        }

    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {


        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index < 1) {
                if (!this.moveItemStackTo(stack, 1, Inventory.INVENTORY_SIZE + SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else {

                if (!this.moveItemStackTo(stack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            //上述代码确保了在容器得到的关于方块实体内部item对应的槽位上shift按下会被转移到背包槽位,背包转移到容器槽位


            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return itemstack;
    }


    @Override
    public boolean stillValid(Player player) {
        return true;
        //return stillValid(ContainerLevelAccess.create(player.level(), pos), player, ModBlocks.CYBER_KILLER_MACHINE.get());
    }

    public int getPower() {
        return this.blockEntity.getPower();
    }

    public int getMaxPower() {
        return this.blockEntity.getMaxPower();
    }
}
