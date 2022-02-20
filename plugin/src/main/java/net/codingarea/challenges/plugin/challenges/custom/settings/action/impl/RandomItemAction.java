package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.TargetEntitiesChallengeAction;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class RandomItemAction extends AbstractChallengeAction {

  public RandomItemAction(String name) {
    super(name, createEntityTargetSettingsBuilder(false));
  }

  @Override
  public void execute(Entity entity, Map<String, String[]> subActions) {
    ArrayList<Material> list = new ArrayList<>(Arrays.asList(Material.values()));
    list.removeIf(material -> !material.isItem());

    for (Entity target : TargetEntitiesChallengeAction.getTargets(entity, subActions)) {
      if (target instanceof Player) {
        Player player = (Player) target;
        InventoryUtils.giveItem(player.getInventory(), player.getLocation(), new ItemStack(random.choose(list)));
      }
    }
  }

  @Override
  public Material getMaterial() {
    return Material.BEACON;
  }

}
