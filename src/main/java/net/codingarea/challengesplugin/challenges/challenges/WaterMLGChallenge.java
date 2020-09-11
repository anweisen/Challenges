package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.AdvancedChallenge;
import net.codingarea.challengesplugin.manager.WorldManager;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-19-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class WaterMLGChallenge extends AdvancedChallenge implements Listener {

    private final ArrayList<Block> blocks = new ArrayList<>();

    public WaterMLGChallenge() {
        super(MenuType.CHALLENGES, 10);
        this.nextActionInSeconds = getRandomSeconds();
    }

    @Override
    public void onTimeActivation() {

        this.nextActionInSeconds = getRandomSeconds();
        Location chunkLocation = new Location(WorldManager.getInstance().getChallengesWorld(), 0.5, 100, 0.5);

        for (Player player : Bukkit.getOnlinePlayers()) {
            chunkLocation.add(50,0,0).getChunk().load(true);
        }

        Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {

            Location location = new Location(WorldManager.getInstance().getChallengesWorld(), 0.5, 100, 0.5);
            HashMap<Player, ItemStack[]> playerInventories = new HashMap<>();
            HashMap<Player, Location> playerLocations = new HashMap<>();
            HashMap<Player, Integer> playerSlots = new HashMap<>();

            for (Player player : Bukkit.getOnlinePlayers()) {

                if (player.getGameMode() == GameMode.SPECTATOR) continue;

                playerInventories.put(player, player.getInventory().getContents().clone());
                playerLocations.put(player, player.getLocation().clone());
                playerSlots.put(player, player.getInventory().getHeldItemSlot());

                Location playerLocation = location.add(50,0,0).clone();
                playerLocation.getChunk().load(true);

                player.getInventory().clear();
                player.getInventory().setItem(4, new ItemStack(Material.WATER_BUCKET));
                player.getInventory().setHeldItemSlot(4);

                player.teleport(playerLocation);

        }

            Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {

                for (Entry<Player, ItemStack[]> entry : playerInventories.entrySet()) {
                    try {
                        entry.getKey().getInventory().setContents(entry.getValue());
                        entry.getKey().teleport(playerLocations.get(entry.getKey()));
                        entry.getKey().getInventory().setHeldItemSlot(playerSlots.get(entry.getKey()));
                    } catch (Exception ignored) { }
                }

                for (Block block : blocks) {
                    block.setType(Material.AIR, true);
                }

            }, 5*20);

        }, 5*20);

    }

    @EventHandler
    public void onPlace(PlayerBucketEmptyEvent event) {
        if (!this.enabled) return;
        if (event.getBlock().getLocation().getWorld() == WorldManager.getInstance().getChallengesWorld()) blocks.add(event.getBlock());
    }

    @Override
    public void onEnable(ChallengeEditEvent event) {
        value = 5;
        nextActionInSeconds = getRandomSeconds();
    }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    @Override
    public @NotNull ItemStack getItem() {
        return new ItemBuilder(Material.WATER_BUCKET, ItemTranslation.WATER_MLG).build();
    }

    @Override
    public void onValueChange(ChallengeEditEvent event) {
        this.nextActionInSeconds = getRandomSeconds();
    }

    private final Random secondsRandom = new Random();

    private int getRandomSeconds() {
        int max = Utils.getRandomSecondsUp(value*60);
        int min = Utils.getRandomSecondsDown(value*60);
        return secondsRandom.nextInt(max - min) + min;
    }

}
