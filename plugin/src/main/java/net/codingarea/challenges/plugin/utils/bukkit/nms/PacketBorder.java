package net.codingarea.challenges.plugin.utils.bukkit.nms;

import org.bukkit.entity.Player;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public class PacketBorder {

    private Object worldBorder;

    private static final Class<?> worldBorderClass;
    private static final Class<?> settingsClass;

    static {
        worldBorderClass = NMSUtils.getClass("world.level.border.WorldBorder");
        settingsClass = NMSUtils.getClass("world.level.border.WorldBorder$c");
    }

    public PacketBorder() {
        try {
            worldBorder = ReflectionUtil.invokeConstructor(worldBorderClass);
            Object defaultSettings = ReflectionUtil.getField(worldBorderClass, "d").get(worldBorder);
            ReflectionUtil.invokeMethod(worldBorder, "a", new Class[]{settingsClass}, new Object[]{defaultSettings});
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void setSize(double size) {
        try {
            ReflectionUtil.invokeMethod(worldBorder, "a", new Class[] {double.class}, new Object[]{size});
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void setCenter(double x, double z) {
        try {
            ReflectionUtil.invokeMethod(worldBorder, "c", new Class[] {double.class, double.class}, new Object[]{x, z});
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void setWarningTime(int time) {
        try {
            ReflectionUtil.invokeMethod(worldBorder, "b", new Class[] {int.class}, new Object[]{time});
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void setWarningDistance(int distance) {
        try {
            ReflectionUtil.invokeMethod(worldBorder, "c", new Class[] {int.class}, new Object[]{distance});
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void send(Player player, UpdateType... updateTypes) {
        for (UpdateType updateType : updateTypes) {
            try {
                Object packet = ReflectionUtil.invokeConstructor(updateType.getPacketClass(), new Class[]{worldBorderClass}, new Object[]{worldBorder}); //Create packet
                CraftPlayer craftPlayer = new CraftPlayer(player);
                craftPlayer.getConnection().sendPacket(packet);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public enum UpdateType {
        CENTER("ClientboundSetBorderCenterPacket"),
        SIZE("ClientboundSetBorderSizePacket"),
        WARNING_DELAY("ClientboundSetBorderWarningDelayPacket"),
        WARNING_DISTANCE("ClientboundSetBorderWarningDistancePacket");

        private final Class<?> packetClass;

        UpdateType(String packetName) {
            this.packetClass = NMSUtils.getClass("network.protocol.game. " + packetName);
        }

        public Class<?> getPacketClass() {
            return packetClass;
        }
    }
}