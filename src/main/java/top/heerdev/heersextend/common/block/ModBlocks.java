package top.heerdev.heersextend.common.block;

import com.mojang.datafixers.DSL;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.heerdev.heersextend.HeersExtend;
import top.heerdev.heersextend.common.block.entity.CyberKillerMachineEntity;
import top.heerdev.heersextend.common.block.entity.NbtFilterEntity;
import top.heerdev.heersextend.common.block.entity.SportsBoyGeneratorEntity;
import top.heerdev.heersextend.common.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, HeersExtend.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, HeersExtend.MODID);

    // register blocks
    public static final RegistryObject<CyberKillerMachineBlock> CYBER_KILLER_MACHINE = registerBlock("cyber_killer", CyberKillerMachineBlock::new);

    public static final RegistryObject<NbtFilterBlock> NBT_FILTER = registerBlock("nbt_filter", NbtFilterBlock::new);

    public static final RegistryObject<SportsBoyGeneratorBlock> SPORTS_BOY_GENERATOR = registerBlock("sports_boy_generator", SportsBoyGeneratorBlock::new);


    // register block entities
    public static final RegistryObject<BlockEntityType<CyberKillerMachineEntity>> CYBER_KILLER_ENTITY =
            BLOCK_ENTITIES.register("cyber_killer", () -> BlockEntityType.Builder.of(CyberKillerMachineEntity::new, CYBER_KILLER_MACHINE.get()).build(DSL.remainderType()));

    public static final RegistryObject<BlockEntityType<NbtFilterEntity>> NBT_FILTER_ENTITY =
            BLOCK_ENTITIES.register("nbt_filter", () -> BlockEntityType.Builder.of(NbtFilterEntity::new, NBT_FILTER.get()).build(DSL.remainderType()));

    public static final RegistryObject<BlockEntityType<SportsBoyGeneratorEntity>> SPORTS_BOY_GENERATOR_ENTITY =
            BLOCK_ENTITIES.register("sports_boy_generator", () -> BlockEntityType.Builder.of(SportsBoyGeneratorEntity::new, SPORTS_BOY_GENERATOR.get()).build(DSL.remainderType()));



    // Register Blocks
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }


    // Register Items with Block Items
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {

        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    // Register Blocks with Events
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}
