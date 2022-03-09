package net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl;

import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class DropItemCondition extends AbstractChallengeCondition {

  public DropItemCondition(String name) {
    super(name);
  }

  @Override
  public Material getMaterial() {
    return Material.DROPPER;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onItemDrop(PlayerDropItemEvent event) {
    createData()
        .entity(event.getPlayer())
        .event(event)
        .execute();
  }

}
