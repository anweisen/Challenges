package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class FoodOnceChallenge extends SettingModifier {

	private final Map<UUID, List<Material>> playerFoods = new HashMap<>();
	private final List<Material> teamFoods = new ArrayList<>();

	public FoodOnceChallenge() {
		super(MenuType.CHALLENGES, 2);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.COOKED_BEEF, Message.forName("item-food-once-challenge"));
	}

	@Override
	public void loadGameState(@Nonnull Document document) {
		List<String> list = document.getStringList("team-foods");


	}

	@Override
	public void writeGameState(@Nonnull Document document) {
		document.set("team-foods", teamFoods);

		
		for (Entry<UUID, List<Material>> entry : playerFoods.entrySet()) {
			document.set("player-foods." + entry.getKey(), entry.getValue());
		}
	}

	@Nonnull
	@Override
	public ItemBuilder createSettingsItem() {
		switch (getValue()) {
			case 1: return DefaultItem.create(Material.PLAYER_HEAD, "§6Player");
			case 2: return DefaultItem.create(Material.ENDER_CHEST, "§5Everyone");
			default: return super.createSettingsItem();
		}

	}

}