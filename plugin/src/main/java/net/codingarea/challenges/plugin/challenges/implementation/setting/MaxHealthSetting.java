package net.codingarea.challenges.plugin.challenges.implementation.setting;

import javax.annotation.Nonnull;
import net.anweisen.utilities.bukkit.utils.item.MaterialWrapper;
import net.anweisen.utilities.common.config.Document;
import net.anweisen.utilities.common.config.document.GsonDocument;
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
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class MaxHealthSetting extends Modifier {

	/**
	 * Offset that can be used to modify the max health for specific players with other challenges.
	 * Is saved within the gamestate so it resets with a world reset.
	 * Saves the data with the uuid as string as the key and with an integer which defines the offset
	 * The health offset for every player is saved with the key "all"
	 */
	private Document valueOffset = new GsonDocument();

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
	public void writeGameState(@NotNull Document document) {
		super.writeGameState(document);
		document.set("offset", valueOffset);
	}

	@Override
	public void loadGameState(@NotNull Document document) {
		super.loadGameState(document);
		valueOffset = document.contains("offset") ? document.getDocument("offset") : new GsonDocument();
		onValueChange();
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
		int newMaxHealth = getMaxHealth(player);
		double oldMaxHealth = attribute.getBaseValue();
		if (oldMaxHealth != newMaxHealth) {
			attribute.setBaseValue(newMaxHealth);

			if (oldMaxHealth < newMaxHealth) {
				double oldHealth = player.getHealth();
				double newHealth = oldHealth + (newMaxHealth - oldMaxHealth);
				player.setHealth(Math.min(Math.max(newHealth, 0), newMaxHealth));
			}

		}
	}

	public int getMaxHealth(Player player) {
		String key = player.getUniqueId().toString();
		return getValue() + valueOffset.getInt("all") + valueOffset.getInt(key);
	}

	public void addHealth(int health) {
		valueOffset.set("all", valueOffset.getInt("all") + health);
		onValueChange();
	}

	public void addHealth(Player player, int health) {
		String key = player.getUniqueId().toString();
		valueOffset.set(key, valueOffset.getInt(key) + health);
		updateHealth(player);
	}

}

