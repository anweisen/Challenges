package net.codingarea.challenges.plugin.spigot.listener;

import java.util.LinkedList;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;
import net.codingarea.challenges.plugin.challenges.type.abstraction.AbstractChallenge;
import net.codingarea.challenges.plugin.management.server.scoreboard.ChallengeScoreboard;
import net.codingarea.challenges.plugin.spigot.events.PlayerIgnoreStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.2
 */
public class ScoreboardUpdateListener implements Listener {

	@EventHandler
	public void onIgnoredChange(PlayerIgnoreStatusChangeEvent event) {

		Bukkit.getScheduler().runTask(Challenges.getInstance(), () -> {
			for (IChallenge challenge : new LinkedList<>(Challenges.getInstance().getChallengeManager().getChallenges())) {
				if (challenge instanceof AbstractChallenge) {
					AbstractChallenge abstractChallenge = (AbstractChallenge) challenge;
					ChallengeScoreboard scoreboard = abstractChallenge.getScoreboard();
					if (scoreboard.isShown()) {
						scoreboard.update();
					}
				}
			}
		});

	}

}
