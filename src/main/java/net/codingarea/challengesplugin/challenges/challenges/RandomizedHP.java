package net.codingarea.challengesplugin.challenges.challenges;

import de.dytanic.cloudnet.driver.event.EventListener;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.4
 */
public class RandomizedHP extends Setting implements Listener {

	public RandomizedHP() {
		super(MenuType.CHALLENGES);
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	private void setHealth(@NotNull LivingEntity entity) {
		double health = entity.getMaxHealth();
		double multiply = new Random().nextInt(1000) / 100D;
		double newHealth = health * multiply;
		if (newHealth < 1) newHealth = 1;
		entity.setMaxHealth(newHealth);
		entity.setHealth(newHealth);
	}

	@EventListener
	public void onSpawn(EntitySpawnEvent event) {
		if (!enabled) return;
		if (!(event.getEntity() instanceof LivingEntity)) return;
		LivingEntity entity = (LivingEntity) event.getEntity();
		if (entity instanceof Player) return;
		setHealth(entity);
	}

	@NotNull
	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.RED_STAINED_GLASS, ItemTranslation.RANDOMIZED_HP).build();
	}

}
