package net.codingarea.challenges.plugin.spigot.command;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.StructureType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class VillageCommand implements PlayerCommand {

	@Override
	public void onCommand(@Nonnull Player player, @Nonnull String[] args) throws Exception {
		player.setNoDamageTicks(10);

		Bukkit.getScheduler().runTaskAsynchronously(Challenges.getInstance(), () -> {

			Location village = player.getWorld().locateNearestStructure(player.getLocation(), StructureType.VILLAGE, 3000, true);
			if (village == null) {
				Message.forName("command-village-not-found").send(player, Prefix.CHALLENGES);
				return;
			}

			village = player.getWorld().getHighestBlockAt(village).getLocation().add(0.5, 1, 0.5);

			Location finalVillage = village;
			Bukkit.getScheduler().runTask(Challenges.getInstance(), () -> {
				finalVillage.getChunk().load(true);
				player.teleport(finalVillage);
				Message.forName("command-village-teleport").send(player, Prefix.CHALLENGES);
			});

			SoundSample.TELEPORT.play(player);

		});

	}

}