package top.heerdev.heersextend;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.heerdev.heersextend.client.render.CyberKillerMachineRenderer;
import top.heerdev.heersextend.client.screen.CyberKillerMachineScreen;
import top.heerdev.heersextend.client.screen.NbtFilterScreen;
import top.heerdev.heersextend.client.screen.SportsBoyGeneratorScreen;
import top.heerdev.heersextend.client.screen.TesterScreen;
import top.heerdev.heersextend.common.ModCreativeTabs;
import top.heerdev.heersextend.common.block.ModBlocks;
import top.heerdev.heersextend.common.gui.ModMenus;
import top.heerdev.heersextend.common.gui.TesterContainer;
import top.heerdev.heersextend.common.item.ModItems;
import top.heerdev.heersextend.common.net.HeersExtendPacketHandler;
import top.heerdev.heersextend.datagen.ItemModelProvider;
import top.heerdev.heersextend.datagen.MyBlockModelProvider;
import top.heerdev.heersextend.datagen.StateProvider;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HeersExtend.MODID)
public class HeersExtend {

    public static final String MODID = "heersextend";

    private static final Logger LOGGER = LogUtils.getLogger();


    public HeersExtend() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModMenus.register(modEventBus);

        modEventBus.register(this);
        modEventBus.addListener(this::onGatherData);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(HeersExtendPacketHandler::register);
    }

    void onGatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var packOutput = gen.getPackOutput();
        var helper = event.getExistingFileHelper();

        gen.addProvider(event.includeClient(), new ItemModelProvider(packOutput, helper));
        gen.addProvider(event.includeClient(), new MyBlockModelProvider(packOutput, helper));
        gen.addProvider(event.includeClient(), new StateProvider(packOutput, helper));
    }


    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {


            MenuScreens.register(ModMenus.CYBER_KILLER.get(), CyberKillerMachineScreen::new);
            MenuScreens.register(ModMenus.NBT_FILTER_CONTAINER.get(), NbtFilterScreen::new);
            MenuScreens.register(ModMenus.SPORTS_BOY_GENERATOR_CONTAINER.get(), SportsBoyGeneratorScreen::new);
            MenuScreens.register(ModMenus.TESTER_CONTAINER.get(),
                    new MenuScreens.ScreenConstructor<TesterContainer, TesterScreen>() {
                        @Override
                        public TesterScreen create(TesterContainer pMenu, Inventory pInventory, Component pTitle) {
                            System.out.println("create TesterScreen");
                            return new TesterScreen(pMenu, pInventory, pTitle);
                        }
                    });
        }

        @SubscribeEvent
        public static void initClient(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlocks.CYBER_KILLER_ENTITY.get(), CyberKillerMachineRenderer::new);
        }
    }
}

