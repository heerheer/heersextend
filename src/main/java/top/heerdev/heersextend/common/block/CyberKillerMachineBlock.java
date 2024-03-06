package top.heerdev.heersextend.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import top.heerdev.heersextend.HeersExtend;
import top.heerdev.heersextend.common.block.entity.CyberKillerMachineEntity;
import top.heerdev.heersextend.common.gui.CyberKillerMachineContainer;

import javax.annotation.Nullable;

public class CyberKillerMachineBlock extends Block implements EntityBlock {

    public CyberKillerMachineBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(1F)
                .sound(SoundType.METAL));
    }


    // Our block has an associated block entity. This method from EntityBlock is used to create that block entity
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CyberKillerMachineEntity(pos, state);
    }

    // This method is used to create a BlockEntityTicker for our block entity. This ticker can be used to perform certain actions every tick
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        } else {
            return (lvl, pos, st, blockEntity) -> {
                CyberKillerMachineEntity be = (CyberKillerMachineEntity) blockEntity;
                be.tickServer();
            };
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (pLevel.isClientSide()) {
            if (pLevel.getBlockEntity(pPos) instanceof CyberKillerMachineEntity blockEntity) {
                //pPlayer.displayClientMessage(Component.literal("[Energy] " + blockEntity.getPower()), true);
            }
            return InteractionResult.SUCCESS;
        }
        if (pLevel.getBlockEntity(pPos) instanceof CyberKillerMachineEntity blockEntity) {
            if (pPlayer instanceof ServerPlayer serverPlayer) {
                NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider((id, inv, p) -> {
                    return new CyberKillerMachineContainer(id, inv, pPos);
                }, Component.translatable("block.heersextend.cyber_killer.title")), pPos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {

        if (!player.isCreative()) {
            var itemStack = ForgeRegistries.ITEMS.getValue(new ResourceLocation(HeersExtend.MODID, "cyber_killer")).asItem().getDefaultInstance();
            Containers.dropItemStack(level, pos.getX(), pos.getY() - 0.5D, pos.getZ(), itemStack);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pLevel.getBlockEntity(pPos) instanceof CyberKillerMachineEntity blockEntity) {
            blockEntity.drops();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

}