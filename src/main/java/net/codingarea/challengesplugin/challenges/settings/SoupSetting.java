package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author anweisen & Dominik
 * Challenges developed on 02-6-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class SoupSetting extends Setting implements Listener {

	public SoupSetting() {
		super(MenuType.SETTINGS);
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {

		if (!enabled || !Challenges.timerIsStarted()) return;
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (event.getItem() == null) return;
		if (event.getItem().getType() != Material.MUSHROOM_STEW) return;

		if (event.getPlayer().getHealth() == event.getPlayer().getMaxHealth()) return;

		event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1));
		event.getPlayer().setItemInHand(new ItemBuilder(Material.BOWL).build());
		event.getPlayer().updateInventory();
		event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_PLAYER_BURP, 1F, 1F);

		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			if (event.getPlayer().getItemInHand().getType() != Material.MUSHROOM_STEW) return;
			event.getPlayer().setItemInHand(new ItemBuilder(Material.BOWL).build());
			event.getPlayer().updateInventory();
		}, 1);

	}

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.MUSHROOM_STEW, ItemTranslation.SOUP_HEALTH).build();
	}
}
