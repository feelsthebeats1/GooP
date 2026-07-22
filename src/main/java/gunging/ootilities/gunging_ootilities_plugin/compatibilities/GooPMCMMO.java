package gunging.ootilities.gunging_ootilities_plugin.compatibilities;

import com.gmail.nossr50.config.AdvancedConfig;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.datatypes.skills.SubSkillType;
import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.skills.SkillManager;
import com.gmail.nossr50.skills.archery.ArcheryManager;
import com.gmail.nossr50.skills.axes.AxesManager;
import com.gmail.nossr50.skills.taming.TamingManager;
import com.gmail.nossr50.skills.unarmed.UnarmedManager;
import com.gmail.nossr50.util.player.UserManager;
import com.gmail.nossr50.util.skills.PerksUtils;
import gunging.ootilities.gunging_ootilities_plugin.Gunging_Ootilities_Plugin;
import gunging.ootilities.gunging_ootilities_plugin.compatibilities.versions.GooPMCMMO_StatType;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

import java.util.Random;

public class GooPMCMMO {

    static Random random = new Random();
    static AdvancedConfig advancedConfig = null;

    /**
     * Gets the AdvancedConfig instance from mcMMO plugin.
     */
    static AdvancedConfig getAdvancedConfig() {
        if (advancedConfig == null) {
            Plugin mcMMOPlugin = Bukkit.getPluginManager().getPlugin("mcMMO");
            if (mcMMOPlugin instanceof mcMMO) {
                advancedConfig = ((mcMMO) mcMMOPlugin).getAdvancedConfig();
            }
        }
        return advancedConfig;
    }

    public void CompatibilityCheck() {
        // Verify mcMMO is available
        AdvancedConfig config = getAdvancedConfig();
        if (config != null) {
            config.getDazeBonusDamage();
        }
    }

    // For the dual-cummulativity of some stats
    public static Double MCMMODoubleStat(Player target, String arg) {

        // A value to return
        Double result = null;

        // MMOCore takes precedence (since it totals everything)
        if (Gunging_Ootilities_Plugin.foundMCMMO) {

            // Value from MMOCore
            result = GetMCMMODoubleStat(target, arg);

        } else {

            // Nothing
            result = 0.0;
        }

        // Final
        return result;
    }

    public static double GetMCMMODoubleStat(Player target, String statname, double base) {
        Double ret = GetMCMMODoubleStat(target, statname);
        if (ret == null) { ret = base; } else { ret += base; }
        return ret;
    }

    public static Double GetMCMMODoubleStat(Player target, String statname) {

        // NO NULL
        if (target == null) { return null; }

        // Get Statt
        switch (statname.toLowerCase()) {
            case "unarmed.iron_fist":
            case "unarmed.ironfist":
            case "unarmed.ironarm":
            case "unarmed.iron_arm":
                return GetMCMMODoubleStat(target, GooPMCMMO_StatType.UNARMED_IRONFIST);
            case "unarmed.berserk":
                return GetMCMMODoubleStat(target, GooPMCMMO_StatType.UNARMED_BERSERK);
            case "archery.skillshot":
                return GetMCMMODoubleStat(target, GooPMCMMO_StatType.ARCHERY_SKILLSHOT);
            case "archery.daze":
                return GetMCMMODoubleStat(target, GooPMCMMO_StatType.ARCHERY_DAZE);
            case "axes.mastery":
                return GetMCMMODoubleStat(target, GooPMCMMO_StatType.AXES_MASTERY);
            case "axes.crit.pvp":
            case "axes.crit":
                return GetMCMMODoubleStat(target, GooPMCMMO_StatType.AXES_CRIT_PVP);
            case "axes.crit.pve":
                return GetMCMMODoubleStat(target, GooPMCMMO_StatType.AXES_CRIT_PVE);
            case "taming.sharp_claws":
            case "taming.sharpened_claws":
            case "taming.claws":
                return GetMCMMODoubleStat(target, GooPMCMMO_StatType.TAMING_CLAWS);
            case "taming.gore":
                return GetMCMMODoubleStat(target, GooPMCMMO_StatType.TAMING_GORE);
            default:
                return null;
        }
    }

