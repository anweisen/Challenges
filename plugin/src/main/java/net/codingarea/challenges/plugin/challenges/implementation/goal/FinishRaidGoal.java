package net.codingarea.challenges.plugin.challenges.implementation.goal;

import java.util.List;
import javax.annotation.Nonnull;
import net.anweisen.utilities.bukkit.utils.misc.MinecraftVersion;
import net.anweisen.utilities.common.annotations.Since;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.abstraction.SettingGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.challenges.annotations.RequireVersion;
import net.codingarea.challenges.plugin.management.server.ChallengeEndCause;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Raid.RaidStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.raid.RaidFinishEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
@RequireVersion(MinecraftVersion.V1_16)
public class FinishRaidGoal extends SettingGoal {


	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.CROSSBOW, Message.forName("item-finish-raid-goal"));
	}

	@Override
	public void getWinnersOnEnd(@Nonnull List<Player> winners) { }

	@EventHandler(priority = EventPriority.HIGH)
	public void onRaidFinish(@Nonnull RaidFinishEvent event) {
		if (!shouldExecuteEffect()) return;
		if (event.getRaid().getStatus() != RaidStatus.VICTORY) return;
		ChallengeAPI.endChallenge(ChallengeEndCause.GOAL_REACHED, event::getWinners);
	}

}