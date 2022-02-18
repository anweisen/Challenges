package net.codingarea.challenges.plugin.challenges.custom.settings.condition.implementation;

import net.codingarea.challenges.plugin.challenges.custom.settings.condition.IChallengeCondition;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class PlaceBlockCondition implements IChallengeCondition {

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onBlockPlace(BlockPlaceEvent event) {
    execute(event.getPlayer(), "any", event.getBlock().getType().name());
  }

}
