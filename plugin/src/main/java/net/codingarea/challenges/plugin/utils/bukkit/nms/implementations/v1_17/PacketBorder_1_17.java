package net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_17;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.bukkit.nms.NMSProvider;
import net.codingarea.challenges.plugin.utils.bukkit.nms.NMSUtils;
import net.codingarea.challenges.plugin.utils.bukkit.nms.ReflectionUtil;
import net.codingarea.challenges.plugin.utils.bukkit.nms.type.CraftPlayer;
import net.codingarea.challenges.plugin.utils.bukkit.nms.type.PacketBorder;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public class PacketBorder_1_17 extends PacketBorder {


    public PacketBorder_1_17(World world) {
        super(
            NMSUtils.getClass("world.level.border.WorldBorder"),
            NMSUtils.getClass("world.level.border.WorldBorder$c"),
            world
        );
    }

    @Override
    protected Object createWorldBorder() {
        try {
            Object worldBorder = ReflectionUtil.invokeConstructor(nmsClass);
            Object defaultSettings = ReflectionUtil.getField(nmsClass, "d").get(worldBorder);
            ReflectionUtil.invokeMethod(worldBorder, "a", new Class[]{settingsClass}, new Object[]{defaultSettings});
            return worldBorder;
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
            return null;
        }
    }

    @Override
    protected void setWorld(World world) {
        try {
            ReflectionUtil.setFieldValue(worldBorder, "world", getWorldServer(world));
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
        }
    }

    @Override
    protected void setSizeField(double size) {
        try {
            ReflectionUtil.invokeMethod(worldBorder, "setSize", new Class[] {double.class}, new Object[]{size});
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
        }
    }

    @Override
    protected void transitionSizeBetween(double oldSize, double newSize, long animationTime) {
        try {
            ReflectionUtil.invokeMethod(worldBorder, "transitionSizeBetween", new Class[]{double.class, double.class, long.class}, new Object[]{oldSize, newSize, animationTime});
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
        }
    }

    @Override
    protected void setCenterField(double x, double z) {
        try {
            ReflectionUtil.invokeMethod(worldBorder, "setCenter", new Class[] {double.class, double.class}, new Object[]{x, z});
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
        }
    }

    @Override
    protected void setWarningTimeField(int warningTime) {
        try {
            ReflectionUtil.invokeMethod(worldBorder, "setWarningTime", new Class[] {int.class}, new Object[]{warningTime});
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
        }
    }

    @Override
    protected void setWarningDistanceField(int warningDistance) {
        try {
            ReflectionUtil.invokeMethod(worldBorder, "setWarningDistance", new Class[] {int.class}, new Object[]{warningDistance});
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
        }
    }

    @Override
    public void send(Player player, UpdateType updateType) {
        try {
            Object packet = ReflectionUtil.invokeConstructor(updateType.getPacketClass(), new Class[]{nmsClass}, new Object[]{worldBorder}); //Create packet
            CraftPlayer craftPlayer = NMSProvider.createCraftPlayer(player);
            craftPlayer.getConnection().sendPacket(packet);
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
        }
    }


}