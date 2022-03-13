package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.TargetEntitiesChallengeAction;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class RandomItemAction extends AbstractChallengeAction {

  public static final List<Material> items;

  public RandomItemAction(String name) {
    super(name, createEntityTargetSettingsBuilder(false, true));
  }

  @Override
  public void execute(Entity entity, Map<String, String[]> subActions) {

    for (Entity target : TargetEntitiesChallengeAction.getTargets(entity, subActions)) {
      if (target instanceof Player) {
        Player player = (Player) target;
        giveRandomItemToPlayer(player);
      }
    }
  }

  public static void giveRandomItemToPlayer(@Nonnull Player player) {
    InventoryUtils.giveItem(player.getInventory(),
        player.getLocation(), new ItemStack(random.choose(items)));
  }

  @Override
  public Material getMaterial() {
    return Material.BEACON;
  }

  static {
    items = new ArrayList<>(Arrays.asList(Material.values()));
    items.removeIf(material -> !material.isItem());
  }

}
