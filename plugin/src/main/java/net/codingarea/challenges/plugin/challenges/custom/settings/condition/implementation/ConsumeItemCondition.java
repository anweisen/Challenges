package net.codingarea.challenges.plugin.challenges.custom.settings.condition.implementation;

import net.codingarea.challenges.plugin.challenges.custom.settings.condition.IChallengeCondition;
import net.codingarea.challenges.plugin.utils.misc.MapUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ConsumeItemCondition implements IChallengeCondition {

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onConsumeItem(PlayerItemConsumeEvent event) {
    execute(event.getPlayer(), MapUtils.createStringListMap("item", "any", event.getItem().getType().name()));
  }

}
