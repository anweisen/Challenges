package net.codingarea.challengesplugin.challenges.goal;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.CollectGoal;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-01-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class CollectDeathsGoal extends CollectGoal<DamageCause> implements Listener {

	public CollectDeathsGoal() {
		menu = MenuType.GOALS;
		name = "collectdeaths";
		scoreboard = Challenges.getInstance().getScoreboardManager().getNewScoreboard(name);
	}

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.LAVA_BUCKET, ItemTranslation.MOST_DEATHS).build();
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		if (Challenges.timerIsStarted()) showScoreboard();
	}

	@Override
	public void onDisable(ChallengeEditEvent event) {
		hideScoreboard();
	}

	@Override
	public void onTimerStart() {
		if (!isCurrentGoal) return;
		points = new ConcurrentHashMap<>();
		showScoreboard();
		updateScoreboard();
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {

		if (!isCurrentGoal || !Challenges.timerIsStarted()) return;
		handleNewPoint(event.getEntity(), event.getEntity().getLastDamageCause().getCause(), event.getEntity().getLastDamageCause().getCause().name(),
				Translation.COLLECT_DEATHS_DEATH_REGISTERED);

	}
}
