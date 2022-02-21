package net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl;

import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerExpChangeEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class GainXPCondition extends AbstractChallengeCondition {

  public GainXPCondition(String name) {
    super(name);
  }

  @Override
  public Material getMaterial() {
    return Material.EXPERIENCE_BOTTLE;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPickup(PlayerExpChangeEvent event) {
    if (event.getAmount() > 0) {
      execute(event.getPlayer(), () -> event.setAmount(0));
    }
  }

}
