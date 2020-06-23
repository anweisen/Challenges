package net.codingarea.challengesplugin.challenges.challenges;

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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-23-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class InventoryClearOnDamageChallenge extends Setting implements Listener {

    public InventoryClearOnDamageChallenge() {
        this.menu = MenuType.CHALLENGES;

    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!this.enabled || !Challenges.timerIsStarted()) return;

        if (!(event.getEntity() instanceof Player)) return;
        Player player =(Player) event.getEntity();

        if (player.getGameMode() == GameMode.SPECTATOR) return;

        Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {

            if (!event.isCancelled()) {
                for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
                    if (currentPlayer.getGameMode() == GameMode.SPECTATOR) continue;
                    currentPlayer.getInventory().clear();
                    currentPlayer.getInventory().setArmorContents(null);

                }
            }
        }, 1);

    }

    @Override
    public void onEnable(ChallengeEditEvent event) { }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.TRAPPED_CHEST, ItemTranslation.DAMAGE_INVENTORY_CLEAR).build();
    }

}