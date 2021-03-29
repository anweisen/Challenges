package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.anntations.Since;
import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.challenges.type.TimedChallenge;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import net.codingarea.challenges.plugin.utils.misc.ListBuilder;
import net.codingarea.challenges.plugin.utils.misc.VersionHelper;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class TsunamiChallenge extends TimedChallenge {

	private int waterHeight = 0, lavaHeight = 0;
	private final List<Chunk> loadedChunks = new ArrayList<>();

	public TsunamiChallenge() {
		super(MenuType.CHALLENGES, 6 * 3);
	}

	@Override
	protected void onEnable() {
		bossbar.setContent((bossbar, player) -> {
			bossbar.setProgress(1);
			bossbar.setStyle(BarStyle.SOLID);
			Environment environment = player.getWorld().getEnvironment();
			if (environment == Environment.NORMAL) {
				bossbar.setColor(BarColor.BLUE);
				bossbar.setTitle("§7Waterheight: §9" + waterHeight);
			}
			else if (environment == Environment.NETHER) {
				bossbar.setColor(BarColor.RED);
				bossbar.setTitle("§7Lavaheight: §c" + lavaHeight);
			}
			else bossbar.setVisible(false);
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
		return new ItemBuilder(Material.BLUE_ICE, Message.forName("item-tsunami-challenge"));
	}

	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return Message.forName("item-time-seconds-description").asArray(getValue() * 1);
	}

	@Override
	protected int getSecondsUntilNextActivation() {
		return getValue() * 1;
	}

	@Override
	protected void onTimeActivation() {
		restartTimer();

		for (World world : Bukkit.getWorlds()) {
			if (!world.getPlayers().isEmpty()) {
				if (world.getEnvironment() == Environment.NORMAL) {
					if (waterHeight >= world.getMaxHeight()) return;
					waterHeight++;
				} else if (world.getEnvironment() == Environment.NETHER) {
					if (lavaHeight >= world.getMaxHeight()) return;
					lavaHeight++;
				}
			}
		}

		bossbar.update();

		loadedChunks.clear();
		for (Chunk chunk : getChunksToFlood()) {
			floodChunk(chunk);
		}

	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	private void onPlayerMove(PlayerMoveEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.SPECTATOR || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		if (!shouldExecuteEffect()) return;
		if (event.getTo() == null) return;
		Chunk newChunk = event.getTo().getChunk();
		if (event.getFrom().getChunk() == newChunk) return;
		for (Chunk chunk : getChunksAroundChunk(newChunk)) {
			floodChunk(chunk);
		}
	}

	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event) {
		loadedChunks.remove(event.getChunk());
	}

	private void floodChunk(@Nonnull Chunk chunk) {
		if (loadedChunks.contains(chunk)) return;
		loadedChunks.add(chunk);
		if (chunk.getWorld().getEnvironment() == Environment.THE_END) return;

		boolean overworld = chunk.getWorld().getEnvironment() == Environment.NORMAL;

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

			for (int x = 0; x < 16; x++) {
				int finalX = x;
				Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

					for (int z = 0; z < 16; z++) {
						int finalZ = z;

							if (!chunk.isLoaded()) return;
							Bukkit.getScheduler().runTask(plugin, () -> {
							for (int y = (overworld ? waterHeight : lavaHeight); y > VersionHelper.getMinBuildHeight(chunk.getWorld()); y--) {
									Block block = chunk.getBlock(finalX, y-1, finalZ);
									Material type = block.getType();
									if (type != Material.WATER && type != Material.LAVA && type.isAir() || (overworld && type == Material.LAVA)) {
										block.setType(overworld ? Material.WATER : Material.LAVA, false);
									}
							}

						});

					}
				});

			}
		});

	}

	private List<Chunk> getChunksToFlood() {
		return new ListBuilder<Chunk>().fill(chunkListBuilder -> {
			Bukkit.getOnlinePlayers().forEach(player -> {
				if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE) return;
				chunkListBuilder.addAllIfNotContains(getChunksAroundChunk(player.getLocation().getChunk()));
			});
		}).build();
	}

	private List<Chunk> getChunksAroundChunk(@Nonnull Chunk origin) {
		return new ListBuilder<Chunk>().fill(builder -> {
			for (int x = -1; x < 2; x++) {
				for (int z = -1; z < 2; z++) {
					builder.addIfNotContains(origin.getWorld().getChunkAt(origin.getX() + x, origin.getZ() + z));
				}
			}
		}).build();
	}

	@Override
	public void writeGameState(@Nonnull Document document) {
		document.set("waterHeight", waterHeight);
		document.set("lavaHeight", lavaHeight);
	}

	@Override
	public void loadGameState(@Nonnull Document document) {
		waterHeight = document.getInt("waterHeight");
		lavaHeight = document.getInt("lavaHeight");
	}

}