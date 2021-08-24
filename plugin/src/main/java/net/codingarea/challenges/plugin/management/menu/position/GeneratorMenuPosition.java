package net.codingarea.challenges.plugin.management.menu.position;

import net.anweisen.utilities.bukkit.utils.menu.MenuPosition;
import net.codingarea.challenges.plugin.management.menu.generator.MenuGenerator;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public abstract class GeneratorMenuPosition implements MenuPosition {

	protected final MenuGenerator generator;
	protected final int page;

	public GeneratorMenuPosition(MenuGenerator generator, int page) {
		this.generator = generator;
		this.page = page;
	}

	public int getPage() {
		return page;
	}

	public MenuGenerator getGenerator() {
		return generator;
	}

}
