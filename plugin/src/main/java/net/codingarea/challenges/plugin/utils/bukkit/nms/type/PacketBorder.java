package net.codingarea.challenges.plugin.utils.bukkit.nms.type;

import net.codingarea.challenges.plugin.utils.bukkit.nms.NMSProvider;
import net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_17.PacketBorder_1_17;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.util.function.Function;

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

    /**
     * Sets the size of the border with an animation
     * @param size The size of the border
     * @param animationTime The time the animation should take (in seconds)
     */
    public void setSize(double size, long animationTime) {
        animationTime *= 1000; // Convert to milliseconds
        transitionSizeBetween(this.size, size, animationTime);
        this.size = size;
    }

    protected abstract void setSizeField(double size);
    protected abstract void transitionSizeBetween(double oldSize, double newSize, long animationTime);

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

    public Object getWorldBorderObject() {
        return worldBorder;
    }

    public enum UpdateType {
        CENTER(NMSProvider.getBorderPacketFactory()::center),
        SIZE(NMSProvider.getBorderPacketFactory()::size),
        WARNING_DELAY(NMSProvider.getBorderPacketFactory()::warningDelay),
        WARNING_DISTANCE(NMSProvider.getBorderPacketFactory()::warningDistance);

        private final Function<PacketBorder, Object> packetFactory;

        UpdateType(Function<PacketBorder, Object> packetFactory) {
            this.packetFactory = packetFactory;
        }

        public Object createPacket(PacketBorder border) {
            return packetFactory.apply(border);
        }
    }
}
