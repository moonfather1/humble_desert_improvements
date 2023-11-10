package moonfather.humble_desert_improvements.pyramids.our_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SignalEscalator extends SignalPipe
{
    @Override
    protected int getOutputSignal(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState)
    {
        if (blockGetter instanceof Level l)
        {
            return this.getInputSignal(l, blockPos, blockState);
        }
        return 0;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction)
    {
        if (! state.getValue(POWERED))
        {
            return 0;
        }
        else if (! direction.equals(Direction.DOWN)) // upwards
        {
            return 0;
        }
        else
        {
            return this.getOutputSignal(blockGetter, pos, state);
        }
    }

    @Override
    protected void updateNeighborsInFront(Level level, BlockPos blockPos, BlockState blockState)
    {
        Direction input = blockState.getValue(FACING);
        BlockPos blockpos = blockPos.above();
        if (! net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(level, blockPos, level.getBlockState(blockPos), java.util.EnumSet.of(Direction.UP), false).isCanceled())
        {
            level.neighborChanged(blockpos, this, blockPos);
            level.updateNeighborsAtExceptFromFacing(blockpos, this, input);
        }
    }
}
