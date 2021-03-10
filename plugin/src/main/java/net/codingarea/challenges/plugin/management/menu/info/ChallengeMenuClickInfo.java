package net.codingarea.challenges.plugin.management.menu.info;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class ChallengeMenuClickInfo extends MenuClickInfo {

	protected final boolean upperItem;

	public ChallengeMenuClickInfo(@Nonnull MenuClickInfo parent, boolean upperItem) {
		this(parent.getPlayer(), parent.getInventory(), parent.isRightClick(), parent.isShiftClick(), parent.getSlot(), upperItem);
	}

	public ChallengeMenuClickInfo(@Nonnull Player player, @Nonnull Inventory inventory, boolean rightClick, boolean shiftClick, @Nonnegative int slot, boolean upperItem) {
		super(player, inventory, rightClick, shiftClick, slot);
		this.upperItem = upperItem;
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
