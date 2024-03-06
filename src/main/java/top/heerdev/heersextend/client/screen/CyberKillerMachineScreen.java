package top.heerdev.heersextend.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import top.heerdev.heersextend.HeersExtend;
import top.heerdev.heersextend.client.screen.util.ScreenWithEnergy;
import top.heerdev.heersextend.common.gui.CyberKillerMachineContainer;

public class CyberKillerMachineScreen extends ScreenWithEnergy<CyberKillerMachineContainer> {

    private final ResourceLocation GUI = new ResourceLocation(HeersExtend.MODID, "textures/gui/cyber_killer.png");

    public CyberKillerMachineScreen(CyberKillerMachineContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
    }

    @Override
    public void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}