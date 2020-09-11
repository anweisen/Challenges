package net.codingarea.challengesplugin.challenges.goal;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.CollectGoal;
import net.codingarea.challengesplugin.challengetypes.extra.ITimerStatusExecutor;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-01-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class CollectDeathsGoal extends CollectGoal<DamageCause> implements Listener, ITimerStatusExecutor {

	public CollectDeathsGoal() {
		super(MenuType.GOALS);
	}

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.LAVA_BUCKET, ItemTranslation.MOST_DEATHS).build();
	}

	@Override
	public void onTimerStart() {
		if (!isCurrentGoal) return;
		points = new ConcurrentHashMap<>();
		updateScoreboard();
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {

		if (!isCurrentGoal || !Challenges.timerIsStarted()) return;
		handleNewPoint(event.getEntity(), event.getEntity().getLastDamageCause().getCause(), event.getEntity().getLastDamageCause().getCause().name(),
				Translation.COLLECT_DEATHS_DEATH_REGISTERED);

	}
}
