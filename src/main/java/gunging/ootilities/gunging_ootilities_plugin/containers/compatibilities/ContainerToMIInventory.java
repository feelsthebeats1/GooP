package gunging.ootilities.gunging_ootilities_plugin.containers.compatibilities;

import gunging.ootilities.gunging_ootilities_plugin.Gunging_Ootilities_Plugin;
import gunging.ootilities.gunging_ootilities_plugin.OotilityCeption;
import gunging.ootilities.gunging_ootilities_plugin.compatibilities.GooPMMOItems;
import gunging.ootilities.gunging_ootilities_plugin.compatibilities.GooPMythicMobs;
import gunging.ootilities.gunging_ootilities_plugin.containers.GOOPCPersonal;
import gunging.ootilities.gunging_ootilities_plugin.containers.loader.GCL_Personal;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.api.player.inventory.EquippedItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Equips the items stored in storage slots to the player.
 * Adapted for MMOItems 6.10.1+ API.
 */
public class ContainerToMIInventory {

    private static io.lumine.mythic.lib.api.player.EquipmentSlot anyEqSlot = null;

    @NotNull private static io.lumine.mythic.lib.api.player.EquipmentSlot getAnyEqSlot() {
        if (anyEqSlot != null) { return anyEqSlot; }
        try { anyEqSlot = io.lumine.mythic.lib.api.player.EquipmentSlot.valueOf("ANY"); }
        catch (IllegalArgumentException ignored) { anyEqSlot = io.lumine.mythic.lib.api.player.EquipmentSlot.valueOf("OTHER"); }
        return anyEqSlot;
    }

    /**
     * Called by GooPMMOItems to get all equipped items from GooP containers for a player.
     */
    @NotNull public List<ContainersEquippedItem> getInventory(@NotNull Player player) {
        ArrayList<ContainersEquippedItem> ret = new ArrayList<>();
        ArrayList<String> duplicateIDs = new ArrayList<>();

        for (GOOPCPersonal p : GCL_Personal.getLoaded()) {
            if (p.getTemplate().getEquipmentSlots().size() <= 0) { continue; }
            p.registerInventoryFor(player.getUniqueId());

            for (Map.Entry<Integer, ItemStack> itm : p.indexedItemsForEquipment(player).entrySet()) {
                if (OotilityCeption.IsAirNullAllowed(itm.getValue())) { continue; }

                String id = GooPMMOItems.GetMMOItemID(itm.getValue(), null);
                if (id == null) {
                    ret.add(new ContainersEquippedItem(p, player, itm.getKey(), itm.getValue(), getAnyEqSlot()));
                    continue;
                }
                if (!p.getTemplate().isAllowDuplicateEquipment() && duplicateIDs.contains(id)) { continue; }
                ret.add(new ContainersEquippedItem(p, player, itm.getKey(), itm.getValue(), getAnyEqSlot()));
                duplicateIDs.add(id);
            }
        }
        return ret;
    }
}

/**
 * MMOItems 6.10.1 compatible EquippedItem.
 */
class ContainersEquippedItem extends EquippedItem {

    @NotNull GOOPCPersonal container;
    @NotNull Player player;
    int index;

    public ContainersEquippedItem(@NotNull GOOPCPersonal container, @NotNull Player player, int index,
                                  @NotNull ItemStack itemStack, @NotNull io.lumine.mythic.lib.api.player.EquipmentSlot slot) {
        super(new net.Indyuce.mmoitems.inventory.EquippedItem(index, slot, 0, NBTItem.get(itemStack)));
        this.container = container;
        this.player = player;
        this.index = index;
        if (Gunging_Ootilities_Plugin.foundMythicMobs) { GooPMythicMobs.newenOlden = true; }
    }

    @Override
    public void setItem(@Nullable ItemStack itemStack) {
        container.setAndSaveOwnerItem(player.getUniqueId(), index, itemStack, false);
    }
}
