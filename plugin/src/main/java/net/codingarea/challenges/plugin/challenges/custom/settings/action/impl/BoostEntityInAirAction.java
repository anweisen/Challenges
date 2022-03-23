package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeTargetAction;
import net.codingarea.challenges.plugin.challenges.type.helper.SubSettingsHelper;
import net.codingarea.challenges.plugin.utils.misc.EntityUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class BoostEntityInAirAction extends AbstractChallengeTargetAction {

  public BoostEntityInAirAction(String name) {
    super(name, SubSettingsHelper.createEntityTargetSettingsBuilder(true));
  }

  @Override
  public Material getMaterial() {
    return Material.FEATHER;
  }

  @Override
  public void executeFor(Entity entity, Map<String, String[]> subActions) {
    Vector velocityToAdd = new Vector(0, 3 / 2, 0);
    Vector newVelocity = EntityUtils.getSucceedingVelocity(entity.getVelocity()).add(velocityToAdd);
    entity.setVelocity(newVelocity);
  }

}
