package com.archyx.aureliumskills.mana;

import com.archyx.aureliumskills.AureliumSkills;
import com.archyx.aureliumskills.abilities.DefenseAbilities;
import com.archyx.aureliumskills.api.event.ManaRegenerateEvent;
import com.archyx.aureliumskills.configuration.Option;
import com.archyx.aureliumskills.configuration.OptionL;
import com.archyx.aureliumskills.data.PlayerData;
import com.archyx.aureliumskills.stats.Stat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class ManaManager implements Listener {

    private final AureliumSkills plugin;
    private final DefenseAbilities defenseAbilities;

    public ManaManager(AureliumSkills plugin, DefenseAbilities defenseAbilities) {
        this.plugin = plugin;
        this.defenseAbilities = defenseAbilities;
    }

    /**
     * Start regenerating Mana
     */
    public void startRegen() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
                    if (playerData != null) {
                        double originalMana = playerData.getMana();
                        double maxMana = getMaxMana(playerData);
                        if (originalMana < maxMana) {
                            if (!defenseAbilities.getAbsorptionActivated().contains(player)) {
                                double regen = OptionL.getDouble(Option.REGENERATION_BASE_MANA_REGEN) + playerData.getStatLevel(Stat.REGENERATION) * OptionL.getDouble(Option.REGENERATION_MANA_MODIFIER);
                                double finalRegen = Math.min(originalMana + regen, maxMana) - originalMana;
                                ManaRegenerateEvent event = new ManaRegenerateEvent(player, finalRegen);
                                Bukkit.getPluginManager().callEvent(event);
                                if (!event.isCancelled()) {
                                    playerData.setMana(originalMana + event.getAmount());
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public double getMaxMana(PlayerData playerData) {
        if (playerData != null) {
            return OptionL.getDouble(Option.BASE_MANA) + (OptionL.getDouble(Option.WISDOM_MAX_MANA_PER_WISDOM) * playerData.getStatLevel(Stat.WISDOM));
        } else {
            return OptionL.getDouble(Option.BASE_MANA);
        }
    }

}
