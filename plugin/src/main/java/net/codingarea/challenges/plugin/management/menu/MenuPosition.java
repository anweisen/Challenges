package net.codingarea.challenges.plugin.management.menu;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.management.menu.info.MenuClickInfo;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@FunctionalInterface
public interface MenuPosition {

	InventoryHolder HOLDER = () -> null;

	void handleClick(@Nonnull MenuClickInfo info);

	static void set(@Nonnull Player player, @Nullable MenuPosition position) {
		Challenges.getInstance().getMenuManager().setPostion(player, position);
	}

	@Nullable
	static MenuPosition get(@Nonnull Player player) {
		return Challenges.getInstance().getMenuManager().getPosition(player);
	}

	class EmptyMenuPosition implements MenuPosition {

		@Override
		public void handleClick(@Nonnull MenuClickInfo info) {
			SoundSample.CLICK.play(info.getPlayer());
		}

	}

}
