package net.codingarea.challengesplugin.commands;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-12-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class HealCommand  implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        List<Player> players = new ArrayList<>();
        if (args.length > 0) {
            if (args[0].equals("@a") || args[0].equals("*")) {
                players.addAll(Bukkit.getOnlinePlayers());
            } else {
                player = Bukkit.getPlayer(args[0]);
                if (player == null) {
                    sender.sendMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.PLAYER_NOT_FOUND.get().replace("%player%", args[0]));
                    return true;
                }
                players.add(player);
            }
        } else {
            players.add(player);
        }

        for (Player currentPlayer : players) {
            currentPlayer.sendMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.HEALED.get());
            currentPlayer.setHealth(currentPlayer.getMaxHealth());
            currentPlayer.setFoodLevel(20);
            currentPlayer.setFireTicks(0);
        }

        if (!players.contains(player)) {
            player.sendMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.HEALED_OTHERS.get().replace("%players%", players.size()+""));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length == 1) {
            ArrayList<String> suggestions = new ArrayList<>();
            for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
                suggestions.add(currentPlayer.getName());
            }
            suggestions.add("@a");
            return Utils.getMatchingSuggestions(args[0].toLowerCase(), suggestions);
        } else {
            return new ArrayList<>();
        }
    }
}
