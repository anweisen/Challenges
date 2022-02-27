package net.codingarea.challenges.plugin.challenges.implementation.setting;

import javax.annotation.Nonnull;
import net.anweisen.utilities.common.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.abstraction.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class ImmediateRespawnSetting extends Setting {

	public ImmediateRespawnSetting() {
		super(MenuType.SETTINGS);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.GOLDEN_APPLE, Message.forName("item-immediate-respawn-setting"));
	}

	@Override
	protected void onEnable() {
		for (World world : Bukkit.getWorlds()) {
			world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
		}
	}

	@Override
	protected void onDisable() {
		for (World world : Bukkit.getWorlds()) {
			world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
		}
	}

}