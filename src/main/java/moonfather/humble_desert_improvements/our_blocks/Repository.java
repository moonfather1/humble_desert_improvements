package moonfather.humble_desert_improvements.our_blocks;

import moonfather.humble_desert_improvements.Constants;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Repository
{
    public static void init()
    {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);

    public static final RegistryObject<Block> ANCIENT_PISTON = BLOCKS.register("ancient_piston", CrumblingPiston::new);
    public static final RegistryObject<Item> ANCIENT_PISTON_ITEM = ITEMS.register("ancient_piston", () -> new BlockItem(ANCIENT_PISTON.get(), new Item.Properties()));

    public static final RegistryObject<Block> PRESSURE_PLATE = BLOCKS.register("sandstone_pressure_plate", PressurePlate::new);
    public static final RegistryObject<Item> PRESSURE_PLATE_ITEM = ITEMS.register("sandstone_pressure_plate", () -> new BlockItem(PRESSURE_PLATE.get(), new Item.Properties()));

    public static final RegistryObject<Block> SIGNAL_TRANSFER_PIPE = BLOCKS.register("signal_transfer_block", SignalPipe::new);
    public static final RegistryObject<Item> SIGNAL_TRANSFER_PIPE_ITEM = ITEMS.register("signal_transfer_block", () -> new BlockItem(SIGNAL_TRANSFER_PIPE.get(), new Item.Properties()));

    public static final RegistryObject<Block> SIGNAL_TRANSFER_RAISER = BLOCKS.register("signal_escalator_block", SignalEscalator::new);
    public static final RegistryObject<Item> SIGNAL_TRANSFER_RAISER_ITEM = ITEMS.register("signal_escalator_block", () -> new BlockItem(SIGNAL_TRANSFER_RAISER.get(), new Item.Properties()));
}
