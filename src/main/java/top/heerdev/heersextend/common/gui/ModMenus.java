package top.heerdev.heersextend.common.gui;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.heerdev.heersextend.HeersExtend;

public class ModMenus {


    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, HeersExtend.MODID);


    public static final RegistryObject<MenuType<CyberKillerMachineContainer>> CYBER_KILLER = MENU_TYPES.register("cyber_killer",
            () -> IForgeMenuType.create((windowId, inv, data) -> new CyberKillerMachineContainer(windowId, inv, data.readBlockPos())));

    public static final RegistryObject<MenuType<NbtFilterContainer>> NBT_FILTER_CONTAINER = MENU_TYPES.register("nbt_filter",
            () -> IForgeMenuType.create((windowId, inv, data) -> new NbtFilterContainer(windowId, inv, data.readBlockPos())));

    public static final RegistryObject<MenuType<SportsBoyGeneratorContainer>> SPORTS_BOY_GENERATOR_CONTAINER = MENU_TYPES.register("sports_boy_generator",
            () -> IForgeMenuType.create((windowId, inv, data) -> new SportsBoyGeneratorContainer(windowId, inv, data.readBlockPos())));


    public static final RegistryObject<MenuType<TesterContainer>> TESTER_CONTAINER = MENU_TYPES.register("tester",
            () -> IForgeMenuType.create((windowId, inv, data) -> new TesterContainer(windowId, inv, data.readBlockPos())));


    public static void register(IEventBus modEventBus) {
        MENU_TYPES.register(modEventBus);
    }
}
