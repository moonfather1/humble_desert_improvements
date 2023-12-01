package moonfather.humble_desert_improvements.pyramids.our_blocks;

import moonfather.humble_desert_improvements.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
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
        POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Constants.MODID);

    public static final RegistryObject<Block> ANCIENT_PISTON = BLOCKS.register("ancient_piston", CrumblingPiston::new);
    public static final RegistryObject<Item> ANCIENT_PISTON_ITEM = ITEMS.register("ancient_piston", () -> new BlockItem(ANCIENT_PISTON.get(), new Item.Properties()));

    public static final RegistryObject<Block> PRESSURE_PLATE = BLOCKS.register("sandstone_pressure_plate", PressurePlate::new);
    public static final RegistryObject<Item> PRESSURE_PLATE_ITEM = ITEMS.register("sandstone_pressure_plate", () -> new BlockItem(PRESSURE_PLATE.get(), new Item.Properties()));

    public static final RegistryObject<Block> SIGNAL_TRANSFER_PIPE = BLOCKS.register("signal_transfer_block", SignalPipe::new);
    public static final RegistryObject<Item> SIGNAL_TRANSFER_PIPE_ITEM = ITEMS.register("signal_transfer_block", () -> new BlockItem(SIGNAL_TRANSFER_PIPE.get(), new Item.Properties()));

    public static final RegistryObject<Block> SIGNAL_TRANSFER_RAISER = BLOCKS.register("signal_escalator_block", SignalEscalator::new);
    public static final RegistryObject<Item> SIGNAL_TRANSFER_RAISER_ITEM = ITEMS.register("signal_escalator_block", () -> new BlockItem(SIGNAL_TRANSFER_RAISER.get(), new Item.Properties()));

    public static final RegistryObject<Block> FACADE = BLOCKS.register("facade_block", Facade::new);
    public static final RegistryObject<Item> FACADE_ITEM = ITEMS.register("facade_block", () -> new BlockItem(FACADE.get(), new Item.Properties()));

    public static final RegistryObject<Block> VENT = BLOCKS.register("ancient_vent", Vent::new);
    public static final RegistryObject<Item> VENT_ITEM = ITEMS.register("ancient_vent", () -> new BlockItem(VENT.get(), new Item.Properties()));

    /////////////////////////////

    public static final RegistryObject<Potion> NASTY_SLOWNESS = POTIONS.register("nasty_slow_potion", () -> new Potion("slowness3", new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 9620, 2), new MobEffectInstance(MobEffects.SLOW_FALLING, 9620, 2)));
    public static final RegistryObject<Potion> NASTY_POISON = POTIONS.register("nasty_poison_potion", () -> new Potion("poison3", new MobEffectInstance(MobEffects.POISON, 840, 1), new MobEffectInstance(MobEffects.BLINDNESS, 840, 0)));
    public static final RegistryObject<Potion> UNLUCKY_POISON = POTIONS.register("unlucky_poison_potion", () -> new Potion("poison4", new MobEffectInstance(MobEffects.POISON, 840, 1), new MobEffectInstance(MobEffects.HUNGER, 840, 0), new MobEffectInstance(MobEffects.UNLUCK, 9620, 2)));
}
