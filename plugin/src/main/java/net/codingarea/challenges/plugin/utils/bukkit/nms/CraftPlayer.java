package net.codingarea.challenges.plugin.utils.bukkit.nms;

import org.bukkit.entity.Player;

/**
 * @author TobiasDeBruijn | https://github.com/TobiasDeBruijn
 * @source https://github.com/TobiasDeBruijn/BukkitReflectionUtil
 * @since 2.2.0
 */
public class CraftPlayer {

	protected static Class<?> CLASS;

	static {
		try {
			CLASS = ReflectionUtil.getBukkitClass("entity.CraftPlayer");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected final Object craftPlayer;

	/**
	 * Create a new CraftPlayer
	 *
	 * @param player The Player
	 */
	public CraftPlayer(Player player) {
		Object craftPlayer;
		try {
			craftPlayer = ReflectionUtil.invokeMethod(CLASS, player, "getHandle");
		} catch (Exception e) {
			e.printStackTrace();
			craftPlayer = null;
		}
		this.craftPlayer = craftPlayer;
	}

	/**
	 * Get a connection to the Player
	 *
	 * @return The PlayerConnection
	 */
	public PlayerConnection getConnection() {
		try {
            if (ReflectionUtil.getMajorVersion() >= 16) {
                return new PlayerConnection(ReflectionUtil.getObject(this.craftPlayer, "playerConnection"));
            }
            return new PlayerConnection(ReflectionUtil.getObject(this.craftPlayer, "b"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}