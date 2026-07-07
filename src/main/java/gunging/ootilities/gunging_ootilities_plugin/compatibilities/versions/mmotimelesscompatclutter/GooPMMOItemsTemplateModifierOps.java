package gunging.ootilities.gunging_ootilities_plugin.compatibilities.versions.mmotimelesscompatclutter;

import gunging.ootilities.gunging_ootilities_plugin.Gunging_Ootilities_Plugin;
import gunging.ootilities.gunging_ootilities_plugin.OotilityCeption;
import gunging.ootilities.gunging_ootilities_plugin.compatibilities.GooPMMOItems;
import gunging.ootilities.gunging_ootilities_plugin.misc.RefSimulator;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.build.MMOItemBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.api.item.template.NameModifier;
import net.Indyuce.mmoitems.manager.TemplateManager;
import net.Indyuce.mmoitems.stat.data.type.Mergeable;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import net.Indyuce.mmoitems.stat.type.NameData;
import net.Indyuce.mmoitems.stat.type.StatHistory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Handles MMOItems modifier operations.
 * In MMOItems 6.10.1, TemplateModifier was removed. 
 * This class uses runtime reflection to check for the old API availability.
 */
public class GooPMMOItemsTemplateModifierOps {

    static Method globalModifiersList;
    static Method templateModifiersList;
    static Method globalModifierGet;
    static Method templateModifierGet;

