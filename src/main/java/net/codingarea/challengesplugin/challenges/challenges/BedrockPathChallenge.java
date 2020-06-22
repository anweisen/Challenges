package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.ItemBuilder.LeatherArmorBuilder;
import net.codingarea.challengesplugin.challengetypes.Challenge;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Color;
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

public class BedrockPathChallenge extends Challenge implements Listener {

    public BedrockPathChallenge() {
        this.menu = MenuType.CHALLENGES;
        this.nextActionInSeconds = -1;
    }

    @Override
    public ItemStack getItem() {
        return new LeatherArmorBuilder(Material.LEATHER_BOOTS, ItemTranslation.BEDROCK_PATH, Color.GRAY).hideAttributes().build();
    }

    @Override
    public void onEnable(ChallengeEditEvent event) { }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    @Override
    public void onTimeActivation() { }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!this.enabled || !Challenges.timerIsStarted()) return;
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        if (!event.getFrom().getBlock().equals(event.getTo().getBlock())) return;

        Location location = event.getPlayer().getLocation();
        if (!Utils.isHalfBlock(location.getBlock().getType())) {
            location.subtract(0, 1, 0);
        }

        if (location.getBlock().isPassable() || location.getBlock().getType() == Material.END_PORTAL || location.getBlock().getType() == Material.NETHER_PORTAL) return;
        location.getBlock().setType(Material.BEDROCK);

    }

}