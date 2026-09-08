package gunging.ootilities.gunging_ootilities_plugin.compatibilities;

import com.google.common.collect.Multimap;
import gunging.ootilities.gunging_ootilities_plugin.Gunging_Ootilities_Plugin;
import gunging.ootilities.gunging_ootilities_plugin.OotilityCeption;
import gunging.ootilities.gunging_ootilities_plugin.compatibilities.versions.*;
import gunging.ootilities.gunging_ootilities_plugin.compatibilities.versions.mmotimelesscompatclutter.GooPMMOItemsModifierNodesOps;
import gunging.ootilities.gunging_ootilities_plugin.compatibilities.versions.mmotimelesscompatclutter.GooPMMOItemsTemplateModifierOps;
import gunging.ootilities.gunging_ootilities_plugin.containers.compatibilities.ContainerToMIInventory;
import gunging.ootilities.gunging_ootilities_plugin.events.ScoreboardLinks;
import gunging.ootilities.gunging_ootilities_plugin.misc.*;
import gunging.ootilities.gunging_ootilities_plugin.misc.goop.TargetedItems;
import gunging.ootilities.gunging_ootilities_plugin.misc.mmoitemstats.*;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ConfigFile;
import net.Indyuce.mmoitems.api.ItemTier;
import net.Indyuce.mmoitems.api.ReforgeOptions;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.interaction.Consumable;
import net.Indyuce.mmoitems.api.interaction.GemStone;
import net.Indyuce.mmoitems.api.interaction.UseItem;
import org.bukkit.inventory.EquipmentSlot;
import net.Indyuce.mmoitems.api.interaction.util.DurabilityItem;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.api.item.mmoitem.VolatileMMOItem;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.api.item.util.identify.IdentifiedItem;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import net.Indyuce.mmoitems.api.player.inventory.EquippedItem;
import net.Indyuce.mmoitems.api.util.MMOItemReforger;
import net.Indyuce.mmoitems.manager.TypeManager;
import net.Indyuce.mmoitems.skill.RegisteredSkill;
import net.Indyuce.mmoitems.stat.data.*;
import net.Indyuce.mmoitems.stat.data.random.RandomStatData;
import net.Indyuce.mmoitems.stat.data.type.Mergeable;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import net.Indyuce.mmoitems.stat.type.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("unused")
public class GooPMMOItems {

    static ConfigurationSection dummySectionReal = null;

    /**
     * Somewhy, MMO loves using Configuration Sections in constructors which it
     * reads directly from files. Then, constructing these classes through API
     * requires the roundabout way of putting that information in a configuration
     * section first, and then creating the object you want:
     *
     * <p></p>
     * <code>
     * boolean shouldDoSomething = true;
     * <p>
     * ConfigurationSection section;
     * section.setBoolean("something-enabled", shouldDoSomething);
     * <p>
     * SomethingOptions options = new SomethingOptions(section);
     * </code>
     * <p></p>
     *
     * It is annoying to create dummy Configuration Sections, so GooP uses its
     * own config and this is just an easy-access method.
     *
     * @param section The section, kindly provided by GooP during loading
     */
    public static void DefineSillyConfigurationSectionForMMOCompat(@NotNull ConfigurationSection section) { dummySectionReal = section; }

    public boolean CompatibilityCheck() {

        // You got that on you?
        try {
            Object plugin = MMOItems.class.getField("plugin").get(null);
            Object types = plugin.getClass().getMethod("getTypes").invoke(plugin);
            types.getClass().getMethod("get", Object.class).invoke(types, (Object) null);
        } catch (Exception ignored) { }

        /*
         * Do not erase in the future.
         *
         * Registers the container equipment slots, I guess at one
         * point this measured that MMOItems was up-to-date and that
         * is how this method ended up here.
         */
        return RegisterContainersEquipment();
    }

    public static void ReflectionOnLoad() {
        try {
            usingModifierNodes = false;
            // In MMOItems 6.10.1, TemplateModifier was removed - check via reflection
            try { Class.forName("net.Indyuce.mmoitems.api.item.template.TemplateModifier"); }
            catch (ClassNotFoundException e) { throw new ClassNotFoundException("Skibidi"); }
            if (!GooPMMOItemsTemplateModifierOps.ReflectMethods()) { usingModifierNodes = null; }
        } catch (ClassNotFoundException|NoClassDefFoundError ignored) {
            usingModifierNodes = true;
        }
    }

    static boolean rced = false;
    public static boolean RegisterContainersEquipment() {
        if (rced) { return Gunging_Ootilities_Plugin.foundMMOItems; }
        if(MMOItems.plugin == null) { return false; }
        // Use reflection for MMOItems version compatibility
        try {
            Method regMethod = MMOItems.class.getMethod("registerPlayerInventory", Class.forName("net.Indyuce.mmoitems.comp.inventory.PlayerInventory"));
            regMethod.invoke(MMOItems.plugin, new ContainerToMIInventory());
        } catch (Exception ignored) {
            // MMOItems 6.10.1+ removed registerPlayerInventory
        }
        //DBG//OotilityCeption.Log("Registerer Containers MIInventory");
        rced = true;
        return true;
    }

    @NotNull public static LiveMMOItem LiveFromNBT(@NotNull NBTItem nbt) {

        // Only if MMOItems gets disabled would this not work
        try {

            // Create new :wazowskibruhmoment:
            return new LiveMMOItem(nbt);

        // Honestly what could possibly go wrong
        } catch (Exception e) {

            Gunging_Ootilities_Plugin.theOots.CPLog("Something weird happened when trying to read a MMOItem, §cis MMOItems working correctly?");
            e.printStackTrace();
            //noinspection ConstantConditions
            return null;
        }
    }

    @NotNull public static VolatileMMOItem VolatileFromNBT(@NotNull NBTItem nbt) {
        // Only if MMOItems gets disabled would this not work
        try {
            // Create new :wazowskibruhmoment:
            return new VolatileMMOItem(nbt);

        // Honestly what could possibly go wrong
        } catch (Exception e) {

            Gunging_Ootilities_Plugin.theOots.CPLog("Something weird happened when trying to read a MMOItem, §cis MMOItems working correctly?");
            e.printStackTrace();
            //noinspection ConstantConditions
            return null;
        }
    }

    //region Easy Get of MMOItems as ItemStacks
    /**
     * Gets an MMOItem of such Type and ID if it exists.
     * @return Structure Void Block saying 'Invalid MMOItem' if it doesnt exist.
     */
    @NotNull
    public static ItemStack GetMMOItemOrDefault(@Nullable String type, @Nullable  String id) {

        // Try to get
        ItemStack res = GetMMOItem(type, id);

        // Return 'Default'
        if (res == null) { res = OotilityCeption.RenameItem(new ItemStack(Material.STRUCTURE_VOID), "§cInvalid MMOItem§e " + type + " " + id, null); }

        //DBG*/Gunging_Ootilities_Plugin.theOots.CLog("Providing " + OotilityCeption.GetItemName(res));
        //noinspection ConstantConditions
        return res;
    }

