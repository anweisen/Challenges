package net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
public class StandsNotOnSpecificBlockTrigger extends AbstractChallengeTrigger {

  public StandsNotOnSpecificBlockTrigger(String name) {
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

    createData()
        .entity(event.getPlayer())
        .event(event)
        .data("block", names)
        .execute();
  }

}
