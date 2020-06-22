package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-06-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */
public class HydraChallenge extends Setting implements Listener {

    public HydraChallenge() {
        this.menu = MenuType.CHALLENGES;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.WITCH_SPAWN_EGG, ItemTranslation.HYDRA).build();
    }

    @Override
    public void onEnable(ChallengeEditEvent event) { }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!this.enabled || !Challenges.timerIsStarted()) return;
        if (event.getEntity() instanceof EnderDragon) return;
        if (event.getEntity() instanceof Player) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (((LivingEntity) event.getEntity()).getHealth() - event.getDamage() > 0) return;
        if (!(event.getDamager() instanceof Player || (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player))) return;

        for (int i = 0; i < 2; i++) {
            event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), event.getEntityType());
        }
        Utils.spawnUpgoingParticleCircle(event.getEntity().getLocation(), Particle.SPELL_MOB, Challenges.getInstance(), 2, 17, 1);
    }

}