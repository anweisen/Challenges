package net.codingarea.challenges.plugin.challenges.implementation.goal;

import net.anweisen.utilities.common.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.abstraction.FirstPlayerAtHeightGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
@Since("2.1.0")
public class MaxHeightGoal extends FirstPlayerAtHeightGoal {

  public MaxHeightGoal() {
    setHeightToGetTo(Bukkit.getWorlds().get(0).getMaxHeight());
  }

  @NotNull
  @Override
  public ItemBuilder createDisplayItem() {
    return new ItemBuilder(Material.FEATHER, Message.forName("item-max-height-goal").asItemDescription(getHeightToGetTo()));
  }

}
