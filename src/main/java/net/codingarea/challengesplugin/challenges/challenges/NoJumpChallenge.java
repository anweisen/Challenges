package net.codingarea.challengesplugin.challenges.challenges;

import com.google.common.collect.Sets;
import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.AdvancedChallenge;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;
import java.util.UUID;

/**
 * @author anweisen
 * Challenges developed on 06-23-2020
 * https://github.com/anweisen
 */

public class NoJumpChallenge extends AdvancedChallenge implements Listener {

	public NoJumpChallenge() {
		menu = MenuType.CHALLENGES;
		value = 20;
		maxValue = 50;
	}

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.GOLDEN_BOOTS, ItemTranslation.NO_JUMP).hideAttributes().build();
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public void onValueChange(ChallengeEditEvent event) { }

	@Override
	public void onTimeActivation() { }

	private Set<UUID> prevPlayersOnGround = Sets.newHashSet();

	@EventHandler
	public void onMove(PlayerMoveEvent event) {

		if (!enabled || !Challenges.timerIsStarted()) return;
		if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;

		Player player = event.getPlayer();

		if (player.getVelocity().getY() > 0) {
			double jumpVelocity = 0.42F;
			if (player.hasPotionEffect(PotionEffectType.JUMP)) {
				jumpVelocity += (float) (player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1) * 0.1F;
			}
			if (player.getLocation().getBlock().getType() != Material.LADDER && prevPlayersOnGround.contains(player.getUniqueId())) {

				if (!player.isOnGround() && Double.compare(player.getVelocity().getY(), jumpVelocity) == 0) {
					Bukkit.broadcastMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.NO_JUMP_PLAYER_JUMPED.get()
							.replace("%player%", player.getName()));
					player.damage(value);
				}
			}
		}
		if (player.isOnGround()) {
			prevPlayersOnGround.add(player.getUniqueId());
		} else {
			prevPlayersOnGround.remove(player.getUniqueId());
		}
	}
}
