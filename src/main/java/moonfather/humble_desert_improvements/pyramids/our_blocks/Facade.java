package moonfather.humble_desert_improvements.pyramids.our_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;
import java.util.Map;

public class Facade extends DirectionalBlock
{
    protected Facade()
    {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.1f).pushReaction(PushReaction.DESTROY).noCollission());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        this.shapes = new HashMap<>();
        this.shapes.put(Direction.DOWN, Shapes.box(0, 15/16d, 0, 1, 1, 1));
        this.shapes.put(Direction.UP, Shapes.box(0, 0, 0, 1, 1/16d, 1));
        this.shapes.put(Direction.EAST, Shapes.box(0, 0, 0, 2/16d, 1, 1));
        this.shapes.put(Direction.WEST, Shapes.box(14/16d, 0, 0, 1, 1, 1));
        this.shapes.put(Direction.SOUTH, Shapes.box(0, 0, 0, 1, 1, 2/16d));
        this.shapes.put(Direction.NORTH, Shapes.box(0, 0, 14/16d, 1, 1, 1));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_60166_)
    {
        return this.defaultBlockState().setValue(FACING, p_60166_.getNearestLookingDirection().getOpposite());
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_)
    {
        return this.shapes.get(state.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_)
    {
        return Shapes.empty();
    }

    private final Map<Direction, VoxelShape> shapes;
}
