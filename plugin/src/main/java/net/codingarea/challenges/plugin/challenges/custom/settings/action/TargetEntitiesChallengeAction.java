package net.codingarea.challenges.plugin.challenges.custom.settings.action;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.custom.settings.ChallengeExecutionData;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.AbstractChallengeTrigger;
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
  default void execute(
      ChallengeExecutionData executionData,
      Map<String, String[]> subActions) {
    for (Entity target : getTargets(entity, subActions)) {
      executeFor(target, subActions);
    }
  }

  void executeFor(Entity entity, Map<String, String[]> subActions);

  static List<Entity> getTargets(Entity triggerTarget, Map<String, String[]> subActions) {
    if (!subActions.containsKey(AbstractChallengeTrigger.TARGET_ENTITY)) {
      return Lists.newLinkedList();
    }
    String targetEntity = subActions.get(AbstractChallengeTrigger.TARGET_ENTITY)[0];

    switch (targetEntity) {
      case "random_player":
        List<Player> players = ChallengeAPI.getIngamePlayers();
        if (players.isEmpty()) return new LinkedList<>();
        return Collections.singletonList(players.get(random.nextInt(players.size())));
      case "every_player":
        return Lists.newLinkedList(ChallengeAPI.getIngamePlayers());
      case "current_player":
        return triggerTarget instanceof Player ? Lists.newArrayList(triggerTarget) : Lists.newLinkedList();
      case "every_mob":
        List<Entity> everyList = Lists.newLinkedList();
        for (World world : ChallengeAPI.getGameWorlds()) {
          everyList.addAll(world.getLivingEntities());
        }
        return everyList;
      case "every_mob_except_current":
        List<Entity> exceptList = Lists.newLinkedList();
        for (World world : ChallengeAPI.getGameWorlds()) {
          exceptList.addAll(world.getLivingEntities());
        }
        exceptList.remove(triggerTarget);
        return exceptList;
      case "every_mob_except_players":
        List<Entity> noPlayers = Lists.newLinkedList();
        for (World world : ChallengeAPI.getGameWorlds()) {
          for (LivingEntity entity : world.getLivingEntities()) {
            if (entity.getType() == EntityType.PLAYER) continue;
            noPlayers.add(entity);
          }
        }

        return noPlayers;
    }
    if (triggerTarget == null) {
      return Lists.newLinkedList();
    }
    return Lists.newArrayList(triggerTarget);
  }

}
