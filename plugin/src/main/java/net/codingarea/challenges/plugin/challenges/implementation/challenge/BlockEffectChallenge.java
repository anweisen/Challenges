package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.common.annotations.Since;
import net.anweisen.utilities.common.collection.IRandom;
import net.anweisen.utilities.common.collection.SeededRandomWrapper;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.abstraction.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.spigot.events.PlayerIgnoreStatusChangeEvent;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.2
 */
@Since("2.1.2")
public class BlockEffectChallenge extends Setting {

	// Saving potion effect to prevent the possibility that an effect remove was somehow skipped
	private Map<UUID, PotionEffect> currentPotionEffects = new HashMap<>();

	public BlockEffectChallenge() {
		super(MenuType.CHALLENGES);
	}

	@NotNull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.CARVED_PUMPKIN, Message.forName("item-block-effect-challenge"));
	}

	@Override
	protected void onEnable() {
		broadcastFiltered(player -> {
			addEffect(player, player.getLocation(), null);
		});
		currentPotionEffects = new HashMap<>();
	}

	@Override
	protected void onDisable() {
		broadcastFiltered(player -> {
			removeEffect(player, player.getLocation(), null);
		});
		currentPotionEffects = null;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onLeave(PlayerQuitEvent event) {
		if (!shouldExecuteEffect()) return;
		currentPotionEffects.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onGameModeChange(PlayerIgnoreStatusChangeEvent event) {
		if (!shouldExecuteEffect()) return;
		if (event.isIgnored()) {
			removeEffect(event.getPlayer(), event.getPlayer().getLocation(), null);
		} else {
			addEffect(event.getPlayer(), event.getPlayer().getLocation(), null);
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onMove(PlayerMoveEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getPlayer())) return;
		if (BlockUtils.isSameBlockLocation(event.getFrom(), event.getTo())) return;
		Block fromBlock = BlockUtils.getBlockBelow(event.getFrom());
		Block toBlock = BlockUtils.getBlockBelow(event.getTo());
		if (toBlock != null && fromBlock != null) {
			if (toBlock.getType() == fromBlock.getType()) return;
		}
		removeEffect(event.getPlayer(), event.getFrom(), fromBlock);
		addEffect(event.getPlayer(), event.getTo(), toBlock);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onTeleport(PlayerTeleportEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getPlayer())) return;
		if (BlockUtils.isSameBlockLocation(event.getFrom(), event.getTo())) return;
		Block fromBlock = BlockUtils.getBlockBelow(event.getFrom());
		Block toBlock = BlockUtils.getBlockBelow(event.getTo());
		if (toBlock != null && fromBlock != null) {
			if (toBlock.getType() == fromBlock.getType()) return;
		}
		removeEffect(event.getPlayer(), event.getFrom(), fromBlock);
		addEffect(event.getPlayer(), event.getTo(), toBlock);
	}

	@ScheduledTask(ticks = 20, async = false)
	public void onSecond() {
		broadcastFiltered(player -> {
			addEffect(player, player.getLocation(), null);
		});
	}

	public void removeEffect(Player player, Location location, Block blockBelow) {
		if (blockBelow == null) {
			blockBelow = BlockUtils.getBlockBelow(location, -1);
		}
		if (blockBelow == null) return;
		PotionEffectType type = getEffect(blockBelow.getType()).getType();
		player.removePotionEffect(type);
	}


	public void addEffect(Player player, Location location, Block blockBelow) {
		if (blockBelow == null) {
			blockBelow = BlockUtils.getBlockBelow(location, -1);
		}
		if (blockBelow == null) return;
		PotionEffect effect = getEffect(blockBelow.getType());
		PotionEffect currentEffect = currentPotionEffects.get(player.getUniqueId());
		if (currentEffect != null && !currentEffect.equals(effect)) {
			player.removePotionEffect(currentEffect.getType());
		} else if (player.hasPotionEffect(effect.getType())) return;
		player.addPotionEffect(effect);
		currentPotionEffects.put(player.getUniqueId(), effect);
	}

	private PotionEffect getEffect(Material material) {
		long seed = ChallengeAPI.getGameWorld(World.Environment.NORMAL).getSeed();
		IRandom random = new SeededRandomWrapper(seed / material.ordinal());
		PotionEffectType[] types = PotionEffectType.values();
		PotionEffectType type = types[random.nextInt(types.length)];
		return type.createEffect(Integer.MAX_VALUE, random.nextInt(type.isInstant() ? 1 : 4));
	}

}