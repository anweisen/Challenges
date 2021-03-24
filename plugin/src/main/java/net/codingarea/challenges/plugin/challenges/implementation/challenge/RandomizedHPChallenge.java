package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.PotionBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class RandomizedHPChallenge extends SettingModifier {

	public RandomizedHPChallenge() {
		super(MenuType.CHALLENGES, 5);
	}

	@Override
	public void onEnable() {
		randomizeExistingEntityHealth();
	}

	@Override
	protected void onValueChange() {
		randomizeExistingEntityHealth();
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onSpawn(@Nonnull EntitySpawnEvent event) {
		if (!shouldExecuteEffect()) return;
		if (!(event.getEntity() instanceof LivingEntity)) return;
		LivingEntity entity = (LivingEntity) event.getEntity();
		randomizeEntityHealth(entity);
	}

	private void randomizeEntityHealth(@Nonnull LivingEntity entity) {
		if (entity instanceof Player) return;
		int health = new Random().nextInt(getValue() * 100) + 1;
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
		entity.setHealth(health);
	}

	private void randomizeExistingEntityHealth() {
		for (World world : Bukkit.getWorlds()) {
			for (LivingEntity entity : world.getLivingEntities()) {
				randomizeEntityHealth(entity);
			}
		}

	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new PotionBuilder(Material.POTION, Message.forName("item-randomized-hp-challenge")).setColor(Color.RED);
	}

	@Nonnull
	@Override
	public ItemBuilder createSettingsItem() {
		return super.createSettingsItem().amount(isEnabled() ? getValue() * 5 : 1);
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChallengeHeartsValueChangeTitle(this, getValue() * 100);
	}

	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return Message.forName("item-randomized-hp-challenge-description").asArray(getValue() * 50);
	}

}