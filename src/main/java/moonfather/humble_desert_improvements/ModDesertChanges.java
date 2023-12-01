package moonfather.humble_desert_improvements;

import com.mojang.logging.LogUtils;
import moonfather.humble_desert_improvements.palms.blocks.PalmRepository;
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

    // block usage list:
    //     movable chest: 3, fake piston 1,
    //     crumbling dispenser: 3, trapped chest: 2,
    //     tripwires: 0, stone pressure plate: 0,
    //     sandy pressure plate: 2, tnt: 1,
    //     pipe: 1, raiser: 3, pot: 1.
    // should make more with pots. other numbers can stay.            tchest+escalator+dispenser+tnt is a viable option

    // sand trap after PD, maybe powdered sand.
    // IMC
    // message on pressure plate?
    // trap with rows of 3 pots instead of chests. pots are on stair-benches and break in unison revealing mini scorpions or baby husks? second row above. maybe fireballs behind one row.
    // trap with scripted falling pots from wall holes. no drops, high damage. maybe piston left behind hole.
    // encrusted lapis and emerald. some more treasure in general (pots?)
    // maybe black scorpions in pots? or a 3x3 shaft with a big black scorpion...
    ////////////////////////////////////////////////////
    // date icon
    // slanted log canSurvive, PalmVoid canSurvive

    private static final Logger LOGGER = LogUtils.getLogger();

    public ModDesertChanges()
    {
        Repository.init();
        PalmRepository.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        DesertTempleTransformers.add(FourHusks::setupHuskTrap);
        DesertTempleTransformers.add(SimplePlateAndTwoTnts::setupSimplestTrap); // don't call two corners here, it is called from pot trap
        DesertTempleTransformers.add(EscalatorsAndFireballs::setupFireChargeTrap);
        DesertTempleTransformers.add(EscalatorsAndArrows::setupArrowTrap);
        DesertTempleTransformers.add(AbovePotAndHusks::suspiciousPot);
        DesertTempleTransformers.add(ArrowsFromBottomLevel::setupEightDispensers); // old version is called from pot trap
        if (ModList.get().isLoaded("scorpions"))
        {
            DesertTempleTransformers.add(ScorpionBehindChestTiny::setupTrap);
            DesertTempleTransformers.add(ScorpionBehindChestTiny::setupTrap); //two to increase odds
            DesertTempleTransformers.add(ScorpionBehindChestBig::setupTrap);
        }
        MovingBlocks.Handlers.put("husks", FourHusks::tryMoveTrappedChest);
        MovingBlocks.Handlers.put("scorpions_in_front", ScorpionBehindChestTiny::tryMakeHorizontalShaft);
        MovingBlocks.Handlers.put("big_frelling_scorpion", ScorpionBehindChestBig::tryMakeBigHorizontalShaft);
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
