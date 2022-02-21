package net.codingarea.challenges.plugin.spigot.listener;

import net.anweisen.utilities.bukkit.utils.menu.MenuPosition;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.custom.InfoMenuGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.custom.InfoMenuGenerator.InfoMenuPosition;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class CustomChallengeNamingListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent event) {

        MenuPosition position = MenuPosition.get(event.getPlayer());

        if (position instanceof InfoMenuPosition) {

            InfoMenuPosition infoMenuGenerator = (InfoMenuPosition) position;
            InfoMenuGenerator generator = infoMenuGenerator.getGenerator();

            if (generator.isInNaming()) {
                event.setCancelled(true);

                if (event.getMessage().length() > 50) {
                    Message.forName("custom-name-max_length").send(event.getPlayer(), Prefix.CUSTOM);
                    return;
                }

                generator.setInNaming(false);

                Bukkit.getScheduler().runTask(Challenges.getInstance(), () -> {
                    generator.setName(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
                    generator.open(event.getPlayer(), 0);
                });

            }
        }
    }
}