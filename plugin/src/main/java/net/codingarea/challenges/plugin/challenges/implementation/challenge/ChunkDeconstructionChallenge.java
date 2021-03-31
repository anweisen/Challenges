package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.TimedChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.ListBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ChunkDeconstructionChallenge extends TimedChallenge {

	public ChunkDeconstructionChallenge() {
		super(MenuType.CHALLENGES, 1, 60);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.DIAMOND_PICKAXE, Message.forName("item-chunk-deconstruction-challenge"));
	}

	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return Message.forName("item-time-seconds-description").asArray(getValue());
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChallengeSecondsValueChangeTitle(this, getValue() * 60);
	}

	@Override
	protected void onValueChange() {
		restartTimer();
	}

	@Override
	protected int getSecondsUntilNextActivation() {
		return getValue();
	}

	@Override
	protected void onTimeActivation() {
		if (ChallengeAPI.isWorldInUse()) return;

		for (Chunk chunk : getChunksToDeconstruct()) {
			if (!chunk.isLoaded()) continue;
			deconstructChunk(chunk);
		}

		restartTimer();
	}

	private void deconstructChunk(@Nonnull Chunk chunk) {

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				deconstructAtLocation(chunk.getBlock(x, 0, z));
			}
		}

	}

	private void deconstructAtLocation(@Nonnull Block block) {
		Block lowestBreakableBlock = getLowestBreakableBlock(block);
		if (lowestBreakableBlock == null) return;

		Bukkit.getScheduler().runTask(plugin, () -> {
			lowestBreakableBlock.setType(Material.AIR, true);
		});
	}

	@Nullable
	private Block getLowestBreakableBlock(@Nonnull Block block) {
		Location location = block.getLocation();
		location.setY(block.getWorld().getMaxHeight());

		while (location.getY() >= 0 && !isBreakable(location.getBlock())) {
			location.subtract(0, 1, 0);
		}

		Block currentBlock = location.getBlock();
		return currentBlock.getType() == Material.BEDROCK || currentBlock.isLiquid() ? null : location.getBlock();
	}

	private boolean isBreakable(@Nonnull Block block) {
		return block.getType() != Material.BEDROCK && !block.getType().isAir() && !block.isLiquid();
	}

	private List<Chunk> getChunksToDeconstruct() {
		return new ListBuilder<Chunk>().fill(builder ->
				Bukkit.getOnlinePlayers().forEach(player -> {
					if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE) return;
					builder.addIfNotContains(player.getLocation().getChunk());
				})
		).build();
	}

}