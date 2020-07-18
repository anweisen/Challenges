package net.codingarea.challengesplugin.utils;

import net.codingarea.challengesplugin.utils.commons.Log;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class StringManager {

    public String PREFIX_TEMPLATE =  "§8§l┃ %name% §8┃ §7";
    public String CHALLENGE_PREFIX = PREFIX_TEMPLATE.replace("%name%", "§6Challenges");
    public String TIMER_PREFIX = PREFIX_TEMPLATE.replace("%name%", "§5Timer");
    public String MASTER_PREFIX = PREFIX_TEMPLATE.replace("%name%", "§eMaster");
    public String POSITION_PREFIX = PREFIX_TEMPLATE.replace("%name%", "§9Position");
    public String BACKPACK_PREFIX = PREFIX_TEMPLATE.replace("%name%", "§aBackpack");
    public String DAMAGE_PREFIX = PREFIX_TEMPLATE.replace("%name%", "§cDamage");
    public String BINGO_PREFIX = PREFIX_TEMPLATE.replace("%name%", "§dBingo");

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

        PREFIX_TEMPLATE = config.getString("prefix");
        if (PREFIX_TEMPLATE == null) Log.severe("Could not load prefix :: Prefix cannot be null");

        CHALLENGE_PREFIX = PREFIX_TEMPLATE.replace("%name%", "§6Challenges");
        TIMER_PREFIX = PREFIX_TEMPLATE.replace("%name%", "§5Timer");
        MASTER_PREFIX = PREFIX_TEMPLATE.replace("%name%", "§eMaster");
        POSITION_PREFIX = PREFIX_TEMPLATE.replace("%name%", "§9Position");
        BACKPACK_PREFIX = PREFIX_TEMPLATE.replace("%name%", "§aBackpack");
        DAMAGE_PREFIX = PREFIX_TEMPLATE.replace("%name%", "§cDamage");
        BINGO_PREFIX = PREFIX_TEMPLATE.replace("%name%", "§dBingo");

    }

}
