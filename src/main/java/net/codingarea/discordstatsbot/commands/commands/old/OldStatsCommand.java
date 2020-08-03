package net.codingarea.discordstatsbot.commands.old;

import net.codingarea.challengesplugin.manager.players.stats.PlayerStats;
import net.codingarea.challengesplugin.manager.players.stats.StatsAttribute;
import net.codingarea.challengesplugin.manager.players.stats.StatsWrapper;
import net.codingarea.challengesplugin.utils.ImageUtils;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.commons.Log;
import net.codingarea.discordstatsbot.DiscordBot;
import net.codingarea.discordstatsbot.commandmanager.events.CommandEvent;
import net.codingarea.discordstatsbot.commandmanager.commands.Command;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.SQLException;

import static net.codingarea.challengesplugin.utils.ImageUtils.addAttribute;

/**
 * @author anweisen
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 */

public class OldStatsCommand extends Command {

	private final BufferedImage background;

	public OldStatsCommand(DiscordBot bot) {
		super("stats", true, "s");
		background = bot.getBackground();
	}

	@Override
	public void onCommand(CommandEvent event) {

		String player = event.isFromGuild() ? event.getMember().getEffectiveName() : event.getUser().getName();

		if (event.getArgs().length > 1 || !nameIsValid(player)) {
			event.queueReply("Benutze " + CommandEvent.syntax(event, "<player>"));
			return;
		}

		if (event.getArgs().length == 1) {
			player = event.getArg(0);
		}

		if (!nameIsValid(player)) {
			event.queueReply("`" + player + "` ist kein gültiger Spielername");
			return;
		}

		try {

			event.getChannel().sendTyping().queue();

			File folder = new File("./images");
			if (!folder.exists()) folder.mkdirs();
			File file = new File(folder + "/stats-" + event.getUser().getId() + ".png");
			if (!file.exists()) file.createNewFile();

			try {

				BufferedImage statsImage = getImage(player);
				ImageIO.write(statsImage, "png", file);
				event.getChannel().sendFile(file, "stats.png").queue(message -> file.delete(), exception -> {});

			} catch (SQLException ex) {
				Log.severe("Could not send stats :: " + ex.getMessage());
				event.queueReply("Etwas ist mit der Datenbank schief gelaufen");
			}

		} catch (Exception ex) {
			Log.severe("Could not send stats :: " + ex.getMessage());
			event.queueReply("Etwas ist mit dem Server schief gelaufen");
		}

	}

	private boolean nameIsValid(String name) {
		return name != null && name.length() > 1 && name.length() < 17;
	}

	private BufferedImage getImage(String playerName) throws SQLException {

		String uuid = Utils.getUUID(playerName);
		PlayerStats playerStats = StatsWrapper.getStatsByUUIDWithException(uuid);

		if (playerStats.getSavedName() != null && playerStats.getSavedName().equalsIgnoreCase(playerName)) {
			playerName = playerStats.getSavedName();
		}

		int width = 2048;
		int height = 1824;

		Color backgroundColor = Color.decode("#2C2F33");
		Color frameColor = Color.decode("#ffffff");
		Color playerNameColor = Color.decode("#D0D3D5");
		Color textColor = Color.decode("#99999A");
		Color attributeColor = Color.decode("#c4c2c2");

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bufferedImage.createGraphics();

		// Drawing background color and image if available
		graphics.setColor(backgroundColor);
		graphics.fillRect(0, 0, width, height);

		if (background != null) graphics.drawImage(background, 0, 0, width, height, null);

		// Drawing a circle wich will be around the players head
		graphics.setColor(frameColor);
		graphics.fillOval(748, 80, 552, 552);

		// Pasting the players head as circle
		Image image = ImageUtils.getHeadByUUID(uuid);
		graphics.setClip(new Ellipse2D.Float(768, 100, 512, 512));
		graphics.drawImage(image, 768, 100, 512, 512, null);
		graphics.setClip(0, 0, width, height);

		// Adding the player's name centered under the image
		graphics.setFont(new Font("Arial", Font.BOLD, 100));
		graphics.setColor(playerNameColor);
		int playerNameWidth = ImageUtils.addCenteredText(graphics, playerName, 775, width);

		// Adding a line under the player's name
		graphics.setColor(frameColor);
		int x1 = width / 2 - playerNameWidth / 2;
		graphics.fillRect(x1 - 20, 815, playerNameWidth + 40, 10);

		// Adding stats text
		int attributeTextSize = 80;
		int attributePaddingTop = 23;
		graphics.setFont(new Font("Arial", Font.BOLD, attributeTextSize));

		String challengesPlayed = playerStats.asString(StatsAttribute.PLAYED, true);
		String challengesWon = playerStats.asString(StatsAttribute.WON, true);
		String damageDealt = playerStats.asString(StatsAttribute.DAMAGE_DEALT, true);
		String damageTaken = playerStats.asString(StatsAttribute.DAMAGE_TAKEN, true);
		String blocksBroken = playerStats.asString(StatsAttribute.BLOCKS_BROKEN, true);
		String entityKills = playerStats.asString(StatsAttribute.ENTITIES_KILLED, true);
		String timeSneaked = playerStats.asString(StatsAttribute.TIME_SNEAKED, true);
		String itemsCollected = playerStats.asString(StatsAttribute.ITEMS_COLLECTED, true);
		String jumps = playerStats.asString(StatsAttribute.JUMPS, true);

		char splitter = '»';
		int firstAttribute = 930;

		addAttribute(graphics, "Challenges gespielt", challengesPlayed, splitter, firstAttribute + (attributePaddingTop + attributeTextSize) * 0, width, textColor, attributeColor);
		addAttribute(graphics, "Challenges gewonnen", challengesWon, splitter, firstAttribute + (attributePaddingTop + attributeTextSize) * 1, width, textColor, attributeColor);
		addAttribute(graphics, "Schaden ausgeteilt", damageDealt, splitter, firstAttribute + (attributePaddingTop + attributeTextSize) * 2, width, textColor, attributeColor);
		addAttribute(graphics, "Schaden genommen", damageTaken, splitter, firstAttribute + (attributePaddingTop + attributeTextSize) * 3, width, textColor, attributeColor);
		addAttribute(graphics, "Blöcke abgebaut", blocksBroken, splitter, firstAttribute + (attributePaddingTop + attributeTextSize) * 4, width, textColor, attributeColor);
		addAttribute(graphics, "Mobs getötet", entityKills, splitter, firstAttribute + (attributePaddingTop + attributeTextSize) * 5, width, textColor, attributeColor);
		addAttribute(graphics, "Items aufgesammelt", itemsCollected, splitter, firstAttribute + (attributePaddingTop + attributeTextSize) * 6, width, textColor, attributeColor);
		addAttribute(graphics, "Sprünge", jumps, splitter, firstAttribute + (attributePaddingTop + attributeTextSize) * 7, width, textColor, attributeColor);
		addAttribute(graphics, "Zeit gesneaked", timeSneaked, splitter, firstAttribute + (attributePaddingTop + attributeTextSize) * 8, width, textColor, attributeColor);

		graphics.dispose();

		// Downscaling the image by 3 so that it can be send faster
		// Could have made the image smaller from the beginning but I don't care I'm too lazy
		BufferedImage scaled = new BufferedImage(width / 3, height / 3, BufferedImage.TYPE_INT_RGB);
		Graphics2D scaledGraphics = scaled.createGraphics();

		scaledGraphics.drawImage(bufferedImage, 0, 0, width / 3, height / 3, null);
		scaledGraphics.dispose();

		return scaled;
	}

}
