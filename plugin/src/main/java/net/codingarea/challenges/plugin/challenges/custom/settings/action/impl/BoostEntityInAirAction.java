package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeTargetAction;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class BoostEntityInAirAction extends AbstractChallengeTargetAction {

  public BoostEntityInAirAction(String name) {
    super(name, createEntityTargetSettingsBuilder(true));
  }

  @Override
  public Material getMaterial() {
    return Material.FEATHER;
  }

  @Override
  public void executeFor(Entity entity, Map<String, String[]> subActions) {
    if (entity instanceof Player) {
      Player player = (Player) entity;
    }
  }

}
