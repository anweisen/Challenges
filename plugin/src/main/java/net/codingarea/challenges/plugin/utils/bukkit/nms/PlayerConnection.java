package net.codingarea.challenges.plugin.utils.bukkit.nms;

import net.codingarea.challenges.plugin.Challenges;

/**
 * @author TobiasDeBruijn | https://github.com/TobiasDeBruijn
 * @source https://github.com/TobiasDeBruijn/BukkitReflectionUtil
 * @since 2.2.0
 */
public class PlayerConnection {

	private static Class<?> packetClass;

	static {
		try {
            if (ReflectionUtil.getMajorVersion() >= 16) {
                packetClass = ReflectionUtil.getNmsClass("Packet");
            } else {
                packetClass = ReflectionUtil.getMinecraftClass("network.protocol.Packet");
            }
		} catch (Exception exception) {
			Challenges.getInstance().getLogger().error("", exception);
		}
	}

	private final Object connection;

	protected PlayerConnection(Object connection) {
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
                ReflectionUtil.invokeMethod(this.connection, "sendPacket", new Class<?>[]{packetClass}, new Object[]{packet});
            } else {
                ReflectionUtil.invokeMethod(this.connection, "a", new Class<?>[]{packetClass}, new Object[]{packet});
            }
		} catch (Exception exception) {
			Challenges.getInstance().getLogger().error("", exception);
		}
	}
}