package net.codingarea.challengesplugin.manager.goal;

import net.codingarea.challengesplugin.challengetypes.Goal;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class GoalManager {

	private final List<Goal> registeredGoals = new ArrayList<>();
	private Goal currentGoal;

	public void setCurrentGoalTo(Goal goal, ChallengeEditEvent event) {

		if (goal.getSound() != null) goal.getSound().play(event.getPlayer());
		if (goal.isCurrentGoal()) {
			goal.setIsCurrentGoal(false, event);
			return;
		}

		for (Goal currentGoal : registeredGoals) {
			currentGoal.setIsCurrentGoal(currentGoal.equals(goal), event);
			updateItem(event.getClickEvent().getClickedInventory(), currentGoal);
		}

		currentGoal = goal;

	}

	// FIXME: 29.07.2020 makes not sense at all?
	private void updateItem(Inventory inventory, Goal goal) {

		if (goal == null) return;
		if (goal.getItem() == null) return;
		if (inventory == null) return;

		List<ItemStack> list = Arrays.asList(inventory.getContents());

		list.forEach(all -> all.setAmount(1));

		int location = Utils.getLocationInList(goal.getItem(), list);

		if (location == -1) return;

		goal.updateItem(inventory, location);

	}

	public void addGoal(Goal goal) {
		registeredGoals.add(goal);
	}

	public List<Goal> getRegisteredGoals() {
		return registeredGoals;
	}

	public Goal getCurrentGoal() {
		return currentGoal;
	}

}
