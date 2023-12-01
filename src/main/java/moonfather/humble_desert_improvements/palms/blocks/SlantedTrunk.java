package moonfather.humble_desert_improvements.palms.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SlantedTrunk extends Block
{
    public SlantedTrunk()
    {
        super(Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).strength(1.5F).sound(SoundType.WOOD).ignitedByLava());
        this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST).setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)).setValue(LEVEL, 1));
    }

    public static final BooleanProperty BEING_ASSEMBLED = BooleanProperty.create("in_progress");
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, 3);
    /////////////////////////////////////////////////

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_)
    {
        return switch (state.getValue(SlantedTrunk.LEVEL))
        {
            case 1 -> switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING))
                    {
                        case WEST -> Shapes.box(-1/16d, 0/16d, 3/16d, 13/16d, 16/16d, 13/16d);
                        case NORTH -> Shapes.box(3/16d, 0/16d, 0/16d, 13/16d, 16/16d, 13/16d);
                        case EAST -> Shapes.box(3/16d, 0/16d, 3/16d, 16/16d, 16/16d, 13/16d);
                        case SOUTH -> Shapes.box(3/16d, 0/16d, 3/16d, 13/16d, 16/16d, 16/16d);
                        default -> throw new IllegalStateException("Unexpected value: " + state.getValue(BlockStateProperties.HORIZONTAL_FACING));
                    };
            case 2 -> switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING))
                    {
                        case WEST -> Shapes.box(-8/16d, 0/16d, 3/16d, 13/16d, 16/16d, 13/16d);
                        case NORTH -> Shapes.box(3/16d, 0/16d, 0/16d, 13/16d, 16/16d, 13/16d);
                        case EAST -> Shapes.box(3/16d, 0/16d, 3/16d, 16/16d, 16/16d, 13/16d);
                        case SOUTH -> Shapes.box(3/16d, 0/16d, 3/16d, 13/16d, 16/16d, 16/16d);
                        default -> throw new IllegalStateException("Unexpected value: " + state.getValue(BlockStateProperties.HORIZONTAL_FACING));
                    };
            case 3 -> switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING))
                    {
                        case WEST -> Shapes.box(-13/16d, 0/16d, 3/16d, 9/16d, 16/16d, 13/16d);
                        case NORTH -> Shapes.box(3/16d, 0/16d, 0/16d, 13/16d, 16/16d, 13/16d);
                        case EAST -> Shapes.box(3/16d, 0/16d, 3/16d, 16/16d, 16/16d, 13/16d);
                        case SOUTH -> Shapes.box(3/16d, 0/16d, 3/16d, 13/16d, 16/16d, 16/16d);
                        default -> throw new IllegalStateException("Unexpected value: " + state.getValue(BlockStateProperties.HORIZONTAL_FACING));
                    };
            default -> throw new IllegalStateException("Unexpected value: 5");
        };
    }
    private static final VoxelShape VISUAL_SHAPE_NORTH_1 = Shapes.box(3/16d, 0/16d, 0/16d, 13/16d, 16/16d, 13/16d); //nz
    private static final VoxelShape VISUAL_SHAPE_WEST_1 = Shapes.box(0/16d, 0/16d, 3/16d, 13/16d, 16/16d, 13/16d); //nx
    private static final VoxelShape VISUAL_SHAPE_SOUTH_1 = Shapes.box(3/16d, 0/16d, 3/16d, 13/16d, 16/16d, 16/16d); //pz
    private static final VoxelShape VISUAL_SHAPE_EAST_1 = Shapes.box(3/16d, 0/16d, 3/16d, 16/16d, 16/16d, 13/16d); //px
    private static final VoxelShape VISUAL_SHAPE_NORTH_2 = Shapes.box(3/16d, 0/16d, 0/16d, 13/16d, 16/16d, 13/16d); //nz
    private static final VoxelShape VISUAL_SHAPE_WEST_2 = Shapes.box(0/16d, 0/16d, 3/16d, 13/16d, 16/16d, 13/16d); //nx
    private static final VoxelShape VISUAL_SHAPE_SOUTH_2 = Shapes.box(3/16d, 0/16d, 3/16d, 13/16d, 16/16d, 16/16d); //pz
    private static final VoxelShape VISUAL_SHAPE_EAST_2 = Shapes.box(3/16d, 0/16d, 3/16d, 16/16d, 16/16d, 13/16d); //px
    private static final VoxelShape VISUAL_SHAPE_NORTH_3 = Shapes.box(3/16d, 0/16d, 0/16d, 13/16d, 16/16d, 13/16d); //nz
    private static final VoxelShape VISUAL_SHAPE_WEST_3 = Shapes.box(0/16d, 0/16d, 3/16d, 13/16d, 16/16d, 13/16d); //nx
    private static final VoxelShape VISUAL_SHAPE_SOUTH_3 = Shapes.box(3/16d, 0/16d, 3/16d, 13/16d, 16/16d, 16/16d); //pz
    private static final VoxelShape VISUAL_SHAPE_EAST_3 = Shapes.box(3/16d, 0/16d, 3/16d, 16/16d, 16/16d, 13/16d); //px

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_)
    {
        return switch (state.getValue(SlantedTrunk.LEVEL))
                {
                    case 1 -> switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING))
                            {
                                case WEST -> Shapes.box(-1/16d, 0/16d, 3/16d, 13/16d, 16/16d, 13/16d);
                                case NORTH -> Shapes.box(3/16d, 0/16d, 0/16d, 13/16d, 16/16d, 13/16d);
                                case EAST -> Shapes.box(3/16d, 0/16d, 3/16d, 16/16d, 16/16d, 13/16d);
                                case SOUTH -> Shapes.box(3/16d, 0/16d, 3/16d, 13/16d, 16/16d, 16/16d);
                                default -> throw new IllegalStateException("Unexpected value: " + state.getValue(BlockStateProperties.HORIZONTAL_FACING));
                            };
                    case 2 -> switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING))
                            {
                                case WEST -> Shapes.box(-8/16d, 0/16d, 3/16d, 13/16d, 16/16d, 13/16d);
                                case NORTH -> Shapes.box(3/16d, 0/16d, 0/16d, 13/16d, 16/16d, 13/16d);
                                case EAST -> Shapes.box(3/16d, 0/16d, 3/16d, 16/16d, 16/16d, 13/16d);
                                case SOUTH -> Shapes.box(3/16d, 0/16d, 3/16d, 13/16d, 16/16d, 16/16d);
                                default -> throw new IllegalStateException("Unexpected value: " + state.getValue(BlockStateProperties.HORIZONTAL_FACING));
                            };
                    case 3 -> switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING))
                            {
                                case WEST -> Shapes.box(-13/16d, 0/16d, 3/16d, 9/16d, 16/16d, 13/16d);
                                case NORTH -> Shapes.box(3/16d, 0/16d, 0/16d, 13/16d, 16/16d, 13/16d);
                                case EAST -> Shapes.box(3/16d, 0/16d, 3/16d, 16/16d, 16/16d, 13/16d);
                                case SOUTH -> Shapes.box(3/16d, 0/16d, 3/16d, 13/16d, 16/16d, 16/16d);
                                default -> throw new IllegalStateException("Unexpected value: " + state.getValue(BlockStateProperties.HORIZONTAL_FACING));
                            };
                    default -> throw new IllegalStateException("Unexpected value: 5");
                };
    }

    private static final VoxelShape COLLI_SHAPE_NORTH = Shapes.box(5/16d, 0/16d, 0/16d, 11/16d, 16/16d, 11/16d); //nz
    private static final VoxelShape COLLI_SHAPE_WEST = Shapes.box(0/16d, 0/16d, 5/16d, 11/16d, 16/16d, 11/16d); //nx
    private static final VoxelShape COLLI_SHAPE_SOUTH = Shapes.box(5/16d, 0/16d, 5/16d, 11/16d, 16/16d, 16/16d); //pz
    private static final VoxelShape COLLI_SHAPE_EAST = Shapes.box(5/16d, 0/16d, 5/16d, 16/16d, 16/16d, 11/16d); //px
    /////////////////////////////////////////////////////

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55933_)
    {
        p_55933_.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.WATERLOGGED, BEING_ASSEMBLED, LEVEL);
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext)
    {
        FluidState fluidstate = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        return PalmRepository.LOG_SIMPLE.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, blockPlaceContext.getHorizontalDirection().getOpposite()).setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
    }

    public FluidState getFluidState(BlockState p_54377_) {
        return p_54377_.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_54377_);
    }

    //////////////////////////////////////////////////

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player)
    {
        return PalmRepository.LOG_SIMPLE_ITEM.get().getDefaultInstance();
    }
}
