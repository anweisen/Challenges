package net.codingarea.challenges.plugin.challenges.custom.settings.action;

import java.util.Map;
import java.util.function.Supplier;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public abstract class AbstractChallengePlayerTargetAction extends AbstractChallengeTargetAction {

  public AbstractChallengePlayerTargetAction(String name,
      SubSettingsBuilder subSettingsBuilder) {
    super(name, subSettingsBuilder);
  }

  public AbstractChallengePlayerTargetAction(String name) {
    super(name);
  }

  public AbstractChallengePlayerTargetAction(String name,
      Supplier<SubSettingsBuilder> builderSupplier) {
    super(name, builderSupplier);
  }

  @Override
  public void executeFor(Entity entity, Map<String, String[]> subActions) {
    if (entity instanceof Player) {
      executeForPlayer(((Player) entity), subActions);
    }
  }

  public abstract void executeForPlayer(Player player, Map<String, String[]> subActions);

}
