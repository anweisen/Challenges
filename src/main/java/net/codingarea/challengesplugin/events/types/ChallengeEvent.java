package net.codingarea.challengesplugin.events.types;

import net.codingarea.challengesplugin.challengetypes.Challenge;
import net.codingarea.challengesplugin.challengetypes.GeneralChallenge;
import net.codingarea.challengesplugin.challengetypes.Goal;
import net.codingarea.challengesplugin.manager.menu.MenuClickHandler.ClickResult;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public interface ChallengeEvent extends IEvent {

	public static abstract class PlayerChallengeEvent extends GeneralPlayerEvent implements ChallengeEvent {
	}

	public static class ChallengeEndEvent extends PlayerChallengeEvent {

		public enum ChallengeEndCause {
			TIMER_END,
			PLAYER_DEATH,
			PLAYER_CHALLENGE_FAIL,
			PLAYER_CHALLENGE_GOAL_REACHED,
			LAST_MAN_STANDING,
			KILL_ALL;
		}

		private ChallengeEndCause cause;

		public ChallengeEndEvent(Player player, ChallengeEndCause cause) {
			this.player = player;
			this.cause = cause;
		}

		public ChallengeEndCause getCause() {
			return cause;
		}
	}

	public static class ChallengeEditEvent extends PlayerChallengeEvent {

		private ChallengeEditTriggerEvent triggerEvent;

		public ChallengeEditEvent(ChallengeEditTriggerEvent event) {

			if (event == null) throw new NullPointerException("ChallengeEditTriggerEvent cannot be null!");

			this.triggerEvent = event;

		}

		public GeneralChallenge getChallenge() {
			return triggerEvent.getChallenge();
		}

	}

	public static class ChallengeEditTriggerEvent extends PlayerChallengeEvent {

		private InventoryClickEvent clickEvent;
		private GeneralChallenge challenge;
		private boolean cancelledEditEvent;
		private ClickResult clickResult;

		public ChallengeEditTriggerEvent(InventoryClickEvent clickEvent, GeneralChallenge challenge, ClickResult clickResult) {
			this.challenge = challenge;
			this.clickEvent = clickEvent;
			this.clickResult = clickResult;
			try {
				this.player = (Player) clickEvent.getWhoClicked();
			} catch (ClassCastException ignored) { }
		}

		public InventoryClickEvent getClickEvent() {
			return clickEvent;
		}

		public boolean isRightClick() {
			return clickEvent.isRightClick();
		}

		public boolean isLeftClick() {
			return clickEvent.isLeftClick();
		}

		public boolean isShiftClick() {
			return clickEvent.isShiftClick();
		}

		public GeneralChallenge getChallenge() {
			return challenge;
		}

		public void setCancelledEditEvent(boolean cancelledEditEvent) {
			this.cancelledEditEvent = cancelledEditEvent;
		}

		public boolean isCancelledEditEvent() {
			return cancelledEditEvent;
		}

		public ClickResult getClickResult() {
			return clickResult;
		}

	}

	public static class ChallengeGoalChangedEvent extends PlayerChallengeEvent {

		private Goal from;
		private Goal to;

		public ChallengeGoalChangedEvent(Goal from, Goal to, Player changer) {
			this.from = from;
			this.to = to;
			this.player = changer;
		}

		public Goal getFrom() {
			return from;
		}

		public Goal getTo() {
			return to;
		}
	}

	public static class ChallengeTimeActivationEvent implements ChallengeEvent {

		private Challenge challenge;

		public ChallengeTimeActivationEvent(Challenge challenge) {
			this.challenge = challenge;
		}

		public Challenge getChallenge() {
			return challenge;
		}
	}

}
