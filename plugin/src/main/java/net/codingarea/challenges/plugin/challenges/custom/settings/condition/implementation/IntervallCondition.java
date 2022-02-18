package net.codingarea.challenges.plugin.challenges.custom.settings.condition.implementation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.IChallengeCondition;
import net.codingarea.challenges.plugin.management.scheduler.policy.PlayerCountPolicy;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class IntervallCondition implements IChallengeCondition {

  public IntervallCondition() {
    Challenges.getInstance().getScheduler().register(this);
  }

  @ScheduledTask(ticks = 20, playerPolicy = PlayerCountPolicy.ALWAYS)
  public void onSecond() {
    long currentTime = Challenges.getInstance().getChallengeTimer().getTime();

    HashMap<String, List<String>> data = new HashMap<>();
    List<String> list = new LinkedList<>();
    data.put("time", list);
    list.add("1");


    if (currentTime % 2 == 0) {
      list.add("2");
    }
    if (currentTime % 5 == 0) {
      list.add("5");
    }
    if (currentTime % 10 == 0) {
      list.add("10");
    }
    if (currentTime % 20 == 0) {
      list.add("20");
    }
    if (currentTime % 30 == 0) {
      list.add("30");
    }
    if (currentTime % 60 == 0) {
      list.add("60");
    }
    if (currentTime % 120 == 0) {
      list.add("120");
    }
    if (currentTime % 180 == 0) {
      list.add("180");
    }
    if (currentTime % 180 == 0) {
      list.add("180");
    }
    if (currentTime % 300 == 0) {
      list.add("300");
    }

    execute(null, data);
  }

}
