package gunging.ootilities.gunging_ootilities_plugin.compatibilities.versions.mmotimelesscompatclutter;

import gunging.ootilities.gunging_ootilities_plugin.Gunging_Ootilities_Plugin;
import gunging.ootilities.gunging_ootilities_plugin.OotilityCeption;
import gunging.ootilities.gunging_ootilities_plugin.compatibilities.GooPMMOItems;
import gunging.ootilities.gunging_ootilities_plugin.misc.RefSimulator;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.build.MMOItemBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;

/**
 * MMOItems ModifierNode operations - adapted for MMOItems 6.10.1+
 * Uses reflection to avoid compile-time classpath issues with MMOPlugin.
 */
@SuppressWarnings({"unchecked", "rawtypes", "ConstantConditions"})
public class GooPMMOItemsModifierNodesOps {

    @Nullable
    public static ArrayList<String> getGlobalModifierNames() {
        ArrayList<String> ret = new ArrayList<>();
        try {
            Object templates = getTemplates();
            Method getModNodes = templates.getClass().getMethod("getModifierNodes");
            Object modNodes = getModNodes.invoke(templates);
            if (modNodes instanceof Collection) {
                for (Object mod : (Collection) modNodes) {
                    Method getId = mod.getClass().getMethod("getId");
                    ret.add((String) getId.invoke(mod));
                }
            }
        } catch (Exception ignored) { }
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

        try {
            Object templates = getTemplates();
            Method getTmpl = templates.getClass().getMethod("getTemplate", io.lumine.mythic.lib.api.item.NBTItem.class);
            Object templateObj = getTmpl.invoke(templates, NBTItem.get(mmo));

            if (templateObj == null && !useGlobl) {
                OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "This " + OotilityCeption.GetItemName(mmo) + " is an MMOItem but the template is not loaded, was it deleted? GooP cannot find its modifiers, enable \u00a7buse-global\u00a77 to use this command with this.");
                return null;
            }

            boolean dummyTemplate = (templateObj == null);
            if (templateObj == null) {
                templateObj = new MMOItemTemplate(net.Indyuce.mmoitems.api.Type.TOOL, "HX_M\u00d1P");
            }

            boolean random = rawModifier.equalsIgnoreCase("random");
            boolean clear = rawModifier.equalsIgnoreCase("none");

            Method hasMod = templateObj.getClass().getMethod("hasModifier", String.class);
            Method getModFromTmpl = templateObj.getClass().getMethod("getModifier", String.class);
            Method hasModGlob = templates.getClass().getMethod("hasModifier", String.class);
            Method getModFromGlob = templates.getClass().getMethod("getModifier", String.class);

            boolean modifierLocal = (boolean) hasMod.invoke(templateObj, rawModifier);
            boolean modifierExists = modifierLocal || (useGlobl && (boolean) hasModGlob.invoke(templates, rawModifier));

            if (!random && !clear && !modifierExists) {
                OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "There is no modifier of name \u00a73" + rawModifier + "\u00a77. ");
                return null;
            }

            NBTItem nbt = NBTItem.get(mmo);
            LiveMMOItem live = new LiveMMOItem(nbt);

