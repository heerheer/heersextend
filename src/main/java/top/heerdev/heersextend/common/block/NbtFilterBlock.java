package top.heerdev.heersextend.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import top.heerdev.heersextend.common.block.entity.NbtFilterEntity;
import top.heerdev.heersextend.common.gui.NbtFilterContainer;

import java.util.Objects;


public class NbtFilterBlock extends DirectionalBlock implements EntityBlock {

    //public static final DirectionProperty FACING = DirectionalBlock.FACING;


    public NbtFilterBlock() {
        super(BlockBehaviour.Properties.of().strength(1F).noOcclusion()
        );
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
        );
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {

        return this.defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            //pPlayer.openMenu(pState.getMenuProvider(pLevel, pPos));
            if (pPlayer instanceof ServerPlayer serverPlayer) {
                if (pPlayer.isShiftKeyDown()) {
                    var entity = (NbtFilterEntity) pLevel.getBlockEntity(pPos);
                    var facing = Objects.requireNonNull(entity).GetBlockFacing();
                    if (facing != null) {
                        var name = facing.getBlock().getName();
                        pPlayer.displayClientMessage(Component.literal("[Facing]" + name.getString()), true);
                    }

                } else {
                    NetworkHooks.openScreen(serverPlayer, pState.getMenuProvider(pLevel, pPos), pPos);
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.SUCCESS;

    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((pContainerId, inventory, pPlayer) ->
                new NbtFilterContainer(pContainerId, inventory, pPos),
                Component.translatable("block.heersextend.nbt_filter.title"));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new NbtFilterEntity(pPos, pState);
    }


    @Nullable
    @Override
    public <T extends
            BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide) {
            return null;
        } else {
            return (level, pos, state, entity) -> {
                if (entity instanceof NbtFilterEntity filterEntity) {
                    filterEntity.tickServer();
                }
            };
        }

    }
}
