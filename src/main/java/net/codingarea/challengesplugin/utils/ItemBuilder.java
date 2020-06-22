package net.codingarea.challengesplugin.utils;

import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class ItemBuilder {

    public static final ItemStack FILL_ITEM = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "§0").build();
    public static final ItemStack FILL_ITEM_2 = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, "§0").build();;

    public static class SimpleEnchantment {

        private Enchantment enchantment;
        private int level;

        public SimpleEnchantment(Enchantment enchantment, int level) {
            this.level = level;
            this.enchantment = enchantment;
        }

        public Enchantment getEnchantment() {
            return enchantment;
        }

        public int getLevel() {
            return level;
        }
    }

    public static class SkullBuilder {

        private ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        private SkullMeta meta = (SkullMeta) item.getItemMeta();

        public SkullBuilder(String skullOwner, String name) {
            meta.setOwner(skullOwner);
            meta.setDisplayName(name);
        }

        public SkullBuilder setDisplayName(String name) {
            meta.setDisplayName(name);
            return this;
        }

        public ItemStack build() {
            item.setItemMeta(meta);
            return item;
        }

    }

    public static class LeatherArmorBuilder {

        private ItemStack item;
        private LeatherArmorMeta meta;

        public LeatherArmorBuilder(Material material, String name, Color color) {
            item = new ItemStack(material);
            meta = (LeatherArmorMeta) item.getItemMeta();
            setDisplayName(name);
            setColor(color);
        }

        public LeatherArmorBuilder(Material material, ItemTranslation translation, Color color) {
            item = new ItemStack(material);
            meta = (LeatherArmorMeta) item.getItemMeta();
            setDisplayName(translation.getName());
            String itemName = translation.getName().substring(0, 2);
            setLore(ItemTranslation.formatLore(translation.getLore(), itemName));
        }

        public LeatherArmorBuilder setColor(Color color) {
            meta.setColor(color);
            return this;
        }

        public LeatherArmorBuilder setDisplayName(String name) {
            meta.setDisplayName(name);
            return this;
      }

        public ItemStack build() {
            item.setItemMeta(meta);
            return item;
        }

        public LeatherArmorBuilder setLore(String... lore) {
            meta.setLore(Arrays.asList(lore));
            return this;
        }

        public LeatherArmorBuilder setLore(List<String> lore) {
            meta.setLore(lore);
            return this;
        }

        public LeatherArmorBuilder addEnchant(Enchantment enchantment, int level) {
            item.addUnsafeEnchantment(enchantment, level);
            return this;
        }

        public LeatherArmorBuilder addEnchant(SimpleEnchantment... enchantment) {
            for (SimpleEnchantment currentEnchantment : enchantment) {
                item.addUnsafeEnchantment(currentEnchantment.getEnchantment(), currentEnchantment.getLevel());
            }
            return this;
        }

        public LeatherArmorBuilder hideAttributes() {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
            return this;
        }

        public LeatherArmorBuilder addAttributes(ItemFlag... flag) {
            meta.addItemFlags(flag);
            return this;
        }

        public LeatherArmorBuilder setUnbreakable() {
            meta.setUnbreakable(true);
            return this;
        }

        public LeatherArmorBuilder setAmount(int amount) {
            item.setAmount(amount);
            return this;
        }

    }

    public static class TippedArrowBuilder {

        private ItemStack item;
        private PotionMeta meta;

        public TippedArrowBuilder(Material potion, PotionType type, String name) {
            item = new ItemStack(potion);
            meta = (PotionMeta) item.getItemMeta();
            setEffect(type);
            setDisplayName(name);
        }

        public TippedArrowBuilder(PotionType type, String name) {
            item = new ItemStack(Material.POTION);
            meta = (PotionMeta) item.getItemMeta();
            setEffect(type);
            setDisplayName(name);
        }

        public TippedArrowBuilder setEffect(PotionType effect) {
            meta.setBasePotionData(new PotionData(effect));
            return this;
        }

        public TippedArrowBuilder addEffect(PotionEffect effect) {
            meta.addCustomEffect(effect, true);
            return this;
        }

        public TippedArrowBuilder setDisplayName(String name) {
            meta.setDisplayName(name);
            return this;
        }

        public TippedArrowBuilder setLore(String... lore) {
            meta.setLore(Arrays.asList(lore));
            return this;
        }

        public TippedArrowBuilder addEnchant(Enchantment enchantment, int level) {
            item.addUnsafeEnchantment(enchantment, level);
            return this;
        }

        public TippedArrowBuilder addEnchant(SimpleEnchantment... enchantment) {
            for (SimpleEnchantment currentEnchantment : enchantment) {
                item.addUnsafeEnchantment(currentEnchantment.getEnchantment(), currentEnchantment.getLevel());
            }
            return this;
        }

        public TippedArrowBuilder hideAttributes() {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
            return this;
        }

        public TippedArrowBuilder addAttributes(ItemFlag... flag) {
            meta.addItemFlags(flag);
            return this;
        }

        public TippedArrowBuilder setUnbreakable() {
            meta.setUnbreakable(true);
            return this;
        }

        public TippedArrowBuilder setAmount(int amount) {
            item.setAmount(amount);
            return this;
        }

        public ItemStack build() {
            item.setItemMeta(meta);
            return item;
        }

    }

    private ItemStack item;
    private ItemMeta meta;

    public ItemBuilder(Material material) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
    }

    public ItemBuilder(Material material, short subID) {
        item = new ItemStack(material, subID);
        meta = item.getItemMeta();
    }

    public ItemBuilder(Material material, String name) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
        setDisplayName(name);
    }

    public ItemBuilder(Material material, short subID, String name) {
        item = new ItemStack(material, subID);
        meta = item.getItemMeta();
        setDisplayName(name);
    }

    public ItemBuilder(Material material, String... lore) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
        setLore(lore);
    }

    public ItemBuilder(Material material, short subID, String... lore) {
        item = new ItemStack(material, subID);
        meta = item.getItemMeta();
        setLore(lore);
    }

    public ItemBuilder(Material material, String name, String... lore) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
        setDisplayName(name);
        setLore(lore);
    }

    public ItemBuilder(Material material, short subID, String name, String... lore) {
        item = new ItemStack(material, subID);
        meta = item.getItemMeta();
        setDisplayName(name);
        setLore(lore);
    }

    public ItemBuilder(Material material, ItemTranslation translation) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
        setDisplayName(translation.getName());
        String color = translation.getName().substring(0, 2);
        setLore(ItemTranslation.formatLore(translation.getLore(), color));
    }

    public ItemBuilder(Material material, ItemTranslation translation, String name) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
        setDisplayName(name);
        String color = name.substring(0, 2);
        setLore(ItemTranslation.formatLore(translation.getLore(), color, new Replacement("%info%", name.toLowerCase())));
    }

    public ItemBuilder setDisplayName(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        meta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addEnchant(SimpleEnchantment... enchantment) {
        for (SimpleEnchantment currentEnchantment : enchantment) {
            item.addUnsafeEnchantment(currentEnchantment.getEnchantment(), currentEnchantment.getLevel());
        }
        return this;
    }

    public ItemBuilder hideAttributes() {
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }

    public ItemBuilder addAttributes(ItemFlag... flag) {
        meta.addItemFlags(flag);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        meta.setUnbreakable(true);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemStack getItem() {
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }



}