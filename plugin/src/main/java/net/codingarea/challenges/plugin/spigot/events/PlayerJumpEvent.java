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

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	public PlayerJumpEvent(Player who) {
		super(who);
	}

	@Override
	@Nonnull
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

}