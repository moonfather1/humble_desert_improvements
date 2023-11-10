package moonfather.humble_desert_improvements.pyramids.vanilla_blocks;

import moonfather.humble_desert_improvements.Constants;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CrumblingBlocks
{
    @SubscribeEvent
    public static void onDestroyBlock(BlockEvent.BreakEvent event)
    {
        if (event.getState().is(Blocks.DISPENSER))
        {
            BlockEntity dispenser = event.getLevel().getBlockEntity(event.getPos());
            if (dispenser != null && dispenser.getPersistentData().getString(Constants.NBT.BEHAVIOR).equals(Constants.NBT.BEHAVIOR_CRUMBLE))
            {
                event.setCanceled(true);
                event.getLevel().setBlock(event.getPos(), Blocks.AIR.defaultBlockState(), 3);
                if (event.getLevel() instanceof ServerLevel sl)
                {
                    Clearable.tryClear(dispenser);
                    sl.addFreshEntity(new ItemEntity(sl, event.getPos().getX() + 0.5, event.getPos().getY() + 0.4, event.getPos().getZ() + 0.5, Blocks.SAND.asItem().getDefaultInstance()));
                    sl.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_TERRACOTTA.defaultBlockState()), event.getPos().getX() + 0.2, event.getPos().getY() + 0.4, event.getPos().getZ() + 0.2, 8, 0.3, 0.2, 0.3, 0.2);
                    sl.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_TERRACOTTA.defaultBlockState()), event.getPos().getX() + 0.2, event.getPos().getY() + 0.4, event.getPos().getZ() + 0.8, 8, 0.3, 0.2, 0.3, 0.2);
                    sl.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_TERRACOTTA.defaultBlockState()), event.getPos().getX() + 0.8, event.getPos().getY() + 0.4, event.getPos().getZ() + 0.2, 8, 0.3, 0.2, 0.3, 0.2);
                    sl.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_TERRACOTTA.defaultBlockState()), event.getPos().getX() + 0.8, event.getPos().getY() + 0.4, event.getPos().getZ() + 0.8, 8, 0.3, 0.2, 0.3, 0.2);
                    sl.playSound(null, event.getPos(), SoundEvents.BONE_BLOCK_BREAK, SoundSource.BLOCKS, 0.5f, 0.3f);
                    sl.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_TERRACOTTA.defaultBlockState()), event.getPos().getX() + 0.5, event.getPos().getY() + 1.0, event.getPos().getZ() + 0.5, 10, 0.3, 0.2, 0.3, 0.2);
                }
            }
        }

        //todo: if BEHABIOR_MOVE then MovingBlocks.try
    }
}
