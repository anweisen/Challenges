package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen
 * https://github/github
 * Challenges developed on 10.08.2020
 */

public class PreGameMovement extends Setting implements Listener {

	public PreGameMovement() {
		super(MenuType.SETTINGS, true);
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.PISTON, ItemTranslation.PREGAME_MOVEMENT).build();
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {

		if (enabled || Challenges.timerIsStarted()) return;
		if (event.getPlayer().getGameMode() == GameMode.SPECTATOR || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;

		if (event.getFrom().getBlockX() != event.getTo().getBlockX()
		 || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {

			event.setCancelled(true);
			event.getPlayer().sendTitle(Translation.PREGAME_MOVEMENT_DENY_TITLE.get(), Translation.PREGAME_MOVEMENT_DENY_SUBTITLE.get());

		}

	}

}
