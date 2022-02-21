package net.codingarea.challenges.plugin.challenges.custom.settings;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.CancelEventAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.IChallengeCondition;
import net.codingarea.challenges.plugin.challenges.type.abstraction.AbstractChallenge;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class ChallengeExecutionData {

  private final IChallengeCondition condition;
  private final Map<String, List<String>> conditionData;
  private Entity entity;
  private Runnable cancelAction;
  private int timesExecuting;

  public ChallengeExecutionData(
      IChallengeCondition condition) {
    this.condition = condition;
    this.conditionData = new HashMap<>();
  }

  public ChallengeExecutionData data(String key, String data) {
    conditionData.put(key, Collections.singletonList(data));
    return this;
  }

  public ChallengeExecutionData data(String key, List<String> data) {
    conditionData.put(key, data);
    return this;
  }

  public ChallengeExecutionData data(String key, String... data) {
    conditionData.put(key, Arrays.asList(data));
    return this;
  }

  public ChallengeExecutionData block(Material material) {
    return data(AbstractChallengeCondition.BLOCK, AbstractChallengeCondition.ANY, material.name());
  }

  public ChallengeExecutionData entityType(EntityType type) {
    return data(AbstractChallengeCondition.ENTITY_TYPE, AbstractChallengeCondition.ANY, type.name());
  }

  public ChallengeExecutionData entity(Entity entity) {
    this.entity = entity;
    return this;
  }

  public ChallengeExecutionData cancelAction(Runnable cancelAction) {
    this.cancelAction = cancelAction;
    return this;
  }

  public ChallengeExecutionData event(Cancellable event) {
    this.cancelAction = () -> event.setCancelled(true);
    return this;
  }

  public ChallengeExecutionData times(int timesExecuting) {
    this.timesExecuting = timesExecuting;
    return this;
  }

  public void execute() {
    if (ChallengeAPI.isStarted() && !ChallengeAPI.isWorldInUse()) {
      if (entity instanceof Player && AbstractChallenge.ignorePlayer(((Player) entity))) {
        return;
      }
      if (cancelAction != null) {
        CancelEventAction.onPreCondition();
      }
      Challenges.getInstance().getCustomChallengesLoader().executeCondition(this);
      if (cancelAction != null && CancelEventAction.shouldCancel()) {
        cancelAction.run();
      }
    }
  }

  public IChallengeCondition getCondition() {
    return condition;
  }

  public Map<String, List<String>> getConditionData() {
    return conditionData;
  }

  public Entity getEntity() {
    return entity;
  }

  public int getTimesExecuting() {
    return timesExecuting;
  }

}
