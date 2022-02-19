package net.codingarea.challenges.plugin.management.menu.generator.implementation.custom;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.bukkit.utils.menu.MenuClickInfo;
import net.anweisen.utilities.bukkit.utils.menu.MenuPosition;
import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.CustomChallenge;
import net.codingarea.challenges.plugin.challenges.custom.settings.ChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.ChallengeCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.SettingType;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.management.menu.InventoryTitleManager;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.generator.MenuGenerator;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils.InventorySetter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class InfoMenuGenerator extends MenuGenerator implements IParentCustomGenerator {

	public static final int DELETE_SLOT = 19+9, SAVE_SLOT = 25+9, CONDITION_SLOT = 21+9, ACTION_SLOT = 23+9, MATERIAL_SLOT = 14, NAME_SLOT = 12;

	private static final boolean 	savePlayerConfigs;

	private final UUID uuid;
	private String name;
	private Material material;
	private ChallengeCondition condition;
	private Map<String, String[]> subConditions;
	private ChallengeAction action;
	private Map<String, String[]> subActions;
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
		this.subConditions = new HashMap<>();
		this.subActions = new HashMap<>();
		this.uuid = UUID.randomUUID();
		this.name = "§7Custom §e#" +
				(Challenges.getInstance().getCustomChallengesLoader().getCustomChallenges().size()+1);
		this.inNaming = false;
	}

	@Override
	public void generateInventories() {
		inventory = Bukkit.createInventory(MenuPosition.HOLDER, 6*9, InventoryTitleManager.getTitle(MenuType.CUSTOM, "Create"));
		InventoryUtils.fillInventory(inventory, ItemBuilder.FILL_ITEM);

		updateItems();

		InventoryUtils.setNavigationItems(inventory, new int[]{36+9}, true, InventorySetter.INVENTORY, 0, 1);
	}

	public void updateItems() {
		String currently = Message.forName("custom-info-currently").asString();
		String none = Message.forName("custom-info-none").asString();

		// Save / Delete Item
		inventory.setItem(DELETE_SLOT, new ItemBuilder(Material.BARRIER, Message.forName("item-custom-info-delete")).build());
		inventory.setItem(SAVE_SLOT, new ItemBuilder(Material.LIME_DYE, Message.forName("item-custom-info-save")).build());

		// Condition Item
		ItemBuilder conditionItem = new ItemBuilder(Material.WITHER_SKELETON_SKULL,
				Message.forName("item-custom-info-condition"))
				.appendLore(
						currently + (condition != null ? Message.forName(condition.getMessage()) : none));
		if (condition != null) {
			conditionItem.appendLore(getSubSettingsDisplay(condition.getSubSettingsBuilder(), subConditions));
		}
		inventory.setItem(CONDITION_SLOT, conditionItem.build());

		// Action Item
		ItemBuilder actionItem = new ItemBuilder(Material.NETHER_STAR,
				Message.forName("item-custom-info-action"))
				.appendLore(currently + (action != null ? Message.forName(action.getMessage()) : none));
		if (action != null) {
			actionItem.appendLore(getSubSettingsDisplay(action.getSubSettingsBuilder(), subActions));
		}
		inventory.setItem(ACTION_SLOT, actionItem.build());

		// Display Item
		inventory.setItem(MATERIAL_SLOT, new ItemBuilder(material == null ? Material.BARRIER : material, Message.forName("item-custom-info-material"))
				.appendLore(currently + (material != null ? StringUtils.getEnumName(material) : none)).build());

		// Name Item
		inventory.setItem(NAME_SLOT, new ItemBuilder(Material.NAME_TAG, Message.forName("item-custom-info-name"))
				.appendLore(currently + "§7" + name).build());
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
	public void accept(Player player, SettingType type, Map<String, String[]> data) {
		open(player, 0);

		switch (type) {
			case CONDITION:
				condition = ChallengeCondition.valueOf(data.remove("condition")[0]);
				this.subConditions = data;
				break;

			case ACTION:
				action = ChallengeAction.valueOf(data.remove("action")[0]);
				this.subActions = data;
				break;

			case MATERIAL:
				material = Material.valueOf(data.remove("material")[0]);
				updateItems();
				break;
		}

		updateItems();
	}

	public void setName(String name) {
		this.name = name;
		updateItems();
	}

	@Override
	public void decline(Player player) {
		open(player, 0);
	}

	public CustomChallenge save() {
		return Challenges.getInstance().getCustomChallengesLoader().registerCustomChallenge(uuid, material, name, condition, subConditions, action, subActions, true);
	}

	public boolean isInNaming() {
		return inNaming;
	}

	public void setInNaming(boolean inNaming) {
		this.inNaming = inNaming;
	}

	@Override
	public String toString() {
		return "InfoMenuGenerator{" +
				"name='" + name + '\'' +
				", material=" + material +
				", condition=" + condition +
				", subConditions=" + subConditions.entrySet() +
				", action=" + action +
				", subActions=" + subActions.entrySet() +
				", inventory=" + inventory +
				", inNaming=" + inNaming +
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
			if (InventoryUtils.handleNavigationClicking(generator, new int[]{36+9}, page, info, () -> 				Challenges.getInstance().getMenuManager().openMenu(info.getPlayer(), MenuType.CUSTOM, 0))) {
				return;
			}

			Player player = info.getPlayer();

			switch (info.getSlot()) {
				default:
					SoundSample.CLICK.play(player);
					break;
				case DELETE_SLOT:
					if (!Challenges.getInstance().getCustomChallengesLoader().unregisterCustomChallenge(uuid)) {
						Message.forName("custom-not-deleted").send(player, Prefix.CUSTOM);
						SoundSample.BASS_OFF.play(player);
						break;
					}
					Challenges.getInstance().getMenuManager().openMenu(player, MenuType.CUSTOM, 0);
					new SoundSample().addSound(Sound.ENTITY_WITHER_BREAK_BLOCK, 0.4f).play(player);
					break;
				case SAVE_SLOT:

					if (new InfoMenuGenerator().toString().equals(InfoMenuGenerator.this.toString())) {
						Message.forName("custom-no-changes").send(player, Prefix.CUSTOM);
						SoundSample.BASS_OFF.play(player);
						return;
					}

					save();
					Challenges.getInstance().getMenuManager().openMenu(player, MenuType.CUSTOM, 0);
					Message.forName("custom-saved").send(player, Prefix.CUSTOM);
					if (savePlayerConfigs) {
						Message.forName("custom-saved-db").send(player, Prefix.CUSTOM);
					}
					SoundSample.LEVEL_UP.play(player);
					break;
				case CONDITION_SLOT:
					new CustomMainSettingMenuGenerator(generator, SettingType.CONDITION, "condition", "Condition", ChallengeCondition.PLAYER_JUMP.getMenuItems(), ChallengeCondition::valueOf).open(player, 0);
					SoundSample.CLICK.play(player);
					break;
				case ACTION_SLOT:
					new CustomMainSettingMenuGenerator(generator, SettingType.ACTION, "action", "Action", ChallengeAction.DAMAGE.getMenuItems(), ChallengeAction::valueOf).open(player, 0);
					SoundSample.CLICK.play(player);
					break;
				case NAME_SLOT:
					Message.forName("custom-name-info").send(player, Prefix.CUSTOM);
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

	static {
		savePlayerConfigs = Challenges.getInstance().getConfigDocument().getBoolean("save-player-configs");
	}

	public static List<String> getSubSettingsDisplay(SubSettingsBuilder builder, Map<String, String[]> activated) {
		List<String> display = new LinkedList<>();
		for (SubSettingsBuilder child : builder.getAllChilds()) {
			display.addAll(child.getDisplay(activated));
		}
		return display;
	}

}
