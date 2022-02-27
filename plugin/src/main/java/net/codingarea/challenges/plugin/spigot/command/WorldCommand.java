package net.codingarea.challenges.plugin.spigot.command;

import java.util.Arrays;
import java.util.List;
import net.codingarea.challenges.plugin.challenges.implementation.setting.PositionSetting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class WorldCommand implements PlayerCommand, TabCompleter {

  @Override
  public void onCommand(@NotNull Player player, @NotNull String[] args) throws Exception {

    if (args.length < 1) {
      Message.forName("syntax").send(player, Prefix.POSITION, "world <world>");
      return;
    }

    String worldName = args[0];
    Environment environment = PositionSetting.getWorldEnvironment(worldName);

    if (environment == null) {
      Message.forName("syntax").send(player, Prefix.POSITION, "world <world>");
      return;
    }

    World world = PositionSetting.getWorldByEnvironment(environment);
    if (world == null) {
      Message.forName("syntax").send(player, Prefix.POSITION, "world <world>");
      return;
    }
    Location location = world.getSpawnLocation();
    Location bedSpawnLocation = player.getBedSpawnLocation();
    if (bedSpawnLocation != null && bedSpawnLocation.getWorld() == world) {
      location = bedSpawnLocation;
    } else if (world.getEnvironment() == Environment.THE_END) {
      location = world.getHighestBlockAt(0, 0).getLocation().add(0.5, 1, 0.5);
    }

    Message.forName("command-world-teleport").send(player, Prefix.CHALLENGES, PositionSetting.getWorldName(location));
    player.teleport(location);
  }

  @Nullable
  @Override
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String alias, @NotNull String[] args) {
    return Arrays.asList("Overworld", "Nether", "End");
  }

}
