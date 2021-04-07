package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class MovementItemRemovingChallenge extends SettingModifier {

	public MovementItemRemovingChallenge() {
		super(MenuType.CHALLENGES,2);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.DETECTOR_RAIL, Message.forName("item-block-chunk-item-remove-challenge"));
	}

	@Nonnull
	@Override
	public ItemBuilder createSettingsItem() {
		if (!isEnabled()) return DefaultItem.disabled();
		if (getValue() == 1) return DefaultItem.create(Material.BOOK, "§6Block");
		return DefaultItem.create(Material.BOOKSHELF, "§6Chunk");
	}

	@Override
	public void playValueChangeTitle() {
		if (getValue() == 1) ChallengeHelper.playChangeChallengeValueTitle(this, "§6Block");
		else ChallengeHelper.playChangeChallengeValueTitle(this, "§6Chunk");
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerMove(@Nonnull PlayerMoveEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getPlayer())) return;
		if (event.getTo() == null) return;

		if (getValue() == 1) {
			if (BlockUtils.isSameBlockIgnoreHeight(event.getFrom(), event.getTo())) return;
		} else {
			if (BlockUtils.isSameChunk(event.getFrom().getChunk(), event.getTo().getChunk())) return;
		}

		InventoryUtils.removeRandomItem(event.getPlayer().getInventory());
	}

}
