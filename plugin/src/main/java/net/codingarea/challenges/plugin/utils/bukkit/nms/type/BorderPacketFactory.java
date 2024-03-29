package net.codingarea.challenges.plugin.utils.bukkit.nms.type;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public abstract class BorderPacketFactory {

    public abstract Object center(PacketBorder packetBorder);
    public abstract Object size(PacketBorder packetBorder);
    public abstract Object warningDelay(PacketBorder packetBorder);
    public abstract Object warningDistance(PacketBorder packetBorder);

}
