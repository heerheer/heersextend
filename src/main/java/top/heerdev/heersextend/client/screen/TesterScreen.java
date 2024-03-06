package top.heerdev.heersextend.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import top.heerdev.heersextend.HeersExtend;
import top.heerdev.heersextend.client.screen.util.ScreenWithEnergy;
import top.heerdev.heersextend.common.gui.TesterContainer;

public class TesterScreen extends ScreenWithEnergy<TesterContainer> {


    private final ResourceLocation GUI = new ResourceLocation(HeersExtend.MODID, "textures/gui/sports_boy_generator.png");


    public TesterScreen(TesterContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

    }


    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }



    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }


}
