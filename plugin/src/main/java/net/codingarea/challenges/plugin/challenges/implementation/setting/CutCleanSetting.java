package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.MenuSetting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.blocks.BlockDropManager.DropPriority;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.annotations.Since;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@Since("2.0")
public class CutCleanSetting extends MenuSetting {

	public CutCleanSetting() {
		super(MenuType.SETTINGS, "CutClean");
		registerSetting("iron->iron_ingot", new ConvertDropSubSetting(() -> new ItemBuilder(Material.IRON_INGOT, Message.forName("item-cut-clean-iron")), true, Material.IRON_ORE, Material.IRON_INGOT));
		registerSetting("gold->gold_ingot", new ConvertDropSubSetting(() -> new ItemBuilder(Material.GOLD_INGOT, Message.forName("item-cut-clean-gold")), true, Material.GOLD_ORE, Material.GOLD_INGOT));
		registerSetting("coal->torch",      new ConvertDropSubSetting(() -> new ItemBuilder(Material.COAL, Message.forName("item-cut-clean-coal")), false, Material.COAL_ORE, Material.TORCH));
		registerSetting("row->cooked",      new CookFoodSubSetting(() -> new ItemBuilder(Material.COOKED_BEEF, Message.forName("item-cut-clean-food")), true));
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.IRON_AXE, Message.forName("item-cut-clean"));
	}

	private class ConvertDropSubSetting extends BooleanSubSetting {

		protected final Material from, to;

		public ConvertDropSubSetting(@Nonnull Supplier<ItemBuilder> item, boolean enabledByDefault, @Nonnull Material from, @Nonnull Material to) {
			super(item, enabledByDefault);
			this.from = from;
			this.to = to;
		}

		@Override
		public void onEnable() {
			Challenges.getInstance().getBlockDropManager().setCustomDrops(from, to, DropPriority.CUT_CLEAN);
		}

		@Override
		public void onDisable() {
			Challenges.getInstance().getBlockDropManager().resetCustomDrop(from, DropPriority.CUT_CLEAN);
		}

	}

	private class CookFoodSubSetting extends BooleanSubSetting {

		public CookFoodSubSetting(@Nonnull Supplier<ItemBuilder> item, boolean enabledByDefault) {
			super(item, enabledByDefault);
		}

		@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
		public void onEntityKill(@Nonnull EntityDeathEvent event) {
			if (!isEnabled()) return;
			event.getDrops().replaceAll(item -> new ItemBuilder(ItemUtils.convertFoodToCookedFood(item.getType())).amount(item.getAmount()).build());
		}

	}

}
