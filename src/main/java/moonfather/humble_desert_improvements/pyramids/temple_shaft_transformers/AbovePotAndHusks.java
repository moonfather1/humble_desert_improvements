package moonfather.humble_desert_improvements.pyramids.temple_shaft_transformers;

import moonfather.humble_desert_improvements.Constants;
import moonfather.humble_desert_improvements.pyramids.utility.TaskScheduler;
import moonfather.humble_desert_improvements.pyramids.utility.TempleShaftUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class AbovePotAndHusks
{
    public static void suspiciousPot(WorldGenLevel genLevel, BlockPos posBlueTerracotta)
    {
        // copy-paste: fix blue terracotta location.
        int fixBlueTerracottaPosition = TempleShaftUtilities.getBlueTerracottaOffset(genLevel, posBlueTerracotta);
        if (fixBlueTerracottaPosition == TempleShaftUtilities.NOT_FOUND) { return; }
        if (fixBlueTerracottaPosition != 0) { posBlueTerracotta = posBlueTerracotta.offset(0, fixBlueTerracottaPosition, 0); }
        //////////////////////////////

        BlockPos above = posBlueTerracotta.above();
        if (genLevel.getBlockState(above).is(Blocks.DECORATED_POT))
        {
            return;
        }
        BlockState potState = Blocks.DECORATED_POT.defaultBlockState().setValue(BlockStateProperties.CRACKED, true);
        genLevel.setBlock(above, potState, 3);
        if (genLevel.getBlockEntity(above) instanceof DecoratedPotBlockEntity pot)
        {
            CompoundTag tag = pot.saveWithFullMetadata();
            ListTag listtag = new ListTag();
            listtag.add(StringTag.valueOf(BuiltInRegistries.ITEM.getKey(Items.MOURNER_POTTERY_SHERD).toString()));
            listtag.add(listtag.get(0));
            listtag.add(listtag.get(0));
            listtag.add(listtag.get(0));
            tag.put(DecoratedPotBlockEntity.TAG_SHERDS, listtag);
            pot.load(tag);
            pot.getPersistentData().putString(Constants.NBT.BEHAVIOR, Constants.NBT.BEHAVIOR_MOVE);
            pot.getPersistentData().putString(Constants.NBT.BEHAVIOR_MOVE_HANDLER, "husks_above_shaft");
            pot.getPersistentData().putString(Constants.NBT.BEHAVIOR_MOVE_CANCELS_BREAKING, Constants.NBT.BOOLEAN_NO);
        }

        int random = genLevel.getRandom().nextInt(100);
        if (random < 50)
        {
            Direction d1;
            do d1 = Direction.getRandom(genLevel.getRandom()); while (d1.getStepY() != 0);
            EscalatorsAndFireballs.setupFireChargeTrapInternal(genLevel, posBlueTerracotta, d1);
            EscalatorsAndFireballs.setupFireChargeTrapInternal(genLevel, posBlueTerracotta, d1.getClockWise());
        }
        else if (random < 80)
        {
            SimplePlateAndTwoTnts.setupSimplestTrapTwoCorners(genLevel, posBlueTerracotta);
        }
        else if (random < 90)
        {
            ArrowsFromBottomLevel.setupEightDispensersOriginal(genLevel, posBlueTerracotta);
        }
    }

    public static void itsATrap(Level level, BlockPos potPos)
    {
        ItemStack loot = level.random.nextBoolean() ? Items.GOLD_INGOT.getDefaultInstance() : Items.SAND.getDefaultInstance();
        level.addFreshEntity(new ItemEntity(level, potPos.getX() + 0.5d, potPos.getY() + 1.1d, potPos.getZ() + 0.5d, loot));
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        for (int dx = -1; dx <= 1; dx += 2)
        {
            for (int dz = -1; dz <= 1; dz += 2)
            {
                mpos.set(potPos);
                mpos.move(4 * dx, 0, 4 * dz);
                makeBlockBreakParticles(level, mpos, 35, 45, 60);
                TaskScheduler.queue(40, level, mpos, AbovePotAndHusks::makeAHusk);
            }
        }
    }

    private static void makeAHusk(Level level, BlockPos pos)
    {
        Zombie husk = EntityType.HUSK.spawn((ServerLevel) level, pos, MobSpawnType.EVENT);
        husk.setBaby(false);
        int healthPercentage = 10 + level.random.nextInt(40); //10-50 -> 30ish%
        husk.setHealth(husk.getMaxHealth() * healthPercentage / 100f);
    }

    private static void makeBlockBreakParticles(Level level, BlockPos pos, int... times)
    {
        for (int time : times)
        {
            if (time == 0)
            {
                makeBlockBreakParticlesInner(level, pos);
            }
            else
            {
                TaskScheduler.queue(time, level, pos, AbovePotAndHusks::makeBlockBreakParticlesInner);
            }
        }
    }

    private static void makeBlockBreakParticlesInner(Level level, BlockPos mpos2)
    {
        ((ServerLevel) level).sendParticles(chips, mpos2.getX() + 0.5d, mpos2.getY() + 0.25d, mpos2.getZ() + 0.5d, 5, 0.3, 0.2, 0.3, 0.2);
    }
    private static final BlockParticleOption chips = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SANDSTONE.defaultBlockState());
}
