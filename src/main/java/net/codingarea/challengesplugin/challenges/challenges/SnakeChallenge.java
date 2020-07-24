package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anweisen & Dominik
 * Challenges developed on 07-24-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class SnakeChallenge extends Setting implements Listener {

    private ArrayList<Block> blocks;

    public SnakeChallenge() {
        super(MenuType.CHALLENGES);
    }

    @Override
    public void onEnable(ChallengeEditEvent event) {
        if (blocks == null) blocks = new ArrayList<>();
    }

    @Override
    public void onDisable(ChallengeEditEvent event) {
        blocks = null;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!this.enabled || !Challenges.timerIsStarted()) return;
        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) return;
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;

        Block from = event.getFrom().clone().subtract(0,1,0).getBlock();
        Block to = event.getTo().clone().subtract(0,1,0).getBlock();

        if (from.getType().isSolid()) {
            from.setType(Utils.getTerracotta(getPlayersWool(event.getPlayer())));
            blocks.add(from);
        }

        if (blocks.contains(to)) {
            event.getPlayer().damage(event.getPlayer().getHealth());
            return;
        }


        if (to.getType().isSolid()) {
            to.setType(Material.BLACK_TERRACOTTA);
        }

    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.BLUE_TERRACOTTA, ItemTranslation.SNAKE).build();
    }

    public int getPlayersWool(Player player) {

        Iterator it = Bukkit.getOnlinePlayers().iterator();
        int i = 0;

        while (it.hasNext()) {
            i++;
            if (((Player)it.next()) == player) return i;

        }

        return 0;

    }

}