package net.codingarea.challenges.plugin.challenges.implementation.goal;

import java.util.Collections;
import java.util.List;
import net.anweisen.utilities.common.annotations.Since;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.abstraction.SettingGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.server.ChallengeEndCause;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.2
 */
@Since("2.1.2")
public class EatCakeGoal extends SettingGoal {

	@Override
	public void getWinnersOnEnd(@NotNull List<Player> winners) {

	}

	@NotNull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.CAKE, Message.forName("item-eat-cake-goal"));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getPlayer())) return;
		if (event.getClickedBlock() == null) return;
		if (event.getClickedBlock().getType() != Material.CAKE) return;
		if (event.getPlayer().getFoodLevel() >= 20) return;
		// Execute on next tick to prevent hunger bar not filling up
		Bukkit.getScheduler().runTask(plugin, () -> {
			ChallengeAPI.endChallenge(ChallengeEndCause.GOAL_REACHED, () -> Collections.singletonList(event.getPlayer()));
		});
	}

}