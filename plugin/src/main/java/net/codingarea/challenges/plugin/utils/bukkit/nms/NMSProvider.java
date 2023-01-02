package net.codingarea.challenges.plugin.utils.bukkit.nms;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_17.CraftPlayer_1_17;
import net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_17.PacketBorder_1_17;
import net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_17.PlayerConnection_1_17;
import net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_17.WorldServer_1_17;
import net.codingarea.challenges.plugin.utils.bukkit.nms.type.CraftPlayer;
import net.codingarea.challenges.plugin.utils.bukkit.nms.type.PacketBorder;
import net.codingarea.challenges.plugin.utils.bukkit.nms.type.PlayerConnection;
import net.codingarea.challenges.plugin.utils.bukkit.nms.type.WorldServer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public class NMSProvider {
    private final static int majorVersion = ReflectionUtil.getMajorVersion();


    /**
     * Creates a world server for the given world
     * @param world The world
     * @return The world server, or null if the server version is not supported
     */
    @Nullable
    public static WorldServer createWorldServer(World world) {
        if(majorVersion >= 17) {
            try {
                return new WorldServer_1_17(world);
            } catch (ClassNotFoundException exception) {
                Challenges.getInstance().getLogger().error("", exception);
            }
        }
        return null; //ToDo
    }

    /**
     * Creates a craft player for the given player
     * @param player The player
     * @return The craft player, or null if the server version is not supported
     */
    @Nullable
    public static CraftPlayer createCraftPlayer(Player player) {
        if(majorVersion >= 17) {
            try {
                return new CraftPlayer_1_17(player);
            } catch (ClassNotFoundException exception) {
                Challenges.getInstance().getLogger().error("", exception);
            }
        }
        return null;
    }

    /**
     * Creates a player connection for the given CraftPlayer
     * @param player The craftplayer
     * @return The player connection, or null if the server version is not supported
     */
    @Nullable
    public static PlayerConnection createPlayerConnection(CraftPlayer player) {
        if(majorVersion >= 17) {
            try {
                return new PlayerConnection_1_17(player.getPlayerConnectionObject());
            } catch (ClassNotFoundException exception) {
                Challenges.getInstance().getLogger().error("", exception);
            }
        }
        return null;
    }

    /** Creates a packet border for the given world
     * @param world The world
     * @return The packet border, or null if the server version is not supported
     */
    @Nullable
    public static PacketBorder createPacketBorder(World world) {
        if(majorVersion >= 17) {
            return new PacketBorder_1_17(world);
        }
        return null;
    }
}
