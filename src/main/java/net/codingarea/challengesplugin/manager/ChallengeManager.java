package net.codingarea.challengesplugin.manager;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.AbstractChallenge;
import net.codingarea.challengesplugin.challengetypes.Goal;
import net.codingarea.challengesplugin.manager.goal.GoalManager;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.commons.Log;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class ChallengeManager {

    private final Challenges plugin;
    private final List<AbstractChallenge> challenges;
    private final GoalManager goalManager;

    public ChallengeManager(Challenges plugin) {

        this.plugin = plugin;
        challenges = new ArrayList<>();

        // Creates settings folder if not exists
        File folder = new File(getSettingsFolder());
        if (!folder.exists()) folder.mkdirs();

        goalManager = new GoalManager();

    }

    public void loadConfigAndMenu() {
        loadChallengeConfigurations();
        generateInventories();
    }

    public void generateInventories() {
        plugin.getMenuManager().load();
    }

    public AbstractChallenge getChallengeByName(String name) {

        for (AbstractChallenge currentChallenge : challenges) {
            if (currentChallenge.getChallengeName().equals(name)) return currentChallenge;
        }

        return null;
    }

    public AbstractChallenge getChallengeByItem(ItemStack item) {

        for (AbstractChallenge currentChallenge : challenges) {
            if (currentChallenge == null) continue;
            if (currentChallenge.getItem().equals(item)) return currentChallenge;
        }

        return null;
    }

    private void loadChallengeConfigurations() {
        try {

            JSONObject jsonObject = Utils.getJSONObject(new File(getInternalFolder() + "settings.json"));
            for (AbstractChallenge currentChallenge : challenges) {
                try {
                    int value = (int) jsonObject.getOrDefault(currentChallenge.getChallengeName(), 0);
                    currentChallenge.setValues(value);
                } catch (Exception ignored) { }
            }

        } catch (ParseException | IOException ignored) { }
    }

    public void saveChallengeConfigurations() {
        try {

            File file = new File(getInternalFolder() + "settings.json");
            if (!file.exists()) file.createNewFile();
            JSONObject jsonObject = new JSONObject();

            for (AbstractChallenge currentChallenge : challenges) {
                jsonObject.put(currentChallenge.getChallengeName(), currentChallenge.toValue());
            }

            Utils.saveJSON(file, jsonObject);

        } catch (IOException ex) {
            Log.severe("Could not save challenge settings :: " + ex.getMessage());
        }
    }

    public String getSettingsFolder() {
        return getInternalFolder() + "challenges/";
    }

    public String getInternalFolder() {
        return plugin.getDataFolder() + "/internal/";
    }

    public Challenges getPlugin() {
        return plugin;
    }

    public GoalManager getGoalManager() {
        return goalManager;
    }

    public List<AbstractChallenge> getChallenges() {
        return challenges;
    }

    public void load(List<AbstractChallenge> queue) {

        for (AbstractChallenge currentChallenge : queue) {

            challenges.add(currentChallenge);

            if (currentChallenge instanceof Goal) {
                goalManager.addGoal((Goal) currentChallenge);
            }
            if (currentChallenge instanceof Listener) {
                Bukkit.getPluginManager().registerEvents((Listener) currentChallenge, plugin);
            }

        }

    }

}
