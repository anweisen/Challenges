package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class MaxBiomeTimeChallenge extends SettingModifier {

	public MaxBiomeTimeChallenge() {
		super(MenuType.CHALLENGES, 3, 20);
	}

	@Override
	protected void onEnable() {
		bossbar.setContent((bossbar, player) -> {
			int currentTime = getCurrentTime(player);
			int timePerBiome = (getValue() * 60);
			bossbar.setTitle(Message.forName("bossbar-biome-time-time-left").asString(StringUtils.getEnumName(getBiome(player)), timePerBiome - currentTime));
			bossbar.setColor(BarColor.GREEN);
			bossbar.setProgress(1 - ((float) currentTime / timePerBiome));
		});
		bossbar.show();
	}

	@Override
	protected void onDisable() {
		bossbar.hide();
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.SPRUCE_SAPLING, Message.forName("item-max-biome-time-challenge"));
	}

	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return Message.forName("item-time-seconds-description").asArray(getValue() * 60);
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChallengeSecondsValueChangeTitle(this, getValue() * 60);
	}

	@EventHandler
	public void onPlayerMove(@Nonnull PlayerMoveEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getPlayer())) return;
		if (event.getTo() == null) return;
		if (event.getFrom().getBlock().getBiome() == event.getTo().getBlock().getBiome()) return;
		bossbar.update(event.getPlayer());
	}

	@ScheduledTask(ticks = 20)
	public void onSecond() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			updateBiomeTime(player);
		}
	}

	private void updateBiomeTime(@Nonnull Player player) {
		if (ignorePlayer(player)) {
			bossbar.update(player);
			return;
		}

		Biome biome = player.getLocation().getBlock().getBiome();
		int time = getCurrentTime(player) + 1;
		if (time >= getValue() * 60) {
			Bukkit.getScheduler().runTask(plugin, () -> {
				player.damage(Integer.MAX_VALUE);
			});
		}
		if (time < getValue() * 60) {
			getPlayerData(player).set(biome.name(), time);
		}
		bossbar.update(player);
	}

	private int getCurrentTime(@Nonnull Player player) {
		return getPlayerData(player).getInt(getBiome(player).name(), 0);
	}

	private Biome getBiome(@Nonnull Player player) {
		return player.getLocation().getBlock().getBiome();
	}

}