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
import net.codingarea.challenges.plugin.spigot.events.PlayerInventoryClickEvent;
import net.codingarea.challenges.plugin.spigot.events.PlayerPickupItemEvent;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.ItemUtils;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import net.codingarea.challenges.plugin.utils.misc.RandomizeUtils;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@Since("2.0")
@ExcludeFromRandomChallenges
public class ForceItemChallenge extends CompletableForceChallenge {

	private Material item;

	public ForceItemChallenge() {
		super(MenuType.CHALLENGES, 2, 15);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.LEATHER_BOOTS, Message.forName("item-force-item-challenge"));
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
			if (ChallengeAPI.isPaused()) {
				bossbar.setTitle(Message.forName("bossbar-timer-paused").asString());
				bossbar.setColor(BarColor.RED);
				return;
			}
			if (getState() == WAITING) {
				bossbar.setTitle(Message.forName("bossbar-force-item-waiting").asString());
				return;
			}

			bossbar.setColor(BarColor.GREEN);
			bossbar.setProgress(getProgress());
			bossbar.setTitle(Message.forName("bossbar-force-item-instruction").asString(StringUtils.getEnumName(item), ChallengeAPI.formatTime(getSecondsLeftUntilNextActivation())));
		};
	}

	@Override
	protected void broadcastFailedMessage() {
		Message.forName("force-item-fail").broadcast(Prefix.CHALLENGES, StringUtils.getEnumName(item));
	}

	@Override
	protected void broadcastSuccessMessage(@Nonnull Player player) {
		Message.forName("force-item-success").broadcast(Prefix.CHALLENGES, NameHelper.getName(player), StringUtils.getEnumName(item));
	}

	@Override
	protected void chooseForcing() {
		List<Material> items = new ArrayList<>(Arrays.asList(Material.values()));
		items.removeIf(material -> !ItemUtils.isObtainableInSurvival(material));
		items.removeIf(material -> !material.isItem());
		items.removeIf(material -> material.name().contains("PURPUR"));
		items.removeIf(material -> material.name().contains("END"));
		items.removeIf(material -> material.name().contains("SHULKER"));
		Utils.removeEnums(items, "ELYTRA", "NETHER_STAR");

		item = random.choose(items);
	}

	@Override
	protected int getForcingTime() {
		return random.range(5 * 60, 8 * 60);
	}

	@Override
	protected int getSecondsUntilNextActivation() {
		return random.around(getValue() * 60, 30);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onClick(@Nonnull PlayerInventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		if (item == null) return;
		if (item.getType() != this.item) return;
		completeForcing(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPickUp(@Nonnull PlayerPickupItemEvent event) {
		Material material = event.getItem().getItemStack().getType();
		if (material != item) return;
		completeForcing(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onInteract(@Nonnull PlayerInteractEvent event) {
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
			Material material = item.getType();
			if (material != this.item) return;
			completeForcing(event.getPlayer());
		}, 1);
	}

}
