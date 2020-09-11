package net.codingarea.challengesplugin.challenges.challenges;

import com.google.common.collect.Sets;
import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author anweisen
 * Challenges developed on 06-25-2020
 * https://github.com/anweisen
 */

public class JumpHigherChallenge extends Setting implements Listener {

	private Map<UUID, Integer> height = new HashMap<>();

	public JumpHigherChallenge() {
		super(MenuType.CHALLENGES);
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		height = new HashMap<>();
	}

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.RABBIT_FOOT, ItemTranslation.JUMP_HIGHER).build();
	}

	private Set<UUID> prevPlayersOnGround = Sets.newHashSet();

	@EventHandler
	public void onMove(PlayerMoveEvent event) {

		if (!enabled || !Challenges.timerIsStarted()) return;
		if (event.getPlayer().getGameMode() == GameMode.SPECTATOR || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;

		Player player = event.getPlayer();

		if ((player.getNoDamageTicks() < 10 || (player.getLastDamageCause() != null && player.getLastDamageCause().getCause() == DamageCause.FALL)) && player.getVelocity().getY() > 0) {

			double jumpVelocity = 0.42F;
			if (player.hasPotionEffect(PotionEffectType.JUMP)) {
				jumpVelocity += (float) (player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1) * 0.1F;
			}

			if (!player.getLocation().getBlock().getType().name().contains("water")
					&& player.getLocation().getBlock().getType() != Material.LADDER
					&& prevPlayersOnGround.contains(player.getUniqueId())) {

				if (!player.isOnGround() && Double.compare(player.getVelocity().getY(), jumpVelocity) == 0) {
					handleJump(player);
				}
			}
		}

		if (player.isOnGround()) {
			prevPlayersOnGround.add(player.getUniqueId());
		} else {
			prevPlayersOnGround.remove(player.getUniqueId());
		}
	}

	private int getJumps(Player player) {
		int jumps = 1;
		if (this.height.get(player.getUniqueId()) == null || this.height.get(player.getUniqueId()) == 0) {
			this.height.put(player.getUniqueId(), 1);
		} else {
			jumps = this.height.get(player.getUniqueId()) + 1;
			this.height.put(player.getUniqueId(), jumps);
		}
		return jumps;
	}

	private void handleJump(Player player) {
		double height = calculateHeight(getJumps(player));
		player.setVelocity(new Vector().setY(height));
	}

	private double calculateHeight(int jumps) {
		return (double) (jumps) / 5D;
	}

}
