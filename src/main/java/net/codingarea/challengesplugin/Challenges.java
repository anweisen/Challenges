package net.codingarea.challengesplugin;

import net.codingarea.challengesplugin.commands.*;
import net.codingarea.challengesplugin.listener.*;
import net.codingarea.challengesplugin.manager.*;
<<<<<<< Updated upstream
=======
import net.codingarea.challengesplugin.manager.checker.APIConnector;
import net.codingarea.challengesplugin.manager.checker.BlacklistChecker;
import net.codingarea.challengesplugin.manager.checker.UpdateChecker;
>>>>>>> Stashed changes
import net.codingarea.challengesplugin.manager.lang.LanguageManager;
import net.codingarea.challengesplugin.manager.lang.LanguageManager.Language;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.loader.PluginChallengeLoader;
import net.codingarea.challengesplugin.manager.loader.ChallengeLoader;
import net.codingarea.challengesplugin.manager.menu.MenuManager;
import net.codingarea.challengesplugin.manager.players.ChallengePlayerManager;
import net.codingarea.challengesplugin.manager.players.PlayerSettingsManager;
import net.codingarea.challengesplugin.manager.players.stats.StatsManager;
import net.codingarea.challengesplugin.manager.scoreboard.ScoreboardManager;
import net.codingarea.challengesplugin.timer.ChallengeTimer;
import net.codingarea.challengesplugin.utils.StringManager;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.YamlConfig;
import net.codingarea.challengesplugin.utils.commons.Log;
import net.codingarea.challengesplugin.utils.sql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public final class Challenges extends JavaPlugin {

    private static Challenges instance;
    private static final int version = Utils.getServerVersion();

    private boolean isNewestVersion = true;

    private StringManager stringManager;
    private ChallengeManager challengeManager;
    private ChallengeTimer challengeTimer;
    private ScoreboardManager scoreboardManager;
    private MenuManager menuManager;
    private ConfigManager configManager;
    private WorldManager worldManager;
    private CloudNetManager cloudnetManager;
    private ChallengePlayerManager playerManager;
    private ServerManager serverManager;
    private PlayerSettingsManager playerSettingsManager;
    private StatsManager statsManager;

    @Override
    public void onLoad() {

        instance = this;
        Log.setLogger(getLogger());

<<<<<<< Updated upstream
        loadConfigVersion();
=======
        if (!Utils.isSpigot()) return;

        BlacklistChecker.updateStatus();
        UpdateChecker.updateStatus();
        APIConnector.startup();
        if (BlacklistChecker.isBlocked()) return;


>>>>>>> Stashed changes
        loadCloudNet();
        loadConfigs();
        loadWorlds();
        loadSQL();
        load();

    }

    @Override
    public void onEnable() {

        saveDefaultConfig();
        reloadConfig();

        enable();
        registerCommands();
        registerListener();

        challengeTimer.loadTimerDataFromSessionConfig();
        challengeTimer.start();
        scoreboardManager.show();

    }

    @Override
    public void onDisable() {
        if (scoreboardManager != null) {
            scoreboardManager.hide();
            scoreboardManager.destroyAllScoreboards();
        }
        if (challengeTimer != null) {
            challengeTimer.saveTimerDataToSessionConfig();
            challengeTimer.stop();
        }
        if (challengeManager != null) {
            challengeManager.saveChallengeConfigurations();
        }
        try {
            MySQL.disconnectWithException();
        } catch (Exception ignored) { }
    }

    private void registerCommands() {
        getCommand("challenges").setExecutor(new ChallengesCommand());
        getCommand("timer").setExecutor(new TimerCommand(this));
        getCommand("start").setExecutor(getCommand("timer").getExecutor());
        getCommand("pause").setExecutor(getCommand("timer").getExecutor());
        getCommand("gamemode").setExecutor(new GamemodeCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("reset").setExecutor(new ResetCommand());
        getCommand("search").setExecutor(new SearchCommand());
        getCommand("position").setExecutor(new PositionCommand());
        getCommand("village").setExecutor(new VillageCommand());
        getCommand("language").setExecutor(new LanguageCommand());
        getCommand("config").setExecutor(new ConfigCommand(this));
        getCommand("stats").setExecutor(new StatsCommand(this));
    }

    private void registerListener() {
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new UtilsListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new TimerNotStartedListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new StatsListener(this), this);
        Bukkit.getPluginManager().registerEvents((Listener) getCommand("stats").getExecutor(), this);
    }

    private void load() {
        LanguageManager.setLanguage(Language.getLanguage(getConfig().getString("language")));
        LanguageManager.loadLanguageMessages();
        Prefix.load();
        serverManager = new ServerManager(this);
        challengeManager = new ChallengeManager(this);
        playerManager = new ChallengePlayerManager(this);
    }

    private void enable() {
        loadPositions();
        worldManager.loadWorld();
        new PluginChallengeLoader(this).loadChallenges();
        ChallengeLoader.load(challengeManager);
        playerSettingsManager = new PlayerSettingsManager(this);
        stringManager = new StringManager();
        stringManager.load(getConfig());
        scoreboardManager = new ScoreboardManager();
        challengeTimer = new ChallengeTimer(this);
        menuManager = new MenuManager(this);
        statsManager = new StatsManager(this);
        statsManager.load();
        challengeManager.loadConfigAndMenu();
        if (getConfig().getBoolean("disable-whitelist-on-start")) Bukkit.setWhitelist(false);
    }

    private void loadConfigs() {
        configManager = new ConfigManager();
        configManager.setInternalConfig(new YamlConfig(getDataFolder() + "/internal/session"));
        CloudNetManager.loadIngameFromConfig();
    }

    private void loadPositions() {
        configManager.setPositionConfig(new YamlConfig(getDataFolder() + "/internal/positions"));
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

    private void loadSQL() {

        if (!getConfig().getBoolean("mysql.enabled")) return;

        String host = getConfig().getString("mysql.host");
        String database = getConfig().getString("mysql.database");
        String user = getConfig().getString("mysql.user");
        String password = getConfig().getString("mysql.password");

        if (host == null || database == null || user == null || password == null)
            return;

<<<<<<< Updated upstream
        MySQL.connect(host, database, user, password);
        MySQL.createDatabases();
=======
        try {
            ConstSQL.connect(host, database, user, password);
            createDatabases();
        } catch (Exception ex) {
            Log.severe("Could not connect to database: " + ex.getMessage());
        }
>>>>>>> Stashed changes

    }

    private void loadConfigVersion() {
        String version = getConfig().getString("config-version");
        if (version == null || !version.equals(getDescription().getVersion())) {
            getLogger().warning("This plugin had an update. Please delete your config and restart the server to see whats new in the config");
            isNewestVersion = false;
        }
    }

    public static boolean timerIsStarted() {
        return !instance.getChallengeTimer().isPaused();
    }

    public static Challenges getInstance() {
        return instance;
    }

    public ChallengeManager getChallengeManager() {
        return challengeManager;
    }

    public static int getVersion() {
        return version;
    }

    public ChallengePlayerManager getPlayerManager() {
        return playerManager;
    }

    public StringManager getStringManager() {
        return stringManager;
    }

    public ChallengeTimer getChallengeTimer() {
        return challengeTimer;
    }

    public CloudNetManager getCloudnetManager() {
        return cloudnetManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public PlayerSettingsManager getPlayerSettingsManager() {
        return playerSettingsManager;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public boolean isNewestVersion() {
        return isNewestVersion;
    }

}
