package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.ListBuilder;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class PositionSetting extends Setting implements PlayerCommand, TabCompleter {

	private final Map<String, Location> positions = new HashMap<>();

	public PositionSetting() {
		super(MenuType.SETTINGS, true);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.BLUE_BANNER, Message.forName("item-position-setting"));
	}

	@Override
	public void onCommand(@Nonnull Player player, @Nonnull String[] args) {
		if (!isEnabled()) {
			Message.forName("positions-disabled").send(player, Prefix.POSITION);
			SoundSample.BASS_OFF.play(player);
			return;
		}

		if (args.length == 0) {
			if (positions.isEmpty()) {
				Message.forName("no-positions").send(player, Prefix.POSITION, "position <name>");
				return;
			}

			positions.forEach((name, location) -> {
				if (location.getWorld() != player.getLocation().getWorld()) return;
				Message.forName("position").send(player, Prefix.POSITION, location.getBlockX(), location.getBlockY(), location.getBlockZ(), getWorldName(location), name, (int) location.distance(player.getLocation()));
			});
		} else if (args.length == 1) {
			String name = args[0].toLowerCase();
			Location position = positions.get(name);
			if (position != null) {
				if (position.getWorld() != player.getLocation().getWorld()) {
					Message.forName("position-other-world").send(player, Prefix.POSITION, getWorldName(position));
					SoundSample.BASS_OFF.play(player);
					return;
				}
				 Message.forName("position").send(player, Prefix.POSITION, position.getBlockX(), position.getBlockY(), position.getBlockZ(), getWorldName(position), name, (int) position.distance(player.getLocation()));
			} else if (ChallengeAPI.isPaused()) {
				Message.forName("timer-not-started").send(player, Prefix.POSITION);
				SoundSample.BASS_OFF.play(player);
			} else {
				positions.put(name, position = player.getLocation());
				Message.forName("position-set").broadcast(Prefix.POSITION, position.getBlockX(), position.getBlockY(), position.getBlockZ(), getWorldName(position), name, NameHelper.getName(player));
				SoundSample.BASS_ON.play(player);
			}

		} else {
			Message.forName("syntax").send(player, Prefix.POSITION, "position [name]");
		}
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
		if (args.length < 1) return new ArrayList<>();
		return Utils.filterRecommendations(args[0], positions.keySet().toArray(new String[0]));
	}

	private String getWorldName(@Nonnull Location location) {
		if (location.getWorld() == null) return "?";
		switch (location.getWorld().getEnvironment()) {
			default:        return "Overworld";
			case NETHER:    return "Nether";
			case THE_END:   return "End";
		}
	}

	public boolean containsPosition(@Nonnull String name) {
		return positions.containsKey(name);
	}

	@Override
	public void loadGameState(@Nonnull Document document) {
		for (String name : document.keys()) {
			positions.put(name, document.getSerializable(name, Location.class));
		}
	}

	@Override
	public void writeGameState(@Nonnull Document document) {
		positions.forEach(document::set);
	}

}
