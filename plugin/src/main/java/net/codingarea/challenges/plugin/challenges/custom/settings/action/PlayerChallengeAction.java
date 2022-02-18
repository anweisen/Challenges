package net.codingarea.challenges.plugin.challenges.custom.settings.action;

import java.util.Map;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public interface PlayerChallengeAction extends IChallengeAction {

	@Override
	default void execute(Entity entity, Map<String, String> data) {
		if (entity instanceof Player) {
			execute((Player) entity, data);
		}
	}

	void execute(Player player, Map<String, String> data);

}
