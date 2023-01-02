package net.codingarea.challenges.plugin.utils.bukkit.nms.type;

import net.codingarea.challenges.plugin.utils.bukkit.nms.NMSProvider;
import net.codingarea.challenges.plugin.utils.bukkit.nms.NMSUtils;
import net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_17.PacketBorder_1_17;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

/**
 * Provides a WorldBorder that can be sent to the client via packets
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public abstract class PacketBorder extends AbstractNMSClass {
    protected Object worldBorder;

    protected final World world;
    protected double size = 0;
    protected double centerX = 0;
    protected double centerZ = 0;
    protected int warningTime = 0;
    protected int warningDistance = 0;

    protected final Class<?> settingsClass;

    /**
     * @param nmsClass      The NMS class
     * @param world The world the border is in
     * @param settingsClass The WorldBorder Settings class
     */
    public PacketBorder(Class<?> nmsClass, Class<?> settingsClass, World world) {
        super(nmsClass);
        this.settingsClass = settingsClass;
        this.world = world;

        worldBorder = createWorldBorder();
        setWorld(world);
    }

    protected abstract Object createWorldBorder();

    /**
     * Sets the world the border is in
     * @param world The world
     */
    protected abstract void setWorld(World world);

    /**
     * Sets the size of the border
     * @param size The size of the border
     */
    public void setSize(double size) {
        this.size = size;
        setSizeField(size);
    }

    protected abstract void setSizeField(double size);

    /**
     * Sets the center of the border
     * @param x The x coordinate of the center
     * @param z The z coordinate of the center
     */
    public void setCenter(double x, double z) {
        this.centerX = x;
        this.centerZ = z;
        setCenterField(x, z);
    }
    protected abstract void setCenterField(double x, double z);

    /**
     * Sets the warning time of the border
     * @param warningTime The warning time
     */
    public void setWarningTime(int warningTime) {
        this.warningTime = warningTime;
        setWarningTimeField(warningTime);
    }
    protected abstract void setWarningTimeField(int warningTime);

    /**
     * Sets the warning distance of the border
     * @param warningDistance The warning distance
     */
    public void setWarningDistance(int warningDistance) {
        this.warningDistance = warningDistance;
        setWarningDistanceField(warningDistance);
    }
    protected abstract void setWarningDistanceField(int warningDistance);

    /**
     * @param world The world
     * @return The world server of the world
     */
    protected Object getWorldServer(World world) {
        return NMSProvider.createWorldServer(world).getWorldServerObject();
    }

    public void reset(Player player) {
        WorldBorder border = player.getWorld().getWorldBorder();
        setSize(border.getSize());
        setCenter(border.getCenter().getX(), border.getCenter().getZ());
        setWarningTime(border.getWarningTime());
        setWarningDistance(border.getWarningDistance());
        send(player, PacketBorder_1_17.UpdateType.values());
    }

    public void send(Player player) {
        send(player, UpdateType.values());
    }
    public void send(Player player, UpdateType... updateTypes) {
        for (UpdateType updateType : updateTypes) {
            send(player, updateType);
        }
    }
    public abstract void send(Player player, PacketBorder_1_17.UpdateType updateType);

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
