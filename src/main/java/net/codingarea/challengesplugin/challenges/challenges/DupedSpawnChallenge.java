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
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-25-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class DupedSpawnChallenge extends Setting implements Listener {

    public DupedSpawnChallenge() {
       super(MenuType.CHALLENGES);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.ELDER_GUARDIAN_SPAWN_EGG, ItemTranslation.DUPED_SPAWN).build();
    }

    @Override
    public void onEnable(ChallengeEditEvent event) { }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    boolean spawned = false;

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (!enabled) return;
        if (spawned) return;
        if (event.getEntity() instanceof EnderDragon) return;
        if (event.getEntity() instanceof Player) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;
        spawned = true;
        event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), event.getEntityType());
        spawned = false;
    }

}