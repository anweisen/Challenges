package net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl;

import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.AbstractChallengeTrigger;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class StandsOnSpecificBlockTrigger extends AbstractChallengeTrigger {

  public StandsOnSpecificBlockTrigger(String name) {
    super(name, createBlockSettingsBuilder());
  }

  @Override
  public Material getMaterial() {
    return Material.PACKED_ICE;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onMove(PlayerMoveEvent event) {
    if (BlockUtils.isSameBlock(event.getTo(), event.getFrom())) return;
    Block blockBelow = BlockUtils.getBlockBelow(
        event.getTo());
    if (blockBelow == null) return;

    createData()
        .entity(event.getPlayer())
        .event(event)
        .block(blockBelow.getType())
        .execute();
  }

}
