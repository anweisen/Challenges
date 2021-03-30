package net.codingarea.challenges.plugin.spigot.listener;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.management.menu.MenuManager;
import net.codingarea.challenges.plugin.management.menu.MenuPosition;
import net.codingarea.challenges.plugin.management.menu.info.MenuClickInfo;
import net.codingarea.challenges.plugin.spigot.events.PlayerInventoryClickEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class InventoryListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onClick(@Nonnull InventoryClickEvent event) {

		HumanEntity human = event.getWhoClicked();
		if (!(human instanceof Player)) return;
		Player player = (Player) human;

		Inventory inventory = event.getClickedInventory();
		if (inventory == null) return;
		if (inventory != event.getView().getTopInventory()) return; // Player inventory was clicked
		if (inventory.getHolder() != MenuPosition.HOLDER) return; // No accepted inventory

		MenuPosition position = Challenges.getInstance().getMenuManager().getPosition(player);
		if (position == null) return; // Currently in no menu

		event.setCancelled(true);
		position.handleClick(new MenuClickInfo(player, inventory, event.isShiftClick(), event.isRightClick(), event.getSlot()));

	}

}
