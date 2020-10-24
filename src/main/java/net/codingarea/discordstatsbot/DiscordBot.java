package net.codingarea.discordstatsbot;

import net.codingarea.challengesplugin.utils.ImageUtils;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.commons.Log;
import net.codingarea.discordstatsbot.commandmanager.CommandHandler;
import net.codingarea.discordstatsbot.commands.*;
import net.codingarea.discordstatsbot.listener.MessageListener;
import net.codingarea.engine.sql.constant.ConstSQL;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author anweisen
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 */

public final class DiscordBot {

	public static final String
			BOT_INVITE = "https://discord.com/api/oauth2/authorize?client_id=731786326437003275&permissions=116736&scope=bot",
			SERVER_INVITE = "https://discord.gg/JubAmHS";

	public static void main(String[] args) {
		new DiscordBot();
	}

	private final BufferedImage background = ImageUtils.getImage(new File("background.png"));

	private String token,
	               host,
				   user,
				   password,
	               database;

	private ShardManager shardManager;
	private CommandHandler commandHandler;

	public DiscordBot() {
		if (background != null) ImageUtils.darkerImage(background);
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
		try {
			ConstSQL.connect(host, database, user, password);
		} catch (SQLException ex) {
			Log.severe("Could not connect to MySQL server: " + ex.getMessage());
			System.exit(4);
		}
	}

	private void startBot() {

		commandHandler = new CommandHandler();
		commandHandler.registerCommands(
				new StatsCommand(), new TopCommand(), new InviteCommand(),
				new HelpCommand(), new CategoriesCommand()
		);

		try {

			shardManager = DefaultShardManagerBuilder.createDefault(token).setStatus(OnlineStatus.ONLINE).build();
			shardManager.addEventListener(new MessageListener(commandHandler));

		} catch (Exception ex) {
			Log.severe("Could not start bot :: " + ex.getMessage());
			System.exit(4);
		}

		startActivityTimer();

	}

	private void startActivityTimer() {

		new Timer().schedule(new TimerTask() {

			private int i = 0;

			@Override
			public void run() {

				String[] status = {
						"cs stats • Statistiken",
						"cs top • Bestenliste"
				};

				i++;
				if (i >= status.length) i = 0;
				shardManager.setActivity(Activity.playing(status[i]));

			}
		}, 1000, 15*1000);

	}

	public BufferedImage getBackground() {
		return background;
	}

}
