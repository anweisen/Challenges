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

	protected final List<EntityType> originalEntitiesToKill;
	protected List<EntityType> entitiesToKill;
	private final boolean throughPlayer;

	public KillMobsGoal(List<EntityType> entitiesToKill, boolean throughPlayer) {
		this.originalEntitiesToKill = entitiesToKill;
		this.throughPlayer = throughPlayer;
		resetBossesToKill();
	}

	public abstract Message getBossbarMessage();

	private void resetBossesToKill() {
		entitiesToKill = new LinkedList<>(originalEntitiesToKill);
	}

	@Override
	public void getWinnersOnEnd(@NotNull List<Player> winners) {

	}

	@Override
	protected void onEnable() {
		bossbar.setContent((bar, player) -> {
			float i = 1 - ((float) entitiesToKill.size() / (float) originalEntitiesToKill.size());
			bar.setProgress(i);
			bar.setColor(BarColor.GREEN);
			bar.setTitle(getBossbarMessage().asString(originalEntitiesToKill.size() - entitiesToKill.size(), originalEntitiesToKill.size()));
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
		entitiesToKill.remove(event.getEntityType());
		Message.forName("boss-kill").broadcast(Prefix.CHALLENGES, StringUtils.getEnumName(event.getEntityType()), originalEntitiesToKill.size() - entitiesToKill.size(), originalEntitiesToKill.size());
		bossbar.update();
		if (!entitiesToKill.isEmpty()) return;
		resetBossesToKill();
		ChallengeAPI.endChallenge(ChallengeEndCause.GOAL_REACHED);
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
