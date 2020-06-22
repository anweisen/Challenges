package net.codingarea.challengesplugin.manager.menu;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public enum MenuType {

	SETTINGS(0),
	CHALLENGES(1),
	GOALS(2),
	BLOCK_ITEMS(3),
	DAMAGE(4),
	DIFFICULTY(5);

	private int pageID;

	MenuType(int pageID) {
		this.pageID = pageID;
	}

	public int getPageID() {
		return pageID;
	}

}
