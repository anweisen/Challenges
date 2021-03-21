package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class KeepInventorySetting extends Setting {

	public KeepInventorySetting() {
		super(MenuType.SETTINGS);
		try {
			setEnabled(Bukkit.getWorlds().get(0).getGameRuleValue(GameRule.KEEP_INVENTORY));
		} catch (Exception ignored) { }
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.ENDER_CHEST, Message.forName("item-keep-inventory-setting"));
	}

	@Override
	protected void onEnable() {
		for (World world : Bukkit.getWorlds()) {
			world.setGameRule(GameRule.KEEP_INVENTORY, true);
		}
	}

	@Override
	protected void onDisable() {
		for (World world : Bukkit.getWorlds()) {
			world.setGameRule(GameRule.KEEP_INVENTORY, false);
		}
	}
}