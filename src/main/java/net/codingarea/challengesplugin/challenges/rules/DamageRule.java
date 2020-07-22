package net.codingarea.challengesplugin.challenges.rules;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.Utils;
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

    private final String name;
    private final Material material;

    public DamageRule(Material material, String name, DamageCause... cause) {
        super(MenuType.DAMAGE);
        this.cause = cause;
        this.name = name;
        this.material = material;
        this.enabled = true;
    }

    @Override
    public String getChallengeName() {
        return Utils.getStringWithoutColorCodes(name).toLowerCase().replace(" ", "");
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(material, ItemTranslation.DAMAGE_RULE, name).hideAttributes().build();
    }

    @Override
    public void onEnable(ChallengeEditEvent event) { }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    @EventHandler
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