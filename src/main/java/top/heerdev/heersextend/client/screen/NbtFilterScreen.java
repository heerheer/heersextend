package top.heerdev.heersextend.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import top.heerdev.heersextend.HeersExtend;
import top.heerdev.heersextend.common.gui.NbtFilterContainer;
import top.heerdev.heersextend.common.net.HeersExtendPacketHandler;
import top.heerdev.heersextend.common.net.NbtFilterUpdatedPacket;

public class NbtFilterScreen extends AbstractContainerScreen<NbtFilterContainer> {

    private final ResourceLocation GUI = new ResourceLocation(HeersExtend.MODID, "textures/gui/nbt_filter.png");

    public NbtFilterScreen(NbtFilterContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 175;

    }

    private EditBox name;

    private Button btn;
    private OptionsList optionsList;

    @Override
    protected void init() {

        super.init();

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        this.name = new EditBox(this.font, leftPos + 7, topPos + 32, 168 - 7 + 1, 47 - 32 + 1, Component.translatable("block.heersextend.nbt_filter.editbox"));
        this.name.setMaxLength(50);
        this.name.setVisible(true);
        this.name.setValue(this.menu.GetFilterString());
        this.name.setEditable(true);
        this.addWidget(this.name);
        this.setInitialFocus(this.name);
        //System.out.println(123);

        this.btn = new Button.Builder(Component.translatable("block.heersextend.nbt_filter.button"), (button) -> {
            saveFilterString();
        }).pos(leftPos + 70, topPos + 60).size(36, 14).build();
        this.addWidget(this.btn);

        // 7,64 63,79
        //this.optionsList = new OptionsList(this.minecraft, 63-7+1, 79-64+1, 32, this.height - 32, 25);
    }

    private void saveFilterString() {
        HeersExtendPacketHandler.INSTANCE.sendToServer(new NbtFilterUpdatedPacket(this.name.getValue(), this.menu.GetEntityPos()));
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
        this.name.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.btn.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {

        if (pKeyCode == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return this.name.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.name.tick();
    }

    //解决resize后界面上文字消失的问题
    @Override
    public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
        String s = this.name.getValue();
        this.init(pMinecraft, pWidth, pHeight);
        this.name.setValue(s);
    }
}
