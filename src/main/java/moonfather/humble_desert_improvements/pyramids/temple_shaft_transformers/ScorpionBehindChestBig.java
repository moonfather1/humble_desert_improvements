package moonfather.humble_desert_improvements.pyramids.temple_shaft_transformers;

import moonfather.humble_desert_improvements.Constants;
import moonfather.humble_desert_improvements.pyramids.our_blocks.Repository;
import moonfather.humble_desert_improvements.pyramids.utility.TaskScheduler;
import moonfather.humble_desert_improvements.pyramids.utility.TempleShaftUtilities;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class ScorpionBehindChestBig
{
    public static void setupTrap(WorldGenLevel genLevel, BlockPos posBlueTerracotta)
    {
        // copy-paste: fix blue terracotta location.
        int fixBlueTerracottaPosition = TempleShaftUtilities.getBlueTerracottaOffset(genLevel, posBlueTerracotta);
        if (fixBlueTerracottaPosition == TempleShaftUtilities.NOT_FOUND) { return; }
        if (fixBlueTerracottaPosition != 0) { posBlueTerracotta = posBlueTerracotta.offset(0, fixBlueTerracottaPosition, 0); }
        //////////////////////////////

        // this is called four times for four chunks that the pyramid takes up. while we can just have these run 4x, there are two reasons not to:
        // 1) call dispenserBlockEntity.addItem(arrow); is cumulative. not a big deal but let's not.
        // 2) setupOneDirectionTrap(randomDirection) results in 2-3 dispensers instead of one. solvable by getting direction from x and z. random enough.
        // 3) speed. let's not lag the game if we can easily avoid it.
        if (TempleShaftUtilities.isTNTRemoved(genLevel, posBlueTerracotta)) { return; }
        //////////////////////////////


        // step 1: lose the 3x3 tnt below
        TempleShaftUtilities.lose3x3TNT(genLevel, posBlueTerracotta, 85);
        // call main
        Direction direction;
        do direction = Direction.getRandom(genLevel.getRandom()); while (direction.getStepY() != 0);
        setupTrapInternal(genLevel, posBlueTerracotta, direction);
        // maybe dispenser or two
        if (genLevel.getRandom().nextInt(100) < 30)
        {
            EscalatorsAndFireballs.setupFireChargeTrapInternal(genLevel, posBlueTerracotta, direction.getClockWise());
        }
        if (genLevel.getRandom().nextInt(100) < 30)
        {
            EscalatorsAndFireballs.setupFireChargeTrapInternal(genLevel, posBlueTerracotta, direction.getCounterClockWise());
        }
    }



    public static void setupTrapInternal(WorldGenLevel genLevel, BlockPos posBlueTerracotta, Direction direction)
    {
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        // step 2: scripted chest
        int dx = direction.getStepX() * 2;
        int dz = direction.getStepZ() * 2;
        mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11, posBlueTerracotta.getZ() + dz);
        BlockEntity chest = genLevel.getBlockEntity(mpos);
        if (chest != null)
        {
            chest.getPersistentData().putString(Constants.NBT.BEHAVIOR, Constants.NBT.BEHAVIOR_MOVE);
            chest.getPersistentData().putString(Constants.NBT.BEHAVIOR_MOVE_HANDLER, "big_frelling_scorpion");
            chest.getPersistentData().putString(Constants.NBT.BEHAVIOR_MOVE_CANCELS_BREAKING, Constants.NBT.BOOLEAN_NO);
        }
    }

    public static void tryMakeBigHorizontalShaft(Level level, BlockPos chestPos)
    {
        Player player  = level.getNearestPlayer(chestPos.getX(), chestPos.getY(), chestPos.getZ(), 5d, true);
        if (player == null || ! ModList.get().isLoaded("scorpions"))
        {
            return;
        }
        BlockState state = level.getBlockState(chestPos);
        Direction outWard = state.getValue(HorizontalDirectionalBlock.FACING).getOpposite();
        //BlockPos center = chestPos.relative(state.getValue(HorizontalDirectionalBlock.FACING), 2);
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        BlockState current;
        mpos.set(chestPos).move(Direction.UP, 3).move(outWard); // shaft ceiling
        mpos.move(outWard.getCounterClockWise());
        for (int k = 1; k <= 3; k++)
        {
            for (int i = 0; i < 6; i++)
            {
                current = level.getBlockState(mpos);
                if (current.is(Tags.Blocks.SAND) || current.is(Tags.Blocks.GRAVEL))
                {
                    level.setBlockAndUpdate(mpos, Blocks.SANDSTONE.defaultBlockState());
                }
                mpos.move(outWard);
            }
            mpos.move(outWard.getOpposite(), 6);
            mpos.move(outWard.getClockWise());
        }
        mpos.set(chestPos).move(Direction.UP, 2).move(outWard); // level3
        mpos.move(outWard.getCounterClockWise());
        for (int v = 1; v <= 3; v++) // vertical 3 to 1
        {
            for (int k = 1; k <= 3; k++) // left to right
            {
                for (int i = 0; i < 6; i++) // outward
                {
                    level.setBlockAndUpdate(mpos, Blocks.AIR.defaultBlockState());
                    mpos.move(outWard);
                }
                mpos.move(outWard.getOpposite(), 6);
                mpos.move(outWard.getClockWise());
            }
            mpos.move(outWard.getCounterClockWise(), 3);
            mpos.move(Direction.DOWN);
        }
        mpos.set(chestPos).move(Direction.DOWN).move(outWard); // shaft floor
        mpos.move(outWard.getCounterClockWise());
        for (int k = 1; k <= 3; k++)
        {
            for (int i = 0; i < 6; i++)
            {
                current = level.getBlockState(mpos);
                if (current.isAir())
                {
                    level.setBlockAndUpdate(mpos, Blocks.SANDSTONE.defaultBlockState());
                }
                mpos.move(outWard);
            }
            mpos.move(outWard.getOpposite(), 6);
            mpos.move(outWard.getClockWise());
        }

        BlockState sand = Blocks.SAND.defaultBlockState();
        mpos.set(chestPos).move(outWard.getCounterClockWise()); // front wall left
        FallingBlockEntity.fall(level, mpos, sand).disableDrop();;
        TaskScheduler.queue(40, level, mpos, ScorpionBehindChestBig::makeBlockBreakParticlesInner);
        level.setBlockAndUpdate(mpos, Blocks.AIR.defaultBlockState());
        mpos.move(Direction.UP);
        FallingBlockEntity.fall(level, mpos, sand).disableDrop();
        level.setBlockAndUpdate(mpos, Blocks.AIR.defaultBlockState());
        mpos.move(Direction.UP);
        BlockState falling = level.getBlockState(mpos);
        FallingBlockEntity.fall(level, mpos, falling).disableDrop();
        level.setBlockAndUpdate(mpos, Blocks.AIR.defaultBlockState());
        TaskScheduler.queue(10, level, mpos, ScorpionBehindChestBig::makeBlockBreakParticlesInner);

        mpos.set(chestPos).move(outWard.getClockWise()); // front wall right
        FallingBlockEntity.fall(level, mpos, sand).disableDrop();;
        TaskScheduler.queue(40, level, mpos, ScorpionBehindChestBig::makeBlockBreakParticlesInner);
        level.setBlockAndUpdate(mpos, Blocks.AIR.defaultBlockState());
        mpos.move(Direction.UP);
        FallingBlockEntity.fall(level, mpos, sand).disableDrop();;
        level.setBlockAndUpdate(mpos, Blocks.AIR.defaultBlockState());
        mpos.move(Direction.UP);
        falling = level.getBlockState(mpos);
        FallingBlockEntity.fall(level, mpos, falling).disableDrop();
        level.setBlockAndUpdate(mpos, Blocks.AIR.defaultBlockState());
        TaskScheduler.queue(10, level, mpos, ScorpionBehindChestBig::makeBlockBreakParticlesInner);

        mpos.set(chestPos); // front wall mid
        mpos.move(Direction.UP);
        level.setBlockAndUpdate(mpos, Blocks.AIR.defaultBlockState());
        mpos.move(Direction.UP);
        falling = level.getBlockState(mpos);
        level.setBlockAndUpdate(mpos, Blocks.AIR.defaultBlockState());
        FallingBlockEntity.fall(level, mpos, falling).disableDrop();
        TaskScheduler.queue(10, level, mpos, ScorpionBehindChestBig::makeBlockBreakParticlesInner);

        BlockState state1 = level.getBlockState(chestPos);  // move the chest down
        if (state1.is(Tags.Blocks.CHESTS))
        {
            BlockEntity chest1 = level.getBlockEntity(chestPos);
            mpos.set(chestPos).move(Direction.DOWN);
            level.setBlockAndUpdate(mpos, state1);
            BlockEntity chest2 = level.getBlockEntity(mpos);
            if (chest2 != null)
            {
                CompoundTag tag2 = chest1.saveWithId();
                tag2.putInt("x", mpos.getX());
                tag2.putInt("z", mpos.getZ());
                chest2.load(tag2);
                chest2.getPersistentData().putString(Constants.NBT.BEHAVIOR, "nothing");
                Clearable.tryClear(chest1);
                if (chest1 instanceof RandomizableContainerBlockEntity rc2)
                {
                    rc2.setLootTable(null, 0);
                }
                level.setBlockAndUpdate(chestPos, Blocks.AIR.defaultBlockState());
                TaskScheduler.queue(5, level, chestPos, ScorpionBehindChestBig::makeBlockBreakParticlesInner);
                TaskScheduler.queue(25, level, chestPos, ScorpionBehindChestBig::makeBlockBreakParticlesInner);
            }
        }

        ((ServerLevel) level).playSound(null, chestPos, SoundEvents.DRIPSTONE_BLOCK_BREAK, SoundSource.BLOCKS, 0.5f, 0.3f);
        mpos.set(chestPos);
        mpos.move(outWard, 4);
        TaskScheduler.queue(5, level, mpos, ScorpionBehindChestBig::makeBlockBreakParticlesInner);
        TaskScheduler.queue(25, level, mpos, ScorpionBehindChestBig::makeBlockBreakParticlesInner);
        ((ServerPlayer) player).lookAt(EntityAnchorArgument.Anchor.EYES, mpos.getCenter());

        Entity scorpion = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation("scorpions:emperor_scorpion")).spawn((ServerLevel) level, mpos, MobSpawnType.EVENT);
        PathfinderMob scorpionMob = (PathfinderMob) scorpion;
        scorpionMob.goalSelector.removeAllGoals(goal -> true);
        scorpionMob.goalSelector.addGoal(3, new FloatGoal(scorpionMob));
        scorpionMob.goalSelector.addGoal(4, new ScorpionBehindChestTiny.LightIndependantSpiderAttackGoal(scorpionMob));
        scorpionMob.targetSelector.removeAllGoals(goal -> true);
        scorpionMob.targetSelector.addGoal(1, new HurtByTargetGoal(scorpionMob));
        scorpionMob.targetSelector.addGoal(2, new ScorpionBehindChestTiny.LightIndependantSpiderTargetGoal<Player>(scorpionMob, Player.class));
        scorpionMob.setHealth(20);
        TaskScheduler.queue(45, level, mpos, (l, p) -> scorpion.hurt(scorpion.damageSources().playerAttack(player), 3)); // looks ugly but works
    }

    /////////////////////////

    private static void makeBlockBreakParticlesInner(Level level, BlockPos mpos2)
    {
        ((ServerLevel) level).sendParticles(chips, mpos2.getX() + 0.5d, mpos2.getY() + 0.25d, mpos2.getZ() + 0.5d, 5, 0.3, 0.2, 0.3, 0.2);
    }
    private static final BlockParticleOption chips = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SANDSTONE.defaultBlockState());
}
