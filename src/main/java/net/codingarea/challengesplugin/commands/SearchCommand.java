package net.codingarea.challengesplugin.commands;

import net.codingarea.challengesplugin.challenges.challenges.randomizer.BlockRandomizerChallenge;
import net.codingarea.challengesplugin.manager.lang.LanguageManager;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map.Entry;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-31-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class SearchCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length != 1) {
            sender.sendMessage(Prefix.CHALLENGES + LanguageManager.syntax("/search <Search>"));
            return true;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i]);
            if (args.length > (i + 1)) builder.append(" ");
        }

        String search = formatString(builder.toString());

        Material material;

        try {
            material = Material.valueOf(search);
        } catch (Exception ignored) {
            sender.sendMessage(Prefix.CHALLENGES + "§7No block found matching this name");
            return true;
        }

        for (Entry<Material, List<Material>> entry : BlockRandomizerChallenge.getMaterials().entrySet()) {
            try {
                if (entry.getValue().contains(material)) {
                    sender.sendMessage(Prefix.CHALLENGES + "§7Blockrandomizer: §a" + Utils.getEnumName(entry.getKey().name()));
                }
            } catch (Exception ignored) { }
        }

        return true;
    }

    private String formatString(String string) {
        return string.toUpperCase().replace(" ", "_");
    }

}
