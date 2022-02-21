package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeTargetAction;
import net.codingarea.challenges.plugin.challenges.implementation.challenge.FreezeChallenge;
import net.codingarea.challenges.plugin.challenges.type.abstraction.AbstractChallenge;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class FreezeAction extends AbstractChallengeTargetAction {

  FreezeChallenge instance = AbstractChallenge
      .getFirstInstance(FreezeChallenge.class);

  public FreezeAction(String name) {
    super(name, createEntityTargetSettingsBuilder(true));
  }

  @Override
  public Material getMaterial() {
    return Material.ICE;
  }

  @Override
  public void executeFor(Entity entity, Map<String, String[]> subActions) {

    if (entity instanceof LivingEntity) {
      LivingEntity livingEntity = (LivingEntity) entity;
      instance.setFreeze(livingEntity, 2);
    }


  }

}
