package net.codingarea.challenges.plugin.challenges.implementation.goal;

import net.anweisen.utilities.commons.annotations.Since;
import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.commons.misc.SeededRandomWrapper;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.SettingGoal;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.management.server.ChallengeEndCause;
import net.codingarea.challenges.plugin.spigot.events.PlayerInventoryClickEvent;
import net.codingarea.challenges.plugin.spigot.events.PlayerPickupItemEvent;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.bukkit.command.SenderCommand;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.ItemUtils;
import net.codingarea.challenges.plugin.utils.misc.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@Since("2.0")
public class CollectAllItemsGoal extends SettingGoal implements SenderCommand {

	private final int totalItemsCount;
	private SeededRandomWrapper random;
	private List<Material> itemsToFind;
	private Material currentItem;

	public CollectAllItemsGoal() {
		random = new SeededRandomWrapper();
		reloadItemsToFind();
		totalItemsCount = itemsToFind.size();
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.GRASS_BLOCK, Message.forName("item-all-items-goal"));
	}

	private void reloadItemsToFind() {
		itemsToFind = new ArrayList<>(Arrays.asList(Material.values()));
		itemsToFind.removeIf(material -> !ItemUtils.isObtainableInSurvival(material));
		Collections.shuffle(itemsToFind, random);
		nextItem();

		if (isEnabled())
			bossbar.update();
	}

	private void nextItem() {
		if (itemsToFind.isEmpty()) {
			ChallengeAPI.endChallenge(ChallengeEndCause.GOAL_REACHED);
			return;
		}
		currentItem = itemsToFind.remove(0);
	}

	@Override
	protected void onEnable() {
		bossbar.setContent((bossbar, player) -> {
			if (currentItem == null) {
				bossbar.setTitle(Message.forName("bossbar-all-items-finished").asString());
				return;
			}

			bossbar.setTitle(Message.forName("bossbar-all-items-current").asString(StringUtils.getEnumName(currentItem), totalItemsCount - itemsToFind.size() + 1));
		});
		bossbar.show();
	}

	@Override
	protected void onDisable() {
		bossbar.hide();
	}

	@Override
	public void onCommand(@Nonnull CommandSender sender, @Nonnull String[] args) throws Exception {
		if (!isEnabled()) {
			Message.forName("challenges-disabled").send(sender, Prefix.CHALLENGES);
			SoundSample.BASS_OFF.playIfPlayer(sender);
			return;
		}
		if (currentItem == null) {
			Message.forName("all-items-already-finished").send(sender, Prefix.CHALLENGES);
			SoundSample.BASS_OFF.playIfPlayer(sender);
			return;
		}

		Message.forName("all-items-skipped").broadcast(Prefix.CHALLENGES, StringUtils.getEnumName(currentItem));
		SoundSample.PLING.broadcast();
		nextItem();
		bossbar.update();

	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onClick(@Nonnull PlayerInventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		if (item == null) return;
		handleNewItem(item.getType());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPickUp(@Nonnull PlayerPickupItemEvent event) {
		Material material = event.getItem().getItemStack().getType();
		handleNewItem(material);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onInteract(@Nonnull PlayerInteractEvent event) {
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
			Material material = item.getType();
			handleNewItem(material);
		}, 1);
	}

	protected void handleNewItem(@Nullable Material material) {
		if (currentItem != material) return;
		Message.forName("all-items-found").broadcast(Prefix.CHALLENGES, StringUtils.getEnumName(currentItem));
		SoundSample.PLING.broadcast();
		nextItem();
		bossbar.update();
	}

	@Override
	public void getWinnersOnEnd(@Nonnull List<Player> winners) {
	}

	@Override
	public void loadGameState(@Nonnull Document document) {
		super.loadGameState(document);
		random = new SeededRandomWrapper(document.getLong("seed"));
		reloadItemsToFind();

		for (int i = 0; i < document.getInt("found"); i++)
			nextItem();
	}

	@Override
	public void writeGameState(@Nonnull Document document) {
		super.writeGameState(document);
		document.set("seed", random.getSeed());
		document.set("found", totalItemsCount - itemsToFind.size());
	}

}
