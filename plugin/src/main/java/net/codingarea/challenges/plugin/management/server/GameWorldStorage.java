package net.codingarea.challenges.plugin.management.server;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;
import net.codingarea.challenges.plugin.Challenges;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class GameWorldStorage {

  private final List<World> worlds = new LinkedList<>();
  private World overworld;
  private World nether;
  private World end;

  public void enable() {
    for (int i = 0; i < 3 && i < Bukkit.getWorlds().size(); i++) {
      World world = Bukkit.getWorlds().get(i);
      switch (i) {
        case 0: {
          overworld = world;
          worlds.add(world);
        } break;
        case 1: {
          if (world.getEnvironment() == Environment.NETHER) {
            nether = world;
            worlds.add(world);
          } else if (world.getEnvironment() == Environment.THE_END) {
            end = world;
            worlds.add(world);
          }
        } break;
        case 2: {
          if (world.getEnvironment() == Environment.THE_END) {
            end = world;
            worlds.add(world);
          }
        } break;
      }
    }
  }

  public World getWorld(@Nullable Environment environment) {
    if (environment == null) return null;
    switch (environment) {
      case NORMAL: return overworld;
      case NETHER: return nether;
      case THE_END: return end;
      case CUSTOM: return Challenges.getInstance().getWorldManager().getExtraWorld();
    }
    return null;
  }

  public List<World> getGameWorlds() {
    return Collections.unmodifiableList(worlds);
  }

}
