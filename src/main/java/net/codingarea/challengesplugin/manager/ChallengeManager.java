package net.codingarea.challengesplugin.manager;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.GeneralChallenge;
import net.codingarea.challengesplugin.manager.goal.GoalManager;
import net.codingarea.challengesplugin.manager.loader.ChallengeLoader;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public class ChallengeManager {

    @Getter private Challenges plugin;
    @Getter private final List<GeneralChallenge> challenges;
    @Getter private final ChallengeLoader loader;
    @Getter private final GoalManager goalManager;

    public ChallengeManager(Challenges plugin) {
        this.plugin = plugin;
        challenges = new ArrayList<>();
        goalManager = new GoalManager();
        loader = new ChallengeLoader(this);
    }

    public void init() {
        loader.loadChallenges();
        generateInventories();
    }

    public void generateInventories() {
        plugin.getMenuManager().load();
    }

    public GeneralChallenge getChallengeByItem(ItemStack item) {

        for (GeneralChallenge currentChallenge : challenges) {

            if (currentChallenge == null) continue;
            if (currentChallenge.getItem() == null) continue;
            if (currentChallenge.getItem().equals(item)) return currentChallenge;

        }

        return null;

    }
}
