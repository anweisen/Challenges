package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import java.util.List;
import java.util.Map;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengePlayerTargetAction;
import net.codingarea.challenges.plugin.challenges.type.helper.SubSettingsHelper;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.2
 */
public class ExecuteCommandAction extends AbstractChallengePlayerTargetAction {

	// Static because cannot be accessed before super has been called
	public static int maxCommandLength = Challenges.getInstance().getConfigDocument().getInt("custom-challenge-settings.max-command-length");
	private static List<String> commandsThatCanBeExecuted = Challenges.getInstance().getConfigDocument().getStringList("custom-challenge-settings.allowed-commands-to-execute");

	public ExecuteCommandAction(String name) {
		super(name, SubSettingsHelper.createEntityTargetSettingsBuilder(true, true).createTextInputChild("command", player -> {
			System.out.println(commandsThatCanBeExecuted);
			Message.forName("custom-command-info").send(player, Prefix.CUSTOM, "/" + String.join(" /", commandsThatCanBeExecuted));
		}, event -> {
			String cmd = event.getMessage().split(" ")[0].toLowerCase();

			if (!commandsThatCanBeExecuted.contains(cmd)) {
				Message.forName("custom-command-not-allowed").send(event.getPlayer(), Prefix.CUSTOM, cmd);
				return false;
			}

			if (event.getMessage().length() > maxCommandLength) {
				Message.forName("custom-chars-max_length").send(event.getPlayer(), Prefix.CUSTOM, maxCommandLength);
				return false;
			}
			return true;
		}));
		// Reload from config on reload
		maxCommandLength = Challenges.getInstance().getConfigDocument().getInt("custom-challenge-settings.max-command-length");
		commandsThatCanBeExecuted = Challenges.getInstance().getConfigDocument().getStringList("custom-challenge-settings.allowed-commands-to-execute");
	}

	@Override
	public void executeForPlayer(Player player, Map<String, String[]> subActions) {
		String fullCommand = subActions.get("command")[0];
		String command = fullCommand.split(" ")[0];

		PermissionAttachment permissionAttachment = player.addAttachment(Challenges.getInstance());
		permissionAttachment.setPermission("minecraft.command.fill", true);
		Bukkit.getServer().dispatchCommand(player, fullCommand);
		permissionAttachment.unsetPermission("minecraft.command.fill");
		player.removeAttachment(permissionAttachment);

	}

	@Override
	public Material getMaterial() {
		return Material.COMMAND_BLOCK;
	}
}
