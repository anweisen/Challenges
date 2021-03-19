package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.Modifier;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class TimberSetting extends Modifier {

	public TimberSetting() {
		super(MenuType.SETTINGS, 3);
	}

	@EventHandler
	public void onBreak(@Nonnull BlockBreakEvent event) {
		if (getValue() == 1 || !ChallengeAPI.isStarted()) return;
		if (!isLog(event.getBlock().getType())) return;

		// TODO: HANDLE TIMBER BREAK
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.DIAMOND_AXE, Message.forName("timer-setting"));
	}

	@Nonnull
	@Override
	public ItemBuilder createSettingsItem() {
		if (getValue() == 1) {
			return DefaultItem.status(false);
		} else if (getValue() == 2) {
			return new ItemBuilder(Material.OAK_LOG, "§6Logs");
		} else {
			return new ItemBuilder(Material.OAK_LEAVES, "§2Logs & Leaves");
		}
	}

	private boolean isLog(Material material) {
		String name = material.name();
		return name.contains("LOG") || name.contains("STEM");
	}
	private boolean isLeaves(Material material) {
		return material.name().contains("LEAVES");
	}

	public enum LogType {

		DARK_OAK,
		BIRCH,
		SPRUCE,
		OAK,
		ACACIA,
		JUNGLE,
		WARPED,
		CRIMSON,
		;

		public static LogType getType(Material material) {
			if (material == null) return null;

			String name = material.name().toLowerCase();

			for (LogType currentLogType : values()) {
				if (name.contains(currentLogType.name().toLowerCase())) return currentLogType;
			}

			return null;

		}

	}


}