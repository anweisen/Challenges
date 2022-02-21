package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengePlayerTargetAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class ClearInventoryAction extends AbstractChallengePlayerTargetAction {

  public ClearInventoryAction(String name) {
    super(name, createEntityTargetSettingsBuilder(false, true));
  }

  @Override
  public Material getMaterial() {
    return Material.TRAPPED_CHEST;
  }

  @Override
  public void executeForPlayer(Player player, Map<String, String[]> subActions) {
    player.getInventory().clear();
  }

}
