package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.AdvancedChallenge;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen
 * Challenges developed on 06-23-2020
 * https://github.com/anweisen
 */

public class NoSneakChallenge extends AdvancedChallenge implements Listener {

	public NoSneakChallenge() {
		menu = MenuType.CHALLENGES;
		value = 20;
		maxValue = 50;
	}

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.CHAINMAIL_BOOTS, ItemTranslation.NO_SNEAK).hideAttributes().build();
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public void onValueChange(ChallengeEditEvent event) { }

	@Override
	public void onTimeActivation() { }

	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event) {

		if (!enabled || !Challenges.timerIsStarted()) return;
		if (!event.isSneaking()) return;
		if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;

		event.setCancelled(true);

		Bukkit.broadcastMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.NO_SNEAK_PLAYER_SNEAKED.get()
					.replace("%player%", event.getPlayer().getName()));
		event.getPlayer().damage(value);

	}
}
