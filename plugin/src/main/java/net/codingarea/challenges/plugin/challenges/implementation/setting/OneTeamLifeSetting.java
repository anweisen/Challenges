package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.challenges.type.abstraction.AbstractChallenge;
import net.codingarea.challenges.plugin.challenges.type.abstraction.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class OneTeamLifeSetting extends Setting {

	private boolean isKilling = false;

	public OneTeamLifeSetting() {
		super(MenuType.SETTINGS, true);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.FIRE_CHARGE, Message.forName("item-one-life-setting"));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDeath(@Nonnull PlayerDeathEvent event) {
		if (!shouldExecuteEffect()) return;
		if (isKilling) return;

		AbstractChallenge.getFirstInstance(DeathMessageSetting.class).setHideMessagesTemporarily(isKilling = true);
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player == event.getEntity()) continue;
			if (ignorePlayer(player)) continue;
			kill(player);
		}
		AbstractChallenge.getFirstInstance(RespawnSetting.class).checkAllPlayersDead();
		AbstractChallenge.getFirstInstance(DeathMessageSetting.class).setHideMessagesTemporarily(isKilling = false);
	}

}
