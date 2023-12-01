package moonfather.humble_desert_improvements.pyramids.vanilla_blocks;

import moonfather.humble_desert_improvements.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Mod.EventBusSubscriber
public class MovingBlocks
{
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        if (! event.getEntity().level().isClientSide())
        {
            BlockEntity chest = event.getEntity().level().getBlockEntity(event.getPos());
            if (chest != null)
            {
                if (chest.getPersistentData().getString(Constants.NBT.BEHAVIOR).equals(Constants.NBT.BEHAVIOR_MOVE))
                {
                    event.setUseBlock(Event.Result.DENY);
                    event.setUseItem(Event.Result.DENY);
                    runSpecifiedAction(event.getEntity().level(), chest, event.getPos());
                }
            }
        }
    }

    public static void runSpecifiedAction(Level level, BlockEntity chest, BlockPos pos)
    {
        String handlerKey = chest.getPersistentData().getString(Constants.NBT.BEHAVIOR_MOVE_HANDLER); // secondary data: key indicating a method.
        chest.getPersistentData().remove(Constants.NBT.BEHAVIOR);
        chest.getPersistentData().remove(Constants.NBT.BEHAVIOR_MOVE_HANDLER);
        for (var x: Handlers.entrySet())
        {
            if (x.getKey().equals(handlerKey))
            {
                x.getValue().accept(level, pos); // we attached a method to a chest.
                return;
            }
        }
    }

    public static final Map<String, BiConsumer<Level, BlockPos>> Handlers = new HashMap<>();
}
