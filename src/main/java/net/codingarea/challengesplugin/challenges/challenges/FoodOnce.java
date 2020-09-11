package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.challengetypes.Modifier;
import net.codingarea.challengesplugin.manager.ItemManager;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author anweisen & KxmischesDomi
 * Challenges developed on 10.08.2020
 * https://www.github.com/anweisen
 * https://www.github.com/KxmischesDomi
 */
public class FoodOnce extends Modifier {

	private final HashMap<UUID, List<Material>> foods = new HashMap<>();
	private final List<Material> teamFoods = new ArrayList<>();

	public FoodOnce() {
		super(MenuType.CHALLENGES, 3);
	}

	@Override
	public void onMenuClick(ChallengeEditEvent event) { }

	@Override
	public @NotNull ItemStack getActivationItem() {
		if (value == 1) {
			return ItemManager.getNotActivatedItem();
		} else if (value == 2) {
			return ItemManager.getTeamItem();
		} else {
			return ItemManager.getPlayerItem();
		}
	}

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.COOKED_BEEF, ItemTranslation.FOOD_ONE).build();
	}

	@EventHandler
	public void onFood(PlayerItemConsumeEvent event) {

		if (value == 1) return;

		Material material = event.getItem().getType();

		List<Material> materials = value == 2 ? teamFoods : foods.getOrDefault(event.getPlayer().getUniqueId(), new ArrayList<>());

		if (value == 3) {
			foods.put(event.getPlayer().getUniqueId(), materials);
		}

		if (materials.contains(material)) {
			event.getPlayer().damage(event.getPlayer().getHealth()*2);
			Bukkit.broadcastMessage(Prefix.CHALLENGES + Translation.FOOD_ONCE_FAIL.get().replace("%player%", event.getPlayer().getName()).replace("%food%", Utils.getEnumName(material)));
		} else {
			materials.add(material);
		}

	}

}
