package com.archyx.aureliumskills.stats;

import com.archyx.aureliumskills.AureliumSkills;
import com.archyx.aureliumskills.Options;
import com.archyx.aureliumskills.Setting;
import com.archyx.aureliumskills.skills.SkillLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Toughness implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			//Check for disabled world
			if (AureliumSkills.worldManager.isInDisabledWorld(player.getLocation())) {
				return;
			}
			if (SkillLoader.playerStats.containsKey(player.getUniqueId())) {
				double toughness = SkillLoader.playerStats.get(player.getUniqueId()).getStatLevel(Stat.TOUGHNESS) * Options.getDoubleOption(Setting.TOUGHNESS_MODIFIER);
				event.setDamage(event.getDamage() * (1 - (-1.0 * Math.pow(1.01, -1.0 * toughness) + 1)));
			}
		}
	}
}
