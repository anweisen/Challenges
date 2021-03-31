package net.codingarea.challenges.plugin.utils.misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public final class CommandHelper {

	private CommandHelper() {}

	public static List<Player> getPlayers(@Nonnull CommandSender sender, @Nonnull String input) {
		ArrayList<Player> list = new ArrayList<>();
		Location senderLocation = getSenderLocation(sender);

		switch (input) {
			case "@a": {
				list.addAll(Bukkit.getOnlinePlayers());
				break;
			}
			case "@r": {
				list.add(new ArrayList<Player>(Bukkit.getOnlinePlayers()).get(new Random().nextInt(Bukkit.getOnlinePlayers().size())));
				break;
			}
			case "@p": {
				if (senderLocation != null) {
					Player nearestPlayer = getNearestPlayer(senderLocation, 500);
					if (nearestPlayer != null) list.add(nearestPlayer);
				}
				break;
			}
			case "@s": {
				if (sender instanceof Player) list.add(((Player) sender));
				break;
			}
			default: {
				Player player = Bukkit.getPlayer(input);
				if (player != null) list.add(player);
			}

		}

		return list;
	}

	@Nullable
	public static Location getSenderLocation(@Nonnull CommandSender sender) {
		if (sender instanceof Player) return ((Player) sender).getLocation();
		if (sender instanceof BlockCommandSender) return ((BlockCommandSender) sender).getBlock().getLocation();
		return null;
	}

	@Nullable
	public static Player getNearestPlayer(@Nonnull Location location, double maxRadius) {
		if (location.getWorld() == null) return null;
		return (Player) location.getWorld().getNearbyEntities(location, maxRadius, maxRadius, maxRadius).stream()
				.filter(entity -> entity instanceof Player)
				.findFirst()
				.orElse(null);

	}

}