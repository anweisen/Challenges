package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeAction;
import net.codingarea.challenges.plugin.challenges.implementation.challenge.WaterMLGChallenge;
import net.codingarea.challenges.plugin.challenges.type.abstraction.AbstractChallenge;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class WaterMLGAction extends AbstractChallengeAction {

  public WaterMLGAction(String name) {
    super(name);
  }

  @Override
  public Material getMaterial() {
    return Material.WATER_BUCKET;
  }

  @Override
  public void execute(Entity entity, Map<String, String[]> subActions) {
    AbstractChallenge.getFirstInstance(WaterMLGChallenge.class).startWorldChallenge();
  }

}
