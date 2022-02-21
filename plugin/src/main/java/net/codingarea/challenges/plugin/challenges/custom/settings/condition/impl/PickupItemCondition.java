package net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl;

import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import net.codingarea.challenges.plugin.spigot.events.PlayerPickupItemEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class PickupItemCondition extends AbstractChallengeCondition {

  public PickupItemCondition(String name) {
    super(name);
  }

  @Override
  public Material getMaterial() {
    return Material.STICK;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPickup(PlayerPickupItemEvent event) {
    createData()
        .entity(event.getPlayer())
        .event(event)
        .execute();
  }

}
