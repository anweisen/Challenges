package net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_17;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.bukkit.nms.NMSUtils;
import net.codingarea.challenges.plugin.utils.bukkit.nms.ReflectionUtil;
import net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v_1_13.PacketBorder_1_13;
import org.bukkit.World;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public class PacketBorder_1_17 extends PacketBorder_1_13 {


    public PacketBorder_1_17(World world) {
        super(
            world,
            NMSUtils.getClass("world.level.border.WorldBorder"),
            NMSUtils.getClass("world.level.border.WorldBorder$c")
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


}