package net.codingarea.challenges.plugin.challenges.type.abstraction;

import net.anweisen.utilities.common.collection.IRandom;
import net.anweisen.utilities.common.collection.SeededRandomWrapper;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.management.menu.MenuType;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class RandomizerSetting extends Setting {

	protected IRandom random = IRandom.create();

	public RandomizerSetting(@Nonnull MenuType menu) {
		super(menu);
	}

	public RandomizerSetting(@Nonnull MenuType menu, boolean enabledByDefault) {
		super(menu, enabledByDefault);
	}

	protected abstract void reloadRandomization();

	protected int getMatches(int pairs, int matchesRemaining) {
		if ((pairs * 3) <= matchesRemaining - 3) return 3;
		if ((pairs * 2) <= matchesRemaining - 2) return 2;
		return 1;
	}

	@Override
	protected void onEnable() {
		reloadRandomization();
	}

	@Override
	public void loadGameState(@Nonnull Document document) {
		super.loadGameState(document);
		if (!document.contains("seed")) {
			random = new SeededRandomWrapper();
			return;
		}

		long seed = document.getLong("seed");
		if (seed == random.getSeed()) return;

		random = IRandom.create(seed);

		if (!isEnabled()) return;
		reloadRandomization();
	}

	@Override
	public void writeGameState(@Nonnull Document document) {
		super.writeGameState(document);
		document.set("seed", random.getSeed());
	}

}
