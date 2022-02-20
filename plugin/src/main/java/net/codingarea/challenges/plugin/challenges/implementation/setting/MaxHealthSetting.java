package net.codingarea.challenges.plugin.challenges.implementation.setting;

import javax.annotation.Nonnull;
import net.anweisen.utilities.bukkit.utils.item.MaterialWrapper;
import net.codingarea.challenges.plugin.challenges.type.abstraction.Modifier;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class MaxHealthSetting extends Modifier {

	public MaxHealthSetting() {
		super(MenuType.SETTINGS, 1, 100 * 2, 20);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(MaterialWrapper.RED_DYE, Message.forName("item-max-health-setting"));
	}

	@Nonnull
	@Override
	public ItemBuilder createSettingsItem() {
		return DefaultItem.value(getValue(), "§e").appendName(" §7HP §8(§e" + (getValue() / 2f) + " §c❤§8)");
	}

	@Override
	public void onValueChange() {
		broadcast(this::updateHealth);
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChallengeHeartsValueChangeTitle(this);
	}

	@EventHandler
	public void onJoin(@Nonnull PlayerJoinEvent event) {
		updateHealth(event.getPlayer());
	}


	private void updateHealth(Player player) {
		AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		if (attribute == null) return; // This should never happen because its a generic attribute, but just in case
		if (attribute.getBaseValue() != getValue()) {
			attribute.setBaseValue(getValue());
			player.setHealth(getValue());
		}
	}

}

