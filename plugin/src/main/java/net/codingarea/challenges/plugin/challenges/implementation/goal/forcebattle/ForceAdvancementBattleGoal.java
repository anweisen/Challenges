package net.codingarea.challenges.plugin.challenges.implementation.goal.forcebattle;

import net.anweisen.utilities.bukkit.utils.misc.BukkitReflectionUtils;
import net.anweisen.utilities.common.annotations.Since;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.challenges.type.abstraction.ForceBattleGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.bukkit.misc.BukkitStringUtils;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.0
 */
@Since("2.2.0")
public class ForceAdvancementBattleGoal extends ForceBattleGoal<Advancement> {

	public ForceAdvancementBattleGoal() {
		super(MenuType.GOAL, Message.forName("menu-force-advancement-battle-goal-settings"));
	}

	private void resetAdvancementProgress(Player player, Advancement advancement) {
		if (advancement == null) return;
		AdvancementProgress progress = player.getAdvancementProgress(advancement);
		progress.getAwardedCriteria().forEach(progress::revokeCriteria);
	}

	@NotNull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.EXPERIENCE_BOTTLE, Message.forName("item-force-advancement-battle-goal"));
	}

	@Override
	protected Advancement[] getTargetsPossibleToFind() {
		List<Advancement> advancements = new ArrayList<>();
		Bukkit.getServer().advancementIterator().forEachRemaining(advancement -> {
			String string = advancement.getKey().toString();
			if (!string.contains(":recipes/") && !string.endsWith("root")) {
				advancements.add(advancement);
			}
		});
		return advancements.toArray(new Advancement[0]);
	}

	@Override
	public void setTargetInDocument(Document document, String key, Advancement target) {
		document.set(key, target.getKey().toString());
	}

	@Override
	public void setFoundListInDocument(Document document, String key, List<Advancement> targets) {
		List<String> foundItems = new LinkedList<>();
		for (Advancement advancement : targets) {
			foundItems.add(advancement.getKey().toString());
		}
		document.set(key, foundItems);
	}

	@Override
	public Advancement getTargetFromDocument(Document document, String key) {
		String advancementKey = document.getString(key);
		try {
			NamespacedKey namespacedKey = BukkitReflectionUtils.fromString(advancementKey);
			return Bukkit.getAdvancement(namespacedKey);
		} catch (Exception exception) {
			// DON'T EXIST
		}
		return null;
	}

	@Override
	public List<Advancement> getListFromDocument(Document document, String key) {
		List<String> advancementKeys = document.getStringList(key);
		List<Advancement> advancements = new ArrayList<>();
		for (String advancementKey : advancementKeys) {
			try {
				advancements.add(Bukkit.getAdvancement(BukkitReflectionUtils.fromString(advancementKey)));
			} catch (Exception exception) {
				// DON'T EXIST
			}
		}
		return advancements;
	}

	@Override
	protected Message getNewTargetMessage(Advancement newTarget) {
		return Message.forName("force-advancement-battle-new-advancement");
	}

	@Override
	protected Message getTargetCompletedMessage(Advancement target) {
		return Message.forName("force-advancement-battle-completed");
	}

	@Override
	public Object getTargetMessageReplacement(Advancement target) {
		return BukkitStringUtils.getAdvancementComponent(target);
	}

	@Override
	public String getTargetName(Advancement target) {
		return BukkitStringUtils.getAdvancementTitle(target).toPlainText();
	}

	@Override
	protected Message getLeaderboardTitleMessage() {
		return Message.forName("force-advancement-battle-leaderboard");
	}

	@Override
	public void setRandomTarget(Player player) {
		super.setRandomTarget(player);
		resetAdvancementProgress(player, currentTarget.get(player.getUniqueId()));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onAdvancement(PlayerAdvancementDoneEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getPlayer())) return;
		Player player = event.getPlayer();
		if (event.getAdvancement() == currentTarget.get(player.getUniqueId())) {
			handleTargetFound(player);
		}
	}

	@Override
	public void handleTargetFound(Player player) {
		Advancement advancement = currentTarget.get(player.getUniqueId());
		super.handleTargetFound(player);
		if (advancement != null) {
			AdvancementProgress progress = player.getAdvancementProgress(advancement);
			for(String criteria : progress.getRemainingCriteria()) {
				progress.awardCriteria(criteria);
			}
		}
	}
}
