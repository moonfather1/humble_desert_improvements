package moonfather.humble_desert_improvements.our_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SignalTransferBlock extends RepeaterBlock  // turn pipe to diode, make item
{
    public SignalTransferBlock()
    {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).forceSolidOn().requiresCorrectToolForDrops().strength(0.3F).pushReaction(PushReaction.NORMAL));
    }



    @Override
    public InteractionResult use(BlockState p_55809_, Level p_55810_, BlockPos p_55811_, Player p_55812_, InteractionHand p_55813_, BlockHitResult p_55814_)
    {
        return InteractionResult.PASS;
    }

    @Override
    protected int getDelay(BlockState p_55830_)
    {
        return 2;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext)
    {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState updateShape(BlockState p_55821_, Direction p_55822_, BlockState p_55823_, LevelAccessor p_55824_, BlockPos p_55825_, BlockPos p_55826_)
    {
        return p_55821_;
    }

    @Override
    public boolean isLocked(LevelReader p_55805_, BlockPos p_55806_, BlockState p_55807_)
    {
        return false;
    }

    @Override
    public void animateTick(BlockState p_221964_, Level p_221965_, BlockPos p_221966_, RandomSource p_221967_)
    {
    }

    @Override
    public VoxelShape getShape(BlockState p_52556_, BlockGetter p_52557_, BlockPos p_52558_, CollisionContext p_52559_)
    {
        return Shapes.block();
    }

    //////////////////////////////////////////////////////

//    @Override
//    protected void updateNeighborsInFront(Level level, BlockPos blockPos, BlockState blockState) {
//        Direction direction = blockState.getValue(FACING);
//        BlockPos blockpos = blockPos.relative(direction.getOpposite());
//        if (! net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(level, blockPos, level.getBlockState(blockPos), java.util.EnumSet.of(direction.getOpposite()), false).isCanceled())
//        {
//            level.neighborChanged(blockpos, this, blockPos);
//            level.updateNeighborsAtExceptFromFacing(blockpos, this, direction);
//        }
//    }

    @Override
    public boolean shouldPrioritize(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        BlockState blockstate1 = blockGetter.getBlockState(blockPos.relative(blockState.getValue(FACING)));
        return blockstate1.getBlock() instanceof SignalTransferBlock;
    }

    @Override
    public boolean canSurvive(BlockState p_52538_, LevelReader p_52539_, BlockPos p_52540_) {
        return true;
    }

//    @Override
//    public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction) {
//        if (! state.getValue(POWERED)) {
//            return 0;
//        } else {
//            return (state.getValue(FACING) == direction) ? this.getOutputSignal(blockGetter, pos, state) : 0;
//        }
//    }

    @Override
    protected int getOutputSignal(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        if (blockGetter instanceof Level l)
        {
            int input = this.getInputSignal(l, blockPos, blockState);
            return Math.max(input - 1, 0);
        }
        return 0;
    }
}
