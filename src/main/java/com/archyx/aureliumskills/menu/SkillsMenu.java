package com.archyx.aureliumskills.menu;

import com.archyx.aureliumskills.AureliumSkills;
import com.archyx.aureliumskills.configuration.OptionL;
import com.archyx.aureliumskills.data.PlayerData;
import com.archyx.aureliumskills.lang.CommandMessage;
import com.archyx.aureliumskills.lang.Lang;
import com.archyx.aureliumskills.lang.MenuMessage;
import com.archyx.aureliumskills.menu.items.CloseItem;
import com.archyx.aureliumskills.menu.items.ItemType;
import com.archyx.aureliumskills.menu.items.YourSkillsItem;
import com.archyx.aureliumskills.menu.templates.SkillTemplate;
import com.archyx.aureliumskills.menu.templates.TemplateType;
import com.archyx.aureliumskills.skills.Skill;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Locale;

public class SkillsMenu implements InventoryProvider{

	private final Locale locale;
	private final MenuOption options;
	private final AureliumSkills plugin;

	public SkillsMenu(Locale locale, MenuOption menuOption, AureliumSkills plugin) {
		this.locale = locale;
		this.options = menuOption;
		this.plugin = plugin;
	}

	public void init(Player player, InventoryContents contents) {
		// Fill item
		if (options.isFillEnabled()) {
			contents.fill(ClickableItem.empty(options.getFillItem()));
		}
		// Close item
		CloseItem closeItem = (CloseItem) options.getItem(ItemType.CLOSE);
		contents.set(closeItem.getPos(), ClickableItem.of(closeItem.getItem(locale), e -> player.closeInventory()));
		// Your skills item
		YourSkillsItem yourSkillsItem = (YourSkillsItem) options.getItem(ItemType.YOUR_SKILLS);
		contents.set(yourSkillsItem.getPos(), ClickableItem.empty(yourSkillsItem.getItem(player, locale)));
		// Skill items
		PlayerData playerData = plugin.getPlayerManager().getPlayerData(player);
		if (playerData != null) {
			SkillTemplate skillTemplate = (SkillTemplate) options.getTemplate(TemplateType.SKILL);
			if (OptionL.isEnabled(Skill.FARMING)) {
				contents.set(skillTemplate.getPosition(Skill.FARMING), ClickableItem.of(skillTemplate.getItem(Skill.FARMING, playerData, locale), e -> open(player, playerData, Skill.FARMING)));
			}
			if (OptionL.isEnabled(Skill.FORAGING)) {
				contents.set(skillTemplate.getPosition(Skill.FORAGING), ClickableItem.of(skillTemplate.getItem(Skill.FORAGING, playerData, locale), e -> open(player, playerData, Skill.FORAGING)));
			}
			if (OptionL.isEnabled(Skill.MINING)) {
				contents.set(skillTemplate.getPosition(Skill.MINING), ClickableItem.of(skillTemplate.getItem(Skill.MINING, playerData, locale), e -> open(player, playerData, Skill.MINING)));
			}
			if (OptionL.isEnabled(Skill.FISHING)) {
				contents.set(skillTemplate.getPosition(Skill.FISHING), ClickableItem.of(skillTemplate.getItem(Skill.FISHING, playerData, locale), e -> open(player, playerData, Skill.FISHING)));
			}
			if (OptionL.isEnabled(Skill.EXCAVATION)) {
				contents.set(skillTemplate.getPosition(Skill.EXCAVATION), ClickableItem.of(skillTemplate.getItem(Skill.EXCAVATION, playerData, locale), e -> open(player, playerData, Skill.EXCAVATION)));
			}
			//Combat Skills
			if (OptionL.isEnabled(Skill.ARCHERY)) {
				contents.set(skillTemplate.getPosition(Skill.ARCHERY), ClickableItem.of(skillTemplate.getItem(Skill.ARCHERY, playerData, locale), e -> open(player, playerData, Skill.ARCHERY)));
			}
			if (OptionL.isEnabled(Skill.DEFENSE)) {
				contents.set(skillTemplate.getPosition(Skill.DEFENSE), ClickableItem.of(skillTemplate.getItem(Skill.DEFENSE, playerData, locale), e -> open(player, playerData, Skill.DEFENSE)));
			}
			if (OptionL.isEnabled(Skill.FIGHTING)) {
				contents.set(skillTemplate.getPosition(Skill.FIGHTING), ClickableItem.of(skillTemplate.getItem(Skill.FIGHTING, playerData, locale), e -> open(player, playerData, Skill.FIGHTING)));
			}
			if (OptionL.isEnabled(Skill.ENDURANCE)) {
				contents.set(skillTemplate.getPosition(Skill.ENDURANCE), ClickableItem.of(skillTemplate.getItem(Skill.ENDURANCE, playerData, locale), e -> open(player, playerData, Skill.ENDURANCE)));
			}
			if (OptionL.isEnabled(Skill.AGILITY)) {
				contents.set(skillTemplate.getPosition(Skill.AGILITY), ClickableItem.of(skillTemplate.getItem(Skill.AGILITY, playerData, locale), e -> open(player, playerData, Skill.AGILITY)));
			}
			//Magic Skills
			if (OptionL.isEnabled(Skill.ALCHEMY)) {
				contents.set(skillTemplate.getPosition(Skill.ALCHEMY), ClickableItem.of(skillTemplate.getItem(Skill.ALCHEMY, playerData, locale), e -> open(player, playerData, Skill.ALCHEMY)));
			}
			if (OptionL.isEnabled(Skill.ENCHANTING)) {
				contents.set(skillTemplate.getPosition(Skill.ENCHANTING), ClickableItem.of(skillTemplate.getItem(Skill.ENCHANTING, playerData, locale), e -> open(player, playerData, Skill.ENCHANTING)));
			}
			if (OptionL.isEnabled(Skill.SORCERY)) {
				contents.set(skillTemplate.getPosition(Skill.SORCERY), ClickableItem.of(skillTemplate.getItem(Skill.SORCERY, playerData, locale), e -> open(player, playerData, Skill.SORCERY)));
			}
			if (OptionL.isEnabled(Skill.HEALING)) {
				contents.set(skillTemplate.getPosition(Skill.HEALING), ClickableItem.of(skillTemplate.getItem(Skill.HEALING, playerData, locale), e -> open(player, playerData, Skill.HEALING)));
			}
			if (OptionL.isEnabled(Skill.FORGING)) {
				contents.set(skillTemplate.getPosition(Skill.FORGING), ClickableItem.of(skillTemplate.getItem(Skill.FORGING, playerData, locale), e -> open(player, playerData, Skill.FORGING)));
			}
		}
		else {
			player.closeInventory();
			player.sendMessage(AureliumSkills.getPrefix(locale) + ChatColor.RED + Lang.getMessage(CommandMessage.NO_PROFILE, locale));
		}
	}

	public void update(Player player, InventoryContents contents) {

	}

	public static int getPage(Skill skill, PlayerData playerData) {
		int page = (playerData.getSkillLevel(skill) - 2) / 24;
		int maxLevelPage = (OptionL.getMaxLevel(skill) - 2) / 24;
		if (page > maxLevelPage) {
			page = maxLevelPage;
		}
		return page;
	}

	private void open(Player player, PlayerData playerData, Skill skill) {
		if (player.hasPermission("aureliumskills." + skill.name().toLowerCase(Locale.ENGLISH))) {
			int page = getPage(skill, playerData);
			LevelProgressionMenu.getInventory(player, skill, page, plugin).open(player, page);
		}
	}

	public static SmartInventory getInventory(Player player, AureliumSkills plugin) {
		Locale locale = Lang.getLanguage(player);
		MenuOption menuOption = plugin.getMenuLoader().getMenu(MenuType.SKILLS);
		return SmartInventory.builder()
				.provider(new SkillsMenu(locale, menuOption, plugin))
				.size(menuOption.getRows(), 9)
				.title(Lang.getMessage(MenuMessage.SKILLS_MENU_TITLE, locale))
				.manager(plugin.getInventoryManager())
				.build();
	}

}
