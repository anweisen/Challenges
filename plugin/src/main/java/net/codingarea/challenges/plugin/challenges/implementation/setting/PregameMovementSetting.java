package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class PregameMovementSetting extends Setting {

	public PregameMovementSetting() {
		super(MenuType.SETTINGS, true);
	}

	@EventHandler
	public void onMove(@Nonnull PlayerMoveEvent event) {
		if (ChallengeAPI.isStarted() || isEnabled()) return;

		Location from = event.getFrom();
		Location to = event.getTo();
		if (to == null) return;
		if (BlockUtils.isSameBlockIgnoreHeight(from, to)) return;

		event.setCancelled(true);
		Message.forName("title-pregame-movement-setting").sendTitle(event.getPlayer());
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.PISTON, Message.forName("pregame-movement-setting"));
	}

}