package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.AdvancedChallenge;
import net.codingarea.challengesplugin.challengetypes.extra.ITimerStatusExecutor;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-02-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class AnvilRainChallenge extends AdvancedChallenge implements ITimerStatusExecutor, Listener {

	private final Random random;
	private final BossBar bossBar;
	private int average = 0;

	public AnvilRainChallenge() {
		this.random = new Random();
		this.menu = MenuType.CHALLENGES;
		this.maxValue = 20;
		bossBar = Bukkit.createBossBar("AnvilRain", BarColor.BLUE, BarStyle.SOLID);
		bossBar.setVisible(false);
	}

	@Override
	public String getChallengeName() {
		return "anvilrain";
	}

	private void updateBossBar() {

		Utils.forEachPlayerOnline(bossBar::addPlayer);

		if (!enabled) {
			bossBar.setVisible(false);
			return;
		}

		bossBar.setVisible(true);
		bossBar.setProgress(1);

		String title;

		if (!Challenges.timerIsStarted()) {
			title = "§cTimer is paused";
			bossBar.setColor(BarColor.RED);
		} else {
			title = "§7Anvils per chunk §9" + value + " §8● §7Anvils/s §9" + average;
			bossBar.setColor(BarColor.BLUE);
		}

		bossBar.setTitle(title);


	}

	@Override
	public void onValueChange(ChallengeEditEvent event) {
		updateBossBar();
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		this.nextActionInSeconds = 1;
		updateBossBar();
	}

	@Override
	public void onDisable(ChallengeEditEvent event) {
		for (World currentWorld : Bukkit.getWorlds()) {
			for (FallingBlock currentFallingBlock : currentWorld.getEntitiesByClass(FallingBlock.class)) {
				if (!currentFallingBlock.getBlockData().getMaterial().name().toLowerCase().contains("anvil")) continue;
				currentFallingBlock.remove();
			}
		}
		updateBossBar();
	}

	@Override
	public void onTimerStart() {
		this.nextActionInSeconds = 1;
	}

	@Override
	public void onTimeActivation() {

		this.nextActionInSeconds = 1;
		updateBossBar();

		Bukkit.getScheduler().runTask(Challenges.getInstance(), () -> {

			List<Chunk> chunks = new ArrayList<>();
			int height = 0;

			for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
				if (currentPlayer.getGameMode() == GameMode.SPECTATOR) continue;

				if ((currentPlayer.getLocation().getBlockY() + 20) > height) height = currentPlayer.getLocation().getBlockY() + 25;

				Chunk chunk = currentPlayer.getLocation().getChunk();
				World world = chunk.getWorld();

				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						Chunk currentChunk = world.getChunkAt(chunk.getX() + i, chunk.getZ() + j);
						if (chunks.contains(currentChunk)) continue;
						chunks.add(currentChunk);
					}
				}
			}

			int spawned = 0;
			for (Chunk currentChunk : chunks) {
				spawnAnvils(currentChunk, height);
				spawned += value;
			}
			average = spawned;

		});

	}

	private void spawnAnvils(Chunk chunk, int height) {
		for (int i = 0; i < value; i++) {
			Location blockLocation = getRandomLocationInChunk(chunk);
			blockLocation.setY(height);
			spawnAnvil(blockLocation.add(0, 1, 0));
		}
	}

	private Location getRandomLocationInChunk(Chunk chunk) {
		int x = getRandomIntInRadius(16);
		int z = getRandomIntInRadius(16);
		return chunk.getBlock(x, 0, z).getLocation().add(0.5, 0, 0.5);
	}

	private int getRandomIntInRadius(int radius) {
		return random.nextInt(radius);
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
		if (!((FallingBlock)event.getEntity()).getBlockData().getMaterial().name().toLowerCase().contains("anvil")) return;
		if (event.getBlock().getLocation().clone().add(0, -1, 0).getBlock().getType() == Material.AIR) return;
		Location location = event.getBlock().getLocation().add(0,-1, 0);

		if (location.getBlock().getType() == Material.GRASS_PATH || Arrays.asList(Utils.getFlowers()).contains(location.getBlock().getType())) {
			location.getBlock().setType(Material.AIR, true);
		}

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
