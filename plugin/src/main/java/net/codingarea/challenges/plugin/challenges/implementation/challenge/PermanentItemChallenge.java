package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.spigot.events.PlayerInventoryClickEvent;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;

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
		if (!isEnabled()) return;
		if (ChallengeAPI.isWorldInUse()) return;
		if (ignorePlayer(event.getPlayer())) return;
		Inventory clickedInventory = event.getClickedInventory();
		if (event.getCursor() == null) return;
		if (clickedInventory == null) return;
		if (clickedInventory.getType() == InventoryType.PLAYER || clickedInventory.getType() == InventoryType.CRAFTING) {
			if (event.getInventory().getType()!=InventoryType.PLAYER && event.getInventory().getType()!=InventoryType.CRAFTING) {
				event.setCancelled(true);
			}
		}

	}

	@EventHandler
	public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
		if (!isEnabled()) return;
		if (ChallengeAPI.isWorldInUse()) return;
		event.setCancelled(true);
	}

}