package net.codingarea.challenges.plugin.management.menu.generator.implementation.custom;

import com.google.common.collect.Lists;
import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.bukkit.utils.menu.MenuPosition;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.CustomChallenge;
import net.codingarea.challenges.plugin.challenges.custom.api.ChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.api.ChallengeCondition;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.management.menu.InventoryTitleManager;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.generator.MenuGenerator;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils.InventorySetter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class InfoMenuGenerator extends MenuGenerator implements IParentCustomGenerator {

	public static final int DELETE_SLOT = 19;
	public static final int SAVE_SLOT = 25;
	public static final int CONDITION_SLOT = 21;
	public static final int ACTION_SLOT = 23;
	private ChallengeCondition condition;
	private String[] subConditions;
	private ChallengeAction action;
	private String[] subActions;
	private UUID uuid;
	private Inventory inventory;

	public InfoMenuGenerator(ChallengeCondition condition, ChallengeAction action, UUID uuid, String... subCondition) {
		this.condition = condition;
		this.subConditions = subCondition;
		this.action = action;
		this.uuid = uuid;
	}

	public InfoMenuGenerator() {
		this.condition = null;
		this.action = null;
		this.subConditions = new String[0];
		this.uuid = UUID.randomUUID();
	}

	@Override
	public void generateInventories() {
		inventory = Bukkit.createInventory(MenuPosition.HOLDER, 5*9, InventoryTitleManager.getTitle(MenuType.CUSTOM, "Create"));
		InventoryUtils.fillInventory(inventory, ItemBuilder.FILL_ITEM);

		inventory.setItem(DELETE_SLOT, new ItemBuilder(Material.BARRIER, "§cDelete").build());
		inventory.setItem(SAVE_SLOT, new ItemBuilder(Material.LIME_DYE, "§aSave").build());
		inventory.setItem(CONDITION_SLOT, new ItemBuilder(Material.WITHER_SKELETON_SKULL, "§6Condition").build());
		inventory.setItem(ACTION_SLOT, new ItemBuilder(Material.NETHER_STAR, "§cAction").build());

		InventoryUtils.setNavigationItems(inventory, new int[]{36}, true, InventorySetter.INVENTORY, 0, 1);
	}

	@Override
	public List<Inventory> getInventories() {
		return Collections.singletonList(inventory);
	}

	@Override
	public MenuPosition getMenuPosition(int page) {
		return info -> {
			if (InventoryUtils.handleNavigationClicking(this, new int[]{36}, page, info)) {
				Challenges.getInstance().getMenuManager().openMenu(info.getPlayer(), MenuType.CUSTOM, 0);
				return;
			}

			Player player = info.getPlayer();

			switch (info.getSlot()) {
				default:
					SoundSample.CLICK.play(player);
					break;
				case DELETE_SLOT:
					Challenges.getInstance().getMenuManager().openMenu(player, MenuType.CUSTOM, 0);
					SoundSample.BREAK.play(player);
					break;
				case SAVE_SLOT:
					save();
					SoundSample.LEVEL_UP.play(player);
					break;
				case CONDITION_SLOT:
					ConditionMenuGenerator conditionMenuGenerator = new ConditionMenuGenerator(this);
					conditionMenuGenerator.open(player, 0);
					SoundSample.CLICK.play(player);
					break;
				case ACTION_SLOT:
					ActionMenuGenerator actionMenuGenerator = new ActionMenuGenerator(this);
					actionMenuGenerator.open(player, 0);
					SoundSample.CLICK.play(player);
					break;
			}
		};
	}

	@Override
	public void open(@Nonnull Player player, int page) {
		if (inventory == null) generateInventories();
		super.open(player, page);
	}

	@Override
	public void accept(Player player, String... data) {
		open(player, 0);
		if (data.length <= 1) return;

		switch (data[0]) {
			case "condition":
				condition = ChallengeCondition.valueOf(data[1]);

				if (data.length > 2) {
					ArrayList<String> list = Lists.newArrayList();
					for (int i = 2; i < data.length; i++) {
						list.add(data[i]);
					}
					this.subConditions = list.toArray(new String[0]);
				}

				break;
			case "action":
				action = ChallengeAction.valueOf(data[1]);

				if (data.length > 2) {
					ArrayList<String> list = Lists.newArrayList();
					for (int i = 2; i < data.length; i++) {
						list.add(data[i]);
					}
					this.subActions = list.toArray(new String[0]);
				}

				break;
		}
	}

	@Override
	public void decline(Player player) {
		open(player, 0);
	}

	public void save() {
		Bukkit.broadcastMessage("Save: " + " (Condition: " + condition + ", SubCondition: " + Arrays.toString(subConditions) + ") (Action: " + action + ", SubAction: " + Arrays.toString(subActions) + ")");
		CustomChallenge challenge = Challenges.getInstance().getCustomChallengesLoader().registerCustomChallenge(Material.BARRIER, condition, subConditions, action, subActions);
		challenge.setEnabled(true);
		ChallengeHelper.updateItems(challenge);
	}

}
