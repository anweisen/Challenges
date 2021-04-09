package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.annotations.Since;
import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.challenges.type.TimedChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.bukkit.wrapper.BukkitReflectionUtils;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.ListBuilder;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class TsunamiChallenge extends TimedChallenge {

	public static final int RANGE = 5;

	private final List<Chunk> floodedChunks = new ArrayList<>();

	private int waterHeight = 0,
				lavaHeight = 0;

	public TsunamiChallenge() {
		super(MenuType.CHALLENGES, 1, 40, 4);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.ICE, Message.forName("item-tsunami-challenge"));
	}

	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return Message.forName("item-time-seconds-description").asArray(getValue() * 15);
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChallengeSecondsValueChangeTitle(this, getValue() * 15);
	}

	@Override
	protected void onEnable() {
		bossbar.setContent((bossbar, player) -> {
			World world = player.getWorld();
			Environment environment = world.getEnvironment();
			int height = environment == Environment.NETHER ? waterHeight : lavaHeight;
			if (height < world.getMaxHeight())
				bossbar.setProgress(getProgress());

			if (environment == Environment.NORMAL) {
				bossbar.setColor(BarColor.BLUE);
				bossbar.setTitle(Message.forName("bossbar-tsunami-water").asString(waterHeight));
			} else if (environment == Environment.NETHER) {
				bossbar.setColor(BarColor.RED);
				bossbar.setTitle(Message.forName("bossbar-tsunami-lava").asString(lavaHeight));
			} else {
				bossbar.setVisible(false);
			}
		});
		bossbar.show();
	}

	@Override
	protected void onDisable() {
		bossbar.hide();
	}

	@Override
	protected int getSecondsUntilNextActivation() {
		return getValue() * 15;
	}

	@Override
	protected void handleCountdown() {
		bossbar.update();
	}

	@Override
	protected void onTimeActivation() {
		restartTimer();

		for (World world : Bukkit.getWorlds()) {
			if (!world.getPlayers().isEmpty()) {
				if (world.getEnvironment() == Environment.NORMAL) {
					if (waterHeight >= world.getMaxHeight()) continue;
					waterHeight++;
				} else if (world.getEnvironment() == Environment.NETHER) {
					if (lavaHeight >= world.getMaxHeight()) continue;
					lavaHeight++;
				}
			}
		}

		bossbar.update();
		floodedChunks.clear();
		for (Chunk chunk : getChunksToFlood()) {
			floodChunk(chunk, false);
		}

	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	private void onPlayerMove(PlayerMoveEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getPlayer()))  return;
		if (event.getTo() == null) return;
		Chunk newChunk = event.getTo().getChunk();
		if (event.getFrom().getChunk() == newChunk) return;
		for (Chunk chunk : getChunksAroundChunk(newChunk)) {
			floodChunk(chunk, true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onTeleport(@Nonnull PlayerTeleportEvent event) {
		if (!shouldExecuteEffect()) return;
		if (event.getTo() == null) return;
		bossbar.update();

		Chunk newChunk = event.getTo().getChunk();
		for (Chunk chunk : getChunksAroundChunk(newChunk)) {
			floodChunk(chunk, true);
		}
	}

	@EventHandler
	public void onChunkUnload(@Nonnull ChunkUnloadEvent event) {
		floodedChunks.remove(event.getChunk());
	}

	private void floodChunk(@Nonnull Chunk chunk, boolean discovered) {
		if (floodedChunks.contains(chunk)) return;
		floodedChunks.add(chunk);
		if (chunk.getWorld().getEnvironment() == Environment.THE_END) return;

		boolean overworld = chunk.getWorld().getEnvironment() == Environment.NORMAL;
		int height = overworld ? waterHeight : lavaHeight;
		if (height >= chunk.getWorld().getMaxHeight()) return;

		floodChunk0(chunk, discovered ? null : height - 1, height, overworld, (delay, task) -> Bukkit.getScheduler().runTaskLater(plugin, task, delay));
	}

	private void floodChunk0(@Nonnull Chunk chunk, @Nullable Integer givenStartAt, int height, boolean overworld, @Nonnull BiConsumer<Integer, Runnable> executor) {
		int startAt = givenStartAt != null ? Math.max(BukkitReflectionUtils.getMinHeight(chunk.getWorld()), givenStartAt) : BukkitReflectionUtils.getMinHeight(chunk.getWorld()) + 1;
		Map<Integer, List<Block>> blocksByDelay = new HashMap<>();
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					int relativeY = 0;
					for (int y = startAt; y <= height; y++, relativeY++) {
						List<Block> blocks = blocksByDelay.computeIfAbsent(relativeY / 10, key -> new ArrayList<>(16));
						Block block = chunk.getBlock(x, y -1, z);
						Material type = block.getType();
						if (type != Material.WATER && type != Material.LAVA && block.isPassable() || (overworld && type == Material.LAVA))
							blocks.add(block);
					}
				}
			}
			blocksByDelay.forEach((delay, blocks) -> {
				executor.accept(delay, () -> {
					for (Block block : blocks) {
						block.setType(overworld ? Material.WATER : Material.LAVA, false);
					}
				});
			});
		});
	}

	private List<Chunk> getChunksToFlood() {
		return new ListBuilder<Chunk>().fill(chunkListBuilder -> {
			broadcastFiltered(player -> chunkListBuilder.addAllIfNotContains(getChunksAroundChunk(player.getLocation().getChunk())));
		}).build();
	}

	private List<Chunk> getChunksAroundChunk(@Nonnull Chunk origin) {
		return new ListBuilder<Chunk>().fill(builder -> {
			for (int x = -RANGE; x <= RANGE; x++) {
				for (int z = -RANGE; z <= RANGE; z++) {
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