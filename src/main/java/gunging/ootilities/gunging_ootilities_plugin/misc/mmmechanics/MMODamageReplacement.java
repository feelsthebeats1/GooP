package gunging.ootilities.gunging_ootilities_plugin.misc.mmmechanics;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.mechanics.CustomMechanic;
import io.lumine.mythic.core.skills.mechanics.DamageMechanic;

public class MMODamageReplacement extends DamageMechanic {

    public MMODamageReplacement(CustomMechanic manager, String line, MythicLineConfig mlc) {
        super(manager.getManager(), manager.getFile(), line, mlc);
        construct();
    }
    public MMODamageReplacement(SkillExecutor manager, String line, MythicLineConfig mlc) {
        super(manager, null, line, mlc);
        construct();
    }

    void construct() {
        String typesString = config.getString(new String[]{"type", "t"}, null);
        if (typesString == null || MythicBukkit.isVolatile()) {
            this.element = PlaceholderString.of(typesString);
        }
    }
}
