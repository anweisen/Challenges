package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challenges.difficulty.SplitHealthSetting;
import net.codingarea.challengesplugin.challengetypes.AdvancedChallenge;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen
 * Challenges developed on 06-23-2020
 * https://github.com/anweisen
 */

public class NoSneakChallenge extends AdvancedChallenge implements Listener {

	public NoSneakChallenge() {
		super(MenuType.CHALLENGES, 50);
		value = 20;
	}

	@Override
	public @NotNull ItemStack getItem() {
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
		if (event.getPlayer().getGameMode() == GameMode.SPECTATOR || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;

		event.setCancelled(true);
		event.getPlayer().setSneaking(false);

		Bukkit.broadcastMessage(Prefix.CHALLENGES + Translation.NO_SNEAK_PLAYER_SNEAKED.get().replace("%player%", event.getPlayer().getName()));
		event.getPlayer().damage(value);
		SplitHealthSetting.sync(event.getPlayer());

	}
}
