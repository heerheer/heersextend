package top.heerdev.heersextend.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.heerdev.heersextend.HeersExtend;

public class MyBlockModelProvider extends BlockModelProvider {

    public MyBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HeersExtend.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {


        cube("sports_boy_generator",
                new ResourceLocation(HeersExtend.MODID, "block/sports_boy_generator_side"),
                new ResourceLocation(HeersExtend.MODID, "block/sports_boy_generator_side"),
                new ResourceLocation(HeersExtend.MODID, "block/sports_boy_generator_north"),
                new ResourceLocation(HeersExtend.MODID, "block/sports_boy_generator_side"),
                new ResourceLocation(HeersExtend.MODID, "block/sports_boy_generator_side"),
                new ResourceLocation(HeersExtend.MODID, "block/sports_boy_generator_side")
        ).texture("particle", new ResourceLocation(HeersExtend.MODID, "block/sports_boy_generator_side"));
    }
}
