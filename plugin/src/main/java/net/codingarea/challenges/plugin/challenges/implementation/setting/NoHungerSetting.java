package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class NoHungerSetting extends Setting {

	public NoHungerSetting() {
		super(MenuType.SETTINGS);
	}

	@EventHandler
	public void onHunger(@Nonnull FoodLevelChangeEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		if (!shouldExecuteEffect()) return;
		event.setCancelled(true);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.BREAD, Message.forName("no-hunger-setting"));
	}

}