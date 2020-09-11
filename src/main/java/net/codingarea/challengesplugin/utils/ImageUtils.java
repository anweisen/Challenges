package net.codingarea.challengesplugin.utils;

//import net.codingarea.challengesplugin.utils.commons.ChatColor;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author anweisen
 * Challenges developed on 06-30-2020
 * https://github.com/anweisen
 */

public class ImageUtils {

	public static final char IMAGE_CHAR = '█';
	public static final int FLAG_HEIGHT = 30, FLAG_WIDTH = 30;
	public static final String SKULL_URL = "https://crafatar.com/avatars/%uuid%?size=128&default=MHF_Steve&overlay";

	public static ChatColor[][] toChatColorArray(BufferedImage image) {

		BufferedImage resizedImage = resizeImage(image);
		ChatColor[][] chatImage = new ChatColor[resizedImage.getWidth()][resizedImage.getHeight()];

		for (int x = 0; x < resizedImage.getWidth(); x++) {
			for (int y = 0; y < resizedImage.getHeight(); y++) {
				int rgb = resizedImage.getRGB(x, y);
				//ChatColor closest = ChatColor.of(new Color(rgb));
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

	private static BufferedImage resizeImage(BufferedImage originalImage) {
		BufferedImage resizedImage = new BufferedImage(FLAG_WIDTH, FLAG_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = resizedImage.createGraphics();
		graphics.drawImage(originalImage, 0, 0, FLAG_WIDTH, FLAG_HEIGHT, null);
		graphics.dispose();
		graphics.setComposite(AlphaComposite.Src);
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		return resizedImage;
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

	public static BufferedImage getImage(File file) {
		try {
			URLConnection connection = file.toURI().toURL().openConnection();
			InputStream input = connection.getInputStream();
			return ImageIO.read(input);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static BufferedImage getLocalImage(String path) {
		try {
			InputStream input = ClassLoader.getSystemResourceAsStream(path);
			return ImageIO.read(input);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static BufferedImage getHeadByName(String playerName) {
		return getHeadByUUID(Utils.getUUID(playerName));
	}

	public static BufferedImage getHeadByUUID(String uuid) {
		String url = getHeadURLByUUID(uuid);
		return getImage(url);
	}

	public static String getHeadURLByUUID(String uuid) {
		if (uuid == null || uuid.equals("error")) uuid = "c06f89064c8a49119c29ea1dbd1aab82"; // uuid of MHF_Steve
		return SKULL_URL.replace("%uuid%", uuid);
	}

	public static String[] getImageLines(String url) {
		BufferedImage image = ImageUtils.getImage(url);
		ChatColor[][] colors = ImageUtils.toChatColorArray(image);
		return toFlagImageMessage(toImageMessage(colors, IMAGE_CHAR));
	}

	/**
	 * @returns the width of the text which got added
	 */
	public static int addCenteredText(Graphics2D graphics, String text, int yPosition, int imageWidth) {

		TextLayout layout = new TextLayout(text, graphics.getFont(), graphics.getFontRenderContext());
		int lineWidth = (int) layout.getBounds().getWidth();

		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.drawString(text, imageWidth / 2 - lineWidth / 2, yPosition);

		return lineWidth;

	}

	public static void addTextEndingAtMid(Graphics2D graphics, String text, int yPosition, int imageWidth) {

		TextLayout layout = new TextLayout(text, graphics.getFont(), graphics.getFontRenderContext());
		int lineWidth = (int) layout.getBounds().getWidth();

		graphics.drawString(text, imageWidth / 2 - lineWidth, yPosition);

	}

	public static void addAttribute(Graphics2D graphics, String label, String value, char splitter, int height, int imageWidth, Color textColor, Color valueColor) {
		graphics.setColor(textColor);
		ImageUtils.addTextEndingAtMid(graphics, label + " " + splitter, height, imageWidth);
		graphics.setColor(valueColor);
		graphics.drawString(" " + value, imageWidth / 2, height);
	}

	public static String getTime(int seconds) {

		int minutes = seconds / 60;
		int hours = minutes / 60;
		seconds %= 60;
		minutes %= 60;

		boolean showHours = hours > 0;
		boolean showMinutes = !showHours && minutes > 0;
		boolean showSeconds = !showMinutes;

		return (showHours ? (hours > 1  ? hours + " Stunden " : hours + " Stunde ") : "")
				+ (showMinutes ? (minutes > 1 ? minutes + " Minuten" : minutes + " Minute ") : "")
				+ (showSeconds ? (seconds > 1  || seconds == 0 ? seconds + " Sekunden" : seconds + " Sekunde") : "");

	}

	public static void darkerImage(@NotNull BufferedImage image) {
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				image.setRGB(i, j, new Color(image.getRGB(i, j)).darker().getRGB());
			}
		}
	}

}