    public static boolean ReflectMethods() {
        try {
            Class<?> templateModClass = Class.forName("net.Indyuce.mmoitems.api.item.template.TemplateModifier");
            globalModifiersList = TemplateManager.class.getMethod("getModifiers");
            templateModifiersList = MMOItemTemplate.class.getMethod("getModifiers");
            globalModifierGet = TemplateManager.class.getMethod("getModifier", String.class);
            templateModifierGet = MMOItemTemplate.class.getMethod("getModifier", String.class);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Collection<Object> getGlobalModifiers() {
        try {
            Object plugin = MMOItems.class.getField("plugin").get(null);
            Object templates = plugin.getClass().getMethod("getTemplates").invoke(plugin);
            return (Collection<Object>) globalModifiersList.invoke(templates);
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    public static Collection<Object> getTemplateModifiers(@NotNull MMOItemTemplate template) {
        try {
            return ((Map<String, Object>) templateModifiersList.invoke(template)).values();
        } catch (IllegalAccessException|InvocationTargetException ignored) {
            return new ArrayList<>();
        }
    }

    public static Object getGlobalModifier(@NotNull String mod) {
        try {
            Object templates = getTemplatesReflect();
            return globalModifierGet.invoke(templates, mod);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static Object getTemplateModifier(@NotNull MMOItemTemplate template, @NotNull String mod) {
        try {
            return templateModifierGet.invoke(template, mod);
        } catch (IllegalAccessException|InvocationTargetException ignored) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<String> getGlobalModifierNames() {
        ArrayList<String> ret = new ArrayList<>();
        for (Object mod : getGlobalModifiers()) {
            try {
                Method getId = mod.getClass().getMethod("getId");
                ret.add((String) getId.invoke(mod));
            } catch (Exception ignored) { }
        }
        return ret;
    }

    @Nullable
    public static ItemStack ModifierOperation(@Nullable String rawModifier, @Nullable ItemStack mmo, boolean useGlobl, boolean chances, @Nullable RefSimulator<String> logger) {
        if (rawModifier == null || mmo == null) {
            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Unspecified modifier or item. ");
            return null;
        }

        if (!GooPMMOItems.IsMMOItem(mmo)) {
            if (useGlobl) {
                mmo = GooPMMOItems.ConvertVanillaToMMOItem(mmo);
            } else {
                OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "This " + OotilityCeption.GetItemName(mmo) + " is not an MMOItem, so it has no modifiers, enable \u00a7buse-global\u00a77 to use this command with this.");
                return null;
            }
        }

        MMOItemTemplate template = null;
        try {
            Object templates = getTemplatesReflect();
            template = (MMOItemTemplate) templates.getClass().getMethod("getTemplate", io.lumine.mythic.lib.api.item.NBTItem.class)
                    .invoke(templates, NBTItem.get(mmo));
        } catch (Exception ignored) { }

        if (template == null && !useGlobl) {
            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "This " + OotilityCeption.GetItemName(mmo) + " is an MMOItem but the template is not loaded, was it deleted? GooP cannot find its modifiers, enable \u00a7buse-global\u00a77 to use this command with this.");
            return null;
        }

        boolean dummyTemplate = (template == null);
        if (template == null) { template = new MMOItemTemplate(Type.TOOL, "HX_M\u00d1P"); }

        boolean random = rawModifier.equalsIgnoreCase("random");
        boolean clear = rawModifier.equalsIgnoreCase("none");

        boolean modifierLocal = template.hasModifier(rawModifier);
        boolean modifierExists = modifierLocal;
        if (useGlobl) {
            try {
                Object templates = getTemplatesReflect();
                modifierExists = modifierExists || (boolean) templates.getClass().getMethod("hasModifier", String.class).invoke(templates, rawModifier);
            } catch (Exception ignored) { }
        }

        if (!random && !clear && !modifierExists) {
            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "There is no modifier of name \u00a73" + rawModifier + "\u00a77. ");
            return null;
        }

        NBTItem nbt = NBTItem.get(mmo);
        LiveMMOItem live = new LiveMMOItem(nbt);

        if (random) {
            List<Object> modifiers = new ArrayList<>(getTemplateModifiers(template));
            if (useGlobl) { modifiers.addAll(getGlobalModifiers()); }

            if (modifiers.size() == 0) {
                OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, dummyTemplate ? ("MMOItem \u00a7e" + template.getType().getId() + " " + template.getId() + "\u00a77 has\u00a7c no modifiers\u00a77, cant pick a random one thus. ") : "There are\u00a7c no global modifiers\u00a77, cant pick a random one thus. ");
                return null;
            }

            Object modifier = null;
            if (chances) {
                int breaker = 0;
                while (modifier == null && breaker < 400) {
                    breaker++;
                    Collections.shuffle(modifiers);
                    for (Object mod : modifiers) {
                        try {
                            Method rollChance = mod.getClass().getMethod("rollChance");
                            if ((boolean) rollChance.invoke(mod)) { modifier = mod; break; }
                        } catch (Exception ignored) { }
                    }
                }
                if (breaker >= 400) {
                    OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "This " + OotilityCeption.GetItemName(mmo) + "\u00a77 could\u00a7c not roll\u00a77 for any modifier after \u00a7b" + breaker + "\u00a77 iterations, chances must be too low. ");
                    return null;
                }
            } else {
                modifier = modifiers.get(OotilityCeption.GetRandomInt(0, modifiers.size() - 1));
            }

            UUID modUUID = UUID.randomUUID();
            MMOItemBuilder freshBuilder = template.newBuilder();

            // Apply modifier via reflection (TemplateModifier API removed in 6.10.1)
            try {
                Method apply = modifier.getClass().getMethod("whenCollected", MMOItemBuilder.class, UUID.class);
                apply.invoke(modifier, freshBuilder, modUUID);
            } catch (Exception ignored) {
                // Fallback: try collect
                try {
                    Method collect = modifier.getClass().getMethod("collect", MMOItemBuilder.class);
                    collect.invoke(modifier, freshBuilder);
                } catch (Exception ignored2) { }
            }

            // Rename via getNameModifier
            try {
                Method hasNameMod = modifier.getClass().getMethod("hasNameModifier");
                if ((boolean) hasNameMod.invoke(modifier)) {
                    Method getNameMod = modifier.getClass().getMethod("getNameModifier");
                    Object namemod = getNameMod.invoke(modifier);

                    StatHistory hist = StatHistory.from(live, ItemStats.NAME);
                    NameData modName = new NameData("");

                    Method getType = namemod.getClass().getMethod("getType");
                    Object modType = getType.invoke(namemod);
                    Method getFormat = namemod.getClass().getMethod("getFormat");

                    if (modType.toString().contains("PREFIX")) { modName.addPrefix((String) getFormat.invoke(namemod)); }
                    if (modType.toString().contains("SUFFIX")) { modName.addSuffix((String) getFormat.invoke(namemod)); }

                    hist.registerModifierBonus(modUUID, modName);
                }
            } catch (Exception ignored) { }

            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Applied \u00a7b" + getModifierId(modifier) + "\u00a77 to " + OotilityCeption.GetItemName(mmo) + "\u00a77. ");

        } else if (clear) {
            for (StatHistory hist : live.getStatHistories()) {
                hist.clearModifiersBonus();
            }
            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Cleared modifiers of " + OotilityCeption.GetItemName(mmo));

        } else {
            Object modifier = modifierLocal ? getTemplateModifier(template, rawModifier) : getGlobalModifier(rawModifier);

            UUID modUUID = UUID.randomUUID();
            MMOItemBuilder freshBuilder = template.newBuilder();

            // Apply modifier via reflection
            try {
                Method apply = modifier.getClass().getMethod("whenCollected", MMOItemBuilder.class, UUID.class);
                apply.invoke(modifier, freshBuilder, modUUID);
            } catch (Exception ignored) {
                try {
                    Method collect = modifier.getClass().getMethod("collect", MMOItemBuilder.class);
                    collect.invoke(modifier, freshBuilder);
                } catch (Exception ignored2) { }
            }

            // Rename
            try {
                Method hasNameMod = modifier.getClass().getMethod("hasNameModifier");
                if ((boolean) hasNameMod.invoke(modifier)) {
                    Method getNameMod = modifier.getClass().getMethod("getNameModifier");
                    Object namemod = getNameMod.invoke(modifier);

                    StatHistory hist = StatHistory.from(live, ItemStats.NAME);
                    NameData modName = new NameData("");

                    Method getType = namemod.getClass().getMethod("getType");
                    Object modType = getType.invoke(namemod);
                    Method getFormat = namemod.getClass().getMethod("getFormat");

                    if (modType.toString().contains("PREFIX")) { modName.addPrefix((String) getFormat.invoke(namemod)); }
                    if (modType.toString().contains("SUFFIX")) { modName.addSuffix((String) getFormat.invoke(namemod)); }

                    hist.registerModifierBonus(modUUID, modName);
                }
            } catch (Exception ignored) { }

            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Applied \u00a7b" + getModifierId(modifier) + "\u00a77 to " + OotilityCeption.GetItemName(mmo));
        }

        return live.newBuilder().build();
    }

    @Nullable private static String getModifierId(Object mod) {
        if (mod == null) return null;
        try {
            Method getId = mod.getClass().getMethod("getId");
            return (String) getId.invoke(mod);
        } catch (Exception e) {
            return mod.toString();
        }
    }

    /** Reflection-safe getter for MMOItems templates manager. */
    private static Object getTemplatesReflect() throws Exception {
        java.lang.reflect.Field pluginField = MMOItems.class.getField("plugin");
        Object plugin = pluginField.get(null);
        return plugin.getClass().getMethod("getTemplates").invoke(plugin);
    }
}
