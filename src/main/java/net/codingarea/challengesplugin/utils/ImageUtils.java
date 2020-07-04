package net.codingarea.challengesplugin.utils;

import net.md_5.bungee.api.ChatColor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author anweisen
 * Challenges developed on 06-30-2020
 * https://github.com/anweisen
 */

public class ImageUtils {

	public static final char IMAGE_CHAR = '█';
	public static final int HEIGHT = 30, WIDTH = 30;

	public static ChatColor[][] toChatColorArray(BufferedImage image) {

		BufferedImage resizedImage = resizeImage(image);
		ChatColor[][] chatImage = new ChatColor[resizedImage.getWidth()][resizedImage.getHeight()];

		for (int x = 0; x < resizedImage.getWidth(); x++) {
			for (int y = 0; y < resizedImage.getHeight(); y++) {
				int rgb = resizedImage.getRGB(x, y);
				ChatColor closest = getClosestChatColor(new Color(rgb));
				chatImage[x][y] = closest;
			}
		}
		return chatImage;
	}

	public static String[] toImageMessage(ChatColor[][] colors, char imageChar) {

		String[] lines = new String[(colors[0]).length];
		for (int y = 0; y < (colors[0]).length; y++) {
			StringBuilder currentLine = new StringBuilder();
			for (int x = 0; x < colors.length; x++) {
				currentLine.append(colors[x][y].toString())
					       .append(imageChar);
			}
			lines[y] = currentLine.toString() + ChatColor.RESET;
		}
		return lines;
	}

	public static String toFlagImageMessageLine(String messageLine) {
		String line = messageLine.substring(3);
		return line.substring(0, line.length() - 5);
	}

	public static String[] toFlagImageMessage(String[] message) {
		String[] flagMessage = new String[message.length - 12];
		for (int i = 0; i < flagMessage.length; i++) {
			flagMessage[i] = message[i + 6];
		}
		for (int i = 0; i < flagMessage.length; i++) {
			flagMessage[i] = toFlagImageMessageLine(flagMessage[i]);
		}
		return flagMessage;
	}

	private static BufferedImage resizeImage(BufferedImage originalImage) {
		BufferedImage resizedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = resizedImage.createGraphics();
		graphics.drawImage(originalImage, 0, 0, WIDTH, HEIGHT, null);
		graphics.dispose();
		graphics.setComposite(AlphaComposite.Src);
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		return resizedImage;
	}

	private static final Color[] colors = new Color[] {
			new Color(0, 0, 0), new Color(0, 0, 168), new Color(0, 168, 0), new Color(0, 168, 168), new Color(168, 0, 0), new Color(168, 0, 168), new Color(251, 168, 0), new Color(168, 168, 168), new Color(84, 84, 84), new Color(84, 84, 251),
			new Color(84, 251, 84), new Color(84, 251, 251), new Color(251, 84, 84), new Color(251, 84, 251), new Color(251, 251, 84), new Color(255, 255, 255) };

	private static ChatColor getClosestChatColor(Color color) {
		if (color.getAlpha() < 128) return null;
		int index = 0;
		double best = -1.0D;
		int i;
		for (i = 0; i < colors.length; i++) {
			if (areIdentical(colors[i], color)) return ChatColor.values()[i];
		}
		for (i = 0; i < colors.length; i++) {
			double distance = getDistance(color, colors[i]);
			if (distance < best || best == -1.0D) {
				best = distance;
				index = i;
			}
		}
		return ChatColor.values()[index];
	}

	// I have no idea how this works, i just copy pasted this method lol
	private static double getDistance(Color color1, Color color2) {
		double rmean = (color1.getRed() + color2.getRed()) / 2.0D;
		double r = (color1.getRed() - color2.getRed());
		double g = (color1.getGreen() - color2.getGreen());
		int b = color1.getBlue() - color2.getBlue();
		double weightR = 2.0D + rmean / 256.0D;
		double weightG = 4.0D;
		double weightB = 2.0D + (255.0D - rmean) / 256.0D;
		return weightR * r * r + weightG * g * g + weightB * b * b;
	}

	private static boolean areIdentical(Color c1, Color c2) {
		return (Math.abs(c1.getRed() - c2.getRed()) <= 5 && Math.abs(c1.getGreen() - c2.getGreen()) <= 5 && Math.abs(c1.getBlue() - c2.getBlue()) <= 5);
	}

	public static BufferedImage getImage(String request) {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(request).openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
			return ImageIO.read(connection.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String[] getImageLines(String url) {
		BufferedImage image = ImageUtils.getImage(url);
		ChatColor[][] colors = ImageUtils.toChatColorArray(image);
		return toFlagImageMessage(toImageMessage(colors, IMAGE_CHAR));
	}

}
