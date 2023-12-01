package moonfather.humble_desert_improvements.pyramids.temple_shaft_transformers;

import moonfather.humble_desert_improvements.Constants;
import moonfather.humble_desert_improvements.pyramids.utility.TaskScheduler;
import moonfather.humble_desert_improvements.pyramids.utility.TempleShaftUtilities;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class ScorpionBehindChestTiny
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
        // maybe dispenser on opposite side
        if (genLevel.getRandom().nextInt(100) < 60)
        {
            EscalatorsAndFireballs.setupFireChargeTrapInternal(genLevel, posBlueTerracotta, direction.getClockWise());
        }
        // maybe one more scorpion
        if (genLevel.getRandom().nextInt(100) < 50)
        {
            setupTrapInternal(genLevel, posBlueTerracotta, direction.getOpposite());
            if (genLevel.getRandom().nextInt(100) < 30)
            {
                EscalatorsAndFireballs.setupFireChargeTrapInternal(genLevel, posBlueTerracotta, direction.getCounterClockWise());
            }
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
            chest.getPersistentData().putString(Constants.NBT.BEHAVIOR_MOVE_HANDLER, "scorpions_in_front");
            chest.getPersistentData().putString(Constants.NBT.BEHAVIOR_MOVE_CANCELS_BREAKING, Constants.NBT.BOOLEAN_YES);
        }
    }

    public static void tryMakeHorizontalShaft(Level level, BlockPos chestPos)
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
        mpos.set(chestPos);
        for (int i = 0; i < 4; i++)
        {
            mpos.move(outWard);
            level.setBlockAndUpdate(mpos, Blocks.SANDSTONE.defaultBlockState()); // below shaft
        }
        mpos.move(Direction.UP);
        for (int i = 0; i < 4; i++)
        {
            level.setBlockAndUpdate(mpos, Blocks.AIR.defaultBlockState()); // shaft
            mpos.move(outWard.getOpposite());
        }
        mpos.move(Direction.UP);
        for (int i = 0; i < 4; i++)
        {
            mpos.move(outWard);
            level.setBlockAndUpdate(mpos, Blocks.SANDSTONE.defaultBlockState()); // above
        }


        ((ServerLevel) level).playSound(null, chestPos, SoundEvents.DRIPSTONE_BLOCK_BREAK, SoundSource.BLOCKS, 0.5f, 0.3f);
        mpos.set(chestPos);
        mpos.move(Direction.UP);
        TaskScheduler.queue(5, level, mpos, ScorpionBehindChestTiny::makeBlockBreakParticlesInner);
        TaskScheduler.queue(25, level, mpos, ScorpionBehindChestTiny::makeBlockBreakParticlesInner);
        mpos.move(outWard);
        TaskScheduler.queue(15, level, mpos, ScorpionBehindChestTiny::makeBlockBreakParticlesInner);
        ((ServerPlayer) player).lookAt(EntityAnchorArgument.Anchor.EYES, mpos.getCenter());
        mpos.move(outWard, 3);
        TaskScheduler.queue(15, level, mpos, ScorpionBehindChestTiny::makeBlockBreakParticlesInner);
        TaskScheduler.queue(35, level, mpos, ScorpionBehindChestTiny::makeBlockBreakParticlesInner);
        mpos.move(Direction.UP, 2);
        level.setBlockAndUpdate(mpos, Blocks.SANDSTONE.defaultBlockState());
        mpos.move(Direction.DOWN, 1);
        level.setBlockAndUpdate(mpos, Blocks.AIR.defaultBlockState());
        Entity scorpion = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation("scorpions:brown_scorpion")).spawn((ServerLevel) level, mpos, MobSpawnType.EVENT);
        PathfinderMob scorpionMob = (PathfinderMob) scorpion;
        scorpionMob.goalSelector.removeAllGoals(goal -> true);
        scorpionMob.goalSelector.addGoal(3, new FloatGoal(scorpionMob));
        scorpionMob.goalSelector.addGoal(4, new LightIndependantSpiderAttackGoal(scorpionMob));
        scorpionMob.targetSelector.removeAllGoals(goal -> true);
        scorpionMob.targetSelector.addGoal(1, new HurtByTargetGoal(scorpionMob));
        scorpionMob.targetSelector.addGoal(2, new LightIndependantSpiderTargetGoal<Player>(scorpionMob, Player.class));
        scorpionMob.setHealth(12);
        scorpionMob.setBaby(true); // want it smaller. no better idea.
        TaskScheduler.queue(45, level, mpos, (l, p) -> scorpion.hurt(scorpion.damageSources().playerAttack(player), 3));
    }

    /////////////////////////

    private static void makeBlockBreakParticlesInner(Level level, BlockPos mpos2)
    {
        ((ServerLevel) level).sendParticles(chips, mpos2.getX() + 0.5d, mpos2.getY() + 0.25d, mpos2.getZ() + 0.5d, 5, 0.3, 0.2, 0.3, 0.2);
    }
    private static final BlockParticleOption chips = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SANDSTONE.defaultBlockState());

    ///////////////////////////////


    static class LightIndependantSpiderAttackGoal extends MeleeAttackGoal
    {
        public LightIndependantSpiderAttackGoal(PathfinderMob p_33822_) {
            super(p_33822_, 1.0D, true);
        }

        public boolean canUse() {
            return super.canUse() && ! this.mob.isVehicle();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        protected double getAttackReachSqr(LivingEntity p_33825_) {
            return (double)(3.0F + p_33825_.getBbWidth());
        }
    }

    static class LightIndependantSpiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T>
    {
        public LightIndependantSpiderTargetGoal(PathfinderMob p_33832_, Class<T> p_33833_) { super(p_33832_, p_33833_, true); }

        public boolean canUse() {
            return super.canUse();
        }

        public void start() {
            super.start();
        }
    }
}
