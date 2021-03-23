package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.challenges.type.Modifier;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.MaterialWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class DifficultySetting extends Modifier {

	public DifficultySetting() {
		super(MenuType.SETTINGS, 0, 3);
		setValue(getCurrentDifficulty().ordinal());
	}

	@Override
	protected void onValueChange() {
		setDifficulty(getDifficultyByValue(getValue()));
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
			case 0:     return DefaultItem.create(Material.LIME_DYE, "§aPeaceful");
			case 1:     return DefaultItem.create(MaterialWrapper.GREEN_DYE, "§2Easy");
			case 2:     return DefaultItem.create(Material.ORANGE_DYE, "§6Normal");
			default:    return DefaultItem.create(MaterialWrapper.RED_DYE, "§cHard");
		}
	}

	private String getCurrentDifficultyName() {
		switch (getValue()) {
			case 0:     return "§aPeaceful";
			case 1:     return "§2Easy";
			case 2:     return "§6Normal";
			default:    return "§cHard";
		}
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChangeChallengeValueTitle(this, getCurrentDifficultyName());
	}

	private void setDifficulty(Difficulty difficulty) {
		for (World world : Bukkit.getWorlds()) {
			world.setDifficulty(difficulty);
		}
	}

	@Nonnull
	private Difficulty getCurrentDifficulty() {
		return Bukkit.getWorlds().isEmpty() ? Difficulty.NORMAL : Bukkit.getWorlds().get(0).getDifficulty();
	}

	@Nonnull
	private Difficulty getDifficultyByValue(int value) {
		Difficulty difficulty = Difficulty.values()[value];
		return difficulty == null ? Difficulty.NORMAL : difficulty;
	}

	@Override
	public void loadSettings(@Nonnull Document document) {
		if (!document.contains("value"))
			setValue(getCurrentDifficulty().ordinal());

		super.loadSettings(document);
	}

}
