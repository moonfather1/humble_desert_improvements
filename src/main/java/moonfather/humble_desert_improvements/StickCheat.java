package moonfather.humble_desert_improvements;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class StickCheat
{
    @SubscribeEvent
    public static void onToss(ItemTossEvent event)
    {
//        if (! event.getEntity().level().isClientSide)
//        {
//            System.out.println("~~~ toss");
//            ((ServerLevel) event.getEntity().level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_TERRACOTTA.defaultBlockState()), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), 5, 0.3, 0.2, 0.3, 0.2);
//            ((ServerLevel) event.getEntity().level()).playSound(null, event.getEntity().blockPosition(), SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5f, 0.3f);
//        }
        if (! event.getEntity().level().isClientSide)
        {
            System.out.println("~~~ toss  " + event.getPlayer().getXRot() + ",   "+event.getPlayer().getViewXRot(0));
            event.getPlayer().setXRot(-0.8f);
        }
    }
}
