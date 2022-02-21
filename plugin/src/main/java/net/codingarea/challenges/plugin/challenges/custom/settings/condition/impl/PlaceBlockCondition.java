package net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl;

import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class PlaceBlockCondition extends AbstractChallengeCondition {

  public PlaceBlockCondition(String name) {
    super(name, createBlockSettingsBuilder());
  }

  @Override
  public Material getMaterial() {
    return Material.BRICKS;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onBlockPlace(BlockPlaceEvent event) {
    createData()
        .entity(event.getPlayer())
        .event(event)
        .block(event.getBlock().getType())
        .execute();
  }

}
