package net.codingarea.challenges.plugin.challenges.custom.api.action;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public interface PlayerChallengeAction extends IChallengeAction {

	@Override
	default void execute(Entity entity, String... data) {
		if (entity instanceof Player) {
			execute(((Player) entity), data);
		}
	}

	void execute(Player player, String... data);

}