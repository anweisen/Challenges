package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import javax.annotation.Nonnull;
import net.anweisen.utilities.common.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.abstraction.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.spigot.events.PlayerInventoryClickEvent;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class PermanentItemChallenge extends Setting {

	public PermanentItemChallenge() {
		super(MenuType.CHALLENGES);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.VINE, Message.forName("item-permanent-item-challenge"));
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryClick(@Nonnull PlayerInventoryClickEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getPlayer())) return;
		Inventory clickedInventory = event.getClickedInventory();
		if (event.getCursor() == null) return;
		if (clickedInventory == null) return;
		InventoryType type = event.getPlayer().getOpenInventory().getTopInventory().getType();
		if (type == InventoryType.WORKBENCH || type == InventoryType.CRAFTING) return;
		if (clickedInventory.getType() == InventoryType.CRAFTING) return;
		if (clickedInventory.getType() == InventoryType.PLAYER) {
			if (event.getInventory().getType() != InventoryType.PLAYER) {
				event.setCancelled(true);
			}
		}

	}

	@EventHandler
	public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
		if (!shouldExecuteEffect()) return;
		event.setCancelled(true);
	}

}