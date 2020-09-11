package net.codingarea.challengesplugin.listener;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.Translation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-31-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class UtilsListener implements Listener {

	private boolean chat;
	private final String chatFormat;

	public UtilsListener() {
		chat = Challenges.getInstance().getConfig().getBoolean("chatformat-enabled");
		chatFormat = Challenges.getInstance().getConfig().getString("chatformat");
		if (chatFormat == null) chat = false;
	}

	@EventHandler
	public void onAnvilUse(PrepareAnvilEvent event) {
		ItemStack result = event.getResult();
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', meta.getDisplayName()));
		result.setItemMeta(meta);
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {

		if (!chat) return;

		try {
			event.setFormat(chatFormat.replace("%player%", event.getPlayer().getDisplayName()).replace("%message%", event.getMessage()));
		} catch (Exception ignored) { }

	}

	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		if (event.getBedEnterResult() == BedEnterResult.OK) {
			event.setUseBed(Result.ALLOW);
			event.setCancelled(false);
		}
	}

	@EventHandler
	public void onAdvancementDone(PlayerAdvancementDoneEvent event) {
		//Bukkit.broadcastMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.ADVANCEMENT_DONE.get().replace("%player%", event.getPlayer().getName()).replace("%advancement%", event.getAdvancement().getKey().getKey()));
	}

}