    public static Double GetMCMMODoubleStat(Player target, GooPMCMMO_StatType stat) {

        // NO NULL
        if (target == null) { return null; }

        // Processed Final Multiplier
        Double processedDamage = null;

        // GEt Managers
        McMMOPlayer agressor_McMMO = UserManager.getPlayer(target);
        ArcheryManager archery_ofAgressor = agressor_McMMO.getArcheryManager();
        AxesManager axes_ofAgressor = agressor_McMMO.getAxesManager();
        UnarmedManager unarmed_ofAgressor = agressor_McMMO.getUnarmedManager();
        TamingManager taming_ofAgressor = agressor_McMMO.getTamingManager();

        // Must look at each one damn separately lmao
        switch (stat) {
            case ARCHERY_DAZE:

                // Roll for daze
                if (skillActivationChance(SubSkillType.ARCHERY_DAZE, archery_ofAgressor, agressor_McMMO, PrimarySkillType.ARCHERY)) {

                    // Successful roll
                    processedDamage = getAdvancedConfig().getDazeBonusDamage();

                } else {

                    // Neuter Additve
                    processedDamage = 0.0;
                }

                break;
            case ARCHERY_SKILLSHOT:

                // Process the shit out of the double stats
                if (archery_ofAgressor.canSkillShot()) {

                    // Skill Shot Modified Damage
                    processedDamage = archery_ofAgressor.skillShot(1.0);

                } else {

                    // Neuter Additve
                    processedDamage = 0.0;
                }
                break;
                //endregion
            //region Axes
            case AXES_MASTERY:

                // Can it use axe master?
                if (axes_ofAgressor.canUseAxeMastery()) {

                    // Axe mastery
                    processedDamage = axes_ofAgressor.axeMastery();

                } else {

                    // Neuter Additive
                    processedDamage = 0.0;
                }
                break;
            case AXES_CRIT_PVE:

                // Roll for crit
                if (skillActivationChance(SubSkillType.AXES_CRITICAL_STRIKES, axes_ofAgressor, agressor_McMMO, PrimarySkillType.AXES)) {

                    // Successful roll
                    processedDamage = getAdvancedConfig().getCriticalStrikesPVEModifier();

                } else {

                    // Neuter Additve
                    processedDamage = 0.0;
                }

                break;
            case AXES_CRIT_PVP:

                // Roll for crit
                if (skillActivationChance(SubSkillType.AXES_CRITICAL_STRIKES, axes_ofAgressor, agressor_McMMO, PrimarySkillType.AXES)) {

                    // Successful roll
                    processedDamage = getAdvancedConfig().getCriticalStrikesPVPModifier();

                } else {

                    // Neuter Additve
                    processedDamage = 0.0;
                }

                break;
                //endregion
            //region Unarmed
            case UNARMED_IRONFIST:

                // Process the shit out of the double stats
                if (unarmed_ofAgressor.canUseSteelArm()) {

                    // Skill Shot Modified Damage
                    processedDamage = unarmed_ofAgressor.calculateSteelArmStyleDamage();

                } else {

                    // Neuter Additve
                    processedDamage = 0.0;
                }
                break;
            case UNARMED_BERSERK:

                // Process the shit out of the double stats
                if (unarmed_ofAgressor.canUseBerserk()) {

                    // Skill Shot Modified Damage
                    processedDamage = unarmed_ofAgressor.berserkDamage(1.0);

                } else {

                    // Neuter Additve
                    processedDamage = 0.0;
                }
                break;
            //endregion
            //region Taming
            case TAMING_GORE:

                // Roll for daze
                if (skillActivationChance(SubSkillType.TAMING_GORE, archery_ofAgressor, agressor_McMMO, PrimarySkillType.TAMING)) {

                    // Successful roll
                    processedDamage = getAdvancedConfig().getGoreModifier();

                } else {

                    // Neuter Additve
                    processedDamage = 0.0;
                }

                break;
            case TAMING_CLAWS:

                // Process the shit out of the double stats
                if (taming_ofAgressor.canUseSharpenedClaws()) {

                    // Skill Shot Modified Damage
                    processedDamage = taming_ofAgressor.sharpenedClaws();

                } else {

                    // Neuter Additve
                    processedDamage = 0.0;
                }
                break;
            //endregion
            default:
                processedDamage = null;
                break;
            //region Archery
        }


        // Return Result
        return processedDamage;
    }

    public static boolean skillActivationChance(SubSkillType ability, SkillManager skillManager, McMMOPlayer mcMMOPlayer, PrimarySkillType parent) {

        // Luck perk bonus
        int luck = PerksUtils.handleLuckyPerks(skillManager.getPlayer(), parent);

        // Scale activation chance with skill level
        int skillLevel = skillManager.getSkillLevel();
        double activationChance = Math.min(0.5, skillLevel * 0.001 + luck * 0.01);

        return random.nextDouble() < activationChance;
    }
}
