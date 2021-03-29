package net.codingarea.challenges.plugin.spigot.events;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class InnerInventoryClickEvent extends Event {

	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private final InventoryClickEvent event;

	public InnerInventoryClickEvent(InventoryClickEvent event) {
		this.event = event;
	}

	public Inventory getClickedInventory() {
		return event.getClickedInventory();
	}

	public InventoryView getView() {
		return event.getView();
	}

	public ClickType getClick() {
		return event.getClick();
	}

	public HumanEntity getWhoClicked() {
		return event.getWhoClicked();
	}

	public int getSlot() {
		return event.getSlot();
	}

	public int getRawSlot() {
		return event.getRawSlot();
	}

	public InventoryAction getAction() {
		return event.getAction();
	}

	public ItemStack getCursor() {
		return event.getCursor();
	}

	public int getHotbarButton() {
		return event.getHotbarButton();
	}

	public SlotType getSlotType() {
		return event.getSlotType();
	}

	public Result getResult() {
		return event.getResult();
	}

	public ItemStack getCurrentItem() {
		return event.getCurrentItem();
	}

	public List<HumanEntity> getViewers() {
		return event.getViewers();
	}

	public void setCancelled(boolean cancel) {
		event.setCancelled(cancel);
	}

	public void setResult(Result result) {
		event.setResult(result);
	}

	public void setCurrentItem(ItemStack item) {
		event.setCurrentItem(item);
	}

	public InventoryClickEvent getEvent() {
		return event;
	}

	public boolean isCancelled() {
		return event.isCancelled();
	}

	public boolean isRightClick() {
		return event.isRightClick();
	}

	public boolean isLeftClick() {
		return event.isLeftClick();
	}
	public boolean isShiftClick() {
		return event.isShiftClick();
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}

}