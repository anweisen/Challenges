package net.codingarea.challenges.plugin.utils.bukkit.nms;

import net.codingarea.challenges.plugin.Challenges;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public class PacketBorder {

    private Object worldBorder;

    private static final Class<?> worldBorderClass;
    private static final Class<?> settingsClass;

    private final World world;
    private double size = 0;
    private double centerX = 0;
    private double centerZ = 0;
    private int warningTime = 0;
    private int warningDistance = 0;

    static {
        worldBorderClass = NMSUtils.getClass("world.level.border.WorldBorder");
        settingsClass = NMSUtils.getClass("world.level.border.WorldBorder$c");
    }

    public PacketBorder(World world) {
        this.world = world;
        try {
            worldBorder = ReflectionUtil.invokeConstructor(worldBorderClass);
            Object defaultSettings = ReflectionUtil.getField(worldBorderClass, "d").get(worldBorder);
            ReflectionUtil.invokeMethod(worldBorder, "a", new Class[]{settingsClass}, new Object[]{defaultSettings});
            setWorld(world);
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
        }
    }

    private void setWorld(World world) {
        try {
            ReflectionUtil.setFieldValue(worldBorder, "world", getWorldServer(world));
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
        }
    }

    private Object getWorldServer(World world) {
        return new WorldServer(world).getWorldServer();
    }

    public void setSize(double size) {
        this.size = size;
        try {
            ReflectionUtil.invokeMethod(worldBorder, "setSize", new Class[] {double.class}, new Object[]{size});
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
        }
    }

    public void setCenter(double x, double z) {
        this.centerX = x;
        this.centerZ = z;
        try {
            ReflectionUtil.invokeMethod(worldBorder, "setCenter", new Class[] {double.class, double.class}, new Object[]{x, z});
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
        }
    }

    public void setWarningTime(int time) {
        this.warningTime = time;
        try {
            ReflectionUtil.invokeMethod(worldBorder, "setWarningTime", new Class[] {int.class}, new Object[]{time});
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
        }
    }

    public void setWarningDistance(int distance) {
        this.warningDistance = distance;
        try {
            ReflectionUtil.invokeMethod(worldBorder, "setWarningDistance", new Class[] {int.class}, new Object[]{distance});
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
        }
    }

    public void reset(Player player) {
        WorldBorder border = player.getWorld().getWorldBorder();
        setSize(border.getSize());
        setCenter(border.getCenter().getX(), border.getCenter().getZ());
        setWarningTime(border.getWarningTime());
        setWarningDistance(border.getWarningDistance());
        send(player, UpdateType.values());
    }

    public void send(Player player, UpdateType... updateTypes) {
        for (UpdateType updateType : updateTypes) {
            try {
                Object packet = ReflectionUtil.invokeConstructor(updateType.getPacketClass(), new Class[]{worldBorderClass}, new Object[]{worldBorder}); //Create packet
                CraftPlayer craftPlayer = new CraftPlayer(player);
                craftPlayer.getConnection().sendPacket(packet);
            } catch (Exception exception) {
                Challenges.getInstance().getLogger().error("", exception);
            }
        }
    }

    public double getSize() {
        return size;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterZ() {
        return centerZ;
    }

    public int getWarningTime() {
        return warningTime;
    }

    public int getWarningDistance() {
        return warningDistance;
    }

    public enum UpdateType {
        CENTER("ClientboundSetBorderCenterPacket"),
        SIZE("ClientboundSetBorderSizePacket"),
        WARNING_DELAY("ClientboundSetBorderWarningDelayPacket"),
        WARNING_DISTANCE("ClientboundSetBorderWarningDistancePacket");

        private final Class<?> packetClass;

        UpdateType(String packetName) {
            this.packetClass = NMSUtils.getClass("network.protocol.game." + packetName);
        }

        public Class<?> getPacketClass() {
            return packetClass;
        }
    }
}