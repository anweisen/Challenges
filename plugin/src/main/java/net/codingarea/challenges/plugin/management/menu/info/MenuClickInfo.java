package net.codingarea.challenges.plugin.management.menu.info;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class MenuClickInfo {

	protected final Player player;
	protected final Inventory inventory;
	protected final boolean shiftClick;
	protected final boolean rightClick;
	protected final int slot;

	public MenuClickInfo(@Nonnull Player player, @Nonnull Inventory inventory, boolean shiftClick, boolean rightClick, @Nonnegative int slot) {
		this.player = player;
		this.inventory = inventory;
		this.shiftClick = shiftClick;
		this.rightClick = rightClick;
		this.slot = slot;
	}

	@Nonnull
	public Player getPlayer() {
		return player;
	}

	@Nonnull
	public Inventory getInventory() {
		return inventory;
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

	public int getSlot() {
		return slot;
	}

}
