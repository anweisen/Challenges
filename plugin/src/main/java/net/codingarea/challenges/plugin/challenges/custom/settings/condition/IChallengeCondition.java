package net.codingarea.challenges.plugin.challenges.custom.settings.condition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.CancelEventAction;
import net.codingarea.challenges.plugin.challenges.type.abstraction.AbstractChallenge;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Listener;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public interface IChallengeCondition extends Listener {

	default void execute(Entity entity) {
		execute(entity, new HashMap<>());
	}

	default void execute(Entity entity, Cancellable event) {
		execute(entity, event, new HashMap<>());
	}

	default void execute(Entity entity, Runnable cancelAction) {
		execute(entity, cancelAction, new HashMap<>());
	}

	default void execute(Entity entity, Map<String, List<String>> data) {
		execute(entity, () -> {}, data);
	}

	default void execute(Entity entity, Cancellable event, Map<String, List<String>> data) {
		execute(entity, () -> event.setCancelled(true), data);
	}

	default void execute(Entity entity, Runnable cancelAction, Map<String, List<String>> data) {
		if (ChallengeAPI.isStarted() && !ChallengeAPI.isWorldInUse()) {
			if (entity instanceof Player && AbstractChallenge.ignorePlayer(((Player) entity))) {
				return;
			}
			if (cancelAction != null) {
				CancelEventAction.onPreCondition();
			}
			Challenges.getInstance().getCustomChallengesLoader().executeCondition(this, entity, data);
			if (cancelAction != null && CancelEventAction.shouldCancel()) {
				cancelAction.run();
			}
		}
	}

}
