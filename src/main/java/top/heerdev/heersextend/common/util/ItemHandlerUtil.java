package top.heerdev.heersextend.common.util;

import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemHandlerUtil {
    public static int FindFirstEmpytSlot(IItemHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            if (handler.getStackInSlot(i).isEmpty())
                return i;
        }
        return -1;
    }

    public static int FindFirstExistSlot(IItemHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty())
                return i;
        }
        return -1;
    }

    public static Tuple<Boolean, Integer> CanInsert(ItemStack stack, IItemHandler insertHandler) {
        for (int i = 0; i < insertHandler.getSlots(); i++) {
            if (insertHandler.getStackInSlot(i).isEmpty())
                return new Tuple<>(true, i);

            if (insertHandler.getStackInSlot(i).isStackable())
                if (insertHandler.getStackInSlot(i).getMaxStackSize() > insertHandler.getStackInSlot(i).getCount())
                    if (insertHandler.insertItem(i, stack, true) == ItemStack.EMPTY)
                        return new Tuple<>(true, i);
        }
        return new Tuple<>(false, -1);
    }

}
