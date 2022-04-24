package net.codingarea.challenges.plugin.challenges.type.abstraction;

import net.anweisen.utilities.common.config.Document;
import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.management.server.ChallengeEndCause;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.3
 */
public abstract class KillMobsGoal extends SettingGoal {

	protected final List<EntityType> entitiesToKill;
	protected List<EntityType> entitiesKilled;
	private final boolean throughPlayer;

	public KillMobsGoal(List<EntityType> entitiesKilled, boolean throughPlayer) {
		this.entitiesToKill = entitiesKilled;
		this.throughPlayer = throughPlayer;
		resetEntitiesToKill();
	}

	public abstract Message getBossbarMessage();

	private void resetEntitiesToKill() {
		entitiesKilled = new LinkedList<>();
	}

	@Override
	public void getWinnersOnEnd(@NotNull List<Player> winners) {

	}

	@Override
	protected void onEnable() {
		bossbar.setContent((bar, player) -> {
			float i = 1 - ((float) getEntitiesLeftToKill().size() / (float) entitiesToKill.size());
			bar.setProgress(i);
			bar.setColor(BarColor.GREEN);
			bar.setTitle(getBossbarMessage().asString(entitiesKilled.size(), entitiesToKill.size()));
		});
		bossbar.show();
	}

	@Override
	protected void onDisable() {
		bossbar.hide();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDeath(@Nonnull EntityDeathEvent event) {
		if (!shouldExecuteEffect()) return;
		if (throughPlayer) {
			if (event.getEntity().getKiller() == null) return;
		}
		if (!entitiesToKill.contains(event.getEntityType())) return;
		if (entitiesKilled.contains(event.getEntityType())) return;
		entitiesKilled.add(event.getEntityType());
		Message.forName("mob-kill").broadcast(Prefix.CHALLENGES, StringUtils.getEnumName(event.getEntityType()), entitiesKilled.size(), entitiesToKill.size());
		bossbar.update();
		if (!getEntitiesLeftToKill().isEmpty()) return;
		resetEntitiesToKill();
		ChallengeAPI.endChallenge(ChallengeEndCause.GOAL_REACHED);
	}

	@Override
	public void writeGameState(@Nonnull Document document) {
		super.writeGameState(document);

		document.set("entities", entitiesKilled);
	}

	@Override
	public void loadGameState(@Nonnull Document document) {
		super.loadGameState(document);

		entitiesKilled = document.getEnumList("entities", EntityType.class);
	}

	public List<EntityType> getEntitiesLeftToKill() {
		LinkedList<EntityType> entityTypes = new LinkedList<>(entitiesToKill);
		entityTypes.removeAll(entitiesKilled);
		return entityTypes;
	}

}
