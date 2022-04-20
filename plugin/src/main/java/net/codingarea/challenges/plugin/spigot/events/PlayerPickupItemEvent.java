package net.codingarea.challenges.plugin.spigot.events;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public class PlayerPickupItemEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Item item;
	private boolean cancel = false;
	private final int remaining;

	public PlayerPickupItemEvent(@Nonnull Player player, @Nonnull Item item, int remaining) {
		super(player);
		this.item = item;
		this.remaining = remaining;
	}

	/**
	 * Gets the Item picked up by the player.
	 *
	 * @return Item
	 */
	@Nonnull
	public Item getItem() {
		return item;
	}

	/**
	 * Gets the amount remaining on the ground, if any
	 *
	 * @return amount remaining on the ground
	 */
	public int getRemaining() {
		return remaining;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
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