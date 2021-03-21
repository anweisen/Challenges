package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class TopCommandSetting extends Setting implements PlayerCommand {

	public TopCommandSetting() {
		super(MenuType.SETTINGS);
	}

	@Override
	public void onCommand(@Nonnull Player player, @Nonnull String[] args) {
		if (!isEnabled()) {
			Message.forName("feature-disabled").send(player, Prefix.CHALLENGES);
			return;
		}

		World world = player.getWorld();
		Environment environment = world.getEnvironment();
		Location playerLocation = player.getLocation();
		if (environment == Environment.NORMAL) {
			Message.forName("top-to-surface").send(player, Prefix.CHALLENGES);

			Location location = player.getWorld().getHighestBlockAt(playerLocation).getLocation().add(0, 1, 0);
			location.setYaw(playerLocation.getYaw());
			location.setPitch(playerLocation.getPitch());
			player.teleport(location);

		} else {
			Message.forName("top-to-overworld").send(player, Prefix.CHALLENGES);

			if (environment == Environment.NETHER) {
				Location location = playerLocation.clone();
				world = Bukkit.getWorlds().get(0);
				location.setWorld(world);
				location.multiply(8);
				location = world.getHighestBlockAt(location).getLocation().add(0, 1, 0);
				player.teleport(location);

			} else {
				world = Bukkit.getWorlds().get(0);
				Location location = player.getBedSpawnLocation();
				if (location == null) location = world.getSpawnLocation();
				player.teleport(location);
			}

		}
		SoundSample.TELEPORT.play(player);

	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.MAGENTA_GLAZED_TERRACOTTA, Message.forName("top-command-setting"));
	}


}