package top.heerdev.heersextend.client.screen.util;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.heerdev.heersextend.common.gui.util.ContainerMenuWithEnergy;

@OnlyIn(Dist.CLIENT)
public abstract class ScreenWithEnergy<T extends ContainerMenuWithEnergy> extends AbstractContainerScreen<T> {

    private static final int ENERGY_LEFT = 160;
    private static final int ENERGY_WIDTH = 8;
    private static final int ENERGY_TOP = 17;
    private static final int ENERGY_HEIGHT = 78 - 17 + 1;

    public ScreenWithEnergy(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;
        this.imageHeight = 175;
        // 放置在Screen上面的「物品栏」的Y位置
        this.inventoryLabelY = this.imageHeight - 95;
        System.out.println("ScreenWithEnergy Screen constructor");

    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        //绘制最基础的信息提示
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);

        //绘制当前能量
        renderEnergyBar(pGuiGraphics, menu.getPower(), menu.getMaxPower(), leftPos, topPos);

        //鼠标悬浮提示能量数据
        var relativeX = pMouseX - leftPos;
        var relativeY = pMouseY - topPos;

        if (relativeX >= ENERGY_LEFT && relativeX < ENERGY_LEFT + ENERGY_WIDTH
                && relativeY >= ENERGY_TOP && relativeY < ENERGY_TOP + ENERGY_HEIGHT) {
            int power = menu.getPower();
            pGuiGraphics.renderTooltip(this.font, Component.literal(power + " FE"), pMouseX, pMouseY);
        }
    }

    public void renderEnergyBar(GuiGraphics graphics, int power, int maxPower, int leftPos, int topPos) {
        if (power > 0) {
            var height = (int) Math.ceil(((double) power / maxPower) * ENERGY_HEIGHT);
            graphics.fill(leftPos + ENERGY_LEFT, topPos + ENERGY_TOP + ENERGY_HEIGHT - height, leftPos + ENERGY_LEFT + ENERGY_WIDTH, topPos + ENERGY_TOP + ENERGY_HEIGHT, 0xFF51D945);
        }
    }

}
