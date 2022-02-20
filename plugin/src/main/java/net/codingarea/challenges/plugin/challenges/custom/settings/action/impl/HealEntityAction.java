package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeTargetAction;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class HealEntityAction extends AbstractChallengeTargetAction {

  public HealEntityAction(String name) {
    super(name, createEntityTargetSettingsBuilder(true).createChooseItemChild("amount").fill(builder -> {
      String prefix = DefaultItem.getItemPrefix();
      for (int i = 1; i < 21; i++) {
        builder.addSetting(i + "", new ItemBuilder(Material.RED_DYE, prefix + "§7" + (i / 2f) + " §c❤").setAmount(i).build());
      }
    }));
  }

  @Override
  public void executeFor(Entity entity, Map<String, String[]> subActions) {
    int amount = Integer.parseInt(subActions.get("amount")[0]);
    if (entity instanceof LivingEntity) {
      LivingEntity livingEntity = (LivingEntity) entity;
      double newHealth = Math.min(livingEntity.getHealth() + amount,
          livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
      livingEntity.setHealth(newHealth);
    }
  }

  @Override
  public Material getMaterial() {
    return Material.RED_DYE;
  }

}
