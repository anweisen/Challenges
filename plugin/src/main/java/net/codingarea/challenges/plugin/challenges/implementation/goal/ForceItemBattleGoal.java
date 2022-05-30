package net.codingarea.challenges.plugin.challenges.implementation.goal;

import net.anweisen.utilities.bukkit.utils.item.ItemUtils;
import net.anweisen.utilities.common.annotations.Since;
import net.anweisen.utilities.common.config.Document;
import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.challenges.type.abstraction.ForceBattleGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.spigot.events.PlayerInventoryClickEvent;
import net.codingarea.challenges.plugin.spigot.events.PlayerPickupItemEvent;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.3
 */
@Since("2.1.3")
public class ForceItemBattleGoal extends ForceBattleGoal<Material> {

	public ForceItemBattleGoal() {
		super(MenuType.GOAL, Message.forName("menu-force-item-battle-goal-settings"));

		registerSetting("give-item", new BooleanSubSetting(
				() -> new ItemBuilder(Material.CHEST, Message.forName("item-force-item-battle-goal-give-item")),
				false
		));
	}

	@Override
	protected Material[] getTargetsPossibleToFind() {
		List<Material> materials = new ArrayList<>(Arrays.asList(Material.values()));
		materials.removeIf(material -> !material.isItem());
		materials.removeIf(material -> !ItemUtils.isObtainableInSurvival(material));
		return materials.toArray(new Material[0]);
	}

	@Override
	public Material getTargetFromDocument(Document document, String key) {
		return document.getEnum(key, Material.class);
	}

	@Override
	public List<Material> getListFromDocument(Document document, String key) {
		return document.getEnumList(key, Material.class);
	}

	@Override
	public Message getLeaderboardTitleMessage() {
		return Message.forName("force-item-battle-leaderboard");
	}

	@NotNull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.END_CRYSTAL, Message.forName("item-force-item-battle-goal"));
	}

	@Override
	public String getTargetName(Material target) {
		return StringUtils.getEnumName(target);
	}

	@Override
	protected Message getNewTargetMessage() {
		return Message.forName("force-item-battle-new-item");
	}

	@Override
	protected Message getTargetFoundMessage() {
		return Message.forName("force-item-battle-new-item");
	}

	@Override
	public void handleJokerUse(Player player) {
		super.handleJokerUse(player);
		if(giveItemOnSkip()) {
			InventoryUtils.dropOrGiveItem(player.getInventory(), player.getLocation(), currentTarget.get(player.getUniqueId()));
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onClick(PlayerInventoryClickEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getPlayer())) return;
		if (event.getClickedInventory() == null) return;
		if (event.getCurrentItem() == null) return;
		Material material = currentTarget.get(event.getPlayer().getUniqueId());
		if (material == null) return;
		if (material == event.getCurrentItem().getType()) {
			handleTargetFound(event.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPickup(PlayerPickupItemEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getPlayer())) return;
		Material material = currentTarget.get(event.getPlayer().getUniqueId());
		if (material == null) return;
		if (material == event.getItem().getItemStack().getType()) {
			handleTargetFound(event.getPlayer());
		}
	}

	@Override
	public void handleDisplayStandUpdate(@NotNull Player player, @NotNull ArmorStand armorStand) {
		Material item = currentTarget.get(player.getUniqueId());
		if (item == null) {
			item = Material.AIR;
		}

		ItemStack helmet = armorStand.getEquipment().getHelmet();
		if (helmet == null || helmet.getType() != item) {
			armorStand.getEquipment().setHelmet(new ItemStack(item));
		}
	}

	@Override
	public double getDisplayStandYOffset() {
		return 0;
	}

	private boolean giveItemOnSkip() {
		return getSetting("give-item").getAsBoolean();
	}
}
