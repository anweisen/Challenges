package net.codingarea.challenges.plugin.challenges.custom.api.condition.implementation;

import net.codingarea.challenges.plugin.challenges.custom.api.condition.IChallengeCondition;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class PlayerSneakCondition implements IChallengeCondition {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onJump(@Nonnull PlayerToggleSneakEvent event) {
		if (event.isSneaking()) {
			execute(event.getPlayer());
		}
	}

}