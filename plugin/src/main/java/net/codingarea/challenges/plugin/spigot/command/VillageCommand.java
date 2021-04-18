package net.codingarea.challenges.plugin.spigot.command;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.StructureType;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public class VillageCommand implements PlayerCommand {

	@Override
	public void onCommand(@Nonnull Player player, @Nonnull String[] args) throws Exception {
		player.setNoDamageTicks(10);

		Bukkit.getScheduler().runTask(Challenges.getInstance(), () -> {

			Location village = player.getWorld().locateNearestStructure(player.getLocation(), StructureType.VILLAGE, 5000, true);
			if (village == null) {
				Message.forName("command-village-not-found").send(player, Prefix.CHALLENGES);
				return;
			}

			village = player.getWorld().getHighestBlockAt(village).getLocation().add(0.5, 1, 0.5);
			village.getChunk().load(true);

			Location finalVillage = village;
			Bukkit.getScheduler().runTask(Challenges.getInstance(), () -> {
				player.teleport(finalVillage);
				SoundSample.TELEPORT.play(player);
				Message.forName("command-village-teleport").send(player, Prefix.CHALLENGES);
			});

		});

	}

}