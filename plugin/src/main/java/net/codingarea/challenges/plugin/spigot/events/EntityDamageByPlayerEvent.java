package net.codingarea.challenges.plugin.spigot.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.2
 */
public class EntityDamageByPlayerEvent extends EntityEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Player damager;
	private final double finalDamage;
	private final Cancellable parentEvent;

	public EntityDamageByPlayerEvent(@NotNull Entity victim, Player damager, double finalDamage, Cancellable parent) {
		super(victim);
		this.damager = damager;
		this.finalDamage = finalDamage;
		this.parentEvent = parent;
	}

	public Player getDamager() {
		return damager;
	}

	public double getFinalDamage() {
		return finalDamage;
	}

	@Override
	public boolean isCancelled() {
		return parentEvent.isCancelled();
	}

	@Override
	public void setCancelled(boolean cancel) {
		parentEvent.setCancelled(cancel);
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@NotNull
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
