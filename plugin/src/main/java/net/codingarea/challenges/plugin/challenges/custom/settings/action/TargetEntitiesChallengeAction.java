package net.codingarea.challenges.plugin.challenges.custom.settings.action;

import java.util.Map;
import org.bukkit.entity.Entity;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface TargetEntitiesChallengeAction extends IChallengeAction {

  @Override
  default void execute(Entity entity, Map<String, String[]> subActions) {
    for (Entity target : IChallengeAction.getTargets(entity, subActions)) {
      executeFor(target, subActions);
    }
  }

  void executeFor(Entity entity, Map<String, String[]> subActions);

}
