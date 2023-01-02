package net.codingarea.challenges.plugin.utils.bukkit.nms;

import net.codingarea.challenges.plugin.Challenges;
import org.bukkit.World;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public class WorldServer {

	protected static Class<?> CLASS;

	static {
		try {
			CLASS = ReflectionUtil.getBukkitClass("CraftWorld");
		} catch (Exception exception) {
			Challenges.getInstance().getLogger().error("", exception);
		}
	}

	protected final Object worldServer;

	/**
	 * Create a new WorldServer
	 *
	 * @param world The world
	 */
	public WorldServer(World world) {
		Object worldServer;
		try {
			worldServer = ReflectionUtil.invokeMethod(CLASS, world, "getHandle");
		} catch (Exception exception) {
			Challenges.getInstance().getLogger().error("", exception);
			worldServer = null;
		}
		this.worldServer = worldServer;
	}

	/**
	 * Get the world server for the world
	 *
	 * @return The WorldServer
	 */
	public Object getWorldServer() {
		return worldServer;
	}
}