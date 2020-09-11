package net.codingarea.challengesplugin.utils;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class StringManager {

    public String MENU_TITLE = "§8» §9Menu";
    public String MENU_TITLE_END = " §8┃ §9%page";
    public String CHALLENGES_TITLE = MENU_TITLE + " §8• §9Challenges";
    public String SETTINGS_TITLE = MENU_TITLE + " §8• §9Settings";
    public String DIFFICULTY_TITLE = MENU_TITLE +  " §8• §9Difficulty";
    public String BLOCKS_TITLE = MENU_TITLE +  " §8• §9Items and Blocks";
    public String GOALS_TITLE = MENU_TITLE +  " §8• §9Game-Goal";
    public String TIMER_TITLE = MENU_TITLE +  " §8• §9Timer";
    public String DAMAGE_TITLE = MENU_TITLE +  " §8• §9Damage";

    public String ACTIONBAR_TIMER_STOP = "§8▶ §7Time: §cstopped §8◀";
    public String ACTIONBAR_TIMER_TIME_UP = "§8▶ §7Time: §b%time% §8◀";
    public String ACTIONBAR_TIMER_TIME_DOWN = "§8▶ §7Time: §b%time% §8◀";
    public String ACTIONBAR_TIMER_TIME_UP_INFO = "§8▶ §7Time: §c%time% §8┃ §7%info% §8◀";
    public String ACTIONBAR_TIMER_TIME_DOWN_INFO = "§8▶ §7Time: §c%time% §8┃ §7%info% §8◀";

    public void load(FileConfiguration config) {

        ACTIONBAR_TIMER_STOP = config.getString("timer-stopped");
        ACTIONBAR_TIMER_TIME_UP = config.getString("timer-up");
        ACTIONBAR_TIMER_TIME_DOWN = config.getString("timer-down");

        ACTIONBAR_TIMER_TIME_DOWN_INFO = config.getString("timer-down-info");
        ACTIONBAR_TIMER_TIME_UP_INFO = config.getString("timer-up-info");

    }

}
