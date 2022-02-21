package net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl;

import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.CraftItemEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class CraftItemCondition extends AbstractChallengeCondition {

  public CraftItemCondition(String name) {
    super(name);
  }

  @Override
  public Material getMaterial() {
    return Material.CRAFTING_TABLE;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onCraft(CraftItemEvent event) {
    if (!(event.getWhoClicked() instanceof Player)) return;
    Player player = (Player) event.getWhoClicked();
    createData()
        .entity(player)
        .event(event)
        .execute();
  }

}
