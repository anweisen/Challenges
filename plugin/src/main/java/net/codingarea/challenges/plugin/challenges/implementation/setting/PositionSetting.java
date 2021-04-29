package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeConfigHelper;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import net.codingarea.challenges.plugin.utils.misc.ParticleUtils;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.Particle.DustOptions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class PositionSetting extends Setting implements PlayerCommand, TabCompleter {

	private final Map<String, Location> positions = new HashMap<>();
	private final boolean particleLines;

	public PositionSetting() {
		super(MenuType.SETTINGS, true);
		particleLines = ChallengeConfigHelper.getSettingsDocument().getBoolean("position-particle-lines");
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
			if (positions.entrySet().stream().noneMatch(entry -> player.getWorld() == entry.getValue().getWorld())) {
				Message.forName("no-positions").send(player, Prefix.POSITION, "position <name>");
				return;
			}

			positions.entrySet().stream()
					.filter(entry -> player.getWorld() == entry.getValue().getWorld())
					.sorted(Comparator.<Entry<String, Location>>comparingDouble(entry -> entry.getValue().distance(player.getLocation())).reversed())
					.forEach(entry -> {

				Location location = entry.getValue();
				Message.forName("position").send(player, Prefix.POSITION, location.getBlockX(), location.getBlockY(), location.getBlockZ(), getWorldName(location), entry.getKey(), (int) location.distance(player.getLocation()));
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
				playParticleLine(player, position);
			} else if (ChallengeAPI.isPaused()) {
				Message.forName("timer-not-started").send(player, Prefix.POSITION);
				SoundSample.BASS_OFF.play(player);
			} else {
				positions.put(name, position = player.getLocation());
				Message.forName("position-set").broadcast(Prefix.POSITION, position.getBlockX(), position.getBlockY(), position.getBlockZ(), getWorldName(position), name, NameHelper.getName(player));
				SoundSample.BASS_ON.play(player);
				broadcastParticleLine(position);
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

	private void broadcastParticleLine(@Nonnull Location location) {
		broadcast(player -> playParticleLine(player, location));
	}

	private void playParticleLine(@Nonnull Player player, @Nonnull Location position) {
		if (!particleLines) return;
		if (player.getWorld() != position.getWorld()) return;

		// Defining target location to
		Location target = position.clone().add(0, 0.3, 0);

		final int[] current = {0};
		Bukkit.getScheduler().runTaskTimer(plugin, task -> {
			current[0]++;
			if (current[0] >= 10) task.cancel();
			ParticleUtils.drawLine(player, player.getLocation(), target, Particle.REDSTONE, new DustOptions(Color.LIME, 1), 1, 0.5, 50);
		}, 0, 10);
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
