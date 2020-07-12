package net.codingarea.discordstatsbot;

import net.codingarea.challengesplugin.utils.Log;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.sql.MySQL;
import net.codingarea.discordstatsbot.commandmanager.CommandHandler;
import net.codingarea.discordstatsbot.commands.StatsCommand;
import net.codingarea.discordstatsbot.listener.MessageListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManager;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author anweisen
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 */

public class DiscordBot {

	public static void main(String[] args) {
		new DiscordBot();
	}

	private String token,
	               host,
				   user,
				   password,
	               database;

	private ShardManager shardManager;
	private CommandHandler commandHandler;

	public DiscordBot() {
		load();
		connectToMySQL();
		startBot();
	}

	private void load() {
		try {

			File config = new File("config.properties");
			if (!config.exists()) config.createNewFile();

			Properties properties = Utils.readProperties(config);

			token = properties.getProperty("TOKEN");
			host = properties.getProperty("MYSQL-HOST");
			user = properties.getProperty("MYSQL-USER");
			password = properties.getProperty("MYSQL-PASSWORD");
			database = properties.getProperty("MYSQL-DATABASE");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void connectToMySQL() {
		MySQL.connect(host, database, user, password);
		MySQL.createDatabases();
	}

	private void startBot() {

		commandHandler = new CommandHandler();
		commandHandler.registerCommands(
				new StatsCommand()
		);

		try {

			shardManager = DefaultShardManagerBuilder.createDefault(token).build();
			shardManager.setActivity(Activity.playing("starting.."));
			shardManager.setStatus(OnlineStatus.ONLINE);

			shardManager.addEventListener(new MessageListener(commandHandler));

		} catch (Exception ex) {
			Log.severe("Could not start bot :: " + ex.getMessage());
		}

		new Timer().schedule(new TimerTask() {

			private int i = 0;

			@Override
			public void run() {

				String[] status = {
					"cs stats • " + shardManager.getGuilds().size() + " Server",
					"cs stats • " + shardManager.getUsers().size() + " User",
					"cs stats • Challenges"
				};

				i++;
				if (i >= status.length) i = 0;
				shardManager.setActivity(Activity.playing(status[i]));

			}
		}, 5*1000, 30*1000);

	}

}
