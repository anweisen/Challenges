package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.animation.AnimationSound;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-12-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class UpCommand extends Setting implements CommandExecutor {

	public UpCommand() {
		super(MenuType.SETTINGS);
	}

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.MAGENTA_GLAZED_TERRACOTTA, ItemTranslation.UP_COMMAND).build();
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		if (!(sender instanceof Player)) return true;

		Player player = (Player) sender;

		if (!enabled ||!Challenges.timerIsStarted()) {
			sender.sendMessage(Prefix.CHALLENGES + Translation.FEATURE_DISABLED.get());
			AnimationSound.OFF_SOUND.play(player);
			return true;
		}

		if (player.getLocation().getWorld().getEnvironment() == Environment.NETHER) {
			player.teleport(Utils.getHighestBlock(Bukkit.getWorlds().get(0).getSpawnLocation()));
		} else {
			player.teleport(Utils.getHighestBlock(player.getLocation()));
		}

		player.sendMessage(Prefix.CHALLENGES + Translation.UP_COMMAND_TELEPORT.get());
		AnimationSound.TELEPORT_SOUND.play(player);

		return true;
	}

}
