package net.codingarea.challengesplugin.utils;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.v1_16_R1.ChatMessageType;
import net.minecraft.server.v1_16_R1.EnumItemSlot;
import net.minecraft.server.v1_16_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R1.PacketPlayOutChat;
import net.minecraft.server.v1_16_R1.PacketPlayOutEntityEquipment;
import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-24-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class Utils16 {

    public static void setFakeArmor(Player viewer, int entityID, Color color) {

        List<Pair<EnumItemSlot, net.minecraft.server.v1_16_R1.ItemStack>> list = new ArrayList<>();

        for (ItemStack item : Utils.getLeatherArmor(color)) {
        list.add(new Pair<>(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(item)));
        }

        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(entityID, list);
        ((CraftPlayer) viewer).getHandle().playerConnection.sendPacket(packet);

    }

    public static void sendActionbar(Player player, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), ChatMessageType.GAME_INFO, player.getUniqueId());
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

    }

}
