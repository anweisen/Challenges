package net.codingarea.challenges.plugin.challenges.custom.api.condition.implementation;

import net.codingarea.challenges.plugin.challenges.custom.api.condition.IChallengeCondition;
import net.codingarea.challenges.plugin.spigot.events.PlayerJumpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class PlayerJumpCondition implements IChallengeCondition {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onJump(@Nonnull PlayerJumpEvent event) {
		execute(event.getPlayer());
	}

}
