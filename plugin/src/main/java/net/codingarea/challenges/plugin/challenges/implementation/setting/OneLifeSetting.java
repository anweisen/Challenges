package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.OneEnabledSetting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.server.ChallengeEndCause;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class OneLifeSetting extends OneEnabledSetting {

	public OneLifeSetting() {
		super(MenuType.SETTINGS, true, "challenge_end_handle");
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.FIRE_CHARGE, Message.forName("item-one-life-setting"));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDeath(@Nonnull PlayerDeathEvent event) {
		if (ChallengeAPI.isPaused() || !isEnabled()) return; // Warum hast du hierfür nicht schon längst eine methode gemacht die beides abfragt?
		ChallengeAPI.endChallenge(ChallengeEndCause.GOAL_FAILED);
	}

}
