package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.AdvancedChallenge;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-02-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public class BedrockWallChallenge extends AdvancedChallenge implements Listener {

    public BedrockWallChallenge() {
        this.menu = MenuType.CHALLENGES;
        this.value = 10;
        this.maxValue = 50;
        this.nextActionInSeconds = -1;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.BEDROCK, ItemTranslation.BEDROCK_WALL).build();
    }

    @Override
    public void onValueChange(ChallengeEditEvent event) { }

    @Override
    public void onEnable(ChallengeEditEvent event) {
        this.value = 10;
    }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    @Override
    public void onTimeActivation() { }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!this.enabled || !Challenges.timerIsStarted()) return;
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) return;

        Location location = event.getPlayer().getLocation();

        Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
            replaceAllBlocks(location, Material.BEDROCK);

        }, this.value*20);

    }

    public void replaceAllBlocks(Location location, Material material) {
        for (int i = 1; i < location.getWorld().getMaxHeight(); i++) {
            location.setY(i);
            if (location.getBlock().getType() == Material.END_PORTAL || location.getBlock().getType() == Material.NETHER_PORTAL) continue;
            location.getBlock().setType(material);
        }

    }

}