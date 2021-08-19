package net.codingarea.challenges.plugin.management.menu.generator.implementation.custom;

import net.codingarea.challenges.plugin.challenges.custom.api.ChallengeCondition;
import net.codingarea.challenges.plugin.management.menu.generator.ChooseItemGenerator;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class ConditionMenuGenerator extends ChooseItemGenerator implements IParentCustomGenerator {

	private final IParentCustomGenerator parent;
	private ChallengeCondition condition;

	public ConditionMenuGenerator(IParentCustomGenerator parent) {
		super(getConditionItems());
		this.parent = parent;
	}

	@Override
	public String[] getSubTitles(int page) {
		return new String[]{ "Create" };
	}

	@Override
	public int[] getNavigationSlots(int page) {
		return MainMenuGenerator.NAVIGATION_SLOTS;
	}

	public IParentCustomGenerator getParent() {
		return parent;
	}

	@Override
	public void accept(Player player, String... data) {
		if (data.length > 0) {
			ArrayList<String> list = new ArrayList<String>(Arrays.asList("condition", condition.name()));
			list.addAll(Arrays.asList(data));
			getParent().accept(player, list.toArray(new String[0]));
		} else {
			getParent().accept(player, "condition", condition.name());
		}
	}

	@Override
	public void onItemClick(Player player, String key) {
		this.condition = ChallengeCondition.valueOf(key);

		if (condition.getSubSettingsBuilder().hasSettings()) {
			ConditionSubMenuGenerator generator = new ConditionSubMenuGenerator(this, condition);
			generator.open(player, 0);

		} else {
			parent.accept(player, "condition", condition.name());
		}

	}

	@Override
	public void onBackToMenuItemClick(Player player) {
		parent.decline(player);
	}

	@Override
	public void decline(Player player) {
		open(player, 0);
	}

	public static Map<String, ItemStack> getConditionItems() {
		TreeMap<String, ItemStack> map = new TreeMap<>();

		for (ChallengeCondition value : ChallengeCondition.values()) {
			map.put(value.name(), new ItemBuilder(value.getMaterial(), value.getMessage()).build());
		}

		return map;
	}

}
