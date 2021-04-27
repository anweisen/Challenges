package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.annotations.Since;
import net.anweisen.utilities.commons.misc.StringUtils;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.CompletableForceChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.management.challenges.annotations.ExcludeFromRandomChallenges;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.server.scoreboard.ChallengeBossBar.BossBarInstance;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import net.codingarea.challenges.plugin.utils.misc.RandomizeUtils;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiConsumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@Since("2.0")
@ExcludeFromRandomChallenges
public class ForceBiomeChallenge extends CompletableForceChallenge {

	private Biome biome;

	public ForceBiomeChallenge() {
		super(MenuType.CHALLENGES, 2, 20, 5);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.CHAINMAIL_BOOTS, Message.forName("item-force-biome-challenge"));
	}

	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return ChallengeHelper.getTimeRangeSettingsDescription(this, 60 * 3, 60);
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChallengeSecondsRangeValueChangeTitle(this, getValue() * 60 * 3 - 60, getValue() * 60 * 3 + 60);
	}

	@Nonnull
	@Override
	protected BiConsumer<BossBarInstance, Player> setupBossbar() {
		return (bossbar, player) -> {
			if (ChallengeAPI.isPaused()) {
				bossbar.setTitle(Message.forName("bossbar-timer-paused").asString());
				bossbar.setColor(BarColor.RED);
				return;
			}
			if (getState() == WAITING) {
				bossbar.setTitle(Message.forName("bossbar-force-biome-waiting").asString());
				return;
			}

			bossbar.setColor(BarColor.GREEN);
			bossbar.setProgress(getProgress());
			bossbar.setTitle(Message.forName("bossbar-force-biome-instruction").asString(StringUtils.getEnumName(biome), ChallengeAPI.formatTime(getSecondsLeftUntilNextActivation())));
		};
	}

	@Override
	protected void broadcastFailedMessage() {
		Message.forName("force-biome-failed").broadcast(Prefix.CHALLENGES, StringUtils.getEnumName(biome));
	}

	@Override
	protected void broadcastSuccessMessage(@Nonnull Player player) {
		Message.forName("force-biome-success").broadcast(Prefix.CHALLENGES, NameHelper.getName(player), StringUtils.getEnumName(biome));
	}

	@Override
	protected void chooseForcing() {
		Biome[] biomes = Arrays.stream(Biome.values())
				.filter(biome -> !biome.name().contains("END"))
				.filter(biome -> !biome.name().contains("MUSHROOM"))
				.filter(biome -> !biome.name().contains("VOID"))
				.toArray(length -> new Biome[length]);

		biome = RandomizeUtils.choose(random, biomes);
	}

	@Override
	protected int getForcingTime() {
		return RandomizeUtils.randomAround(random, getRarity(biome) * 60 * 6, 60);
	}

	private int getRarity(@Nonnull Biome biome) {
		Object[][] mapping = {
			{ "BADLANDS", 5 },
			{ "JUNGLE", 4 },
			{ "BAMBOO", 4 },
			{ "MODIFIED", 3 },
			{ "TALL", 3 },
			{ "SWAMP", 2 },
		};

		for (Object[] pair : mapping) {
			String key = (String) pair[0];
			if (biome.name().contains(key))
				return (int) pair[1];
		}
		return 1;
	}

	@Override
	protected int getSecondsUntilNextActivation() {
		return RandomizeUtils.randomAround(random, getValue() * 60 * 3, 60);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMove(@Nonnull PlayerMoveEvent event) {
		if (!shouldExecuteEffect()) return;
		if (event.getTo() == null) return;
		if (event.getTo().getBlock().getBiome() != biome) return;
		completeForcing(event.getPlayer());
	}

}
