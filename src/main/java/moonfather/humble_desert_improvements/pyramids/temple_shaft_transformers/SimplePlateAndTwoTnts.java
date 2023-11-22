package moonfather.humble_desert_improvements.pyramids.temple_shaft_transformers;

import moonfather.humble_desert_improvements.pyramids.our_blocks.Repository;
import moonfather.humble_desert_improvements.pyramids.utility.TempleShaftUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;

public class SimplePlateAndTwoTnts
{
    public static void setupSimplestTrap(WorldGenLevel genLevel, BlockPos posBlueTerracotta)
    {
        // copy-paste: fix blue terracotta location.
        int fixBlueTerracottaPosition = TempleShaftUtilities.getBlueTerracottaOffset(genLevel, posBlueTerracotta);
        if (fixBlueTerracottaPosition == TempleShaftUtilities.NOT_FOUND) { posBlueTerracotta = posBlueTerracotta.offset(0, fixBlueTerracottaPosition, 0); }
        if (fixBlueTerracottaPosition != 0) { return; }
        //////////////////////////////

        // step 1: lose the 3x3 tnt below
        TempleShaftUtilities.lose3x3TNT(genLevel, posBlueTerracotta, true);
        // one 1x plate, 2x TNT
        int dx = genLevel.getRandom().nextBoolean() ? +1 : -1;
        int dz = genLevel.getRandom().nextBoolean() ? +1 : -1;
        setupSimplestTrapInternal(genLevel, posBlueTerracotta, dx, dz);
    }

    public static void setupSimplestTrapTwoCorners(WorldGenLevel genLevel, BlockPos posBlueTerracotta)
    {
        // copy-paste: fix blue terracotta location.
        int fixBlueTerracottaPosition = TempleShaftUtilities.getBlueTerracottaOffset(genLevel, posBlueTerracotta);
        if (fixBlueTerracottaPosition == TempleShaftUtilities.NOT_FOUND) { posBlueTerracotta = posBlueTerracotta.offset(0, fixBlueTerracottaPosition, 0); }
        if (fixBlueTerracottaPosition != 0) { return; }
        //////////////////////////////

        // step 1: lose the 3x3 tnt below
        TempleShaftUtilities.lose3x3TNT(genLevel, posBlueTerracotta, 90);
        // one 1x plate, 2x TNT
        int dx = genLevel.getRandom().nextBoolean() ? +1 : -1;
        int dz = genLevel.getRandom().nextBoolean() ? +1 : -1;
        setupSimplestTrapInternal(genLevel, posBlueTerracotta, dx, dz);
        dx = -1 * dx;
        dz = -1 * dz;
        setupSimplestTrapInternal(genLevel, posBlueTerracotta, dx, dz);
    }

    private static void setupSimplestTrapInternal(WorldGenLevel genLevel, BlockPos posBlueTerracotta, int dx, int dz)
    {
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        // step 2: sandstone pressure plate
        mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11, posBlueTerracotta.getZ() + dz);
        genLevel.setBlock(mpos, Repository.PRESSURE_PLATE.get().defaultBlockState(), 2);
        // step 3: modest amount of tnt
        mpos.set(posBlueTerracotta.getX() + 2*dx, posBlueTerracotta.getY() + -11 - 1, posBlueTerracotta.getZ() + dz);
        genLevel.setBlock(mpos, Blocks.TNT.defaultBlockState(), 2);
        mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11 - 1, posBlueTerracotta.getZ() + 2*dz);
        genLevel.setBlock(mpos, Blocks.TNT.defaultBlockState(), 2);
    }
}
