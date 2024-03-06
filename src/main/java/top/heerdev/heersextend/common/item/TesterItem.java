package top.heerdev.heersextend.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TesterItem extends Item {
    public TesterItem() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        if (!pLevel.isClientSide) {

            if (pPlayer.isShiftKeyDown()) {
            }

        } else {
            //Minecraft.getInstance().setScreen(new TesterScreen());
            System.out.println("Test");

        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
