package net.codingarea.challengesplugin.challenges.difficulty;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Modifier;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-03-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */
public class DamageMultiplierModifier extends Modifier implements Listener {

    public DamageMultiplierModifier() {
        maxValue = 5;
        menu = MenuType.DIFFICULTY;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.STONE_SWORD, ItemTranslation.DAMAGE_MULTIPLIER).build();
    }

    @Override
    public void onMenuClick(ChallengeEditEvent event) { }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!Challenges.timerIsStarted()) return;
        if (event.getCause() == DamageCause.VOID) return;

        event.setDamage(event.getDamage() * value);

    }

}
