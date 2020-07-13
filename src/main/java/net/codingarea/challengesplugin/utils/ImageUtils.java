package net.codingarea.challengesplugin.utils;

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
				ChatColor closest = ChatColor.of(new Color(rgb));
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
		String line = messageLine.substring(4);
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
		if (uuid.equals("error")) uuid = "c06f89064c8a49119c29ea1dbd1aab82"; // uuid of MHF_Steve
		return getImage(SKULL_URL.replace("%uuid%", uuid));
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

}
