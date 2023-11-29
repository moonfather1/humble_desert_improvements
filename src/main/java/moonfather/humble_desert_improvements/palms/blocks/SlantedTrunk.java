package moonfather.humble_desert_improvements.palms.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SimpleTrunk extends RotatedPillarBlock
{
    public SimpleTrunk()
    {
        super(BlockBehaviour.Properties.of()
                       .mapColor((state) ->
                       {
                           return state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.TERRACOTTA_WHITE : MapColor.PODZOL;
                       }
        ).instrument(NoteBlockInstrument.BASS).strength(1.5F).sound(SoundType.WOOD).ignitedByLava());
        this.registerDefaultState(this.stateDefinition.any().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y).setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)));
    }

    /////////////////////////////////////////////////

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_)
    {
        return switch (state.getValue(RotatedPillarBlock.AXIS))
        {
            case Y -> VISUAL_SHAPE_Y;
            case X -> VISUAL_SHAPE_X;
            case Z -> VISUAL_SHAPE_Z;
        };
    }
    private static final VoxelShape VISUAL_SHAPE_Y = Shapes.box(3/16d, 0/16d, 3/16d, 13/16d, 16/16d, 13/16d);
    private static final VoxelShape VISUAL_SHAPE_Z = Shapes.box(3/16d, 3/16d, 0/16d, 13/16d, 13/16d, 16/16d);
    private static final VoxelShape VISUAL_SHAPE_X = Shapes.box(0/16d, 3/16d, 3/16d, 16/16d, 13/16d, 13/16d);

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_)
    {
        return switch (state.getValue(RotatedPillarBlock.AXIS))
                {
                    case Y -> COLLI_SHAPE_Y;
                    case X -> COLLI_SHAPE_X;
                    case Z -> COLLI_SHAPE_Z;
                };
    }

    private static final VoxelShape COLLI_SHAPE_Y = Shapes.box(5/16d, 0/16d, 5/16d, 11/16d, 16/16d, 11/16d);
    private static final VoxelShape COLLI_SHAPE_Z = Shapes.box(5/16d, 5/16d, 0/16d, 11/16d, 11/16d, 16/16d);
    private static final VoxelShape COLLI_SHAPE_X = Shapes.box(0/16d, 5/16d, 5/16d, 16/16d, 11/16d, 11/16d);
    /////////////////////////////////////////////////////

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55933_)
    {
        p_55933_.add(AXIS, BlockStateProperties.WATERLOGGED);
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext)
    {
        FluidState fluidstate = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        return this.defaultBlockState().setValue(AXIS, blockPlaceContext.getClickedFace().getAxis()).setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
    }

    public FluidState getFluidState(BlockState p_54377_) {
        return p_54377_.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_54377_);
    }
}
