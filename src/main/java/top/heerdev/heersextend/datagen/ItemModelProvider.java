package top.heerdev.heersextend.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.heerdev.heersextend.HeersExtend;

//物品的模型生成器
public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(PackOutput gen, ExistingFileHelper helper) {
        super(gen, HeersExtend.MODID, helper);
    }

    @Override
    protected void registerModels() {
        // 第一个参数为模型对应的物品 ID，此示例为 sulfur_dust，因此在 xiaozhong:item/sulfur_dust 处生成模型文件
        // 第二个参数为父模型，一般物品的父模型均为 minecraft:item/generated，此处简写为 new ResourceLocation("item/generated")
        // 第三个参数及第四个参数为纹理名称及位置，对于当前父模型而言需要指定 layer0 对应的纹理名称，此处为 xiaozhong:item/sulfur_dust

//        ModItems.ITEMS.getEntries().forEach(entry -> {
//            var id = ForgeRegistries.ITEMS.getKey(entry.get()).getPath();
//            this.singleTexture(id, new ResourceLocation("item/generated"), "layer0", new ResourceLocation(ExampleMod.MODID, "item/" + id));
//        });

        // 生成一些简单的item
        var singleItems = new String[]{"cyber_core", "cyber_power_core", "upgrade", "white_socks", "dirty_white_socks"};

        for (String item : singleItems) {
            this.singleTexture(item, new ResourceLocation("item/generated"), "layer0", new ResourceLocation(HeersExtend.MODID, "item/" + item));
        }


    }
}


