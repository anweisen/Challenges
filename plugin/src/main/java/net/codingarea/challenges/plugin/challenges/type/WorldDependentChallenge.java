package net.codingarea.challenges.plugin.challenges.type;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.server.WorldManager.WorldSettings;
import net.codingarea.challenges.plugin.utils.bukkit.container.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class WorldDependentChallenge extends TimedChallenge {

	private final Map<UUID, PlayerData> before = new HashMap<>();

	private boolean inExtraWorld;
	private BiConsumer<Player, Integer> lastTeleport;
	private int teleportIndex;

	public WorldDependentChallenge(@Nonnull MenuType menu) {
		super(menu);
	}

	public WorldDependentChallenge(@Nonnull MenuType menu, int max) {
		super(menu, max);
	}

	public WorldDependentChallenge(@Nonnull MenuType menu, int min, int max) {
		super(menu, min, max);
	}

	public WorldDependentChallenge(@Nonnull MenuType menu, int min, int max, int defaultValue) {
		super(menu, min, max, defaultValue);
	}

	@Override
	protected boolean getTimerCondition() {
		return inExtraWorld || !Challenges.getInstance().getWorldManager().isWorldInUse();
	}

	protected void teleportToWorld(boolean allowJoinCatchUp, @Nonnull BiConsumer<Player, Integer> action) {
		Challenges.getInstance().getWorldManager().setWorldIsInUse(inExtraWorld = true);
		lastTeleport = allowJoinCatchUp ? action : null;

		teleportIndex = 0;
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.getInventory().clear();
			before.put(player.getUniqueId(), new PlayerData(player));
			action.accept(player, teleportIndex++);
		}
	}

	protected void teleportBack() {
		Challenges.getInstance().getWorldManager().setWorldIsInUse(inExtraWorld = false);
		lastTeleport = null;
		teleportIndex = 0;

		List<UUID> remove = new ArrayList<>();
		before.forEach((uuid, data) -> {
			Player player = Bukkit.getPlayer(uuid);
			if (player == null) return;

			player.setNoDamageTicks(20);
			data.apply(player);

			remove.add(uuid);
		});
		remove.forEach(before::remove);
	}


	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onJoin(@Nonnull PlayerJoinEvent event) {
		if (!isInExtraWorld()) return;
		if (lastTeleport == null) return;

		UUID uuid = event.getPlayer().getUniqueId();
		PlayerData data = before.get(uuid);
		if (data != null) return;

		data = new PlayerData(event.getPlayer());
		before.put(uuid, data);

		lastTeleport.accept(event.getPlayer(), teleportIndex++);
	}

	@Nonnull
	protected final World getExtraWorld() {
		return Challenges.getInstance().getWorldManager().getExtraWorld();
	}

	@Nonnull
	protected final WorldSettings getExtraWorldSettings() {
		return Challenges.getInstance().getWorldManager().getSettings();
	}

	public final boolean isInExtraWorld() {
		return inExtraWorld;
	}

}
