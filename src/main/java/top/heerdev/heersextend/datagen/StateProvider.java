package top.heerdev.heersextend.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.heerdev.heersextend.HeersExtend;
import top.heerdev.heersextend.common.block.ModBlocks;

public class StateProvider extends BlockStateProvider {
    public StateProvider(PackOutput gen, ExistingFileHelper helper) {
        super(gen, HeersExtend.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {

        this.simpleBlockWithItem(ModBlocks.CYBER_KILLER_MACHINE.get(), this.cubeAll(ModBlocks.CYBER_KILLER_MACHINE.get()));


        this.simpleBlockItem(ModBlocks.NBT_FILTER.get(),
                this.models().getExistingFile(new ResourceLocation(HeersExtend.MODID, "block/nbt_filter")));


        //this.simpleBlock(ModBlocks.CYBER_TNT.get(), this.cubeAll(ModBlocks.CYBER_TNT.get()));
        this.simpleBlockItem(ModBlocks.SPORTS_BOY_GENERATOR.get(),
                this.models().getExistingFile(new ResourceLocation(HeersExtend.MODID, "block/sports_boy_generator")));

    }
}
