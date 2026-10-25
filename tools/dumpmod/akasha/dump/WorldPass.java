package akasha.dump;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/** Second pass: create a world, then dump NEI recipe catalogs and BetterQuesting load results. */
public class WorldPass {

    private static final Gson GSON = new Gson();
    private final File outDir;
    private int stage = 0;
    private int wait = 0;

    public WorldPass(File outDir) {
        this.outDir = outDir;
    }

    /** @return true when finished */
    public boolean tick(Minecraft mc) {
        try {
            switch (stage) {
                case 0:
                    WorldSettings ws = new WorldSettings(4242L, WorldSettings.GameType.CREATIVE, false, false, WorldType.FLAT);
                    ws.enableCommands();
                    mc.launchIntegratedServer("akashadump", "akashadump", ws);
                    stage = 1;
                    return false;
                case 1:
                    if (mc.thePlayer == null || mc.theWorld == null) return false;
                    if (++wait < 400) return false;
                    stage = 2;
                    return false;
                case 2:
                    log("world ready; dumping NEI");
                    dumpNei();
                    log("dumping BQ");
                    dumpBq();
                    stage = 3;
                    return true;
                default:
                    return true;
            }
        } catch (Throwable t) {
            t.printStackTrace();
            log("FAILED " + t);
            return true;
        }
    }

