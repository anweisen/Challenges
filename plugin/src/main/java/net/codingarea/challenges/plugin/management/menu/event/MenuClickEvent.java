package net.codingarea.challenges.plugin.management.menu.event;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class MenuClickEvent {

	private final Player player;
	private final boolean rightClick, shiftClick, upperItem;
	private final Inventory inventory;

	public MenuClickEvent(@Nonnull Player player, @Nonnull Inventory inventory, boolean rightClick, boolean shiftClick, boolean upperItem) {
		this.player = player;
		this.inventory = inventory;
		this.rightClick = rightClick;
		this.shiftClick = shiftClick;
		this.upperItem = upperItem;
	}

	public boolean isRightClick() {
		return rightClick;
	}

	public boolean isLeftClick() {
		return !rightClick;
	}

	public boolean isShiftClick() {
		return shiftClick;
	}

	public boolean isUpperItemClick() {
		return upperItem;
	}

	public boolean isLowerItemClick() {
		return !upperItem;
	}

	@Nonnull
	public Player getPlayer() {
		return player;
	}

	@Nonnull
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public String toString() {
		return "MenuClickEvent{" +
				"rightClick=" + rightClick +
				", shiftClick=" + shiftClick +
				", upperItem=" + upperItem +
				'}';
	}

}
