package moonfather.humble_desert_improvements.palms.blocks;

import moonfather.humble_desert_improvements.Constants;
import moonfather.humble_desert_improvements.pyramids.our_blocks.*;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PalmRepository
{
    public static void init()
    {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);

    public static final RegistryObject<Block> LOG_SIMPLE = BLOCKS.register("palm_log", SimpleTrunk::new);
    public static final RegistryObject<Item> LOG_SIMPLE_ITEM = ITEMS.register("palm_log", () -> new BlockItem(LOG_SIMPLE.get(), new Item.Properties()));
    public static final RegistryObject<Block> LOG_SLANTED = BLOCKS.register("palm_slanted_log", SlantedTrunk::new);
    public static final RegistryObject<Item> DATE_ITEM = ITEMS.register("date_fruit", () -> new Item(new Item.Properties().food(Foods.DRIED_KELP)));
}
