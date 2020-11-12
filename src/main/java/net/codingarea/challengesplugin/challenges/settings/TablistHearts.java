package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen & Dominik
 * Challenges developed on 11-09-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class TablistHearts extends Setting {

	private static final String name = "HEALTH";

	public TablistHearts() {
		super(MenuType.SETTINGS);
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		Utils.performConsoleCommand("scoreboard objectives add " + name + " health");
		Utils.performConsoleCommand("scoreboard objectives setdisplay list " + name);
	}

	@Override
	public void onDisable(ChallengeEditEvent event) {
		Utils.performConsoleCommand("scoreboard objectives remove " + name + "");
	}

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.REDSTONE, ItemTranslation.TABLIST_HEARTS).build();
	}

}