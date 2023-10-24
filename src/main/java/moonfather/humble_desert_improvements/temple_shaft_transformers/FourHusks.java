package moonfather.humble_desert_improvements.temple_shaft_transformers;

import moonfather.humble_desert_improvements.Constants;
import moonfather.humble_desert_improvements.our_blocks.Repository;
import moonfather.humble_desert_improvements.utility.TaskScheduler;
import moonfather.humble_desert_improvements.vanilla_blocks.MovingBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
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
                chest.getPersistentData().putString(Constants.NBT.BEHAVIOR_MOVE_HANDLER, "husks");
                MovingBlocks.Handlers.put("husks", FourHusks::tryMoveTrappedChest);
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

    ///////////////////////////////////////////////////////////////////////////////////

    private static void tryMoveTrappedChest(Level level, BlockPos chestPos)
    {
        BlockState state = level.getBlockState(chestPos);
        BlockPos center = chestPos.relative(state.getValue(HorizontalDirectionalBlock.FACING), 2);
        BlockState stateCenter = level.getBlockState(center);
        BlockPos.MutableBlockPos mpos2 = new BlockPos.MutableBlockPos();
        boolean playSound = false;
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            int dx = direction.getStepX() * 2;
            int dz = direction.getStepZ() * 2;
            mpos2.set(center.getX() + dx, center.getY(), center.getZ() + dz);
            BlockEntity chest2 = level.getBlockEntity(mpos2);
            BlockState state2 = level.getBlockState(mpos2);
            if (chest2 != null)
            {
                mpos2.move(direction.getClockWise());
                level.setBlockAndUpdate(mpos2, state2);
                BlockEntity chest3 = level.getBlockEntity(mpos2);
                if (chest3 != null)
                {
                    CompoundTag tag2 = chest2.saveWithId();
                    tag2.putInt("x", mpos2.getX());
                    tag2.putInt("z", mpos2.getZ());
                    chest3.load(tag2);
                    chest3.getPersistentData().putString(Constants.NBT.BEHAVIOR, "nothing");
                    mpos2.move(direction.getCounterClockWise()); // chest pos
                    Clearable.tryClear(chest2);
                    if (chest2 instanceof RandomizableContainerBlockEntity rc2)
                    {
                        rc2.setLootTable(null, 0);
                    }
                    level.setBlockAndUpdate(mpos2, Blocks.AIR.defaultBlockState());
                    makeBlockBreakParticles(level, mpos2, 5, 15, 25);
                    playSound = true;
                    mpos2.move(direction.getCounterClockWise()); // piston pos
                    level.setBlockAndUpdate(mpos2, Repository.ANCIENT_PISTON.get().defaultBlockState().setValue(DirectionalBlock.FACING, direction.getClockWise()));
                    mpos2.move(direction.getClockWise()).move(direction).move(Direction.UP).move(Direction.UP); // husk pos, y+2
                    level.setBlockAndUpdate(mpos2, Blocks.SANDSTONE_SLAB.defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP));
                    mpos2.move(Direction.DOWN); // husk pos,  y+1
                    level.setBlockAndUpdate(mpos2, Blocks.AIR.defaultBlockState());
                    mpos2.move(Direction.DOWN).move(Direction.DOWN); // husk pos,  y-1
                    level.setBlockAndUpdate(mpos2, Blocks.SANDSTONE.defaultBlockState());
                    mpos2.move(Direction.UP); // husk pos
                    level.setBlockAndUpdate(mpos2, Blocks.AIR.defaultBlockState());
                    Zombie husk = EntityType.HUSK.spawn((ServerLevel) level, mpos2, MobSpawnType.EVENT);
                    adjustHusk(husk, level.random);
                    makeBlockBreakParticles(level, mpos2, 0, 10);
                }
            }
        }
        if (playSound)
        {
            ((ServerLevel) level).playSound(null, center, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5f, 0.3f);
            TaskScheduler.queue(15, level, center, (l, p) ->
            {
                ((ServerLevel) l).playSound(null, p, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5f, 0.3f);
            });
        }
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
                TaskScheduler.queue(time, level, pos, FourHusks::makeBlockBreakParticlesInner);
            }
        }
    }

    private static void makeBlockBreakParticlesInner(Level level, BlockPos mpos2)
    {
        ((ServerLevel) level).sendParticles(chips, mpos2.getX() + 0.5d, mpos2.getY() + 0.25d, mpos2.getZ() + 0.5d, 5, 0.3, 0.2, 0.3, 0.2);
    }
    private static final BlockParticleOption chips = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_TERRACOTTA.defaultBlockState());



    private static void adjustHusk(Zombie husk, RandomSource random)
    {
        husk.setBaby(false);
        int healthPercentage = 10 + random.nextInt(30);
        husk.setHealth(husk.getMaxHealth() * healthPercentage / 100f);
    }
    private static final int HuskChance_MAKE_OPTIONAL = 50;
    private static final int SusSandInShaftChance_MAKE_OPTIONAL = 10;
}
