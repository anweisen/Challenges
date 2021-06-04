package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class EnderChestCommandSetting extends Setting implements PlayerCommand {

	public EnderChestCommandSetting() {
		super(MenuType.SETTINGS);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.ENDER_CHEST, Message.forName("item-enderchest-command-setting"));
	}

	@Override
	public void onCommand(@Nonnull Player player, @Nonnull String[] args) throws Exception {
		if (!isEnabled() || ChallengeAPI.isWorldInUse()) {
			Message.forName("feature-disabled").send(player, Prefix.CHALLENGES);
			return;
		}

		player.openInventory(player.getEnderChest());
		Message.forName("command-enderchest-open").send(player, Prefix.CHALLENGES);
	}

}