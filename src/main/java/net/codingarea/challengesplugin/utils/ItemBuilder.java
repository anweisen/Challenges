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

import java.util.ArrayList;
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

        private final Enchantment enchantment;
        private final int level;

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

    public static class SkullBuilder extends ItemBuilder {

        public SkullBuilder(String skullOwner, String name) {
            super(Material.PLAYER_HEAD, name);
        }

        @SuppressWarnings("depraction")
        public SkullBuilder setOwner(String owner) {
            ((SkullMeta)meta).setOwner(owner);
            return this;
        }

    }

    public static class LeatherArmorBuilder extends ItemBuilder {

        public LeatherArmorBuilder(Material material, String name, Color color) {
            super(material, name);
            setColor(color);
        }

        public LeatherArmorBuilder(Material material, ItemTranslation translation, Color color) {
           super(material, translation);
           setColor(color);
        }

        public LeatherArmorBuilder setColor(Color color) {
            ((LeatherArmorMeta)meta).setColor(color);
            return this;
        }

    }

    public static class TippedArrowBuilder extends ItemBuilder {

        public TippedArrowBuilder(Material potion, PotionType type, String name) {
            super(potion, name);
            setEffect(type);
            setDisplayName(name);
        }

        public TippedArrowBuilder(PotionType type, String name) {
            super(Material.POTION, name);
            setEffect(type);
            setDisplayName(name);
        }

        public TippedArrowBuilder setEffect(PotionType effect) {
            ((PotionMeta)meta).setBasePotionData(new PotionData(effect));
            return this;
        }

        public TippedArrowBuilder addEffect(PotionEffect effect) {
           ((PotionMeta)meta).addCustomEffect(effect, true);
            return this;
        }

    }

    protected final ItemStack item;
    protected final ItemMeta meta;

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

    public ItemBuilder(Material material, ItemTranslation translation, String name) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
        setDisplayName(name);
        String color = name.substring(0, 2);
        setLore(ItemTranslation.formatLore(translation.getLore(), color, new Replacement<>("%info%", name.toLowerCase())));
        hideAttributes();
    }

    public ItemBuilder(Material material, ItemTranslation translation, Replacement<?>... replacements) {
        item = new ItemStack(material);
        meta = item.getItemMeta();
        setDisplayName(translation.getName());
        String color = translation.getName().substring(0, 2);
        setLore(ItemTranslation.formatLore(translation.getLore(), color, replacements));
        hideAttributes();
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
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

    public ItemBuilder addLore(String... lines) {
        List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();
        lore.addAll(Arrays.asList(lines));
        meta.setLore(lore);
        return this;
    }

    public ItemBuilder addLore(List<String> lines) {
        List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();
        lore.addAll(lines);
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
            meta.addEnchant(currentEnchantment.getEnchantment(), currentEnchantment.getLevel(), true);
        }
        return this;
    }

    public ItemBuilder addEnchant(List<SimpleEnchantment> enchantment) {
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

    public ItemBuilder setDamage(short damage) {
        item.setDurability(damage);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public String getDisplayName() {
        return meta.getDisplayName();
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