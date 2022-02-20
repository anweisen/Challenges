package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeTargetAction;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class KillEntityAction extends AbstractChallengeTargetAction {

  public KillEntityAction(String name) {
    super(name, createEntityTargetSettingsBuilder(true));
  }

  @Override
  public void executeFor(Entity entity, Map<String, String[]> subActions) {
    if (entity instanceof Player) {
      ChallengeHelper.kill(((Player) entity));
    } else if (entity instanceof LivingEntity) {
      ((LivingEntity) entity).damage(((LivingEntity) entity).getHealth());
    }
  }

  @Override
  public Material getMaterial() {
    return Material.COMMAND_BLOCK;
  }

}
