package moonfather.humble_desert_improvements.pyramids.temple_shaft_transformers;

import moonfather.humble_desert_improvements.pyramids.our_blocks.Repository;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;

public class SimplePlateAndTwoTnts
{
    public static void setupSimplestTrap(WorldGenLevel genLevel, BlockPos posBlueTerracotta)
    {
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        // step 1: lose the 3x3 tnt below
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dz = -1; dz <= 1; dz++)
            {
                mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11 - 2, posBlueTerracotta.getZ() + dz);
                genLevel.setBlock(mpos, Blocks.SANDSTONE.defaultBlockState(), 2);
            }
        }
        // step 2: sandstone pressure plate
        int dx = genLevel.getRandom().nextBoolean() ? +1 : -1;
        int dz = genLevel.getRandom().nextBoolean() ? +1 : -1;
        mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11, posBlueTerracotta.getZ() + dz);
        genLevel.setBlock(mpos, Repository.PRESSURE_PLATE.get().defaultBlockState(), 2);
        // step 3: modest amount of tnt
        mpos.set(posBlueTerracotta.getX() + 2*dx, posBlueTerracotta.getY() + -11 - 1, posBlueTerracotta.getZ() + dz);
        genLevel.setBlock(mpos, Blocks.TNT.defaultBlockState(), 2);
        mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11 - 1, posBlueTerracotta.getZ() + 2*dz);
        genLevel.setBlock(mpos, Blocks.TNT.defaultBlockState(), 2);
    }
}
