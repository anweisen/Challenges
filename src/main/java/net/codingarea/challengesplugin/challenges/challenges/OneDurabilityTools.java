package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen
 * Challenges developed on 06-23-2020
 * https://github.com/anweisen
 */

public class OneDurabilityTools extends Setting implements Listener {

	public OneDurabilityTools() {
		super(MenuType.CHALLENGES);
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		Utils.forEachPlayerOnline((currentPlayer) -> {
			for (ItemStack currentItem : currentPlayer.getInventory().getContents()) {
				setDurability(currentItem);
			}
			currentPlayer.updateInventory();
		});
	}

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.WOODEN_PICKAXE, ItemTranslation.ITEM_ONE_TIME_USE).setDamage((short) 60).hideAttributes().build();
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!enabled || !Challenges.timerIsStarted()) return;
		if (event.getView().getTitle().contains(Challenges.getInstance().getStringManager().MENU_TITLE)) return;
		setDurability(event.getCurrentItem());
		setDurability(event.getCursor());
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (!enabled || !Challenges.timerIsStarted()) return;
		setDurability(event.getItem());
	}

	private void setDurability(ItemStack item) {
		if (item == null) return;
		item.setDurability((short) (item.getType().getMaxDurability() + 1));
	}

}
