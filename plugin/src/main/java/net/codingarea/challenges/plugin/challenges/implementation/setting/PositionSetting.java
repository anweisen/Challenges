package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.PotionBuilder;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class PositionSetting extends Setting implements PlayerCommand {

	private final Map<String, Location> positions = new HashMap<>();

	public PositionSetting() {
		super(MenuType.SETTINGS, true);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new PotionBuilder(Material.POTION, Message.forName("item-position-setting")).setColor(Color.BLUE);
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

	private String getWorldName(@Nonnull Location location) {
		if (location.getWorld() == null) return "?";
		switch (location.getWorld().getEnvironment()) {
			default:        return "Overworld";
			case NETHER:    return "Nether";
			case THE_END:   return "End";
		}
	}

	@Override
	public void loadGameState(@Nonnull Document document) {
		for (String name : document.keys()) {
			positions.put(name, document.getLocation(name));
		}
	}

	@Override
	public void writeGameState(@Nonnull Document document) {
		positions.forEach(document::set);
	}

}
