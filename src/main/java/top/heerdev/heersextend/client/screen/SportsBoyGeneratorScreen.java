package top.heerdev.heersextend.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import top.heerdev.heersextend.HeersExtend;
import top.heerdev.heersextend.client.screen.util.ScreenWithEnergy;
import top.heerdev.heersextend.common.block.entity.SportsBoyGeneratorEntity;
import top.heerdev.heersextend.common.gui.SportsBoyGeneratorContainer;

public class SportsBoyGeneratorScreen extends ScreenWithEnergy<SportsBoyGeneratorContainer> {
    private SportsBoyGeneratorEntity blockEntity;

    private final ResourceLocation GUI = new ResourceLocation(HeersExtend.MODID, "textures/gui/sports_boy_generator.png");
    //private StringWidget levelDisplay;

    public SportsBoyGeneratorScreen(SportsBoyGeneratorContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);

        this.blockEntity = container.getSportsBoyGeneratorEntity();


    }

    @Override
    protected void init() {
        super.init();
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
        //this.levelDisplay.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);


        pGuiGraphics.drawString(font,
                "当前产电能力: " + blockEntity.getCurrentEnergyPerTick() + "FE / 20ticks",
                leftPos + this.titleLabelX, topPos + 60, 0x282c34, false);
        pGuiGraphics.drawString(font,
                "沉淀等级: " + blockEntity.getGeneratorLevel() + "(已沉淀 %d ticks)".formatted(blockEntity.getTickCounts()),
                leftPos + this.titleLabelX, topPos + 74, 0x282c34, false);
    }
}