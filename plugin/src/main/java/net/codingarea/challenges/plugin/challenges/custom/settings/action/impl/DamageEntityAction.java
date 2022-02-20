package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeTargetAction;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class DamageEntityAction extends AbstractChallengeTargetAction {

  public DamageEntityAction(String name) {
    super(name, createEntityTargetSettingsBuilder(true).createChooseItemChild("damage").fill(builder -> {
      String prefix = Message.forName("item-prefix").asString();
      for (int i = 1; i < 21; i++) {
        builder.addSetting(i + "", new ItemBuilder(Material.RED_DYE, prefix + "§7" + (i / 2f) + " §c❤").setAmount(i).build());
      }
    }));
  }

  @Override
  public void executeFor(Entity entity, Map<String, String[]> subActions) {
    int damage = Integer.parseInt(subActions.get("damage")[0]);
    if (entity instanceof LivingEntity) {
      ((LivingEntity) entity).setNoDamageTicks(0);
      ((LivingEntity) entity).damage(damage);
      ((LivingEntity) entity).setNoDamageTicks(0);
    }
  }

  @Override
  public Material getMaterial() {
    return Material.FERMENTED_SPIDER_EYE;
  }

}
