package top.heerdev.heersextend.common.block.entity.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class WrappedItemHandler implements IItemHandlerModifiable {

    private final IItemHandlerModifiable handler;
    private final Predicate<Integer> extract;
    private final BiPredicate<Integer, ItemStack> insert;

    public WrappedItemHandler(IItemHandlerModifiable handler, Predicate<Integer> extract, BiPredicate<Integer, ItemStack> insert) {
        this.handler = handler;
        this.extract = extract;
        this.insert = insert;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.handler.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return handler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return insert.test(slot, stack) ? handler.insertItem(slot, stack, simulate) : stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return extract.test(slot) ? handler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return handler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return handler.isItemValid(slot, stack) && insert.test(slot, stack);
    }
}
