package net.codingarea.challenges.plugin.challenges.custom.settings.condition;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.abstraction.AbstractChallenge;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public interface IChallengeCondition extends Listener {

	default void execute(Entity entity, String... data) {
		if (ChallengeAPI.isStarted() && !ChallengeAPI.isWorldInUse()) {
			if (entity instanceof Player && AbstractChallenge.ignorePlayer(((Player) entity))) {
				return;
			}
			Challenges.getInstance().getCustomChallengesLoader().executeCondition(this, entity, data);
		}
	}

}
