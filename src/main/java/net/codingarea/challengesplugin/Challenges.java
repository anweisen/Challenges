package net.codingarea.challengesplugin;

import com.github.challengesplugin.commands.*;
import com.github.challengesplugin.listener.*;
import com.github.challengesplugin.manager.*;
import net.codingarea.challengesplugin.commands.*;
import net.codingarea.challengesplugin.listener.*;
import net.codingarea.challengesplugin.manager.*;
import net.codingarea.challengesplugin.manager.menu.MenuManager;
import net.codingarea.challengesplugin.manager.permissions.MasterSystem;
import net.codingarea.challengesplugin.utils.StringManager;
import net.godingarea.challengesplugin.commands.*;
import net.godingarea.challengesplugin.listener.*;
import net.godingarea.challengesplugin.manager.*;
import net.codingarea.challengesplugin.manager.lang.LanguageManager;
import net.codingarea.challengesplugin.manager.lang.LanguageManager.Language;
import net.codingarea.challengesplugin.manager.players.ChallengePlayerManager;
import net.codingarea.challengesplugin.manager.scoreboard.ScoreboardManager;
import net.codingarea.challengesplugin.timer.ChallengeTimer;
import net.codingarea.challengesplugin.utils.YamlConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class Challenges extends JavaPlugin {

    @Getter private static Challenges instance;

    @Getter private StringManager stringManager;
    @Getter private ChallengeManager challengeManager;
    @Getter private ChallengeItemManager itemManager;
    @Getter private ChallengeTimer challengeTimer;
    @Getter private ScoreboardManager scoreboardManager;
    @Getter private MenuManager menuManager;
    @Getter private ConfigManager configManager;
    @Getter private WorldManager worldManager;
    @Getter private CloudNetManager cloudnetManager;
    @Getter private MasterSystem permissionsSystem;
    @Getter private ChallengePlayerManager playerManager;
    @Getter private ServerManager serverManager;

    @Override
    public void onLoad() {
        instance = this;

        loadCloudNet();
        loadConfig();
        loadWorlds();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        load();
        registerCommands();
        registerListener();

        challengeTimer.start();

        scoreboardManager.setCurrentBoard(scoreboardManager.getNewScoreboard("main"));
        scoreboardManager.getCurrentBoard().show();
        scoreboardManager.show();

    }

    @Override
    public void onDisable() {

        if (scoreboardManager != null) scoreboardManager.hide();
        if (challengeTimer != null) {
            challengeTimer.stop();
            challengeTimer = null;
        }
    }

    private void registerCommands() {
        getCommand("challenges").setExecutor(new ChallengesCommand());
        getCommand("timer").setExecutor(new TimerCommand(this));
        getCommand("start").setExecutor(getCommand("timer").getExecutor());
        getCommand("pause").setExecutor(getCommand("timer").getExecutor());
        getCommand("gamemode").setExecutor(new GamemodeCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("reset").setExecutor(new ResetCommand());
        getCommand("setmaster").setExecutor(new SetMasterCommand());
        getCommand("search").setExecutor(new SearchCommand());
        getCommand("position").setExecutor(new PositionCommand());
        getCommand("village").setExecutor(new VillageCommand());
        getCommand("language").setExecutor(new LanguageCommand());
    }

    private void registerListener() {
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new UtilsListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new TimerNotStartedListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
    }

    private void load() {
        LanguageManager.setLanguage(Language.getLanguage(getConfig().getString("language")));
        serverManager = new ServerManager(this);
        playerManager = new ChallengePlayerManager(this);
        stringManager = new StringManager();
        stringManager.load(getConfig());
        itemManager = new ChallengeItemManager(LanguageManager.getLanguage());
        permissionsSystem = new MasterSystem(this, getConfig().getString("master-changed-message"), getConfig().getString("master-set-message"), getConfig().getBoolean("master-system"));
        permissionsSystem.loadPermissions();
        permissionsSystem.onEnable();
        scoreboardManager = new ScoreboardManager(this);
        challengeTimer = new ChallengeTimer(this);
        challengeManager = new ChallengeManager(this);
        menuManager = new MenuManager(this);
        challengeManager.init();
        if (getConfig().getBoolean("disable-whitelist-on-start")) {
            Bukkit.setWhitelist(false);
        }
    }

    private void loadConfig() {
        configManager = new ConfigManager();
        configManager.setBackpackConfig(new YamlConfig("internal/backpack"));
        configManager.setInternalConfig(new YamlConfig("internal/session"));
        configManager.setPositionConfig(new YamlConfig("internal/positions"));
    }

    private void loadWorlds() {
        worldManager = new WorldManager(this);
        worldManager.loadSettings();
        worldManager.resetWorlds();
    }

    private void loadCloudNet() {
        cloudnetManager = new CloudNetManager(getConfig().getBoolean("cloudnet-support"));

        if (cloudnetManager.cloudnetSupport) {
            cloudnetManager.setLobby();
        }
    }

    public static boolean timerIsStarted() {
        return !getInstance().getChallengeTimer().isPaused();
    }

}
