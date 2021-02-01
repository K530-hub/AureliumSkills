package com.archyx.aureliumskills.abilities;

import com.archyx.aureliumskills.AureliumSkills;
import com.archyx.aureliumskills.api.event.LootDropCause;
import com.archyx.aureliumskills.api.event.PlayerLootDropEvent;
import com.archyx.aureliumskills.configuration.OptionL;
import com.archyx.aureliumskills.data.PlayerData;
import com.archyx.aureliumskills.lang.Lang;
import com.archyx.aureliumskills.lang.ManaAbilityMessage;
import com.archyx.aureliumskills.loot.Loot;
import com.archyx.aureliumskills.mana.MAbility;
import com.archyx.aureliumskills.mana.ManaAbilityManager;
import com.archyx.aureliumskills.mana.Terraform;
import com.archyx.aureliumskills.skills.Skill;
import com.archyx.aureliumskills.util.LoreUtil;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

public class ExcavationAbilities extends AbilityProvider implements Listener {

	private static final Random r = new Random();
	private final Material[] loadedMaterials;

	public ExcavationAbilities(AureliumSkills plugin) {
		super(plugin, Skill.EXCAVATION);
		//Load materials
		XMaterial[] materials = new XMaterial[]{
				XMaterial.DIRT, XMaterial.GRASS_BLOCK, XMaterial.COARSE_DIRT, XMaterial.PODZOL,
				XMaterial.SAND, XMaterial.RED_SAND, XMaterial.SOUL_SAND, XMaterial.SOUL_SOIL,
				XMaterial.CLAY, XMaterial.GRAVEL, XMaterial.MYCELIUM
		};
		loadedMaterials = new Material[materials.length];
		for (int i = 0; i < loadedMaterials.length; i++) {
			loadedMaterials[i] = materials[i].parseMaterial();
		}
	}

