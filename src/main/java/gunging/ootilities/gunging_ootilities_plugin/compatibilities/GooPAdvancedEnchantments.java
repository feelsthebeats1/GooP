package gunging.ootilities.gunging_ootilities_plugin.compatibilities;

import net.advancedplugins.ae.api.AEAPI;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Compatibility hook for the AdvancedEnchantments plugin (net.advancedplugins.ae).
 *
 * <p>All methods are null/class-load safe: if AdvancedEnchantments is not
 * installed on the server, every operation simply returns a graceful
 * fallback instead of crashing the plugin.</p>
 */
@SuppressWarnings("unused")
public class GooPAdvancedEnchantments {

    /**
     * Whether the AE API is loadable at runtime.
     * Cached after the first check to avoid expensive Class lookups.
     */
    @Nullable private static Boolean aeCompatibility = null;

    /**
     * @return true if AdvancedEnchantments is installed and its API can be loaded.
     */
    public static boolean isAvailable() {
        if (aeCompatibility != null) { return aeCompatibility; }

        try {
            Class.forName("net.advancedplugins.ae.api.AEAPI");
            aeCompatibility = true;
        } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
            aeCompatibility = false;
        }

        return aeCompatibility;
    }

    /**
     * Reads the Advanced Enchantments stored on an item.
     *
     * @param item The item to inspect.
     * @return A map of AE enchantment names to their levels, or {@code null}
     *         if AE is not installed or the item has no AE enchantments.
     */
    @Nullable
    public static Map<String, Integer> getEnchantments(@NotNull ItemStack item) {
        if (!isAvailable()) { return null; }

        try {
            Map<String, Integer> ret = AEAPI.getEnchantmentsOnItem(item);
            return (ret == null || ret.isEmpty()) ? null : ret;
        } catch (Throwable ignored) {
            return null;
        }
    }

    /**
     * Re-applies the given Advanced Enchantments onto an item.
     * <p>
     * Used by the {@code /goop mmoitems regenerate} command to make sure
     * AE enchantments survive the regeneration process.
     * </p>
     *
     * @param item The regenerated item.
     * @param enchantments The AE enchantments previously read from the original item.
     * @return The item with the AE enchantments restored (unchanged if AE is missing).
     */
    @NotNull
    public static ItemStack reapplyEnchantments(@NotNull ItemStack item, @Nullable Map<String, Integer> enchantments) {
        if (enchantments == null || enchantments.isEmpty()) { return item; }
        if (!isAvailable()) { return item; }

        try {
            ItemStack result = item;
            for (Map.Entry<String, Integer> entry : enchantments.entrySet()) {
                result = AEAPI.applyEnchant(entry.getKey(), entry.getValue(), result);
            }
            return AEAPI.organizeEnchants(result);
        } catch (Throwable ignored) {
            return item;
        }
    }
}