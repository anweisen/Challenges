package net.codingarea.challenges.plugin.management.menu.generator.implementation.custom;

import com.google.common.collect.Lists;
import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.bukkit.utils.menu.MenuClickInfo;
import net.anweisen.utilities.bukkit.utils.menu.MenuPosition;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.CustomChallenge;
import net.codingarea.challenges.plugin.challenges.custom.api.ChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.api.ChallengeCondition;
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

	public static final int DELETE_SLOT = 19, SAVE_SLOT = 25, CONDITION_SLOT = 21, ACTION_SLOT = 23, MATERIAL_SLOT = 14, NAME_SLOT = 12;
	private final UUID uuid;
	private String name;
	private Material material;
	private ChallengeCondition condition;
	private String[] subConditions;
	private ChallengeAction action;
	private String[] subActions;
	private Inventory inventory;
	private boolean inNaming;

	public InfoMenuGenerator(CustomChallenge customChallenge) {
		this.uuid = customChallenge.getUniqueId();
		this.material = customChallenge.getMaterial();
		this.name = customChallenge.getDisplayName();
		this.condition = customChallenge.getCondition();
		this.subConditions = customChallenge.getSubConditions();
		this.action = customChallenge.getAction();
		this.subActions = customChallenge.getSubActions();
	}

	public InfoMenuGenerator() {
		this.condition = null;
		this.action = null;
		this.subConditions = new String[0];
		this.uuid = UUID.randomUUID();
		this.inNaming = false;
	}

	@Override
	public void generateInventories() {
		inventory = Bukkit.createInventory(MenuPosition.HOLDER, 5*9, InventoryTitleManager.getTitle(MenuType.CUSTOM, "Create"));
		InventoryUtils.fillInventory(inventory, ItemBuilder.FILL_ITEM);

		inventory.setItem(DELETE_SLOT, new ItemBuilder(Material.BARRIER, "§cDelete").build());
		inventory.setItem(SAVE_SLOT, new ItemBuilder(Material.LIME_DYE, "§aSave").build());
		inventory.setItem(CONDITION_SLOT, new ItemBuilder(Material.WITHER_SKELETON_SKULL, "§6Condition").build());
		inventory.setItem(ACTION_SLOT, new ItemBuilder(Material.NETHER_STAR, "§cAction").build());

		inventory.setItem(MATERIAL_SLOT, new ItemBuilder(Material.BRICKS, "§cMaterial").build());
		inventory.setItem(NAME_SLOT, new ItemBuilder(Material.NAME_TAG, "§6Name Challenge").build());

		InventoryUtils.setNavigationItems(inventory, new int[]{36}, true, InventorySetter.INVENTORY, 0, 1);
	}

	@Override
	public List<Inventory> getInventories() {
		return Collections.singletonList(inventory);
	}

	@Override
	public MenuPosition getMenuPosition(int page) {
		return new InfoMenuPosition(page, this);
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

			case "material":
				material = Material.valueOf(data[1]);
				break;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void decline(Player player) {
		open(player, 0);
	}

	public void save() {
		Bukkit.broadcastMessage(toString());
		Challenges.getInstance().getCustomChallengesLoader().registerCustomChallenge(uuid, material, name, condition, subConditions, action, subActions);
	}

	public boolean isInNaming() {
		return inNaming;
	}

	@Override
	public String toString() {
		return "InfoMenuGenerator{" +
				"uuid=" + uuid +
				", name='" + name + "§r" + '\'' +
				", material=" + material +
				", condition=" + condition +
				", subConditions=" + Arrays.toString(subConditions) +
				", action=" + action +
				", subActions=" + Arrays.toString(subActions) +
				'}';
	}

	public class InfoMenuPosition implements MenuPosition {

		private final int page;
		private final InfoMenuGenerator generator;

		public InfoMenuPosition(int page, InfoMenuGenerator generator) {
			this.page = page;
			this.generator = generator;
		}

		@Override
		public void handleClick(@Nonnull MenuClickInfo info) {
			if (InventoryUtils.handleNavigationClicking(generator, new int[]{36}, page, info)) {
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
					Challenges.getInstance().getCustomChallengesLoader().unregisterCustomChallenge(uuid);
					SoundSample.BREAK.play(player);
					break;
				case SAVE_SLOT:
					save();
					Challenges.getInstance().getMenuManager().openMenu(player, MenuType.CUSTOM, 0);
					SoundSample.LEVEL_UP.play(player);
					break;
				case CONDITION_SLOT:
					ConditionMenuGenerator conditionMenuGenerator = new ConditionMenuGenerator(generator);
					conditionMenuGenerator.open(player, 0);
					SoundSample.CLICK.play(player);
					break;
				case ACTION_SLOT:
					ActionMenuGenerator actionMenuGenerator = new ActionMenuGenerator(generator);
					actionMenuGenerator.open(player, 0);
					SoundSample.CLICK.play(player);
					break;
				case NAME_SLOT:
					inNaming = true;
					player.closeInventory();
					break;
				case MATERIAL_SLOT:
					MaterialMenuGenerator materialMenuGenerator = new MaterialMenuGenerator(generator);
					materialMenuGenerator.open(player, 0);
					SoundSample.CLICK.play(player);
					break;
			}
		}

		public InfoMenuGenerator getGenerator() {
			return generator;
		}
	}

}
