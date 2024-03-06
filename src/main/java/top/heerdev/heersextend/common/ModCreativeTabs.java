package top.heerdev.heersextend.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import top.heerdev.heersextend.HeersExtend;
import top.heerdev.heersextend.common.item.ModItems;


public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HeersExtend.MODID);

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_TABS.register("heersextend_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativeTabs.heersextend"))
                    .icon(() -> ModItems.ITEMS.getEntries().stream().findFirst().get().get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        ModItems.ITEMS.getEntries().stream().forEach((entry) -> {
                            output.accept(entry.get().asItem());
                        });
                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_TABS.register(eventBus);
    }
}
