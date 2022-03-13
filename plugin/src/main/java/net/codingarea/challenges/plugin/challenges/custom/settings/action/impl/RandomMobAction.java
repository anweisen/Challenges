package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeTargetAction;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class RandomMobAction extends AbstractChallengeTargetAction {

  public RandomMobAction(String name) {
    super(name, createEntityTargetSettingsBuilder(false));
  }

  @Override
  public void executeFor(Entity entity, Map<String, String[]> subActions) {
    if (entity.getLocation().getWorld() == null) return;
    for (int i = 0; i < 100; i++) {
      EntityType value = AbstractChallengeAction.random.choose(EntityType.values());
      if (value.isSpawnable()) {
        try {
          entity.getLocation().getWorld().spawnEntity(entity.getLocation(), value);
        } catch (Exception ex) {
          continue;
        }
        break;
      }
    }
  }

  @Override
  public Material getMaterial() {
    return Material.BLAZE_SPAWN_EGG;
  }

}
