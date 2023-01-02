package net.codingarea.challenges.plugin.utils.bukkit.nms.implementations.v1_17;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.bukkit.nms.type.CraftPlayer;
import net.codingarea.challenges.plugin.utils.bukkit.nms.ReflectionUtil;
import org.bukkit.entity.Player;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public class CraftPlayer_1_17 extends CraftPlayer {

    /**
     * Create a new CraftPlayer
     *
     * @param player The Player
     */
    public CraftPlayer_1_17(Player player) throws ClassNotFoundException {
        super(ReflectionUtil.getBukkitClass("entity.CraftPlayer"), player);
    }

    @Override
    public Object get(Player object) {
        Object craftPlayer;
        try {
            craftPlayer = ReflectionUtil.invokeMethod(nmsClass, object, "getHandle");
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
            craftPlayer = null;
        }
        return craftPlayer;
    }

    @Override
    public Object getPlayerConnectionObject() {
        try {
            return ReflectionUtil.getObject(this.nmsObject, "b");
        } catch (Exception exception) {
            Challenges.getInstance().getLogger().error("", exception);
            return null;
        }
    }
}
