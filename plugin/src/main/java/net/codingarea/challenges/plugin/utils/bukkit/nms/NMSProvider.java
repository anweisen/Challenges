package net.codingarea.challenges.plugin.utils.bukkit.nms;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_13.*;
import net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_17.BorderPacketFactory_1_17;
import net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_17.CraftPlayer_1_17;
import net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_17.PacketBorder_1_17;
import net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_18.PacketBorder_1_18;
import net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_18.PlayerConnection_1_18;
import net.codingarea.challenges.plugin.utils.bukkit.nms.type.*;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public class NMSProvider {
    private final static int majorVersion;
    private static BorderPacketFactory borderPacketFactory;

    static {
        majorVersion = ReflectionUtil.getMajorVersion();
        if (versionIsAtLeast(17)) {
            borderPacketFactory = new BorderPacketFactory_1_17();
        } else if (versionIsAtLeast(13)) {
            borderPacketFactory = new BorderPacketFactory_1_13();
        }
    }


    /**
     * Creates a world server for the given world
     * @param world The world
     * @return The world server, or null if the server version is not supported
     */
    @Nullable
    public static WorldServer createWorldServer(World world) {
        try {
            if (versionIsAtLeast(13)) {
                return new WorldServer_1_13(world);
            }
        } catch (ClassNotFoundException exception) {
            Challenges.getInstance().getLogger().error("Failed to create WorldServer instance for version {}:", majorVersion, exception);
        }
        return null;
    }

    /**
     * Creates a craft player for the given player
     * @param player The player
     * @return The craft player, or null if the server version is not supported
     */
    @Nullable
    public static CraftPlayer createCraftPlayer(Player player) {
        try {
            if (versionIsAtLeast(17)) {
                return new CraftPlayer_1_17(player);
            } else if (versionIsAtLeast(13)) {
                return new CraftPlayer_1_13(player);
            }
        } catch (ClassNotFoundException exception) {
            Challenges.getInstance().getLogger().error("Failed to create CraftPlayer instance for version {}:", majorVersion, exception);
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
        try {
            if (versionIsAtLeast(18)) {
                return new PlayerConnection_1_18(player.getPlayerConnectionObject());
            } else if (versionIsAtLeast(13)) {
                return new PlayerConnection_1_13(player.getPlayerConnectionObject());
            }
        } catch (ClassNotFoundException exception) {
            Challenges.getInstance().getLogger().error("Failed to create PlayerConnection instance for version {}:", majorVersion, exception);
        }

        return null;
    }

    /** Creates a packet border for the given world
     * @param world The world
     * @return The packet border, or null if the server version is not supported
     */
    @Nullable
    public static PacketBorder createPacketBorder(World world) {
        if (versionIsAtLeast(18)) {
            return new PacketBorder_1_18(world);
        } else if (versionIsAtLeast(17)) {
            return new PacketBorder_1_17(world);
        } else if (versionIsAtLeast(13)) {
            return new PacketBorder_1_13(world);
        }
        return null;
    }

    /**
     * @return A border packet factory, or null if the server version is not supported
     */
    @Nullable
    public static BorderPacketFactory getBorderPacketFactory() {
        return borderPacketFactory;
    }

    private static boolean versionIsAtLeast(int majorVersion) {
        return NMSProvider.majorVersion >= majorVersion;
    }
}