    /**
     * Gets an MMOItem of such Type and ID if it exists.
     * @return NULL If it doesnt exist.
     */
    @Nullable
    public static ItemStack GetMMOItem(@Nullable String type, @Nullable String id) {
        try {
            Object plugin = MMOItems.class.getField("plugin").get(null);
            return (ItemStack) plugin.getClass().getMethod("getItem", String.class, String.class).invoke(plugin, type, id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * The MMOItems reforger allows to update MMOItems in several levels:   <br>
     * + Completely Replace         <br>
     * + Reforge (reroll RNG)       <br>
     * + Update (keep previous RNG) <br>
     *  <p></p>
     * One may also specify which things to keep :)
     *
     * @param item ItemStack before knowing if it even is an MMOItem.
     *
     * @param regenParams In the following order, which things to keep: <br>
     *                    <p><code>(no params)</code> Nothing, regenerate completely.
     *                    </p><code>0</code> Keep Name?
     *                    <p><code>1</code> Keep Lore?
     *                    </p><code>2</code> Keep Enchantments?
     *                    <p><code>3</code> Keep Upgrades?
     *                    </p><code>4</code> Keep Gemstones?
     *                    <p><code>5</code> Keep Soulbound?
     *                    </p><code>6</code> Keep External SH?
     *                    </p><code>7</code> Keep RNG Rolls?
     *                    </p><code>8</code> Keep Modifiers?
     *                    </p><code>9</code> Keep AEnchantments?
     *
     * @return A regenerated ItemStack if successful.
     */
    @Nullable public static ItemStack ReforgeMMOItem(@NotNull ItemStack item, boolean... regenParams) { return ReforgeMMOItem(item, null, regenParams); }
    @Nullable public static ItemStack ReforgeMMOItem(@NotNull ItemStack item, @Nullable RefSimulator<String> logAddition, boolean... regenParams) {

        // Create and use reforger
        /*CURRENT-MMOITEMS*/MMOItemReforger mod = new MMOItemReforger(item);

        // Valid?
        /*CURRENT-MMOITEMS*/if (!mod.canReforge()) {
        /*CURRENT-MMOITEMS*/    OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Item " + OotilityCeption.GetItemName(item) + "§7 is §cnot§7 reforgeable. ");
        /*CURRENT-MMOITEMS*/    return null; }

        ReforgeOptions reforgeOptionsBuild = null;

        Boolean mmoitems_6_10_1 = null;
        if (!OotilityCeption.If(mmoitems_6_10_1)) {
            try {

                // Build using the good old boolean array (reflection for API compat)
                Constructor<ReforgeOptions> oldCtor = ReforgeOptions.class.getConstructor(boolean[].class);
                reforgeOptionsBuild = oldCtor.newInstance(new Object[]{regenParams});

                mmoitems_6_10_1 = false;
            } catch (Exception ignored) {
                mmoitems_6_10_1 = true;
            }
        }
        if (OotilityCeption.If(mmoitems_6_10_1)) {
            ConfigurationSection section = dummySectionReal;

            // Order of boolean values:
            // finalName, finalLore, finalEnch, finalUpgr, finalGems, finalSoul, finalExsh, finalReroll, finalMods, finalAe, finalSkin

            dummySectionReal.set("display-name", regenParams[0]);
            dummySectionReal.set("lore", regenParams[1]);
            dummySectionReal.set("enchantments", regenParams[2]);
            dummySectionReal.set("upgrades", regenParams[3]);
            dummySectionReal.set("gemstones", regenParams[4]);
            dummySectionReal.set("soulbound", regenParams[5]);
            dummySectionReal.set("external-sh", regenParams[6]);
            dummySectionReal.set("reroll", regenParams[7]);
            dummySectionReal.set("modifications", regenParams[8]);
            dummySectionReal.set("advanced-enchantments", regenParams[9]);
            dummySectionReal.set("skins", regenParams[10]);
            reforgeOptionsBuild = new ReforgeOptions(dummySectionReal);
        }

        // Proc
        /*CURRENT-MMOITEMS*/if (!mod.reforge(reforgeOptionsBuild)) {
        /*CURRENT-MMOITEMS*/    OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Item " + OotilityCeption.GetItemName(item) + "§7 could §cnot§7 be reforged. ");
        /*CURRENT-MMOITEMS*/    return null; }

        // Notify
        /*CURRENT-MMOITEMS*/OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Item " + OotilityCeption.GetItemName(item) + "§7 was §areforged§7. ");

        // Output
        /*CURRENT-MMOITEMS*/return mod.getResult();

        //YE-OLDEN-MMO//if (!IsMMOItem(item)) { return null; }
        //YE-OLDEN-MMO//MMOItemReforger mod = new MMOItemReforger(NBTItem.get(item));

        //  0       ,       1   ,       2   ,       3   ,       4   ,       5   ,       6   ,       7    ,      8   ,       9,      10
        //finalName , finalLore , finalEnch , finalUpgr , finalGems , finalSoul , finalExsh , finalReroll, finalMods, finalAe, finalSkin
        //YE-OLDEN-MMO//boolean keptOne = false; for (int i = 0; i < 6; i++) { if (regenParams[i]) { keptOne = true; break; } }
        //YE-OLDEN-MMO//ReforgeOptions revisions = new ReforgeOptions(regenParams[0], regenParams[1], regenParams[2], regenParams[3], regenParams[4], regenParams[5], regenParams[6], !keptOne);
        //YE-OLDEN-MMO//if (regenParams[7]) { mod.reforge((Player) null, revisions); } else { mod.update((Player) null, revisions); }

        //YE-OLDEN-MMO//if (!mod.hasChanges()) { return null; } else { return mod.toStack(); }
    }

    /**
     * Gets a MMOItem as the actual class itself from the plugin
     * (while other methods return it as a built ItemStack).
     */
    @Nullable public static MMOItem GetMMOItemRaw(@NotNull String type, @NotNull String id) {
        //noinspection ConstantConditions
        return MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(type), id);
    }

    /**
     * Just returns the MMOItem type, if it exists.
     * @return NULL if it doesnt
     */
    @Nullable public static Type GetMMOItemTypeFromString(@Nullable String type) {
        return MMOItems.plugin.getTypes().get(type);
    }

    public static boolean IsConsumable(@Nullable ItemStack consumable, @NotNull Player player) {
        if (consumable == null) { return false; }
        if (!IsMMOItem(consumable)) { return false; }
        return IsConsumable(NBTItem.get(consumable), player);
    }

    public static boolean IsConsumable(@NotNull NBTItem consumable, @NotNull Player player) {

        // Decrease usage if consumable
        return (UseItem.getItem(player, consumable, GetMMOItemType(consumable))) instanceof Consumable;
    }

    public static boolean IsGemstone(@Nullable ItemStack gemstone, @NotNull Player player) {
        if (gemstone == null) { return false; }
        if (!IsMMOItem(gemstone)) { return false; }
        return IsGemstone(NBTItem.get(gemstone), player);
    }

    public static boolean IsGemstone(@NotNull NBTItem gemstone, @NotNull Player player) {

        // Decrease usage if consumable
        return (UseItem.getItem(player, gemstone, GetMMOItemType(gemstone))) instanceof GemStone;
    }

    @NotNull
    public static UseItem GetUseItem(@NotNull NBTItem itm, @NotNull Player player) {

        // Decrease usage if consumable
        return UseItem.getItem(player, itm, GetMMOItemType(itm));
    }
    //endregion

    //region Stat Registry

    public static ItemStat XBOW_LOADED_STAT;
    public static ItemStat GROUND_POUND_STAT;
    public static ItemStat LUCK;
    public static ItemStat MINIONS;
    public static ItemStat APPLICABLE_COMMAND;
    public static ItemStat APPLICABLE_MASK;
    public static ItemStat APPLICABLE_CONSUME;
    public static ItemStat APPLICABLE_LIMIT;
    public static ItemStat APPLICABLE_CLASS;
    public static ItemStat APPLICABLE_TIMES;
    public static ItemStat HAT;
    public static ItemStat REVARIABLE;
    public static ItemStat CONTAINER;
    public static ItemStat ONKILL_COMMAND;
    public static ItemStat ONHIT_COMMAND;

    public static HashMap<String, ItemStat> STR_MISC = new HashMap<>();
    static Material[] stringMats = new Material[]{Material.PAPER, Material.MAP, Material.FILLED_MAP, Material.NAME_TAG, Material.BOOK, Material.WRITTEN_BOOK, Material.WRITABLE_BOOK, Material.KNOWLEDGE_BOOK};

    public static HashMap<String, ItemStat> MISC = new HashMap<>();
    static Material[] miscMats = new Material[]{Material.PUFFERFISH_BUCKET, Material.TROPICAL_FISH_BUCKET, Material.SALMON_BUCKET, Material.COD_BUCKET, Material.WATER_BUCKET, Material.MILK_BUCKET, Material.LAVA_BUCKET, Material.BUCKET};

    public static HashMap<String, ItemStat> RST_MISC = new HashMap<>();
    static Material[] restMats = new Material[]{Material.RED_CONCRETE, Material.RED_CONCRETE_POWDER, Material.RED_GLAZED_TERRACOTTA, Material.RED_STAINED_GLASS, Material.RED_TERRACOTTA};

    static String[] miscOrder = new String[]{ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
    @Nullable public static ItemStat GetMiscStat(@NotNull String key) {

        // Convert to nomenklature
        String k = key.toUpperCase(); boolean asSTR = false, asRST = false;
        if (k.startsWith("MISC")) { k = k.substring("MISC".length()); }
        if (k.startsWith(strPrefix)) { asSTR = true; k = k.substring(strPrefix.length()); }
        if (k.startsWith(rstPrefix)) { asRST = true; k = k.substring(rstPrefix.length()); }
        if (k.startsWith(miscPrefix)) { k = k.substring(miscPrefix.length()); }
        if (k.startsWith("STR_")) { asSTR = true; k = k.substring("STR_".length()); }
        if (k.startsWith("RST_")) { asRST = true; k = k.substring("RST_".length()); }

        // Find
        return asSTR ? STR_MISC.get(k) : asRST ? RST_MISC.get(k) : MISC.get(k);
    }

    static int currentMisc = 0, generation = 0, currentMat = 0;
    static final String miscPrefix = "GOOP_MISC_";
    static final String strPrefix = "GOOP_MISC_STR_";
    static final String rstPrefix = "GOOP_MISC_RST_";
    public static void RegisterMiscStat() {

        // Get Name
        StringBuilder mOrder = new StringBuilder(miscPrefix);
        mOrder.append(miscOrder[currentMisc]);
        if (generation > 0) { mOrder.append(generation); }
        currentMisc++; if (currentMisc >= miscOrder.length) { currentMisc = 0; generation++; }
        String name = mOrder.toString();
        String terminology = name.substring(miscPrefix.length());

        // Get Material
        Material mMat = miscMats[currentMat];
        currentMat++; if (currentMat >= miscMats.length) { currentMat = 0; }

        ItemStat MISCC = new DoubleStat(name, mMat, "Extra Stat §l" + terminology, new String[]{"Doesnt do anything by itself.", "§a", "You can retrieve it in mythic", "skills though, using these:", "§e<goop.castermmostat.misc" + terminology.toLowerCase() + ">", "§e<goop.triggermmostat.misc" + terminology.toLowerCase() + ">", "", "or use in PlaceholderAPI with", "§3%mmoitems_stat_goop_misc_" + terminology.toLowerCase() + "%", "", ""}, new String[]{"!consumable", "!miscellaneous", "all"});
        RegisterStat(name, MISCC);
        MISC.put(terminology, MISCC);
    }

    public static void RegisterStrMiscStat() {

        // Get Name
        StringBuilder mOrder = new StringBuilder(strPrefix);
        mOrder.append(miscOrder[currentMisc]);
        if (generation > 0) { mOrder.append(generation); }
        currentMisc++; if (currentMisc >= miscOrder.length) { currentMisc = 0; generation++; }
        String name = mOrder.toString();
        String terminology = name.substring(strPrefix.length());

        // Get Material
        Material mMat = stringMats[currentMat];
        currentMat++; if (currentMat >= stringMats.length) { currentMat = 0; }

        ItemStat MISCC = new StringStat(name, mMat, "Extra String Stat §l" + terminology, new String[]{"Doesnt do anything by itself.", "§a", "You can retrieve it in mythic", "skills though, using these:", "§e<goop.castermmostat.[slot]." + name.toUpperCase() + ">", "§e<goop.triggermmostat.[slot]." + name.toUpperCase() + ">"}, new String[]{ "all" });
        RegisterStat(name, MISCC);
        STR_MISC.put(terminology, MISCC);
    }

    public static void RegisterRstMiscStat(@Nullable String rst) {
        if (rst == null) { Gunging_Ootilities_Plugin.theOots.CLog(OotilityCeption.LogFormat("MMOItems - Misc Restriction Stat", "Null entry in §econfig.yml§7 list. ")); return; }

        // Format is "[%YES% ... %HO% %HE%] 1..3"
        String[] rstSplit;
        if (rst.contains(" ")) { rstSplit = rst.split(" "); }
        else { rstSplit = new String[] { rst }; }

        // Parse the last
        QuickNumberRange qnr = QuickNumberRange.FromString(rstSplit[rstSplit.length - 1]);
        if (qnr == null) { Gunging_Ootilities_Plugin.theOots.CLog(OotilityCeption.LogFormat("MMOItems - Misc Restriction Stat", "Invalid number range '§c" + rst + "§7' specified in §econfig.yml§7 list. It must always end with a number range in the format §b4..28§7 or whatever. ")); return; }

        ArrayList<String> px = new ArrayList<>();
        px.addAll(Arrays.asList(rstSplit).subList(0, rstSplit.length - 1));

        // Get Name
        StringBuilder mOrder = new StringBuilder(rstPrefix);
        mOrder.append(miscOrder[currentMisc]);
        if (generation > 0) { mOrder.append(generation); }
        currentMisc++; if (currentMisc >= miscOrder.length) { currentMisc = 0; generation++; }
        String name = mOrder.toString();
        String terminology = name.substring(rstPrefix.length());

        // Get Material
        Material mMat = restMats[currentMat];
        currentMat++; if (currentMat >= restMats.length) { currentMat = 0; }

        ItemStat MISCC = new MiscRestrictionStat(name, mMat, "Extra Restriction Stat §l" + terminology, new String[]{"Specify in the §econfig.yml§7 the", "number range which this stat", "allows the item to be used. ", "§3", "Allows use at range§e " + qnr, "Other config additions: §bx" + (rstSplit.length - 1), "§8Changes require a server reboot. " }, new String[]{"!consumable", "!miscellaneous", "all"}, qnr, px);
        RegisterStat(name, MISCC);
        RST_MISC.put(terminology, MISCC);
    }

    public static void ReloadMiscStatLore() {

        // Yes
        for (ItemStat misc : MISC.values()) {

            // Get its translation
            String translation = MMOItems.plugin.getLanguage().getStatFormat(misc.getPath());

            // Last lore line
            misc.getLore()[misc.getLore().length - 1] = translation;
        }
    }

    public static void RegisterCustomStats(int miscAmount, int miscStrAmount, @NotNull List<String> miscRstAmount) {
        if (GooP_MinecraftVersions.GetMinecraftVersion() >= 14.0) {
            XBOW_LOADED_STAT = new XBow_Loaded_Stat();
            RegisterStat(XBOW_LOADED_STAT); }

        LUCK = new LuckStat();
        RegisterStat(LUCK);

        HAT = new BooleanStat("GOOP_HAT", Material.CHAINMAIL_HELMET, "Hat / Helmet", new String[]{"This item will automatically equip", "in the head slot if:", "§a + §7It is Shift+LeftClicked in inventory", "§a + §7It is placed on the helmet slot.", "", "This will not:", "§c - §7Prevent blocks from being placed (disable interactions for this)", "§c - §7Equip it to the head if the item is right-clicked while held."}, new String[]{"all"});
        RegisterStat(HAT);

        currentMisc = 0; generation = 0; currentMat = 0;
        for (int m = 1; m <= miscAmount; m++) { RegisterMiscStat(); }
        currentMisc = 0; generation = 0; currentMat = 0;
        for (int m = 1; m <= miscStrAmount; m++) { RegisterStrMiscStat(); }
        currentMisc = 0; generation = 0; currentMat = 0;
        for (int m = 1; m <= miscRstAmount.size(); m++) { RegisterRstMiscStat(miscRstAmount.get(m - 1)); }

        GROUND_POUND_STAT = new StringStat("GROUND_POUND_SKILL", Material.COARSE_DIRT, "Ground Pound Skill", new String[]{"When the item is dropped to the ground,", "what mythicmob skill to run when", "it hits the ground?", "", "§6Player that dropped the item: §f@Self", "§6Item Entity Itself: §e@Trigger"}, new String[]{"all"});
        RegisterStat(GROUND_POUND_STAT);

        if (Gunging_Ootilities_Plugin.foundMythicMobs) {
            MINIONS = new DoubleStat("GOOP_MINIONS", Material.TOTEM_OF_UNDYING, "Max Minions", new String[]{"The max number of entities that this player", "can have as minions, will kill the oldest", "one if this is exceeded and a new spawns.", "", "§e/goop mythicmobs minion"}, new String[]{"!consumable", "!miscellaneous", "all"});
            RegisterStat(MINIONS);
        }

        ONKILL_COMMAND = new StringStat("GOOP_MM_KILL", Material.BONE, "On Kill Command", new String[]{"Run a command when murdering something", "while having this item equipped.", "", "§3Maybe run a mythic skill?", "§b/goop mythicmobs runSkillAs <SKILL> %player% @%victim%"}, new String[]{"!consumable", "!miscellaneous", "!skin", "all", "!skin", "!miscellaneous", "!consumable" });
        RegisterStat(ONKILL_COMMAND);

        ONHIT_COMMAND = new StringStat("GOOP_MM_HIT", Material.STONE_SWORD, "On Hit Command", new String[]{"Run a command when damaging something", "while having this item equipped.", "", "§3Maybe run a mythic skill?", "§b/goop mythicmobs runSkillAs <SKILL> %player% @%victim%"}, new String[]{"!consumable", "!miscellaneous", "!skin", "all", "!skin", "!miscellaneous", "!consumable" });
        RegisterStat(ONHIT_COMMAND);

        APPLICABLE_COMMAND = new StringStatProper("GOOP_APPLY_COMMAND", Material.FILLED_MAP, "GooP OnApply Command", new String[]{"What command is run when this item", "is applied on other items?", "§a", "§aRestrict which types this can be", "§aapplied to with an apply mask.", "§a", "§3Supports GooP commands by choosing slot", "§3as §a%player% %provided-slot%§3;", "§b§lPAPI supported."}, new String[]{"all"});
        RegisterStat(APPLICABLE_COMMAND);

        APPLICABLE_MASK = new StringStatProper("GOOP_APPLY_MASK", Material.FILLED_MAP, "GooP OnApply Mask", new String[]{"Define Masks in §bonapply-masks.yml", "§a", "Restricts to which MMOItem types", "this item can be applied to IF", "it has an OnApply Command."}, new String[]{"!gem_stone", "all", "!gem_stone"});
        RegisterStat(APPLICABLE_MASK);

        APPLICABLE_CONSUME = new BooleanStat("GOOP_APPLY_CONSUME", Material.PAPER, "Not Consume on GooP OnApply", new String[]{"Consumables are consumed when using their OnApply.", "This will prevent them from doing so."}, new String[]{"consumable"});
        RegisterStat(APPLICABLE_CONSUME);

        APPLICABLE_CLASS = new StringStatProper("GOOP_APPLY_CLASS", Material.MAP, "GooP OnApply Reference", new String[]{"Used to limit how many times the", "OnApply Command can run on the", "same item.", "", "§bIf this is not specified, but a limit is,", "§bthe limit will apply to only this MMOItem."}, new String[]{"all"});
        RegisterStat(APPLICABLE_CLASS);

        APPLICABLE_LIMIT = new DoubleStatProper("GOOP_APPLY_LIMIT", Material.MAP, "GooP OnApply Limit", new String[]{"Used to limit how many times the", "OnApply Command can run on the", "same item.", "", "§3This is the number of times OnApply", "§3commands of the same §bReference§3 will", "§3act upon the same item."}, new String[]{"all"});
        RegisterStat(APPLICABLE_LIMIT);

        APPLICABLE_TIMES = new StringListStatProper("GOOP_APPLY_TIMES", Material.LIGHT_GRAY_STAINED_GLASS_PANE, "GooP OnApply Repetitions", new String[]{"Tech stat that keeps track of how", "many times an OnApply item has been", "used on this item.", "", "§6I guess you could use it to make", "§6an item think an OnApply has been used", "§6a few times on it already.", "", "§8Format: &n[reference] [number of times]", "§cEx: &nUpgradeRef 2"}, new String[]{"all"});
        RegisterStat(APPLICABLE_TIMES);

        CONTAINER = new StringStat("GOOP_CONTAINER", Material.CHEST, "GooP Container", new String[]{"This item will serve as a a bag!", "Right-Click to open as if it was a backpack", "", "§cThe container uuid will be saved in this item.", "§8Can only be opened as a single stack."}, new String[]{"!consumable", "!gem_stone", "!musket", "!bow", "!crossbow", "!lute", "!skin", "all", "!skin", "!consumable", "!gem_stone", "!musket", "!bow", "!crossbow", "!lute"});
        RegisterStat(CONTAINER);

        REVARIABLE = new StringStat("GOOP_REVARIABLE", Material.NAME_TAG, "Revariable", new String[]{"When a player puts this in the second", "anvil slot, and renames the item in", "the first anvil slot, this variable will", "be rewritten by the renaming operation.", "", "§cCase sensitive§7, no spaces, preferably alphanumeric.", "", "§cNot working with MMOItems anymore :c"}, new String[]{"miscellaneous", "consumable"});
        //RegisterStat(REVARIABLE);
    }

    public static void RegisterStat(@Deprecated String legacyID, @NotNull ItemStat statt) { RegisterStat(statt); }
    public static void RegisterStat(@NotNull ItemStat statt) {
        if (!Gunging_Ootilities_Plugin.foundMMOItems) { return; }
        if (MMOItems.plugin == null || MMOItems.plugin.getStats() == null) { Gunging_Ootilities_Plugin.foundMMOItems = false; return; }

        /*
         * When GooP gets reloaded via PlugMan (unload + load) without MMOItems
         * being reloaded, MMOItems' StatManager still holds an entry for this
         * stat id, and its register(...) throws on duplicate ids - which used to
         * kill the whole plugin load and disable every GooP command.
         * If it is already registered, reuse the existing instance instead; GooP
         * resolves stats by id through the registry anyway, so this is identical.
         */
        try {

            // Already there?
            if (MMOItems.plugin.getStats().get(statt.getId()) != null) { return; }

            // Register
            MMOItems.plugin.getStats().register(statt);

        } catch (IllegalArgumentException duplicate) {

            // Someone beat us to it (or the registry was in a weird state), fine.
        }
    }
    //endregion

    //region Getting Values (as well as the cummulative forms)
    static Method equipped = null;
    static Method getitem = null;
    static boolean usingEquippedPlayerItems = true;

    /**
     * Gets the MMOItems this player has equipped.
     */
    @NotNull public static ArrayList<VolatileMMOItem> GetPlayerEquipment(@NotNull Player player) {

        // Ret
        ArrayList<VolatileMMOItem> vot = new ArrayList<>();

        // Get Eq
        PlayerData p;
        try { p = PlayerData.get(player); } catch (NullPointerException ignored) {
            //EQP//OotilityCeption.Log("§8MMO§3 EQP§c Null Player Data");
            return vot; }

        // Find method
        if (equipped == null) {
            //EQP//OotilityCeption.Log("§8MMO§3 EQP§5 Finding Equipment Method...");

            // Reflection or not
            try {

                // Identify Method
                equipped = p.getInventory().getClass().getMethod("getEquipped");
                //EQP//OotilityCeption.Log("§8MMO§3 EQP§d Equipment Method Found");

            // lol no
            } catch (NoSuchMethodException ignored) {
                //EQP//OotilityCeption.Log("§8MMO§3 EQP§c No Equipment Method");
                return vot; }
        }

        try {

            // Attempt to invoek
            Object equippedItems = equipped.invoke(p.getInventory());

            // Using Equipped Player Items?
            if (usingEquippedPlayerItems) {
                //EQP//OotilityCeption.Log("§8MMO§3 EQP§5 Identifying Equipment Class...");

                try {

                    // We all know it is a list (real)
                    if (equippedItems instanceof List list) {

                        // List of something
                        for (int i = 0; i < list.size(); i++) {

                            // Get that item I hope
                            Object equippedPlayerItem = list.get(i);

                            // Find method
                            if (getitem == null) {

                                // Reflection or not
                                getitem = equippedPlayerItem.getClass().getMethod("getItem");
                                //EQP//OotilityCeption.Log("§8MMO§3 EQP§d Using EquippedPlayerItem.getItem()");
                            }

                            // That should be the result - might be NBTItem or VolatileMMOItem
                            Object itemResult = getitem.invoke(equippedPlayerItem);
                            if (itemResult instanceof VolatileMMOItem) {
                                vot.add((VolatileMMOItem) itemResult);
                            } else if (itemResult instanceof NBTItem) {
                                vot.add(VolatileFromNBT((NBTItem) itemResult));
                            }
                            //EQP//OotilityCeption.Log("§8MMO§3 EQP§a +§7 Added!");
                        }
                    } else if (equippedItems instanceof Collection collection) {

                        // Collection of something (e.g. HashSet from newer MMOItems)
                        for (Object equippedPlayerItem : collection) {

                            // Find method
                            if (getitem == null) {

                                // Reflection or not
                                getitem = equippedPlayerItem.getClass().getMethod("getItem");
                                //EQP//OotilityCeption.Log("§8MMO§3 EQP§d Using EquippedPlayerItem.getItem()");
                            }

                            // That should be the result - might be NBTItem or VolatileMMOItem
                            Object itemResult = getitem.invoke(equippedPlayerItem);
                            if (itemResult instanceof VolatileMMOItem) {
                                vot.add((VolatileMMOItem) itemResult);
                            } else if (itemResult instanceof NBTItem) {
                                vot.add(VolatileFromNBT((NBTItem) itemResult));
                            }
                            //EQP//OotilityCeption.Log("§8MMO§3 EQP§a +§7 Added!");
                        }
                    }

                    // Right....
                    return vot;

                } catch (NoClassDefFoundError|NoSuchMethodException ignored) {
                    //EQP//OotilityCeption.Log("§8MMO§3 EQP§d Equipped Item Method must be used");
                    usingEquippedPlayerItems = false; getitem = null; }
            }

            // Cast into list
            List<EquippedItem> list = (List<EquippedItem>) equippedItems;
            for (EquippedItem e : list) {

                // Find method
                if (getitem == null) {

                    // Reflection or not
                    try {

                        // Identify Method
                        getitem = e.getClass().getMethod("getNBT");
                        //EQP//OotilityCeption.Log("§8MMO§3 EQP§d Using EquippedItem.getNBT()");

                    // lol no
                    } catch (NoSuchMethodException ignored) {
                        //EQP//OotilityCeption.Log("§8MMO§3 EQP§c Could not find EquippedItem.getNBT()");
                        return vot; }
                }

                vot.add(new VolatileMMOItem((NBTItem) getitem.invoke(e)));
                //EQP//OotilityCeption.Log("§8MMO§3 EQP§a +§7 Added!");
            }

        } catch (InvocationTargetException|IllegalAccessException ignored) {

            //EQP//OotilityCeption.Log("§8MMO§3 EQP§c Could not identify Equipped Items method");
        }

        // Return thay
        return vot;
    }

    public static double CummaltiveEquipmentDoubleStatValue(@NotNull Player target, @NotNull String statname, double base) {
        Double ret = CummaltiveEquipmentDoubleStatValue(target, statname);
        if (ret == null) { ret = base; } else { ret += base; }
        return ret;
    }
    public static double CummaltiveEquipmentDoubleStatValue(@NotNull Player target, @Nullable ItemStat statname, double base) {
        Double ret = CummaltiveEquipmentDoubleStatValue(target, statname);
        if (ret == null) { ret = base; } else { ret += base; }
        return ret;
    }
    @Nullable public static Double CummaltiveEquipmentDoubleStatValue(@NotNull Player target, @NotNull String statname) {

        // Cut
        String bakedStatname = statname.toLowerCase();
        if (bakedStatname.startsWith("misc")) { bakedStatname = "misc"; }

        // Get Statt
        switch (bakedStatname) {
            case "attackdamage":
            case "adamage":
            case "admg":
            case "attack":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.ATTACK_DAMAGE));
            case "attackspeed":
            case "aspeed":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.ATTACK_SPEED));
            case "armor":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.ARMOR));
            case "defnse":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.DEFENSE));
            case "armortoughness":
            case "atoughness":
            case "toughness":
            case "atough":
            case "at":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.ARMOR_TOUGHNESS));
            case "knockbackresistance":
            case "kresistance":
            case "knockback":
            case "kres":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.KNOCKBACK_RESISTANCE));
            case "movementspeed":
            case "mspeed":
            case "speed":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.MOVEMENT_SPEED));
            case "maxhealth":
            case "mhealth":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.MAX_HEALTH));
            case "luck":
                return CummaltiveEquipmentDoubleStatValue(target, LUCK);
            case "criticalstrikechance":
            case "critchance":
            case "criticalchance":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.CRITICAL_STRIKE_CHANCE));
            case "criticalstrikepower":
            case "critpower":
            case "critpow":
            case "criticalpower":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.CRITICAL_STRIKE_POWER));
            case "weapondamage":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.WEAPON_DAMAGE));
            case "skilldamage":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.SKILL_DAMAGE));
            case "projectiledamage":
            case "projdamage":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.PROJECTILE_DAMAGE));
            case "magicdamage":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.MAGIC_DAMAGE));
            case "physicaldamage":
            case "meleedamage":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.PHYSICAL_DAMAGE));
            case "undeaddamage":
                return CummaltiveEquipmentDoubleStatValue(target, Stat(GooPMMOItemsItemStats.UNDEAD_DAMAGE));
            case "misc":
                return CummaltiveEquipmentDoubleStatValue(target, GetMiscStat(statname));

            default: return CummaltiveEquipmentDoubleStatValue(target, Stat(bakedStatname));
        }
    }

    /**
     * Will sum the value of this stat throughout the player's equipment.
     */
    @Nullable public static Double CummaltiveEquipmentDoubleStatValue(@NotNull Player target, @Nullable ItemStat statName) {

        // If null no
        if (statName == null) { return null; }

        // Attempt via Mythic Lib
        try {

            // IF it works it works
            MMOPlayerData mmo = MMOPlayerData.get(target.getUniqueId());
            return mmo.getStatMap().getStat(statName.getId());

        } catch (Exception ignored) { }

        // Will return null if player doesnt have any at all
        Double ret = null;

        // Get all the items
        ArrayList<VolatileMMOItem> pItems = GetPlayerEquipment(target);

        // I dont know how many there
        for (MMOItem pItem : pItems) {

            if (pItem != null) {

                // Get its stat
                Double cValue = GetDoubleStatValue(pItem, statName);

                // Ad if non-null
                if (cValue != null) {

                    // Set if current is null
                    if (ret == null) {

                        // Set
                        ret = cValue;

                        // Value existed already
                    } else {

                        // Add
                        ret += cValue;
                    }

                    //DBG//oots.C Log("§e - §7" + OotilityCeption.GetItemName(mod.getItem()) + "§ " + cValue + " §3Cummulative " + ret);
                }
            }

        }


        // Return the result
        return ret;
    }
    /**
     * If it is base TRUE, a single FALSE will make it FALSE forever.<p></p>
     * If it is base FALSE, a single TRUE will make it TRUE forever
     */
    public static boolean CummaltiveEquipmentBooleanStatValue(@NotNull Player target, @Nullable ItemStat statName, boolean base) {
        if (statName == null) { return base; }
        boolean ret = base;

        //DBG//OotilityCeption oots = new OotilityCeption();
        //DBG//oots.C Log("Getting cummulative §e" + statName.getName() + "§7 from §3" + target.getName());

        // Get all the items
        ArrayList<VolatileMMOItem> pItems = GetPlayerEquipment(target);

        // I dont know how many there
        for (MMOItem pItem : pItems) {

            if (pItem != null) {

                // Get its stat
                Boolean cValue = GetBooleanStatValue(pItem, statName);

                // If there was a value
                if (cValue != null) {

                    // If it is natively TRUE, a single FALSE will make it FALSE forever
                    if (base) {

                        // Both must be true
                        ret = ret && cValue;

                        // If it is natively FALSE, a single TRUE will make it TRUE forever
                    } else {

                        ret = ret || cValue;
                    }
                    //DBG//oots.C Log("§e - §7" + OotilityCeption.GetItemName(mod.getItem()) + "§ " + cValue + " §3Cummulative " + ret);
                }
            }
        }

        // Return the result
        return ret;
    }
    /**
     * Will return an empty array if it fails for any reason.
     */
    @NotNull public static ArrayList<String> CummaltiveEquipmentStringStatValue(@NotNull Player target, @Nullable ItemStat statName) {

        // Will just count the number of unique entries
        ArrayList<String> ret = new ArrayList<>();
        if (statName == null) { return ret; }

        // Get all the items
        ArrayList<VolatileMMOItem> pItems = GetPlayerEquipment(target);
        OotilityCeption.Log("§8MMO§3 EQP§7 Player Equipment§9 x" + pItems.size());

        // I dont know how many there
        for (MMOItem pItem : pItems) {
            OotilityCeption.Log("§8MMO§3 EQP§3 +§7 " + pItem.getType() + " " + pItem.getId());

            if (pItem != null) {

                // Get its stat
                String cValue = GetStringStatValue(pItem, statName);

                // Ad if non-null
                if (cValue != null) {

                    // Add if it is not contained
                    if (!ret.contains(cValue)) { ret.add(cValue); }

                    //DBG//oots.C Log("§e - §7" + OotilityCeption.GetItemName(mod.getItem()) + "§ " + cValue + " §3Cummulative " + ret);
                }
            }
        }

        // Return the result
        return ret;
    }

    /**
     * Returns the double data of such stat from that item if it exists (MAKE SURE YOU KNOW IT IS A DOUBLE STAT).
     * <p></p>
     * 'If such stat from that item exists' means that this will return a double if and only if:
     * <p>+ The stat exists and it is a DoubleStat
     * </p>+ The item is a MMOItem that has a value for it.
     * <p></p>
     * All other cases return NULL.
     * @param itemThatMayNotEvenBeAMMOItem Some ItemStack you have.
     * @param statName Use <code>GooPMMOItems.Stat( ... );</code> for this.
     * @return NULL if there is no such thing to retrieve, or the player doesnt meet the requirements to use.
     */
    @Nullable
    public static Double GetDoubleStatValue(@Nullable ItemStack itemThatMayNotEvenBeAMMOItem, @Nullable ItemStat statName) { return GetDoubleStatValue(itemThatMayNotEvenBeAMMOItem, statName, null, false); }

    /**
     * Returns the double data of such stat from that item if it exists (MAKE SURE YOU KNOW IT IS A DOUBLE STAT).
     * @param mmoitemAsNBTItem NBT Item containing the item stat.
     * @param statName Use <code>GooPMMOItems.Stat( ... );</code> for this.
     * @return NULL if there is no such thing to retrieve, or the player doesnt meet the requirements to use.
     */
    @Nullable
    public static Double GetDoubleStatValue(@Nullable NBTItem mmoitemAsNBTItem, @Nullable ItemStat statName) { return GetDoubleStatValue(mmoitemAsNBTItem, statName, null, false); }

    /**
     * Gets the Double Value from a stat (MAKE SURE YOU KNOW IT IS A DOUBLE STAT)
     * @param item Item containing the value
     * @param statName Use <code>GooPMMOItems.Stat( ... );</code> for this.
     * @param considerRPGrequirements Player to reject if they dont meet class reqs and stuff
     * @param silent If they fail the RPG reqs, should the fail message be displayed?
     * @return The value of the stat in the item, or NULL if the item doesn't ahve the stat, or it is not a MMOItem
     */
    @Nullable
    public static Double GetDoubleStatValue(@Nullable ItemStack item, @Nullable ItemStat statName, @Nullable Player considerRPGrequirements, boolean silent) {

        // Test if MMOItem
        if (IsMMOItem(item)) {

            // Return Value
            return GetDoubleStatValue(NBTItem.get(item), statName, considerRPGrequirements, silent);
        }

        return null;
    }

    /**
     * Returns the double data of such stat from that item if it exists (MAKE SURE YOU KNOW IT IS A DOUBLE STAT).
     * @param mmoitemAsNBTItem NBT Item containing the item stat.
     * @param statName Use <code>GooPMMOItems.Stat( ... );</code> for this.
     * @param considerRPGrequirements Player to test RPG requirements gainst, leave NULL to ignore RPG requirements.
     * @param silent if there is a player to test RPG requirements against, hide the message 'You dont have the correct class' if they fail the test.
     * @return NULL if there is no such thing to retrieve, or the player doesnt meet the requirements to use.
     */
    @Nullable
    public static Double GetDoubleStatValue(@Nullable NBTItem mmoitemAsNBTItem, @Nullable ItemStat statName, @Nullable Player considerRPGrequirements, boolean silent) {
        if (mmoitemAsNBTItem == null) { return null; }
        if (statName == null) { return null; }

        // Consider RPG requirements
        if (MeetsRequirements(considerRPGrequirements, mmoitemAsNBTItem, !silent)) {

            // Convert and Return
            return GetDoubleStatValue(VolatileFromNBT(mmoitemAsNBTItem), statName);
        }

        // Didnt meet the requirements - null
        return null;
    }

    /**
     * Returns the double data of such stat from that item if it exists.
     * @return NULL if there is no such thing to retrieve.
     */
    @Nullable
    public static Double GetDoubleStatValue(@Nullable MMOItem mmoitem, @Nullable ItemStat statName) {
        if (mmoitem == null) { return null; }
        if (statName == null) { return null; }

        // If the player meets the requirements, return the value
        if (mmoitem.hasData(statName)) {

            DoubleData dddData = (DoubleData) mmoitem.getData(statName);
            if (dddData != null) { return dddData.getValue(); }
        }

        return null;
    }

    @Nullable
    public static ArrayList<String> GetStringListStatValue(@Nullable ItemStack itemThatMayNotEvenBeAMMOItem, @Nullable ItemStat statName) { return GetStringListStatValue(itemThatMayNotEvenBeAMMOItem, statName, null, false); }
    /**
     * Returns the double data of such stat from that item if it exists (MAKE SURE YOU KNOW IT IS A DOUBLE STAT).
     * @param mmoitemAsNBTItem NBT Item containing the item stat.
     * @param statName Use <code>GooPMMOItems.Stat( ... );</code> for this.
     * @return NULL if there is no such thing to retrieve, or the player doesnt meet the requirements to use.
     */
    @Nullable

    /**
     * Returns the double data of such stat from that item if it exists (MAKE SURE YOU KNOW IT IS A DOUBLE STAT).
     * @param mmoitemAsNBTItem NBT Item containing the item stat.
     * @param statName Use <code>GooPMMOItems.Stat( ... );</code> for this.
     * @param considerRPGrequirements Player to test RPG requirements gainst, leave NULL to ignore RPG requirements.
     * @param silent if there is a player to test RPG requirements against, hide the message 'You dont have the correct class' if they fail the test.
     * @return NULL if there is no such thing to retrieve, or the player doesnt meet the requirements to use.
     */
    public static ArrayList<String> GetStringListStatValue(@Nullable NBTItem mmoitemAsNBTItem, @Nullable ItemStat statName) {
        if (mmoitemAsNBTItem == null) { return null; }

        // Consider RPG requirements
        MeetsRequirements(null, mmoitemAsNBTItem, true);

        // Convert and Return
        return GetStringListStatValue(VolatileFromNBT(mmoitemAsNBTItem), statName);

        // Didnt meet the requirements - null
    }


    /**
     * Returns the double data of such stat from that item if it exists.
     * @return NULL if there is no such thing to retrieve.
     */
    @Nullable
    public static ArrayList<String> GetStringListStatValue(@Nullable MMOItem mmoitem, @Nullable ItemStat statName) {
        if (mmoitem == null) { return null; }
        if (statName == null) { return null; }
        if (!mmoitem.hasData(statName)) { return null; }

        // Get data
        StatData sData = mmoitem.getData(statName);

        // Found?
        if (sData != null) {

            // Is it string list?
            if (sData instanceof StringListData) {

                return ValueOfStringListData(sData);

            } else if (sData instanceof GemSocketsData) {

                // Convert into string list of gem scokets
                return new ArrayList<>(((GemSocketsData)sData).getEmptySlots());
            }
        }

        return null;
    }


    @Nullable
    public static ArrayList<String> ValueOfStringListData(@Nullable StatData stat) {

        if (stat instanceof StringListData) {
            return new ArrayList<String>(((StringListData) stat).getList());
        }

        return null;
    }

    /**
     * Gets the Double Value from a stat (MAKE SURE YOU KNOW IT IS A DOUBLE STAT)
     * @param itemThatMayNotEvenBeAMMOItem Item containing the value
     * @param statName Use <code>GooPMMOItems.Stat( ... );</code> for this.
     * @param considerRPGrequirements Player to reject if they dont meet class reqs and stuff
     * @param silent If they fail the RPG reqs, should the fail message be displayed?
     * @return The value of the stat in the item, or NULL if the item doesn't ahve the stat, or it is not a MMOItem
     */
    @Nullable
    public static ArrayList<String> GetStringListStatValue(@Nullable ItemStack itemThatMayNotEvenBeAMMOItem, @Nullable ItemStat statName, @Nullable Player considerRPGrequirements, boolean silent) {

        // Test if MMOItem
        if (IsMMOItem(itemThatMayNotEvenBeAMMOItem)) {

            NBTItem itm = NBTItem.get(itemThatMayNotEvenBeAMMOItem);


            if (MeetsRequirements(considerRPGrequirements, itm, !silent)) {


                // Return Value
                return GetStringListStatValue(itm, statName);
            }
        }

        return null;
    }


    /**
     * Gets the Boolean Value from a stat (MAKE SURE YOU KNOW IT IS A BOOLEAN STAT)
     * @param itemThatMayNotEvenBeAMMOItem Item containing the value
     * @param statName Use <code>GooPMMOItems.Stat( ... );</code> for this.
     * @param considerRPGrequirements Player to reject if they dont meet class reqs and stuff
     * @param silent If they fail the RPG reqs, should the fail message be displayed?
     * @return The value of the stat in the item, or NULL if the item doesn't ahve the stat, or it is not a MMOItem
     */
    @Nullable
    public static Boolean GetBooleanStatValue(@Nullable ItemStack itemThatMayNotEvenBeAMMOItem, @NotNull ItemStat statName, @Nullable Player considerRPGrequirements, boolean silent) {

        if (IsMMOItem(itemThatMayNotEvenBeAMMOItem)) {
            //STAT//OotilityCeption. Log(" §c+§7 As MMOItem");

            // Yes it is, does it have a ... tag?
            return GetBooleanStatValue(NBTItem.get(itemThatMayNotEvenBeAMMOItem), statName, considerRPGrequirements, silent);
        }

        //STAT//OotilityCeption. Log(" §c- Fail");
        return null;
    }

    public static String GetStringStatValue(@Nullable MMOItem mmoitem, @Nullable ItemStat statName) {
        if (mmoitem == null) { return null; }
        if (statName == null) { return null; }

        // Has data??? >:O
        if (mmoitem.hasData(statName)) {

            // If the player meets the requirements, return the value
            StringData sData = (StringData) mmoitem.getData(statName);
            if (sData != null) { return sData.toString(); }
        }

        return null;
    }

    @Nullable
    public static Boolean GetBooleanStatValue(@Nullable NBTItem mmoitemAsNBTItem, @Nullable ItemStat statName, @Nullable Player considerRPGrequirements, boolean silent) {
        if (mmoitemAsNBTItem == null) {
            //STAT//OotilityCeption. Log(" §c- Null Fail");
            return null; }
        if (statName == null) {
            //STAT//OotilityCeption. Log(" §c- Stat Fail");
            return null; }
        //STAT//OotilityCeption. Log(" §c+§7 Null Check Passed");

        // Consider the Requirements
        if (MeetsRequirements(considerRPGrequirements, mmoitemAsNBTItem, !silent)) {
            //STAT//OotilityCeption. Log(" §c+§7 Requirements Met");

            // Convert and Return
            return GetBooleanStatValue(VolatileFromNBT(mmoitemAsNBTItem), statName);
        }
        //STAT//OotilityCeption. Log(" §c- Requirements Fail");
        // Didnt meet the requirements, null.
        return null;
    }
    public static Boolean GetBooleanStatValue(@Nullable MMOItem mmoitem, @Nullable ItemStat statName) {
        if (mmoitem == null) {
            //STAT//OotilityCeption. Log(" §c- Null Fail 2");
            return null; }
        if (statName == null) {
            //STAT//OotilityCeption. Log(" §c- Stat Fail 2");
        return null; }
        //STAT//OotilityCeption. Log(" §c+§7 Null Check 2 Passed");

        // Has data??? >:O
        if (mmoitem.hasData(statName)) {
            //STAT//OotilityCeption. Log(" §c+§7 Found Data");

            // If the player meets the requirements, return the value
            BooleanData dddData = ( BooleanData ) mmoitem.getData(statName);
            if (dddData != null) {
                //STAT//OotilityCeption. Log(" §c+§7 Data Found: §6" + dddData.isEnabled());
                return dddData.isEnabled();
            }
        }

        //STAT//OotilityCeption. Log(" §c+ Data Fail");
        return null;
    }

    /**
     * Gets the String Value from a stat (MAKE SURE YOU KNOW IT IS A STRING STAT)
     * @param itemThatMayNotEvenBeAMMOItem Item containing the value
     * @param statName Use <code>GooPMMOItems.Stat( ... );</code> for this.
     * @param considerRPGrequirements Player to reject if they dont meet class reqs and stuff
     * @param silent If they fail the RPG reqs, should the fail message be displayed?
     * @return The value of the stat in the item, or NULL if the item doesn't ahve the stat, or it is not a MMOItem
     */
    public static String GetStringStatValue(@Nullable ItemStack itemThatMayNotEvenBeAMMOItem, ItemStat statName, Player considerRPGrequirements, boolean silent) {

        if (IsMMOItem(itemThatMayNotEvenBeAMMOItem)) {

            // Yes it is, does it have a ... tag?
            return GetStringStatValue(NBTItem.get(itemThatMayNotEvenBeAMMOItem), statName, considerRPGrequirements, silent);
        }

        return null;
    }
    public static String GetStringStatValue(@Nullable NBTItem mmoitemAsNBTItem, ItemStat statName, Player considerRPGrequirements, boolean silent) {
        if (mmoitemAsNBTItem == null) { return null; }
        if (statName == null) { return null; }

        // Consider the Requirements
        if (GooPMMOItems.MeetsRequirements(considerRPGrequirements, mmoitemAsNBTItem, !silent)) {

            // Convert and Return
            return GetStringStatValue(VolatileFromNBT(mmoitemAsNBTItem), statName);
        }

        // Didnt meet the requirements, null.
        return null;
    }

    // Get Item Name
    public static int GetItemLevelNonull(@Nullable ItemStack iSource) {

        // Get Level
        Integer actualLevel = GetItemLevel(iSource);

        // There
        if (actualLevel == null) { return 0; }
        return actualLevel;
    }
    public static Integer GetItemLevel(@Nullable ItemStack iSource) {
        if (iSource == null) { return null; }
        if (!IsMMOItem(iSource)) { return null; }

        MMOItem mmoitem = VolatileFromNBT(NBTItem.get(iSource));
        if (mmoitem == null) { return null; }

        ItemStat oupgrade = GooPMMOItems.Stat(GooPMMOItemsItemStats.UPGRADE);

        if (oupgrade == null) { return null; }

        if (!mmoitem.hasData(oupgrade)) { return null; }

        // If the player meets the requirements, return the value
        UpgradeData iUpgrade = (UpgradeData) mmoitem.getData(oupgrade);
        if (iUpgrade != null) {

            // Get data
            return iUpgrade.getLevel();
        }

        return null;
    }
    //endregion

    //region Testing RPG Requirements
    public static boolean MeetsRequirements(Player target, ItemStack item, boolean message) { return MeetsRequirements(target, NBTItem.get(item), message); }
    public static boolean MeetsRequirements(Player target, NBTItem item, boolean message) {
        if (target != null) {
            PlayerData iData = PlayerData.get(target);

            if (iData != null) {

                RPGPlayer iRPG = iData.getRPG();

                if (iRPG != null) {

                    return iRPG.canUse(item, message);
                }
            }

            // Guess Yes
            return true;

        } else {

            // Guess Yes
            return true;
        }
    }

    public static int GetPlayerLevel(@NotNull Player target) {

        // Get Data
        PlayerData data = PlayerData.get(target);

        if (data == null) { return 0; }
        if (data.getRPG() == null) { return 0; }

        // Find level
        return data.getRPG().getLevel();
    }

    @NotNull public static String GetPlayerClass(@NotNull Player target) {

        // Get Data
        PlayerData data = PlayerData.get(target);

        if (data == null) { return ""; }
        if (data.getRPG() == null) { return ""; }

        // Find class
        String ret = data.getRPG().getClassName();

        // Never null
        return ret != null ? ret : "";
    }
    //endregion

    //region Gemstone Stuff
    @Nullable public static ItemStack MMOItemCountGems(@Nullable ItemStack base, @Nullable Boolean includeEmpty, @NotNull RefSimulator<Integer> numericOutput, @Nullable RefSimulator<String> logOutput) {

        // I hope that bitch is not null to begin with
        if (base != null) {

            // Gettat Target NBT
            NBTItem tNBT = NBTItem.get(base);

            // Is it an mmoItem to begin with?
            if (tNBT.hasType()) {

                // Add slot heck
                if (logOutput != null) { logOutput.SetValue(null); }
                return CountGemSlots(tNBT, includeEmpty, numericOutput);

            // Its a vanilla item. Thus it has no gemslots
            } else {

                OotilityCeption.Log4Success(logOutput, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "This vanilla item has no gem stones. ");
                numericOutput.setValue(0);
                return null;
            }

            // This man passing on a null item wth
        } else {

            // Log if appropiate
            OotilityCeption.Log4Success(logOutput, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Cant count gem slots of air! Well I guess it has 0. ");
            numericOutput.setValue(0);
            return null;
        }

       // Memmories...
        // OotilityCeption.Log4Success(logOutput, !Gunging_Ootilities_Plugin.blockImportantErrorFeedback, "§cOutdated MMOItems version. §7Counting Gem Slots to items requires §aMMOItems 5.4 §o(Build #254)§7 or newer.");

    }
    public static ItemStack MMOItemAddGemSlot(ItemStack base, String gemSlotColour, RefSimulator<String> logOutput) {
        // Gemstone Support is Enabled (Correct MMOItems version)

        // I hope that bitch is not null to begin with
        if (base != null) {

            // Result
            int count = base.getAmount();
            ItemStack result = null;

            // Gettat Target NBT
            NBTItem tNBT = NBTItem.get(base);

            // Is it an mmoItem to begin with?
            if (tNBT.hasType()) {

                // Add slot heck
                result = AddGemSlot(tNBT, gemSlotColour);

                // Log if appropiate
                if (logOutput != null) {

                    // If there is fail feedback to send
                    if (Gunging_Ootilities_Plugin.sendGooPSuccessFeedback){

                        // Return a Log Message, for good shit.
                        logOutput.SetValue("Added a new §3" + gemSlotColour + "§7 gem slot to §3" + OotilityCeption.GetItemName(base) + "§7. ");

                        // Otherwise, send nothing.
                    } else {

                        // Clear da shit
                        logOutput.SetValue(null);
                    }
                }

            // Its a vanilla item. Convert into MMOItem and add tha fucking gem slot
            } else {

                // Is it Air?
                if (OotilityCeption.IsAir(base.getType())) {

                    // Log if appropiate
                    if (logOutput != null) {

                        // If there is fail feedback to send
                        if (Gunging_Ootilities_Plugin.sendGooPSuccessFeedback){

                            // Return a Log Message, for good shit.
                            logOutput.SetValue("Cant add §3" + gemSlotColour + "§7 slots to air! ");

                        // Otherwise, send nothing.
                        } else {

                            // Clear da shit
                            logOutput.SetValue(null);
                        }
                    }

                // Its an actual item (Not Air)
                } else {

                    // Attempt to Convert to NBT
                    int oCount = base.getAmount();
                    base = ConvertVanillaToMMOItem(base);
                    tNBT = NBTItem.get(base);

                    // Add slot heck
                    result = AddGemSlot(tNBT, gemSlotColour);
                    if (result != null) { result.setAmount(oCount); }

                    // Log if appropiate
                    if (logOutput != null) {

                        // There was an error wth
                        if (result == null) {

                            // If there is fail feedback to send
                            if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Return a Log Message, for good shit.
                                logOutput.SetValue("§7To add gems to vanilla items, they must first be converted to MMOItems. §cFor that to be possible, please create an empty MMOItem with ID (name) '§eVANILLA§c' under the type '§e" + tNBT.getString("MMOITEMS_ITEM_TYPE") + "§c.' §7(The type that §3" + base.getType().name() + "§7' falls into). ");

                            // Otherwise, send nothing.
                            } else {

                                // Clear da shit
                                logOutput.SetValue(null);
                            }

                        // Perhaps the thing doesnt support gem slots
                        } else if (result.getType() == Material.STRUCTURE_VOID) {

                            // If there is fail feedback to send
                            if (Gunging_Ootilities_Plugin.sendGooPFailFeedback){

                                // Return a Log Message, for good shit.
                                logOutput.SetValue("Item Type §e" + tNBT.getString("MMOITEMS_ITEM_TYPE") + "§7 does not support Gem Slots. Cancelling operation. ");

                            // Otherwise, send nothing.
                            } else {

                                // Clear da shit
                                logOutput.SetValue(null);
                            }

                            // Clear it btw
                            result = null;

                        // If there was a success
                        } else {

                            // If there is success feedback to send
                            if (Gunging_Ootilities_Plugin.sendGooPSuccessFeedback){

                                // Return a Log Message, for good shit.
                                logOutput.SetValue("Successfuly Converted §3" + OotilityCeption.GetItemName(base) + "§7 into a MMOItem and added an empty §3" + gemSlotColour + "§7 slot. ");

                            // Otherwise, send nothing.
                            } else {

                                // Clear da shit
                                logOutput.SetValue(null);
                            }

                        }
                    }
                }
            }

            if (result != null) { result.setAmount(count); }
            return result;

        // This man passing on a null item wth
        } else {

            // Log if appropiate
            if (logOutput != null) {

                // If there is fail feedback to send
                if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) {

                    // Return a Log Message, for good shit.
                    logOutput.SetValue("Cant add §3" + gemSlotColour + "§7 gem slots to air!");

                // Otherwise, send nothing.
                } else {

                    // Clear da shit
                    logOutput.SetValue(null);
                }
            }
            return null;
        }


        // Gemstone Support is not enabled
    }
    public static ItemStack AddGemSlot(NBTItem base, String gemSlotColour) {

        // Test of existence of such type and IDs
        try {

            // Compatible to Gemstone?
            if (GooPMMOItems.TypeGemstoneCompatible(GooPMMOItems.GetMMOItemType(base))) {

                // Attempt to get MMOItem Instance
                MMOItem mmoitem = new LiveMMOItem(base);

                // Make new mergeable gem sockets data
                GemSocketsData newSocket = new GemSocketsData(new ArrayList<>());
                newSocket.addEmptySlot(gemSlotColour);

                // Merge :b
                mmoitem.mergeData(ItemStats.GEM_SOCKETS, newSocket, null);

                // Get Finished Product
                return mmoitem.newBuilder().build();

            // No. Return a Structure Void Block as a result
            } else {

                // Returning a Structure Void means the type doesn't support gems on it
                return new ItemStack(Material.STRUCTURE_VOID);
            }

            // Seems that those Types/IDs did not match wth
        } catch (NullPointerException e) {
            return null;
        }
    }
    @Nullable public static ItemStack CountGemSlots(NBTItem base, @Nullable Boolean incEmpty, @NotNull RefSimulator<Integer> countOutput) {
        if (incEmpty == null) { incEmpty = false; }

        // Test of existence of such type and IDs
        try {

            // Compatible to Gemstone?
            if (GooPMMOItems.TypeGemstoneCompatible(GooPMMOItems.GetMMOItemType(base))) {

                // Attempt to get MMOItem Instance
                MMOItem mmoitem = new LiveMMOItem(base);

                // Get Socket Data
                GemSocketsData sockets = (GemSocketsData) mmoitem.getData(GooPMMOItems.Stat(GooPMMOItemsItemStats.GEM_SOCKETS));

                // Has the items any sockets already?
                if (sockets != null) {

                    // Then just add the entry to the existing list
                    int ret = sockets.getGemstones().size();
                    if (incEmpty) { ret += sockets.getEmptySlots().size(); }
                    countOutput.setValue(ret);
                    return null;

                    // Item has no gem slots at all, straight up 0
                } else {

                    countOutput.setValue(0);
                    return null;
                }

                // No. Return a Strucutre Void Block as a result
            } else {

                countOutput.setValue(0);
                return null;
            }

            // Seems that those Types/IDs did not match wth
        } catch (NullPointerException e) {

            countOutput.setValue(0);
            return null;
        }
    }

    /**
     * Will return true if the type provided is compatible with having GemStones.
     * <p></p>
     * This is because some things, like MISCELLANEOUS, cannot have Gem Sockets.
     */
    public static boolean TypeGemstoneCompatible(@Nullable Type tType) {

        // Null is incompatible
        if (tType == null) { return false;}

        // Result Variable
        boolean ret = false;

        // Search Avaliable Stats for Gemstone
        for (ItemStat statt : tType.getAvailableStats()) {

            // Found a Gem Sockets Support?
            if (statt == Stat(GooPMMOItemsItemStats.GEM_SOCKETS)) {

                // Sure, it is supported.
                ret = true;

                // No need to keep searching
                break;
            }
        }

        // Return Result
        return ret;
    }


    /**
     * YOU MUST KNOW THESE ARE MMOITEMS, the GEMSTONE and the TARGET, otherwise it will produce NULL POINTER EXCEPTIONS
     */
    public static boolean GemstoneFitsOnto(Player applier, NBTItem gemstone, NBTItem target) {

        // In what terms are we dealing?
        Type tType = GetMMOItemType(target);

        // Success?
        if (tType != null) {

            // Use
            return GemstoneFitsOnto(applier, gemstone,target, tType);

        } else {

            // I guess not lma0
            return false;
        }
    }

    static Method getItemSet = null;
    static Method getItemSetName = null;
    static Boolean usingGetItemSet = null;
    /**
     * YOU MUST KNOW THESE ARE MMOITEMS, the GEMSTONE and the TARGET, otherwise it will produce NULL POINTER EXCEPTIONS
     */
    public static boolean GemstoneFitsOnto(Player applier, NBTItem gemstone, NBTItem target, Type targetType) {

        // Get Target
        MMOItem targetMMO = VolatileFromNBT(target);
        if (targetMMO == null) { return false; }

        // Does it have any gem sockets?
        if (!targetMMO.hasData(GooPMMOItems.Stat(GooPMMOItemsItemStats.GEM_SOCKETS))) { return false; }
        // Get Ouse Item
        UseItem asUse = GooPMMOItems.GetUseItem(gemstone, applier);

        // Is it a gemstone?
        if (!(asUse instanceof GemStone asGem)) { return false; }

        // Does it match the colours?
        String gemType = asGem.getNBTItem().getString("MMOITEMS_GEM_COLOR");

        GemSocketsData sockets = (GemSocketsData)targetMMO.getData(GooPMMOItems.Stat(GooPMMOItemsItemStats.GEM_SOCKETS));
        if (!sockets.canReceive(gemType)) { return false; }

        // Does it match the type
        String appliableTypes = asGem.getNBTItem().getString("MMOITEMS_ITEM_TYPE_RESTRICTION");
        if (!appliableTypes.equals("")) {

            // Identify useability of Using get Item Set
            if (usingGetItemSet == null) {
                try {
                    getItemSet = targetType.getClass().getMethod("getItemSet");
                    getItemSetName = getItemSet.invoke(targetType).getClass().getMethod("name");
                    usingGetItemSet = true;
                } catch (NoSuchMethodException|IllegalAccessException|InvocationTargetException ignored) {
                    usingGetItemSet = false;
                }
            }

            // Actually read this method
            boolean applicableCancel;
            if (usingGetItemSet) {
                try {
                    Object itemTypeSet = getItemSet.invoke(targetType);

                    // applicableCancel = !appliableTypes.contains(targetType.getItemSet().name())
                    applicableCancel = !appliableTypes.contains((String) getItemSetName.invoke(itemTypeSet));
                } catch (IllegalAccessException|InvocationTargetException ignored) {
                    usingGetItemSet = false;
                    applicableCancel = true;
                }

            // It is true by default
            } else { applicableCancel = true; }

            boolean nonWeaponCancel = !targetType.isWeapon() || !appliableTypes.contains("WEAPON");
            boolean explicitCancel = !appliableTypes.contains(targetType.getId());
            return !nonWeaponCancel || !applicableCancel || !explicitCancel;
        }

        // HUH, Success!
        return true;
    }
    //endregion

    //region Gemstone Operations

    /**
     * Extracts a gemstone from an item at the given socket index.
     */
    @Nullable
    public static ItemStack ExtractGemstone(@NotNull ItemStack itemStack, int socketIndex, @NotNull List<String> logReturn) {
        if (!IsMMOItem(itemStack)) {
            logReturn.add("§cItem is not an MMOItem!");
            return null;
        }

        try {
            LiveMMOItem mmoitem = new LiveMMOItem(NBTItem.get(itemStack));
            GemSocketsData sockets = (GemSocketsData) mmoitem.getData(Stat(GooPMMOItemsItemStats.GEM_SOCKETS));

            if (sockets == null) {
                logReturn.add("§cItem has no gem sockets!");
                return null;
            }

            List<GemstoneData> gems = sockets.getGems();
            if (socketIndex < 0 || socketIndex >= gems.size()) {
                logReturn.add("§cInvalid socket index! Item has " + gems.size() + " gemstone(s).");
                return null;
            }

            GemstoneData gemData = gems.get(socketIndex);
            String gemType = gemData.getMMOItemType();
            String gemId = gemData.getMMOItemID();

            // Build the gemstone item
            Type type = MMOItems.plugin.getTypes().get(gemType);
            if (type == null) {
                logReturn.add("§cGemstone type '§e" + gemType + "§c' not found!");
                return null;
            }

            MMOItem gemMMO = MMOItems.plugin.getMMOItem(type, gemId);
            if (gemMMO == null) {
                logReturn.add("§cGemstone '§e" + gemType + " " + gemId + "§c' not found!");
                return null;
            }

            ItemStack gemItem = gemMMO.newBuilder().build();
            if (gemItem == null) {
                logReturn.add("§cFailed to build gemstone item!");
                return null;
            }

            // Remove the gemstone from the item
            List<GemstoneData> newGems = new ArrayList<>(gems);
            newGems.remove(socketIndex);

            GemSocketsData newSockets = new GemSocketsData(new ArrayList<>(sockets.getEmptySlots()));
            for (GemstoneData gd : newGems) {
                newSockets.add(gd);
            }

            mmoitem.setData(Stat(GooPMMOItemsItemStats.GEM_SOCKETS), newSockets);
            ItemStack result = mmoitem.newBuilder().build();

            logReturn.add("§aExtracted gemstone: §e" + gemType + " " + gemId);
            return result;

        } catch (Exception e) {
            logReturn.add("§cError extracting gemstone: " + e.getMessage());
            return null;
        }
    }

    //endregion

    //region Gemstone Swap

    /**
     * Swaps a gemstone in an item at the given socket index with a new gemstone.
     */
    @Nullable
    public static ItemStack SwapGemstone(@NotNull ItemStack itemStack, int socketIndex, @NotNull String newGemType, @NotNull String newGemId, @NotNull List<String> logReturn) {
        if (!IsMMOItem(itemStack)) {
            logReturn.add("§cItem is not an MMOItem!");
            return null;
        }

        try {
            LiveMMOItem mmoitem = new LiveMMOItem(NBTItem.get(itemStack));
            GemSocketsData sockets = (GemSocketsData) mmoitem.getData(Stat(GooPMMOItemsItemStats.GEM_SOCKETS));

            if (sockets == null) {
                logReturn.add("§cItem has no gem sockets!");
                return null;
            }

            List<GemstoneData> gems = sockets.getGems();
            if (socketIndex < 0 || socketIndex >= gems.size()) {
                logReturn.add("§cInvalid socket index! Item has " + gems.size() + " gemstone(s).");
                return null;
            }

            GemstoneData oldGem = gems.get(socketIndex);
            String oldGemType = oldGem.getMMOItemType();
            String oldGemId = oldGem.getMMOItemID();

            // Verify the new gemstone exists
            Type type = MMOItems.plugin.getTypes().get(newGemType);
            if (type == null) {
                logReturn.add("§cGemstone type '§e" + newGemType + "§c' not found!");
                return null;
            }

            MMOItem newGemMMO = MMOItems.plugin.getMMOItem(type, newGemId);
            if (newGemMMO == null) {
                logReturn.add("§cGemstone '§e" + newGemType + " " + newGemId + "§c' not found!");
                return null;
            }

            // Create new gemstone data with same color as old gemstone
            GemstoneData newGemData = new GemstoneData(newGemType, newGemId, oldGem.getSocketColor(), oldGem.getHistoricUUID().toString());
            newGemData.setLevel(oldGem.getLevel());

            // Replace the gemstone
            List<GemstoneData> newGems = new ArrayList<>(gems);
            newGems.set(socketIndex, newGemData);

            GemSocketsData newSockets = new GemSocketsData(new ArrayList<>(sockets.getEmptySlots()));
            for (GemstoneData gd : newGems) {
                newSockets.add(gd);
            }

            mmoitem.setData(Stat(GooPMMOItemsItemStats.GEM_SOCKETS), newSockets);
            ItemStack result = mmoitem.newBuilder().build();

            logReturn.add("§aSwapped gemstone: §e" + oldGemType + " " + oldGemId + " → " + newGemType + " " + newGemId);
            return result;

        } catch (Exception e) {
            logReturn.add("§cError swapping gemstone: " + e.getMessage());
            return null;
        }
    }

    //endregion

    //region Gemstone Migration

    /**
     * Migrates gemstone IDs across all MMOItems of a specific type.
     */
    public static int MigrateGemstones(@NotNull String gemType, @NotNull String oldId, @NotNull String newId, @NotNull List<String> logReturn) {
        Type type = MMOItems.plugin.getTypes().get(gemType);
        if (type == null) {
            logReturn.add("§cGemstone type '§e" + gemType + "§c' not found!");
            return 0;
        }

        MMOItem oldMMO = MMOItems.plugin.getMMOItem(type, oldId);
        if (oldMMO == null) {
            logReturn.add("§cGemstone '§e" + gemType + " " + oldId + "§c' not found!");
            return 0;
        }

        // Check if new ID already exists
        MMOItem newMMO = MMOItems.plugin.getMMOItem(type, newId);
        if (newMMO != null) {
            logReturn.add("§cGemstone '§e" + gemType + " " + newId + "§c' already exists! Delete it first.");
            return 0;
        }

        try {
            // Create new MMOItem template with new ID
            MMOItemTemplate oldTemplate = MMOItems.plugin.getTemplates().getTemplate(type, oldId);
            if (oldTemplate == null) {
                logReturn.add("§cNo template found for gemstone '§e" + gemType + " " + oldId + "§c'!");
                return 0;
            }

            // Build the old MMOItem to get its stats
            MMOItem oldMMOItem = oldTemplate.newBuilder().build();
            if (oldMMOItem == null) {
                logReturn.add("§cFailed to build old gemstone '§e" + gemType + " " + oldId + "§c'!");
                return 0;
            }

            // Create new template with new ID
            MMOItemTemplate newTemplate = new MMOItemTemplate(type, newId);

            // Copy base data from old template using reflection-free approach
            // We'll use the newBuilder approach to copy stats
            Map<ItemStat, RandomStatData> baseData = oldTemplate.getBaseItemData();
            for (Map.Entry<ItemStat, RandomStatData> entry : baseData.entrySet()) {
                // Use reflection to set base data
                try {
                    java.lang.reflect.Field baseField = MMOItemTemplate.class.getDeclaredField("base");
                    baseField.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    Map<ItemStat, RandomStatData> newBase = (Map<ItemStat, RandomStatData>) baseField.get(newTemplate);
                    newBase.put(entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    // Skip this stat if we can't copy it
                }
            }

            // Register the new template
            MMOItems.plugin.getTemplates().registerTemplate(newTemplate);

            logReturn.add("§aCreated new gemstone template: §e" + gemType + " " + newId);

            // Migrate items that have the old gemstone socketed
            int itemsMigrated = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item == null || !IsMMOItem(item)) continue;

                    try {
                        LiveMMOItem mmoitem = new LiveMMOItem(NBTItem.get(item));
                        GemSocketsData sockets = (GemSocketsData) mmoitem.getData(Stat(GooPMMOItemsItemStats.GEM_SOCKETS));

                        if (sockets == null || sockets.getGemstones().isEmpty()) continue;

                        boolean modified = false;
                        List<GemstoneData> newGems = new ArrayList<>();

                        for (GemstoneData gd : sockets.getGems()) {
                            if (gd.getMMOItemType().equals(gemType) && gd.getMMOItemID().equals(oldId)) {
                                GemstoneData newGd = new GemstoneData(gemType, newId, gd.getSocketColor(), gd.getHistoricUUID().toString());
                                newGd.setLevel(gd.getLevel());
                                newGems.add(newGd);
                                modified = true;
                            } else {
                                newGems.add(gd);
                            }
                        }

                        if (modified) {
                            GemSocketsData newSockets = new GemSocketsData(new ArrayList<>(sockets.getEmptySlots()));
                            for (GemstoneData gd : newGems) {
                                newSockets.add(gd);
                            }
                            mmoitem.setData(Stat(GooPMMOItemsItemStats.GEM_SOCKETS), newSockets);
                            ItemStack result = mmoitem.newBuilder().build();
                            if (result != null) {
                                item.setItemMeta(result.getItemMeta());
                                itemsMigrated++;
                            }
                        }
                    } catch (Exception ignored) {
                        // Skip items that can't be processed
                    }
                }
            }

            logReturn.add("§aMigrated §e" + itemsMigrated + "§a item(s) with gemstones socketed.");
            return 1;

        } catch (Exception e) {
            logReturn.add("§cError migrating gemstones: " + e.getMessage());
            return 0;
        }
    }

    //endregion

    //region Gemstone List & Info

    /**
     * Lists all gemstones of a specific type.
     */
    public static void ListGemstones(@NotNull String gemType, @NotNull List<String> logReturn) {
        Type type = MMOItems.plugin.getTypes().get(gemType);
        if (type == null) {
            logReturn.add("§cGemstone type '§e" + gemType + "§c' not found!");
            return;
        }

        List<String> templateIds = MMOItems.plugin.getTemplates().getTemplateNames(type);
        if (templateIds == null || templateIds.isEmpty()) {
            logReturn.add("§7No gemstones found for type '§e" + gemType + "§7'.");
            return;
        }

        logReturn.add("§3Gemstones of type §e" + gemType + "§3:");
        for (String id : templateIds) {
            logReturn.add("  §7- §e" + id);
        }
    }

    /**
     * Gets information about a specific gemstone.
     */
    public static void GetGemstoneInfo(@NotNull String gemType, @NotNull String gemId, @NotNull List<String> logReturn) {
        Type type = MMOItems.plugin.getTypes().get(gemType);
        if (type == null) {
            logReturn.add("§cGemstone type '§e" + gemType + "§c' not found!");
            return;
        }

        MMOItem mmo = MMOItems.plugin.getMMOItem(type, gemId);
        if (mmo == null) {
            logReturn.add("§cGemstone '§e" + gemType + " " + gemId + "§c' not found!");
            return;
        }

        logReturn.add("§3Gemstone Info: §e" + gemType + " " + gemId);
        if (mmo.hasData(ItemStats.NAME)) {
            NameData nameData = (NameData) mmo.getData(ItemStats.NAME);
            logReturn.add("§7Name: §f" + (nameData != null ? nameData.getString() : "N/A"));
        } else {
            logReturn.add("§7Name: §fN/A");
        }
    }

    //endregion

    //region Load Gemstone Migration from Config
    // Parses the Gemstone_Migration section from mmoitems-converter.yml
    // and runs automated migration for each old->new gemstone ID pair.
    // Returns a list of log messages describing what was done.
    @NotNull
    public static List<String> LoadGemstoneMigrationConfig(@Nullable ConfigurationSection gemstoneMigrationSection) {
        List<String> logReturn = new ArrayList<>();

        // No section? Nothing to do.
        if (gemstoneMigrationSection == null) {
            logReturn.add("§7§o[GooP] Gemstone_Migration section not found in config. Skipping.");
            return logReturn;
        }

        // Iterate over each gemstone type (e.g., GEM_STONE)
        for (String gemType : gemstoneMigrationSection.getKeys(false)) {
            ConfigurationSection typeSection = gemstoneMigrationSection.getConfigurationSection(gemType);
            if (typeSection == null) {
                logReturn.add("§c§o[GooP] Invalid Gemstone_Migration entry for type '§e" + gemType + "§c'. Skipping.");
                continue;
            }

            // Iterate over each old->new mapping
            for (String oldId : typeSection.getKeys(false)) {
                String newId = typeSection.getString(oldId);

                // Validate entries
                if (newId == null || newId.isBlank()) {
                    logReturn.add("§c§o[GooP] Empty or invalid new ID for '§e" + oldId + "§c' in type '§e" + gemType + "§c'. Skipping.");
                    continue;
                }

                // Skip if old and new are the same
                if (oldId.equalsIgnoreCase(newId)) {
                    logReturn.add("§7§o[GooP] Skipping '§e" + oldId + "§7' -> '§e" + newId + "§7': same ID.");
                    continue;
                }

                // Run the migration and log the result
                logReturn.add("§6§o[GooP] Migrating gemstone '§e" + gemType + " " + oldId + "§6' -> '§e" + gemType + " " + newId + "§6'...");
                MigrateGemstones(gemType, oldId, newId, logReturn);
            }
        }

        return logReturn;
    }

    //endregion

    //region Converting from Vanilla to MMOItem
    public static String VANILLA_MIID = "VANILLA";
    @NotNull public static NBTItem ConvertVanillaToMMOItemAsNBT(@NotNull ItemStack base) {
        return ConvertVanillaToMMOItemAsNBT(base, null, null, false);
    }

    @NotNull public static NBTItem ConvertVanillaToMMOItemAsNBT(@NotNull ItemStack base, @Nullable String forcedTypePrefix, @Nullable String forcedIDFormat, boolean forcedType) {

        // Lets very quickly add a display name to this boi
        OotilityCeption.RenameItem(base, OotilityCeption.GetItemName(base), null);

        if (forcedTypePrefix == null) { forcedTypePrefix = ""; }

        // Get NBT item
        NBTItem tNBT = NBTItem.get(base);

        // Attribute Information
        double vDamage = 0.0, vSpeed, vArmor, vArmorT, mHealthT = 0.0, mSpeedT = 0.0, mKRes, mLuck = 0.0;

        // Decide what TYPE this item will be
        String tName;

        // Get vals
        RefSimulator<String> tNameRef = new RefSimulator<>("MISCELLANEOUS");
        RefSimulator<Double> vDamageRef = new RefSimulator<>(0.0), vSpeedRef = new RefSimulator<>(0.0), vArmorRef = new RefSimulator<>(0.0), vArmorTRef = new RefSimulator<>(0.0), mHealthTRef = new RefSimulator<>(0.0), mKResRef = new RefSimulator<>(0.0);
        OotilityCeption.GatherDefaultVanillaAttributes(base.getType(), tNameRef, vDamageRef, vSpeedRef, vArmorRef, vArmorTRef, mKResRef);

        // If they actually deal damage; in the conversion to MMOItems it is 1 + that;
        if (vDamageRef.getValue() > 0) { vDamage = OotilityCeption.RoundAtPlaces(vDamageRef.getValue(), 1) + 1; }
        vSpeed = vSpeedRef.getValue(); vArmor = vArmorRef.getValue(); vArmorT = vArmorTRef.getValue(); mKRes = mKResRef.getValue(); tName = tNameRef.getValue();

        // Replace if missing
        ArrayList<String> realTypes = GooPMMOItems.GetMMOItem_TypeNames();
        if (!realTypes.contains(tName)) {

            // Well replace I guess
            if (tName.equals("SHIELD")) { tName = "ARMOR"; }
            if (tName.equals("WHIP")) { tName = "MATERIAL"; }

            if (!realTypes.contains(tName)) {

                // Well make it funny I guess
                tName = "SWORD";
            }
        }

        // Create the Item Tags
        ItemTag tTypeMI = new ItemTag("MMOITEMS_ITEM_TYPE", forcedType ? forcedTypePrefix : forcedTypePrefix + tName);
        ItemTag tID = new ItemTag("MMOITEMS_ITEM_ID", forcedIDFormat == null ? VANILLA_MIID : forcedIDFormat);

        // Add those tags
        tNBT.addTag(tTypeMI);
        tNBT.addTag(tID);

        // If applicable, fill item with attributes
        Multimap<Attribute, AttributeModifier> attribs = base.getItemMeta().getAttributeModifiers();
        ItemTag tDamage, tSpeed, tArmor, tArmorToughness, tMHealth, tMSpeed, tKRes, tLuck;

        // If there are any custom attributes
        if (attribs != null) {
            // Retrieve Values
            for (AttributeModifier atrb : attribs.get(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_ATTACK_DAMAGE))) { vDamage += atrb.getAmount(); }
            for (AttributeModifier atrb : attribs.get(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_ATTACK_SPEED))) { vSpeed += atrb.getAmount(); }
            for (AttributeModifier atrb : attribs.get(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_ARMOR))) { vArmor += atrb.getAmount(); }
            for (AttributeModifier atrb : attribs.get(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_ARMOR_TOUGHNESS))) { vArmorT += atrb.getAmount(); }
            for (AttributeModifier atrb : attribs.get(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_MOVEMENT_SPEED))) { mSpeedT += atrb.getAmount(); }
            for (AttributeModifier atrb : attribs.get(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_MAX_HEALTH))) { mHealthT += atrb.getAmount(); }
            for (AttributeModifier atrb : attribs.get(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_KNOCKBACK_RESISTANCE))) { mKRes += atrb.getAmount(); }
            for (AttributeModifier atrb : attribs.get(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_LUCK))) { mLuck += atrb.getAmount(); }
        }

        // Create Tags
        tDamage = new ItemTag(ItemStats.ATTACK_DAMAGE.getNBTPath(), vDamage);
        tSpeed = new ItemTag(ItemStats.ATTACK_SPEED.getNBTPath(), vSpeed);
        tArmor = new ItemTag(Gunging_Ootilities_Plugin.useMMOLibDefenseConvert ? ItemStats.DEFENSE.getNBTPath() : ItemStats.ARMOR.getNBTPath(), vArmor);
        tArmorToughness = new ItemTag(ItemStats.ARMOR_TOUGHNESS.getNBTPath(), vArmorT);
        tMHealth = new ItemTag(ItemStats.MAX_HEALTH.getNBTPath(), mHealthT);
        tMSpeed = new ItemTag(ItemStats.MOVEMENT_SPEED.getNBTPath(), mSpeedT);
        tKRes = new ItemTag(ItemStats.KNOCKBACK_RESISTANCE.getNBTPath(), mKRes);
        tLuck = new ItemTag(LUCK.getNBTPath(), mLuck);

        // Append Tags
        if (vDamage != 0) { tNBT.addTag(tDamage); }
        if (vSpeed != 0) { tNBT.addTag(tSpeed); }
        if (vArmor != 0) { tNBT.addTag(tArmor); }
        if (vArmorT != 0) { tNBT.addTag(tArmorToughness); }
        if (mSpeedT != 0) { tNBT.addTag(tMSpeed); }
        if (mHealthT != 0) { tNBT.addTag(tMHealth); }
        if (mKRes != 0) { tNBT.addTag(tKRes); }
        if (mLuck != 0) { tNBT.addTag(tLuck); }

        // Enable Upgr8d
        tNBT = EnableParentTypeUpgrades(tNBT, tName, "VANILLA");

        // Complete Operation
        return tNBT;
    }

    @NotNull public static ItemStack ConvertVanillaToMMOItem(@NotNull ItemStack base) {
        return ConvertVanillaToMMOItem(base, null, null, false);
    }
    @NotNull public static ItemStack ConvertVanillaToMMOItem(@NotNull ItemStack base, @Nullable String forcedTypePrefix, @Nullable String forcedIDFormat, boolean forcedType) {

        // Store important nbt data
        int oCount = base.getAmount();
        ArrayList<String> oLore = OotilityCeption.GetLore(base);
        Map<Enchantment, Integer> oEnchantments = base.getEnchantments();
        boolean oUnbreak = base.getItemMeta().isUnbreakable();
        Integer oCMD = null;
        if (GooP_MinecraftVersions.GetMinecraftVersion() >= 14.0) { if (base.getItemMeta().hasCustomModelData()) { oCMD = base.getItemMeta().getCustomModelData(); } }

        // Convert it as NBT and then Build it
        NBTItem asNBT = GooPMMOItems.ConvertVanillaToMMOItemAsNBT(base, forcedTypePrefix, forcedIDFormat, forcedType);
        MMOItem mmoitem = LiveFromNBT(asNBT);
        if (mmoitem == null) { return base; }

        //region Set Original Datas
        // Build and add Enchant - If enchanted to begin with
        if (!oEnchantments.isEmpty()) {
            EnchantListData elData = new EnchantListData();
            for (Enchantment e : oEnchantments.keySet()) { elData.addEnchant(e, oEnchantments.get(e)); }
            mmoitem.mergeData(GooPMMOItems.Stat(GooPMMOItemsItemStats.ENCHANTS), elData, null);
        }

        // Lore Adding - If has lore to begin with
        if (oLore != null) {
            if (!oLore.isEmpty()) {
                StringListData slData = new StringListData(oLore);
                mmoitem.setData(GooPMMOItems.Stat(GooPMMOItemsItemStats.LORE), slData);
            }
        }

        // Unbreakable - If Unbrekable
        if (oUnbreak) {
            BooleanData bdData = new BooleanData(true);
            mmoitem.setData(GooPMMOItems.Stat(GooPMMOItemsItemStats.UNBREAKABLE), bdData);
        }

        // Custom Model Data if apliccable
        if (oCMD != null) {
            DoubleData cmdData = new DoubleData(oCMD + 0.0D);
            mmoitem.setData(GooPMMOItems.Stat(GooPMMOItemsItemStats.CUSTOM_MODEL_DATA), cmdData);
        }
        //endregion

        // Get Finished Product
        ItemStack ret = mmoitem.newBuilder().build();

        // Set original count
        ret.setAmount(oCount);
        return ret;
    }
    public static NBTItem EnableParentTypeUpgrades(NBTItem childNBT, String parentTypeName, String parentID) {

        // If type exists
        MMOItem parenTemplate = GetMMOItemRaw(parentTypeName, parentID);

        // Return if falure
        if (parenTemplate == null) { return childNBT; }

        UpgradeData uData = (UpgradeData) parenTemplate.getData(GooPMMOItems.Stat(GooPMMOItemsItemStats.UPGRADE));

        if(uData != null) {
            MMOItem m = LiveFromNBT(childNBT);

            m.setData(GooPMMOItems.Stat(GooPMMOItemsItemStats.UPGRADE), uData);

            return m.newBuilder().buildNBT();
        }

        return childNBT;
    }
    //endregion

    //region Modify MMOItem Attributes
    @Nullable public static ItemStack StatOps(@Nullable ItemStack iSource, @NotNull ItemStat stat, @Nullable String unidentifiedValue, @Nullable String unparsedRange, @Nullable RefSimulator<Double> finalValue, @Nullable RefSimulator<String> logAddition) {

        // Counter trash, convert non-mmoitems
        if (OotilityCeption.IsAirNullAllowed(iSource)) { return null; }
        if (!IsMMOItem(iSource)) { iSource = ConvertVanillaToMMOItem(iSource); }
        boolean readonly = "read".equals(unidentifiedValue);
        boolean clearStatSuccess = false;

        //STAT//OotilityCeption.Log("§7STAT§c OPS§7 Editing §3" + stat.getId() + "§7 of " + OotilityCeption.GetItemName(iSource) + "§7 into§e " + unidentifiedValue + "§7 if §b" + unparsedRange);

        // Get as NBT Item
        NBTItem iNBT = NBTItem.get(iSource);
        Type miType = GetMMOItemType(iNBT);

        // Cancel operation
        if (miType == null) {

            // Mention
            OotilityCeption.Log4Success(logAddition, !Gunging_Ootilities_Plugin.blockImportantErrorFeedback, "MMOItem type §e" + GetMMOItemTypeRaw(iNBT) + "§7 is not loaded. ");
            return null;
        }

        // Stat available?
        if (!miType.getAvailableStats().contains(stat)) {

            // Mention
            OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "MMOItem type §e" + GetMMOItemTypeRaw(iNBT) + "§7 does not support stat §3" + stat.getId() + "§7. ");

            // Well so it cannot be edited... but is it being read?
            if (readonly) {

                // Will succeed if the stat is clear
                clearStatSuccess = true;

            // Cannot be edited ~ fail
            } else { return null; }
        }

        // Open up
        LiveMMOItem mmo = new LiveMMOItem(iSource);

        // All right what treatment
        if (stat instanceof DoubleStat) {
            //STAT//OotilityCeption.Log("§7STAT§c OPS§7 Treating as §eDOUBLE DATA");

            // The value to put
            DoubleData endData;
            double expectedData = 0;

            // Get its data
            DoubleData current = (DoubleData) mmo.getData(stat);
            if (current == null) { current = new DoubleData(((DoubleData) stat.getClearStatData()).getValue()); }
            //STAT//OotilityCeption.Log("§7STAT§e DBD§7 Current:§b " + current.getValue());

            // Does it have a value?
            if (unidentifiedValue != null && !readonly && !clearStatSuccess) {

                // Parse value as number
                PlusMinusPercent value = PlusMinusPercent.GetPMP(unidentifiedValue, logAddition);

                // Failure
                if (value == null) {
                    OotilityCeption.Log4Success(logAddition, !Gunging_Ootilities_Plugin.blockImportantErrorFeedback, "Expected numeric operation (§b+4§7, §b12§7) instead of §e" + unidentifiedValue + "§7. ");

                    // Kap
                    return null;
                }

                // Perform operation
                expectedData = value.apply(current.getValue());

                //STAT//OotilityCeption.Log("§7STAT§e DBD§7 Expected:§b " + expectedData);

            // Just read bro
            } if (readonly) {
                expectedData = current.getValue();

                //STAT//OotilityCeption.Log("§7STAT§e DBD§7 Expected:§b" + expectedData + "§3 (read-only)");
            }

            // Is the expected data in range?
            if (unparsedRange != null) {

                // This thing
                QuickNumberRange qnr = QuickNumberRange.FromString(unparsedRange);

                // There should have been a range, excuse me.
                if (qnr == null) {
                    OotilityCeption.Log4Success(logAddition, !Gunging_Ootilities_Plugin.blockImportantErrorFeedback, "Expected numeric range (§b2..10§7, §b4..§7) instead of §e" + unparsedRange + "§7. ");

                    // Kap
                    return null;

                } else if (!qnr.InRange(expectedData)) {
                    OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Operation result §b" + expectedData + "§7 would not fall in range §e" + qnr.qrToString() + "§7; §cCancelling§7. ");

                    // Kap
                    return null;
                }

                //STAT//OotilityCeption.Log("§7STAT§e DBD§7 Expected within §e" + qnr.qrToString());
            }

            // Store final value
            if (finalValue != null) { finalValue.setValue(expectedData); }

            // Edit the data
            if (!readonly && !clearStatSuccess) {

                /*
                 * Since we are merging (additively), for the final value
                 * to be true, we will add an External Stat History (EXSH)
                 * of the difference between the current and desired
                 */
                endData = new DoubleData(current.getValue());
                endData.setValue(expectedData - current.getValue());

                //STAT//OotilityCeption.Log("§7STAT§e DBD§7 Difference:§b " + endData.getValue());

                // Get SH
                StatHistory hist = StatHistory.from(mmo, stat);

                // Register
                hist.registerExternalData(endData);
                if (hist.getExternalData().size() > doubleHist) { hist.consolidateEXSH(); }

                // Recalculate
                mmo.setData(stat, hist.recalculate(mmo.getUpgradeLevel()));

                // Notify
                OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Value §e" + stat.getId() + "§7 of " + OotilityCeption.GetItemName(iSource) + "§7 changed by §b" + endData.getValue() + "§7 into §b" + expectedData + "§7. ");

            // Read only notify
            } else {

                OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Value §e" + stat.getId() + "§7 of " + OotilityCeption.GetItemName(iSource) + "§7 was §b" + expectedData + "§7. ");
            }

        } else if (stat instanceof BooleanStat) {
            //STAT//OotilityCeption.Log("§7STAT§c OPS§7 Treating as §aBOOLEAN DATA");

            // The value to put
            BooleanData endData = null;
            boolean clearState = ((BooleanData) stat.getClearStatData()).isEnabled();
            boolean expectedData = clearState;

            // Get its data
            BooleanData current = (BooleanData) mmo.getData(stat);
            if (current == null) { current = new BooleanData(((BooleanData) stat.getClearStatData()).isEnabled()); }
            //STAT//OotilityCeption.Log("§7STAT§a BOL§7 Current:§b " + current.isEnabled());

            // Does it have a value?
            if (unidentifiedValue != null && !readonly && !clearStatSuccess) {

                // Flip if toggle
                if ("toggle".equalsIgnoreCase(unidentifiedValue)) { expectedData = !current.isEnabled(); } else {

                    // Parse value as number
                    Boolean value = SilentNumbers.BooleanParse(unidentifiedValue);

                    // Failure
                    if (value == null) {
                        OotilityCeption.Log4Success(logAddition, !Gunging_Ootilities_Plugin.blockImportantErrorFeedback, "Expected boolean (§btrue§7 or §bfalse§7) or §btoggle§7 instead of §e" + unidentifiedValue + "§7. ");

                        // Kap
                        return null;
                    }

                    // Perform operation
                    expectedData = value;
                }

                //STAT//OotilityCeption.Log("§7STAT§a BOL§7 Expected:§b" + expectedData);

            } else if (readonly) {
                expectedData = current.isEnabled();

                //STAT//OotilityCeption.Log("§7STAT§a BOL§7 Expected:§b" + expectedData + "§3 (read-only)");
            }

            // Is the expected data in range?
            if (unparsedRange != null) {

                // This thing
                Boolean qnr = SilentNumbers.BooleanParse(unparsedRange);

                // There should have been a range, excuse me.
                if (qnr == null) {
                    OotilityCeption.Log4Success(logAddition, !Gunging_Ootilities_Plugin.blockImportantErrorFeedback, "Expected boolean (§btrue§7 or §bfalse§7) instead of §e" + unparsedRange + "§7. ");

                    // Kap
                    return null;

                } else if (qnr != expectedData) {
                    OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Operation result §b" + expectedData + "§7 would not be the expected §e" + qnr + "§7; §cCancelling§7. ");

                    // Kap
                    return null;
                }

                //STAT//OotilityCeption.Log("§7STAT§a BOL§7 Expected matched §e" + qnr);
            }

            // Store final value
            if (finalValue != null) { finalValue.setValue(expectedData ? 1D : 0D); }

            // Make data
            if (expectedData != clearState) { endData = new BooleanData(expectedData); }

            if (!readonly && !clearStatSuccess) {

                //STAT//OotilityCeption.Log("§7STAT§a BOL§7 Result:§b " + endData);

                // Register
                if (endData == null) { mmo.removeData(stat); } else { mmo.setData(stat, endData); }

                // Notify
                OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Set value §e" + stat.getId() + "§7 of " + OotilityCeption.GetItemName(iSource) + "§7 to §b" + expectedData + "§7. ");

            } else {

                // Notify
                OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Value §e" + stat.getId() + "§7 of " + OotilityCeption.GetItemName(iSource) + "§7 was §b" + current.isEnabled() + "§7. ");
            }

        } else if (stat instanceof StringStat) {
            //STAT//OotilityCeption.Log("§7STAT§c OPS§7 Treating as §dSTRING DATA");

            // The value to put
            StringData endData;
            String expectedData = null;

            // Get its data
            StringData current = (StringData) mmo.getData(stat);
            if (current == null) { current = new StringData(((StringData) stat.getClearStatData()).getString()); }
            //STAT//OotilityCeption.Log("§7STAT§3 STR§7 Current:§b " + current);

            // Does it have a value? Replace those spaces and roll
            if (unidentifiedValue != null && !readonly && !clearStatSuccess) {
                expectedData = unidentifiedValue.replace("__", " ");
                //STAT//OotilityCeption.Log("§7STAT§3 STR§7 Expected:§b " + expectedData);
            } else if (readonly) {
                expectedData = current.getString();
                //STAT//OotilityCeption.Log("§7STAT§3 STR§7 Expected:§b " + expectedData + "§3 (read-only)");
            }
            //STAT//else { OotilityCeption.Log("§7STAT§3 STR§7 Expected:§b " + expectedData); }

            // Is the expected data?
            if (unparsedRange != null) {

                // They must be the same
                if (!unparsedRange.equals(expectedData)) {

                    OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Operation result §b" + expectedData + "§7 would not be the expected §e" + unparsedRange + "§7; §cCancelling§7. ");

                    // Kap
                    return null;
                }

                //STAT//OotilityCeption.Log("§7STAT§3 STR§7 Expected Matched §e " + unparsedRange);
            }

            // Store final value
            if (finalValue != null) { finalValue.setValue(expectedData != null ? 1D : 0D); }
            if (expectedData == null) { expectedData = ""; }

            if (!readonly && !clearStatSuccess) {

                // What ill be the latest string data?
                endData = new StringData(expectedData);

                // Mergeable allows stat hisotry operation
                if (stat instanceof Mergeable) {

                    // Get SH
                    StatHistory hist = StatHistory.from(mmo, stat);

                    // Register
                    hist.registerExternalData(endData);
                    if (hist.getExternalData().size() > stringHist) { hist.consolidateEXSH(); }

                    // Recalculate
                    mmo.setData(stat, hist.recalculate(mmo.getUpgradeLevel()));

                // Not mergeable = just override data
                } else { mmo.setData(stat, endData); }

                // Notify
                OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Set value §e" + stat.getId() + "§7 of " + OotilityCeption.GetItemName(iSource) + "§7 to §b" + endData.getString() + "§7. ");

            // Read only notify
            } else {

                OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Value §e" + stat.getId() + "§7 of " + OotilityCeption.GetItemName(iSource) + "§7 was §b" + current.getString() + "§7. ");
            }

        } else if (stat instanceof StringListStat) {
            //STAT//OotilityCeption.Log("§7STAT§c OPS§7 Treating as §9STRING LIST DATA");

            // The value to put
            StringListData endData;
            StringListData currentData = (StringListData) mmo.getData(stat);
            ArrayList<String> expectedData = currentData == null ? new ArrayList<>() : new ArrayList<>(currentData.getList());
            ArrayList<String> addedData = new ArrayList<>();
            //STAT//OotilityCeption.Log("§7STAT§9 STL§7 Current size: §b " + expectedData.size());

            // What kinda mode is it
            boolean removeMode = false;
            boolean clearMode = false;
            boolean actuallyRemoved = false;

            // Does it have a value? Replace those spaces and roll
            if (unidentifiedValue != null && !readonly && !clearStatSuccess) {

                // Remove mode: Starts with a minus
                if (unidentifiedValue.startsWith("-")) {
                    removeMode = true;
                    unidentifiedValue = unidentifiedValue.substring(1);

                    // Clear Mode
                    clearMode = "all".equals(unidentifiedValue);
                    //STAT//OotilityCeption.Log("§7STAT§9 STL§7 Clear mode? §3 " + clearMode + "§8 " + unidentifiedValue);
                }

                // Process spaces
                unidentifiedValue = unidentifiedValue.replace("__", " ");

                // Perform operation
                if (removeMode) {

                    // Clearing??? :flushed:
                    if (clearMode) {
                        actuallyRemoved = !expectedData.isEmpty();
                        expectedData.clear();

                    } else { actuallyRemoved = expectedData.remove(unidentifiedValue); }

                    //STAT//OotilityCeption.Log("§7STAT§9 STL§7 Removing: §b " + unidentifiedValue + "§8 (Succ?§3 " + actuallyRemoved + "§8)");
                } else {
                    expectedData.add(unidentifiedValue);
                    addedData.add(unidentifiedValue);
                    //STAT//OotilityCeption.Log("§7STAT§9 STL§7 Added: §b " + unidentifiedValue);
                }

            } else if (readonly) {
                removeMode = true;
                actuallyRemoved = false;
                //STAT//OotilityCeption.Log("§7STAT§9 STL§7 (read only)");

            // Not a list operation
            } else {
                OotilityCeption.Log4Success(logAddition, !Gunging_Ootilities_Plugin.blockImportantErrorFeedback, "You must specify a value to use with String List Stats. ");

                // Kap
                return null;
            }

            // Is the expected data?
            if (unparsedRange != null) {

                // This thing
                QuickNumberRange qnr = QuickNumberRange.FromString(unparsedRange);

                // There should have been a range, excuse me.
                if (qnr == null) {

                    if (!expectedData.contains(unparsedRange)) {

                        OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "The result would not have §3" + unparsedRange + "§7; §cCancelling§7. ");

                        // Kap
                        return null;
                    }

                    //STAT//OotilityCeption.Log("§7STAT§9 STL§7 Final data§a had§b " + unparsedRange);

                } else if (!qnr.InRange(expectedData.size())) {
                    OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Operation result would have §b" + expectedData.size() + "§7 entries, so it would not fall in range §e" + qnr.qrToString() + "§7; §cCancelling§7. ");

                    // Kap
                    return null;
                }

                //STAT//else { OotilityCeption.Log("§7STAT§9 STL§7 Expected List Size§b " + expectedData.size() + "§7 was in range§e " + qnr.qrToString()); }
            }

            // Store final value
            if (finalValue != null) { finalValue.setValue((double) expectedData.size()); }

            if (!readonly && !clearStatSuccess) {

                // Get SH
                StatHistory hist = StatHistory.from(mmo, stat);

                // Was it additive?
                if (!removeMode) {

                    /*
                     * Since we are merging (additively), for the final value
                     * to be true, we will add an External Stat History (EXSH)
                     * of the difference between the current and desired
                     */
                    endData = new StringListData(addedData);

                    // Register
                    hist.registerExternalData(endData);
                    if (hist.getExternalData().size() > listHist) { hist.consolidateEXSH(); }

                    // Update
                    mmo.setData(stat, hist.recalculate(mmo.getUpgradeLevel()));

                // Did the operation even work?
                } else if (actuallyRemoved) {
                    //STAT//OotilityCeption.Log("§7STAT§9 STL§7 Removing data from the Stat History....");

                    // Clearing?? :flushed:
                    if (clearMode) {
                        //STAT//OotilityCeption.Log("§7STAT§9 STL§b-CLEAR§7 Clearing Stat History....");

                        // Removing EXSH
                        hist.clearExternalData();

                        // Removing GEMS
                        hist.clearGemstones();

                        // Removing MODS
                        hist.clearModifiersBonus();

                        // Finally, original data
                        ArrayList<String> lst = new ArrayList<>(((StringListData) hist.getOriginalData()).getList());
                        for (String str : lst) {
                            //STAT//OotilityCeption.Log("§7STAT§9 STL§b-CLEAR§7 Removing§b " + str);

                            // Remove via the provided methode
                            try {
                                /*CURRENT-MMOITEMS*/((StringListData) hist.getOriginalData()).remove(str);
                                //YE-OLDEN-MMO//slData.getList().remove(unidentifiedValue);

                                // That's not good
                            } catch (Exception ex) {
                                OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "§cCould not remove §e" + str + "§7c from " + stat.getId() + " for unknown reasons.§7 ");

                                // Kap
                                return null;
                            }

                            /*CURRENT-MMOITEMS*/((StringListData) hist.getOriginalData()).remove(unidentifiedValue);
                            //YE-OLDEN-MMO//((StringListData) hist.getOriginalData()).getList().remove(unidentifiedValue);
                        }

                        //STAT//OotilityCeption.Log("§7STAT§9 STL§b-CLEAR§7 Result");
                        //STAT//for (String str : ((StringListData) hist.getOriginalData()).getList()) { OotilityCeption.Log("§7STAT§9 STL§b-CLEAR§7 Data: §b " + str); }

                    } else {
                        //STAT//OotilityCeption.Log("§7STAT§9 STL§b-REM§7 Removing '§b" + unidentifiedValue + "' Stat History....");

                        boolean foundAndDestroyed = false;

                        /*
                         * Removing an entry is more tricky, we must actually
                         * find and destroy it --- EXSH
                         */
                        for (StatData slData : hist.getExternalData()) {

                            // Fin
                            if (foundAndDestroyed) { break; }

                            // Skip this
                            if (!(slData instanceof StringListData)) { continue; }

                            // Does it have it? No? Skip it then
                            if (!((StringListData) slData).getList().contains(unidentifiedValue)) { continue; }

                            // Remove via the provided methode
                            try {
                                /*CURRENT-MMOITEMS*/foundAndDestroyed = ((StringListData) slData).remove(unidentifiedValue);
                                //YE-OLDEN-MMO//foundAndDestroyed = ((StringListData) slData).getList().remove(unidentifiedValue);

                                // That's not good
                            } catch (Exception ex) {
                                OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "§cCould not remove §e" + unidentifiedValue + "§7c from " + stat.getId() + " for unknown reasons.§7 ");

                                // Kap
                                return null;
                            }
                        }

                        /*
                         * Removing an entry is more tricky, we must actually
                         * find and destroy it --- GEMS
                         */
                        for (UUID gemStone : hist.getAllGemstones()) {

                            // Fin
                            if (foundAndDestroyed) { break; }

                            // Get that gem's data
                            StatData slData = hist.getGemstoneData(gemStone);

                            // Skip this
                            if (!(slData instanceof StringListData)) { continue; }

                            // Does it have it? No? Skip it then
                            if (!((StringListData) slData).getList().contains(unidentifiedValue)) { continue; }

                            // Remove via the provided methode
                            try {
                                /*CURRENT-MMOITEMS*/foundAndDestroyed = ((StringListData) slData).remove(unidentifiedValue);
                                //YE-OLDEN-MMO//foundAndDestroyed = ((StringListData) slData).getList().remove(unidentifiedValue);

                                // That's not good
                            } catch (Exception ex) {
                                OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "§cCould not remove §e" + unidentifiedValue + "§7c from " + stat.getId() + " for unknown reasons.§7 ");

                                // Kap
                                return null;
                            }
                        }

                        /*
                         * Removing an entry is more tricky, we must actually
                         * find and destroy it --- MODS
                         */
                        for (UUID modifier : hist.getAllModifiers()) {

                            // Fin
                            if (foundAndDestroyed) { break; }

                            // Get that gem's data
                            StatData slData = hist.getModifiersBonus(modifier);

                            // Skip this
                            if (!(slData instanceof StringListData)) { continue; }

                            // Does it have it? No? Skip it then
                            if (!((StringListData) slData).getList().contains(unidentifiedValue)) { continue; }

                            // Remove via the provided methode
                            try {
                                /*CURRENT-MMOITEMS*/foundAndDestroyed = ((StringListData) slData).remove(unidentifiedValue);
                                //YE-OLDEN-MMO//foundAndDestroyed = ((StringListData) slData).getList().remove(unidentifiedValue);

                                // That's not good
                            } catch (Exception ex) {
                                OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "§cCould not remove §e" + unidentifiedValue + "§7c from " + stat.getId() + " for unknown reasons.§7 ");

                                // Kap
                                return null;
                            }
                        }

                        // Finally, original data
                        if (!foundAndDestroyed) {

                            // Get that gem's data
                            StringListData slData = (StringListData) hist.getOriginalData();

                            // Does it have it? No? Skip it then
                            if (slData.getList().contains(unidentifiedValue)) {


                                // Remove via the provided methode
                                try {
                                    /*CURRENT-MMOITEMS*/slData.remove(unidentifiedValue);
                                    //YE-OLDEN-MMO//slData.getList().remove(unidentifiedValue);

                                    // That's not good
                                } catch (Exception ex) {
                                    OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "§cCould not remove §e" + unidentifiedValue + "§7c from " + stat.getId() + " for unknown reasons.§7 ");

                                    // Kap
                                    return null;
                                }
                            }
                        }
                    }

                    // Yeah, update
                    mmo.setData(stat, hist.recalculate(mmo.getUpgradeLevel()));

                } else {
                    OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Could not remove §3" + unidentifiedValue + "§7 because it was§c not in the list§7 initially. ");

                    // Kap
                    return null;
                }

                StringListData finalData = (StringListData) mmo.getData(stat);
                ArrayList<String> finalList = finalData == null ? new ArrayList<>() : new ArrayList<>(finalData.getList());

                //STAT//for (String str : finalList) { OotilityCeption.Log("§7STAT§9 STL§7 Final data: §b " + str); }

                OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Changed list §e" + stat.getId() + "§7 of " + OotilityCeption.GetItemName(iSource) + "§7 to have §b" + finalList.size() + "§7 entries. ");

            } else {

                StringListData finalData = (StringListData) mmo.getData(stat);
                ArrayList<String> finalList = finalData == null ? new ArrayList<>() : new ArrayList<>(finalData.getList());

                //STAT//for (String str : finalList) { OotilityCeption.Log("§7STAT§9 STL§7 Final data: §b " + str); }

                OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "List §e" + stat.getId() + "§7 of " + OotilityCeption.GetItemName(iSource) + "§7 had §b" + finalList.size() + "§7 entries. ");
            }

        } else if (stat == ItemStats.GEM_SOCKETS) {
            //STAT//OotilityCeption.Log("§7STAT§c OPS§7 Treating as §fGEM SOCKETS DATA");

            // The value to put
            GemSocketsData endData;
            GemSocketsData currentData = (GemSocketsData) mmo.getData(stat);
            ArrayList<String> expectedData = currentData == null ? new ArrayList<>() : new ArrayList<>(currentData.getEmptySlots());
            ArrayList<String> addedData = new ArrayList<>();
            //STAT//OotilityCeption.Log("§7STAT§f GEM§7 Current size: §b " + expectedData.size());

            // What kinda mode is it
            boolean removeMode = false;
            boolean clearMode = false;
            boolean actuallyRemoved = false;

            // Does it have a value? Replace those spaces and roll
            if (unidentifiedValue != null && !readonly && !clearStatSuccess) {

                // Remove mode: Starts with a minus
                if (unidentifiedValue.startsWith("-")) {
                    removeMode = true;
                    unidentifiedValue = unidentifiedValue.substring(1);

                    // Clear Mode
                    clearMode = "all".equals(unidentifiedValue);
                    //STAT//OotilityCeption.Log("§7STAT§f GEM§7 Clear mode? §3 " + clearMode + "§8 " + unidentifiedValue);
                }

                // Process spaces
                unidentifiedValue = unidentifiedValue.replace("__", " ");

                // Perform operation
                if (removeMode) {

                    // Clearing??? :flushed:
                    if (clearMode) {
                        actuallyRemoved = !expectedData.isEmpty();
                        expectedData.clear();

                    } else { actuallyRemoved = expectedData.remove(unidentifiedValue); }

                    //STAT//OotilityCeption.Log("§7STAT§f GEM§7 Removing: §b " + unidentifiedValue + "§8 (Succ?§3 " + actuallyRemoved + "§8)");
                } else {
                    expectedData.add(unidentifiedValue);
                    addedData.add(unidentifiedValue);
                    //STAT//OotilityCeption.Log("§7STAT§f GEM§7 Added: §b " + unidentifiedValue);
                }

            } else if (readonly) {
                removeMode = true;
                actuallyRemoved = false;
                //STAT//OotilityCeption.Log("§7STAT§f GEM§7 (read only)");

                // Not a list operation
            } else {
                OotilityCeption.Log4Success(logAddition, !Gunging_Ootilities_Plugin.blockImportantErrorFeedback, "You must specify a value to use with String List Stats. ");

                // Kap
                return null;
            }

            // Is the expected data?
            if (unparsedRange != null) {

                // This thing
                QuickNumberRange qnr = QuickNumberRange.FromString(unparsedRange);

                // There should have been a range, excuse me.
                if (qnr == null) {

                    if (!expectedData.contains(unparsedRange)) {

                        OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "The result would not have §3" + unparsedRange + "§7; §cCancelling§7. ");

                        // Kap
                        return null;
                    }

                    //STAT//OotilityCeption.Log("§7STAT§f GEM§7 Final data§a had§b " + unparsedRange);

                } else if (!qnr.InRange(expectedData.size())) {
                    OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Operation result would have §b" + expectedData.size() + "§7 entries, so it would not fall in range §e" + qnr.qrToString() + "§7; §cCancelling§7. ");

                    // Kap
                    return null;
                }

                //STAT//else { OotilityCeption.Log("§7STAT§f GEM§7 Expected List Size§b " + expectedData.size() + "§7 was in range§e " + qnr.qrToString()); }
            }

            // Store final value
            if (finalValue != null) { finalValue.setValue((double) expectedData.size()); }

            if (!readonly && !clearStatSuccess) {

                // Get SH
                StatHistory hist = StatHistory.from(mmo, stat);

                // Was it additive?
                if (!removeMode) {

                    /*
                     * Since we are merging (additively), for the final value
                     * to be true, we will add an External Stat History (EXSH)
                     * of the difference between the current and desired
                     */
                    endData = new GemSocketsData(addedData);

                    // Register
                    hist.registerExternalData(endData);

                    // Update
                    mmo.setData(stat, hist.recalculate(mmo.getUpgradeLevel()));

                    // Did the operation even work?
                } else if (actuallyRemoved) {

                    // Clearing?? :flushed:
                    if (clearMode) {

                        // Removing EXSH
                        hist.clearExternalData();

                        // Removing GEMS
                        hist.clearGemstones();

                        // Removing MODS
                        hist.clearModifiersBonus();

                        // Finally, original data
                        ArrayList<String> lst = new ArrayList<>(((GemSocketsData) hist.getOriginalData()).getEmptySlots());
                        for (String str : lst) { ((GemSocketsData) hist.getOriginalData()).getEmptySlots().remove(str); }

                    } else {

                        boolean foundAndDestroyed = false;

                        /*
                         * Removing an entry is more tricky, we must actually
                         * find and destroy it --- EXSH
                         */
                        for (StatData slData : hist.getExternalData()) {

                            // Fin
                            if (foundAndDestroyed) { break; }

                            // Skip this
                            if (!(slData instanceof GemSocketsData)) { continue; }

                            // Does it have it? No? Skip it then
                            foundAndDestroyed = ((GemSocketsData) slData).getEmptySlots().remove(unidentifiedValue);
                        }

                        /*
                         * Removing an entry is more tricky, we must actually
                         * find and destroy it --- GEMS
                         */
                        for (UUID gemStone : hist.getAllGemstones()) {

                            // Fin
                            if (foundAndDestroyed) { break; }

                            // Get that gem's data
                            StatData slData = hist.getGemstoneData(gemStone);

                            // Skip this
                            if (!(slData instanceof GemSocketsData)) { continue; }

                            // Does it have it? No? Skip it then
                            foundAndDestroyed = ((GemSocketsData) slData).getEmptySlots().remove(unidentifiedValue);
                        }

                        /*
                         * Removing an entry is more tricky, we must actually
                         * find and destroy it --- MODS
                         */
                        for (UUID modifier : hist.getAllModifiers()) {

                            // Fin
                            if (foundAndDestroyed) { break; }

                            // Get that gem's data
                            StatData slData = hist.getModifiersBonus(modifier);

                            // Skip this
                            if (!(slData instanceof GemSocketsData)) { continue; }

                            // Does it have it? No? Skip it then
                            foundAndDestroyed = ((GemSocketsData) slData).getEmptySlots().remove(unidentifiedValue);
                        }

                        // Finally, original data
                        if (!foundAndDestroyed) {

                            // Get that gem's data
                            GemSocketsData slData = (GemSocketsData) hist.getOriginalData();

                            // Does it have it? No? Skip it then
                            slData.getEmptySlots().remove(unidentifiedValue);
                        }
                    }

                    // Yeah, update
                    mmo.setData(stat, hist.recalculate(mmo.getUpgradeLevel()));

                } else {
                    OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Could not remove §3" + unidentifiedValue + "§7 because it was§c not in the list§7 initially. ");

                    // Kap
                    return null;
                }

                GemSocketsData finalData = (GemSocketsData) mmo.getData(stat);
                ArrayList<String> finalList = finalData == null ? new ArrayList<>() : new ArrayList<>(finalData.getEmptySlots());

                //STAT//for (String str : finalList) { OotilityCeption.Log("§7STAT§f GEM§7 Final data: §b " + str); }

                OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Empty gem sockets of " + OotilityCeption.GetItemName(iSource) + "§7 changed to a total of §b" + finalList.size() + "§7. ");

            } else {

                GemSocketsData finalData = (GemSocketsData) mmo.getData(stat);
                ArrayList<String> finalList = finalData == null ? new ArrayList<>() : new ArrayList<>(finalData.getEmptySlots());

                //STAT//for (String str : finalList) { OotilityCeption.Log("§7STAT§f GEM§7 Final data: §b " + str); }

                OotilityCeption.Log4Success(logAddition, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Empty gem sockets of " + OotilityCeption.GetItemName(iSource) + "§7 were a total of §b" + finalList.size() + "§7. ");
            }
        }

        // Yeah that was a good run ngl
        return mmo.newBuilder().build();
    }

    /**
     * @param section Section of the GooP config 'StatHistories'
     */
    public static void readStatHistoryLimits(@Nullable ConfigurationSection section) {

        // No section? Uuuh
        if (section == null) {

            // Reset
            doubleHist = -1;
            stringHist = -1;
            listHist = -1;
            return; }

        // Read values
        doubleHist = section.getInt("DoubleStat", -1);
        stringHist = section.getInt("StringStat", -1);
        listHist = section.getInt("StringListStat", -1);
    }
    static int doubleHist = -1;
    static int stringHist = -1;
    static int listHist = -1;

    /**
     * Upgrades a MMOItem to this level
     *
     * @param base ItemStack that may not be an MMOItem
     * @param toLevel Level operaton
     * @param breakLimit if it should overshoot the max upgrade level of an item
     * @return <code>null</code> if anything goes wrong
     */
    @Nullable public static ItemStack UpgradeMMOItem(@Nullable ItemStack base, @Nullable PlusMinusPercent toLevel, boolean breakLimit, @Nullable RefSimulator<Integer> finalLevel, @Nullable RefSimulator<String> logger) {

        // Neh
        if (OotilityCeption.IsAirNullAllowed(base)) {
            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Cant upgrade air! ");
            return null; }

        // Is MMOItem right
        NBTItem nbt = NBTItem.get(base);
        if (!nbt.hasType()) {
            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Only MMOItems can be upgraded. ");
            return null; }

        // Check volatile
        VolatileMMOItem vol = VolatileFromNBT(nbt);
        if (!vol.hasUpgradeTemplate()) {
            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "This MMOItem has no Upgrade Template (§citem not upgradable§7). ");
            return null; }

        // Live
        LiveMMOItem live = LiveFromNBT(nbt);

        int liveUpgradeLevel = live.getUpgradeLevel();
        if (toLevel == null) {

            // Just read level
            if (finalLevel != null) { finalLevel.setValue(liveUpgradeLevel); }
            return base;
        }

        // What level will it be?
        int result = SilentNumbers.floor(toLevel.apply((double) liveUpgradeLevel));

        // Respect limit
        boolean limited = false;

        if (!breakLimit && (result > live.getMaxUpgradeLevel()) && live.getMaxUpgradeLevel() > 0) { result = live.getMaxUpgradeLevel(); limited = true; }

        live.getUpgradeTemplate().upgradeTo(live, result);
        if (finalLevel != null) { finalLevel.setValue(result); }

        if (limited) {

            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Upgraded " + OotilityCeption.GetItemName(base) + "§7 to maximum level §b" + result + "§7. ");
        } else {

            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Upgraded " + OotilityCeption.GetItemName(base) + "§7 to level §b" + result + "§7. ");
        }

        // Deal
        return live.newBuilder().build();
    }

    /**
     * Identifies a stack of Unidentified MMOItems
     *
     * @param base ItemStack that may not be an MMOItem
     * @return <code>null</code> if anything goes wrong
     */
    @Nullable public static ItemStack IdentifyMMOItem(@Nullable ItemStack base, @Nullable RefSimulator<String> logger) {

        // Neh
        if (OotilityCeption.IsAirNullAllowed(base)) {
            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Cant identify air! ");
            return null; }

        // Is Unidentified right
        if (!IsUnidentified(base)) {
            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, OotilityCeption.GetItemName(base) + "§7 is not an unidentified MMOItem. ");
            return null; }

        // Get NBT
        ItemStack result = (new IdentifiedItem(NBTItem.get(base)).identify());

        // Log success
        OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Identified " + OotilityCeption.GetItemName(result) + "§7. ");

        // Deal
        return result;
    }

    /**
     * Will return the MMOItems lore of this. Not the vanilla.
     */
    @NotNull
    public static ArrayList<String> MMOItemGetLore(@Nullable ItemStack base) {

        // Gemstone Support is Enabled (Correct MMOItems version)
        if (IsMMOItem(base)) {

            // I hope that bitch is not null to begin with
            if (base != null) {

                // Test of existance of such type and IDs
                try {

                    // Attempt to get MMOItem Instance
                    MMOItem mmoitem = VolatileFromNBT(NBTItem.get(base));
                    if (mmoitem == null) { return null; }

                    if (!mmoitem.hasData(GooPMMOItems.Stat(GooPMMOItemsItemStats.LORE))) { return new ArrayList<>(); }

                    // Get Socket Data
                    StringListData loreData = (StringListData) mmoitem.getData(GooPMMOItems.Stat(GooPMMOItemsItemStats.LORE));

                    // Is it the first or last
                    ArrayList<String> iLore = new ArrayList<>();

                    // Has the items any sockets already?
                    if (loreData != null) {

                        // Get original lore
                        if (loreData.getList() != null) { iLore = new ArrayList<>(loreData.getList()); }

                        // Return thay
                        return iLore;
                    }

                    // Had no lore.
                    return new ArrayList<>();

                    // Seems that those Types/IDs did not match wth
                } catch (NullPointerException e) {

                    // Hmph
                    return new ArrayList<>();
                }
            }
        }

        // Empty
        return new ArrayList<>();
    }
    /**
     * Will set the MMOItems lore of this. Fails if its not an MMOItem
     */
    @Nullable
    public static ItemStack MMOItemSetLore(@Nullable ItemStack base, @Nullable ArrayList<String> lore) {
        if (lore == null) { lore = new ArrayList<>(); }
        if (base == null) { return null; }

        // Test of existance of such type and IDs
        try {

            // Attempt to get MMOItem Instance
            MMOItem mmoitem = VolatileFromNBT(NBTItem.get(base));
            if (mmoitem == null) { return null; }

            // Get Socket Data
            StringListData loreData = (StringListData) mmoitem.getData(GooPMMOItems.Stat(GooPMMOItemsItemStats.LORE));
            StringListData finalLore  = new StringListData(lore);

            // Has the items any sockets already?
            if (loreData != null) {

                // Remove momentaneously
                mmoitem.removeData(GooPMMOItems.Stat(GooPMMOItemsItemStats.LORE));
            }

            // Append it to the mmoitem
            mmoitem.setData(GooPMMOItems.Stat(GooPMMOItemsItemStats.LORE), finalLore);

            // Get Finished Product
            return mmoitem.newBuilder().build();

            // Seems that those Types/IDs did not match wth
        } catch (NullPointerException e) {

            // Hmph
            return null;
        }
    }
    public static ItemStack MMOItemAddLoreLine(ItemStack base, String loreLine, Integer index, RefSimulator<String> logger) {

        // I hope that bitch is not null to begin with
        if (base != null) {

            // Result
            ItemStack result = null;

            // Gettat Target NBT
            NBTItem tNBT = NBTItem.get(base);

            // Kount
            int kount = base.getAmount();

            // Is it an mmoItem to begin with?
            if (tNBT.hasType()) {

                // Add lore heck
                result = AddLoreLine(tNBT, loreLine, index);

                // Log if Appropiate
                if (result != null) {

                    // Gunging_Ootilities_Plugin.theOots.CLog("§3-------------Resultant Lore--------------");
                    // for (String str : result.getLore()) { Gunging_Ootilities_Plugin.theOots.CLog("§e - §7" + str); }
                    OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Successfully added lore line " + OotilityCeption.ParseColour(loreLine) + "§7 to " + OotilityCeption.GetItemName(base));

                } else {

                    OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Index out of range!");
                }
            }

            // Set
            if (!OotilityCeption.IsAirNullAllowed(result)) { result.setAmount(kount); }

            return result;

            // This man passing on a null item wth
        } else {

            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Cant add lore lines to air!");
            return null;
        }
    }
    public static ItemStack MMOItemRemoveLoreLine(ItemStack base, Integer index, RefSimulator<String> logger) { return MMOItemRemoveLoreLine(base, index, false, logger); }
    public static ItemStack MMOItemRemoveLoreLine(ItemStack base, Integer index, boolean removeAll, RefSimulator<String> logger) {

        // I hope that is not null to begin with
        if (base != null) {

            // Result
            ItemStack result = null;

            // Gettat Target NBT
            NBTItem tNBT = NBTItem.get(base);

            // Kount
            int kount = base.getAmount();

            // Is it an mmoItem to begin with?
            if (tNBT.hasType()) {

                // Add lore heck
                result = RemoveLoreLine(tNBT, index, removeAll);

                // Log if Appropiate
                if (result != null) {

                    OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Successfully removed lore line from "+ OotilityCeption.GetItemName(base));

                } else {

                    OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Index out of range!");
                }
            }

            // Set
            if (!OotilityCeption.IsAirNullAllowed(result)) { result.setAmount(kount); }

            return result;

            // This man passing on a null item wth
        } else {

            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Cant remove lore lines from air!");
            return null;
        }
    }
    public static ItemStack MMOItemModifyAttribute(ItemStack base, PlusMinusPercent operation, Attribute attrib, RefSimulator<Double> reslt, RefSimulator<String> logger) {

        // I hope that bitch is not null to begin with
        if (base != null) {

            // Result
            ItemStack result = null;

            // Gettat Target NBT
            NBTItem tNBT = NBTItem.get(base);

            // Store count?
            int kount = base.getAmount();

            // Is it an mmoItem to begin with?
            if (tNBT.hasType()) {

                // Add lore heck
                result = ModifyAttrib(tNBT, operation, attrib, reslt);

                // Log if Appropiate
                if (result != null) {

                    OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Modified attribute §3" + attrib.name() + "§7 of "+ OotilityCeption.GetItemName(base));

                } else {

                    OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "§7Attribute §3" + attrib.name() + "§7 doesnt seem to be supported as to modify its MMOItem counterpart.");
                }
            }

            // Set amount
            if (!OotilityCeption.IsAirNullAllowed(result)) { result.setAmount(kount); }

            return result;

            // This man passing on a null item wth
        } else {

            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Cant edit attributes of air!");
            return null;
        }
    }

    @Nullable public static ItemStack FixStackableness(@Nullable ItemStack base, @Nullable RefSimulator<String> logger) {

        // I hope that bitch is not null to begin with
        if (base != null) {

            // Kount
            int kount = base.getAmount();

            // Gettat Target NBT
            NBTItem b = NBTItem.get(base);

            // Is it an mmoItem t
            // o begin with?
            if (b.hasType()) {

                // Test of existance of such type and IDs
                try {

                    // Re MMOItemize it
                    MMOItem tMMO = new LiveMMOItem(b);

                    /*
                    // GEt Type
                    Type typ = GooPMMOItems.GetMMOItemType(b);
                    if (typ == null) { return null; }

                    // Get all available stats
                    ArrayList<ItemStat> aStats = new ArrayList<>(typ.getAvailableStats());

                    // Attempt to get MMOItem Instance
                    MMOItem mmoitem = LiveFromNBT(b);
                    if (mmoitem == null) { return null; }

                    // Get ALl Stats from thay item
                    HashMap<ItemStat, StatData> dStats = new HashMap<>();
                    for (ItemStat statt : aStats) {

                        // Git Data
                        StatData datt = mmoitem.getData(statt);

                        // Log ig
                        //OotilityCeption. Log("-----------------");
                        //OotilityCeption. Log("Stat: §3" + statt.getName());

                        // Contained?
                        if (datt != null) {

                            //region Special Cases (apparently)

                            // This one causes a cast exception; Seems to be retrieved as StringData but should be StringListData
                            if (statt.equals(GooPMMOItems.Stat(GooPMMOItemsItemStats.ITEM_TYPE_RESTRICTION))) {

                                // Is it a damn string data?
                                if (datt instanceof StringData){

                                    // Get String bruh
                                    String datContent = ((StringData) datt).toString();

                                    // Make StringListDatae
                                    StringListData nDatContent = new StringListData();
                                    nDatContent.getList().add(datContent);

                                    //Override
                                    datt = nDatContent;
                                }
                            }

                            // kk
                            //OotilityCeption. Log("N: §e" + datt.getClass().getName());
                            //OotilityCeption. Log("C: §e" + datt.getClass().getCanonicalName());
                            //OotilityCeption. Log("S: §e" + datt.getClass().getSimpleName());
                            //OotilityCeption. Log("T: §e" + datt.getClass().getTypeName());
                            //endregion

                            // Save
                            dStats.put(statt, datt);
                        }
                    }

                    // Save &7 Lore Lines
                    ItemMeta iMeta = b.getItem().getItemMeta();
                    //ArrayList<String> iLore = new ArrayList<>(iMeta.getLore());
                    ArrayList<String> keptLore = new ArrayList<>();
                    //for (String str : iLore) { if (str.startsWith("§7")) { keptLore.add(str); } }

                    // Extract Tier and Type
                    RefSimulator<String> mType = new RefSimulator<>(""), mID = new RefSimulator<>("");
                    GooPMMOItems.GetMMOItemInternals(b.getItem(), mType, mID);

                    // Refresh
                    ItemStack tSource = GooPMMOItems.GetMMOItem(mType.getValue(), mID.getValue());
                    NBTItem tNBT = NBTItem.get(tSource);
                    MMOItem tMMO = LiveFromNBT(tNBT);
                    if (tMMO == null) { return null; }

                    // Append
                    for (ItemStat statt : dStats.keySet()) {

                        // Edit Lore
                        if (statt == GooPMMOItems.Stat(GooPMMOItemsItemStats.LORE)) {

                            // Append Lore
                            StringListData slDat = (StringListData) dStats.get(statt);

                            // Add
                            slDat.getList().addAll(keptLore);
                        }

                        // Append
                        tMMO.setData(statt, dStats.get(statt));
                    }
                    //*/

                    OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Updated MMOItem to latest format (so it can stack). ");

                    // Build
                    ItemStack built = tMMO.newBuilder().build();
                    built.setAmount(kount);
                    return built;

                    // Seems that those Types/IDs did not match wth
                } catch (Exception e) {

                    OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Could not update MMOItem to latest format. ");
                    return null;
                }

                    /*
                    try {

                        // Add lore heck
                        if (version_6_0_1) {

                            // Load 6.0 Method
                            result = GooPMMOItems_6_0.Regenerate(tNBT);

                        } else {

                            // Load 5.5- Method
                            result = GooPMMOItems_Pre6.Regenerate(tNBT);
                        }

                        OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Updated MMOItem to latest format (so it can stack). ");

                    } catch (Exception ignored) {

                        OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "For reasons unknown, could not fix stackableness of " + OotilityCeption.GetItemName(base) +"! ");
                        return null;
                    }       //*/

            } else {

                OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "That is not a MMOItem! ");

                return null;
            }

            // Set

        // This man passing on a null item wth
        } else {

            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Can't fix stackableness of air! ");
            return null;
        }
    }

    /**
     * Performs an operation on the durability of an item. If it succeeded on setting the durability, it will return the modified ItemStack.
     * <p>Will target MMOItems durability if applicable.</p> Does not bypass Unbreakable attribute.
     * <p></p>
     * Reasons it could fail: <p>
     * + The item does not exist </p>
     * + The item cannot be damaged (say, a stone block) <p>
     * <p><p>
     * If it would break by receiving such damage, it is prevented from breaking, and this will not fail
     * and just return the item at 1 remaining durability.
     * @param base Item to modify damage of
     * @param operation Operation to apply, based on the current damage of the item
     * @param reslt Stores numerically the final damage the item has
     * @param logger Stores a string saying what went wrong, if something did.
     */
    @Nullable
    public static ItemStack MMOItemModifyDurability(@Nullable ItemStack base, @Nullable Player holder, @NotNull PlusMinusPercent operation, @Nullable RefSimulator<Double> reslt, @Nullable RefSimulator<String> logger) {
        return MMOItemModifyDurability(base, holder, operation, reslt, true, logger);
    }

    /**
     * Performs an operation on the durability of an item. If it succeeded on setting the durability, it will return the modified ItemStack.
     * <p>Will target MMOItems durability if applicable.</p> Does not bypass Unbreakable attribute.
     * <p></p>
     * Reasons it could fail: <p>
     * + The item does not exist </p>
     * + The item cannot be damaged (say, a stone block) <p>
     * <p><p>
     * If it would break by receiving such damage, but it is prevented from breaking, this will not fail
     * and just return the item at 1 remaining durability.
     * @param base Item to modify damage of
     * @param operation Operation to apply, based on the current damage of the item
     * @param holder Player under whose name to break the MMOItem (will display a message of breaking to them).
     * @param reslt Stores numerically the final damage the item has.
     * @param preventBreaking If the operation would break the item, it will set at 1 remaining.
     * @param logger Stores a string saying what went wrong, if something did.
     *
     * @return If it breaks without prevent breaking, the amount will be set to zero.
     */
    @Nullable
    public static ItemStack MMOItemModifyDurability(@Nullable ItemStack base, @Nullable Player holder, @NotNull PlusMinusPercent operation, @Nullable RefSimulator<Double> reslt, boolean preventBreaking, @Nullable RefSimulator<String> logger) {
        return MMOItemModifyDurability(base, holder, operation, reslt, preventBreaking, false, logger);
    }

    static Constructor durabilityItemNonAbstract;
    static Method durabilityItemValidity;
    static Boolean usingDurabilityItemAbstract = null;

    /**
     * Performs an operation on the durability of an item. If it succeeded on setting the durability, it will return the modified ItemStack.
     * <p>Will target MMOItems durability if applicable.</p> Does not bypass Unbreakable attribute.
     * <p></p>
     * Reasons it could fail: <p>
     * + The item does not exist </p>
     * + The item cannot be damaged (say, a stone block) <p>
     * <p><p>
     * If it would break by receiving such damage, but it is prevented from breaking, this will not fail
     * and just return the item at 1 remaining durability.
     * @param base Item to modify damage of
     * @param operation Operation to apply, based on the current damage of the item
     * @param holder Player under whose name to break the MMOItem (will display a message of breaking to them).
     * @param reslt Stores numerically the final damage the item has.
     * @param preventBreaking If the operation would break the item, it will set at 1 remaining.
     * @param useMaxDura If using max durability as source of operation instead of current dura
     * @param logger Stores a string saying what went wrong, if something did.
     *
     * @return If it breaks without prevent breaking, the amount will be set to zero.
     */
    @Nullable
    public static ItemStack MMOItemModifyDurability(@Nullable ItemStack base, @Nullable Player holder, @NotNull PlusMinusPercent operation, @Nullable RefSimulator<Double> reslt, boolean preventBreaking, boolean useMaxDura, @Nullable RefSimulator<String> logger) {
        String pname = "no";
        if (holder != null) { pname = holder.getName(); }
        //dur//OotilityCeption. Log("Dura As MMOItem: " + OotilityCeption.GetItemName(base) + "§7, holder " + pname + "§7, prevent break §b" + preventBreaking);

        // Gemstone Support is Enabled (Correct MMOItems version)
        //dur//OotilityCeption. Log("Gem Stuppot Found ");

        // I hope that bitch is not null to begin with
        if (base != null) {
            //dur//OotilityCeption. Log("Base Real ");

            // Result
            ItemStack result;
            DurabilityItem durItem;

            if (usingDurabilityItemAbstract == null) {
                try {
                    durabilityItemNonAbstract = DurabilityItem.class.getConstructor(Player.class, ItemStack.class);
                    durabilityItemValidity = DurabilityItem.class.getMethod("isValid");
                    usingDurabilityItemAbstract = false;
                } catch (NoSuchMethodException ignored ){
                    usingDurabilityItemAbstract = true;
                    durabilityItemNonAbstract = null;
                    durabilityItemValidity = null;
                }
            }

	            boolean isValid;
	            if (usingDurabilityItemAbstract) {
	                // DurabilityItem is abstract in 6.10.1+, use reflection
	                try {
	                    Constructor<?> ctor = DurabilityItem.class.getDeclaredConstructor(Player.class, NBTItem.class, EquipmentSlot.class);
	                    ctor.setAccessible(true);
	                    durItem = (DurabilityItem) ctor.newInstance(holder, base, EquipmentSlot.HAND);
	                    isValid = (durItem != null);
	                } catch (Exception e) {
	                    durItem = null;
	                    isValid = false;
	                }

            } else {
                try {
                    //DurabilityItem durItem = new DurabilityItem(holder, base);
                    durItem = (DurabilityItem) durabilityItemNonAbstract.newInstance(holder, base);
                    // isValid = durItem.isValid()
                    isValid = (boolean) durabilityItemValidity.invoke(durItem);

                } catch (IllegalAccessException|InstantiationException|InvocationTargetException ignored) {
                    if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback){  Gunging_Ootilities_Plugin.theOots.CPLog(OotilityCeption.LogFormat("MMOItems - Modify Durability", "§cCould not instantiate Durability Item class in§e GooPMMOitems.MMOItemModifyDurability(ItemStack, Player, PlusMinusPercent, RefSimulator, boolean, boolean, RefSimulator)")); }
                    OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Could not modify durability due to compatibility issues. ");
                    return null;
                }
            }

            // Attempt to get durability item
            if (isValid) {
                //dur//OotilityCeption. Log("§aUses MMOItems dura ");

                // Get Name
                String iName = OotilityCeption.GetItemName(base);

                // Durabilitied Item Meta
                ItemMeta dur;

                // Get Current amount
                int currentDura = durItem.getDurability();

                // Get max
                int maxDura = durItem.getMaxDurability();
                //dur//OotilityCeption. Log("§7Current Durability: §b" + currentDura + "/" + maxDura);

                // Simulate Vanilla
                int vanillaStyleDamage = maxDura - currentDura;
                double finalValue;

                /*
                 * Maybe using max durability as operation source.
                 *
                 * #1 Setting ~ Set the current durability to this number, no change.
                 *
                 * #2 Adding ~ Adds to the current durability
                 *
                 * #3 Percent ~ Sets current damage to this percent of the max damage
                 *
                 * #4 Plus Percent ~ Adds to the current damage this percent of the max damage
                 */
                //DUR//OotilityCeption.Log("§8DURA§b FV§7 ------ MMOITems ------ §f" + operation.toString());
                //DUR//OotilityCeption.Log("§8DURA§b FV§7 Current Damage:§e " + vanillaStyleDamage);
                if (useMaxDura) {

                    // Recreate plus minus percent
                    PlusMinusPercent toMax = operation.clone();

                    // Not relative, just get percent from the max durability
                    toMax.setRelative(false);
                    //DUR//OotilityCeption.Log("§8DURA§b FV§7 Applying Max Operation:§e " + toMax.toString() + "§7 to max durability§9 " + base.getType().getMaxDurability());

                    // Apply to that
                    double convertedMax = toMax.apply((double) base.getType().getMaxDurability());

                    //DUR//OotilityCeption.Log("§8DURA§b FV§7 Max Operation Result:§e " + convertedMax);

                    // Was it additive in the first place?
                    if (operation.getRelative()) {

                        // Add this percent of max durability to the current
                        finalValue = convertedMax + vanillaStyleDamage;
                        //DUR//OotilityCeption.Log("§8DURA§b FV§7 Adding result:§e " + finalValue);

                        // It was a set command
                    } else {

                        // The final value is this percent
                        finalValue = convertedMax;
                        //DUR//OotilityCeption.Log("§8DURA§b FV§7 Setting result:§e " + finalValue);
                    }

                } else {
                    //DUR//OotilityCeption.Log("§8DURA§b FV§7 Applying Direct Operation:§e " + operation.toString());

                    // Yeah
                    finalValue = operation.apply((double) vanillaStyleDamage);
                    //DUR//OotilityCeption.Log("§8DURA§b FV§7 Result:§e " + finalValue);
                }

                // Perform operation to current damage
                int finalDamage = (int) Math.round(finalValue);
                //dur//OotilityCeption. Log("§7As Damage: §b" + vanillaStyleDamage + " -> " + finalDamage);

                // Constrain I suppose, if its preventing from breaking or there is no holder :thinking:
                if (preventBreaking) { if (finalDamage >= (maxDura - 1)) { finalDamage = (maxDura - 1); } } else if (finalDamage > maxDura) { finalDamage = maxDura + 1; }
                if (finalDamage < 0) { finalDamage = 0; }
                //DUR//OotilityCeption.Log("§8DURA§b FV§7 True Result:§6 " + finalValue);

                // Get Actual Durability
                int finalDura = maxDura - finalDamage;

                // Get shift
                int shift = finalDura - currentDura;
                //dur//OotilityCeption. Log("§7Processed: §e" + finalDamage + " -> " + finalDura + "§7(§a" + shift + "§7) ");

                // Set result
                if (reslt != null) { reslt.setValue(finalDamage + 0.0D); }

                // Did it break? Must GooP do something (because MMOItems will generate an exception)?
                if (finalDura < 0 && holder == null) {
                    //dur//OotilityCeption. Log("§cBroke - No Playr ");

                    // Broke
                    OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Modified durability of §f" + iName + "§7, it broke though. ");

                    // Return as air
                    return OotilityCeption.asQuantity(base, 0);
                }

                // If positive
                if (shift > 0) {

                    // Add durability
                    dur = durItem.addDurability(shift).toItem().getItemMeta();

                    // If shift negative
                } else {

                    ItemStack unbroken = durItem.decreaseDurability(-shift).toItem();
                    if (unbroken == null) { finalDura = -1; }

                    // Substract durability
                    dur = (unbroken == null ? null : unbroken.getItemMeta());
                }

                // Did it break?
                if (finalDura < 0) {

                    // Can it break?
                    Boolean breakable = GetBooleanStatValue(base, Stat(GooPMMOItemsItemStats.WILL_BREAK),null, true);
                    if (OotilityCeption.If(breakable)) {

                        // Broke
                        OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Modified durability of §f" + iName + "§7, it broke though. ");

                        ItemStack broken = base.clone();
                        broken.setAmount(0);

                        // Return as air
                        return broken;
                    }
                }

                // Convert result
                result = new ItemStack(base);
                result.setItemMeta(dur);

                // Result
                OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Modified durability of §f" + iName + "§7. ");

                // If false, do the operation vanilla-wise alv
            } else {

                // Perform operation vanilla-wise
                result = OotilityCeption.SetDurabilityVanilla(base, operation, reslt, preventBreaking, useMaxDura, logger);
            }

            return result;

            // This man passing on a null item wth
        } else {

            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Cant repair of air! ");
            return null;
        }
    }

    /**
     * Will return the durability of thay item, if such a thing can have durability.
     * Gives you the vanilla durability if the custom MAX DURABILITY is not defined.
     * @return Null if such a thing doesnt make sense to have durability.
     */
    @Nullable
    public static Integer GetMaxDurabilityOf(@Nullable ItemStack base) {

        // I hope that bitch is not null to begin with
        if (base != null) {

            // Get stat ig
            Double fromStat = GetDoubleStatValue(base, Stat(GooPMMOItemsItemStats.MAX_DURABILITY));

            // Did it exist?
            if (fromStat != null) {

                // Round-yo
                return OotilityCeption.RoundToInt(fromStat);

            } else {

                // Vanilla it is
                return OotilityCeption.GetMaxDurabilityVanilla(base);
            }

            // This man passing on a null item wth
        } else {

            return null;
        }
    }

    public static ItemStack AddLoreLine(NBTItem base, String loreLine, Integer index) {

        // Test of existance of such type and IDs
        try {

            // Attempt to get MMOItem Instance
            MMOItem mmoitem = LiveFromNBT(base);
            if (mmoitem == null) { return null; }

            // Get Socket Data
            StringListData loreData = (StringListData) mmoitem.getData(GooPMMOItems.Stat(GooPMMOItemsItemStats.LORE));
            StringListData finalLore;

            // Is it the first or last
            boolean fORl = (index == null);
            if (index != null) { fORl = (index == 0);}
            ArrayList<String> iLore = new ArrayList<>();

            // Has the items any sockets already?
            if (loreData != null) {

                // Get original lore
                if (loreData.getList() != null) { iLore = new ArrayList<>(loreData.getList()); }

                //Choose True Index
                Integer tIndex = OotilityCeption.BakeIndex4Add(index, iLore.size());
                if (tIndex == null) {
                    // Index out of range
                    return null;
                }

                // Then just add the entry to the existing list
                iLore.add(tIndex, OotilityCeption.ParseColour(loreLine));

                // Create that data, and assign this list to it
                finalLore = new StringListData(iLore);

                // Remove momentaneously
                mmoitem.removeData(GooPMMOItems.Stat(GooPMMOItemsItemStats.LORE));

                // Item has no lore data at all, must create if  the index makes sense
            } else if (fORl) {

                // Make a new Array of Strings, containing that sole new lore line
                iLore.add(OotilityCeption.ParseColour(loreLine));

                // Create that data, and assign this list to it
                finalLore = new StringListData(iLore);

                // Lore data didnt exist and index did not mean 'line 0' so rip.
            } else {

                // Index Out of Range (No Lore)
                return null;
            }

            // Append it to the mmoitem
            mmoitem.setData(GooPMMOItems.Stat(GooPMMOItemsItemStats.LORE), finalLore);

            // Get Finished Product
            return mmoitem.newBuilder().build();

            // Seems that those Types/IDs did not match wth
        } catch (NullPointerException e) {

            // Hmph
            return null;
        }
    }
    public static ItemStack RemoveLoreLine(NBTItem base, Integer index) { return RemoveLoreLine(base, index, false); }
    public static ItemStack RemoveLoreLine(NBTItem base, Integer index, boolean removeAll) {

        // Test of existance of such type and IDs
        try {

            // Attempt to get MMOItem Instance
            MMOItem mmoitem = LiveFromNBT(base);
            if (mmoitem == null) { return null; }

            // Get Socket Data
            StringListData loreData = (StringListData) mmoitem.getData(GooPMMOItems.Stat(GooPMMOItemsItemStats.LORE));
            // Has the items any sockets already?
            if (loreData != null) {

                // Clear lore
                if (removeAll) {

                    // Replace old lore with a fresh array
                    mmoitem.setData(GooPMMOItems.Stat(GooPMMOItemsItemStats.LORE), new StringListData());

                    // Get Finished Product
                    return mmoitem.newBuilder().build();
                }

                // Get Lore Data
                ArrayList<String> iLore = new ArrayList<>(loreData.getList());

                // Make sure there is any relevant lore data
                if (!iLore.isEmpty()) {

                    //Choose True Index
                    Integer tIndex = OotilityCeption.BakeIndex4Remove(index, iLore.size());
                    if (tIndex == null) {
                        // Index out of Range
                        return null;
                    }

                    // Then just add the entry to the existing list
                    iLore.remove((int)tIndex);

                    // Remove old lore
                    mmoitem.removeData(GooPMMOItems.Stat(GooPMMOItemsItemStats.LORE));

                    // Was there any lore left?
                    if (!iLore.isEmpty()) {

                        // Create that data, and assign this list to it
                        StringListData finalLore = new StringListData(iLore);

                        // Append it to the mmoitem
                        mmoitem.setData(GooPMMOItems.Stat(GooPMMOItemsItemStats.LORE), finalLore);
                    }

                    // Item has no actual lore data
                } else {

                    // Thats a fail cuz cant remove what doesnt exist
                    return null;
                }

                // Item has no lore data at all lol
            } else {

                // Thats a fail cuz cant remove what doesnt exist
                return null;
            }

            // Get Finished Product
            return mmoitem.newBuilder().build();

            // Seems that those Types/IDs did not match wth
        } catch (NullPointerException e) {
            return null;
        }
    }
    public static ItemStack ModifyAttrib(NBTItem base, PlusMinusPercent operation, Attribute attrib, RefSimulator<Double> result) {

        // Test of existance of such type and IDs
        try {

            // Attempt to get MMOItem Instance
            MMOItem mmoitem = LiveFromNBT(base);
            if (mmoitem == null) { return null; }

            // Get Socket Data
            DoubleData mData;
            ItemStat stt;

            // Gather initial vanilla
            if (attrib.equals(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_MOVEMENT_SPEED))) {
                stt = GooPMMOItems.Stat(GooPMMOItemsItemStats.MOVEMENT_SPEED);

            } else if (attrib.equals(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_ATTACK_DAMAGE))) {
                stt = GooPMMOItems.Stat(GooPMMOItemsItemStats.ATTACK_DAMAGE);

            } else if (attrib.equals(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_MAX_HEALTH))) {
                stt = GooPMMOItems.Stat(GooPMMOItemsItemStats.MAX_HEALTH);

            } else if (attrib.equals(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_ARMOR))) {
                stt = Gunging_Ootilities_Plugin.useMMOLibDefenseConvert ? ItemStats.DEFENSE : ItemStats.ARMOR;

            } else if (attrib.equals(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_ATTACK_SPEED))) {
                stt = GooPMMOItems.Stat(GooPMMOItemsItemStats.ATTACK_SPEED);

            } else if (attrib.equals(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_KNOCKBACK_RESISTANCE))) {
                stt = GooPMMOItems.Stat(GooPMMOItemsItemStats.KNOCKBACK_RESISTANCE);

            } else if (attrib.equals(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_LUCK))) {
                stt = GooPMMOItems.LUCK;

            } else if (attrib.equals(GooP_MinecraftVersions.GetVersionAttribute(GooPVersionAttributes.GENERIC_ARMOR_TOUGHNESS))) {
                stt = GooPMMOItems.Stat(GooPMMOItemsItemStats.ARMOR_TOUGHNESS);

            } else {
                return null;
            }

            // Get value
            mData = (DoubleData) mmoitem.getData(stt);

            // Retrieve existing
            double vData;
            double cData;
            if (mData != null) {

                // Get Current Value
                cData = mData.getValue();

            } else {

                // Otherwise apply operation to zero
                cData = 0.0;
            }

            // Apply operation
            vData = operation.apply(cData);

            // Get difference
            vData -= cData;

            // Obtain difference
            mData = new DoubleData(vData);
            if (result != null) { result.setValue(vData + cData); }

            // Append
            mmoitem.mergeData(stt, mData, null);

            // Get Finished Product
            return mmoitem.newBuilder().build();

            // Seems that those Types/IDs did not match wth
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static ItemStack SetTier(@Nullable ItemStack base, @Nullable String newTier, @Nullable RefSimulator<String> tierNameRef, @Nullable RefSimulator<String> logger) {

        // I hope that bitch is not null to begin with
        if (!OotilityCeption.IsAirNullAllowed(base)) {

            // Result
            ItemStack result;

            // Convert if not a MMOItem
            if (!IsMMOItem(base)) { base = ConvertVanillaToMMOItem(base); }

            // Get Target NBT
            NBTItem tNBT = NBTItem.get(base);

            // Kount
            int kount = base.getAmount();

            // Get actual values
            ItemTier actualTier = MMOItems.plugin.getTiers().get(newTier);

            // Add lore heck
            boolean get = true;
            if (tierNameRef == null) { get = false; tierNameRef = new RefSimulator<>(""); }

            // Set the tier
            result = SetTier(tNBT, actualTier, tierNameRef, ((actualTier != null) || "none".equals(newTier)));

            // Log if Appropriate
            if (result != null) {

                if (get)  {
                    OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "The tier of "+ OotilityCeption.GetItemName(base) + "§7 was §b" + tierNameRef.getValue() + "§7. ");

                } else {
                    OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Modified tier of "+ OotilityCeption.GetItemName(base) + "§7 to §b" + tierNameRef.getValue() + "§7. ");
                }

            } else {

                OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Failed to modify tier, perhaps §3" + newTier + "§7 is not a loaded tier? ");
            }

            // Set
            if (!OotilityCeption.IsAirNullAllowed(result)) { result.setAmount(kount); }

            return result;

            // This man passing on a null item wth
        } else {

            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Cant edit tier of air! ");
            return null;
        }
    }

    @Nullable public static ItemStack AddAbility(@Nullable ItemStack base, @NotNull String ability, @NotNull String trigger, @NotNull ArrayList<CompactCodedValue> mods, @Nullable RefSimulator<String> logger) {
        if (!MMOItems.plugin.getSkills().hasSkill(ability)) {
            OotilityCeption.Log4Success(logger, !Gunging_Ootilities_Plugin.blockImportantErrorFeedback, "Ability §c" + ability + "§7 is not loaded. ");
            return null;}

        TriggerType tt = TriggerType.valueOf(trigger);
        if (tt == null) {
            OotilityCeption.Log4Success(logger, !Gunging_Ootilities_Plugin.blockImportantErrorFeedback, "Ability Trigger §c" + trigger + "§7 is not loaded. ");
            return null;
        }

        // I hope that it is not null to begin with
        if (!OotilityCeption.IsAirNullAllowed(base) && MMOItems.plugin.getSkills().hasSkill(ability)) {

            // Result
            ItemStack result;

            // Convert if not a MMOItem
            if (!IsMMOItem(base)) { base = ConvertVanillaToMMOItem(base); }

            // Get Target NBT
            NBTItem tNBT = NBTItem.get(base);

            // Kount
            int kount = base.getAmount();

            // Get actual values
            RegisteredSkill skill = MMOItems.plugin.getSkills().getSkill(ability);
            AbilityData ab = new AbilityData(skill, tt);
            for (CompactCodedValue ccv : mods) {
                Double val = Double.parseDouble(ccv.getValue());
                if (OotilityCeption.NumericTolerance(skill.getDefaultModifier(ccv.GetID()), val, 0.009)) { continue; }
                ab.setModifier(ccv.GetID(), val);
            }
            AbilityListData abs = new AbilityListData();
            abs.add(ab);

            // Attempt to get MMOItem Instance
            MMOItem mmoitem = LiveFromNBT(tNBT);
            if (mmoitem == null) { return null; }

            StatHistory hist = StatHistory.from(mmoitem, ItemStats.ABILITIES);
            hist.registerExternalData(abs);
            mmoitem.setData(ItemStats.ABILITIES, hist.recalculate(mmoitem.getUpgradeLevel()));

            // Get Finished Product
            result = mmoitem.newBuilder().build();
            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPSuccessFeedback, "Successfuly added ability §3" + ability+ "§7 to " + OotilityCeption.GetItemName(result) + "§7. ");
            return result;

        // This man passing on a null item wth
        } else {

            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Cant edit tier of air! ");
            return null;
        }
    }


    /**
     * Sets the Tier of target Item Stack. Will convert into MMOItem if vanilla, and tier is specified.
     * @param iSource Item Stack you want to know/modify the tier of.
     * @param newTier The new tier, or null if you just want to read the current.
     * @param finalTier Stores the name of the tier the item had at the end of this operation.
     * @return The tier name at the end of the operation, or null if it has no tier.
     */
    @Nullable public static ItemStack SetTier(@NotNull NBTItem iSource, @Nullable ItemTier newTier, @Nullable RefSimulator<String> finalTier, boolean setTier) {

        // Test of existance of such type and IDs
        try {

            // Attempt to get MMOItem Instance
            MMOItem mmoitem = LiveFromNBT(iSource);
            if (mmoitem == null) { return null; }

            // Get Socket Data
            StringData tData;

            // Apply if non-null
            if (setTier) {

                // Create tier yeah
                if (newTier != null) {

                    // Create
                    tData = new StringData(newTier.getId());

                    // Append
                    mmoitem.setData(ItemStats.TIER, tData);

                // Removing Tier
                } else {

                    // Remove
                    mmoitem.removeData(ItemStats.TIER);
                }
            }

            if (finalTier != null) {

                // Has data?
                if (mmoitem.hasData(ItemStats.TIER)) {

                    // TData is that, swell
                    tData = (StringData) mmoitem.getData(ItemStats.TIER);

                    // Store
                    finalTier.setValue(tData.toString());

                // No tier, that sets the return to 'none'
                } else { finalTier.setValue("none"); }
            }

            // Get Finished Product
            return mmoitem.newBuilder().build();

            // Seems that those Types/IDs did not match wth
        } catch (NullPointerException e) {
            return null;
        }
    }
    //endregion

    //region Identifying MMOItems
    @NotNull public static MMOItem getOrCreate(@NotNull Type type, @NotNull String id, @NotNull Material base) {
        id = id.toUpperCase().replace(" ", "_").replace("-", "_");

        // Find template
        MMOItemTemplate found = MMOItems.plugin.getTemplates().getTemplate(type, id);

        // None found? create
        if (found == null) {

            // Create section
            ConfigFile config = type.getConfigFile();
            config.getConfig().set(id + ".base.material", base.name());
            config.save();

            // Register edition?
            MMOItems.plugin.getTemplates().requestTemplateUpdate(type, id);

            // Should now exist
            found = MMOItems.plugin.getTemplates().getTemplate(type, id);
        }

        // Thats it apprently
        return found.newBuilder().build();
    }

    /**
     * @param items List of items which tags to compare
     *
     * @return A graphic list of the tags the items had
     */
    @NotNull public static HashMap<String, HashMap<String, Integer>> compareNBTTags(@NotNull ArrayList<ItemStack> items) {

        // Comparison map
        HashMap<String, HashMap<String, Integer>> tagValues = new HashMap<>();
        
        // For every item
        for (ItemStack item : items) {

            // Skip air ffs
            if (OotilityCeption.IsAirNullAllowed(item)) { continue; }

            // Make NBT Tag compound
            NBTItem nbt = NBTItem.get(item);

            // Include all tags
            for (String tag : nbt.getTags()) {

                // Find value of tag
                Object value = nbt.get(tag);
                if (value == null) { continue; }

                // Actual value
                String val = String.valueOf(value);

                // All right get current list
                HashMap<String, Integer> list = tagValues.get(tag);
                if (list == null) { list = new HashMap<>(); }

                // Does the list contain the value already?
                // Include as count 1
                // Increase count and put
                list.merge(val, 1, Integer::sum);

                // Yeah
                tagValues.put(tag, list);
            }
        }

        // That is the result found
        return tagValues;
    }

    public static boolean IsUnidentified(@Nullable ItemStack item) {

        // Not unident
        if (OotilityCeption.IsAirNullAllowed(item)) { return false; }

        // Well
        return IsUnidentified(NBTItem.get(item));
    }

    public static boolean IsUnidentified(@Nullable NBTItem item) {
        if (item == null) { return false; }

        // Check Tag
        return item.hasTag("MMOITEMS_UNIDENTIFIED_ITEM");
    }

    /**
     * Forces an update of MMOItems player equipment
     *
     * @param p Player reference
     */
    public static void UpdatePlayerEquipment(@Nullable OfflinePlayer p) {
        if (p == null) { return; }

        PlayerData.get(p.getUniqueId()).updateInventory();
    }
    /**
     * Forces an update of MMOItems player equipment.
     * Use this when the ID is not necessarily from a
     * player, otherwise, use {@link #UpdatePlayerEquipment(OfflinePlayer)}
     *
     * @param p UUID that could be a player
     *
     * @see {@link #UpdatePlayerEquipment(OfflinePlayer)}
     */
    public static void UpdatePlayerEquipment(@Nullable UUID p) {
        if (p == null) { return; }

        OfflinePlayer potentialPlayer = Bukkit.getOfflinePlayer(p);
        if (!potentialPlayer.hasPlayedBefore()) { return; }
        if (!potentialPlayer.isOnline()) { return; }

        // KK its a real player proceeed
        PlayerData.get(p).updateInventory();
    }

    /**
     * Checks if that thing is a MMOItem
     */
    @NotNull
    public static Boolean IsMMOItem(@Nullable ItemStack targetItem) {

        // If null then no
        if (targetItem == null) { return false; }

        // Gettat Target NBT
        NBTItem tNBT = NBTItem.get(targetItem);

        // Is it an mmoItem?
        return tNBT.hasType();
    }

    /**
     * Checks if that thing is a MMOItem that matches that type and that ID
     */
    @NotNull
    public static Boolean MMOItemMatch(ItemStack targetItem, String type, String idName) {

        // Gettat Target NBT
        NBTItem tNBT = NBTItem.get(targetItem);

        // Is it an mmoItem?
        if (tNBT.hasType()) {

            // Yea but is it the same type?
            if (type.equalsIgnoreCase(tNBT.getString("MMOITEMS_ITEM_TYPE"))) {

                // Vro are they named the same?
                return OotilityCeption.wildcardMatches(tNBT.getString("MMOITEMS_ITEM_ID"), idName.split("\\*"), true);
            }
        }

        return  false;

        //net.Indyuce.mmoitems.api.Type tMMoitem = tNBT.getType();
        //logReturn.add(ChatColor.YELLOW + "MMOItem Type: " + ChatColor.DARK_AQUA + tMMoitem.getId());
        // ItemStat BREAKDOWN
        // Stat Casual Name: ItemStat.getName();
        // MMI Browse Name: ItemStat.getId();
        // NBT Tag Name: ItemStat.getNBTPath();
        // YML-Name: ItemStat.getPath();
        // To get the value: NBTItem.getStat(ItemStat)

        //logReturn.add(ChatColor.GRAY + "§lStats:");
        //for (ItemStat statt : tMMoitem.getAvailableStats()) { logReturn.add("§e - §7Stat: §3" + statt.getId() + "§b " + tNBT.getStat(statt)); }

        //if (tMMoitem.getItemSet() != null) { logReturn.add(ChatColor.GRAY + "Item Set:" + ChatColor.DARK_AQUA + tMMoitem.getItemSet().getName()); }
        //if (tMMoitem.getParent() != null) { logReturn.add(ChatColor.YELLOW + "Parent Name:" + ChatColor.DARK_AQUA + tMMoitem.getParent().getName()); }

        //logReturn.add(ChatColor.YELLOW + "Equipment Type:" + ChatColor.DARK_AQUA + tMMoitem.getEquipmentType().toString());
        //logReturn.add(ChatColor.GRAY + "§lNBT Tags:");
        //for (String tagg : tNBT.getTags()) { logReturn.add("§e - §7" + tagg); }
        //String tagg = "";
        //tagg = "MMOITEMS_ITEM_ID"; logReturn.add("§e - §7" + tagg + " §b" + tNBT.getString(tagg));
        //tagg = "MMOITEMS_ITEM_TYPE"; logReturn.add("§e - §7" + tagg + " §b" + tNBT.getString(tagg));
    }

    static List<Type> lastTypes = new ArrayList<>();
    public static void GetMMOItem_Types() {
        // Clear da shit
        lastTypes.clear();

        // Get those types
        TypeManager types = MMOItems.plugin.getTypes();

        // Add them
        // To the Type Rememberance
        lastTypes.addAll(types.getAll());
    }
    public static ArrayList<String> GetTierNames() {

        // New ArrayList
        ArrayList<String> ret = new ArrayList<>();

        // Add for each tier
        for (ItemTier t : MMOItems.plugin.getTiers().getAll()) {

            // Add name
            ret.add(t.getId());
        }

        return ret;
    }
    public static ArrayList<String> GetMMOItem_TypeNames() {
        // Clear da shit
        lastTypes.clear();

        TypeManager types = MMOItems.plugin.getTypes();
        ArrayList<String> lTypes = new ArrayList<String>();

        // Add them
        for (Type t :  types.getAll()) {

            // To the Type Rememberance
            lastTypes.add(t);

            // To the returning string
            lTypes.add(t.getId());
        }

        return lTypes;
    }
    @NotNull public static List<String> GetMMOItem_IDNames(String ofType) {

        // GEt type
        Type type = MMOItems.plugin.getTypes().get(ofType);
        if (type == null) { return new ArrayList<>(); }

        // Alr
        return MMOItems.plugin.getTemplates().getTemplateNames(type);
    }
    static ArrayList<String> statNames = null;
    public static ArrayList<String> GetMMOItem_StatNames() {
        if (statNames != null) { return statNames; }

        // Get Ret
        ArrayList<String> ret = new ArrayList<>();

        // Git Stats
        for (ItemStat st : MMOItems.plugin.getStats().getAll()) {

            // Ad by nmae
            ret.add(st.getId());

            // Also advertise the NBT-style alias (eg MMOITEMS_SUCCESS_RATE) for tab completion
            if (!st.getId().startsWith("MMOITEMS_")) { ret.add("MMOITEMS_" + st.getId()); }
        }

        // Return
        statNames = ret;

        return statNames;
    }

    /**
     * Returns the MMOItem type of target NBT Item if it has type.
     */
    @Nullable
    public static Type GetMMOItemType(@Nullable NBTItem target) {

        // From string
        return GetMMOItemTypeFromString(GetMMOItemTypeRaw(target));
    }

    @Nullable
    public static Type GetParentmostMMOItemType(@Nullable ItemStack iSource) {

        if (!OotilityCeption.IsAirNullAllowed(iSource)) {

            if (!IsMMOItem(iSource)) { return null; }
            NBTItem iNBT = NBTItem.get(iSource);

            // As helm
            Type mthelm = GooPMMOItems.GetMMOItemType(iNBT);

            // If helm existed
            if (mthelm != null) {

                // Bringeth highest
                while (mthelm.isSubtype()) { mthelm = mthelm.getParent(); }

                // Return thay
                return mthelm;
            }
        }

        return null;
    }

    /**
     * Returns a STRING for MMOItems 6.5+, or a MMOItem Type for 6.4-
     */
    @Nullable
    public static String GetMMOItemTypeRaw(@Nullable NBTItem target) {

        // Get type
        return target.getType();
    }

    /**
     * Will get the MMOItem Type of target ItemStack
     * @param iSource ItemStack you dont even know if its a MMOItem
     * @param failureRet What to return if the ItemStack is NOT a MMOItem
     * @return Either the MMOItem Type (if MMOItem) or failureRet
     */
    @Nullable
    public static String GetMMOItemType(@Nullable ItemStack iSource, @Nullable String failureRet) {

        // If MMOItem
        if (IsMMOItem(iSource)) {

            // What type is it
            NBTItem itm = NBTItem.get(iSource);

            // Return thay
            return itm.getString("MMOITEMS_ITEM_TYPE");

            // If its not a MMOItem
        } else {

            // Clearly returns an invalid MMOItem Type
            return failureRet;
        }
    }

    /**
     * Will get the MMOItem Type of target ItemStack
     * @param iSource ItemStack you dont even know if its a MMOItem
     * @param failureRet What to return if the ItemStack is NOT a MMOItem
     * @return Either the MMOItem Type (if MMOItem) or failureRet
     */
    @Nullable
    @Contract("_, !null -> !null")
    public static String GetMMOItemID(@Nullable ItemStack iSource, @Nullable String failureRet) {

        // If MMOItem
        if (IsMMOItem(iSource)) {

            // What type is it
            NBTItem itm = NBTItem.get(iSource);

            // Return thay
            String ret = itm.getString("MMOITEMS_ITEM_ID");

            return ret == null ? failureRet : ret;

            // If its not a MMOItem
        } else {

            // Clearly returns an invalid MMOItem Type
            return failureRet;
        }
    }

    /**
     * Gets the MMOItem TYPE and MMOItem ID of an item simultaneously.
     * <p></p>
     * If it is not a MMOItem, the RefSimulators will be unchanged.
     * @param iSource Item that may be a MMOItem.
     * @param typeStorage RefSimulator to store TYPE
     * @param idStorage RefSimulator to store ID
     */
    public static void GetMMOItemInternals(@Nullable ItemStack iSource, @NotNull RefSimulator<String> typeStorage, @NotNull RefSimulator<String> idStorage) {

        // If MMOItem
        if (IsMMOItem(iSource)) {

            // What type is it
            NBTItem itm = NBTItem.get(iSource);

            // Return thay
            typeStorage.SetValue(itm.getString("MMOITEMS_ITEM_TYPE"));
            idStorage.SetValue(itm.getString("MMOITEMS_ITEM_ID"));
        }

        //CNV// else { OotilityCeption.Log("§8GOOPMI §cINTERNALS§7 Object " + OotilityCeption.GetItemName(iSource) + " §7is §cnot§7 a MMOItem"); }
    }

    /**
     * Checks if it is a valid MMOItems tier.
     */
    public static boolean TierExists(@Nullable String tier) { if (tier == null) { return false; } return MMOItems.plugin.getTiers().has(tier); }

    /**
     * Checks if it is a valid MMOItems Modifier.
     */
    @Nullable public static ItemStack ModifierOperation(@Nullable String rawModifier, @Nullable ItemStack mmo, @Nullable RefSimulator<String> logger) {
        return ModifierOperation(rawModifier, mmo, true, false, logger);
    }
    @Nullable public static ItemStack ModifierOperation(@Nullable String rawModifier, @Nullable ItemStack mmo, boolean useGlobl, boolean chances, @Nullable RefSimulator<String> logger) {
        if (usingModifierNodes == null) {
            OotilityCeption.Log4Success(logger, Gunging_Ootilities_Plugin.sendGooPFailFeedback, "Could not perform modifier operations due to compatibility issues. GooPMMOItems.ModifierOperation(String, ItemStack, boolean, boolean, RefSimulator)");
            return null; }

        if (usingModifierNodes) {
            return GooPMMOItemsModifierNodesOps.ModifierOperation(rawModifier, mmo, useGlobl, chances,logger);
        } else {
            return GooPMMOItemsTemplateModifierOps.ModifierOperation(rawModifier, mmo, useGlobl, chances,logger);
        }
    }

    public static ArrayList<String> getGlobalModifierNames() {
        if (usingModifierNodes == null) { return new ArrayList<>(); }

        if (usingModifierNodes) {
            return GooPMMOItemsModifierNodesOps.getGlobalModifierNames();
        } else {
            return GooPMMOItemsTemplateModifierOps.getGlobalModifierNames();
        }
    }

    public static Boolean usingModifierNodes = null;

    public static final String invalidTypeID = "no u";
    //endregion

    //region Item Stat Stuff

    /**
     * For the scope of today at 3:24am, this method only targets the
     * Original Data of Stat Histories. If I ever use it for anything
     * else than the MMOItems Converter (where this is not a problem),
     * I guess I will add support for EXSH / GEMS / MODS
     */
    @NotNull public static ItemStack internallyParsePlaceholdes(@NotNull ItemStack item, @NotNull Player player) {
        if (!IsMMOItem(item)) { return OotilityCeption.ParseLore(item, player); }

        LiveMMOItem mmo = new LiveMMOItem(NBTItem.get(item));
        StatHistory lore = StatHistory.from(mmo, ItemStats.LORE);

        // Parse lore
        ArrayList<String> parsedLore = new ArrayList<>();

        for (String str : ((StringListData) lore.getOriginalData()).getList()) {

            // Parse lore!
            parsedLore.add(OotilityCeption.ParseConsoleCommand(str, player, player, player.getLocation().getBlock(), player.getActiveItem()));
        }

        // Set new list
        lore.setOriginalData(new StringListData(parsedLore));
        mmo.setData(ItemStats.LORE, lore.recalculate(0));   // Not only is level 0 part of the scope... does lore even level up?

        StatHistory name = StatHistory.from(mmo, ItemStats.NAME);
        NameData nameData = ((NameData) name.getOriginalData());
        nameData.setString(OotilityCeption.ParseConsoleCommand(nameData.getMainName(), player, player, player.getLocation().getBlock(), player.getActiveItem()));

        mmo.setData(ItemStats.NAME, name.recalculate(mmo.getUpgradeLevel()));

        // Yeah
        return mmo.newBuilder().build();
    }

    /**
     * @param item Item to clear stat data from
     *
     * @return Removes enchants from stat data, then rebuilds I guess yeah.
     */
    @NotNull public static ItemStack removeBlacklistedEnchantments(@NotNull ItemStack item) {

        // Vanilla procedure
        if (!IsMMOItem(item)) { return ScoreboardLinks.removeBlacklistedEnchantmentsVanilla(item); }

        // Okay
        if (!hasBlacklistedEnchants(new VolatileMMOItem(NBTItem.get(item)))) { return null; }

        // Well time to get real
        int amount = item.getAmount();
        LiveMMOItem mmo = new LiveMMOItem(NBTItem.get(item));

        // Lets see if its worth making it a live MMOItem...
        StatHistory hist = StatHistory.from(mmo, ItemStats.ENCHANTS);

        for (StatData data : hist.getExternalData()) {
            if (!(data instanceof EnchantListData)) { continue; }

            int addedLvl = 0;
            for (Enchantment e : Gunging_Ootilities_Plugin.blacklistedEnchantments) {
                addedLvl += ((EnchantListData) data).getLevel(e);
                ((EnchantListData) data).addEnchant(e, 0); }

            if (Gunging_Ootilities_Plugin.replacementEnchantment != null && addedLvl != 0) {
                if (addedLvl > Gunging_Ootilities_Plugin.replacementEnchantment.getMaxLevel()) { addedLvl = Gunging_Ootilities_Plugin.replacementEnchantment.getMaxLevel(); }
                ((EnchantListData) data).addEnchant(Gunging_Ootilities_Plugin.replacementEnchantment, addedLvl); }

        }

        for (UUID mod : hist.getAllModifiers()) {
            StatData data = hist.getModifiersBonus(mod);
            if (!(data instanceof EnchantListData)) { continue; }

            int addedLvl = 0;
            for (Enchantment e : Gunging_Ootilities_Plugin.blacklistedEnchantments) {
                addedLvl += ((EnchantListData) data).getLevel(e);
                ((EnchantListData) data).addEnchant(e, 0); }

            if (Gunging_Ootilities_Plugin.replacementEnchantment != null && addedLvl != 0) {
                if (addedLvl > Gunging_Ootilities_Plugin.replacementEnchantment.getMaxLevel()) { addedLvl = Gunging_Ootilities_Plugin.replacementEnchantment.getMaxLevel(); }
                ((EnchantListData) data).addEnchant(Gunging_Ootilities_Plugin.replacementEnchantment, addedLvl); }
        }

        for (UUID gem : hist.getAllGemstones()) {
            StatData data = hist.getModifiersBonus(gem);
            if (!(data instanceof EnchantListData)) { continue; }

            int addedLvl = 0;
            for (Enchantment e : Gunging_Ootilities_Plugin.blacklistedEnchantments) {
                addedLvl += ((EnchantListData) data).getLevel(e);
                ((EnchantListData) data).addEnchant(e, 0); }

            if (Gunging_Ootilities_Plugin.replacementEnchantment != null && addedLvl != 0) {
                if (addedLvl > Gunging_Ootilities_Plugin.replacementEnchantment.getMaxLevel()) { addedLvl = Gunging_Ootilities_Plugin.replacementEnchantment.getMaxLevel(); }
                ((EnchantListData) data).addEnchant(Gunging_Ootilities_Plugin.replacementEnchantment, addedLvl); }
        }

        StatData data = hist.getOriginalData();
        if (data instanceof EnchantListData) {
            int addedLvl = 0;
            for (Enchantment e : Gunging_Ootilities_Plugin.blacklistedEnchantments) {
                addedLvl += ((EnchantListData) data).getLevel(e);
                ((EnchantListData) data).addEnchant(e, 0); }

            if (Gunging_Ootilities_Plugin.replacementEnchantment != null && addedLvl != 0) {
                if (addedLvl > Gunging_Ootilities_Plugin.replacementEnchantment.getMaxLevel()) { addedLvl = Gunging_Ootilities_Plugin.replacementEnchantment.getMaxLevel(); }
                ((EnchantListData) data).addEnchant(Gunging_Ootilities_Plugin.replacementEnchantment, addedLvl); }
        }

        // Recalculate
        mmo.setData(ItemStats.ENCHANTS, hist.recalculate(mmo.getUpgradeLevel()));

        // Yeah
        ItemStack ret = mmo.newBuilder().build();
        ret.setAmount(amount);
        return ret;
    }

    public static boolean hasBlacklistedEnchants(@NotNull  VolatileMMOItem mmo) {
        if (!mmo.hasData(ItemStats.ENCHANTS)) {
            //ENCH//OotilityCeption.Log("§8MMOITEMS§3 IE§7 Item has no enchantments");
            return false; }

        // Lets see if its worth making it a live MMOItem...
        StatHistory hist = StatHistory.from(mmo, ItemStats.ENCHANTS);

        for (StatData data : hist.getExternalData()) {
            if (!(data instanceof EnchantListData)) { continue; }

            for (Enchantment e : Gunging_Ootilities_Plugin.blacklistedEnchantments) {
                //ENCH//OotilityCeption.Log("§8MMOITEMS§3 IE§7 Checking§e EXSH§7 ~§a " + e.getName() + " " + ((EnchantListData) data).getLevel(e));
                if (((EnchantListData) data).getLevel(e) != 0) {
                    //ENCH//OotilityCeption.Log("§8MMOITEMS§3 IE§7 Item is§c illegal§7.");
                    return true; }
            }
        }

        for (UUID mod : hist.getAllModifiers()) {
            StatData data = hist.getModifiersBonus(mod);
            if (!(data instanceof EnchantListData)) { continue; }

            for (Enchantment e : Gunging_Ootilities_Plugin.blacklistedEnchantments) {
                //ENCH//OotilityCeption.Log("§8MMOITEMS§3 IE§7 Checking§a MOD§7 ~§a " + e.getName() + " " + ((EnchantListData) data).getLevel(e));
                if (((EnchantListData) data).getLevel(e) != 0) {
                    //ENCH//OotilityCeption.Log("§8MMOITEMS§3 IE§7 Item is§c illegal§7.");
                    return true; }
            }
        }

        for (UUID gem : hist.getAllGemstones()) {
            StatData data = hist.getModifiersBonus(gem);
            if (!(data instanceof EnchantListData)) { continue; }

            for (Enchantment e : Gunging_Ootilities_Plugin.blacklistedEnchantments) {
                //ENCH//OotilityCeption.Log("§8MMOITEMS§3 IE§7 Checking§c GEM§7 ~§a " + e.getName() + " " + ((EnchantListData) data).getLevel(e));
                if (((EnchantListData) data).getLevel(e) != 0) {
                    //ENCH//OotilityCeption.Log("§8MMOITEMS§3 IE§7 Item is§c illegal§7.");
                    return true; }
            }
        }

        StatData data = hist.getOriginalData();
        if (!(data instanceof EnchantListData)) {
            //ENCH//OotilityCeption.Log("§8MMOITEMS§3 IE§7 Item is§a clear§7.");
            return false; }

        for (Enchantment e : Gunging_Ootilities_Plugin.blacklistedEnchantments) {
            //ENCH//OotilityCeption.Log("§8MMOITEMS§3 IE§7 Checking§9 OG§7 ~§a " + e.getName() + " " + ((EnchantListData) data).getLevel(e));
            if (((EnchantListData) data).getLevel(e) != 0) {
                //ENCH//OotilityCeption.Log("§8MMOITEMS§3 IE§7 Item is§c illegal§7.");
                return true; } }

        //ENCH//OotilityCeption.Log("§8MMOITEMS§3 IE§7 Item is§a clear§7.");
        return false;
    }

    /**
     * Apparently, for MMOItems 6.3, instead of using <code>ItemStat.DURABILITY</code> you have to use <code>ItemStats.DURABILITY</code>.
     * <p>
     * Also, some item stats evolve so this will give you the item stat closest to what you mean if it is not loaded in that MI version.
     * @param statt Stat you want
     * @return The stat you want or the closest stat (if theres one that makes sense) if it doesnt exist in thay MMOItems version.
     */
    @Nullable
    public static ItemStat Stat(@Nullable String statt) {
        if (statt == null) { return null;}
        statt = statt.toUpperCase().replace(" ", "_").replace("-","_");

        // Some folk write stats like the NBT keys on the item itself: MMOITEMS_SUCCESS_RATE
        if (statt.startsWith("MMOITEMS_")) { statt = statt.substring("MMOITEMS_".length()); }

        // Does it make sense?
        try {

            // Try get
            GooPMMOItemsItemStats trueStat = GooPMMOItemsItemStats.valueOf(statt);

            // Return
            return Stat(trueStat);

        // Name fail
        } catch (IllegalArgumentException e) {

            // No lol
            return StatFromString(statt);
        }
    }

    @Nullable
    public static ItemStat StatFromString(@Nullable String statt) {
        if (statt == null) { return null; }

        // Ret
        ItemStat fo = idstringStats.get(statt);
        if (fo != null) { return fo; }

        // Try as goop misc
        if (statt.startsWith(miscPrefix)) {

            // Gotten
            ItemStat miscGotten = GetMiscStat(statt);

            // Ret
            if (miscGotten != null) {

                // Put
                idstringStats.put(statt, miscGotten);
                return miscGotten;
            }
        }

        // Get that stat that way
        ItemStat o = MMOItems.plugin.getStats().get(statt);

        // Append
        if (o != null) { idstringStats.put(statt, o); }

        // Return
        return o;
    }
    public static HashMap<String, ItemStat> idstringStats = new HashMap<>();

    /**
     * Apparently, for MMOItems 6.3, instead of using <code>ItemStat.DURABILITY</code> you have to use <code>ItemStats.DURABILITY</code>.
     * <p></p>
     * Also, some item stats evolve so this will give you the item stat closest to what you mean if it is not loaded in that MI version.
     * @param statt Stat you want
     * @return The stat you want or the closest stat (if theres one that makes sense) if it doesnt exist in thay MMOItems version.
     */
    @Nullable
    public static ItemStat Stat(@Nullable GooPMMOItemsItemStats statt) {
        if (statt == null) { return null;}

        // Return identified
        ItemStat o = identifiedStats.get(statt);
        if (o != null) { return o; }

        // Get directly as name
        ItemStat ret = MMOItems.plugin.getStats().get(statt.name());

        if (ret == null) {
            switch (statt) {
                case NBT_TAGS: return ItemStats.NBT_TAGS;
                case RESTORE: return ItemStats.RESTORE_HEALTH;

                default:
                    Gunging_Ootilities_Plugin.theOots.CPLog("No Update Stat for §e" + statt.name() + "§7 is defined! ");
                    break;
            }
        }

        // Return that
        o = ret;

        // Append
        if (o != null) { identifiedStats.put(statt, o); }

        // Return
        return o;
    }
    public static HashMap<GooPMMOItemsItemStats, ItemStat> identifiedStats = new HashMap<>();

    // Get Stats AHH
    @Nullable
    public static MMOItem MergeStatData(@NotNull NBTItem iSource, @NotNull ItemStat stat, @NotNull StatData statDaa) {

        // Test of existance of such type and IDs
        try {

            // Attempt to get MMOItem Instance
            MMOItem mmoitem = LiveFromNBT(iSource);

            //STAT//OotilityCeption. Log("    §8>§b£§8<§7 Got Live");

            // Its not socket gems is it?
            if ((GooPMMOItems.Stat(GooPMMOItemsItemStats.GEM_SOCKETS)).getName().equals(stat.getName())) {

                //STAT//OotilityCeption. Log("    §8>§b£§8<§7 As GemstoneData");

                // Must be string list or gemstone
                if (!(statDaa instanceof GemSocketsData) && !(statDaa instanceof StringListData)) {

                    // Get Array
                    ArrayList<String> slottz = ValueOfStringListData(statDaa);

                    //STAT//OotilityCeption. Log("    §8>§3£§8<§7 Got List Data");

                    // Get Socket Data
                    statDaa = new GemSocketsData(slottz);

                    //STAT//OotilityCeption. Log("    §8>§3£§8<§7 Converted to Gemstone Data");

                } else {

                    //STAT//OotilityCeption. Log("    §8>§c£§8<§7 Not a valid Stat Data");
                    return null;
                }
            }

            // just append
            mmoitem.mergeData(stat, statDaa, null);
            StatHistory.from(mmoitem, stat).consolidateEXSH();

            //STAT//OotilityCeption. Log("    §8>§b£§8<§7 Data was Set");

            // Return thay
            return mmoitem;

            // Seems that those Types/IDs did not match wth
        } catch (Exception e) {

            //STAT//OotilityCeption. Log("   §4***§c Exception - §n" + e.getMessage());
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets the stat data of an item, <b>make sure to have a good reason to set it instead of merge it.</b>
     *
     * @param iSource Target Item
     * @param stat Target Stat
     * @param statDaa Setting Data
     * @param asEXSHConsolidate You must have done something to the ORIGINAL and External SH data, setting this
     *                          to <code>true</code> will clear both of these and set the one you are providing
     *                          as the single EXSH data in this.
     *
     * @return The item with its stat recalculated
     */
    @Nullable
    public static MMOItem SetStatData(@NotNull NBTItem iSource, @NotNull ItemStat stat, @NotNull StatData statDaa, boolean asEXSHConsolidate) {

        // Test of existance of such type and IDs
        try {

            // Attempt to get MMOItem Instance
            MMOItem mmoitem = LiveFromNBT(iSource);

            // Does not participate in stat history
            if (!(statDaa instanceof Mergeable)) {

                // Set stat data
                mmoitem.setData(stat, statDaa);
                return mmoitem;
            }

            //STAT//OotilityCeption. Log("    §8>§b£§8<§7 Got Live");

            // Its not socket gems is it?
            if ((GooPMMOItems.Stat(GooPMMOItemsItemStats.GEM_SOCKETS)).getName().equals(stat.getName())) {

                //STAT//OotilityCeption. Log("    §8>§b£§8<§7 As GemstoneData");

                // Must be string list or gemstone
                if (!(statDaa instanceof GemSocketsData) && !(statDaa instanceof StringListData)) {

                    // Get Array
                    ArrayList<String> slottz = ValueOfStringListData(statDaa);

                    //STAT//OotilityCeption. Log("    §8>§3£§8<§7 Got List Data");

                    // Get Socket Data
                    statDaa = new GemSocketsData(slottz);

                    //STAT//OotilityCeption. Log("    §8>§3£§8<§7 Converted to Gemstone Data");

                } else {

                    //STAT//OotilityCeption. Log("    §8>§c£§8<§7 Not a valid Stat Data");
                    return null;
                }
            }

            // just append
            StatHistory hist = StatHistory.from(mmoitem, stat);


            //OotilityCeption.Log("§6<---- §eBefore Setting §6---->");
            //logSH(hist);

            if (asEXSHConsolidate) {

                // Clear original and set as external
                hist.setOriginalData(hist.getItemStat().getClearStatData());
                hist.getExternalData().clear();
                hist.registerExternalData(statDaa);

            } else {

                // Set as original
                hist.setOriginalData(statDaa);
            }
            mmoitem.setData(stat, hist.recalculate(mmoitem.getUpgradeLevel()));

            //STAT//OotilityCeption. Log("    §8>§b£§8<§7 Data was Set");

            // Return thay
            return mmoitem;

            // Seems that those Types/IDs did not match wth
        } catch (Exception e) {

            //STAT//OotilityCeption. Log("   §4***§c Exception - §n" + e.getMessage());
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * Will return null if it fails for any reason.
     */
    @Nullable
    public static ItemStack SetDoubleStatData(@Nullable ItemStack itm, @Nullable String stat, double value) {
        if (stat == null) { return null; }
        return SetDoubleStatData(itm, Stat(stat), value);
    }
    @Nullable
    public static ItemStack SetDoubleStatData(@Nullable ItemStack itm, @Nullable ItemStat stat, double value) {
        if (itm == null) { return null; }
        if (!IsMMOItem(itm)) { return null; }
        if (stat == null) { return null; }
        if (!(stat instanceof DoubleStat)) { return null; }

        // Create Stat Data
        DoubleData dData = new DoubleData(value);
        NBTItem nbt = NBTItem.get(itm);

        // Put
        MMOItem result = MergeStatData(nbt, stat, dData);

        // Return
        if (result == null) { return null; }

        // Build
        return result.newBuilder().build();
    }

    /**
     * Will return null if it fails for any reason.
     */
    @Nullable
    public static ItemStack SetStringStatData(@Nullable ItemStack itm, @Nullable String stat, String value) {
        if (stat == null) { return null; }
        //STAT//OotilityCeption. Log("   §6*§7 Nonull Stat ");
        return SetStringStatData(itm, Stat(stat), value);
    }
    @Nullable
    public static ItemStack SetStringStatData(@Nullable ItemStack itm, @Nullable ItemStat stat, @Nullable String value) {
        if (itm == null) { return null; }
        if (!IsMMOItem(itm)) { return null; }
        if (stat == null) { return null; }
        //STAT//OotilityCeption. Log("   §6*§7 Nonull Qualificatons ");


        // Create Stat Data
        StatData sData;
        NBTItem nbt = NBTItem.get(itm);

        // String list or String?
        if (stat instanceof StringStat) {
            //STAT//OotilityCeption. Log("   §6*§7 As §eString Stat ");

            // Get as SData
            sData = new StringData(value);

        // Perform List Operations
        } else {
            //STAT//OotilityCeption. Log("   §6*§7 As §eString List Stat ");

            // Cant be null
            if (value == null) {
                //STAT//OotilityCeption. Log("   §6*§c No Value ");
                return null;
            }

            // Check as list
            if (!(stat instanceof StringListStat) && !(stat.equals(Stat(GooPMMOItemsItemStats.GEM_SOCKETS)))) {
                //STAT//OotilityCeption. Log("   §6*§c Not correct stat ");
                return null;
            }

            // Get already existing list
            ArrayList<String> old = GetStringListStatValue(nbt, stat);
            if (old == null) {
                //STAT//OotilityCeption. Log("   §6*§7 No Old Value found, §eCreated ");
                old = new ArrayList<>();
            }

            // Does it begin with a -
            if (value.startsWith("-")) {
                //STAT//OotilityCeption. Log("   §6*§7 Removant ");

                // Cook
                String v = value.substring(1);

                // Is it contained?
                if (old.contains(v)) {
                    //STAT//OotilityCeption. Log("   §6*§a Removed Old ");

                    // Remove
                    old.remove(v);

                } else {
                    //STAT//OotilityCeption. Log("   §6*§e Missing Faulure ");

                    // Nope, fail
                    return null;
                }

            } else {
                //STAT//OotilityCeption. Log("   §6*§a Added ");

                // Append
                old.add(value);
            }

            // Set Data
            sData = new StringListData(old);

        }

        // Put
        MMOItem result = SetStatData(nbt, stat, sData, false);

        // Return
        if (result == null) {
            //STAT//OotilityCeption. Log("   §6*§c Fatal Result Error ");
            return null;
        }

        // Build
        return result.newBuilder().build();
    }
    /**
     * Will return null if it fails for any reason.
     */
    @Nullable
    public static ItemStack SetBooleanStatData(@Nullable ItemStack itm, @Nullable String stat, Boolean value) {
        if (stat == null) { return null; }
        //STAT//OotilityCeption. Log("   §6*§7 Nonull Stat ");
        return SetBooleanStatData(itm, Stat(stat), value);
    }
    @Nullable
    public static ItemStack SetBooleanStatData(@Nullable ItemStack itm, @Nullable ItemStat stat, @Nullable Boolean value) {
        if (itm == null) { return null; }
        if (!IsMMOItem(itm)) { return null; }
        if (stat == null) { return null; }
        if (value == null) { return null; }
        if (!(stat instanceof BooleanStat)) { return null; }
        //STAT//OotilityCeption. Log("   §6*§7 Nonull Qualificatons ");


        // Create Stat Data
        NBTItem nbt = NBTItem.get(itm);
        BooleanData sData = new BooleanData(value);

        // Put
        MMOItem result = SetStatData(nbt, stat, sData, false);

        // Return
        if (result == null) {
            //STAT//OotilityCeption. Log("   §6*§c Fatal Result Error ");
            return null;
        }

        // Build
        return result.newBuilder().build();
    }

    @NotNull
    public static ItemStack Build(@NotNull NBTItem itm) {
        //STAT//OotilityCeption. Log("   §a*§7 Building ");

        // Build
        return (LiveFromNBT(itm)).newBuilder().build();
    }

    /**
     * @return The abilities registered onto MMOItems
     */
    @NotNull public static ArrayList<String> getRegisteredSkillNames() {
        ArrayList<String> ret = new ArrayList<>();
        for (RegisteredSkill skill : MMOItems.plugin.getSkills().getAll()) { ret.add(skill.getHandler().getId()); }
        return ret; }

    /**
     * @return The trigger types registered onto MythicLib
     */
    @NotNull public static ArrayList<String> getTriggerNames() {
        ArrayList<String> ret = new ArrayList<>();
        for (TriggerType skill : TriggerType.values()) { ret.add(skill.toString()); }
        return ret; }

    /**
     * @return The modifiers of this ability if it exists.
     */
    @NotNull public static ArrayList<String> getRegisteredSkillModifiers(@Nullable String ability, boolean withDefault) {
        String ab = ability.toUpperCase().replace("-", "_").replace(" ", "_").replaceAll("[^A-Z0-9_]", "");
        if (MMOItems.plugin.getSkills().hasSkill(ab)) {
            RegisteredSkill sk = MMOItems.plugin.getSkills().getSkill(ab);
            if (withDefault) {
                ArrayList<String> ret = new ArrayList<>();
                for (String p : sk.getHandler().getParameters()) {
                    ret.add(p + "=" + sk.getDefaultModifier(p));}
                return ret;
            } else {return new ArrayList<>(sk.getHandler().getParameters());} }
        return new ArrayList<>(); }
    //endregion

    public static void onCommand_GooPMMOItems(@NotNull CommandSender sender, Command command, @NotNull String label, @NotNull String[] args, @Nullable Location senderLocation, boolean chained, @Nullable SuccessibleChain commandChain, @NotNull RefSimulator<List<String>> logReturnUrn, @Nullable String failMessage) {
        // Has permission?
        boolean permission = true;

        // What will be said to the caster (caster = sender of command)
        List<String> logReturn = new ArrayList<>();

        // Check 5 Permission
        if (sender instanceof Player) {
            // Solid check for permission
            permission = sender.hasPermission("gunging_ootilities_plugin.mmoitems");
        }

        // Got permission?
        if (permission) {
            if (args.length >= 2) {

                // Failure
                boolean failure = false;

                //region Command targets
                ArrayList<Player> targets = null;
                Entity asDroppedItem = null;
                String subsonic = args[1].toLowerCase();
                int playerIndex = 2;
                if (subsonic.equals("addgemslot") ||
                    subsonic.equals("stat") ||
                    subsonic.equals("countgems") ||
                    subsonic.equals("gemstoneextract") ||
                    subsonic.equals("gemstoneswap")) { playerIndex++; }
                if (args.length > playerIndex) {
                    targets = OotilityCeption.GetPlayers(senderLocation, args[playerIndex], null);
                    asDroppedItem = OotilityCeption.getEntityByUniqueId(args[playerIndex]); }
                if (!(asDroppedItem instanceof Item)) { asDroppedItem = null; }
                //endregion

                // Amount of successes
                RefSimulator<String> refAddition = new RefSimulator<>(null);

                // Help Parameters
                int argsMinLength, argsMaxLength;
                String subcommand, subcategory, usage;

                if (OotilityCeption.hasPermission(sender, "mmoitems", subsonic)) {

                    switch (subsonic) {
                        //region Equipment Update
                        case "equipmentupdate":
                            //   0      1           2            3      args.Length
                            // /goop mmoitems equipmentupdate <player>
                            //   -      0           1            2      args[n]
                            
                            argsMinLength = 3;
                            argsMaxLength = 3;
                            usage = "/goop mmoitems equipmentupdate <player>";
                            subcommand = "Equipment Update";
                            subcategory = "MMOItems -Equipment Update";

                            // Help form?
                            if (args.length == 2)  {

                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Forces MMOItems to recalculate player's stats.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<player> §7Player to update equipment from.");

                            // Correct number of args?
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {

                                // Does the player exist?
                                if (targets.isEmpty()) {

                                    // Failure
                                    failure = true;

                                    // Notify the error
                                    if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Target must be an online player!"));
                                }

                                if (!failure) {

                                    // Through all of them
                                    for (Player target : targets) {

                                        // Open it for them
                                        UpdatePlayerEquipment(target);

                                        // Log Success
                                        if (Gunging_Ootilities_Plugin.sendGooPSuccessFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Forced §3" + target.getName() + "§7 to reload their equipped items. "));

                                        // Run Chain
                                        commandChain.chain(chained, target, sender);
                                    }
                                }

                                // Incorrect number of args
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Notify Error
                                if (args.length >= argsMinLength) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§e many§7 args). For info: §e/goop mmoitems " + subsonic));

                                } else {

                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§6 few§7 args). For info: §e/goop mmoitems " + subsonic));
                                }

                                // Notify Usage
                                logReturn.add("§3Usage: §e" + usage);
                            }

                            break;
                        //endregion
                        //region addGemSlot
                        case "addgemslot":

                            //   0      1        2          3       4       5     args.Length
                            // /goop mmoitems addGemSlot <color> <player> <slot>
                            //   -      0        1          2       3       4     args[n]
                            argsMinLength = 5;
                            argsMaxLength = 5;
                            usage = "/goop mmoitems addGemSlot <color> <player> <slot>";
                            subcommand = "Add Gem Slot";
                            subcategory = "MMOItems - Add Gem Slot";

                            // Help form?
                            if (args.length == 2)  {

                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Adds gem slots to items.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<color> §7Color of the gem slot to add.");
                                logReturn.add("§3 - §e<player> §7Player who has the item.");
                                logReturn.add("§3 - §e<slot> §7Slot of the target item.");

                            // Correct number of args?
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {

                                // Does the player exist?
                                if (targets.isEmpty() && asDroppedItem == null) {

                                    // Failure
                                    failure = true;

                                    // Notify the error
                                    if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Target must be an online player!"));
                                }

                                if (!failure) {

                                    /*
                                     * Final-style copy over
                                     */
                                    String colour = args[2].replace("_", " ");

                                    // Preparation of Methods
                                    TargetedItems executor = new TargetedItems(false, true,
                                            chained, commandChain, sender, failMessage,

                                            // What method to use to process the item
                                            iSource -> GooPMMOItems.MMOItemAddGemSlot(iSource.getValidOriginal(), colour, iSource.getLogAddition()),

                                            // When will it succeed
                                            iSource -> (iSource.getResult() != null && iSource.getValidOriginal().getType() != Material.STRUCTURE_VOID),

                                            // No scoreboards are involved
                                            null);

                                    // Register the ItemStacks
                                    if (asDroppedItem != null) { executor.registerDroppedItem((Item) asDroppedItem); }
                                    executor.registerPlayers(targets, args[4], executor.getIncludedStrBuilder());

                                    // Process the stuff
                                    executor.process();

                                    // Was there any log messages output?
                                    if (!executor.getIncludedStrBuilder().isEmpty()) { logReturn.add(OotilityCeption.LogFormat(subcategory, executor.getIncludedStrBuilder().toString())); }
                                }

                            // Incorrect number of args
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Notify Error
                                if (args.length >= argsMinLength) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§e many§7 args). For info: §e/goop mmoitems " + subsonic));

                                } else {

                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§6 few§7 args). For info: §e/goop mmoitems " + subsonic));
                                }

                                // Notify Usage
                                logReturn.add("§3Usage: §e" + usage);
                            }

                            break;
                        //endregion
                        //region Count Gems
                        case "countgems":
                            //   0      1        2            3           4       5     [6]     6  [7]      args.Length
                            // /goop mmoitems countGems <includeSlots> <player> <slot> [qnr] [scoreboard]
                            //   -      0        1            2           3       4     [5]     5  [6]      args[n]
                            argsMinLength = 6;
                            argsMaxLength = 7;
                            usage = "/goop mmoitems countGems <includeEmpty> <player> <slot> [range] [scoreboard]";
                            subcommand = "Count Gem Slots";
                            subcategory = "MMOItems - Count Gem Slots";

                            // Help form?
                            if (args.length == 2)  {

                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Counts gemstones in items.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<includeEmpty> §7If empty slots should also count.");
                                logReturn.add("§3 - §e<player> §7Player who has the item.");
                                logReturn.add("§3 - §e<slot> §7Slot of the target item.");
                                logReturn.add("§3 - §e[range] §7Count that succeeds this command.");
                                logReturn.add("§3 - §e[scoreboard] §7To store the result of the count.");
                                logReturn.add("§8You must specify either a range or a scoreboard (or both).");

                            // Correct number of args?
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {

                                // Does the player exist?
                                if (targets.isEmpty() && asDroppedItem == null) {

                                    // Failure
                                    failure = true;

                                    // Notify the error
                                    if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Target must be an online player!"));
                                }

                                // Boolean parses?
                                Boolean includeEmpty = null;
                                if (!OotilityCeption.BoolTryParse(args[2])) {

                                    // Fail
                                    failure = true;

                                    if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Expected §atrue§7 or §cfalse§7 instead of '§3" + args[2] + "§7'"));

                                } else {
                                    includeEmpty = OotilityCeption.BoolParse(args[2]);
                                }

                                QuickNumberRange qnr = QuickNumberRange.FromString(args[5]);
                                String objectiveName = null; @Nullable Objective targetObjective = null;
                                if (qnr == null) { objectiveName = args[5]; } else if (args.length > 6) { objectiveName = args[6]; }
                                if (objectiveName != null) {

                                    // Some scoreboards to test
                                    ScoreboardManager manager = Bukkit.getScoreboardManager();
                                    Scoreboard targetScoreboard = manager.getMainScoreboard();
                                    targetObjective = targetScoreboard.getObjective(objectiveName);


                                    // Does the scoreboard objective exist?
                                    if (targetObjective == null) {

                                        // Failure
                                        failure = true;

                                        // Mention
                                        if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Scoreboard Objective '§3" + objectiveName + "§7' does not exist."));
                                    }
                                }

                                if (qnr == null && args.length > 6) {

                                    // Failure
                                    failure = true;

                                    // Mention
                                    if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Expected vanilla range format instead of '§3" + args[5] + "§7'"));
                                }

                                if (!failure) {

                                    // Final initial
                                    final Boolean finalIncludeEmpty = includeEmpty;
                                    @Nullable final Objective finalTargetObjective = targetObjective;

                                    // Preparation of Methods
                                    TargetedItems executor = new TargetedItems(false, false,
                                            chained, commandChain, sender, failMessage,

                                            // What method to use to process the item
                                            iSource -> GooPMMOItems.MMOItemCountGems(iSource.getValidOriginal(), finalIncludeEmpty, iSource.getRef_int_a(), iSource.getLogAddition()),

                                            // When will it succeed
                                            iSource -> {
                                                boolean iSuccess = true;

                                                if (qnr != null) {

                                                    // Does it meet?
                                                    iSuccess = qnr.InRange(iSource.getRef_int_a().getValue());
                                                    if (iSuccess) {

                                                        // Notify
                                                        if (Gunging_Ootilities_Plugin.sendGooPSuccessFeedback) {
                                                            iSource.addToLogAddition("Counted gems of " + OotilityCeption.GetItemName(iSource.getOriginal()) + "§7,§b " + iSource.getRef_int_a() + "§7 §awas§7 in the specified range§e " + qnr.qrToString() + "§7. ");
                                                        }

                                                    } else {

                                                        // Notify
                                                        if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) {
                                                            iSource.addToLogAddition("Counted gems of " + OotilityCeption.GetItemName(iSource.getOriginal()) + "§7,§b " + iSource.getRef_int_a() + "§7 was §cnot§7 in the specified range§e " + qnr.qrToString() + "§7. ");
                                                        }
                                                    }
                                                }

                                                return iSuccess;
                                            },

                                            // Store scores
                                            (iSource, sInfo) -> {

                                                // Increase the output score of this player by this cont.
                                                if (finalTargetObjective != null) {

                                                    // Initialize as zero
                                                    sInfo.setInitZero(finalTargetObjective);

                                                    // Add relevant values
                                                    sInfo.addScoreboardOpp(finalTargetObjective, iSource.getRef_int_a().getValue(), true, false);
                                                }

                                                // Succesible Value
                                                sInfo.setValueOfSuccess(iSource.getRef_int_a().getValue());
                                            }
                                    );

                                    // Register the ItemStacks
                                    if (asDroppedItem != null) { executor.registerDroppedItem((Item) asDroppedItem); }
                                    executor.registerPlayers(targets, args[4], executor.getIncludedStrBuilder());

                                    // Process the stuff
                                    executor.process();

                                    // Was there any log messages output?
                                    if (!executor.getIncludedStrBuilder().isEmpty()) { logReturn.add(OotilityCeption.LogFormat(subcategory, executor.getIncludedStrBuilder().toString())); }

                                }

                            // Incorrect number of args
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Notify Error
                                if (args.length >= argsMinLength) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§e many§7 args). For info: §e/goop mmoitems " + subsonic));

                                } else {

                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§6 few§7 args). For info: §e/goop mmoitems " + subsonic));
                                }

                                // Notify Usage
                                logReturn.add("§3Usage: §e" + usage);
                            }

                            break;
                        //endregion
                        //region Upgrade
                        case "upgrade":
                            //   0       1     2         3       4          5             6           7              8            args.Length
                            // /goop mmoitems upgrade <player> <slot> [±]<levels>[%] [break max] [objective] [±][score][%]
                            //   -       0     1         2       3          4             5           6              7            args[n]
                            argsMinLength = 5;
                            argsMaxLength = 8;
                            usage = "/goop mmoitems upgrade <player> <slot> [±]<levels>[%] [break max] [objective] [±][score][%]";
                            subcommand = "Upgrade";
                            subcategory = "MMOItems - Upgrade";

                            // Help form?
                            if (args.length == 2)  {

                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Upgrades items.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<player> §7Player who has the item.");
                                logReturn.add("§3 - §e<slot> §7Slot of the target item.");
                                logReturn.add("§3 - §e[±]<levels>[%] §7Operation on the upgrade level.");
                                logReturn.add("§3      * §bread§7 Keyword to only read the level.");
                                logReturn.add("§3 - §e[break max] §7Can this command upgrade beyond limit?");
                                logReturn.add("§3      * §7There is no limit to downgrading (negative levels).");
                                logReturn.add("§3 - §e[objective] §7Scoreboard to output the result.");
                                logReturn.add("§3 - §e[scoreboard] §7The result that will be written onto the score.");
                                logReturn.add("§3      * §blevel§7 keyword to set the score to the result level.");

                                // Correct number of args?
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {

                                // Does the player exist?
                                if (targets.isEmpty() && asDroppedItem == null) {
                                    // Failure
                                    failure = true;

                                    // Notify the error
                                    if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Target must be an online player!"));
                                }

                                PlusMinusPercent pmpLevel = PlusMinusPercent.GetPMP(args[4], refAddition);
                                if (pmpLevel == null) {

                                    // Fail if not read keyword
                                    if (!"read".equalsIgnoreCase(args[4])) {

                                        // Failure
                                        failure = true;

                                        // Mention it
                                        if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Expected a number or operation for level (like §b1§7, §b5,§7 §bn2§7, or §b+2§7) instead of §e" + args[4]));

                                    } else { pmpLevel = new PlusMinusPercent(0D, true, false); }
                                }

                                boolean breakLimit = false;
                                if (args.length >= 6) {

                                    if (OotilityCeption.BoolTryParse(args[5])) {

                                        breakLimit = Boolean.parseBoolean(args[5]);

                                    } else {
                                        // Failure
                                        failure = true;

                                        if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Expected §btrue§7 or §bfalse§7 instead of §e" + args[5]));
                                    }
                                }

                                Objective targetObjective = null;
                                PlusMinusPercent score = null;
                                if (args.length >= 7) {
                                    ScoreboardManager manager = Bukkit.getScoreboardManager();
                                    Scoreboard targetScoreboard = manager.getMainScoreboard();
                                    targetObjective = targetScoreboard.getObjective(args[6]);

                                    if (targetObjective == null) {
                                        // Failure
                                        failure = true;

                                        if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Scoreboard objective §3" + args[6] + "§7 does not exist."));
                                    }

                                    if (args.length >= 8) {
                                        score = PlusMinusPercent.GetPMP(args[7], refAddition);

                                        if (score == null && !(args[7].equalsIgnoreCase("level"))) {
                                            // Failure
                                            failure = true;

                                            // Mention it
                                            if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "The final score '§3" + args[7] + "§7' should be an integer number (maybe with an operation)."));
                                        }
                                    }
                                }

                                if (!failure) {

                                    // Copy of finals
                                    final boolean finalBreakLimit = breakLimit;
                                    final Objective finalTargetObjective = targetObjective;
                                    final PlusMinusPercent finalScore = score;
                                    final PlusMinusPercent pmpReal = pmpLevel;

                                    // Preparation of Methods
                                    TargetedItems executor = new TargetedItems(false, !pmpLevel.isNeutral(),
                                            chained, commandChain, sender, failMessage,

                                            // What method to use to process the item
                                            iSource -> GooPMMOItems.UpgradeMMOItem(iSource.getValidOriginal(), pmpReal, finalBreakLimit, iSource.getRef_int_a(), iSource.getLogAddition()),

                                            // When will it succeed
                                            iSource -> iSource.getResult() != null,

                                            // Store scores
                                            (iSource, sInfo) -> {

                                                // If the scoreboard is enabled
                                                if (finalTargetObjective != null) {

                                                    // Score override
                                                    if (finalScore != null) { sInfo.setFinalScoreboardOpp(finalTargetObjective, finalScore);}
                                                    else { sInfo.setInitZero(finalTargetObjective); }

                                                    // Addition of Upgrade
                                                    sInfo.addScoreboardOpp(finalTargetObjective, iSource.getRef_int_a().getValue(), true, false);
                                                }

                                                // Succesible Value
                                                sInfo.setValueOfSuccess(iSource.getRef_int_a().getValue());
                                            }
                                        );

                                    // Register the ItemStacks
                                    if (asDroppedItem != null) { executor.registerDroppedItem((Item) asDroppedItem); }
                                    executor.registerPlayers(targets, args[3], executor.getIncludedStrBuilder());

                                    // Process the stuff
                                    executor.process();

                                    // Was there any log messages output?
                                    if (!executor.getIncludedStrBuilder().isEmpty()) { logReturn.add(OotilityCeption.LogFormat(subcategory, executor.getIncludedStrBuilder().toString())); }

                                }

                            // Incorrect number of args
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Notify Error
                                if (args.length >= argsMinLength) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§e many§7 args). For info: §e/goop mmoitems " + subsonic));

                                } else {

                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§6 few§7 args). For info: §e/goop mmoitems " + subsonic));
                                }

                                // Notify Usage
                                logReturn.add("§3Usage: §e" + usage);
                            }

                            break;
                        //endregion
                        //region Identify
                        case "identify":
                            //   0       1       2         3       4     args.Length
                            // /goop mmoitems identify <player> <slot>
                            //   -       0       1         2       3     args[n]
                            argsMinLength = 4;
                            argsMaxLength = 4;
                            usage = "/goop mmoitems identify <player> <slot>";
                            subcommand = "Identify";
                            subcategory = "MMOItems - Identify";

                            // Help form?
                            if (args.length == 2)  {

                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Identifies all items in the slot.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<player> §7Player who has the item.");
                                logReturn.add("§3 - §e<slot> §7Slot of the target item.");

                                // Correct number of args?
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {

                                // Does the player exist?
                                if (targets.isEmpty() && asDroppedItem == null) {
                                    // Failure
                                    failure = true;

                                    // Notify the error
                                    if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Target must be an online player!"));
                                }

                                if (!failure) {

                                    // Preparation of Methods
                                    TargetedItems executor = new TargetedItems(false, true,
                                            chained, commandChain, sender, failMessage,

                                            // What method to use to process the item
                                            iSource -> GooPMMOItems.IdentifyMMOItem(iSource.getValidOriginal(), iSource.getLogAddition()),

                                            // When will it succeed
                                            iSource -> iSource.getResult() != null,

                                            // Store scores
                                            null
                                    );

                                    // Register the ItemStacks
                                    if (asDroppedItem != null) { executor.registerDroppedItem((Item) asDroppedItem); }
                                    executor.registerPlayers(targets, args[3], executor.getIncludedStrBuilder());

                                    // Process the stuff
                                    executor.process();

                                    // Was there any log messages output?
                                    if (!executor.getIncludedStrBuilder().isEmpty()) { logReturn.add(OotilityCeption.LogFormat(subcategory, executor.getIncludedStrBuilder().toString())); }

                                }

                                // Incorrect number of args
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Notify Error
                                if (args.length >= argsMinLength) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§e many§7 args). For info: §e/goop mmoitems " + subsonic));

                                } else {

                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§6 few§7 args). For info: §e/goop mmoitems " + subsonic));
                                }

                                // Notify Usage
                                logReturn.add("§3Usage: §e" + usage);
                            }

                            break;
                        //endregion
                        //region Abilities
                        case "abilities":
                        case "ability":
                            //   0       1        2        3       4       5       6        7       args.Length
                            // /goop mmoitems abilities <player> <slot> <ability> <trigger> [params]
                            //   -       0        1         2      3       4       5        6       args[n]

                            // Correct number of args?
                            argsMinLength = 6;
                            argsMaxLength = 7;
                            usage = "/goop mmoitems abilities <player> <slot> <ability> <trigger> [modifiers]";
                            subcommand = "Ability";
                            subcategory = "MMOItems - Ability";

                            // Help form?
                            if (args.length == 2)  {

                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Adds abilities to items.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<player> §7Player who has the item.");
                                logReturn.add("§3 - §e<slot> §7Slot of the target item.");
                                logReturn.add("§3 - §e<ability> §7Internal name of the ability.");
                                logReturn.add("§3 - §e<trigger> §7Trigger of the ability.");
                                logReturn.add("§3 - §e[modifier1]=[value];[modifier2]=[value];... §7Modifiers of the ability");

                                // Correct number of args?
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {

                                // Does the player exist?
                                if (targets.isEmpty() && asDroppedItem == null) {
                                    // Failure
                                    failure = true;

                                    // Notify the error
                                    if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Target must be an online player!"));
                                }

                                // Confirm that this ability exists
                                String ability = args[4].toUpperCase().replace("-", "_").replace(" ", "_").replaceAll("[^A-Z0-9_]", "");
                                RegisteredSkill skill = null;
                                if (!MMOItems.plugin.getSkills().hasSkill(ability)) {
                                    failure = true;
                                    if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Specified ability §3" + ability + "§7 is not loaded."));
                                } else {
                                    skill = MMOItems.plugin.getSkills().getSkill(ability);
                                }

                                String trigger = args[5].toUpperCase().replace("-", "_").replace(" ", "_").replaceAll("[^A-Z0-9_]", "");
                                TriggerType tt = TriggerType.valueOf(trigger);
                                if (tt == null) {
                                    failure = true;
                                    if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Ability trigger type §3" + trigger + "§7 does not exist."));}

                                final ArrayList<CompactCodedValue> mods = new ArrayList<>();
                                if (skill == null) { failure = true; } else if (args.length == 7) {
                                    mods.addAll(CompactCodedValue.ListFromString(args[6]));
                                    for (CompactCodedValue mod : mods) {
                                        if (!skill.getHandler().getParameters().contains(mod.getID())) {
                                            failure = true;
                                            if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Specified modifier §3" + mod.getID() + "§7 is not loaded for ability §e" + ability));}

                                        if (!OotilityCeption.DoubleTryParse(mod.getValue())) {
                                            failure = true;
                                            if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Specified value §b" + mod.getValue() + "§7 for modifier §3" + mod.getID() + "§7 is not a number. "));}
                                    }
                                }

                                if (!failure) {

                                    // Preparation of Methods
                                    TargetedItems executor = new TargetedItems(false, true,
                                            chained, commandChain, sender, failMessage,

                                            // What method to use to process the item
                                            iSource -> AddAbility(iSource.getValidOriginal(), ability, trigger, mods, iSource.getLogAddition()),

                                            // When will it succeed
                                            iSource -> iSource.getResult() != null,

                                            null
                                    );

                                    // Register the ItemStacks
                                    if (asDroppedItem != null) { executor.registerDroppedItem((Item) asDroppedItem); }
                                    executor.registerPlayers(targets, args[3], executor.getIncludedStrBuilder());

                                    // Process the stuff
                                    executor.process();

                                    // Was there any log messages output?
                                    if (!executor.getIncludedStrBuilder().isEmpty()) { logReturn.add(OotilityCeption.LogFormat(subcategory, executor.getIncludedStrBuilder().toString())); }

                                }

                            // Incorrect number of args
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Notify Error
                                if (args.length >= argsMinLength) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§e many§7 args). For info: §e/goop mmoitems " + subsonic));

                                } else {

                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§6 few§7 args). For info: §e/goop mmoitems " + subsonic));
                                }

                                // Notify Usage
                                logReturn.add("§3Usage: §e" + usage);
                            }

                            break;
                        //endregion
                        //region Set Tier
                        case "settier":
                            //   0       1     2      3       4       5         6       args.Length
                            // /goop mmoitems tier <player> <slot> <value>
                            //   -       0     1      2       3       4         5       args[n]

                            // Correct number of args?
                            argsMinLength = 5;
                            argsMaxLength = 5;
                            usage = "/goop mmoitems setTier <player> <slot> <value>";
                            subcommand = "Set Tier";
                            subcategory = "MMOItems - Set Tier";

                            // Help form?
                            if (args.length == 2)  {

                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Changes the tier of items.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<player> §7Player who has the item.");
                                logReturn.add("§3 - §e<slot> §7Slot of the target item.");
                                logReturn.add("§3 - §e<value> §7Tier to set.");
                                logReturn.add("§3      * §bnone§7 keyword to remove tier.");

                                // Correct number of args?
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {

                                // Does the player exist?
                                if (targets.isEmpty() && asDroppedItem == null) {
                                    // Failure
                                    failure = true;

                                    // Notify the error
                                    if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Target must be an online player!"));
                                }

                                String tier = args[4];  // No way to get it wrong lol
                                if (!"none".equals(args[4])) {

                                    // Parse true tier
                                    tier = args[4].toUpperCase();
                                    if (!GooPMMOItems.TierExists(tier)) {
                                        // Failure
                                        failure = true;

                                        if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Specified Tier §3" + args[4] + "§7 is not loaded."));
                                    }
                                }

                                if (!failure) {

                                    // Copy of finals
                                    final String finalTier = tier;

                                    // Preparation of Methods
                                    TargetedItems executor = new TargetedItems(false, true,
                                            chained, commandChain, sender, failMessage,

                                            // What method to use to process the item
                                            iSource -> SetTier(iSource.getValidOriginal(), finalTier, null, iSource.getLogAddition()),

                                            // When will it succeed
                                            iSource -> iSource.getResult() != null,

                                            null
                                        );

                                    // Register the ItemStacks
                                    if (asDroppedItem != null) { executor.registerDroppedItem((Item) asDroppedItem); }
                                    executor.registerPlayers(targets, args[3], executor.getIncludedStrBuilder());

                                    // Process the stuff
                                    executor.process();

                                    // Was there any log messages output?
                                    if (!executor.getIncludedStrBuilder().isEmpty()) { logReturn.add(OotilityCeption.LogFormat(subcategory, executor.getIncludedStrBuilder().toString())); }

                                }

                            // Incorrect number of args
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Notify Error
                                if (args.length >= argsMinLength) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§e many§7 args). For info: §e/goop mmoitems " + subsonic));

                                } else {

                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§6 few§7 args). For info: §e/goop mmoitems " + subsonic));
                                }

                                // Notify Usage
                                logReturn.add("§3Usage: §e" + usage);
                            }

                            break;
                        //endregion
                        //region Modifier
                        case "modifier":
                            //   0       1      2          3       4      5       6         args.Length
                            // /goop mmoitems modifier <player> <slot> <name> [use-global]
                            //   -       0      1          2       3      4       5         args[n]

                            // Correct number of args?
                            argsMinLength = 5;
                            argsMaxLength = 7;
                            usage = "/goop mmoitems modifier <player> <slot> <name> [use-global] [use-chances]";
                            subcommand = "Modifier";
                            subcategory = "MMOItems - Modifier";

                            // Help form?
                            if (args.length == 2)  {

                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Changes the modifiers of items.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<player> §7Player who has the item.");
                                logReturn.add("§3 - §e<slot> §7Slot of the target item.");
                                logReturn.add("§3 - §e<name> §7Name of the modifier to add.");
                                logReturn.add("§3      * §bnone§7 keyword to remove all modifiers.");
                                logReturn.add("§3      * §brandom§7 keyword to to add random modifier.");
                                logReturn.add("§3 - §e[use-global] §7Should this also consider those in modifiers.yml?");
                                logReturn.add("§3 - §e[use-chances] §7Take into account the modifier chances?");

                                // Correct number of args?
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {

                                // Does the player exist?
                                if (targets.isEmpty() && asDroppedItem == null) {
                                    // Failure
                                    failure = true;

                                    // Notify the error
                                    if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Target must be an online player!"));
                                }

                                boolean scryAll = true;

                                // Read that
                                if (args.length >= 6) {

                                    // Read value
                                    if (OotilityCeption.BoolTryParse(args[5])) {

                                        // Parse
                                        scryAll = OotilityCeption.BoolParse(args[5]);

                                    } else {

                                        // Failure
                                        failure = true;

                                        // Notify the error
                                        if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Expected §btrue§7 or §bfalse§7 for 'use-global' instead of §e" + args[5] + "§7. "));
                                    }
                                }

                                boolean chances = false;

                                // Read that
                                if (args.length >= 7) {

                                    // Read value
                                    if (OotilityCeption.BoolTryParse(args[6])) {

                                        // Parse
                                        chances = OotilityCeption.BoolParse(args[6]);

                                    } else {

                                        // Failure
                                        failure = true;

                                        // Notify the error
                                        if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Expected §btrue§7 or §bfalse§7 for 'use-chances' instead of §e" + args[6] + "§7. "));
                                    }
                                }

                                if (!failure) {

                                    /*
                                     *      Copy of Finals
                                     */
                                    final boolean finalScryAll = scryAll;
                                    final boolean finalChances = chances;
                                    final String modifierName = args[4];

                                    /*
                                     *      Preparation of Methods and Lambdas
                                     */
                                    TargetedItems executor = new TargetedItems(false, true,
                                            chained, commandChain, sender, failMessage,

                                            // What method to use to process the item
                                            iSource -> GooPMMOItems.ModifierOperation(modifierName, iSource.getValidOriginal(), finalScryAll, finalChances, iSource.getLogAddition()),

                                            // When will it succeed
                                            iSource -> iSource.getResult() != null,

                                            null
                                    );

                                    /*
                                     *      Registration of items
                                     */
                                    if (asDroppedItem != null) { executor.registerDroppedItem((Item) asDroppedItem); }
                                    executor.registerPlayers(targets, args[3], executor.getIncludedStrBuilder());

                                    /*
                                     *      Processing
                                     */
                                    executor.process();

                                    /*
                                     *      Output Consolidation
                                     */
                                    if (!executor.getIncludedStrBuilder().isEmpty()) { logReturn.add(OotilityCeption.LogFormat(subcategory, executor.getIncludedStrBuilder().toString())); }

                                }

                            // Incorrect number of args
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Notify Error
                                if (args.length >= argsMinLength) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§e many§7 args). For info: §e/goop mmoitems " + subsonic));

                                } else {

                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§6 few§7 args). For info: §e/goop mmoitems " + subsonic));
                                }

                                // Notify Usage
                                logReturn.add("§3Usage: §e" + usage);
                            }
                            break;
                        //endregion
                        //region Regenerate
                        case "regenerate":
                            //   0       1     2        3       4       5       6           args.Length
                            // /goop mmoitems regen <player> <slot> [reroll] [keep...]
                            //   -       0     1        2       3       4       5           args[n]

                            // Correct number of args?
                            argsMinLength = 4;
                            argsMaxLength = 16;
                            usage = "/goop mmoitems regenerate <player> <slot> [reroll] [keep...]";
                            subcommand = "Regenerate";
                            subcategory = "MMOItems - Regenerate";

                            // Help form?
                            if (args.length == 2)  {

                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 RevID-updates items to fix stacking.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<player> §7Player who has the item.");
                                logReturn.add("§3 - §e<slot> §7Slot of the target item.");
                                logReturn.add("§3 - §e[reroll] §7Should reroll item RNG stats?");
                                logReturn.add("§3 - §e[keep] §7Item data to keep.");
                                logReturn.add("§3      *§b name§7 Keep the display name.");
                                logReturn.add("§3      *§b lore§7 Keep lore... refer to MI Docs.");
                                logReturn.add("§3      *§b ench§7 Keep enchantments.");
                                logReturn.add("§3      *§b upgr§7 Keep upgrades.");
                                logReturn.add("§3      *§b gems§7 Keep gemstones.");
                                logReturn.add("§3      *§b soul§7 Keep soulbounds.");
                                logReturn.add("§3      *§b skin§7 Keep skin.");
                                logReturn.add("§3      *§b exsh§7 Keep GooP's added stats.");
                                logReturn.add("§3      *§b mods§7 Keep modifiers.");
                                logReturn.add("§3      *§b aench§7 Keep advanced enchantments.");
                                logReturn.add("§8Specify all the data to keep by listing the keywords.");
                                logReturn.add("§8ex: §6...<slot> [reroll] ench upgr gems skin exsh mods");

                                // Correct number of args?
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {

                                // Does the player exist?
                                if (targets.isEmpty() && asDroppedItem == null) {
                                    // Failure
                                    failure = true;

                                    // Notify the error
                                    if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Target must be an online player!")); }

                                boolean reroll = true;
                                if (args.length >= 5) {
                                    if (OotilityCeption.BoolTryParse(args[4])) {
                                        reroll = Boolean.parseBoolean(args[4]);
                                    } else {

                                        // Failure
                                        failure = true;

                                        // Notify the error
                                        if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Expected §btrue§7 or §bfalse§7 instead of §e" + args[4] + "§7 regarding whether RNG stats should be rerolled."));
                                    } }

                                boolean name = false,
                                        lore = false,
                                        ench = false,
                                        upgr = false,
                                        gems = false,
                                        soul = false,
                                        exsh = false,
                                        mods = false,
                                        ae = false,
                                        skin = false;

                                if (args.length >= 6) {

                                    for (int i = 5; i < args.length; i++) {
                                        String str = args[i].toLowerCase();
                                        if (str.contains("name")) { name = true; }
                                        if (str.contains("lore")) { lore = true; }
                                        if (str.contains("ench")) { ench = true; }
                                        if (str.contains("upgr")) { upgr = true; }
                                        if (str.contains("gems")) { gems = true; }
                                        if (str.contains("soul")) { soul = true; }
                                        if (str.contains("skin")) { skin = true; }
                                        if (str.contains("ex") && str.contains("sh")) { exsh = true; }
                                        if (str.contains("mod")) { mods = true; }
                                        if (str.contains("aench") || str.contains("advanced") || str.contains("adv")) { ae = true; } }
                                }

                                if (!failure) {


                                    /*
                                     *      Copy of Finals
                                     */
                                    final boolean finalName = name;
                                    final boolean finalLore = lore;
                                    final boolean finalEnch = ench;
                                    final boolean finalUpgr = upgr;
                                    final boolean finalGems = gems;
                                    final boolean finalSkin = skin;
                                    final boolean finalAe = ae;
                                    final boolean finalMods = mods;
                                    final boolean finalReroll = reroll;
                                    final boolean finalExsh = exsh;
                                    final boolean finalSoul = soul;

                                    /*
                                     *      Preparation of Methods and Lambdas
                                     */
                                    TargetedItems executor = new TargetedItems(false, true,
                                            chained, commandChain, sender, failMessage,

                                            // What method to use to process the item
                                            iSource -> {
                                                // Keep Advanced Enchantments?
                                                Map<String, Integer> aeEnchants = null;
                                                if (finalAe) { aeEnchants = GooPAdvancedEnchantments.getEnchantments(iSource.getValidOriginal()); }

                                                // Reforge
                                                ItemStack regenerated = GooPMMOItems.ReforgeMMOItem(iSource.getValidOriginal(), iSource.getLogAddition(), finalName, finalLore, finalEnch, finalUpgr, finalGems, finalSoul, finalExsh, finalReroll, finalMods, finalAe, finalSkin);

                                                // Reapply Advanced Enchantments
                                                if (regenerated != null && aeEnchants != null) { regenerated = GooPAdvancedEnchantments.reapplyEnchantments(regenerated, aeEnchants); }

                                                return regenerated;
                                            },

                                            // When will it succeed
                                            iSource -> iSource.getResult() != null,

                                            null
                                    );

                                    /*
                                     *      Registration of items
                                     */
                                    if (asDroppedItem != null) { executor.registerDroppedItem((Item) asDroppedItem); }
                                    executor.registerPlayers(targets, args[3], executor.getIncludedStrBuilder());

                                    /*
                                     *      Processing
                                     */
                                    executor.process();

                                    /*
                                     *      Output Consolidation
                                     */
                                    if (!executor.getIncludedStrBuilder().isEmpty()) { logReturn.add(OotilityCeption.LogFormat(subcategory, executor.getIncludedStrBuilder().toString())); }
                                }


                            // Incorrect number of args
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Notify Error
                                if (args.length >= argsMinLength) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§e many§7 args). For info: §e/goop mmoitems " + subsonic));

                                } else {

                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§6 few§7 args). For info: §e/goop mmoitems " + subsonic));
                                }

                                // Notify Usage
                                logReturn.add("§3Usage: §e" + usage);
                            }
                            break;
                        //endregion
                        //region Get Tier
                        case "gettier":
                            //   0       1     2      3       4       5             6       7  args.Length
                            // /goop mmoitems getTier <player> <slot> <value> [objective] [±][score][%]
                            //   -       0     1      2       3       4             5       6    args[n]

                            // Correct number of args?
                            argsMinLength = 5;
                            argsMaxLength = 7;
                            usage = "/goop mmoitems getTier <player> <slot> <value> [objective] [±][score][%]";
                            subcommand = "Get Tier";
                            subcategory = "MMOItems - Get Tier";

                            // Help form?
                            if (args.length == 2)  {

                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Detect items by tier.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<player> §7Player who has the item.");
                                logReturn.add("§3 - §e<slot> §7Slot of the target item.");
                                logReturn.add("§3 - §e<name> §7Name of the modifier to add.");
                                logReturn.add("§3 - §e<value> §7Internal name of the tier to match.");
                                logReturn.add("§3      * §bnone§7 detect an item with no tier.");
                                logReturn.add("§3 - §e[objective] §7Scoreboard objective to output.");
                                logReturn.add("§3 - §e[±][score][%] §7Score operation if success.");

                            // Correct number of args?
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {

                                // Does the player exist?
                                if (targets.isEmpty()) {
                                    // Failure
                                    failure = true;

                                    // Notify the error
                                    if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Target must be an online player!"));
                                }

                                String tier = args[4].toUpperCase();  // No way to get it wrong lol
                                if (args[4].equals("none")) {
                                    tier = args[4];

                                } else if (!GooPMMOItems.TierExists(tier)) {
                                    // Failure
                                    failure = true;

                                    if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Specified Tier §3" + args[4] + "§7 is not loaded. If trying to detect the item not having any tier, use the §bnone§7 keyword."));
                                }

                                // Some scoreboards to test
                                Objective targetObjective = null;
                                PlusMinusPercent score = null;
                                if (args.length >= 7) {
                                    ScoreboardManager manager = Bukkit.getScoreboardManager();
                                    Scoreboard targetScoreboard = manager.getMainScoreboard();
                                    targetObjective = targetScoreboard.getObjective(args[5]);

                                    if (targetObjective == null) {
                                        // Failure
                                        failure = true;

                                        if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Scoreboard objective §3" + args[5] + "§7 does not exist."));
                                    }

                                    score = PlusMinusPercent.GetPMP(args[6], refAddition);
                                    if (score == null) {
                                        // Failure
                                        failure = true;

                                        // Mention it
                                        if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "The final score '§3" + args[6] + "§7' should be an integer number (maybe with an operation)."));
                                    }
                                }

                                if (!failure) {

                                    /*
                                     *      Copy of Finals
                                     */
                                    final String finalTier = tier;
                                    final boolean useObjective = (targetObjective != null);
                                    final PlusMinusPercent finalScore = score;
                                    final Objective finalTargetObjective = targetObjective;

                                    /*
                                     *      Preparation of Methods and Lambdas
                                     */
                                    TargetedItems executor = new TargetedItems(false, false,
                                            chained, commandChain, sender, failMessage,

                                            // What method to use to process the item
                                            iSource -> GooPMMOItems.SetTier(iSource.getValidOriginal(), null, iSource.getRef_str_a(), iSource.getLogAddition()),

                                            // When will it succeed
                                            iSource -> {

                                                // If the result
                                                if (iSource.getResult() == null) { return false; }

                                                // If they equal
                                                return finalTier.equals(iSource.getRef_str_a().getValue());
                                            },

                                            // Handle score if
                                            (iSource, sInfo) -> {

                                                // Anything to consider
                                                if (useObjective) {

                                                    sInfo.setFinalScoreboardOpp(finalTargetObjective, finalScore);
                                                }
                                            }
                                    );

                                    /*
                                     *      Registration of items
                                     */
                                    if (asDroppedItem != null) { executor.registerDroppedItem((Item) asDroppedItem); }
                                    executor.registerPlayers(targets, args[3], executor.getIncludedStrBuilder());

                                    /*
                                     *      Processing
                                     */
                                    executor.process();

                                    /*
                                     *      Output Consolidation
                                     */
                                    if (!executor.getIncludedStrBuilder().isEmpty()) { logReturn.add(OotilityCeption.LogFormat(subcategory, executor.getIncludedStrBuilder().toString())); }

                                }

                            // Incorrect number of args
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Notify Error
                                if (args.length >= argsMinLength) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§e many§7 args). For info: §e/goop mmoitems " + subsonic));

                                } else {

                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§6 few§7 args). For info: §e/goop mmoitems " + subsonic));
                                }

                                // Notify Usage
                                logReturn.add("§3Usage: §e" + usage);
                            }
                            break;
                        //endregion
                        //region New Shrub
                        case "newshrub":
                            //   0      1        2       3     4   5   6   7     args.Length
                            // /goop mmoitems newShrub <type> [w] [x] [y] [z]
                            //   -      0        1       2     3   4   5   6     args[n]

                            // Correct number of args?
                            argsMinLength = 3;
                            argsMaxLength = 7;
                            usage = "/goop mmoitems newShrub <type> [w] [x] [y] [z]";
                            subcommand = "New Shrub";
                            subcategory = "MMOItems - New Shrub";

                            if (!Gunging_Ootilities_Plugin.usingMMOItemShrubs) {

                                // Notify fuCk
                                if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "§cYou don't have MMOItem Shrubs Module Installed!"));
                                }

                            // Help form?
                            } else if (args.length == 2)  {

                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Create a new shrub.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<type> §7Type of shrub");
                                logReturn.add("§3 - §e[w] §7World name");
                                logReturn.add("§3 - §e[x] §7X Position");
                                logReturn.add("§3 - §e[y] §7Y Position");
                                logReturn.add("§3 - §e[z] §7Z Position");

                                // Correct number of args?
                                } else if (args.length == argsMinLength || args.length == argsMaxLength) {

                                // Gets that location boi
                                Location targetLocation = null;
                                if (args.length == 3) {
                                    if (sender instanceof Player) {

                                        // Just target location I guess?
                                        Block bLock = ((Player)sender).getTargetBlockExact(30, FluidCollisionMode.NEVER);

                                        // If exists
                                        if (bLock == null) {

                                            // Invalid location
                                            failure = true;

                                            // Mention
                                            if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory,"You are not looking at any block!"));

                                            // I suppose its not air, right?
                                        } else if (!OotilityCeption.IsAir(bLock.getType())) {

                                            // Git Target Location
                                            targetLocation = bLock.getLocation();

                                            // Nvm it is air.
                                        } else {

                                            // Invalid location
                                            failure = true;

                                            // Mention
                                            if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory,"You are not looking at any block!"));

                                        }

                                    } else {
                                        // Vro need coords
                                        failure = true;

                                        // Say
                                        if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "When making shrubs from the console, you must specify co-ordinates and a world!"));
                                    }

                                    // Build Location from args, later, if they parse.
                                }

                                // Is type loaded?
                                RefSimulator<String> logAddition = new RefSimulator<>("");
                                if (!GooPE_Shrubs.IsShrubTypeLoaded(args[2], logAddition)) {

                                    // Not loaded - fail
                                    failure = true;

                                    // Comment
                                    if (logAddition.GetValue() != null) { logReturn.add(OotilityCeption.LogFormat(subcategory, logAddition.GetValue())); }
                                }

                                // Parse location?
                                if (args.length == 7) {

                                    // Relativity
                                    Player rel = null;
                                    if (sender instanceof Player) { rel = (Player)sender;}

                                    // Get
                                    targetLocation = OotilityCeption.ValidLocation(rel, args[3], args[4], args[5], args[6], logAddition);

                                    // Ret
                                    if (targetLocation == null) { failure = true; }

                                    // Add Log
                                    if (logAddition.GetValue() != null) { logReturn.add(OotilityCeption.LogFormat(subcategory, logAddition.GetValue())); }
                                }

                                // Byce syntax
                                if (!failure) {

                                    // Exec
                                    GooPE_Shrubs.CreateShrubInstanceAt(args[2], targetLocation, logAddition);

                                    // Notify
                                    if (logAddition.GetValue() != null) { logReturn.add(OotilityCeption.LogFormat(subcategory, logAddition.GetValue())); }
                                }

                            // Incorrect number of args
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Notify Error
                                if (args.length <= argsMaxLength) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§e many§7 args). For info: §e/goop mmoitems " + subsonic));

                                } else {

                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§6 few§7 args). For info: §e/goop mmoitems " + subsonic));
                                }

                                // Notify Usage
                                logReturn.add("§3Usage: §e" + usage);
                            }

                            break;
                        //endregion
                        //region Stat
                        case "stat":
                            //   0      1       2    3       4        5         6         [7]     [8]  7       [9] 8       args.Length
                            // /goop mmoitems stat <stat> <player> <slot> [±]<value>[%] [range] [objective] [±][score][%]
                            //   -      0       1    2       3       4          5         [6]     [7]  6       [8] 7       args[n]

                            // Correct number of args?
                            argsMinLength = 6;
                            argsMaxLength = 9;
                            usage = "/goop mmoitems stat <stat> <player> <slot> [±]<value>[%] [range] [objective] [±][score][%]";
                            subcommand = "Stat";
                            subcategory = "MMOItems - Stat";

                            // Help form?
                            if (args.length == 2)  {

                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Edit MMOItem stat values.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<stat> §7MMOItem Stat to Edit");
                                logReturn.add("§3 - §e<player> §7Player who has the item.");
                                logReturn.add("§3 - §e<slot> §7Slot of the target item.");
                                logReturn.add("§3 - §e[±]<value>[%] §7Operation to perform on the stat.");
                                logReturn.add("§3      *§e boolean§7 Either §btrue§7, §bfalse§7, or §btoggle§7.");
                                logReturn.add("§3      *§e text§7 This can be any text value,§b __§7 as spaces.");
                                logReturn.add("§3      *§e text list§7 Precede text with §b-§7 to remove.");
                                logReturn.add("§3      *§b read§7 Keyword to read the value only.");
                                logReturn.add("§3 - §e[range] §7Range that allows this command to succeed.");
                                logReturn.add("§3      *§e boolean§7 Supports §btrue§7 or §bfalse§7.");
                                logReturn.add("§3      *§e text§7 Must match this exactly.");
                                logReturn.add("§3      *§e text list§7 List size must fall in range (if number range).");
                                logReturn.add("§3      *§e text list§7 List must contain this entry.");
                                logReturn.add("§3 - §e[objective] §7Scoreboard objective to output.");
                                logReturn.add("§3 - §e[±][score][%] §7Score operation if success.");
                                logReturn.add("§3      *§7 Only works with numeric stats.");
                                logReturn.add("§3      *§b read§7 keyword to store result of operation. Numeric");
                                logReturn.add("§7        stats only. Multiplies by §b100§7 to preserve decimals.");

                                // Correct number of args?
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {

                                // Does the player exist?
                                if (targets.isEmpty() && asDroppedItem == null) {
                                    // Failure
                                    failure = true;

                                    // Notify the error
                                    if (Gunging_Ootilities_Plugin.sendGooPFailFeedback)
                                        logReturn.add(OotilityCeption.LogFormat(subcategory, "Target must be an online player!"));
                                }

                                // Attempt to get
                                final ItemStat stat = Stat(args[2]);
                                if (stat == null) {

                                    // Failure
                                    failure = true;

                                    // Notify the error
                                    if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback)
                                        logReturn.add(OotilityCeption.LogFormat(subcategory, "Stat§3 " + args[2] + "§7 does not exist."));
                                }

                                // Value will be identified.... later...
                                final String unidentifiedValue = args[5];

                                // Enough args to have something to test idk
                                Objective targetObjective = null;
                                PlusMinusPercent scoreOperation = null;
                                String unparsedRange = null;
                                boolean readValue = unidentifiedValue.equals("read");

                                String objectiveName = null;
                                String objectiveOpp = null;
                                //STAT//OotilityCeption.Log("§7STAT§6 CMD§7 Default Readonly:§b " + readValue + "§8 at§9 " + args.length);

                                // Is it range or default-read objective?
                                if (args.length == 7) {
                                    objectiveName = args[6];
                                    targetObjective = OotilityCeption.GetObjective(objectiveName);

                                    // Because this is unknown, it cannot fail yet.
                                    if (targetObjective == null) { unparsedRange = args[6]; }
                                }

                                // Must be scored objective
                                if (args.length == 8) {

                                    // Will fail if null
                                    objectiveName = args[6];
                                    targetObjective = OotilityCeption.GetObjective(objectiveName);

                                    // Will fail if nonsense
                                    objectiveOpp = args[7];
                                    readValue = objectiveOpp.equals("read");
                                    //STAT//OotilityCeption.Log("§7STAT§6 CMD§7 L8 Readonly:§b " + readValue);
                                    if (!readValue) { scoreOperation = PlusMinusPercent.GetPMP(objectiveOpp, refAddition); }
                                }

                                // Both range and objective
                                if (args.length == 9) {
                                    unparsedRange = args[6];

                                    // Will fail if null
                                    objectiveName = args[7];
                                    targetObjective = OotilityCeption.GetObjective(objectiveName);

                                    // Will fail if nonsense
                                    objectiveOpp = args[8];
                                    readValue = objectiveOpp.equals("read");
                                    //STAT//OotilityCeption.Log("§7STAT§6 CMD§7 L9 Readonly:§b " + readValue);
                                    if (!readValue) { scoreOperation = PlusMinusPercent.GetPMP(objectiveOpp, refAddition); }
                                }

                                // Fail due to nonsense score params
                                if (args.length >= 8) {
                                    if (targetObjective == null) {

                                        // Failure
                                        failure = true;

                                        // Notify
                                        if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) { logReturn.add(OotilityCeption.LogFormat(subcategory, "Scoreboard objective §3" + objectiveName + "§7 does not exist. ")); }
                                    }

                                    if (!readValue && scoreOperation == null) {

                                        // Failure
                                        failure = true;

                                        // Notify
                                        if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) { logReturn.add(OotilityCeption.LogFormat(subcategory, "Scoreboard operation §3" + objectiveOpp + "§7 is not in the correct format (§b+4§7, §b10§7, §b-20%§7...) nor the §bread§7 keyword: " + refAddition.getValue())); }
                                    }
                                }

                                // Not that stat!
                                if (!failure) {

                                    if (stat == ItemStats.UPGRADE) {

                                        // Failure
                                        failure = true;

                                        // Notify
                                        if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) { logReturn.add(OotilityCeption.LogFormat(subcategory, "Please use §e/goop mmoitems upgrade§7 to upgrade items, using §e/goop mmoitems stat UPGRADE§7 is not supported because the upgrade stat stores information on the item's upgrade level, max level, min level, upgrade template, upgrade reference...")); }
                                    }
                                }

                                // For every player
                                if (!failure) {

                                    /*
                                     *      Copy of Finals
                                     */
                                    final String finalUnparsedRange = unparsedRange;
                                    final boolean useObjective = (targetObjective != null);
                                    final PlusMinusPercent finalScoreOperation = scoreOperation;
                                    final Objective finalTargetObjective = targetObjective;
                                    final boolean finalReadValue = readValue;
                                    //STAT//OotilityCeption.Log("§7STAT§6 CMD§7 Final Readonly:§b " + finalReadValue);

                                    /*
                                     *      Preparation of Methods and Lambdas
                                     */
                                    TargetedItems executor = new TargetedItems(false, !finalReadValue,
                                            chained, commandChain, sender, failMessage,

                                            // What method to use to process the item
                                            iSource -> GooPMMOItems.StatOps(iSource.getValidOriginal(), stat, unidentifiedValue, finalUnparsedRange, iSource.getRef_dob_a(), iSource.getLogAddition()),

                                            // When will it succeed ~ When operation succeeds, or when it is reading without a range specified
                                            iSource -> (iSource.getResult() != null) || (finalReadValue && finalUnparsedRange == null),

                                            // Handle score if
                                            (iSource, sInfo) -> {

                                                // If the scoreboard stuff is even active
                                                if (useObjective) {

                                                    // Is there a PMP set?
                                                    if (finalScoreOperation != null) {

                                                        // Override them all
                                                        sInfo.setFinalScoreboardOpp(finalTargetObjective, finalScoreOperation);
                                                    } else {

                                                        // Initialize at zero
                                                        sInfo.setInitZero(finalTargetObjective);

                                                        // Add the individual stat values
                                                        sInfo.addScoreboardOpp(finalTargetObjective, iSource.getRef_dob_a().getValue() * 100, true, false);
                                                    }
                                                }

                                                if (finalScoreOperation == null) {

                                                    // Succesible Value
                                                    sInfo.setValueOfSuccess(iSource.getRef_dob_a().getValue());
                                                } else {

                                                    // Succesible Value
                                                    sInfo.setValueOfSuccess(finalScoreOperation.apply(0.0));
                                                }
                                            }
                                    );

                                    /*
                                     *      Registration of items
                                     */
                                    if (asDroppedItem != null) { executor.registerDroppedItem((Item) asDroppedItem); }
                                    executor.registerPlayers(targets, args[4], executor.getIncludedStrBuilder());

                                    /*
                                     *      Processing
                                     */
                                    executor.process();

                                    /*
                                     *      Output Consolidation
                                     */
                                    if (!executor.getIncludedStrBuilder().isEmpty()) { logReturn.add(OotilityCeption.LogFormat(subcategory, executor.getIncludedStrBuilder().toString())); }

                                }

                            // Incorrect number of args
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Notify Error
                                if (args.length >= argsMinLength) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§e many§7 args). For info: §e/goop mmoitems " + subsonic));

                                } else {

                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§6 few§7 args). For info: §e/goop mmoitems " + subsonic));
                                }

                                // Notify Usage
                                logReturn.add("§3Usage: §e" + usage);
                            }
                            break;
                        //endregion
                        //region List Shrub Types
                        case "listshrubtypes":
                            //   0      1           2       args.Length
                            // /goop mmoitems listShrubTypes
                            //   -      0           1       args[n]

                            // Correct number of args?
                            argsMaxLength = 2;
                            usage = "/goop mmoitems listShrubTypes";
                            subcommand = "List Shrub Types";
                            subcategory = "MMOItems - List Shrub Types";

                            if (!Gunging_Ootilities_Plugin.usingMMOItemShrubs) {

                                // Notify fuCk
                                if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "§cYou don't have MMOItem Shrubs Module Installed!"));
                                }

                                // Help form?
                            } else if (args.length <= argsMaxLength) {

                                // Exec
                                ArrayList<String> lShrubs = GooPE_Shrubs.getLoadedShrubTypes();

                                if (lShrubs.isEmpty()) {
                                    GooPE_Shrubs.ReloadShrubNames();
                                    lShrubs = GooPE_Shrubs.getLoadedShrubTypes();
                                }

                                logReturn.add("§e______________________________________________");
                                if (lShrubs.isEmpty()) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "§7Would list all loaded shrub types if there were any."));
                                } else {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "§7All loaded shrub types:"));
                                    for (String struct : lShrubs) { logReturn.add("§3 - §7" + struct); }
                                }


                            // Incorrect number of args
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Notify Error
                                logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§e many§7 args). For info: §e/goop mmoitems " + subsonic));

                                // Notify Usage
                                logReturn.add("§3Usage: §e" + usage);
                            }

                            break;
                        //endregion
                        //region Fix Stacks
                        case "fixstacks":
                            //   0      1        2          3       args.Length
                            // /goop mmoitems fixstacks [player]
                            //   -      0        1          2       args[n]
                            argsMaxLength = 3;
                            usage = "/goop mmoitems fixstacks [player]";
                            subcommand = "Fix Stacks";
                            subcategory = "MMOItems - Fix Stacks";

                            // Help form?
                            if (args.length <= argsMaxLength) {

                                // Get Target (if included)
                                if (args.length == 3 && OotilityCeption.hasPermission(sender, "mmoitems", "fixstacks.others")) {

                                    // Git if online
                                    targets = OotilityCeption.GetPlayers(senderLocation, args[2], null);
                                    
                                } else if (sender instanceof Player) {

                                    // Git if exist
                                    targets = new ArrayList<>();
                                    targets.add((Player) sender);

                                } else {
                                    failure = true;

                                    // Not contained, and from console
                                    if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) {
                                        logReturn.add(OotilityCeption.LogFormat(subcategory, "§7This cannot be called from the console without specifying a player."));
                                    }
                                }
                                
                                if (targets == null || targets.isEmpty()) {

                                    // Fail
                                    failure = true;

                                    // Not contained, and from console
                                    if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) {
                                        logReturn.add(OotilityCeption.LogFormat(subcategory, "§7Must specify an online player."));
                                    }
                                }
                                
                                if (!failure) {


                                    /*
                                     *      Copy of Finals
                                     */

                                    /*
                                     *      Preparation of Methods and Lambdas
                                     */
                                    TargetedItems executor = new TargetedItems(false, true,
                                            chained, commandChain, sender, failMessage,

                                            // What method to use to process the item
                                            iSource -> GooPMMOItems.FixStackableness(iSource.getValidOriginal(), iSource.getLogAddition()),

                                            // When will it succeed
                                            iSource -> iSource.getResult() != null,

                                            // Handle score if
                                            null
                                    );

                                    /*
                                     *      Registration of items
                                     */
                                    if (asDroppedItem != null) { executor.registerDroppedItem((Item) asDroppedItem); }
                                    executor.registerPlayers(targets, "*", executor.getIncludedStrBuilder());

                                    /*
                                     *      Processing
                                     */
                                    executor.notSuccessible().process();

                                    /*
                                     *      Output Consolidation
                                     */
                                    if (!executor.getIncludedStrBuilder().isEmpty()) { logReturn.add(OotilityCeption.LogFormat(subcategory, executor.getIncludedStrBuilder().toString())); }
                                }
                                
                            // Incorrect number of args
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Notify Error
                                logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§e many§7 args). For info: §e/goop mmoitems " + subsonic));

                                // Notify Usage
                                logReturn.add("§3Usage: §e" + usage);
                            }
                            
                            break;
                        //endregion
                        //region Reload Shrub Types
                        case "reloadshrubtypes":
                            //   0      1           2       args.Length
                            // /goop mmoitems reloadShrubTypes
                            //   -      0           1       args[n]

                            // Correct number of args?
                            argsMaxLength = 2;
                            usage = "/goop mmoitems reloadShrubTypes";
                            subcommand = "Reload Shrub Types";
                            subcategory = "MMOItems - Reload Shrub Types";

                            if (!Gunging_Ootilities_Plugin.usingMMOItemShrubs) {

                                // Notify fuCk
                                if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {
                                    logReturn.add(OotilityCeption.LogFormat(subcategory, "§cYou don't have MMOItem Shrubs Module Installed!"));
                                }

                                // Help form?
                            } else if (args.length <= argsMaxLength) {

                                // Exec
                                GooPE_Shrubs.ReloadShrubTypes();
                                logReturn.add(OotilityCeption.LogFormat(subcategory, "Reloaded MMOItem Shrub Types."));

                                // Incorrect number of args
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {

                                // Notify Error
                                logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage (too§e many§7 args). For info: §e/goop mmoitems " + subsonic));

                                // Notify Usage
                                logReturn.add("§3Usage: §e" + usage);
                            }

                            break;
                        //endregion
                        //region Gemstone Extract
                        case "gemstoneextract":
                            argsMinLength = 4;
                            argsMaxLength = 5;
                            usage = "/goop mmoitems gemstoneextract <player> <slot> [socketIndex]";
                            subcommand = "Gemstone Extract";
                            subcategory = "MMOItems - Gemstone Extract";

                            if (args.length == 2) {
                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Extracts a gemstone from an item.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<player> §7Player who has the item.");
                                logReturn.add("§3 - §e<slot> §7Slot of the item.");
                                logReturn.add("§3 - §e[socketIndex] §7Index of the gemstone to extract (0-based, default: 0).");
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {
                                if (targets.isEmpty()) {
                                    failure = true;
                                    if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Target must be an online player!"));
                                }
                                if (!failure) {
                                    // Parse slot from string
                                    org.bukkit.inventory.EquipmentSlot slot = null;
                                    int slotIndex = -1;
                                    String slotArg = args[3].toLowerCase();
                                    switch (slotArg) {
                                        case "head": slot = org.bukkit.inventory.EquipmentSlot.HEAD; break;
                                        case "chest": slot = org.bukkit.inventory.EquipmentSlot.CHEST; break;
                                        case "legs": slot = org.bukkit.inventory.EquipmentSlot.LEGS; break;
                                        case "feet": slot = org.bukkit.inventory.EquipmentSlot.FEET; break;
                                        case "mainhand": case "hand": slot = org.bukkit.inventory.EquipmentSlot.HAND; break;
                                        case "offhand": slot = org.bukkit.inventory.EquipmentSlot.OFF_HAND; break;
                                        default:
                                            try { slotIndex = Integer.parseInt(args[3]); } catch (NumberFormatException ignored) {
                                                failure = true;
                                                if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Invalid slot: §e" + args[3]));
                                            }
                                            break;
                                    }
                                    if (!failure) {
                                        int socketIndex = 0;
                                        if (args.length >= 5) {
                                            try { socketIndex = Integer.parseInt(args[4]); } catch (NumberFormatException e) {
                                                failure = true;
                                                if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Invalid socket index: §e" + args[4]));
                                            }
                                        }
                                        if (!failure) {
                                            ItemStack item = null;
                                            Player target = targets.get(0);
                                            if (slot != null) { item = target.getEquipment().getItem(slot); }
                                            else if (slotIndex >= 0) { item = target.getInventory().getItem(slotIndex); }
                                            if (item == null || item.getType().isAir()) {
                                                failure = true;
                                                if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "No item in slot §e" + args[3]));
                                            }
                                            if (!failure) {
                                                List<String> extractLog = new ArrayList<>();
                                                ItemStack modifiedItem = ExtractGemstone(item, socketIndex, extractLog);
                                                logReturn.addAll(extractLog);
                                                if (modifiedItem != null) {
                                                    if (slot != null) { target.getEquipment().setItem(slot, modifiedItem); }
                                                    else { target.getInventory().setItem(slotIndex, modifiedItem); }
                                                    logReturn.add("§aGemstone extracted successfully!");
                                                } else { failure = true; }
                                            }
                                        }
                                    }
                                }
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {
                                logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage. For info: §e/goop mmoitems " + subsonic));
                                logReturn.add("§3Usage: §e" + usage);
                            }
                            break;
                        //endregion
                        //region Gemstone Swap
                        case "gemstoneswap":
                            argsMinLength = 7;
                            argsMaxLength = 7;
                            usage = "/goop mmoitems gemstoneswap <player> <slot> <socketIndex> <newGemType> <newGemId>";
                            subcommand = "Gemstone Swap";
                            subcategory = "MMOItems - Gemstone Swap";

                            if (args.length == 2) {
                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Swaps a gemstone in an item.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<player> §7Player who has the item.");
                                logReturn.add("§3 - §e<slot> §7Slot of the item.");
                                logReturn.add("§3 - §e<socketIndex> §7Index of the gemstone to swap (0-based).");
                                logReturn.add("§3 - §e<newGemType> §7Type of the new gemstone (e.g., GEM_STONE).");
                                logReturn.add("§3 - §e<newGemId> §7ID of the new gemstone (e.g., atk1).");
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {
                                if (targets.isEmpty()) {
                                    failure = true;
                                    if (Gunging_Ootilities_Plugin.sendGooPFailFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Target must be an online player!"));
                                }
                                if (!failure) {
                                    // Parse slot from string
                                    org.bukkit.inventory.EquipmentSlot slot = null;
                                    int slotIndex = -1;
                                    String slotArg = args[3].toLowerCase();
                                    switch (slotArg) {
                                        case "head": slot = org.bukkit.inventory.EquipmentSlot.HEAD; break;
                                        case "chest": slot = org.bukkit.inventory.EquipmentSlot.CHEST; break;
                                        case "legs": slot = org.bukkit.inventory.EquipmentSlot.LEGS; break;
                                        case "feet": slot = org.bukkit.inventory.EquipmentSlot.FEET; break;
                                        case "mainhand": case "hand": slot = org.bukkit.inventory.EquipmentSlot.HAND; break;
                                        case "offhand": slot = org.bukkit.inventory.EquipmentSlot.OFF_HAND; break;
                                        default:
                                            try { slotIndex = Integer.parseInt(args[3]); } catch (NumberFormatException ignored) {
                                                failure = true;
                                                if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Invalid slot: §e" + args[3]));
                                            }
                                            break;
                                    }
                                    if (!failure) {
                                        int socketIndex = 0;
                                        try { socketIndex = Integer.parseInt(args[4]); } catch (NumberFormatException e) {
                                            failure = true;
                                            if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "Invalid socket index: §e" + args[4]));
                                        }
                                        if (!failure) {
                                            String newGemType = args[5];
                                            String newGemId = args[6];
                                            ItemStack item = null;
                                            Player target = targets.get(0);
                                            if (slot != null) { item = target.getEquipment().getItem(slot); }
                                            else if (slotIndex >= 0) { item = target.getInventory().getItem(slotIndex); }
                                            if (item == null || item.getType().isAir()) {
                                                failure = true;
                                                if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat(subcategory, "No item in slot §e" + args[3]));
                                            }
                                            if (!failure) {
                                                List<String> swapLog = new ArrayList<>();
                                                ItemStack modifiedItem = SwapGemstone(item, socketIndex, newGemType, newGemId, swapLog);
                                                logReturn.addAll(swapLog);
                                                if (modifiedItem != null) {
                                                    if (slot != null) { target.getEquipment().setItem(slot, modifiedItem); }
                                                    else { target.getInventory().setItem(slotIndex, modifiedItem); }
                                                    logReturn.add("§aGemstone swapped successfully!");
                                                } else { failure = true; }
                                            }
                                        }
                                    }
                                }
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {
                                logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage. For info: §e/goop mmoitems " + subsonic));
                                logReturn.add("§3Usage: §e" + usage);
                            }
                            break;
                        //endregion
                        //region Gemstone Migration
                        case "gemstonemigrate":
                            argsMinLength = 5;
                            argsMaxLength = 5;
                            usage = "/goop mmoitems gemstonemigrate <gemType> <oldId> <newId>";
                            subcommand = "Gemstone Migration";
                            subcategory = "MMOItems - Gemstone Migration";

                            if (args.length == 2) {
                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Migrates gemstone IDs across the server.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<gemType> §7Type of gemstone (e.g., GEM_STONE).");
                                logReturn.add("§3 - §e<oldId> §7Current gemstone ID (e.g., atk_1).");
                                logReturn.add("§3 - §e<newId> §7New gemstone ID (e.g., atk1).");
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {
                                String gemType = args[2];
                                String oldId = args[3];
                                String newId = args[4];
                                List<String> migrateLog = new ArrayList<>();
                                int result = MigrateGemstones(gemType, oldId, newId, migrateLog);
                                logReturn.addAll(migrateLog);
                                if (result > 0) { logReturn.add("§aGemstone migration completed!"); }
                                else { failure = true; }
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {
                                logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage. For info: §e/goop mmoitems " + subsonic));
                                logReturn.add("§3Usage: §e" + usage);
                            }
                            break;
                        //endregion
                        //region Gemstone List
                        case "gemstonelist":
                            argsMinLength = 3;
                            argsMaxLength = 3;
                            usage = "/goop mmoitems gemstonelist <gemType>";
                            subcommand = "Gemstone List";
                            subcategory = "MMOItems - Gemstone List";

                            if (args.length == 2) {
                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Lists all gemstones of a type.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<gemType> §7Type of gemstone (e.g., GEM_STONE).");
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {
                                String gemType = args[2];
                                List<String> listLog = new ArrayList<>();
                                ListGemstones(gemType, listLog);
                                logReturn.addAll(listLog);
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {
                                logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage. For info: §e/goop mmoitems " + subsonic));
                                logReturn.add("§3Usage: §e" + usage);
                            }
                            break;
                        //endregion
                        //region Gemstone Info
                        case "gemstoneinfo":
                            argsMinLength = 4;
                            argsMaxLength = 4;
                            usage = "/goop mmoitems gemstoneinfo <gemType> <gemId>";
                            subcommand = "Gemstone Info";
                            subcategory = "MMOItems - Gemstone Info";

                            if (args.length == 2) {
                                logReturn.add("§e______________________________________________");
                                logReturn.add("§3MMOItems - §b" + subcommand + ",§7 Shows info about a gemstone.");
                                logReturn.add("§3Usage: §e" + usage);
                                logReturn.add("§3 - §e<gemType> §7Type of gemstone (e.g., GEM_STONE).");
                                logReturn.add("§3 - §e<gemId> §7ID of the gemstone (e.g., atk1).");
                            } else if (args.length >= argsMinLength && args.length <= argsMaxLength) {
                                String gemType = args[2];
                                String gemId = args[3];
                                List<String> infoLog = new ArrayList<>();
                                GetGemstoneInfo(gemType, gemId, infoLog);
                                logReturn.addAll(infoLog);
                            } else if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {
                                logReturn.add(OotilityCeption.LogFormat(subcategory, "Incorrect usage. For info: §e/goop mmoitems " + subsonic));
                                logReturn.add("§3Usage: §e" + usage);
                            }
                            break;
                        //endregion
                        default:
                            // I have no memory of that shit
                            if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) logReturn.add(OotilityCeption.LogFormat("MMOItems", "'§3" + args[1] + "§7' is not a valid MMOItems action! do §e/goop mmoitems§7 for the list of actions."));
                            break;
                    }

            } else if (args.length == 1) {
                logReturn.add("§e______________________________________________");
                logReturn.add("§3GooP-MMOItems, §7Related to the third party plugin.");
                logReturn.add("§3Usage: §e/goop mmoitems {action}");
                logReturn.add("§3 - §e{action} §7What actions to perform:");
                logReturn.add("§3 --> §eaddGemSlot§7,§e countGems§7,§e getTier§7,§e setTier");
                logReturn.add("§3 --> §estat§7,§e regenerate§7,§e upgrade§7,§e modifier");
                logReturn.add("§3 --> §efixStacks [player]");
                logReturn.add("§3      * §7Updates MMOItems to the newest format, so that they stack.");
                logReturn.add("§3 --> §egemstoneExtract§7,§e gemstoneSwap§7,§e gemstoneMigrate");
                logReturn.add("§3      * §7Extract gemstones from items, swap them, or migrate IDs.");
                logReturn.add("§3 --> §egemstoneList <type>§7,§e gemstoneInfo <type> <id>");
                logReturn.add("§3      * §7List gemstones or get info about a specific gemstone.");
                if (Gunging_Ootilities_Plugin.usingMMOItemShrubs) {
                    logReturn.add("§3 --> §enewShrub <type> [w] [x] [y] [z]");
                    logReturn.add("§3      * §7Creates a new shrub of type <type>");
                    logReturn.add("§3 --> §elistShrubTypes");
                    logReturn.add("§3      * §7Lists the loaded shrub types");
                    logReturn.add("§3 --> §ereloadShrubTypes");
                    logReturn.add("§3      * §7Reloads shrub types config");
                }
                logReturn.add("§3 - §e<slot> §7Target slot in player's inventory.");
                logReturn.add("§3 --> §7Possible slots: §bhead§3, §bchest§3, §blegs§3, §bfeet§3, §bmainhand§3, §boffhand§3, and any number §b0§3-§b35§3.");

            } else {
                if (!Gunging_Ootilities_Plugin.blockImportantErrorFeedback) {
                    logReturn.add(OotilityCeption.LogFormat("MMOItems", "Incorrect usage. For info: §e/goop mmoitems"));
                    logReturn.add("§3Usage: §e/goop mmoitems {action}");
                }
            }

        // No perms
        } else {

            // Tell him lmao
            logReturn.add(OotilityCeption.LogFormat("§cYou don't have permission to use mmoitems-related commands!"));
        }

        //Set Log Return Urn Value
        logReturnUrn.SetValue(logReturn);
    }
}
}