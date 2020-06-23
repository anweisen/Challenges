package net.codingarea.challengesplugin.challenges.rules;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-2-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class DamageRule extends Setting implements Listener {

    private final DamageCause[] cause;

    private String name;
    private Material material;

    public DamageRule(Material material, String name, DamageCause... cause) {
        this.cause = cause;
        this.menu = MenuType.DAMAGE;
        this.name = name;
        this.material = material;
        this.enabled = true;

    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(material, ItemTranslation.DAMAGE_RULE, name).hideAttributes().build();
    }

    @Override
    public void onEnable(ChallengeEditEvent event) { }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent event) {
        if (enabled || !Challenges.timerIsStarted()) return;
        if (!(event.getEntity() instanceof Player)) return;
        for (DamageCause damageCause : cause) {
            if (damageCause == event.getCause()) {
                event.setCancelled(true);
                return;
            }
        }

    }

}