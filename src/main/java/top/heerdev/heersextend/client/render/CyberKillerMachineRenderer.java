package top.heerdev.heersextend.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.heerdev.heersextend.common.block.entity.CyberKillerMachineEntity;

public class CyberKillerMachineRenderer implements BlockEntityRenderer<CyberKillerMachineEntity> {


    public CyberKillerMachineRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CyberKillerMachineEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

            ItemStack stack = be.getInputItems().getStackInSlot(0);
            if (!stack.isEmpty()) {
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                long millis = System.currentTimeMillis();

                poseStack.pushPose();
                poseStack.pushPose();
                poseStack.scale(.5f, .5f, .5f);
                poseStack.translate(1f, 2.8f, 1f);
                float angle = ((millis / 45) % 360);
                poseStack.mulPose(Axis.YP.rotationDegrees(angle));
                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT, combinedOverlay, poseStack, bufferSource, Minecraft.getInstance().level, 0);
                poseStack.popPose();

                poseStack.translate(0, 0.5f, 0);
                poseStack.popPose();
            }

    }
}