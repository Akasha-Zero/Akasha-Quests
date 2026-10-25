package akasha.dump;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.Language;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.potion.Potion;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameData;

@Mod(modid = "akashadump", version = "1", name = "AkashaDump", acceptedMinecraftVersions = "[1.7.10]")
public class AkashaDump {

    private static final Gson GSON = new Gson();
    private File outDir;
    private boolean done = false;
    private int ticks = 0;

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        outDir = new File(System.getProperty("akashadump.out", "akashadump"));
        outDir.mkdirs();
        if (FMLCommonHandler.instance().getSide().isClient()) {
            FMLCommonHandler.instance().bus().register(this);
        }
    }

    private WorldPass worldPass;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (done || e.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if ("world".equals(System.getProperty("akashadump.mode"))) {
            if (worldPass == null) {
                if (mc.currentScreen == null || ++ticks < 100) return;
                worldPass = new WorldPass(outDir);
            }
            if (worldPass.tick(mc)) {
                done = true;
                log("WORLD DONE");
                mc.shutdown();
            }
            return;
        }
        if (mc.currentScreen == null || mc.theWorld != null) return;
        if (++ticks < 100) return;
        done = true;
        try {
            dumpStatic();
            for (String lang : new String[] { "en_US", "pt_BR" }) {
                setLang(mc, lang);
                dumpItems(lang);
                dumpNames(lang);
            }
            log("DONE");
        } catch (Throwable t) {
            t.printStackTrace();
            log("FAILED " + t);
        }
        if (!"0".equals(System.getProperty("akashadump.exit", "1"))) {
            mc.shutdown();
        }
    }

    private void setLang(Minecraft mc, String code) {
        mc.gameSettings.language = code;
        mc.getLanguageManager().setCurrentLanguage(new Language(code, "", "", false));
        mc.getLanguageManager().onResourceManagerReload(mc.getResourceManager());
    }

    private void log(String s) {
        System.out.println("[AkashaDump] " + s);
        try {
            Files.write(new File(outDir, "status.txt").toPath(), s.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ignored) {}
    }

    private PrintWriter open(String name) throws Exception {
        return new PrintWriter(new OutputStreamWriter(Files.newOutputStream(new File(outDir, name).toPath()), StandardCharsets.UTF_8));
    }

    private static String regName(Item item) {
        Object n = GameData.getItemRegistry().getNameForObject(item);
        return n == null ? null : n.toString();
    }

    public static JsonObject stackJson(ItemStack s) {
        JsonObject o = new JsonObject();
        if (s == null || s.getItem() == null) {
            o.addProperty("id", "null");
            return o;
        }
        o.addProperty("id", regName(s.getItem()));
        o.addProperty("meta", s.getItemDamage());
        o.addProperty("count", s.stackSize);
        if (s.stackTagCompound != null) o.addProperty("nbt", s.stackTagCompound.toString());
        return o;
    }

    private static JsonArray ingredientJson(Object in) {
        JsonArray a = new JsonArray();
        if (in == null) return a;
        if (in instanceof ItemStack) {
            a.add(stackJson((ItemStack) in));
        } else if (in instanceof String) {
            JsonObject o = new JsonObject();
            o.addProperty("ore", (String) in);
            a.add(o);
        } else if (in instanceof List) {
            List<?> l = (List<?>) in;
            String ore = findOreName(l);
            if (ore != null) {
                JsonObject o = new JsonObject();
                o.addProperty("ore", ore);
                a.add(o);
            } else {
                for (Object x : l) if (x instanceof ItemStack) a.add(stackJson((ItemStack) x));
            }
        }
        return a;
    }

    private static String findOreName(List<?> l) {
        for (String name : OreDictionary.getOreNames()) {
            if (OreDictionary.getOres(name) == l) return name;
        }
        return null;
    }

    private List<ItemStack> subItems(Item item) {
        List<ItemStack> out = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        List<CreativeTabs> tabs = new ArrayList<>();
        try {
            for (CreativeTabs t : item.getCreativeTabs()) tabs.add(t);
        } catch (Throwable ignored) {}
        if (tabs.isEmpty()) tabs.add(null);
        for (CreativeTabs tab : tabs) {
            List<ItemStack> l = new ArrayList<>();
            try {
                item.getSubItems(item, tab, l);
            } catch (Throwable t) {
                continue;
            }
            for (ItemStack s : l) {
                if (s == null || s.getItem() == null) continue;
                String key = regName(s.getItem()) + "@" + s.getItemDamage() + "@" + s.stackTagCompound;
                if (seen.add(key)) out.add(s);
            }
        }
        return out;
    }

    private void dumpItems(String lang) throws Exception {
        try (PrintWriter w = open("items_" + lang + ".jsonl")) {
            for (Object o : GameData.getItemRegistry()) {
                Item item = (Item) o;
                String name = regName(item);
                List<ItemStack> subs = subItems(item);
                boolean inCreative = !subs.isEmpty();
                if (subs.isEmpty()) subs.add(new ItemStack(item, 1, 0));
                for (ItemStack s : subs) {
                    JsonObject j = new JsonObject();
                    j.addProperty("id", name);
                    j.addProperty("num", Item.getIdFromItem(item));
                    j.addProperty("meta", s.getItemDamage());
                    if (s.stackTagCompound != null) j.addProperty("nbt", s.stackTagCompound.toString());
                    String dn;
                    try { dn = s.getDisplayName(); } catch (Throwable t) { dn = "<err>"; }
                    j.addProperty("name", dn);
                    String un;
                    try { un = s.getUnlocalizedName(); } catch (Throwable t) { un = "<err>"; }
                    j.addProperty("unloc", un);
                    j.addProperty("block", item instanceof ItemBlock);
                    j.addProperty("creative", inCreative);
                    j.addProperty("cls", item.getClass().getName());
                    j.addProperty("stack", item.getItemStackLimit(s));
                    j.addProperty("maxDmg", item.getMaxDamage());
                    try {
                        CreativeTabs t = item.getCreativeTab();
                        if (t != null) j.addProperty("tab", t.getTabLabel());
                    } catch (Throwable ignored) {}
                    JsonArray ores = new JsonArray();
                    try {
                        for (int id : OreDictionary.getOreIDs(s)) ores.add(new com.google.gson.JsonPrimitive(OreDictionary.getOreName(id)));
                    } catch (Throwable ignored) {}
                    j.add("ore", ores);
                    w.println(GSON.toJson(j));
                }
            }
        }
    }

    private void dumpNames(String lang) throws Exception {
        try (PrintWriter w = open("names_" + lang + ".jsonl")) {
            for (Fluid f : FluidRegistry.getRegisteredFluids().values()) {
                JsonObject j = new JsonObject();
                j.addProperty("kind", "fluid");
                j.addProperty("id", f.getName());
                try { j.addProperty("name", f.getLocalizedName()); } catch (Throwable t) { j.addProperty("name", "<err>"); }
                w.println(GSON.toJson(j));
            }
            for (Object k : EntityList.stringToClassMapping.keySet()) {
                JsonObject j = new JsonObject();
                j.addProperty("kind", "entity");
                j.addProperty("id", k.toString());
                j.addProperty("name", StatCollector.translateToLocal("entity." + k + ".name"));
                w.println(GSON.toJson(j));
            }
            for (Enchantment en : Enchantment.enchantmentsList) {
                if (en == null) continue;
                JsonObject j = new JsonObject();
                j.addProperty("kind", "enchant");
                j.addProperty("num", en.effectId);
                j.addProperty("id", en.getName());
                j.addProperty("name", StatCollector.translateToLocal(en.getName()));
                w.println(GSON.toJson(j));
            }
            for (Potion p : Potion.potionTypes) {
                if (p == null) continue;
                JsonObject j = new JsonObject();
                j.addProperty("kind", "potion");
                j.addProperty("num", p.id);
                j.addProperty("id", p.getName());
                j.addProperty("name", StatCollector.translateToLocal(p.getName()));
                w.println(GSON.toJson(j));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void dumpStatic() throws Exception {
        try (PrintWriter w = open("mods.jsonl")) {
            for (ModContainer mc : Loader.instance().getActiveModList()) {
                JsonObject j = new JsonObject();
                j.addProperty("modid", mc.getModId());
                j.addProperty("name", mc.getName());
                j.addProperty("version", mc.getVersion());
                j.addProperty("file", mc.getSource() == null ? "" : mc.getSource().getName());
                w.println(GSON.toJson(j));
            }
        }
        try (PrintWriter w = open("entities.jsonl")) {
            for (Object e : EntityList.stringToClassMapping.entrySet()) {
                Map.Entry<Object, Object> en = (Map.Entry<Object, Object>) e;
                JsonObject j = new JsonObject();
                j.addProperty("id", en.getKey().toString());
                j.addProperty("cls", ((Class<?>) en.getValue()).getName());
                w.println(GSON.toJson(j));
            }
        }
        try (PrintWriter w = open("fluids.jsonl")) {
            for (Fluid f : FluidRegistry.getRegisteredFluids().values()) {
                JsonObject j = new JsonObject();
                j.addProperty("id", f.getName());
                j.addProperty("unloc", f.getUnlocalizedName());
                j.addProperty("temp", f.getTemperature());
                j.addProperty("gas", f.isGaseous());
                j.addProperty("block", f.getBlock() == null ? "" : String.valueOf(GameData.getBlockRegistry().getNameForObject(f.getBlock())));
                w.println(GSON.toJson(j));
            }
            for (FluidContainerRegistry.FluidContainerData d : FluidContainerRegistry.getRegisteredFluidContainerData()) {
                JsonObject j = new JsonObject();
                j.addProperty("container", true);
                j.addProperty("fluid", d.fluid == null ? "" : d.fluid.getFluid().getName());
                j.add("filled", stackJson(d.filledContainer));
                j.add("empty", stackJson(d.emptyContainer));
                w.println(GSON.toJson(j));
            }
        }
        try (PrintWriter w = open("biomes.jsonl")) {
            for (BiomeGenBase b : BiomeGenBase.getBiomeGenArray()) {
                if (b == null) continue;
                JsonObject j = new JsonObject();
                j.addProperty("num", b.biomeID);
                j.addProperty("name", b.biomeName);
                j.addProperty("cls", b.getClass().getName());
                w.println(GSON.toJson(j));
            }
        }
        try (PrintWriter w = open("dimensions.jsonl")) {
            for (Integer id : DimensionManager.getStaticDimensionIDs()) {
                JsonObject j = new JsonObject();
                j.addProperty("num", id);
                try {
                    int type = DimensionManager.getProviderType(id);
                    j.addProperty("type", type);
                    WorldProvider p = DimensionManager.createProviderFor(id);
                    j.addProperty("cls", p.getClass().getName());
                    try { j.addProperty("name", p.getDimensionName()); } catch (Throwable t) { j.addProperty("name", "<err>"); }
                } catch (Throwable t) {
                    j.addProperty("err", t.toString());
                }
                w.println(GSON.toJson(j));
            }
        }
        try (PrintWriter w = open("oredict.jsonl")) {
            for (String name : OreDictionary.getOreNames()) {
                JsonObject j = new JsonObject();
                j.addProperty("ore", name);
                JsonArray a = new JsonArray();
                for (ItemStack s : OreDictionary.getOres(name)) a.add(stackJson(s));
                j.add("items", a);
                w.println(GSON.toJson(j));
            }
        }
        try (PrintWriter w = open("recipes_crafting.jsonl")) {
            for (Object o : CraftingManager.getInstance().getRecipeList()) {
                IRecipe r = (IRecipe) o;
                JsonObject j = new JsonObject();
                j.addProperty("cls", r.getClass().getName());
                try { j.add("out", stackJson(r.getRecipeOutput())); } catch (Throwable t) { continue; }
                JsonArray in = new JsonArray();
                try {
                    if (r instanceof ShapedRecipes) {
                        ShapedRecipes s = (ShapedRecipes) r;
                        j.addProperty("w", s.recipeWidth);
                        for (ItemStack x : s.recipeItems) in.add(ingredientJson(x));
                    } else if (r instanceof ShapelessRecipes) {
                        for (Object x : ((ShapelessRecipes) r).recipeItems) in.add(ingredientJson(x));
                    } else if (r instanceof ShapedOreRecipe) {
                        ShapedOreRecipe s = (ShapedOreRecipe) r;
                        Field f = ShapedOreRecipe.class.getDeclaredField("width");
                        f.setAccessible(true);
                        j.addProperty("w", f.getInt(s));
                        for (Object x : s.getInput()) in.add(ingredientJson(x));
                    } else if (r instanceof ShapelessOreRecipe) {
                        for (Object x : ((ShapelessOreRecipe) r).getInput()) in.add(ingredientJson(x));
                    } else {
                        j.addProperty("opaque", true);
                    }
                } catch (Throwable t) {
                    j.addProperty("err", t.toString());
                }
                j.add("in", in);
                w.println(GSON.toJson(j));
            }
        }
        try (PrintWriter w = open("recipes_furnace.jsonl")) {
            for (Object e : FurnaceRecipes.smelting().getSmeltingList().entrySet()) {
                Map.Entry<ItemStack, ItemStack> en = (Map.Entry<ItemStack, ItemStack>) e;
                JsonObject j = new JsonObject();
                j.add("in", stackJson(en.getKey()));
                j.add("out", stackJson(en.getValue()));
                w.println(GSON.toJson(j));
            }
        }
        dumpThaumcraft();
    }

    @SuppressWarnings("unchecked")
    private void dumpThaumcraft() {
        try (PrintWriter w = open("thaumcraft_research.jsonl")) {
            Class<?> rc = Class.forName("thaumcraft.api.research.ResearchCategories");
            Map<String, Object> cats = (Map<String, Object>) rc.getField("researchCategories").get(null);
            for (Map.Entry<String, Object> c : cats.entrySet()) {
                Map<String, Object> research = (Map<String, Object>) c.getValue().getClass().getField("research").get(c.getValue());
                for (Map.Entry<String, Object> r : research.entrySet()) {
                    Object ri = r.getValue();
                    JsonObject j = new JsonObject();
                    j.addProperty("cat", c.getKey());
                    j.addProperty("key", r.getKey());
                    try { j.addProperty("name", (String) ri.getClass().getMethod("getName").invoke(ri)); } catch (Throwable ignored) {}
                    try {
                        String[] parents = (String[]) ri.getClass().getField("parents").get(ri);
                        if (parents != null) j.addProperty("parents", String.join(",", parents));
                    } catch (Throwable ignored) {}
                    try {
                        ItemStack icon = (ItemStack) ri.getClass().getField("icon_item").get(ri);
                        if (icon != null) j.add("icon", stackJson(icon));
                    } catch (Throwable ignored) {}
                    w.println(GSON.toJson(j));
                }
            }
        } catch (Throwable t) {
            log("thaumcraft dump failed " + t);
        }
    }
}
