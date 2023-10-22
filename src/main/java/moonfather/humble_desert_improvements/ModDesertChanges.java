package moonfather.humble_desert_improvements;

import com.mojang.logging.LogUtils;
import moonfather.humble_desert_improvements.our_blocks.Repository;
import moonfather.humble_desert_improvements.temple_shaft_transformers.FourHusks;
import moonfather.humble_desert_improvements.temple_shaft_transformers.SimplePlateAndTwoTnts;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.function.BiConsumer;


@Mod(Constants.MODID)
public class ModDesertChanges
{
    public static final ArrayList< BiConsumer<WorldGenLevel, BlockPos> > DesertTempleTransformers = new ArrayList<>();
    // make raiser block for trapped chest?
    // make simple plate+2tnt trap
    // sand trap after PD
    // arrow trap is a must
    // fire charge trap is a must
    // block usage list:
    //     movable chest: 1, fake piston 1,
    //     crumbling dispenser: 0, trapped chest: 0,
    //     tripwires: 0, stone pressure plate: 0,
    //     sandy pressure plate: 0, tnt: 0,
    //     pipe: 0, raiser: 0.

    // reconsider loot tables. maybe silk touch for piston



    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public ModDesertChanges()
    {
        Repository.init();

        // Register the commonSetup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        DesertTempleTransformers.add(FourHusks::setupHuskTrap);
        DesertTempleTransformers.add(SimplePlateAndTwoTnts::setupSimplestTrap);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
        }
    }
}
