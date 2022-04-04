package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeTargetAction;
import net.codingarea.challenges.plugin.challenges.implementation.challenge.UncraftItemsChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.SubSettingsHelper;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class UncraftInventoryAction extends AbstractChallengeTargetAction {

  public UncraftInventoryAction(String name) {
    super(name, SubSettingsHelper.createEntityTargetSettingsBuilder(false));
  }

  @Override
  public void executeFor(Entity entity, Map<String, String[]> subActions) {
    if (entity instanceof Player) {
      Player player = (Player) entity;
      UncraftItemsChallenge.uncraftInventory(player);
    }
  }

  @Override
  public Material getMaterial() {
    return Material.CRAFTING_TABLE;
  }

}
