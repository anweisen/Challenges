package net.codingarea.challenges.plugin.spigot.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public class PlayerJumpEvent extends PlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	public PlayerJumpEvent(@Nonnull Player who) {
		super(who);
	}

	@Nonnull
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Nonnull
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
