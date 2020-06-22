package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.AdvancedChallenge;
import net.codingarea.challengesplugin.challengetypes.extra.ITimerStatusExecutor;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * @author anweisen
 * Challenges developed on 06-12-2020
 * https://github.com/anweisen
 */

public class AnvilRainChallenge extends AdvancedChallenge implements ITimerStatusExecutor, Listener {

	private final Random random;

	public AnvilRainChallenge() {
		this.random = new Random();
		this.menu = MenuType.CHALLENGES;
		this.maxValue = 60;
	}

	@Override
	public void onValueChange(ChallengeEditEvent event) { }

	@Override
	public void onEnable(ChallengeEditEvent event) {
		this.nextActionInSeconds = 1;
	}

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public void onTimerStart() {
		this.nextActionInSeconds = 1;
	}

	@Override
	public void onTimeActivation() {

		this.nextActionInSeconds = 1;
		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			if (currentPlayer.getGameMode() == GameMode.SPECTATOR) continue;
			Location anvil = currentPlayer.getLocation().getBlock().getLocation().clone().add(0.5, 0, 0.5);
			anvil.setY(calculateHeight(anvil));
			spawnAnvils(anvil, calculateRadius(), random);
		}

	}

	private int calculateHeight(Location player) {

		int i = 0;
		while (player.getBlock().isPassable() && player.getBlockY() < 250 && i < 35) {
			player.add(0, 1, 0);
			i++;
		}

		if (i < 15) {
			int j = Utils.getHighestBlock(player).getBlockY();
			return (j - 15) > player.getBlockY() ? j : player.getBlockY() + 10;
		}

		return i - 3 + player.getBlockY();

	}

	private int calculateRadius() {
		if (value <= 10) {
			return 10;
		} else if (value <= 30) {
			return 25;
		} else if (value <= 45) {
			return 40;
		} else {
			return 50;
		}
	}

	private void spawnAnvils(Location middle, int radius, Random random) {

		for (int i = 0; i < value; i++) {
			spawnAnvil(getRandomLocationInRadius(middle, radius, random).add(0, 1, 0)).getLocation().getBlockY();
		}

	}

	private Location getRandomLocationInRadius(Location middle, int radius, Random random) {
		int[] distance = getRandomDistance(radius, random);
		return middle.clone().add(distance[0], 0, distance[1]);
	}

	private int getRandomIntInRadius(int radius, Random random) {
		return random.nextInt(radius) - radius / 2;
	}

	private int[] getRandomDistance(int radius, Random random) {

		int[] array = new int[2];

		int a = getRandomIntInRadius(radius, random);
		int b = getRandomIntInRadius(radius, random);

		if (a > b) {
			a += random.nextInt(4);
			b -= random.nextInt(4);
		} else {
			a -= random.nextInt(4);
			b += random.nextInt(4);
		}
		array[0] = a;
		array[1] = b;

		return array;

	}

	private FallingBlock spawnAnvil(Location anvil) {
		return anvil.getWorld().spawnFallingBlock(anvil, Material.ANVIL, (byte) 0);
	}

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.CHIPPED_ANVIL, ItemTranslation.ANVIL_RAIN).build();
	}

	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {

		if (!enabled || !Challenges.timerIsStarted()) return;
		if (!(event.getEntity() instanceof FallingBlock)) return;
		if (event.getBlock().getLocation().clone().add(0, -1, 0).getBlock().getType() == Material.AIR) return;
		event.getBlock().setType(Material.AIR);
		event.setCancelled(true);
		event.getEntity().remove();
		charge(event.getBlock().getLocation().clone());

	}

	private void charge(Location destination) {
		int i = random.nextInt(2);
		if (i == 0 || (i == 2 && value > 25)) return;
		int blocks = i == 1 && value < 16 ? -1 : 0;
		while (blocks < 1 && destination.getBlockY() > 1) {
			if (destination.getBlock().getType() == Material.WATER || destination.getBlock().getType() == Material.LAVA) return;
			destination.subtract(0, 1, 0);
			if (destination.getBlock().isPassable()) blocks--;
			destination.getBlock().setType(Material.AIR);
			blocks++;
		}
	}

}
