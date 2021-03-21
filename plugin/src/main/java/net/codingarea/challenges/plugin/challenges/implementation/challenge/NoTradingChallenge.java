package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class NoTradingChallenge extends Setting {

	public NoTradingChallenge() {
		super(MenuType.CHALLENGES);
	}

	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		if (!shouldExecuteEffect()) return;
		if (event.getRightClicked() instanceof Villager) {
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
			event.setCancelled(true);
		}
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.EMERALD, Message.forName("item-no-trading-challenge"));
	}

}