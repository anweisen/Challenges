package net.codingarea.challenges.plugin.management.cloud;

import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.commons.misc.WrappedException;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.management.scheduler.task.TimerTask;
import net.codingarea.challenges.plugin.management.scheduler.timer.TimerStatus;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class CloudSupportManager {

	private final boolean startNewService;
	private final boolean nameSupport;
	private final boolean resetToLobby;
	private final String type;

	private CloudSupport support;

	public CloudSupportManager() {
		Document config = Challenges.getInstance().getConfigDocument().getDocument("cloud-support");

		startNewService = config.getBoolean("start-new-service");
		nameSupport = config.getBoolean("name-rank-colors");
		resetToLobby = config.getBoolean("reset-to-lobby");
		type = config.getString("type", "none");
		Logger.debug("Detected cloud support type {}", type);

		if (type.equals("none")) return;

		loadSupport(type);

	}

	private void loadSupport(@Nonnull String name) {
		switch (name) {
			case "cloudnet":
				support = new CloudNetSupport();
				return;
		}
	}

	@Nonnull
	public String getColoredName(@Nonnull Player player) {
		if (support == null) throw new IllegalStateException("No support loaded! Check compatibility before use");

		try {
			return support.getColoredName(player);
		} catch (NoClassDefFoundError ex) {
			Logger.error("Unable to get name with cloud support '{}', missing dependencies", type);
			throw new WrappedException(ex);
		}
	}

	@Nonnull
	public String getColoredName(@Nonnull UUID uuid) {
		if (support == null) throw new IllegalStateException("No support loaded! Check compatibility before use");

		try {
			return support.getColoredName(uuid);
		} catch (NoClassDefFoundError ex) {
			Logger.error("Unable to get name with cloud support '{}', missing dependencies", type);
			throw new WrappedException(ex);
		}
	}

	public boolean hasNameFor(@Nonnull UUID uuid) {
		if (support == null) return false;

		try {
			return support.hasNameFor(uuid);
		} catch (NoClassDefFoundError ex) {
			Logger.error("Unable to check name with cloud support '{}', missing dependencies", type);
			return false;
		}
	}

	@TimerTask(status = TimerStatus.RUNNING, async = false)
	public void setIngame() {
		if (!startNewService || !ChallengeAPI.isFresh()) return;
		if (support == null) return;

		try {
			support.setIngame();
		} catch (NoClassDefFoundError ex) {
			Logger.error("Unable to set to ingame with cloud support '{}', missing dependencies", type);
		}
	}

	@TimerTask(status = TimerStatus.PAUSED, async = false)
	public void setLobby() {
		if (!resetToLobby || !startNewService) return;
		if (support == null) return;

		try {
			support.setLobby();
		} catch (NoClassDefFoundError ex) {
			Logger.error("Unable to set to lobby with cloud support '{}', missing dependencies", type);
		}
	}

	protected boolean isEnabled() {
		return support != null;
	}

	public boolean isNameSupport() {
		return isEnabled() && nameSupport;
	}

	public boolean isResetToLobby() {
		return isEnabled() && resetToLobby;
	}

	public boolean isStartNewService() {
		return isEnabled() && startNewService;
	}

}
