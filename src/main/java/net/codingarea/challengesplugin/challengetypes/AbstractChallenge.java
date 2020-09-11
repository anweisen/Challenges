package net.codingarea.challengesplugin.challengetypes;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public abstract class AbstractChallenge {

	private final MenuType menu;

	public AbstractChallenge(MenuType menu) {
		this.menu = menu;
	}

	public abstract void handleClick(ChallengeEditEvent event);

	@NotNull
	public abstract ItemStack getItem();

	@NotNull
	public abstract ItemStack getActivationItem();

	public abstract void setValues(int value);
	public abstract int toValue();

	@NotNull
	public String getChallengeName() {
		return this.getClass().getSimpleName().toLowerCase().replace("setting", "").replace("challenge", "").replace("modifier", "");
	}

	public ItemStack getItemToShow() {
		return getItem();
	}

	public final void updateItem(Inventory inventory, int item) {
		inventory.setItem(item, getItemToShow());
		inventory.setItem(item + 9, getActivationItem());
	}

	public final ItemStack getNonAmountedItem() {
		try {
			ItemStack item = getItem();
			item.setAmount(1);
			return item;
		} catch (NullPointerException ignored) {
			return null;
		}
	}

	public void handleTimerSecond() { };

	public final MenuType getMenu() {
		return menu;
	}

	public final String getDataFile(String fileType) {
		return Challenges.getInstance().getChallengeManager().getSettingsFolder() + getChallengeName() + "." + fileType.toLowerCase();
	}

	@Override
	public final boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof AbstractChallenge) {
			AbstractChallenge other = (AbstractChallenge) o;
			return other.getChallengeName().equals(this.getChallengeName());
		} else {
			return false;
		}
	}

}
