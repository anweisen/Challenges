package net.codingarea.challengesplugin.utils;

import net.codingarea.challengesplugin.utils.commons.ChatColor;
import net.codingarea.challengesplugin.utils.commons.IOUtils;
import net.codingarea.challengesplugin.utils.commons.Log;
import net.codingarea.challengesplugin.utils.nms.Utils13;
import net.codingarea.challengesplugin.utils.nms.Utils14;
import net.codingarea.challengesplugin.utils.nms.Utils15;
import net.codingarea.challengesplugin.utils.nms.Utils16;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class Utils {

    private static final int version = getServerVersion();

    public static Material getRedDye() {
        return Material.valueOf(version == 13 ? "ROSE_RED" : "RED_DYE");
    }

    public static Material getGreenDye() {
        return Material.valueOf(version == 13 ? "CACTUS_GREEN" : "GREEN_DYE");
    }

    public static void setFakeArmor(Player viewer, int entityID, Color color) {
        if (version == 16) Utils16.setFakeArmor(viewer, entityID, color);
        if (version == 15) Utils15.setFakeArmor(viewer, entityID, color);
        if (version == 14) Utils15.setFakeArmor(viewer, entityID, color);
        if (version == 13) Utils13.setFakeArmor(viewer, entityID, color);
    }

    public static void sendActionbar(Player player, String message) {
        if (version == 16) Utils16.sendActionbar(player, message);
        if (version == 15) Utils15.sendActionbar(player, message);
        if (version == 14) Utils14.sendActionbar(player, message);
        if (version == 13) Utils13.sendActionbar(player, message);
    }

    public static int getRandomSecondsDistance(int seconds) {
        return (((seconds / 60)) * 10) + 20;
    }

    public static int getRandomSecondsUp(int seconds) {
        return seconds + getRandomSecondsDistance(seconds);
    }

    public static int getRandomSecondsDown(int seconds) {
        return seconds - getRandomSecondsDistance(seconds);
    }

    public static Material[] getArmors() {
        return new Material[] {
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
            Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
            Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS};
    }

    public static Material[] getFlowers() {
        return new Material[] {
            Material.RED_MUSHROOM, Material.BROWN_MUSHROOM, Material.SUNFLOWER, Material.LILY_OF_THE_VALLEY, Material.LILY_PAD,
            Material.DANDELION, Material.WITHER_ROSE, Material.CORNFLOWER, Material.OXEYE_DAISY, Material.ORANGE_TULIP, Material.PINK_TULIP,
            Material.WHITE_TULIP, Material.RED_TULIP, Material.ROSE_BUSH, Material.AZURE_BLUET, Material.ALLIUM, Material.BLUE_ORCHID,
            Material.POPPY, Material.DEAD_BUSH, Material.FLOWER_POT, Material.TORCH, Material.REDSTONE_TORCH, Material.WALL_TORCH,
            Material.REDSTONE_WALL_TORCH
        };
    }

    public static boolean isHalfBlock(Material material) {
        return material == Material.SOUL_SAND
            || material == Material.GRASS_PATH
            || material == Material.STONECUTTER
            || material == Material.FARMLAND
            || material == Material.ENCHANTING_TABLE
            || material == Material.CHEST
            || material == Material.TRAPPED_CHEST
            || material == Material.ENDER_CHEST
            || material == Material.CAMPFIRE
            || material == Material.LANTERN
            || material == Material.BREWING_STAND
            || material == Material.CACTUS
            || material == Material.SNOW;
    }

    public static List<Material> getRandomizerBlocks() {
        List<Material> list = new ArrayList<>(Arrays.asList(Material.values()));
        list.remove(Material.COMMAND_BLOCK);
        list.remove(Material.CHAIN_COMMAND_BLOCK);
        list.remove(Material.REPEATING_COMMAND_BLOCK);
        list.remove(Material.STRUCTURE_BLOCK);
        list.remove(Material.STRUCTURE_VOID);
        list.remove(Material.NETHER_PORTAL);
        list.remove(Material.END_PORTAL);
        list.removeIf(material -> !material.isBlock());
        return list;
    }

    public static List<Material> getRandomizerDrops() {
        List<Material> list = new ArrayList<>(Arrays.asList(Material.values()));
        list.remove(Material.COMMAND_BLOCK);
        list.remove(Material.CHAIN_COMMAND_BLOCK);
        list.remove(Material.REPEATING_COMMAND_BLOCK);
        list.remove(Material.STRUCTURE_BLOCK);
        list.remove(Material.STRUCTURE_VOID);
        list.remove(Material.NETHER_PORTAL);
        list.remove(Material.END_PORTAL);
        return list;
    }

    public static String getRomanNumbers(int number) {
        switch (number) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
            default:
                return "?";
        }
    }

    public static String getEnumName(Enum<?> enun) {
        return getEnumName(enun.name());
    }

    public static String getEnumName(String enumName) {

        if (enumName == null) return "";

        StringBuilder builder = new StringBuilder();
        String[] chars = enumName.split("");
        chars[0] = chars[0].toUpperCase();
        boolean nextUp = true;
        for (String currentChar : chars) {
            if (currentChar.equals("_")) {
                nextUp = true;
                builder.append(" ");
                continue;
            }
            if (nextUp) {
                builder.append(currentChar.toUpperCase());
                nextUp = false;
            } else {
                builder.append(currentChar.toLowerCase());
            }
        }

        return builder.toString()
                .replace(" And ", " and ")
                .replace(" The ", " the ")
                .replace(" Or ", " or ")
                .replace(" Of " , " of")
                .replace(" In ", " in ")
                .replace(" On " , " on ")
                .replace(" Off ", " off ");
    }

    public static String getPlayerListAsString(List<Player> list) {

        if (list == null || list.isEmpty()) return "";

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {

            Player currentPlayer = list.get(i);
            builder.append("§7").append(currentPlayer.getDisplayName()).append("§7");

            if (i < list.size() - 1) {
                builder.append(", ");
            }

        }

        return builder.toString();

    }

    public static void fillInventory(ItemStack item, Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, item);
        }
    }

    public static String getStringWithoutColorCodes(String string) {

        if (string == null) return "";

        String[] colorCodes = ChatColor.ALL_CODES.split("");

        for (String currentString : colorCodes) {
            string = string.replace("§" + currentString, "");
        }

        return string;

    }

    public static <Type> int getLocationInList(Type toFind, List<Type> list) {

        if (list == null || toFind == null) return -1;
        if (!list.contains(toFind)) return -1;

        for (int i = 0; i < list.size(); i++)  {
            Type currentEntry = list.get(i);
            if (currentEntry.equals(toFind)) return i;
        }

        return -1;

    }

    public static String NO_PERMS_MESSAGE = "§cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe this is in error.";

    public static boolean playerIsInArea(Player player, Location loc1, Location loc2) {
        return locationIsInArea(player.getLocation(), loc1, loc2);
    }

    public static boolean locationIsInArea(Location location, Location loc1, Location loc2) {

        double[] dim = new double[2];

        dim[0] = loc1.getX();
        dim[1] = loc2.getX();
        Arrays.sort(dim);
        if(location.getX() > dim[1] || location.getX() < dim[0]) return false;

        dim[0] = loc1.getY();
        dim[1] = loc2.getY();
        Arrays.sort(dim);
        if(location.getY() > dim[1] || location.getY() < dim[0]) return false;

        dim[0] = loc1.getZ();
        dim[1] = loc2.getZ();
        Arrays.sort(dim);
        if (location.getZ() > dim[1] || location.getZ() < dim[0]) return false;

        return true;

    }

    public static int getServerVersion() {
        try {
            Material.NETHERITE_INGOT.name();
            return 16;
        } catch (NoSuchFieldError ignored) {
            try {
                Material.BEE_SPAWN_EGG.name();
                return 15;
            } catch (NoSuchFieldError ignored1) {
                try {
                    Material.BAMBOO.name();
                    return 14;
                } catch (NoSuchFieldError ignored2) {
                    return 13;
                }
            }
        }
    }

    public static void spawnFireworks(Location location, int amount) {
        try {

            Location loc = location.clone();
            Firework firework = loc.getWorld().spawn(loc, Firework.class);
            FireworkMeta fireworkMeta = firework.getFireworkMeta();

            fireworkMeta.setPower(1);
            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.LIME, Color.AQUA, Color.RED, Color.YELLOW, Color.PURPLE, Color.BLUE).flicker(true).withTrail().build());

            firework.setFireworkMeta(fireworkMeta);
            firework.detonate();

            Random random = new Random();
            for(int i = 0; i < amount; i++) {
                double x = 0.1 + (0.9 - 0.1) * random.nextDouble();
                double z = 0.1 + (0.9 - 0.1) * random.nextDouble();
                Firework fireworkSpawned = (Firework) loc.getWorld().spawnEntity(loc.subtract(0.5, 0, 0.5).add(x, 0, z), EntityType.FIREWORK);
                fireworkSpawned.setFireworkMeta(fireworkMeta);
            }

        } catch (IllegalArgumentException ignored) { };
    }

    public static void boostAway(Player player) {
        Vector vector = player.getLocation().getDirection();
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.5F, 1F);
        player.setVelocity(vector.multiply(-1F).setY(0.5F));
    }

    public static void boostForwards(Player player) {
        Vector vector = player.getLocation().getDirection();
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.5F, 1F);
        player.setVelocity(vector.multiply(3.4F).setY(1.3F));
    }

    public static ItemStack getHelmet(Color color) {
        ItemStack armor = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
        meta.setColor(color);
        armor.setItemMeta(meta);
        return armor;
    }

    public static ItemStack getChestplate(Color color) {
        ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
        meta.setColor(color);
        armor.setItemMeta(meta);
        return armor;
    }

    public static ItemStack getLeggings(Color color) {
        ItemStack armor = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
        meta.setColor(color);
        armor.setItemMeta(meta);
        return armor;
    }

    public static ItemStack getBoots(Color color) {
        ItemStack armor = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
        meta.setColor(color);
        armor.setItemMeta(meta);
        return armor;
    }

    public static ItemStack[] getLeatherArmor(Color color) {
        return new ItemStack[] {
                getHelmet(color), getChestplate(color), getLeggings(color), getBoots(color)
        };
    }

    @SuppressWarnings("deprecation")
    public static ItemStack getHead(String skullOwner) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName(skullOwner);
        meta.setOwner(skullOwner);
        item.setItemMeta(meta);
        return item;
    }

    public static void spawnParticleCircle(Location location, Effect particle, double points, double radius) {
        for (byte i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            Location point = location.clone().add(radius * Math.sin(angle), 0.0d, radius * Math.cos(angle));
            location.getWorld().playEffect(point, particle, 1);
        }
    }

    public static void spawnParticleCircle(Location location, Particle particle, double points, double radius) {
        for (byte i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            Location point = location.clone().add(radius * Math.sin(angle), 0.0d, radius * Math.cos(angle));
            location.getWorld().spawnParticle(particle, point, 1);
        }
    }

    public static void spawnAroundAndUpGoingParticleCircle(Location location, Effect particle, double startOfY, double endOfY, JavaPlugin plugin) {
        byte points = 25;
        double radius = 1.0D;
        int delay = 0;
        double y = startOfY;

        for (byte i = 0; i < points; i++) {

            final double Y = y;
            final int I = i;

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                double angle = 2 * Math.PI * I / points;
                Location point = location.clone().add(radius * Math.sin(angle), 0.0d, radius * Math.cos(angle)).add(0D, Y, 0D);
                location.getWorld().playEffect(point, particle, 1);
            }, delay);
            delay++;
            y += endOfY / (double) points;
        }
    }

    public static void spawnAroundAndUpGoingParticleCircle(Location location, Particle particle, double startOfY, double endOfY, JavaPlugin plugin) {
        byte points = 25;
        double radius = 1.0D;
        int delay = 0;
        double y = startOfY;

        for (byte i = 0; i < points; i++) {

            final double Y = y;
            final int I = i;

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                double angle = 2 * Math.PI * I / points;
                Location point = location.clone().add(radius * Math.sin(angle), 0.0d, radius * Math.cos(angle)).add(0D, Y, 0D);
                location.getWorld().spawnParticle(particle, point, 1);
            }, delay);
            delay++;
            y += endOfY / (double) points;
        }
    }

    public static void spawnAroundAndDownGoingParticleCircle(Location location, Effect particle, double startOfY, double endOfY, JavaPlugin plugin) {
        byte points = 25;
        double radius = 1.0D;
        int delay = 0;
        double y = startOfY;

        for (byte i = 0; i < points; i++) {

            final double Y = y;
            final int I = i;

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                double angle = 2 * Math.PI * I / points;
                Location point = location.clone().add(radius * Math.sin(angle), 0.0d, radius * Math.cos(angle)).add(0D, Y, 0D);
                point.getWorld().playEffect(point, particle, 1);
            }, delay);
            delay++;
            y -= startOfY / (double) points;
        }
    }

    public static void spawnAroundDownAndUpGoingParticleCircle(Location location, Effect particle, JavaPlugin plugin) {
        spawnAroundAndDownGoingParticleCircle(location, particle, 2.0D, 0.0D, plugin);
        spawnAroundAndUpGoingParticleCircle(location, particle, 0.0D, 2.0D, plugin);
    }

    public static void spawnUpGoingParticleCircle(Location location, Effect particle, JavaPlugin plugin, double height, double points, double radius) {
        int delay = 0;

        for (double d = 0.0D; d < height; d += 0.25) {
            delay++;

            final double D = d;

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                spawnParticleCircle(location.clone().add(0, D, 0), particle, points, radius);
            }, delay);
        }
    }

    public static void spawnUpGoingParticleCircle(Location location, Particle particle, JavaPlugin plugin, double height, double points, double radius) {
        int delay = 0;

        for (double d = 0.0D; d < height; d += 0.25) {
            delay++;

            final double D = d;

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                spawnParticleCircle(location.clone().add(0, D, 0), particle, points, radius);
            }, delay);
        }
    }

    public static List<Integer> getRandomDifferentNumbers(int max, int numbers) {
        if (numbers > max) return null;

        List<Integer> numbersGenerated = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numbers; i++) {
            int number = random.nextInt(max);

            while (numbersGenerated.contains(number)) {
                number = random.nextInt(max);
            }

            numbersGenerated.add(number);
        }

        return numbersGenerated;

    }

    public static ItemStack getItemStackWithTempDesc(ItemStack original, String desc, boolean enchant, boolean clearLine) {
        if (original == null) return null;
        if (desc == null || desc.equals("null") || desc.equals("")) return null;

        ItemStack item = original.clone();

        if (enchant) {
            item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        }

        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
        String[] lore;

        if (meta.getLore() == null || meta.getLore().isEmpty()) {
            if (clearLine) {
                lore = new String[] { " ", desc };
            } else {
                lore = new String[] { desc };
            }
        } else {
            lore = new String[meta.getLore().size() + (clearLine ? 2 : 1)];
            for (int i = 0; i < lore.length - (clearLine ? 2 : 1); i++) {
                lore[i] = meta.getLore().get(i);
            }
            if (clearLine) {
                lore[lore.length - 2] = " ";
            }
            lore[lore.length - 1] = desc;
        }

        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getUnknownItem() {
        ItemStack item = getHead("MHF_Question");
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName("§8?????");
        meta.setLore(Arrays.asList("§7§kYou dont have this gadget"));
        item.setItemMeta(meta);
        return item;
    }

    public static Vector getVectorForTwoPoints(Location locationOne, Location locationTwo) {
        double g = -0.15;
        double d = locationTwo.distance(locationOne);
        double t = d;
        double vX = (1.0 + 0.07 * t) * (locationTwo.getX() - locationOne.getX()) / t;
        double vY = (1.0 + 0.03 * t) * (locationTwo.getY() - locationOne.getY()) / t - 0.5 * g * t;
        double vZ = (1.0 + 0.07 * t) * (locationTwo.getZ() - locationOne.getZ()) / t;
        return new Vector(vX / 2, vY / 2, vZ / 2).multiply(2);
    }

    public static void addPlayerHealth(double health, Player player) {

        double max = player.getMaxHealth();

        double healthToSet = player.getHealth();
        healthToSet += health;

        if (healthToSet > max) {
            healthToSet = max;
        }

        player.setHealth(healthToSet);

    }

    public static void clearInventory(Inventory inventory, Integer... dontClear) {

        if (inventory instanceof PlayerInventory) {
            ((PlayerInventory) inventory).setArmorContents(null);
        }

        if (dontClear == null) {
            inventory.clear();
            return;
        }

        List<Integer> dontClearList = Arrays.asList(dontClear);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (dontClearList.contains(i)) continue;
            inventory.setItem(i, null);
        }

    }

    public static void fillInventory(Inventory inventory, ItemStack item, Integer[] dontFill) {

        List<Integer> dontFillList = dontFill != null ? Arrays.asList(dontFill) : null;

        for (int i = 0; i < inventory.getSize(); i++) {

            if (dontFillList != null && dontFillList.contains(i)) continue;
            inventory.setItem(i, item);

        }

    }

    public static <T> boolean arrayHasSameContents(T[] array1, T[] array2) {

        if (array1.length != array2.length) return false;

        List<T> list1 = Arrays.asList(array1);
        List<T> list2 = Arrays.asList(array2);

        for (T currentItem : list1) {
            if (!list2.contains(currentItem)) {
                return false;
            }
        }

        for (T currentItem : list2) {
            if (!list1.contains(currentItem)) {
                return false;
            }
        }

        return true;

    }

    public static boolean deleteWorld(File path) {

        if (path.exists()) {

            File[] files = path.listFiles();
            if (files == null) return false;

            for (File currentFile : files) {
                if (currentFile.isDirectory()) {
                    deleteWorld(currentFile);
                } else {
                    if (currentFile.getName().equals("session.lock")) continue;
                    currentFile.delete();
                }
            }
        }

        return(path.delete());

    }

    public static String getWorldName(String worldName) {

        if (worldName == null) return "";

        if (worldName.toLowerCase().endsWith("_nether")) {
            return "Nether";
        } else if (worldName.toLowerCase().endsWith("_the_end")) {
            return "The End";
        } else {
            return "Overworld";
        }

    }

    public static Location getHighestBlock(Location location) {

        location = location.clone();
        location.getChunk().load(true);
        location.setY(255);

        while ((location.getBlock().isPassable() && location.getBlock().getType() != Material.WATER) && location.getBlockY() > 1) {
            location.subtract(0, 1, 0);
            System.out.println(location.getBlockY());
        }

        location.add(0, 1, 0);
        return location;

    }

    public static void replaceItems(Inventory inventory, Material material, ItemStack item) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType() == material) {
                inventory.setItem(i, item);
            }
        }
    }

    public static void forEachPlayerOnline(Consumer<Player> consumer) {
        Bukkit.getOnlinePlayers().forEach(consumer);
    }

    public static ArrayList<String> getMatchingSuggestions(String argument, String... suggestions) {
        return getMatchingSuggestions(argument, new ArrayList<>(Arrays.asList(suggestions)));
    }

    public static ArrayList<String> getMatchingSuggestions(String argument, ArrayList<String> suggestions) {
        suggestions.removeIf(currentSuggestion -> !currentSuggestion.toLowerCase().startsWith(argument));
        Collections.sort(suggestions);
        return suggestions;
    }

    public static Material getWool(org.bukkit.ChatColor color) {
        switch (color) {
            case DARK_GREEN:
                return Material.GREEN_WOOL;
            case BLUE:
                return Material.CYAN_WOOL;
            case DARK_RED:
                return Material.RED_WOOL;
            case GREEN:
                return Material.LIME_WOOL;
            case LIGHT_PURPLE:
                return Material.PINK_WOOL;
            case YELLOW:
                return Material.YELLOW_WOOL;
            case GRAY:
                return Material.LIGHT_GRAY_WOOL;
            case DARK_GRAY:
                return Material.GRAY_WOOL;
            case DARK_PURPLE:
                return Material.PURPLE_WOOL;
            case GOLD:
                return Material.ORANGE_WOOL;
            case DARK_BLUE:
                return Material.BLUE_WOOL;
            case BLACK:
                return Material.BLACK_WOOL;
            case RED:
                return Material.MAGENTA_WOOL;
            case AQUA:
                return Material.LIGHT_BLUE_WOOL;
            default:
                return Material.WHITE_WOOL;
        }
    }

    public static Material getTerracotta(int subid) {
        switch (subid) {
            case 2:
                return Material.ORANGE_TERRACOTTA;
            case 3:
                return Material.MAGENTA_TERRACOTTA;
            case 4:
                return Material.LIGHT_BLUE_TERRACOTTA;
            case 5:
                return Material.YELLOW_TERRACOTTA;
            case 6:
                return Material.LIME_TERRACOTTA;
            case 7:
                return Material.PINK_TERRACOTTA;
            case 8:
                return Material.GRAY_TERRACOTTA;
            case 9:
                return Material.LIGHT_GRAY_TERRACOTTA;
            case 10:
                return Material.CYAN_TERRACOTTA;
            case 11:
                return Material.PURPLE_TERRACOTTA;
            case 12:
                return Material.BLUE_TERRACOTTA;
            case 13:
                return Material.BROWN_TERRACOTTA;
            case 14:
                return Material.GREEN_TERRACOTTA;
            case 15:
                return Material.RED_TERRACOTTA;
            case 16:
                return Material.BLACK_TERRACOTTA;
            default:
                return Material.WHITE_TERRACOTTA;
        }
    }

    public static Properties readProperties(File file) throws IOException {
        return readProperties(file.toURI().toURL());
    }

    public static Properties readProperties(URL url) throws IOException {
        Properties properties = new Properties();
        InputStream input = url.openConnection().getInputStream();
        properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        return properties;
    }

    public static void copyProperties(Properties source, Properties destination, File destinationFile) throws IOException {
        for (String currentKey : source.stringPropertyNames()) {
            String currentValue = source.getProperty(currentKey);
            destination.setProperty(currentKey, currentValue);
        }
        saveProperties(destination, destinationFile);
    }

    public static void saveProperties(Properties properties, File file) throws IOException {
        file.createNewFile();
        Writer writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);
        properties.store(writer, null);
        writer.flush();
        writer.close();
    }

    public static void saveJSON(File file, JSONObject jsonObject) throws IOException {
        Writer writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);
        jsonObject.writeJSONString(writer);
        writer.flush();
        writer.close();
    }

    public static JSONObject getJSONObject(File file) throws IOException, ParseException {
        return (JSONObject) new JSONParser().parse(new FileReader(file));
    }

    private final static ConcurrentHashMap<String, String> playerUUIDs = new ConcurrentHashMap<>();

    public static String getUUID(String playerName) {

        String uuid = playerUUIDs.get(playerName);
        if (uuid != null) {
            return uuid;
        }

        uuid = fetchUUID(playerName);
        playerUUIDs.put(playerName, uuid);
        return uuid;

    }

    @SuppressWarnings("depraction")
    public static String fetchUUID(String playerName) {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
        try {
            String UUIDJson = IOUtils.toString(new URL(url), Charset.defaultCharset());
            if (UUIDJson.isEmpty()) return "error";
            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
            return UUIDObject.get("id").toString();
        } catch (IOException | ParseException ex) {
            Log.severe("Could not fetch player uuid :: " + ex.getMessage());
            return "error";
        }
    }

    public static JSONObject fetch(String playerName) throws IOException, ParseException {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
        String UUIDJson = IOUtils.toString(new URL(url), Charset.defaultCharset());
        if (UUIDJson.isEmpty()) return null;
        return (JSONObject) JSONValue.parseWithException(UUIDJson);
    }

    public static int getInt(JSONObject jsonObject, String property) {
        try {
            return Math.toIntExact((Long) jsonObject.get(property));
        } catch (Exception ignored) {
            return 0;
        }
    }

    public static int getIntOrDefault(JSONObject jsonObject, String property, int defaultValue) {
        try {
            return Math.toIntExact((Long) jsonObject.getOrDefault(property, defaultValue));
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    public static boolean listContainsStringIgnoreCase(List<String> list, String searched) {
        for (String current : list) {
            if (current.equalsIgnoreCase(searched)) return true;
        }
        return false;
    }

    public static boolean nameIsValid(String name) {
        return name != null && name.length() > 1 && name.length() < 17;
    }

    public static int decimals(double number, int amount) {
        String string = String.valueOf(number).split("\\.")[1];
        while (amount > string.length()) {
            string += "0";
        }
        return Integer.parseInt(string);
    }

    public static void lookAt(Location edit, Location point, boolean lookUp, boolean lookDown) {
        Vector target = point.toVector();
        Location direction = edit.clone();
        direction.setDirection(target.subtract(edit.toVector()));
        float yaw = direction.getYaw();
        float pitch = direction.getPitch();
        edit.setYaw(yaw);
        if (lookDown && pitch > 0) {
            edit.setPitch(pitch);
        } else if (lookUp && pitch < 0) {
            edit.setPitch(pitch);
        }
    }

}
