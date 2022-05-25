package net.codingarea.challenges.plugin.spigot.command;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.implementation.goal.ForceItemBattleGoal;
import net.codingarea.challenges.plugin.challenges.implementation.goal.ForceMobBattleGoal;
import net.codingarea.challenges.plugin.challenges.type.abstraction.AbstractChallenge;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.1.4
 */
public class ResultCommand implements PlayerCommand {
    @Override
    public void onCommand(@NotNull Player player, @NotNull String[] args) throws Exception {
        if (ChallengeAPI.isPaused()) {
            Message.forName("timer-not-started").send(player, Prefix.CHALLENGES);
            SoundSample.BASS_OFF.play(player);
            return;
        }

        ForceItemBattleGoal forceItemBattleInstance = AbstractChallenge.getFirstInstance(ForceItemBattleGoal.class);
        if(forceItemBattleInstance.isEnabled()) {
            forceItemBattleInstance.sendResult(player);
            return;
        }

        ForceMobBattleGoal forceMobBattleInstance = AbstractChallenge.getFirstInstance(ForceMobBattleGoal.class);
        if(forceMobBattleInstance.isEnabled()) {
            forceMobBattleInstance.sendResult(player);
            return;
        }

        Message.forName("command-result-no-battle-active").send(player, Prefix.CHALLENGES);
    }
}
