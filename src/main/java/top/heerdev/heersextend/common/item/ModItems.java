package top.heerdev.heersextend.common.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.heerdev.heersextend.HeersExtend;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HeersExtend.MODID);


    public static final RegistryObject<Item> CYBER_CORE = ITEMS.register("cyber_core",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CYBER_POWER_CORE_ITEM = ITEMS.register("cyber_power_core",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> UPGRADE = ITEMS.register("upgrade",
            () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> WHITE_SOCKS = ITEMS.register("white_socks",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DIRTY_WHITE_SOCKS = ITEMS.register("dirty_white_socks",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TESTER = ITEMS.register("tester", TesterItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
