package net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl;

import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import net.codingarea.challenges.plugin.utils.misc.MapUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class StandsOnSpecificBlock extends AbstractChallengeCondition {

  public StandsOnSpecificBlock(String name) {
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
    execute(event.getPlayer(), event,
        MapUtils.createStringListMap("block",
            blockBelow.getType().name()));
  }

}
