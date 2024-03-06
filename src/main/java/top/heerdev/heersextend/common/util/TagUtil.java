package top.heerdev.heersextend.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.HashMap;
import java.util.Map;

public class TagUtil {


    private ITagManager itemTagManager;

    private Map<String, ITag> tagKeyMap = new HashMap<>(); // <String, TagKey<Item>>

    public static final TagUtil INSTANCE = new TagUtil();

    public TagUtil() {

        this.itemTagManager = ForgeRegistries.ITEMS.tags();

    }


    public ITag getItemTag(String name) {
        if (tagKeyMap.containsKey(name)) {
            return tagKeyMap.get(name);
        } else {
            var tagKey = itemTagManager.createTagKey(new ResourceLocation(name));
            var tag = itemTagManager.getTag(tagKey);
            tagKeyMap.put(name, tag);
            return tag;
        }
    }


}
