package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class SnakeChallenge extends Setting {

	private ArrayList<Block> blocks = new ArrayList<>();

	public SnakeChallenge() {
		super(MenuType.CHALLENGES);
	}

	@Override
	protected void onEnable() {

	}

	@Override
	protected void onDisable() {
		blocks.clear();
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onMove(@Nonnull PlayerMoveEvent event) {
		if (!shouldExecuteEffect()) return;
		if (event.getFrom().getBlock().equals(event.getTo().getBlock())) return;
		if (event.getPlayer().getGameMode() == GameMode.SPECTATOR || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		if (ChallengeAPI.isWorldInUse()) return;

		Block from = event.getFrom().clone().subtract(0, 0.15, 0).getBlock();
		Block to = event.getTo().clone().subtract(0, 0.15,0).getBlock();

		if (from.getType().isSolid()) {
			from.setType(BlockUtils.getTerracotta(getPlayersWool(event.getPlayer())));
			blocks.add(from);
		}

		if (blocks.contains(to)) {
			Message.forName("snake-failed").broadcast(Prefix.CHALLENGES, NameHelper.getName(event.getPlayer()));
			event.getPlayer().damage(event.getPlayer().getHealth());
			return;
		}

		if (to.getType().isSolid()) {
			to.setType(Material.BLACK_TERRACOTTA);

			Block block = event.getPlayer().getLocation().getBlock();
			if (!block.getType().isSolid()) {
				block.breakNaturally();
			}
		}

	}

	public int getPlayersWool(Player player) {

		int i = 0;
		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			i++;
			if (i > 17) i = 0;
			if (currentPlayer == player) return i;
		}

		return 0;

	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.BLUE_TERRACOTTA, Message.forName("item-snake-challenge"));
	}

}