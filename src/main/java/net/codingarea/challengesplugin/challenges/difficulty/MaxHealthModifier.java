package net.codingarea.challengesplugin.challenges.difficulty;

import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.challengetypes.Modifier;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-12-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class MaxHealthModifier extends Modifier implements Listener {

    public MaxHealthModifier() {
        this.value = 20;
        maxValue = 64;
        menu = MenuType.DIFFICULTY;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.RED_DYE, ItemTranslation.MAX_HEALTH).build();
    }

    @Override
    public void onMenuClick(ChallengeEditEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setMaxHealth(this.value);
            player.setHealth(this.value);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setMaxHealth(this.value);
    }
}