package moonfather.humble_desert_improvements.pyramids.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class TaskScheduler2
{
    public static void queue(PathfinderMob mob, Consumer<PathfinderMob> action)
    {
        ScheduledEvent2 event = new ScheduledEvent2();
        event.action = action;
        event.mob = mob;
        list.add(event);
    }

    private static final LinkedList<ScheduledEvent2> list = new LinkedList<>();

    private static class ScheduledEvent2
    {
        private PathfinderMob mob;
        private Consumer<PathfinderMob> action;
    }

    /////////////////////////////////////////////
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase.equals(TickEvent.Phase.END) && event.getServer().getTickCount() % 5  == 4)
        {
            Iterator<ScheduledEvent2> i = list.iterator();
            while (i.hasNext())
            {
                ScheduledEvent2 e = i.next();
                if (e.mob.isDeadOrDying())
                {
                    i.remove();
                }
                else
                {
                    e.action.accept(e.mob);
                }
            }
            //System.out.println("~~~ tick " + event.side + "   " + event.phase + "   " + event.getServer().getTickCount());
        }
    }
}
