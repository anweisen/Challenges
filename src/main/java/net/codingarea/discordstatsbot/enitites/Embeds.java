package net.codingarea.discordstatsbot.enitites;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

/**
 * @author anweisen
 * Challenges developed on 08-02-2020
 * https://github.com/anweisen
 */

public class Embeds {

	public static final Color COLOR = Color.decode("#2ECC71");

	public static EmbedBuilder builder() {
		return new EmbedBuilder().setColor(COLOR);
	}

}
