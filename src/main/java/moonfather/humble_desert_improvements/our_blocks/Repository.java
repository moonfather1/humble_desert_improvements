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

    public static final RegistryObject<Block> ANCIENT_PISTON = BLOCKS.register("ancient_piston", () -> new CrumblingPiston());
    public static final RegistryObject<Item> ANCIENT_PISTON_ITEM = ITEMS.register("ancient_piston", () -> new BlockItem(ANCIENT_PISTON.get(), new Item.Properties()));
}
