package net.codingarea.challenges.plugin.challenges.custom.settings.action;

import java.util.Map;
import java.util.function.Supplier;
import net.codingarea.challenges.plugin.challenges.custom.settings.ChallengeExecutionData;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import org.bukkit.entity.Entity;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public abstract class EntityTargetAction extends ChallengeAction implements IEntityTargetAction {

  public EntityTargetAction(String name,
                            SubSettingsBuilder subSettingsBuilder) {
    super(name, subSettingsBuilder);
  }

  public EntityTargetAction(String name) {
    super(name);
  }

  public EntityTargetAction(String name,
                            Supplier<SubSettingsBuilder> builderSupplier) {
    super(name, builderSupplier);
  }

  @Override
  public void execute(
      ChallengeExecutionData executionData,
      Map<String, String[]> subActions) {
    for (Entity target : IEntityTargetAction.getTargets(executionData.getEntity(), subActions)) {
      executeFor(target, subActions);
    }
  }

}
