package top.heerdev.heersextend.common.gui.util;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public abstract class ContainerMenuWithEnergy extends AbstractContainerMenu {


    public ContainerMenuWithEnergy(MenuType<?> type, int windowId, Inventory inv) {
        super(type, windowId);
        layoutPlayerInventorySlots(inv, 8, 94);
    }

    public abstract int getPower();

    public abstract int getMaxPower();


    public void addSingleItemSlot(IItemHandler slotItemHandler, int index, int x, int y) {
        addSlot(new SlotItemHandler(slotItemHandler, index, x, y));

    }

    public int addSlotBox(Container playerInventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(playerInventory, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    public int addSlotRange(Container playerInventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new Slot(playerInventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(Container playerInventory, int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

}
