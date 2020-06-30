package net.codingarea.challengesplugin.utils;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-05-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class RandomizerUtil {

    public static List<Material> getRandomizerBlocks() {
        List<Material> list = new ArrayList<>(Arrays.asList(Material.values()));
        list.remove(Material.COMMAND_BLOCK);
        list.remove(Material.CHAIN_COMMAND_BLOCK);
        list.remove(Material.BARRIER);
        list.remove(Material.REPEATING_COMMAND_BLOCK);
        list.remove(Material.STRUCTURE_BLOCK);
        list.remove(Material.STRUCTURE_VOID);
        list.remove(Material.NETHER_PORTAL);
        list.remove(Material.END_PORTAL);
        list.remove(Material.LAVA);
        list.remove(Material.WATER);

        try {
            list.remove(Material.JIGSAW);
        } catch (NoSuchFieldError ignored) {

        }
        list.removeIf(material -> isAir(material) || material.isLegacy() || material.isItem());

        return list;

    }

    public static List<EntityType> getRandomizerEntities() {
        List<EntityType> list = new ArrayList<>();

        for (EntityType entity : EntityType.values()) {
            if (entity.isAlive()) list.add(entity);
        }
        list.remove(EntityType.ENDER_DRAGON);
        list.remove(EntityType.PLAYER);
        list.remove(EntityType.GIANT);
        list.remove(EntityType.ZOMBIE_HORSE);

        return list;
    }

    public static List<Material> getRandomizerDrops() {
        List<Material> list = new ArrayList<>(Arrays.asList(Material.values()));
        list.remove(Material.COMMAND_BLOCK);
        list.remove(Material.BARRIER);
        list.remove(Material.CHAIN_COMMAND_BLOCK);
        list.remove(Material.REPEATING_COMMAND_BLOCK);
        list.remove(Material.STRUCTURE_BLOCK);
        list.remove(Material.STRUCTURE_VOID);
        list.remove(Material.NETHER_PORTAL);
        list.remove(Material.END_PORTAL);
        list.remove(Material.AIR);
        list.remove(Material.LAVA);
        list.remove(Material.WATER);
        list.remove(Material.DEBUG_STICK);
        list.remove(Material.KNOWLEDGE_BOOK);
        try {
            list.remove(Material.JIGSAW);
        } catch (NoSuchFieldError ignored) {

        }
        list.removeIf(material -> isAir(material) || material.isLegacy() || !material.isItem());
        return list;
    }

    private static boolean isAir(Material material) {

        switch(material.ordinal()) {
            case 18: case 145: case 922: case 976:
                return true;
            default:
                return false;

        }


    }

}