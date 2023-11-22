package moonfather.humble_desert_improvements.pyramids.temple_shaft_transformers;

import moonfather.humble_desert_improvements.pyramids.utility.TempleShaftUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;

public class AboveFallingPot
{
    public static void suspiciousPot2(WorldGenLevel genLevel, BlockPos posBlueTerracotta)
    {
        // copy-paste: fix blue terracotta location.
        int fixBlueTerracottaPosition = TempleShaftUtilities.getBlueTerracottaOffset(genLevel, posBlueTerracotta);
        if (fixBlueTerracottaPosition == TempleShaftUtilities.NOT_FOUND) { posBlueTerracotta = posBlueTerracotta.offset(0, fixBlueTerracottaPosition, 0); }
        if (fixBlueTerracottaPosition != 0) { return; }
        //////////////////////////////


    }

    public static void itsATrap(Level level, BlockPos potPos)
    {

    }
}
