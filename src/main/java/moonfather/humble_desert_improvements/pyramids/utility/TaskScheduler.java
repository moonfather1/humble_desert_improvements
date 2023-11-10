package moonfather.humble_desert_improvements.pyramids.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.BiConsumer;

@Mod.EventBusSubscriber
public class TaskScheduler
{
    public static void queue(int tickCount, Level level, BlockPos pos, BiConsumer<Level, BlockPos> action)
    {
        ScheduledEvent event = new ScheduledEvent();
        event.action = action;
        event.tickCount = tickCount;
        event.pos = new BlockPos(pos);
        event.level = level;
        list.add(event);
    }

    private static final LinkedList<ScheduledEvent> list = new LinkedList<>();

    private static class ScheduledEvent
    {
        private int tickCount;
        private BiConsumer<Level, BlockPos> action;
        private Level level;
        private BlockPos pos;
    }

    /////////////////////////////////////////////
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase.equals(TickEvent.Phase.END) && event.getServer().getTickCount() % 5  == 3)
        {
            Iterator<ScheduledEvent> i = list.iterator();
            while (i.hasNext())
            {
                ScheduledEvent e = i.next();
                e.tickCount -= 5;
                if (e.tickCount <= 0)
                {
                    e.action.accept(e.level, e.pos);
                    i.remove();
                }
            }
            //System.out.println("~~~ tick " + event.side + "   " + event.phase + "   " + event.getServer().getTickCount());
        }
    }
}
