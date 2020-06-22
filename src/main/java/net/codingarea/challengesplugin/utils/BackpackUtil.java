package net.codingarea.challengesplugin.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class BackpackUtil {

    public static void saveArrayToConfig(FileConfiguration config, String path, ItemStack[] array) {

        if (path == null) return;
        if (array == null) return;

        for (int i = 0; i < array.length; i++) {

            config.set(path + "." + i, array[i]);

        }

    }

    public static List<ItemStack> getListFromConfig(FileConfiguration config, String path, int size) {

        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) return null;

        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {

            try {
                list.add(config.getItemStack(path + "." + i));
            } catch (NullPointerException ignored) { }

        }

        return list;

    }

    public static ItemStack[] listToArray(List<ItemStack> list) {

        if (list == null) return null;

        int size = list.size();
        ItemStack[] array = new ItemStack[size];

        for (int i = 0; i < array.length; i++) {

            array[i] = list.get(i);

        }

        return array;

    }

    public static ItemStack[] getContent(FileConfiguration config, String path, int size) {

        return listToArray(getListFromConfig(config, path, size));

    }

}
