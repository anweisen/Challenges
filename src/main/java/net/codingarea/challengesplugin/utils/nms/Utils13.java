package net.codingarea.challengesplugin.utils.nms;

import net.codingarea.challengesplugin.utils.Utils;
import net.minecraft.server.v1_13_R2.ChatMessageType;
import net.minecraft.server.v1_13_R2.EnumItemSlot;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityEquipment;
import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-24-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class Utils13 {

    public static void setFakeArmor(Player viewer, int entityID, Color color) {
        ItemStack armor = Utils.getChestplate(color);
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(entityID, EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(armor));
        ((CraftPlayer) viewer).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendActionbar(Player player, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), ChatMessageType.GAME_INFO);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
