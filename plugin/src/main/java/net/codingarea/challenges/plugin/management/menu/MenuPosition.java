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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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

	class SlottedMenuPosition implements MenuPosition {

		protected final Map<Integer, Consumer<MenuClickInfo>> actions = new HashMap<>();

		@Override
		public void handleClick(@Nonnull MenuClickInfo info) {
			Consumer<MenuClickInfo> action = actions.get(info.getSlot());
			if (action == null) {
				SoundSample.CLICK.play(info.getPlayer());
				return;
			}

			action.accept(info);
		}

		@Nonnull
		public SlottedMenuPosition setPlayerAction(int slot, @Nonnull Consumer<? super Player> action) {
			actions.put(slot, info -> action.accept(info.getPlayer()));
			return this;
		}

		@Nonnull
		public SlottedMenuPosition setAction(int slot, @Nonnull Consumer<? super MenuClickInfo> action) {
			actions.put(slot, info -> action.accept(info));
			return this;
		}

		@Nonnull
		public SlottedMenuPosition setAction(int slot, @Nonnull Runnable action) {
			actions.put(slot, player -> action.run());
			return this;
		}

	}

}
