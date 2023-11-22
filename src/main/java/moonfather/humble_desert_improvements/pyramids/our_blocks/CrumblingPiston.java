package moonfather.humble_desert_improvements.pyramids.our_blocks;

import moonfather.humble_desert_improvements.pyramids.utility.TaskScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class CrumblingPiston extends DirectionalBlock
{
    protected CrumblingPiston()
    {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.6f).pushReaction(PushReaction.DESTROY));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_60166_)
    {
        return this.defaultBlockState().setValue(FACING, p_60166_.getNearestLookingDirection().getOpposite());
    }

    // only particles below

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid)
    {
        ParticleUtils.spawnParticleBelow(level, pos.above(), level.random, dust);
        if (level instanceof ServerLevel)
        {
            ((ServerLevel) level).sendParticles(chips, pos.getX() + 0.5d, pos.getY() + 0.75d, pos.getZ() + 0.5d, 5, 0.3, 0.2, 0.3, 0.2);
            TaskScheduler.queue(10, level, pos, (l, p) ->
            {
                ((ServerLevel) l).sendParticles(chips, p.getX() + 0.5d, p.getY() + 0.75d, p.getZ() + 0.5d, 5, 0.3, 0.2, 0.3, 0.2);
            });
            TaskScheduler.queue(20, level, pos, (l, p) ->
            {
                ((ServerLevel) l).sendParticles(chips, p.getX() + 0.5d, p.getY() + 0.15d, p.getZ() + 0.5d, 10, 0.3, 0.2, 0.3, 0.2);
            });
            TaskScheduler.queue(15, level, pos, (l, p) ->
            {
                ((ServerLevel) l).sendParticles(dust, p.getX() + 0.5d, p.getY() + 0.95d, p.getZ() + 0.5d, 10, 0.3, 0.2, 0.3, 0.2);
            });
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
    private static final BlockParticleOption dust = new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.WHITE_TERRACOTTA.defaultBlockState());
    private static final BlockParticleOption chips = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_TERRACOTTA.defaultBlockState());



    @Override
    public void animateTick(BlockState state, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextInt(16) == 0) {
            double d0 = (double)blockPos.getX() + randomSource.nextDouble();
            double d1 = (double)blockPos.getY() - 0.05D;
            double d2 = (double)blockPos.getZ() + randomSource.nextDouble();
            level.addParticle(dust, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
}
