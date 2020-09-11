package net.codingarea.challengesplugin.challenges.goal;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Goal;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.events.ChallengeEndCause;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-13-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class WitherGoal extends Goal implements Listener {

	public WitherGoal() {
		super(MenuType.GOALS);
	}

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.NETHER_STAR, ItemTranslation.KILL_WHITER).getItem();
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public List<Player> getWinners() {
		return null;
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {

		if (!isCurrentGoal || !Challenges.timerIsStarted()) return;
		if (!(event.getEntity() instanceof Wither)) return;

		Challenges.getInstance().getServerManager().handleChallengeEnd(event.getEntity().getKiller(), ChallengeEndCause.PLAYER_CHALLENGE_GOAL_REACHED, null);

	}

}
