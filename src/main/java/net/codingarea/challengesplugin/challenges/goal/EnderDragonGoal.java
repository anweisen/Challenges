package net.codingarea.challengesplugin.challenges.goal;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Goal;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.events.ChallengeEndCause;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class EnderDragonGoal extends Goal implements Listener {

    public EnderDragonGoal() {
        super(MenuType.GOALS, true);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.DRAGON_EGG, ItemTranslation.Kill_ENDERDRAGON).getItem();
    }

    @Override
    public void onEnable(ChallengeEditEvent event) { }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    @Override
    public List<Player> getWinners() {
        return null;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        if (!isCurrentGoal || !Challenges.timerIsStarted()) return;
        if (!(event.getEntity() instanceof EnderDragon)) return;

        Challenges.getInstance().getServerManager().handleChallengeEnd(event.getEntity().getKiller(), ChallengeEndCause.PLAYER_CHALLENGE_GOAL_REACHED, null);

    }

}
