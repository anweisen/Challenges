package net.codingarea.challengesplugin.manager.menu;

import net.codingarea.challengesplugin.challengetypes.AdvancedGoal;
import net.codingarea.challengesplugin.challengetypes.GeneralChallenge;
import net.codingarea.challengesplugin.challengetypes.Goal;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import static net.codingarea.challengesplugin.manager.menu.MenuClickHandler.ClickResult.NOTHING_FOUND_RESULT;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public class MenuClickHandler {

	public static class ClickResult {

		public static final ClickResult NOTHING_FOUND_RESULT = new ClickResult(null, -1, -1);

		private GeneralChallenge challenge;
		private int challengeSlot;
		private int clickedSlot;

		public ClickResult(GeneralChallenge challenge, int challengeSlot, int clickedSlot) {
			this.challengeSlot = challengeSlot;
			this.clickedSlot = clickedSlot;
			this.challenge = challenge;
		}

		public GeneralChallenge getChallenge() {
			return challenge;
		}

		public int getChallengeSlot() {
			return challengeSlot;
		}

		public int getClickedSlot() {
			return clickedSlot;
		}
	}


	private MenuManager manager;

	public MenuClickHandler(MenuManager manager) {
		this.manager = manager;
	}

	/**
	 * @return returns a click result with the slot clicked and the challenge on this slot,
	 * the challenge wil be null when no challenge on this slot was found.
	 */
	public ClickResult getClickResult(InventoryClickEvent clickEvent, int menu) {

		if (clickEvent == null) return NOTHING_FOUND_RESULT;
		if (clickEvent.getClickedInventory() == null) return NOTHING_FOUND_RESULT;
		if (clickEvent.getCurrentItem() == null) return NOTHING_FOUND_RESULT;

		ItemStack item = clickEvent.getCurrentItem().clone();
		item.setAmount(1);

		GeneralChallenge challenge = manager.getPlugin().getChallengeManager().getChallengeByItem(item);

		if (challenge != null) return new ClickResult(challenge, clickEvent.getSlot(), clickEvent.getSlot());

		// This will be executed if no challenge was clicked, it will look if a challenge is above the clicked item

		if (clickEvent.getSlot() < 9 || clickEvent.getClickedInventory().getItem(clickEvent.getSlot() - 9) == null) return NOTHING_FOUND_RESULT;

		Inventory inventory = clickEvent.getClickedInventory();
		item = inventory.getItem(clickEvent.getSlot() - 9).clone();
		item.setAmount(1);

		challenge = manager.getPlugin().getChallengeManager().getChallengeByItem(item);
		return new ClickResult(challenge, clickEvent.getSlot() - 9, clickEvent.getSlot());

	}

	public boolean handle(InventoryClickEvent clickEvent, int menuPage, MenuType menuType) {

		if (menuType == null) return false;
		if (clickEvent == null) return false;
		if (!(clickEvent.getWhoClicked() instanceof Player)) return false;
		if (clickEvent.getClickedInventory() == null) return false;
		if (clickEvent.getCurrentItem() == null) return false;
		Player player = (Player) clickEvent.getWhoClicked();

		byte v = 0;
		if (clickEvent.getCurrentItem().getType() == manager.getPlugin().getChallengeManager().getPlugin().getItemManager().getBackMainMenuItem().getType()) {
			manager.getMainMenu().openLastFrame(player, true);
			return false;
		} else {
			if (clickEvent.getCurrentItem().getType() == Material.PLAYER_HEAD) {
				SkullMeta meta = (SkullMeta) clickEvent.getCurrentItem().getItemMeta();
				if (meta != null && meta.getOwner() != null ) {
					if (meta.getOwner().equals("MHF_ArrowRight")) {
						v = 1;
					} else {
						v = 2;
					}
				}
			}
		}

		if (v != 0) {
			player.openInventory(manager.getMenus().get(menuType.getPageID()).get(v == 1 ? menuPage + 1 : menuPage - 1));
			return false;
		}

		ClickResult result = getClickResult(clickEvent, menuPage);
		GeneralChallenge challenge = result.getChallenge();

		if (challenge == null) return false;

		ChallengeEditEvent event = new ChallengeEditEvent(player, result, clickEvent);

		if (challenge instanceof Goal && !(challenge instanceof AdvancedGoal && shouldChangeGoalValue(result)) || challenge instanceof AdvancedGoal && !((Goal)challenge).isCurrentGoal()) {
			manager.getPlugin().getChallengeManager().getGoalManager().setCurrentGoalTo((Goal) challenge, event);
			if (challenge instanceof AdvancedGoal) {
				((AdvancedGoal)challenge).onValueChange(event);
			}
		} else {
			challenge.handleClick(event);
		}

		challenge.updateItem(clickEvent.getClickedInventory(), result.getChallengeSlot());
		return true;
	}

	public int getPageByInventoryTitle(String title) {

		try {
			String[] array = Utils.getStringWithoutColorCodes(title).split(" ");
			return Integer.parseInt(array[array.length - 1]);
		} catch (NumberFormatException ex) {
			return -1;
		}

	}

	public static boolean shouldChangeGoalValue(ClickResult result) {
		if (result == null) return false;
		return result.getClickedSlot() == result.getChallengeSlot();
	}

}
