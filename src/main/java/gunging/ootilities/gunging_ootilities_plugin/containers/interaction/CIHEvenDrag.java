package gunging.ootilities.gunging_ootilities_plugin.containers.interaction;


import gunging.ootilities.gunging_ootilities_plugin.Gunging_Ootilities_Plugin;
import gunging.ootilities.gunging_ootilities_plugin.OotilityCeption;
import gunging.ootilities.gunging_ootilities_plugin.containers.GOOPCDeployed;
import gunging.ootilities.gunging_ootilities_plugin.containers.GOOPCManager;
import gunging.ootilities.gunging_ootilities_plugin.containers.GOOPCSlot;
import gunging.ootilities.gunging_ootilities_plugin.containers.GOOPCTemplate;
import gunging.ootilities.gunging_ootilities_plugin.containers.inventory.ContainerInventory;
import gunging.ootilities.gunging_ootilities_plugin.containers.player.GOOPCPlayer;
import gunging.ootilities.gunging_ootilities_plugin.misc.RefSimulator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CIHEvenDrag extends ContainersDragHandler {

    /**
     * For the use of {@link DragType#EVEN}
     */
    public CIHEvenDrag() { super(DragType.EVEN); }

    /**
     * For the use of {@link DragType#SINGLE}
     */
    CIHEvenDrag(DragType alt) { super(alt); }

    /**
     * @param raw Raw slot number, from 0 to topSize+36
     *
     * @param topSize Size of the top inventory
     *
     * @return Correct raw slot for use with {@link InventoryView#getBottomInventory()} or {@link InventoryView#getTopInventory()}
     */
    public int rawToPlayerInventoryRaw(int raw, int topSize) {
        //CLI// OotilityCeption.Log("§8CIH §cI§7 Adjusting §3" + raw + "§7 among§b " + topSize);

        // No adjustment
        if (raw < topSize) { return raw; }

        // Adjust for bottom inventory
        int mRaw = raw - topSize;

        // First 27 inventory slots
        if (mRaw < 27) { return mRaw + 9 + topSize; }

        // Last 9 inventory slots
        return mRaw - 27 + topSize;
    }

    @Override
    @Nullable
    public ContainersInteractionResult handleContainersOperation(@Nullable GOOPCDeployed deployed, @Nullable ContainerInventory observed, @Nullable GOOPCPlayer rpg, @NotNull Player player, @NotNull InventoryDragEvent event) {

        // Always cancelled, the action is taken over
        event.setCancelled(true);

        // Preview mode? No more actions.
        if (GOOPCManager.isUsage_Preview(event.getView())) {
            //CLI//OotilityCeption.Log("§8CIH §8EVD§e -§7 Preview Mode");

            // Cancel again if any slot is in the container ~ preview mode prevents this
            for (int i : event.getRawSlots()) { if (i < event.getView().getTopInventory().getSize()) { return null; } }
        }

        // Explore contents of top
        //CLX// OotilityCeption.Log("§8CIH §e ----------------------------------------------");
        //CLX//for (int i = 0; i < event.getView().getTopInventory().getSize(); i++) { OotilityCeption.Log("§8CIH§d TOP#" + i + "§7 Found " + OotilityCeption.GetItemName(event.getView().getTopInventory().getItem(i), true)); }
        //CLX//for (int i = 0; i < event.getView().getBottomInventory().getSize(); i++) { OotilityCeption.Log("§8CIH§6 BOT#" + i + "§e#R" + (i + event.getView().getTopInventory().getSize()) + "§7 Found " + OotilityCeption.GetItemName(event.getView().getBottomInventory().getItem(i), true)); }
        //CLX//for (int i : event.getRawSlots()) { OotilityCeption.Log("§8CIH§9 RAW#" + rawToPlayerInventoryRaw(i, event.getView().getTopInventory().getSize()) + "§7 Found " + OotilityCeption.GetItemName((rawToPlayerInventoryRaw(i, event.getView().getTopInventory().getSize()) >= event.getView().getTopInventory().getSize()) ? event.getView().getBottomInventory().getItem(rawToPlayerInventoryRaw(i, event.getView().getTopInventory().getSize()) - event.getView().getTopInventory().getSize()) : event.getView().getTopInventory().getItem(rawToPlayerInventoryRaw(i, event.getView().getTopInventory().getSize())), true)); }
        //CLX//for (Map.Entry<Integer, ItemStack> newItm : event.getNewItems().entrySet()) { OotilityCeption.Log("§8CIH§c NEW#R" + rawToPlayerInventoryRaw(newItm.getKey(), event.getView().getTopInventory().getSize()) + "§7 Found " + OotilityCeption.GetItemName(newItm.getValue(), true)); }
        //CLX// OotilityCeption.Log("§8CIH §e ----------------------------------------------");

        ItemStack finalCursor = event.getCursor();
        ItemStack originalCursor = event.getOldCursor().clone();
        //CLI// OotilityCeption.Log("§8CIH §8EVD§3 -->§7 Start Cursor:§f " + OotilityCeption.GetItemName(originalCursor, true));
        //CLI// OotilityCeption.Log("§8CIH §8EVD§3 -->§7 Final Cursor:§f " + OotilityCeption.GetItemName(finalCursor, true));

        // Please let us use zero as final then
        if (finalCursor == null) { finalCursor = OotilityCeption.asQuantity(originalCursor, 0); }

        // Identify templates to be used
        @Nullable GOOPCTemplate containerTemplate = null;
        if (deployed != null && observed != null) { containerTemplate = deployed.getTemplate(); }
        @Nullable GOOPCTemplate rpgTemplate = null;
        if (rpg != null) { rpgTemplate = rpg.getTemplate(); }

        boolean crafting = (event.getView().getType() == InventoryType.CRAFTING);

        // Go through all affected slots
        ContainersInteractionResult result = new ContainersInteractionResult(null, CIRClickType.CLICK);
        for (Integer uglyContinuousRawSlot : event.getRawSlots()) {

            // Find item
            ItemStack newItem = event.getNewItems().get(uglyContinuousRawSlot);
            if (newItem == null) {
                //CLI// OotilityCeption.Log("§8CIH §8EVD§7 Continuous Slot #" + uglyContinuousRawSlot + "§c null new item ~ skipped. ");
                continue; }

            int trueSlot;
            boolean inBottomInventory;

            // Exceptions
            if (crafting) {

                // Crafting inventory counts as bottom inventory always
                inBottomInventory = true;
                int trueRawSlot;

                // Which slot is it
                trueSlot = switch (uglyContinuousRawSlot) {

                    // Offhand
                    case 45 -> -106;

                    // Armor Slots
                    case 8 -> 100;
                    case 7 -> 101;
                    case 6 -> 102;
                    case 5 -> 103;

                    // Crafting Slots
                    case 4 -> 83;
                    case 3 -> 82;
                    case 2 -> 81;
                    case 1 -> 80;
                    case 0 -> 84;

                    // Bulk inventory slots
                    default -> {

                        /*
                         * The armor slots insert four slots before the actual inventory comes in,
                         * but are not counted in the top inventory, so this four slots offset fixes
                         * the four slots offset introduced by armor slots.
                         *
                         * This will give you the slot numbers identical to the way the player inventory
                         * is laid out, but offset by 5 slots (the crafting "top inventory" size. Thus,
                         * subtracting the crafting slots gives you their true slot number.
                         */
                        trueRawSlot = rawToPlayerInventoryRaw(uglyContinuousRawSlot - 4, event.getView().getTopInventory().getSize());

                        // Subtract crafting inventory size
                        yield trueRawSlot - event.getView().getTopInventory().getSize();
                    }
                };

                //CLI// OotilityCeption.Log("§8CIH §8EVD§3 ----------------------- Drag Raw #" + uglyContinuousRawSlot + "§2 True Slot " + trueSlot + "§c True Raw" + trueRawSlot + " §3:§7 Crafting View");
            } else {

                /*
                 * This solely flips the hotbar slots to come before the bottom inventory bulk slots
                 * but keeping it in RAW slot format, so it is known to which inventory it belongs.
                 *
                 * The baked I will subtract the size of the top inventory if it took place in the
                 * bottom inventory, to give the slot number relative to the bottom inventory.
                 */
                int trueRawSlot = rawToPlayerInventoryRaw(uglyContinuousRawSlot, event.getView().getTopInventory().getSize());

                // Identify which
                inBottomInventory = (trueRawSlot >= event.getView().getTopInventory().getSize());
                trueSlot = inBottomInventory ? trueRawSlot - event.getView().getTopInventory().getSize() : trueRawSlot;

                //CLI// OotilityCeption.Log("§8CIH §8EVD§3 ----------------------- Drag Raw #" + uglyContinuousRawSlot + "§2 True Slot " + trueSlot + "§c True Raw" + trueRawSlot + " §3:§7 Non-Crafting View");
            }

            /*
             * The following things are now known:
             *
             * This raw slot is where some dragged items are being attempted to be stacked onto.
             * The item currently in that slot (its amount)
             * The item now being placed there (its amount)
             */

            // What are the properties of the dragged slot of interest?
            @Nullable GOOPCSlot usedSlot = inBottomInventory ?

                    // If the bottom inventory clicked, we are interested in the RPG Inventory Slot
                    (rpgTemplate != null ? rpgTemplate.getSlotAt(trueSlot) : null) :

                    // If the top inventory clicked, we are interested in the Container slot.
                    (containerTemplate != null ? containerTemplate.getSlotAt(trueSlot) : null);

            // Get interacting slot
            CIInteractingSlot interactingSlot = ContainersClickHandler.knownTarget(inBottomInventory ? rpg : deployed, usedSlot, trueSlot, inBottomInventory ? event.getView().getBottomInventory() : event.getView().getTopInventory(), event.getView(), observed);

            // If they don't stack, skip this slot
            if (interactingSlot == null) {

                // That's not good
                Gunging_Ootilities_Plugin.theOots.CLog("§7Null slot§c #" + trueSlot + "§7 for container §c" + (inBottomInventory ? rpg.getTemplate().getInternalName() : deployed.getTemplate().getInternalName()) + "§7, please rebuild its layout with §e/goop containers config contents§7. ");
                continue; }

            // Find current
            ItemStack currentItem = interactingSlot.getCurrentItem();
            if (OotilityCeption.IsAirNullAllowed(currentItem)) { currentItem = OotilityCeption.asQuantity(newItem, 0);}

            //CLI// OotilityCeption.Log("§8CIH §8EVD§3 -->§7 Current Slot Item:§f " + OotilityCeption.GetItemName(currentItem, true));
            //CLI// OotilityCeption.Log("§8CIH §8EVD§3 -->§7 New Slot Item:§f " + OotilityCeption.GetItemName(newItem, true));

            // If they don't stack, skip this slot
            if (!originalCursor.isSimilar(currentItem)) {
                //CLI// OotilityCeption.Log("§8CIH §8EVD§7 The item in there is not similar,§c skipped: " + OotilityCeption.GetItemName(currentItem, true));
                continue; }

            // Identifying
            RefSimulator<CIInteractingFailReason> interactionFail = new RefSimulator<>(null);

            // Interact
            boolean couldInteract = ContainersClickHandler.canInteract(interactingSlot, player, currentItem, interactionFail);
            boolean isDefault = interactionFail.getValue() == CIInteractingFailReason.DEFAULT_ITEM;


            // Any other forms of failed interaction cause no result.
            if (!couldInteract) {

                /*
                 * Storing items on default items is indeed allowed
                 */
                if (!(isDefault && usedSlot != null && usedSlot.isForStorage())) {

                    // Return to the cursor the amount of items that would have had been added to this slot. Cancels changes to this slot
                    finalCursor.setAmount(finalCursor.getAmount() + newItem.getAmount() - currentItem.getAmount());
                    continue;
                }
            }

            // Set item, was able to interact
            interactingSlot.setItem(newItem);
            //CLI// OotilityCeption.Log("§8CIH §8EVD§a +§7 Storing in #" + uglyContinuousRawSlot + ": " + OotilityCeption.GetItemName(newItem, true));

            // Null? That's good enough
            if (usedSlot == null) { continue; }

            /*
             * The item was successfully put into a GooPCSlot
             */
            result.addInteraction((CIContainerInteracting) interactingSlot, CIRClickType.STORE);
        }

        // Finally
        //CLI// OotilityCeption.Log("§8CIH §8EVD§a +§7 Cursor Final " + OotilityCeption.GetItemName(finalCursor, true));
        result.setCursorUpdate(finalCursor);
        return result;
    }
}
