package com.archyx.aureliumskills.listeners;

import com.archyx.aureliumskills.AureliumSkills;
import com.archyx.aureliumskills.data.PlayerData;
import com.archyx.aureliumskills.data.PlayerManager;
import com.archyx.aureliumskills.lang.Lang;
import com.archyx.aureliumskills.skills.PlayerSkillInstance;
import com.archyx.aureliumskills.util.UpdateChecker;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Locale;

public class PlayerJoinQuit implements Listener {

	private final AureliumSkills plugin;

	public PlayerJoinQuit(AureliumSkills plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerManager playerManager = plugin.getPlayerManager();
		if (playerManager.getPlayerData(player) == null) {
			plugin.getStorageProvider().load(player);
			PlayerData playerData = playerManager.getPlayerData(player);
			if (playerData != null) {
				plugin.getLeaderboard().queueAdd(new PlayerSkillInstance(playerData));
			}
		}
		// Load player skull
		Location playerLoc = player.getLocation();
		Location loc = new Location(playerLoc.getWorld(), playerLoc.getX(), 0, playerLoc.getZ());
		Block b = loc.getBlock();
		BlockState state = b.getState();
		SkullCreator.blockWithUuid(b, player.getUniqueId());
		state.update(true);
		// Update message
		if (player.isOp()) {
			if (System.currentTimeMillis() > plugin.getReleaseTime() + 21600000L) {
				// Check for updates
				new UpdateChecker(plugin, 81069).getVersion(version -> {
					if (!plugin.getDescription().getVersion().contains("Pre-Release")) {
						if (!plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
							Locale locale = Lang.getLanguage(player);
							player.sendMessage(AureliumSkills.getPrefix(locale) + ChatColor.WHITE + "New update available! You are on version " + ChatColor.AQUA + plugin.getDescription().getVersion() + ChatColor.WHITE + ", latest version is " + ChatColor.AQUA + version);
							player.sendMessage(AureliumSkills.getPrefix(locale) + ChatColor.WHITE + "Download it on Spigot: " + ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "http://spigotmc.org/resources/81069");
						}
					}
				});
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		plugin.getStorageProvider().save(player);
		plugin.getActionBar().resetActionBar(player);
	}

}
