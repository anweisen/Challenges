package net.codingarea.challengesplugin.events;

import net.codingarea.challengesplugin.events.types.ChallengeEvent;
import net.codingarea.challengesplugin.events.types.ChallengeEvent.*;
import net.codingarea.challengesplugin.events.types.IEvent;
import net.codingarea.challengesplugin.events.types.ScoreboardEvent;
import net.codingarea.challengesplugin.events.types.ScoreboardEvent.ScoreboardHideEvent;
import net.codingarea.challengesplugin.events.types.ScoreboardEvent.ScoreboardShowEvent;
import net.codingarea.challengesplugin.events.types.TimerEvent;
import net.codingarea.challengesplugin.events.types.TimerEvent.TimerResetEvent;
import net.codingarea.challengesplugin.events.types.TimerEvent.TimerResumeEvent;
import net.codingarea.challengesplugin.events.types.TimerEvent.TimerStartEvent;
import net.codingarea.challengesplugin.events.types.TimerEvent.TimerStopEvent;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public abstract class ChallengeListener {

	public final void onEvent(IEvent event) {

		onGeneralEvent(event);

		if (event instanceof ChallengeEditEvent) {
			onChallengeEvent((ChallengeEditEvent) event);
			onChallengeEdit((ChallengeEditEvent) event);
			onPlayerChallengeEvent((PlayerChallengeEvent) event);
		} else if (event instanceof ChallengeEndEvent) {
			onChallengeEvent((ChallengeEndEvent) event);
			onChallengeEnd((ChallengeEndEvent) event);
			onPlayerChallengeEvent((PlayerChallengeEvent) event);
		} else if (event instanceof ChallengeEditTriggerEvent) {
			onChallengeEvent((ChallengeEditTriggerEvent) event);
			onChallengeEditTrigger((ChallengeEditTriggerEvent) event);
			onPlayerChallengeEvent((PlayerChallengeEvent) event);
		} else if (event instanceof ChallengeTimeActivationEvent) {
			onChallengeTimeActivation((ChallengeTimeActivationEvent) event);
			onChallengeEvent((ChallengeTimeActivationEvent) event);
		} else if (event instanceof TimerStopEvent) {
			onTimerEvent((TimerStopEvent) event);
			onTimerStop((TimerStopEvent) event);
		} else if (event instanceof TimerResumeEvent) {
			onTimerEvent((TimerResumeEvent) event);
			onTimerResume((TimerResumeEvent) event);
		} else if (event instanceof TimerStartEvent) {
			onTimerEvent((TimerStartEvent) event);
			onTimerStart((TimerStartEvent) event);
		} else if (event instanceof TimerResetEvent) {
			onTimerEvent((TimerResetEvent) event);
			onTimerReset((TimerResetEvent) event);
		} else if (event instanceof ScoreboardShowEvent) {
			onScoreboardEvent((ScoreboardShowEvent) event);
			onScoreboardShow((ScoreboardShowEvent) event);
		} else if (event instanceof ScoreboardHideEvent) {
			onScoreboardEvent((ScoreboardHideEvent) event);
			onScoreboardHide((ScoreboardHideEvent) event);
		}

	}

	public void onGeneralEvent(IEvent event) { }

	public void onChallengeEvent(ChallengeEvent event) { }
	public void onPlayerChallengeEvent(PlayerChallengeEvent event) { }
	public void onChallengeEdit(ChallengeEditEvent event) { }
	public void onChallengeEditTrigger(ChallengeEditTriggerEvent event) { }
	public void onChallengeEnd(ChallengeEndEvent event) { }
	public void onChallengeTimeActivation(ChallengeTimeActivationEvent event) { }

	public void onTimerEvent(TimerEvent event) { }
	public void onTimerStop(TimerStopEvent event) { }
	public void onTimerStart(TimerStartEvent event) { }
	public void onTimerResume(TimerResumeEvent event) { }
	public void onTimerReset(TimerResetEvent event) { }

	public void onScoreboardEvent(ScoreboardEvent event) { }
	public void onScoreboardHide(ScoreboardHideEvent event) { }
	public void onScoreboardShow(ScoreboardShowEvent event) { }

}