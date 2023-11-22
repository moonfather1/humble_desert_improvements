package moonfather.humble_desert_improvements.pyramids.vanilla_blocks;

import moonfather.humble_desert_improvements.Constants;
import moonfather.humble_desert_improvements.pyramids.temple_shaft_transformers.FourHusks;
import moonfather.humble_desert_improvements.pyramids.utility.TaskScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
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
                    sl.sendParticles(bits, event.getPos().getX() + 0.2, event.getPos().getY() + 0.4, event.getPos().getZ() + 0.2, 8, 0.3, 0.2, 0.3, 0.2);
                    sl.sendParticles(bits, event.getPos().getX() + 0.2, event.getPos().getY() + 0.4, event.getPos().getZ() + 0.8, 8, 0.3, 0.2, 0.3, 0.2);
                    sl.sendParticles(bits, event.getPos().getX() + 0.8, event.getPos().getY() + 0.4, event.getPos().getZ() + 0.2, 8, 0.3, 0.2, 0.3, 0.2);
                    sl.sendParticles(bits, event.getPos().getX() + 0.8, event.getPos().getY() + 0.4, event.getPos().getZ() + 0.8, 8, 0.3, 0.2, 0.3, 0.2);
                    sl.playSound(null, event.getPos(), SoundEvents.BONE_BLOCK_BREAK, SoundSource.BLOCKS, 0.5f, 0.3f);
                    sl.sendParticles(bits, event.getPos().getX() + 0.5, event.getPos().getY() + 1.0, event.getPos().getZ() + 0.5, 10, 0.3, 0.2, 0.3, 0.2);
                    TaskScheduler.queue(20, sl, event.getPos(), CrumblingBlocks::makeBlockBreakParticlesInner);
                    TaskScheduler.queue(30, sl, event.getPos(), CrumblingBlocks::makeBlockBreakParticlesInner);
                }
            }
        }

        BlockEntity chest = event.getLevel().getBlockEntity(event.getPos());
        if (chest != null && chest.getPersistentData().getString(Constants.NBT.BEHAVIOR).equals(Constants.NBT.BEHAVIOR_MOVE))
        {
            if (event.getLevel() instanceof Level level)
            {
                event.setCanceled(true);
                MovingBlocks.runSpecifiedAction(level, chest, event.getPos());
            }
        }
    }

    private static void makeBlockBreakParticlesInner(Level level, BlockPos mpos2)
    {
        ((ServerLevel) level).sendParticles(chips, mpos2.getX() + 0.5d, mpos2.getY() + 0.25d, mpos2.getZ() + 0.5d, 5, 0.3, 0.2, 0.3, 0.2);
    }
    private static final BlockParticleOption bits = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.LIGHT_GRAY_TERRACOTTA.defaultBlockState());
    private static final BlockParticleOption chips = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SANDSTONE.defaultBlockState());
}
