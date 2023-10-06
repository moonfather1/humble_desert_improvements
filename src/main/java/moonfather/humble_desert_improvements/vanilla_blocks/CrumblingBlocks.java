package moonfather.humble_desert_improvements.vanilla_blocks;

import moonfather.humble_desert_improvements.Constants;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CrumblingBlocks
{
    @SubscribeEvent
    public static void onDestroyBlock(LivingDestroyBlockEvent event)
    {
        if (event.getState().is(Blocks.DISPENSER))
        {
            System.out.println("~~~ destroy disp");
            BlockEntity dispenser = event.getEntity().level().getBlockEntity(event.getPos());
            if (dispenser != null && dispenser.getPersistentData().getString(Constants.NBT.BEHAVIOR).equals(Constants.NBT.BEHAVIOR_CRUMBLE))
            {
                System.out.println("~~~ is supposed to crumble");
            }
        }

        //todo: if BEHABIOR_MOVE then MovingBlocks.try
    }
}
