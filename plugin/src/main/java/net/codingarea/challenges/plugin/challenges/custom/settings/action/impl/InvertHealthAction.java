package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengePlayerTargetAction;
import net.codingarea.challenges.plugin.challenges.implementation.challenge.InvertHealthChallenge;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class InvertHealthAction extends AbstractChallengePlayerTargetAction {

  public InvertHealthAction(String name) {
    super(name, createEntityTargetSettingsBuilder(false, true));
  }

  @Override
  public Material getMaterial() {
    return Material.REDSTONE;
  }


  @Override
  public void executeForPlayer(Player player, Map<String, String[]> subActions) {
    InvertHealthChallenge.invertHealth(player);
  }

}
