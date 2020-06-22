package net.codingarea.challengesplugin.manager.goal;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Goal;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.utils.Utils;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public class GoalManager {


	private final List<Goal> registeredGoals;

	@Getter private Goal currentGoal;

	public GoalManager() {
		registeredGoals = new ArrayList<>();
	}

	public void setCurrentGoalTo(Goal goal, ChallengeEditEvent event) {

		if (goal == null) throw new NullPointerException("Goal cannot be null!");

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

		if (goal == null) throw new NullPointerException("Goal cannot be null!");

		registeredGoals.add(goal);

	}

	public boolean isActiveGoal(Goal goal) {
		if (goal == null) return false;
		if (!Challenges.timerIsStarted()) return false;
		if (currentGoal == null) return false;
		return currentGoal.equals(goal);

	}

	public List<Goal> getRegisteredGoals() {
		return registeredGoals;
	}

}
