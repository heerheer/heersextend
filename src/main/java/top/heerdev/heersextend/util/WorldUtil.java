package top.heerdev.heersextend.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class WorldUtil {
    public static BlockPos GetRelativePos(BlockPos pos, Direction dir){
        return new BlockPos(pos.getX() + dir.getStepX(), pos.getY() + dir.getStepY(), pos.getZ() + dir.getStepZ());

    }
}
