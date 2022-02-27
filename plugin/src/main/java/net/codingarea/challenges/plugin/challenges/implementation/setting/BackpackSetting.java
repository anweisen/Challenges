package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.abstraction.SettingModifier;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeConfigHelper;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.management.menu.InventoryTitleManager;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.bukkit.command.PlayerCommand;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class BackpackSetting extends SettingModifier implements PlayerCommand {

	public static final int SHARED = 1,
							PLAYER = 2;

	private final int size;
	private final Map<UUID, Inventory> backpacks = new HashMap<>();
	private final Inventory sharedBackpack;

	public BackpackSetting() {
		super(MenuType.SETTINGS, 1, 2, SHARED);
		size = Math.max(Math.min(ChallengeConfigHelper.getSettingsDocument().getInt("backpack-size") * 9, 6*9), 9);
		sharedBackpack = createInventory("§5Team Backpack");
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.CHEST, Message.forName("item-backpack-setting"));
	}

	@Nonnull
	@Override
	public ItemBuilder createSettingsItem() {
		if (getValue() == SHARED)
			return DefaultItem.create(Material.ENDER_CHEST, "§5Team");
		return DefaultItem.create(Material.PLAYER_HEAD, "§6Player");
	}


	@Override
	public void playValueChangeTitle() {
		switch (getValue()) {
			case SHARED:
				ChallengeHelper.playChangeChallengeValueTitle(this, "§5Team");
				break;
			case PLAYER:
				ChallengeHelper.playChangeChallengeValueTitle(this, "§6Player");
				break;
			default:
				ChallengeHelper.playToggleChallengeTitle(this, false);
		}
	}

	@Override
	public void onCommand(@Nonnull Player player, @Nonnull String[] args) {
		if (ChallengeAPI.isPaused()) {
			Message.forName("timer-not-started").send(player, Prefix.BACKPACK);
			SoundSample.BASS_OFF.play(player);
			return;
		}

		if (!isEnabled()) {
			Message.forName("backpacks-disabled").send(player, Prefix.BACKPACK);
			SoundSample.BASS_OFF.play(player);
			return;
		}

		if (getValue() == SHARED || getValue() == PLAYER) {
			Message.forName("backpack-opened").send(player, Prefix.BACKPACK, getValue() == SHARED ? "§5Team Backpack" : "§6Player Backpack");
			player.openInventory(getCurrentBackpack(player));
			SoundSample.OPEN.play(player);
		} else {
			Message.forName("backpacks-disabled").send(player, Prefix.BACKPACK);
			SoundSample.BASS_OFF.play(player);
		}
	}

	@Override
	public void loadGameState(@Nonnull Document document) {
		super.loadGameState(document);

		Document shared = document.getDocument("shared");
		load(shared, sharedBackpack);

		Document players = document.getDocument("players");
		for (String key : players.keys()) {
			Document backpack = players.getDocument(key);
			load(backpack, backpacks.computeIfAbsent(UUID.fromString(key), k -> createInventory("§6Backpack")));
		}

	}

	protected void load(@Nonnull Document document, @Nonnull Inventory inventory) {
		for (String key : document.keys()) {
			try {
				int index = Integer.parseInt(key);
				ItemStack item = document.getSerializable(key, ItemStack.class);
				inventory.setItem(index, item);
			} catch (Exception ex) {
			}
		}
	}

	@Override
	public void writeGameState(@Nonnull Document document) {
		super.writeGameState(document);

		Document shared = document.getDocument("shared");
		write(shared, sharedBackpack);

		Document players = document.getDocument("players");
		backpacks.forEach((uuid, inventory) -> {
			Document backpack = players.getDocument(uuid.toString());
			write(backpack, inventory);
		});
	}

	protected void write(@Nonnull Document document, @Nonnull Inventory inventory) {
		for (int i = 0; i < inventory.getSize(); i++) {
			ItemStack item = inventory.getItem(i);
			document.set(String.valueOf(i), item);
		}
	}

	@Nonnull
	protected Inventory createInventory(@Nonnull String title) {
		return Bukkit.createInventory(null, size, InventoryTitleManager.getTitle(title));
	}

	@Nonnull
	protected Inventory getCurrentBackpack(@Nonnull Player player) {
		return (getValue() == SHARED) ? sharedBackpack : backpacks.computeIfAbsent(player.getUniqueId(), key -> createInventory("§6Backpack"));
	}

}
