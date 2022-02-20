package net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl;

import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class MoveCameraCondition extends AbstractChallengeCondition {

  public MoveCameraCondition(String name) {
    super(name);
  }

  @Override
  public Material getMaterial() {
    return Material.COMPASS;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onMove(PlayerMoveEvent event) {
    if (event.getTo() == null) return;
    if (event.getFrom().getDirection().equals(event.getTo().getDirection())) return;
    execute(event.getPlayer(), event);

  }

}