            if (random) {
                // Get template modifiers
                Method getModsTmpl = templateObj.getClass().getMethod("getModifiers");
                Object modMap = getModsTmpl.invoke(templateObj);
                List<Object> modifiers = new ArrayList<>();
                if (modMap instanceof Map) {
                    modifiers.addAll(((Map<String, Object>) modMap).values());
                }

                if (useGlobl) {
                    Method getModNodes = templates.getClass().getMethod("getModifierNodes");
                    Object globMods = getModNodes.invoke(templates);
                    if (globMods instanceof Collection) {
                        modifiers.addAll((Collection) globMods);
                    }
                }

                if (modifiers.isEmpty()) {
                    OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback,
                            dummyTemplate ? "There are\u00a7c no global modifiers\u00a77, cant pick a random one thus." :
                                    "MMOItem has\u00a7c no modifiers\u00a77, cant pick a random one thus.");
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
                        OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Could\u00a7c not roll\u00a77 for any modifier after \u00a7b" + breaker + "\u00a77 iterations.");
                        return null;
                    }
                } else {
                    modifier = modifiers.get(OotilityCeption.GetRandomInt(0, modifiers.size() - 1));
                }

                applyModifier(templateObj, modifier, live, logger);

            } else if (clear) {
                for (net.Indyuce.mmoitems.stat.type.StatHistory hist : live.getStatHistories()) {
                    hist.clearModifiersBonus();
                }
                OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Cleared modifiers of " + OotilityCeption.GetItemName(mmo));

            } else {
                Object modifier = modifierLocal ? getModFromTmpl.invoke(templateObj, rawModifier) : getModFromGlob.invoke(templates, rawModifier);
                applyModifier(templateObj, modifier, live, logger);
            }

            Method newBuilder = live.getClass().getMethod("newBuilder");
            Object builder = newBuilder.invoke(live);
            Method build = builder.getClass().getMethod("build");
            return (ItemStack) build.invoke(builder);

        } catch (Exception e) {
            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Error during modifier operation: " + e.getMessage());
            return mmo;
        }
    }

    private static void applyModifier(Object templateObj, Object modifier, LiveMMOItem live, @Nullable RefSimulator<String> logger) {
        if (modifier == null) return;
        try {
            UUID modUUID = UUID.randomUUID();
            Method newBuilder = templateObj.getClass().getMethod("newBuilder");
            Object freshBuilder = newBuilder.invoke(templateObj);

            // Try whenCollected (newer API) or collect (older API)
            try {
                Method whenCollected = modifier.getClass().getMethod("whenCollected", MMOItemBuilder.class, UUID.class);
                whenCollected.invoke(modifier, freshBuilder, modUUID);
            } catch (NoSuchMethodException e1) {
                try {
                    Method collect = modifier.getClass().getMethod("collect", MMOItemBuilder.class);
                    collect.invoke(modifier, freshBuilder);
                } catch (NoSuchMethodException e2) { }
            }

            // Name modifier
            try {
                Method hasNameMod = modifier.getClass().getMethod("hasNameModifier");
                if ((boolean) hasNameMod.invoke(modifier)) {
                    Method getNameMod = modifier.getClass().getMethod("getNameModifier");
                    Object namemod = getNameMod.invoke(modifier);

                    net.Indyuce.mmoitems.stat.type.StatHistory hist = net.Indyuce.mmoitems.stat.type.StatHistory.from(live, net.Indyuce.mmoitems.ItemStats.NAME);
                    net.Indyuce.mmoitems.stat.type.NameData modName = new net.Indyuce.mmoitems.stat.type.NameData("");

                    Method getType = namemod.getClass().getMethod("getType");
                    String modType = getType.invoke(namemod).toString();
                    Method getFormat = namemod.getClass().getMethod("getFormat");

                    if (modType.contains("PREFIX")) modName.addPrefix((String) getFormat.invoke(namemod));
                    if (modType.contains("SUFFIX")) modName.addSuffix((String) getFormat.invoke(namemod));

                    hist.registerModifierBonus(modUUID, modName);
                }
            } catch (Exception ignored) { }

            // Get modifier ID for logging
            String modId = "unknown";
            try {
                Method getId = modifier.getClass().getMethod("getId");
                modId = (String) getId.invoke(modifier);
            } catch (Exception ignored) { }

            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Applied \u00a7b" + modId + "\u00a77. ");

        } catch (Exception e) {
            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Failed to apply modifier: " + e.getMessage());
        }
    }

    /** Reflection-safe getter for MMOItems templates manager. */
    private static Object getTemplates() throws Exception {
        java.lang.reflect.Field pluginField = MMOItems.class.getField("plugin");
        Object plugin = pluginField.get(null);
        return plugin.getClass().getMethod("getTemplates").invoke(plugin);
    }
}
