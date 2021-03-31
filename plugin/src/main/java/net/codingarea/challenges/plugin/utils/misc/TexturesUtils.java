package net.codingarea.challenges.plugin.utils.misc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.anweisen.utilities.bukkit.utils.GameProfileUtils;
import net.anweisen.utilities.commons.config.document.EmptyDocument;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class TexturesUtils {

	private TexturesUtils() {}

	public static void saveTextures(@Nonnull Player player) {
		try {

			if (!Challenges.getInstance().getDatabaseManager().isConnected()) return;

			GameProfile profile = GameProfileUtils.getGameProfile(player);
			PropertyMap properties = profile.getProperties();
			List<Property> textures = new ArrayList<>(properties.get("textures"));
			if (textures.isEmpty()) return;
			Property texture = textures.get(0);

			Challenges.getInstance().getDatabaseManager().getDatabase()
					.insertOrUpdate("challenges")
					.set("textures", texture.getValue())
					.where("uuid", player.getUniqueId())
					.execute();

		} catch (Exception ex) {
			Logger.severe("Unable to update textures for " + player.getName() + " | " + player.getUniqueId(), ex);
		}
	}

	@Nullable
	public static String getTextures(@Nonnull UUID uuid) {
		try {
			return Challenges.getInstance().getDatabaseManager().getDatabase()
					.query("challenges")
					.select("textures")
					.where("uuid", uuid)
					.execute().firstOrEmpty()
					.getString("textures");
		} catch (Exception ex) {
			Logger.severe("Unable to get textures for " + uuid, ex);
			return null;
		}
	}

}
