package moonfather.humble_desert_improvements;

import com.mojang.logging.LogUtils;
import moonfather.humble_desert_improvements.pyramids.our_blocks.Repository;
import moonfather.humble_desert_improvements.pyramids.temple_shaft_transformers.*;
import moonfather.humble_desert_improvements.pyramids.vanilla_blocks.MovingBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.function.BiConsumer;


@Mod(Constants.MODID)
public class ModDesertChanges
{
    public static final ArrayList< BiConsumer<WorldGenLevel, BlockPos> > DesertTempleTransformers = new ArrayList<>();
    // sand trap after PD
    // arrow trap is a must
    // block usage list:
    //     movable chest: 1, fake piston 1,
    //     crumbling dispenser: 1, trapped chest: 1,
    //     tripwires: 0, stone pressure plate: 0,
    //     sandy pressure plate: 1, tnt: 1,
    //     pipe: 0, raiser: 1.
    // + facade for dispenser1 trap
    // decide directions and count for dispenser1
    // ~ verify posBlueTerracotta
    // ~ bug: see that chest handlers persist across reloads
    // IMC
    // dispenser1 is okay but tame
    // message on pressure plate?
    // ~ bug: had pot at -12 in pot1 trap
    // un-hardcode dispensers in pot1 trap
    // + when scorpions are fixed make tunnel smaller
    // destroying moving blocks
    // sus sand in husk trap?
    // optimize some traps not to do work 4 times
    // encrusted lapis and emerald. some more treasure in general (pots?)
    // maybe black scorpions in pots? or a 3x3 shaft with a big black scorpion...


    private static final Logger LOGGER = LogUtils.getLogger();

    public ModDesertChanges()
    {
        Repository.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        //DesertTempleTransformers.add(FourHusks::setupHuskTrap);
        //DesertTempleTransformers.add(SimplePlateAndTwoTnts::setupSimplestTrap);
        //DesertTempleTransformers.add(SimplePlateAndTwoTnts::setupSimplestTrapTwoCorners); // maybe with % remove stone plate
        //DesertTempleTransformers.add(EscalatorsAndFireballs::setupFireChargeTrap); // it's a little timid
        DesertTempleTransformers.add(AbovePotAndHusks::suspiciousPot);// randomize below thingy
        DesertTempleTransformers.add(AbovePotAndHusks::suspiciousPot);// randomize below thingy
        //DesertTempleTransformers.add(ArrowsFromBottomLevel::setupEightDispensers); // maybe 5% chance for an old version?
        if (ModList.get().isLoaded("scorpions"))
        {
            DesertTempleTransformers.add(ScorpionsBehindMovingChests::setupTrap);
            DesertTempleTransformers.add(ScorpionsBehindMovingChests::setupTrap);
            DesertTempleTransformers.add(ScorpionsBehindMovingChests::setupTrap);
            DesertTempleTransformers.add(ScorpionsBehindMovingChests::setupTrap);
        }
        MovingBlocks.Handlers.put("husks", FourHusks::tryMoveTrappedChest);
        MovingBlocks.Handlers.put("scorpions_in_front", ScorpionsBehindMovingChests::tryMakeHorizontalShaft);
        MovingBlocks.Handlers.put("husks_above_shaft", AbovePotAndHusks::itsATrap);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
    }

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
