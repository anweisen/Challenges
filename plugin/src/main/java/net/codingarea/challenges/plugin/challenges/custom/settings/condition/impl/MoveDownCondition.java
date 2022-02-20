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
public class MoveDownCondition extends AbstractChallengeCondition {

  public MoveDownCondition(String name) {
    super(name);
  }

  @Override
  public Material getMaterial() {
    return Material.NETHER_BRICK_STAIRS;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onMove(PlayerMoveEvent event) {
    if (event.getTo() == null) return;
    if (event.getTo().getBlockY() < event.getFrom().getBlockY()) {
      execute(event.getPlayer(), event);
    }

  }

}
