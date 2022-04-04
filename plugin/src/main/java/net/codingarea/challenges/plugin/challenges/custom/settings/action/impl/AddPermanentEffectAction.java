package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengePlayerTargetAction;
import net.codingarea.challenges.plugin.challenges.implementation.challenge.PermanentEffectOnDamageChallenge;
import net.codingarea.challenges.plugin.challenges.type.abstraction.AbstractChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.SubSettingsHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class AddPermanentEffectAction extends AbstractChallengePlayerTargetAction {

  PermanentEffectOnDamageChallenge instance = AbstractChallenge
      .getFirstInstance(PermanentEffectOnDamageChallenge.class);

  public AddPermanentEffectAction(String name) {
    super(name, SubSettingsHelper.createEntityTargetSettingsBuilder(false, true));
  }

  @Override
  public Material getMaterial() {
    return Material.MAGMA_CREAM;
  }

  @Override
  public void executeForPlayer(Player player, Map<String, String[]> subActions) {
    instance.addRandomEffect(player);
    instance.updateEffects();
  }

}
