package net.codingarea.challenges.plugin.management.menu.generator.categorised;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.2.0
 */
public class SmallCategorisedMenuGenerator extends CategorisedMenuGenerator {

	@Override
	public int getEntriesPerPage() {
		return 7;
	}

	@Override
	public int getSize() {
		return 9 * 3;
	}

	@Override
	public int[] getNavigationSlots(int page) {
		return new int[]{18, 26};
	}
}
