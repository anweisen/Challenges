package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.anweisen.utilities.bukkit.utils.misc.BukkitReflectionUtils;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.abstraction.EndingForceChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.management.challenges.annotations.ExcludeFromRandomChallenges;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.server.scoreboard.ChallengeBossBar.BossBarInstance;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@ExcludeFromRandomChallenges
public class ForceHeightChallenge extends EndingForceChallenge {

	private int height;

	public ForceHeightChallenge() {
		super(MenuType.CHALLENGES, 2, 15);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.IRON_BOOTS, Message.forName("item-force-height-challenge"));
	}

	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return ChallengeHelper.getTimeRangeSettingsDescription(this, 60, 30);
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChallengeSecondsRangeValueChangeTitle(this, getValue() * 60 - 30, getValue() * 60 + 30);
	}

	@Nonnull
	@Override
	protected BiConsumer<BossBarInstance, Player> setupBossbar() {
		return (bossbar, player) -> {
			if (getState() == WAITING) {
				bossbar.setTitle(Message.forName("bossbar-force-height-waiting").asString());
				return;
			}

			bossbar.setColor(BarColor.GREEN);
			bossbar.setProgress(getProgress());
			bossbar.setTitle(Message.forName("bossbar-force-height-instruction").asString(height, ChallengeAPI.formatTime(getSecondsLeftUntilNextActivation())));
		};
	}

	@Override
	protected boolean isFailing(@Nonnull Player player) {
		return player.getLocation().getBlockY() != height;
	}

	@Override
	protected void broadcastFailedMessage(@Nonnull Player player) {
		Message.forName("force-height-fail").broadcast(Prefix.CHALLENGES, NameHelper.getName(player), player.getLocation().getBlockY());
	}

	@Override
	protected void broadcastSuccessMessage() {
		Message.forName("force-height-success").broadcast(Prefix.CHALLENGES);
	}

	@Override
	protected void chooseForcing() {
		World world = ChallengeAPI.getGameWorld(Environment.NORMAL);
		height = random.range(BukkitReflectionUtils.getMinHeight(world), world.getMaxHeight());
	}

	@Override
	protected int getSecondsUntilNextActivation() {
		return random.around(getValue() * 60, 30);
	}

	@Override
	protected int getForcingTime() {
		return random.range(3 * 60 + 30, 5 * 60);
	}

}
