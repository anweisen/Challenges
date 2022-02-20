package net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl;

import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.MapUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ConsumeItemCondition extends AbstractChallengeCondition {

  public ConsumeItemCondition(String name) {
    super(name, SubSettingsBuilder.createChooseMultipleItem("item").fill(builder -> {
      for (Material material : Material.values()) {
        if (material.isEdible()) {
          builder.addSetting(material.name(), new ItemBuilder(material, DefaultItem.getItemPrefix() + StringUtils
              .getEnumName(material)).build());
        }
      }
    }));
  }

  @Override
  public Material getMaterial() {
    return Material.COOKED_BEEF;
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onConsumeItem(PlayerItemConsumeEvent event) {
    execute(event.getPlayer(), MapUtils.createStringListMap("item", "any", event.getItem().getType().name()));
  }

}
