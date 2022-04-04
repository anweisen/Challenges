package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengePlayerTargetAction;
import net.codingarea.challenges.plugin.challenges.implementation.challenge.RandomItemSwappingChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.SubSettingsHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class SwapRandomItemAction extends AbstractChallengePlayerTargetAction {

  public SwapRandomItemAction(String name) {
    super(name, SubSettingsHelper.createEntityTargetSettingsBuilder(false, true));
  }

  @Override
  public Material getMaterial() {
    return Material.HOPPER;
  }

  @Override
  public void executeForPlayer(Player player, Map<String, String[]> subActions) {
      RandomItemSwappingChallenge.swapRandomItems(player);
  }

}
