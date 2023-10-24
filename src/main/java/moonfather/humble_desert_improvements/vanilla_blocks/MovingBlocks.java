package moonfather.humble_desert_improvements.vanilla_blocks;

import moonfather.humble_desert_improvements.Constants;
import moonfather.humble_desert_improvements.our_blocks.Repository;
import moonfather.humble_desert_improvements.utility.TaskScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
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
                    chest.getPersistentData().remove(Constants.NBT.BEHAVIOR);
                    String handlerKey = chest.getPersistentData().getString(Constants.NBT.BEHAVIOR_MOVE_HANDLER); // secondary data: key indicating a method.
                    for (var x: Handlers.entrySet())
                    {
                        if (x.getKey().equals(handlerKey))
                        {
                            x.getValue().accept(event.getEntity().level(), event.getPos()); // we attached a method to a chest.
                            return;
                        }
                    }
                }
            }
        }
    }
    public static final Map<String, BiConsumer<Level, BlockPos>> Handlers = new HashMap<>();
}
