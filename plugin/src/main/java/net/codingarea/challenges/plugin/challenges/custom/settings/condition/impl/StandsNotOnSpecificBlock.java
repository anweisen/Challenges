package net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
public class StandsNotOnSpecificBlock extends AbstractChallengeCondition {

  public StandsNotOnSpecificBlock(String name) {
    super(name, createBlockSettingsBuilder());
  }

  @Override
  public Material getMaterial() {
    return Material.MAGMA_BLOCK;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onMove(PlayerMoveEvent event) {
    if (BlockUtils.isSameBlock(event.getTo(), event.getFrom())) return;

    Block blockBelow = BlockUtils.getBlockBelow(event.getTo());
    if (blockBelow == null) return;

    Material type = blockBelow.getType();

    List<Material> materials = new LinkedList<>(Arrays.asList(Material.values()));
    materials.remove(type);
    materials.removeIf(material -> !material.isBlock() || !material.isItem());

    List<String> names = new LinkedList<>();
    for (Material material : materials) {
      names.add(material.name());
    }

    execute(event.getPlayer(), event,
        MapUtils.createStringListMap("block", names.toArray(new String[0])));

  }

}