	public void spadeMaster(EntityDamageByEntityEvent event, Player player, PlayerData playerData) {
		if (OptionL.isEnabled(Skill.EXCAVATION)) {
			if (plugin.getAbilityManager().isEnabled(Ability.SPADE_MASTER)) {
				//Check permission
				if (!player.hasPermission("aureliumskills.excavation")) {
					return;
				}
				if (playerData.getAbilityLevel(Ability.SPADE_MASTER) > 0) {
					event.setDamage(event.getDamage() * (1 + (getValue(Ability.SPADE_MASTER, playerData) / 100)));
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void biggerScoop(PlayerData playerData, Block block, Player player) {
		if (isExcavationMaterial(block.getType())) {
			if (r.nextDouble() < (getValue(Ability.BIGGER_SCOOP, playerData) / 100)) {
				ItemStack tool = player.getInventory().getItemInMainHand();
				Material mat =  block.getType();
				for (ItemStack item : block.getDrops(tool)) {
					//If silk touch
					if (tool.getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0) {
						if (mat.equals(XMaterial.GRASS_BLOCK.parseMaterial())) {
							Material grassBlock = XMaterial.GRASS_BLOCK.parseMaterial();
							if (grassBlock != null) {
								PlayerLootDropEvent event = new PlayerLootDropEvent(player, new ItemStack(grassBlock, 2), block.getLocation().add(0.5, 0.5, 0.5), LootDropCause.BIGGER_SCOOP);
								Bukkit.getPluginManager().callEvent(event);
								if (!event.isCancelled()) {
									block.getWorld().dropItem(event.getLocation(), event.getItemStack());
								}
							}
						}
						else if (mat.equals(XMaterial.MYCELIUM.parseMaterial())) {
							Material mycelium = XMaterial.MYCELIUM.parseMaterial();
							if (mycelium != null) {
								PlayerLootDropEvent event = new PlayerLootDropEvent(player, new ItemStack(mycelium, 2), block.getLocation().add(0.5, 0.5, 0.5), LootDropCause.BIGGER_SCOOP);
								Bukkit.getPluginManager().callEvent(event);
								if (!event.isCancelled()) {
									block.getWorld().dropItem(event.getLocation(), event.getItemStack());
								}
							}
						}
						else if (mat.equals(XMaterial.CLAY.parseMaterial())) {
							Material clay = XMaterial.CLAY.parseMaterial();
							if (clay != null) {
								PlayerLootDropEvent event = new PlayerLootDropEvent(player, new ItemStack(clay, 2), block.getLocation().add(0.5, 0.5, 0.5), LootDropCause.BIGGER_SCOOP);
								Bukkit.getPluginManager().callEvent(event);
								if (!event.isCancelled()) {
									block.getWorld().dropItem(event.getLocation(), event.getItemStack());
								}
							}
						}
						if (XMaterial.isNewVersion()) {
							if (mat.equals(XMaterial.PODZOL.parseMaterial())) {
								PlayerLootDropEvent event = new PlayerLootDropEvent(player, new ItemStack(Material.PODZOL, 2), block.getLocation().add(0.5, 0.5, 0.5), LootDropCause.BIGGER_SCOOP);
								Bukkit.getPluginManager().callEvent(event);
								if (!event.isCancelled()) {
									block.getWorld().dropItem(event.getLocation(), event.getItemStack());
								}
							}
						}
						else {
							if (mat.equals(Material.DIRT)) {
								if (block.getData() == 2) {
									PlayerLootDropEvent event = new PlayerLootDropEvent(player, new ItemStack(Material.DIRT, 2), block.getLocation().add(0.5, 0.5, 0.5), LootDropCause.BIGGER_SCOOP);
									Bukkit.getPluginManager().callEvent(event);
									if (!event.isCancelled()) {
										block.getWorld().dropItem(event.getLocation(), event.getItemStack());
									}
								}
							}
						}
					}
					//Drop regular item if not silk touch
					else {
						ItemStack drop = item.clone();
						drop.setAmount(2);
						PlayerLootDropEvent event = new PlayerLootDropEvent(player, drop, block.getLocation().add(0.5, 0.5, 0.5), LootDropCause.BIGGER_SCOOP);
						Bukkit.getPluginManager().callEvent(event);
						if (!event.isCancelled()) {
							block.getWorld().dropItem(event.getLocation(), event.getItemStack());
						}
					}
				}
			}
		}
	}

	public void metalDetector(Player player, PlayerData playerData, Block block) {
		if (isExcavationMaterial(block.getType())) {
			if (r.nextDouble() < (getValue(Ability.METAL_DETECTOR, playerData) / 100)) {
				int lootTableSize = plugin.getLootTableManager().getLootTable("excavation-rare").getLoot().size();
				if (lootTableSize > 0) {
					Loot loot = plugin.getLootTableManager().getLootTable("excavation-rare").getLoot().get(r.nextInt(lootTableSize));
					// If has item
					if (loot.hasItem()) {
						ItemStack drop = loot.getDrop();
						if (drop != null) {
							PlayerLootDropEvent event = new PlayerLootDropEvent(player, drop.clone(), block.getLocation().add(0.5, 0.5, 0.5), LootDropCause.METAL_DETECTOR);
							Bukkit.getPluginManager().callEvent(event);
							if (!event.isCancelled()) {
								block.getWorld().dropItem(event.getLocation(), event.getItemStack());
							}
						}
					}
					// If has command
					else if (loot.hasCommand()) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), LoreUtil.replace(loot.getCommand(), "{player}", player.getName()));
					}
				}
			}
		}
	}

	public void luckySpades(Player player, PlayerData playerData, Block block) {
		if (isExcavationMaterial(block.getType())) {
			if (r.nextDouble() < (getValue(Ability.LUCKY_SPADES, playerData) / 100)) {
				int lootTableSize = plugin.getLootTableManager().getLootTable("excavation-epic").getLoot().size();
				if (lootTableSize > 0) {
					Loot loot = plugin.getLootTableManager().getLootTable("excavation-epic").getLoot().get(r.nextInt(lootTableSize));
					// If has item
					if (loot.hasItem()) {
						ItemStack drop = loot.getDrop();
						if (drop != null) {
							PlayerLootDropEvent event = new PlayerLootDropEvent(player, drop.clone(), block.getLocation().add(0.5, 0.5, 0.5), LootDropCause.LUCKY_SPADES);
							Bukkit.getPluginManager().callEvent(event);
							if (!event.isCancelled()) {
								block.getWorld().dropItem(event.getLocation(), event.getItemStack());
							}
						}
					}
					// If has command
					else if (loot.hasCommand()) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), LoreUtil.replace(loot.getCommand(), "{player}", player.getName()));
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void excavationListener(BlockBreakEvent event) {
		if (OptionL.isEnabled(Skill.EXCAVATION)) {
			if (!event.isCancelled()) {
				Player player = event.getPlayer();
				Block block = event.getBlock();
				if (blockAbility(player)) return;
				//Applies abilities
				PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
				if (playerData == null) return;
				if (plugin.getAbilityManager().isEnabled(MAbility.TERRAFORM)) {
					if (!block.hasMetadata("AureliumSkills-Terraform")) {
						applyTerraform(player, block);
					}
				}
				//Check game mode
				if (!player.getGameMode().equals(GameMode.SURVIVAL)) {
					return;
				}
				if (!block.hasMetadata("skillsPlaced")) {
					if (isEnabled(Ability.BIGGER_SCOOP)) {
						biggerScoop(playerData, block, player);
					}
					if (isEnabled(Ability.METAL_DETECTOR)) {
						metalDetector(player, playerData, block);
					}
					if (isEnabled(Ability.LUCKY_SPADES)) {
						luckySpades(player, playerData, block);
					}
				}
			}
		}
	}

	private boolean isExcavationMaterial(Material material) {
		for (Material checkedMaterial : loadedMaterials) {
			if (material == checkedMaterial) {
				return true;
			}
		}
		return false;
	}

	private void applyTerraform(Player player, Block block) {
		Locale locale = Lang.getLanguage(player);
		ManaAbilityManager manager = plugin.getManaAbilityManager();
		if (!isExcavationMaterial(block.getType())) return;
		// Apply if activated
		if (manager.isActivated(player.getUniqueId(), MAbility.TERRAFORM)) {
			terraformBreak(player, block);
			return;
		}
		//Checks if speed mine is ready
		if (manager.isReady(player.getUniqueId(), MAbility.TERRAFORM)) {
			//Checks if holding pickaxe
			Material mat = player.getInventory().getItemInMainHand().getType();
			if (mat.name().contains("SHOVEL") || mat.name().contains("SPADE")) {
				PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
				if (playerData == null) return;
				if (playerData.getMana() >= getManaCost(MAbility.TERRAFORM, playerData)) {
					manager.activateAbility(player, MAbility.TERRAFORM, (int) (getValue(MAbility.TERRAFORM, playerData) * 20), new Terraform(plugin));
					terraformBreak(player, block);
				}
				else {
					plugin.getAbilityManager().sendMessage(player, Lang.getMessage(ManaAbilityMessage.NOT_ENOUGH_MANA, locale).replace("{mana}", String.valueOf(getManaCost(MAbility.TERRAFORM, playerData))));
				}
			}
		}
	}

	@EventHandler
	private void readyTerraform(PlayerInteractEvent event) {
		plugin.getManaAbilityManager().getActivator().readyAbility(event, Skill.EXCAVATION, new String[] {"SHOVEL", "SPADE"}, Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);
	}

	private void terraformBreak(Player player, Block block) {
		Material material = block.getType();
		BlockFace[] faces = new BlockFace[] {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
		LinkedList<Block> toCheck = new LinkedList<>();
		toCheck.add(block);
		int count = 0;
		while ((block = toCheck.poll()) != null && count < 61) {
			if (block.getType() == material) {
				block.setMetadata("AureliumSkills-Terraform", new FixedMetadataValue(plugin, true));
				breakBlock(player, block);
				for (BlockFace face : faces) {
					toCheck.add(block.getRelative(face));
				}
				count++;
			}
		}
	}

	private void breakBlock(Player player, Block block) {
		BlockBreakEvent event = new BlockBreakEvent(block, player);
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			block.breakNaturally(player.getInventory().getItemInMainHand());
		}
		block.removeMetadata("AureliumSkills-Terraform", plugin);
	}

}
