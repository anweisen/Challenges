package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class AllMobsToDeathPoint extends Setting {

	public AllMobsToDeathPoint() {
		super(MenuType.CHALLENGES);
	}

	@EventHandler
	public void onEntityDeath(@Nonnull EntityDamageByEntityEvent event) {
		if (!shouldExecuteEffect()) return;
		if (event.getEntity() instanceof EnderDragon || event.getEntity() instanceof Player) return;
		if (!(event.getEntity() instanceof LivingEntity)) return;
		LivingEntity entity = (LivingEntity) event.getEntity();
		if (entity.getHealth() - event.getDamage() > 0) return;
		if (ChallengeHelper.ignoreDamager(event.getDamager())) return;

		teleportAllMobsOfType(event.getEntityType(), event.getEntity().getLocation());
	}

	private void teleportAllMobsOfType(@Nonnull EntityType entityType, @Nonnull Location location) {
		if (location.getWorld() == null) return;
		Collection<Entity> entities = location.getWorld().getEntitiesByClasses(entityType.getEntityClass());
		for (Entity entity : entities) {
			if (!(entity instanceof LivingEntity)) return;
			((LivingEntity) entity).setNoDamageTicks(20);
			entity.teleport(location);
		}

	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.SPAWNER, Message.forName("item-all-mobs-to-death-position-challenge"));
	}

}