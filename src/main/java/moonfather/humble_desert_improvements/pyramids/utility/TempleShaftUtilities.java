package moonfather.humble_desert_improvements.pyramids.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;

public class TempleShaftUtilities
{
    public static int getBlueTerracottaOffset(WorldGenLevel genLevel, BlockPos pos)
    {
        if (genLevel.getBlockState(pos).is(Blocks.BLUE_TERRACOTTA))
        {
            return 0; // ok
        }
        BlockPos.MutableBlockPos mpos1 = new BlockPos.MutableBlockPos();
        mpos1.set(pos);
        for (int i = 1; i <= 12; i++)
        {
            mpos1.move(0, -1, 0);
            if (genLevel.getBlockState(mpos1).is(Blocks.BLUE_TERRACOTTA))
            {
                return -1 * i;
            }
        }
        mpos1.set(pos);
        for (int i = 1; i <= 12; i++)
        {
            mpos1.move(0, 1, 0);
            if (genLevel.getBlockState(mpos1).is(Blocks.BLUE_TERRACOTTA))
            {
                return i;
            }
        }
        return NOT_FOUND; // not found
    }
    public static final int NOT_FOUND = -333;

    //-------------------------

    public static void lose3x3TNT(WorldGenLevel genLevel, BlockPos posBlueTerracotta, boolean removeStonePlate)
    {
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dz = -1; dz <= 1; dz++)
            {
                mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11 - 2, posBlueTerracotta.getZ() + dz);
                genLevel.setBlock(mpos, Blocks.SANDSTONE.defaultBlockState(), 2);
            }
        }
        if (removeStonePlate)
        {
            mpos.set(posBlueTerracotta.getX(), posBlueTerracotta.getY() + -11, posBlueTerracotta.getZ());
            genLevel.setBlock(mpos, Blocks.AIR.defaultBlockState(), 2);
        }
    }

    public static void lose3x3TNT(WorldGenLevel genLevel, BlockPos posBlueTerracotta, int percentageChanceToRemoveStonePlate)
    {
        lose3x3TNT(genLevel, posBlueTerracotta, genLevel.getRandom().nextInt(100) < percentageChanceToRemoveStonePlate);
    }
}
