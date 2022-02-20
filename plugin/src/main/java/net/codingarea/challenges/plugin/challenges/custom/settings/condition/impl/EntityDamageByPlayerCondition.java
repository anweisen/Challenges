package net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl;

import javax.annotation.Nonnull;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import net.codingarea.challenges.plugin.utils.misc.MapUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class EntityDamageByPlayerCondition extends AbstractChallengeCondition {

	public EntityDamageByPlayerCondition(String name) {
		super(name, createEntityTypeSettingsBuilder());
	}

	@Override
	public Material getMaterial() {
		return Material.WOODEN_SWORD;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDeath(@Nonnull EntityDamageByEntityEvent event) {

		Entity damager = event.getDamager();

		if (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player) {
			damager = ((Player) ((Projectile) damager).getShooter());
		}

		if (damager instanceof Player) {
			execute(event.getEntity(), MapUtils
					.createStringListMap("entity_type","any", event.getEntityType().name()));
		}

	}

}
