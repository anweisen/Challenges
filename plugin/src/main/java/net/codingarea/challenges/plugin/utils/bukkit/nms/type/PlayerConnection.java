package net.codingarea.challenges.plugin.utils.bukkit.nms.type;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.bukkit.nms.ReflectionUtil;

/**
 * @author TobiasDeBruijn | https://github.com/TobiasDeBruijn
 * @source https://github.com/TobiasDeBruijn/BukkitReflectionUtil
 * @since 2.2.0
 */
public abstract class PlayerConnection extends AbstractNMSClass {

	private final Object connection;

	public PlayerConnection(Class<?> nmsClass, Object connection) {
		super(nmsClass);
		this.connection = connection;
	}

	/**
	 * Send a packet
	 * <p>
	 * The caller must guarantee the passed in object is a Packet
	 *
	 * @param packet The packet to send
	 */
	public void sendPacket(Object packet) {
		try {
		    if (ReflectionUtil.getMajorVersion() >= 16) {
                ReflectionUtil.invokeMethod(this.connection, "sendPacket", new Class<?>[]{nmsClass}, new Object[]{packet});
            } else {
                ReflectionUtil.invokeMethod(this.connection, "a", new Class<?>[]{nmsClass}, new Object[]{packet});
            }
		} catch (Exception exception) {
			Challenges.getInstance().getLogger().error("", exception);
		}
	}
}