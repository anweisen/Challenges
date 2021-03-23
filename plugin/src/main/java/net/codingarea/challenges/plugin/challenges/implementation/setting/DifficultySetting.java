package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.challenges.type.Modifier;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.MaterialWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class DifficultySetting extends Modifier {

	public DifficultySetting() {
		super(MenuType.SETTINGS, 0, 3);
	}

	@Override
	protected void onValueChange() {
		setDifficulty(getDifficultyByValue(getValue()));
	}

	@Override
	public void loadGameState(@Nonnull Document document) {
		if (document.contains("difficulty")) {
			setValue(getDifficulty().getValue());
			return;
		}

		setValue(document.getInt("difficulty"));
		Difficulty difficulty = getDifficultyByValue(getValue());
		setDifficulty(difficulty);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.GLISTERING_MELON_SLICE, Message.forName("item-difficulty-setting"));
	}

	@Nonnull
	@Override
	public ItemBuilder createSettingsItem() {
		switch (getValue()) {
			case 0: return new ItemBuilder(Material.LIME_DYE, "§aPeaceful");
			case 1: return new ItemBuilder(MaterialWrapper.GREEN_DYE, "§2Easy");
			case 2: return new ItemBuilder(Material.ORANGE_DYE, "§6Normal");
			default: return new ItemBuilder(MaterialWrapper.RED_DYE, "§cHard");
		}
	}

	private void setDifficulty(Difficulty difficulty) {
		for (World world : Bukkit.getWorlds()) {
			world.setDifficulty(difficulty);
		}
	}

	private Difficulty getDifficulty() {
		return Bukkit.getWorlds().get(0).getDifficulty();
	}

	private Difficulty getDifficultyByValue(int value) {
		Difficulty difficulty = Difficulty.getByValue(value);
		if (difficulty == null) difficulty = Difficulty.NORMAL;
		return difficulty;
	}

}