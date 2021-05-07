package net.codingarea.challenges.plugin.challenges.type;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.misc.ParticleUtils;
import org.bukkit.Particle;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public abstract class HydraChallenge extends Setting {

	public HydraChallenge(@Nonnull MenuType menu) {
		super(menu);
	}

	public HydraChallenge(@Nonnull MenuType menu, boolean enabledByDefault) {
		super(menu, enabledByDefault);
	}

	public abstract int getNewMobsCount(@Nonnull EntityType entityType);

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDamageByEntity(@Nonnull EntityDamageByEntityEvent event) {
		if (!shouldExecuteEffect()) return;
		if (!(event.getDamager() instanceof Player)) return;
		if (event.getEntity() instanceof EnderDragon || event.getEntity() instanceof Player) return;
		if (!(event.getEntity() instanceof LivingEntity)) return;
		LivingEntity entity = (LivingEntity) event.getEntity();
		if (entity.getHealth() - event.getDamage() > 0) return;
		if (ChallengeHelper.ignoreDamager(event.getDamager())) return;

		int mobsCount = getNewMobsCount(event.getEntityType());

		for (int i = 0; i < mobsCount; i++) {
			event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), event.getEntityType());
		}
		ParticleUtils.spawnUpGoingParticleCircle(Challenges.getInstance(), event.getEntity().getLocation(), Particle.SPELL_MOB, 2, 17, 1);
	}

}