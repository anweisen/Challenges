package net.codingarea.challengesplugin.manager.events;

import net.codingarea.challengesplugin.manager.menu.MenuClickHandler.ClickResult;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-11-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class ChallengeEditEvent {

	private final Player player;
	private final ClickResult clickResult;
	private final InventoryClickEvent clickEvent;

	public ChallengeEditEvent(Player player, ClickResult result, InventoryClickEvent clickEvent) {
		this.player = player;
		this.clickResult = result;
		this.clickEvent = clickEvent;
	}

	public Player getPlayer() {
		return player;
	}

	public ClickResult getClickResult() {
		return clickResult;
	}

	public boolean wasRightClick() {
		return clickEvent.isRightClick();
	}

	public boolean wasLeftClick() {
		return clickEvent.isLeftClick();
	}

	public boolean wasShiftClick() {
		return clickEvent.isShiftClick();
	}

	public InventoryClickEvent getClickEvent() {
		return clickEvent;
	}

}
