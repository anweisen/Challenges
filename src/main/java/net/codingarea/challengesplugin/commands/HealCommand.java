package net.codingarea.challengesplugin.commands;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.Translation;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        Player[] players = {player};

        if (args.length > 0) {
            if (args[0].equals("@a")) {
                players = (Player[]) Bukkit.getOnlinePlayers().toArray();
            } else {
                player = Bukkit.getPlayer(args[0]);
                if (player == null) {
                    return true;
                }
                players = new Player[]{player};

            }
        }

        for (Player currentPlayer : players) {
            currentPlayer.sendMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.HEALED.get());
            currentPlayer.setHealth(currentPlayer.getMaxHealth());

        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return null;
        } else if (args.length > 1) {
            return new ArrayList<>();
        }

        return null;
    }
}
