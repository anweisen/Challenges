package net.codingarea.challenges.plugin.utils.item;

import net.anweisen.utilities.bukkit.utils.GameProfileUtils;
import net.anweisen.utilities.commons.annotations.DeprecatedSince;
import net.anweisen.utilities.commons.annotations.ReplaceWith;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.ItemDescription;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import net.codingarea.challenges.plugin.utils.misc.DatabaseHelper;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class ItemBuilder {

	public static final ItemStack FILL_ITEM     = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§0").build(),
								  FILL_ITEM_2   = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§0").build(),
								  BLOCKED_ITEM  = new ItemBuilder(Material.BARRIER, "§cBlocked").build(),
								  AIR           = new ItemStack(Material.AIR);

	protected ItemStack item;
	protected ItemMeta meta;

	protected ItemDescription builtByItemDescription;

	public ItemBuilder(@Nonnull ItemStack item) {
		this(item, item.getItemMeta());
	}

	public ItemBuilder(@Nonnull ItemStack item, @Nullable ItemMeta meta) {
		this.item = item;
		this.meta = meta;
	}

	public ItemBuilder() {
		this(Material.BARRIER, ItemDescription.empty());
	}

	public ItemBuilder(@Nonnull Material material) {
		this(new ItemStack(material));
	}

	public ItemBuilder(@Nonnull Material material, @Nonnull Message message) {
		this(material, message.asItemDescription());
	}

	public ItemBuilder(@Nonnull Material material, @Nonnull ItemDescription description) {
		this(material);
		applyFormat(description);
	}

	public ItemBuilder(@Nonnull Material material, @Nonnull String name) {
		this(material);
		setName(name);
	}

	public ItemBuilder(@Nonnull Material material, @Nonnull String name, @Nonnull String... lore) {
		this(material);
		setName(name);
		setLore(lore);
	}

	public ItemBuilder(@Nonnull Material material, @Nonnull String name, int amount) {
		this(material);
		setName(name);
		setAmount(amount);
	}

	@Nonnull
	public ItemMeta getMeta() {
		return getCastedMeta();
	}

	@Nonnull
	@SuppressWarnings("unchecked")
	public final <M> M getCastedMeta() {
		return (M) (meta == null ? meta = item.getItemMeta() : meta);
	}

	@Nonnull
	public ItemBuilder setLore(@Nonnull Message message) {
		return setLore(message.asArray());
	}

	@Nonnull
	public ItemBuilder setLore(@Nonnull List<String> lore) {
		getMeta().setLore(lore);
		return this;
	}

	@Nonnull
	public ItemBuilder setLore(@Nonnull String... lore) {
		return setLore(Arrays.asList(lore));
	}

	@Nonnull
	public ItemBuilder appendLore(@Nonnull String... lore) {
		return appendLore(Arrays.asList(lore));
	}

	@Nonnull
	public ItemBuilder appendLore(@Nonnull Collection<String> lore) {
		List<String> newLore = getMeta().getLore();
		if (newLore == null) newLore = new ArrayList<>();
		newLore.addAll(lore);
		setLore(newLore);
		return this;
	}

	@Nonnull
	public ItemBuilder setName(@Nullable String name) {
		getMeta().setDisplayName(name);
		return this;
	}

	@Nonnull
	public ItemBuilder setName(@Nullable Object name) {
		return setName(name == null ? null : name.toString());
	}

	@Nonnull
	public ItemBuilder setName(@Nonnull String... content) {
		if (content.length > 0) setName(content[0]);
		if (content.length > 1) setLore(Arrays.copyOfRange(content, 1, content.length));
		return this;
	}

	@Nonnull
	public ItemBuilder appendName(@Nullable Object sequence) {
		String name = getMeta().getDisplayName();
		return setName(name  + sequence);
	}

	@Nonnull
	public ItemBuilder name(@Nullable Object name) {
		return setName(name);
	}

	@Nonnull
	public ItemBuilder name(@Nonnull String... content) {
		return setName(content);
	}

	@Nonnull
	public ItemBuilder addEnchantment(@Nonnull Enchantment enchantment, int level) {
		getMeta().addEnchant(enchantment, level, true);
		return this;
	}

	@Nonnull
	public ItemBuilder enchant(@Nonnull Enchantment enchantment, int level) {
		return addEnchantment(enchantment, level);
	}

	@Nonnull
	public ItemBuilder addFlag(@Nonnull ItemFlag... flags) {
		getMeta().addItemFlags(flags);
		return this;
	}

	@Nonnull
	public ItemBuilder removeFlag(@Nonnull ItemFlag... flags) {
		getMeta().removeItemFlags(flags);
		return this;
	}

	@Nonnull
	public ItemBuilder hideAttributes() {
		return addFlag(ItemFlag.values());
	}

	@Nonnull
	public ItemBuilder showAttributes() {
		return removeFlag(ItemFlag.values());
	}

	@Nonnull
	public ItemBuilder setUnbreakable(boolean unbreakable) {
		getMeta().setUnbreakable(unbreakable);
		return this;
	}

	@Nonnull
	public ItemBuilder unbreakable() {
		return setUnbreakable(true);
	}

	@Nonnull
	public ItemBuilder breakable() {
		return setUnbreakable(false);
	}

	@Nonnull
	public ItemBuilder setAmount(int amount) {
		item.setAmount(Math.min(Math.max(amount, 0), 64));
		return this;
	}

	@Nonnull
	public ItemBuilder amount(int amount) {
		return setAmount(amount);
	}

	@Nonnull
	public ItemBuilder setDamage(int damage) {
		this.<Damageable>getCastedMeta().setDamage(damage);
		return this;
	}

	@Nonnull
	public ItemBuilder damage(int damage) {
		return setDamage(damage);
	}

	@Nonnull
	public ItemBuilder applyFormat(@Nonnull ItemDescription description) {
		builtByItemDescription = description;
		setName(description.getName());
		setLore(description.getLore());
		return this;
	}

	@Nullable
	public ItemDescription getBuiltByItemDescription() {
		return builtByItemDescription;
	}

	@Nonnull
	public String getName() {
		return getMeta().getDisplayName();
	}

	@Nonnull
	public List<String> getLore() {
		List<String> lore = getMeta().getLore();
		return lore == null ? new ArrayList<>() : lore;
	}

	public int getAmount() {
		return item.getAmount();
	}

	public int getDamage() {
		return this.<Damageable>getCastedMeta().getDamage();
	}

	@Nonnull
	public ItemStack build() {
		item.setItemMeta(getMeta()); // Call to getter to prevent null value
		return item;
	}

	@Nonnull
	public ItemStack toItem() {
		return build();
	}

	@Override
	public ItemBuilder clone() {
		ItemBuilder builder = new ItemBuilder(item.clone(), getMeta().clone());
		builder.builtByItemDescription = builtByItemDescription;
		return builder;
	}

	public static class BannerBuilder extends ItemBuilder {

		public BannerBuilder(@Nonnull Material material) {
			super(material);
		}

		public BannerBuilder(@Nonnull Material material, @Nonnull Message message) {
			super(material, message);
		}

		public BannerBuilder(@Nonnull Material material, @Nonnull ItemDescription description) {
			super(material, description);
		}

		public BannerBuilder(@Nonnull Material material, @Nonnull String name) {
			super(material, name);
		}

		public BannerBuilder(@Nonnull Material material, @Nonnull String name, @Nonnull String... lore) {
			super(material, name, lore);
		}

		public BannerBuilder(@Nonnull Material material, @Nonnull String name, int amount) {
			super(material, name, amount);
		}

		public BannerBuilder(@Nonnull ItemStack item) {
			super(item);
		}

		@Nonnull
		public BannerBuilder addPattern(@Nonnull BannerPattern pattern, @Nonnull DyeColor color) {
			return addPattern(pattern.getPatternType(), color);
		}

		@Nonnull
		public BannerBuilder addPattern(@Nonnull PatternType pattern, @Nonnull DyeColor color) {
			getMeta().addPattern(new Pattern(color, pattern));
			return this;
		}

		@Nonnull
		@Override
		public BannerMeta getMeta() {
			return getCastedMeta();
		}

	}

	public static class SkullBuilder extends ItemBuilder {

		public SkullBuilder() {
			super(Material.PLAYER_HEAD);
		}

		/**
		 * @deprecated Use uuid and name to be able to access database cached textures to reduce loading time
		 */
		@Deprecated
		@DeprecatedSince("2.0")
		public SkullBuilder(@Nonnull String owner) {
			super(Material.PLAYER_HEAD);
			setOwner(owner);
		}

		/**
		 * @deprecated Use uuid and name to be able to access database cached textures to reduce loading time
		 */
		@Deprecated
		@DeprecatedSince("2.0")
		public SkullBuilder(@Nonnull String owner, @Nonnull String name, @Nonnull String... lore) {
			super(Material.PLAYER_HEAD, name, lore);
			setOwner(owner);
		}

		public SkullBuilder(@Nonnull UUID ownerUUID, @Nonnull String ownerName) {
			super(Material.PLAYER_HEAD);
			setOwner(ownerUUID, ownerName);
		}

		public SkullBuilder(@Nonnull UUID ownerUUID, @Nonnull String ownerName, @Nonnull Message message) {
			super(Material.PLAYER_HEAD, message);
			setOwner(ownerUUID, ownerName);
		}

		public SkullBuilder(@Nonnull UUID ownerUUID, @Nonnull String ownerName, @Nonnull ItemDescription description) {
			super(Material.PLAYER_HEAD, description);
			setOwner(ownerUUID, ownerName);
		}

		public SkullBuilder(@Nonnull UUID ownerUUID, @Nonnull String ownerName, @Nonnull String name, @Nonnull String... lore) {
			super(Material.PLAYER_HEAD, name, lore);
			setOwner(ownerUUID, ownerName);
		}

		@Nonnull
		@Deprecated
		@DeprecatedSince("2.0")
		@ReplaceWith("setOwner(UUID)")
		public SkullBuilder setOwner(@Nonnull String owner) {
			getMeta().setOwner(owner);
			return this;
		}

		@Nonnull
		public SkullBuilder setOwner(@Nonnull UUID uuid, @Nonnull String name) {
			if (Challenges.getInstance().getDatabaseManager().isEnabled()) {
				String textures = DatabaseHelper.getTextures(uuid);
				if (textures != null) {
					GameProfileUtils.applyTextures(getMeta(), uuid, name, textures);
					return this;
				}
			}

			getMeta().setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
			return this;
		}

		@Nonnull
		@Override
		public SkullMeta getMeta() {
			return getCastedMeta();
		}

	}

	public static class PotionBuilder extends ItemBuilder {

		@Nonnull
		@CheckReturnValue
		public static ItemBuilder createWaterBottle() {
			return new PotionBuilder(Material.POTION).setColor(Color.BLUE).hideAttributes();
		}

		public PotionBuilder(@Nonnull Material material) {
			super(material);
		}

		public PotionBuilder(@Nonnull Material material, @Nonnull Message message) {
			super(material, message);
		}

		public PotionBuilder(@Nonnull Material material, @Nonnull String name) {
			super(material, name);
		}

		public PotionBuilder(@Nonnull Material material, @Nonnull String name, @Nonnull String... lore) {
			super(material, name, lore);
		}

		public PotionBuilder(@Nonnull Material material, @Nonnull String name, int amount) {
			super(material, name, amount);
		}

		public PotionBuilder(@Nonnull ItemStack item) {
			super(item);
		}

		@Nonnull
		public PotionBuilder addEffect(@Nonnull PotionEffect effect) {
			getMeta().addCustomEffect(effect, true);
			return this;
		}

		@Nonnull
		public PotionBuilder setColor(@Nonnull Color color) {
			getMeta().setColor(color);
			return this;
		}

		@Nonnull
		public PotionBuilder color(@Nonnull Color color) {
			return setColor(color);
		}

		@Nonnull
		@Override
		public PotionMeta getMeta() {
			return getCastedMeta();
		}

	}

	public static class LeatherArmorBuilder extends ItemBuilder {

		public LeatherArmorBuilder(@Nonnull Material material) {
			super(material);
		}

		public LeatherArmorBuilder(@Nonnull Material material, @Nonnull Message message) {
			super(material, message);
		}

		public LeatherArmorBuilder(@Nonnull Material material, @Nonnull String name) {
			super(material, name);
		}

		public LeatherArmorBuilder(@Nonnull Material material, @Nonnull String name, @Nonnull String... lore) {
			super(material, name, lore);
		}

		public LeatherArmorBuilder(@Nonnull Material material, @Nonnull String name, int amount) {
			super(material, name, amount);
		}

		public LeatherArmorBuilder(@Nonnull ItemStack item) {
			super(item);
		}

		@Nonnull
		public LeatherArmorBuilder setColor(@Nonnull Color color) {
			getMeta().setColor(color);
			return this;
		}

		@Nonnull
		public LeatherArmorBuilder color(@Nonnull Color color) {
			return setColor(color);
		}

		@Nonnull
		@Override
		public LeatherArmorMeta getMeta() {
			return getCastedMeta();
		}

	}

}
