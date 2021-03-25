package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.LeatherArmorBuilder;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class FloorIsLavaChallenge extends SettingModifier {

	public FloorIsLavaChallenge() {
		super(MenuType.CHALLENGES, 1, 15);
	}

	@EventHandler
	public void onMove(@Nonnull PlayerMoveEvent event) {
		if (!shouldExecuteEffect()) return;
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
		if (event.getTo() == null) return;
		if (!BlockUtils.isSameBlockIgnoreHeight(event.getFrom(), event.getTo())) return;

		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			createMagmaFloor(event.getTo());
		}, getValue() * 20L);
	}

	private void createMagmaFloor(@Nonnull Location to) {
		BlockUtils.setBlockNatural(BlockUtils.getBlockBelow(to), Material.MAGMA_BLOCK, true);
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			createLavaFloor(to);
		}, getValue() * 20L);
	}

	private void createLavaFloor(@Nonnull Location to) {
		BlockUtils.setBlockNatural(BlockUtils.getBlockBelow(to), Material.LAVA, true);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.MAGMA_BLOCK, Message.forName("item-floor-lava-challenge"));
	}

	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return Message.forName("item-time-seconds-description").asArray(getValue());
	}

}