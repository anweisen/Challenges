package net.codingarea.challengesplugin.events;

import net.codingarea.challengesplugin.events.types.ChallengeEvent.ChallengeEditEvent;
import net.codingarea.challengesplugin.events.types.ChallengeEvent.ChallengeEditTriggerEvent;
import net.codingarea.challengesplugin.events.types.IEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public class EventHandler {

	private List<ChallengeListener> listeners;

	public EventHandler() {
		listeners = new ArrayList<>();
	}

	public void handleEvent(IEvent event) {

		if (event == null) throw new NullPointerException("Event cannot be null!");

		for (ChallengeListener currentListener : listeners) {
			currentListener.onEvent(event);
		}

		triggerSubEvents(event);

	}

	private void triggerSubEvents(IEvent event) {

		if (event instanceof ChallengeEditTriggerEvent) {

			ChallengeEditTriggerEvent thisEvent = (ChallengeEditTriggerEvent) event;

			if (thisEvent.isCancelledEditEvent()) return;

			ChallengeEditEvent triggeredEvent = new ChallengeEditEvent(thisEvent);
			handleEvent(triggeredEvent);

		}

	}

	public void registerListener(ChallengeListener listener) {

		if (listener == null) throw new NullPointerException("Listener cannot be null!");

		listeners.add(listener);

	}

}
