package moonfather.humble_desert_improvements.pyramids.our_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;
import java.util.Map;

public class Vent extends HorizontalDirectionalBlock
{
    protected Vent()
    {
        super(Properties.of().mapColor(MapColor.SAND).strength(0.6f).pushReaction(PushReaction.NORMAL).requiresCorrectToolForDrops());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

        VoxelShape shape1 = Shapes.or(Shapes.box(0, 0/16d, 0, 1, 4/16d, 1),
                                    Shapes.box(0, 4/16d, 0, 4/16d, 12/16d, 1),
                                    Shapes.box(12/16d, 4/16d, 0, 1, 12/16d, 1),
                                    Shapes.box(0, 12/16d, 0, 1, 16/16d, 1));
        VoxelShape shape2 = Shapes.or(Shapes.box(0, 0/16d, 0, 1, 4/16d, 1),
                Shapes.box(0, 4/16d, 0, 1, 12/16d, 4/16d),
                Shapes.box(0, 4/16d, 12/16d, 1, 12/16d, 1),
                Shapes.box(0, 12/16d, 0, 1, 16/16d, 1));
        this.shapes = new HashMap<>();
        this.shapes.put(Direction.EAST, shape2);
        this.shapes.put(Direction.WEST, shape2);
        this.shapes.put(Direction.SOUTH, shape1);
        this.shapes.put(Direction.NORTH, shape1);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext)
    {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_)
    {
        return this.shapes.get(state.getValue(FACING));
    }

    private final Map<Direction, VoxelShape> shapes;
}
