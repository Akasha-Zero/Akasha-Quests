package akasha.questui.client.data;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import betterquesting.api.properties.IPropertyType;
import betterquesting.api.properties.basic.PropertyTypeString;

/**
 * Extra properties stored in BetterQuesting's own NBT under the "akashaui" domain (DESIGN §9).
 * <p>
 * BQ's PropertyContainer keeps unknown domains through read, write, save and server-to-client sync, so these travel
 * with the quest database and are edited with BQ's own packets.
 */
public final class UiProps {

    public static final String DOMAIN = "akashaui";

    /** Quest: shape id. Chapter: default shape of its quests. Empty = inherit. */
    public static final IPropertyType<String> SHAPE = new PropertyTypeString(new ResourceLocation(DOMAIN, "shape"), "");

    /** Chapter: group title in the chapter panel. Empty = ungrouped. */
    public static final IPropertyType<String> GROUP = new PropertyTypeString(new ResourceLocation(DOMAIN, "group"), "");

    /** Chapter: short name for the chapter panel (the full BQ name stays in tooltips and BQ screens). */
    public static final IPropertyType<String> TITLE = new PropertyTypeString(new ResourceLocation(DOMAIN, "title"), "");

    /** Chapter: list of images (DESIGN §8). */
    public static final IPropertyType<NBTTagList> IMAGES = new PropertyTypeList(new ResourceLocation(DOMAIN, "images"));

    private UiProps() {}

    /** Compound-list property. Always returns a copy so callers can never mutate the stored tag by accident. */
    public static final class PropertyTypeList implements IPropertyType<NBTTagList> {

        private final ResourceLocation key;

        PropertyTypeList(ResourceLocation key) {
            this.key = key;
        }

        @Override
        public ResourceLocation getKey() {
            return key;
        }

        @Override
        public NBTTagList getDefault() {
            return new NBTTagList();
        }

        @Override
        public NBTTagList readValue(NBTBase nbt) {
            if (nbt instanceof NBTTagList) return (NBTTagList) nbt.copy();
            return new NBTTagList();
        }

        @Override
        public NBTBase writeValue(NBTTagList value) {
            return value == null ? new NBTTagList() : value.copy();
        }
    }
}
