package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.Map;
import javax.annotation.Nonnull;
import net.codingarea.challenges.plugin.challenges.custom.settings.ChallengeExecutionData;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.TargetEntitiesChallengeAction;
import net.codingarea.challenges.plugin.challenges.type.helper.SubSettingsHelper;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class RandomItemAction extends AbstractChallengeAction {

  public RandomItemAction(String name) {
    super(name, SubSettingsHelper.createEntityTargetSettingsBuilder(false, true));
  }

  @Override
  public void execute(
      ChallengeExecutionData executionData,
      Map<String, String[]> subActions) {

    for (Entity target : TargetEntitiesChallengeAction.getTargets(executionData.getEntity(), subActions)) {
      if (target instanceof Player) {
        Player player = (Player) target;
        giveRandomItemToPlayer(player);
      }
    }
  }

  public static void giveRandomItemToPlayer(@Nonnull Player player) {
    InventoryUtils.giveItem(player.getInventory(),
        player.getLocation(), InventoryUtils.getRandomItem(true, false));
  }

  @Override
  public Material getMaterial() {
    return Material.BEACON;
  }

}
