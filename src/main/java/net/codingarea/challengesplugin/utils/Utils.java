package net.codingarea.challengesplugin.utils;

import net.minecraft.server.v1_15_R1.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class Utils {

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

        return new Material[]{Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
                Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
                Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
                Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
                Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS};
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
        List<Material> list = new ArrayList<Material>(Arrays.asList(Material.values()));
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
        List<Material> list = new ArrayList<Material>(Arrays.asList(Material.values()));
        list.remove(Material.COMMAND_BLOCK);
        list.remove(Material.CHAIN_COMMAND_BLOCK);
        list.remove(Material.REPEATING_COMMAND_BLOCK);
        list.remove(Material.STRUCTURE_BLOCK);
        list.remove(Material.STRUCTURE_VOID);
        list.remove(Material.NETHER_PORTAL);
        list.remove(Material.END_PORTAL);
        return list;
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

        return builder.toString();

    }

    public static String getPlayerListAsString(List<Player> list) {

        if (list == null) return "";
        if (list.isEmpty()) return "";

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

        String[] strings = { "0", "1", "2", "3", "4", "5", "6", "8", "9", "o", "k", "l", "m", "f", "e", "d", "b", "a", "c" };

        for (String currentString : strings) {
            string = string.replace("§" + currentString, "");
        }

        return string;

    }

    public static int getLocationInList(ItemStack toFind, List<ItemStack> list) {

        if (list == null || toFind == null) return -1;
        if (!list.contains(toFind)) return -1;

        for (int i = 0; i < list.size(); i++)  {

            ItemStack currentItem = list.get(i);
            if (currentItem.equals(toFind)) return i;

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
        if(location.getX() > dim[1] || location.getX() < dim[0])
            return false;

        dim[0] = loc1.getY();
        dim[1] = loc2.getY();
        Arrays.sort(dim);
        if(location.getY() > dim[1] || location.getY() < dim[0])
            return false;

        dim[0] = loc1.getZ();
        dim[1] = loc2.getZ();
        Arrays.sort(dim);
        if(location.getZ() > dim[1] || location.getZ() < dim[0])
            return false;

        return true;

    }

    public static void sendActionbar(Player player, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), ChatMessageType.GAME_INFO);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void spawnFireworks(Location location, int amount) {
        Location loc = location;
        Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        fireworkMeta.setPower(1);
        fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.LIME, Color.AQUA, Color.RED, Color.YELLOW, Color.PURPLE, Color.BLUE).flicker(true).withTrail().build());

        firework.setFireworkMeta(fireworkMeta);
        firework.detonate();

        for(int i = 0; i < amount; i++) {
            Random rand = new Random();
            double x = 0.1 + (0.9 - 0.1) * rand.nextDouble();
            double z = 0.1 + (0.9 - 0.1) * rand.nextDouble();
            Firework fireworkSpawned = (Firework) loc.getWorld().spawnEntity(loc.subtract(0.5, 0, 0.5).add(x, 0, z), EntityType.FIREWORK);
            fireworkSpawned.setFireworkMeta(fireworkMeta);
        }
    }

    public static void boostAway(Player player) {
        Vector vector = player.getLocation().getDirection();
        player.getLocation().getYaw();
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.5F, 1F);
        player.setVelocity(vector.multiply(-1F).setY(0.5F));
    }

    public static void boostForwards(Player player) {
        Vector vector = player.getLocation().getDirection();
        player.getLocation().getYaw();
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.5F, 1F);
        player.setVelocity(vector.multiply(3.4F).setY(1.3F));
    }

    public static void setFakeArmor(Player viewer, int entityID, Color color) {
        ItemStack armor = getChestplate(color);

        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(entityID, EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(armor));
        ((CraftPlayer) viewer).getHandle().playerConnection.sendPacket(packet);
    }

    public static ItemStack getChestplate(Color color) {
        ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
        meta.setColor(color);
        armor.setItemMeta(meta);
        return armor;
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
            }, delay);
            delay++;
            y -= startOfY / (double) points;
        }
    }

    public static void spawnAroundDownAndUpGoingParticleCircle(Location location, Effect particle, JavaPlugin plugin) {
        spawnAroundAndDownGoingParticleCircle(location, particle, 2.0D, 0.0D, plugin);
        spawnAroundAndUpGoingParticleCircle(location, particle, 0.0D, 2.0D, plugin);
    }

    public static void spawnUpgoingParticleCircle(Location location, Effect particle, JavaPlugin plugin, double height, double points, double radius) {
        int delay = 0;

        for (double d = 0.0D; d < height; d += 0.25) {
            delay++;

            final double D = d;

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                spawnParticleCircle(location.clone().add(0, D, 0), particle, points, radius);
            }, delay);
        }
    }

    public static void spawnUpgoingParticleCircle(Location location, Particle particle, JavaPlugin plugin, double height, double points, double radius) {
        int delay = 0;

        for (double d = 0.0D; d < height; d += 0.25) {
            delay++;

            final double D = d;

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                spawnParticleCircle(location.clone().add(0, D, 0), particle, points, radius);
            }, delay);
        }
    }

    public static List<Integer> getRandomDiffrentNumbers(int max, int numbers) {
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

        Integer[] array = dontClear;
        List<Integer> dontClearList = Arrays.asList(array);

        for (int i = 0; i < inventory.getSize(); i++) {

            if (dontClearList.contains(i)) continue;

            inventory.setItem(i, null);

        }

    }

    public static void fillInventory(Inventory inventory, ItemStack item, Integer[] dontFill) {

        List<Integer> dontFillList = (dontFill != null ? Arrays.asList(dontFill) : null);

        for (int i = 0; i < inventory.getSize(); i++) {

            if (dontFillList != null && dontFillList.contains(i)) continue;

            inventory.setItem(i, item);

        }

    }

    public static boolean arrayHasSameContents(ItemStack[] array1, ItemStack[] array2) {

        if (array1.length != array2.length) return false;

        List<ItemStack> list1 = Arrays.asList(array1);
        List<ItemStack> list2 = Arrays.asList(array2);

        for (ItemStack currentItem : list1) {

            if (!list2.contains(currentItem)) {
                return false;
            }

        }

        for (ItemStack currentItem : list2) {

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

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    Bukkit.broadcastMessage("Deleting world file " + files[i].getName());
                    if (files[i].getName().equals("session.lock")) continue;
                    files[i].delete();
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
            return "End";
        } else {
            return getEnumName(worldName);
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

}
