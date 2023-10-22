package moonfather.humble_desert_improvements.our_blocks;

import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class PressurePlate extends PressurePlateBlock
{
    public PressurePlate()
    {
        super(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.of().mapColor(MapColor.SAND).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().strength(0.3F).pushReaction(PushReaction.DESTROY), BlockSetType.STONE);
    }

    protected int getSignalForState(BlockState state)
    {
        return state.getValue(POWERED) ? 5 : 0;
    }
}
