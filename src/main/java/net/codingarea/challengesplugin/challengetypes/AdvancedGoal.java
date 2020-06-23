package net.codingarea.challengesplugin.challengetypes;

import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.menu.MenuClickHandler;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public abstract class AdvancedGoal extends Goal {

	protected int value = 1;
	protected int maxValue = 2;
	protected int minValue = 1;

	protected int countUp = 1;

	public abstract void onValueChange(ChallengeEditEvent event);

	@Override
	public void handleClick(ChallengeEditEvent event) {

		if (MenuClickHandler.shouldChangeGoalValue(event.getClickResult())) {

			if (event.wasRightClick()) {
				if ((value - countUp) >= minValue) {
					value -= countUp;
				} else {
					value = maxValue;
				}
			} else {
				if (!((value + countUp) > maxValue)) {
					value += countUp;
				} else {
					value = minValue;
				}
			}

			onValueChange(event);

		}

	}

	@Override
	public ItemStack getItemToShow() {
		ItemStack item = getItem();
		item.setAmount(isCurrentGoal ? value : 1);
		return item;
	}

	@Override
	public void setIsCurrentGoal(boolean currentGoal, ChallengeEditEvent event) {

		if (isCurrentGoal && !currentGoal || !isCurrentGoal && currentGoal) {
			value = minValue;
		}

		super.setIsCurrentGoal(currentGoal, event);

	}
}