    private void log(String s) {
        System.out.println("[AkashaDump/World] " + s);
        try {
            Files.write(new File(outDir, "status_world.txt").toPath(), s.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ignored) {}
    }

    private PrintWriter open(String name) throws Exception {
        return new PrintWriter(new OutputStreamWriter(Files.newOutputStream(new File(outDir, name).toPath()), StandardCharsets.UTF_8));
    }

    private static Object call(Object o, String name, Object... args) throws Exception {
        for (Method m : o.getClass().getMethods()) {
            if (m.getName().equals(name) && m.getParameterTypes().length == args.length) {
                return m.invoke(o, args);
            }
        }
        throw new NoSuchMethodException(name);
    }

    private static JsonArray positioned(Object ps) throws Exception {
        JsonArray a = new JsonArray();
        if (ps == null) return a;
        ItemStack[] items = (ItemStack[]) ps.getClass().getField("items").get(ps);
        if (items == null) return a;
        int n = 0;
        for (ItemStack s : items) {
            if (s == null) continue;
            a.add(AkashaDump.stackJson(s));
            if (++n >= 4) break;
        }
        return a;
    }

    @SuppressWarnings("unchecked")
    private void dumpNei() throws Exception {
        Class<?> gcr = Class.forName("codechicken.nei.recipe.GuiCraftingRecipe");
        List<Object> handlers = (List<Object>) gcr.getField("craftinghandlers").get(null);
        List<Object> serial = (List<Object>) gcr.getField("serialCraftingHandlers").get(null);
        Set<Object> all = new java.util.LinkedHashSet<>(handlers);
        if (serial != null) all.addAll(serial);
        Class<?> trh = Class.forName("codechicken.nei.recipe.TemplateRecipeHandler");
        Field rectsF = trh.getField("transferRects");
        Class<?> rtr = Class.forName("codechicken.nei.recipe.TemplateRecipeHandler$RecipeTransferRect");
        Field outF = rtr.getDeclaredField("outputId");
        outF.setAccessible(true);
        Field resF = rtr.getDeclaredField("results");
        resF.setAccessible(true);
        int total = 0;
        try (PrintWriter w = open("nei_recipes.jsonl"); PrintWriter hw = open("nei_handlers.jsonl")) {
            for (Object h : all) {
                Set<String> ids = new HashSet<>();
                JsonObject hj = new JsonObject();
                hj.addProperty("cls", h.getClass().getName());
                String name;
                try { name = String.valueOf(call(h, "getRecipeName")); } catch (Throwable t) { name = "?"; }
                hj.addProperty("name", name);
                String overlay = null;
                try { overlay = (String) call(h, "getOverlayIdentifier"); } catch (Throwable ignored) {}
                hj.addProperty("overlay", overlay);
                int count = 0;
                if (trh.isInstance(h)) {
                    Collection<Object> rects = (Collection<Object>) rectsF.get(h);
                    if (rects != null) {
                        for (Object r : rects) {
                            String id = (String) outF.get(r);
                            Object[] res = (Object[]) resF.get(r);
                            if (id == null || !ids.add(id)) continue;
                            count += dumpHandler(w, h, name, id, res == null ? new Object[0] : res);
                        }
                    }
                }
                if (overlay != null && !ids.contains(overlay)) {
                    ids.add(overlay);
                    count += dumpHandler(w, h, name, overlay, new Object[0]);
                }
                hj.addProperty("recipes", count);
                JsonArray idsA = new JsonArray();
                for (String s : ids) idsA.add(new com.google.gson.JsonPrimitive(s));
                hj.add("ids", idsA);
                hw.println(GSON.toJson(hj));
                hw.flush();
                total += count;
            }
        }
        log("NEI recipes: " + total);
    }

    private int dumpHandler(PrintWriter w, Object h, String name, String id, Object[] res) {
        int n = 0;
        try {
            Object sub = call(h, "getRecipeHandler", id, res);
            if (sub == null) return 0;
            int num = (Integer) call(sub, "numRecipes");
            for (int i = 0; i < num && i < 20000; i++) {
                JsonObject j = new JsonObject();
                j.addProperty("handler", name);
                j.addProperty("hcls", h.getClass().getName());
                j.addProperty("id", id);
                try {
                    JsonArray ins = new JsonArray();
                    for (Object ps : (List<?>) call(sub, "getIngredientStacks", i)) ins.add(positioned(ps));
                    j.add("in", ins);
                } catch (Throwable ignored) {}
                try { j.add("out", positioned(call(sub, "getResultStack", i))); } catch (Throwable ignored) {}
                try {
                    JsonArray oth = new JsonArray();
                    for (Object ps : (List<?>) call(sub, "getOtherStacks", i)) oth.add(positioned(ps));
                    j.add("other", oth);
                } catch (Throwable ignored) {}
                w.println(GSON.toJson(j));
                n++;
            }
        } catch (Throwable t) {
            System.out.println("[AkashaDump/World] handler " + name + "/" + id + " failed: " + t);
        }
        return n;
    }

    @SuppressWarnings("unchecked")
    private void dumpBq() {
        try (PrintWriter w = open("bq_check.txt")) {
            Class<?> qdb = Class.forName("betterquesting.questing.QuestDatabase");
            Object inst = qdb.getField("INSTANCE").get(null);
            int quests = (Integer) call(inst, "size");
            Class<?> qldb = Class.forName("betterquesting.questing.QuestLineDatabase");
            Object linst = qldb.getField("INSTANCE").get(null);
            int lines = (Integer) call(linst, "size");
            w.println("quests=" + quests);
            w.println("lines=" + lines);
            // count tasks/rewards that failed to resolve (placeholders) and icons that resolved to null
            int placeholderTasks = 0, placeholderRewards = 0;
            for (Object e : (Collection<Object>) call(inst, "values")) {
                Object tasks = call(e, "getTasks");
                for (Object t : (Collection<Object>) call(tasks, "getEntries")) {
                    Object v = call(t, "getValue");
                    if (v.getClass().getName().contains("Placeholder")) placeholderTasks++;
                }
                Object rewards = call(e, "getRewards");
                for (Object r : (Collection<Object>) call(rewards, "getEntries")) {
                    Object v = call(r, "getValue");
                    if (v.getClass().getName().contains("Placeholder")) placeholderRewards++;
                }
            }
            w.println("placeholderTasks=" + placeholderTasks);
            w.println("placeholderRewards=" + placeholderRewards);
        } catch (Throwable t) {
            t.printStackTrace();
            log("bq check failed " + t);
        }
    }
}
