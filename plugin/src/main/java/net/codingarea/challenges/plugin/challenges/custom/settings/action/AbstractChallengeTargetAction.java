package net.codingarea.challenges.plugin.challenges.custom.settings.action;

import java.util.Map;
import java.util.function.Supplier;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import org.bukkit.entity.Entity;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public abstract class AbstractChallengeTargetAction extends AbstractChallengeAction implements TargetEntitiesChallengeAction {

  public AbstractChallengeTargetAction(String name,
      SubSettingsBuilder subSettingsBuilder) {
    super(name, subSettingsBuilder);
  }

  public AbstractChallengeTargetAction(String name) {
    super(name);
  }

  public AbstractChallengeTargetAction(String name,
      Supplier<SubSettingsBuilder> builderSupplier) {
    super(name, builderSupplier);
  }

  @Override
  public void execute(Entity entity, Map<String, String[]> subActions) {
    for (Entity target : TargetEntitiesChallengeAction.getTargets(entity, subActions)) {
      executeFor(target, subActions);
    }
  }

}
