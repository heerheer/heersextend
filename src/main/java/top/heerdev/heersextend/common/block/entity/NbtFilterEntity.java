package top.heerdev.heersextend.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import top.heerdev.heersextend.common.block.ModBlocks;
import top.heerdev.heersextend.common.block.NbtFilterBlock;
import top.heerdev.heersextend.common.block.entity.util.DatableBlockEntity;
import top.heerdev.heersextend.common.util.ItemHandlerUtil;
import top.heerdev.heersextend.util.WorldUtil;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class NbtFilterEntity extends DatableBlockEntity {

    enum FilterMode {
        EQUAL,
        NOT_EQUAL,
        CONTAINS,
        NOT_CONTAINS

    }

    BlockPos pos;
    private BlockPos facingBlockPos;
    private BlockPos backBlockPos;

    private Direction facing;


    private List<String> filters;
    private FilterMode filterMode;
    private String filterValue;


    public NbtFilterEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.NBT_FILTER_ENTITY.get(), pos, state);
        this.pos = pos;

        facingBlockPos = WorldUtil.GetRelativePos(this.pos, this.getBlockState().getValue(NbtFilterBlock.FACING));
        backBlockPos = WorldUtil.GetRelativePos(this.pos, this.getBlockState().getValue(NbtFilterBlock.FACING).getOpposite());

        facing = this.getBlockState().getValue(NbtFilterBlock.FACING);
    }

    public String FilterString = "";

    @Override
    protected void saveClientData(CompoundTag tag) {
        tag.putString("FilterString", FilterString);
    }

    @Override
    protected void loadClientData(CompoundTag tag) {
        if (tag.contains("FilterString"))
            SetFilterString(tag.getString("FilterString"));
    }

    public void SetFilterString(String filter) {
        FilterString = filter;
        setChanged();
        if (this.level != null)
            this.level.sendBlockUpdated(pos, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);

        //分析
        var splits = this.FilterString.split(" ");
        if (splits.length >= 3) {
            this.filters = Arrays.stream(splits[0].split("\\.")).toList();
            //System.out.println(String.join(">", this.filters));
            this.filterMode = FilterMode.valueOf(splits[1]);
            //System.out.println("Mode:" + this.filterMode);
            this.filterValue = splits[2];
            //System.out.println("Value:" + this.filterValue);

        }
    }


    public void tickServer() {

        //一次tick最多运载多少个物品?
        if (level.getGameTime() % 5 != 0)
            return;

        if (this.FilterString.isEmpty()) {
            return;
        }


        var insertBlcok = level.getBlockEntity(facingBlockPos);
        var extraBlcok = level.getBlockEntity(backBlockPos);

        if (insertBlcok != null && extraBlcok != null) {
            //System.out.println("Two sides are both present");

            var insertCap = insertBlcok.getCapability(ForgeCapabilities.ITEM_HANDLER, facing);
            var extraCap = extraBlcok.getCapability(ForgeCapabilities.ITEM_HANDLER, facing.getOpposite());

            if (!insertCap.isPresent() || !extraCap.isPresent())
                return;


            var insertHandler = insertCap.resolve().get();
            var extraHandler = extraCap.resolve().get();

            for (int i = 0; i < extraHandler.getSlots(); i++) {
                var stack = extraHandler.getStackInSlot(i);


                if (!stack.hasTag())
                    continue;

                if (!match(getTagValueByFilters(stack.getTag()), this.filterMode, this.filterValue))
                    continue;


                var canInsert = ItemHandlerUtil.CanInsert(stack, insertHandler);
                if (!stack.isEmpty() && canInsert.getA()) {
                    stack = extraHandler.extractItem(i, 1, false);
                    insertHandler.insertItem(canInsert.getB(), stack, false);
                    return;
                    //System.out.println("insert: " + stack.getDisplayName().getString());
                }
            }

        }
    }

    public @Nullable BlockState GetBlockFacing() {
        if (level.getBlockEntity(facingBlockPos) != null)
            return level.getBlockEntity(facingBlockPos).getBlockState();
        return null;
    }

    private String getTagValueByFilters(CompoundTag tag) {
        for (int i = 0; i < this.filters.size(); i++) {
            var key = this.filters.get(i);
            if (i == this.filters.size() - 1) {
                if (tag.contains(key)) {
                    return tag.getString(key);
                }
            }
            if (tag.contains(key)) {
                tag = tag.getCompound(key);
            }
        }
        return "";
    }

    private boolean match(String source, FilterMode mode, String target) {
        switch (mode) {
            case EQUAL:
                return source.equals(target);
            case NOT_EQUAL:
                return !source.equals(target);
            case CONTAINS:
                return source.contains(target);
            case NOT_CONTAINS:
                return !source.contains(target);
        }
        return false;
    }
}

