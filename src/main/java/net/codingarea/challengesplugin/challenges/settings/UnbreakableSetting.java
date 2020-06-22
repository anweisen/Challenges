package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-03-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public class UnbreakableSetting extends Setting implements Listener {

	public UnbreakableSetting() {
		menu = MenuType.SETTINGS;
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent event) {

		if (!enabled || !Challenges.timerIsStarted()) return;

		event.setCancelled(true);
		event.getPlayer().updateInventory();

	}

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.ANVIL, ItemTranslation.NO_ITEM_DAMAGE).build();
	}
}
