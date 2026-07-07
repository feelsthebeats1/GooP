package gunging.ootilities.gunging_ootilities_plugin.compatibilities;

import gunging.ootilities.gunging_ootilities_plugin.Gunging_Ootilities_Plugin;
import gunging.ootilities.gunging_ootilities_plugin.compatibilities.versions.GooPVersionPotionEffects;
import gunging.ootilities.gunging_ootilities_plugin.compatibilities.versions.GooP_MinecraftVersions;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Method;

public class GooPGraveyards {

    public GooPGraveyards() { }

    public void CompatibilityCheck(){
        // Runtime check - Graveyards API may vary by version
        try {
            Class<?> graveyardClass = Class.forName("io.lumine.mythicgraveyards.respawning.Graveyard");
            Method getMethod = graveyardClass.getMethod("get", String.class);
            Object reckon = getMethod.invoke(null, "No");
            if (reckon != null) {
                try {
                    Method getDiscoverRange = reckon.getClass().getMethod("getDiscoverRange");
                    getDiscoverRange.invoke(reckon);
                } catch (NoSuchMethodException ignored) { }
            }
        } catch (Exception ignored) { }
    }

    public static void SimulateGraveyardsRespawn(Player gPlayer) {

        try {
            // Try the new API package first (MythicGraveyards 5.x)
            Class<?> graveyardClass = Class.forName("io.lumine.mythicgraveyards.respawning.Graveyard");
            Method getNearest = graveyardClass.getMethod("getNearestGraveyard", Player.class);
            Object gGrave = getNearest.invoke(null, gPlayer);

            if (gGrave != null) {
                Method getImmunityTicks = gGrave.getClass().getMethod("getImmunityTicks");
                Object immunityTicks = getImmunityTicks.invoke(gGrave);

                if (immunityTicks instanceof Number && ((Number) immunityTicks).intValue() > 0) {
                    gPlayer.addPotionEffect(new PotionEffect(
                            GooP_MinecraftVersions.GetVersionPotionEffect(GooPVersionPotionEffects.DAMAGE_RESISTANCE),
                            ((Number) immunityTicks).intValue(),
                            100
                    ));
                }

                // Check for mythic skill
                if (Gunging_Ootilities_Plugin.foundMythicMobs) {
                    try {
                        Method getMythicSkill = gGrave.getClass().getMethod("getMythicSkill");
                        Object mythicSkillOptional = getMythicSkill.invoke(gGrave);
                        if (mythicSkillOptional != null) {
                            Method isPresent = mythicSkillOptional.getClass().getMethod("isPresent");
                            if ((boolean) isPresent.invoke(mythicSkillOptional)) {
                                Method get = mythicSkillOptional.getClass().getMethod("get");
                                Object skill = get.invoke(mythicSkillOptional);
                                GooPMythicMobs.GraveyardsRespawnSkill(String.valueOf(skill), gPlayer.getLocation(), gPlayer);
                            }
                        }
                    } catch (NoSuchMethodException ignored) { }
                }

                // Teleport to graveyard spawn
                try {
                    Method getSpawnLocation = gGrave.getClass().getMethod("getSpawnLocation");
                    Object spawnLocation = getSpawnLocation.invoke(gGrave);
                    if (spawnLocation instanceof org.bukkit.Location) {
                        gPlayer.teleport((org.bukkit.Location) spawnLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
                    }
                } catch (NoSuchMethodException ignored) { }
            }
        } catch (Exception ignored) {
            // Graveyards not available or API mismatch - silently skip
        }
    }
}
