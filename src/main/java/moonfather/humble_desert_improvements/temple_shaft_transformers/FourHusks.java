package moonfather.humble_desert_improvements.temple_shaft_transformers;

import moonfather.humble_desert_improvements.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

public class FourHusks
{
    public static void setupHuskTrap(WorldGenLevel genLevel, BlockPos posBlueTerracotta)
    {
        // todo: what if a player just takes the axe and whacks the chest open? do the same thing if it has the "move" marker
        // step 1: upside down stairs next to chests
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int dx = direction.getStepX() * 2;
            int dz = direction.getStepZ() * 2;
            if (dx == 0) // dz is +2 or -2
            {
                mpos.set(posBlueTerracotta.getX() + 1, posBlueTerracotta.getY() + -11 + 1, posBlueTerracotta.getZ() + dz);
                setBlockToStairs(genLevel, mpos, direction);
                mpos.set(posBlueTerracotta.getX() - 1, posBlueTerracotta.getY() + -11 + 1, posBlueTerracotta.getZ() + dz);
                setBlockToStairs(genLevel, mpos, direction);
            }
            else // dx is +2 or -2
            {
                mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11 + 1, posBlueTerracotta.getZ() + +1);
                setBlockToStairs(genLevel, mpos, direction);
                mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11 + 1, posBlueTerracotta.getZ() + -1);
                setBlockToStairs(genLevel, mpos, direction);
            }
        }
        // step 2: add tag to chests
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            int dx = direction.getStepX() * 2;
            int dz = direction.getStepZ() * 2;
            mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11, posBlueTerracotta.getZ() + dz);
            BlockEntity chest = genLevel.getBlockEntity(mpos);
            if (chest != null)
            {
                chest.getPersistentData().putString(Constants.NBT.BEHAVIOR, Constants.NBT.BEHAVIOR_MOVE);
            }
        }
        // step 3: lose the tnt
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dz = -1; dz <= 1; dz++)
            {
                mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11 - 2, posBlueTerracotta.getZ() + dz);
                genLevel.setBlock(mpos, Blocks.SANDSTONE.defaultBlockState(), 2);
            }
        }
        // that's it for generation. there is much more MovingBlocks class, when player opens a chest.
    }
    private static void setBlockToStairs(WorldGenLevel genLevel, BlockPos pos, Direction direction)
    {
        genLevel.setBlock(pos, Blocks.SANDSTONE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, direction).setValue(StairBlock.HALF, Half.TOP).setValue(StairBlock.SHAPE, StairsShape.STRAIGHT).setValue(StairBlock.WATERLOGGED, Boolean.valueOf(false)), 2);
        //genLevel.getChunk(mpos).markPosForPostprocessing(mpos);
    }
}
