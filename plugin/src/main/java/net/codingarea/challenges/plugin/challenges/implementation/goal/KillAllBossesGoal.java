package net.codingarea.challenges.plugin.challenges.implementation.goal;

import net.anweisen.utilities.commons.annotations.Since;
import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.commons.misc.StringUtils;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.SettingGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.management.server.ChallengeEndCause;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class KillAllBossesGoal extends SettingGoal {

	protected List<EntityType> entitiesToKill;
	protected int totalBossCount;

	public KillAllBossesGoal() {
		resetBossesToKill();
	}

	private void resetBossesToKill() {
		entitiesToKill = new ArrayList<>(Arrays.asList(EntityType.ENDER_DRAGON, EntityType.WITHER, EntityType.ELDER_GUARDIAN));
		totalBossCount = entitiesToKill.size();
	}

	@Override
	public void getWinnersOnEnd(@Nonnull List<Player> winners) { }

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onKill(@Nonnull EntityDeathEvent event) {
		if (!shouldExecuteEffect()) return;
		if (!entitiesToKill.contains(event.getEntityType())) return;
		entitiesToKill.remove(event.getEntityType());
		Message.forName("boss-kill").broadcast(Prefix.CHALLENGES, StringUtils.getEnumName(event.getEntityType()), totalBossCount - entitiesToKill.size(), totalBossCount);
		if (!entitiesToKill.isEmpty()) return;
		resetBossesToKill();
		ChallengeAPI.endChallenge(ChallengeEndCause.GOAL_REACHED);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.DIAMOND_SWORD, Message.forName("item-all-bosses-goal"));
	}

	@Override
	public void writeGameState(@Nonnull Document document) {
		super.writeGameState(document);

		document.set("entities", entitiesToKill);
	}

	@Override
	public void loadGameState(@Nonnull Document document) {
		super.loadGameState(document);

		entitiesToKill = document.getEnumList("entities", EntityType.class);
	}

}