package net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl;

import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class AdvancementCondition extends AbstractChallengeCondition {

  public AdvancementCondition(String name) {
    super(name);
  }

  @Override
  public Material getMaterial() {
    return Material.KNOWLEDGE_BOOK;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPickup(PlayerAdvancementDoneEvent event) {
    if (event.getAdvancement().getKey().toString().contains("minecraft:recipes/")) return;
    createData().entity(event.getPlayer()).execute();
  }

}
