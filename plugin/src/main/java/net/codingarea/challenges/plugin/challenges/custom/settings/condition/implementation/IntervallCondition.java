package net.codingarea.challenges.plugin.challenges.custom.settings.condition.implementation;

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

    execute(null, "1");

    if (currentTime % 2 == 0) {
      execute(null, "2");
    }
    if (currentTime % 5 == 0) {
      execute(null, "5");
    }
    if (currentTime % 10 == 0) {
      execute(null, "10");
    }
    if (currentTime % 20 == 0) {
      execute(null, "20");
    }
    if (currentTime % 30 == 0) {
      execute(null, "30");
    }
    if (currentTime % 60 == 0) {
      execute(null, "60");
    }
    if (currentTime % 120 == 0) {
      execute(null, "120");
    }
    if (currentTime % 180 == 0) {
      execute(null, "120");
    }
    if (currentTime % 180 == 0) {
      execute(null, "240");
    }
    if (currentTime % 300 == 0) {
      execute(null, "300");
    }
  }

}
