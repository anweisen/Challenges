package net.codingarea.challenges.plugin.challenges.custom.settings.action;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface TargetEntitiesChallengeAction extends IChallengeAction {

  @Override
  default void execute(Entity entity, Map<String, String[]> subActions) {
    for (Entity target : getTargets(entity, subActions)) {
      executeFor(target, subActions);
    }
  }

  void executeFor(Entity entity, Map<String, String[]> subActions);

  static List<Entity> getTargets(Entity conditionTarget, Map<String, String[]> subActions) {
    if (!subActions.containsKey(AbstractChallengeCondition.TARGET_ENTITY)) {
      return Lists.newLinkedList();
    }
    String targetEntity = subActions.get(AbstractChallengeCondition.TARGET_ENTITY)[0];

    switch (targetEntity) {
      case "random_player":
        List<Player> players = ChallengeAPI.getPlayingPlayers();
        if (players.isEmpty()) return new LinkedList<>();
        return Collections.singletonList(players.get(random.nextInt(players.size())));
      case "every_player":
        return Lists.newLinkedList(ChallengeAPI.getPlayingPlayers());
      case "current_player":
        return conditionTarget instanceof Player ? Lists.newArrayList(conditionTarget) : Lists.newLinkedList();
      case "every_mob":
        List<Entity> everyList = Lists.newLinkedList();
        for (World world : Bukkit.getWorlds()) {
          everyList.addAll(world.getLivingEntities());
        }
        return everyList;
      case "every_mob_except_current":
        List<Entity> exceptList = Lists.newLinkedList();
        for (World world : Bukkit.getWorlds()) {
          exceptList.addAll(world.getLivingEntities());
        }
        exceptList.remove(conditionTarget);
        return exceptList;
      case "every_mob_except_players":
        List<Entity> noPlayers = Lists.newLinkedList();
        for (World world : Bukkit.getWorlds()) {
          for (LivingEntity entity : world.getLivingEntities()) {
            if (entity.getType() == EntityType.PLAYER) continue;
            noPlayers.add(entity);
          }
        }

        return noPlayers;
    }
    if (conditionTarget == null) {
      return Lists.newLinkedList();
    }
    return Lists.newArrayList(conditionTarget);
  }

}
