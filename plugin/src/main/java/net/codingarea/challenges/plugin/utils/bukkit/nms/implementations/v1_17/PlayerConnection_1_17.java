package net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_17;

import net.codingarea.challenges.plugin.utils.bukkit.nms.ReflectionUtil;
import net.codingarea.challenges.plugin.utils.bukkit.nms.type.PlayerConnection;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public class PlayerConnection_1_17 extends PlayerConnection {
    public PlayerConnection_1_17(Object connection) throws ClassNotFoundException {
        super(ReflectionUtil.getMinecraftClass("network.protocol.Packet"), connection);
    }
}
