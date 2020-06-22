package net.codingarea.challengesplugin.challenges.difficulty;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */
public class SplitHealthSetting extends Setting implements Listener {

    public SplitHealthSetting() {
        this.menu = MenuType.DIFFICULTY;
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemBuilder(Material.TIPPED_ARROW, ItemTranslation.SPLIT_HEALTH).hideAttributes().getItem();
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void onEnable(ChallengeEditEvent event) { }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!enabled || !Challenges.timerIsStarted()) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() == DamageCause.CUSTOM) return;

        setHealth(((Player)event.getEntity()).getHealth() + ((Player)event.getEntity()).getAbsorptionAmount() - event.getDamage(), (Player)event.getEntity());

    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent event) {
        if (!enabled || !Challenges.timerIsStarted()) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getRegainReason() == RegainReason.CUSTOM) return;

        setHealth(((Player)event.getEntity()).getHealth() + ((Player)event.getEntity()).getAbsorptionAmount() + event.getAmount(), (Player)event.getEntity());

    }

    private void setHealth(double health, Player player) {

        for (Player currentPlayer : Bukkit.getOnlinePlayers()) {

            if (currentPlayer.getGameMode() == GameMode.SPECTATOR) continue;
            if (currentPlayer.equals(player)) continue;

            if (health <= 0) {
                currentPlayer.damage(currentPlayer.getMaxHealth());
                continue;
            } else if (health > currentPlayer.getMaxHealth()) {
                currentPlayer.setHealth(currentPlayer.getMaxHealth());
                continue;
            }

            currentPlayer.setHealth(health);

        }

    }

}