package net.codingarea.challenges.plugin.management.menu.generator;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.bukkit.utils.logging.Logger;
import net.anweisen.utilities.bukkit.utils.menu.MenuClickInfo;
import net.anweisen.utilities.bukkit.utils.menu.MenuPosition;
import net.anweisen.utilities.common.version.Version;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuManager;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public abstract class ChallengeMenuGenerator extends MultiPageMenuGenerator {

	protected final List<IChallenge> challenges = new LinkedList<>();

	private final boolean newSuffix;
	private final int startPage;
	private final Consumer<Player> onLeaveClick;

	public ChallengeMenuGenerator(int startPage, Consumer<Player> onLeaveClick) {
		newSuffix = Challenges.getInstance().getConfigDocument().getBoolean("new-suffix");
		this.startPage = startPage;
		this.onLeaveClick = onLeaveClick;
	}

	public ChallengeMenuGenerator(int startPage) {
		this(startPage, player -> Challenges.getInstance().getMenuManager().openGUIInstantly(player));
	}

	public ChallengeMenuGenerator() {
		this(0);
	}

	@Override
	public MenuPosition getMenuPosition(int page) {
		return new ChallengeMenuPosition(page);
	}

	@Override
	public int getPagesCount() {
		return (int) (Math.ceil((double) challenges.size() / getSlots().length) + startPage);
	}

	@Override
	public void generatePage(@Nonnull Inventory inventory, int page) {

	}

	@Override
	public void generateInventories() {
		super.generateInventories();

		for (IChallenge challenge : challenges) {
			updateItem(challenge);
		}

	}

	public void updateItem(IChallenge challenge) {

		int index = challenges.indexOf(challenge);
		if (index == -1) return; // Challenge not registered or menus not loaded

		int page = (index / getSlots().length);
		if (page >= getInventories().size()) return; // This should never happen

		int slot = index - getSlots().length * page;

		Inventory inventory = getInventories().get(page + startPage);
		setChallengeItems(inventory, challenge, slot);
	}

	public void setChallengeItems(@Nonnull Inventory inventory, @Nonnull IChallenge challenge, int topSlot) {
		inventory.setItem(getSlots()[topSlot], getDisplayItem(challenge));
		inventory.setItem(getSlots()[topSlot] + 9, getSettingsItem(challenge));
	}

	public void resetChallengeCache() {
		this.challenges.clear();
	}

	public void addChallengeToCache(@Nonnull IChallenge challenge) {
		if (isNew(challenge) && Challenges.getInstance().getMenuManager().isDisplayNewInFront()) {
			challenges.add(countNewChallenges(), challenge);
		} else {
			challenges.add(challenge);
		}
	}

	protected ItemStack getDisplayItem(@Nonnull IChallenge challenge) {
		try {
			ItemBuilder item = new ItemBuilder(challenge.getDisplayItem()).hideAttributes();
			if (newSuffix && isNew(challenge)) {
				return item.appendName(" " + Message.forName("new-challenge")).build();
			} else {
				return item.build();
			}
		} catch (Exception ex) {
			Logger.error("Error while generating challenge display item for challenge {}", challenge.getClass().getSimpleName(), ex);
			return new ItemBuilder().build();
		}
	}

	protected ItemStack getSettingsItem(@Nonnull IChallenge challenge) {
		try {
			ItemBuilder item = new ItemBuilder(challenge.getSettingsItem()).hideAttributes();
			return item.build();
		} catch (Exception ex) {
			Logger.error("Error while generating challenge settings item for challenge {}", challenge.getClass().getSimpleName(), ex);
			return new ItemBuilder().build();
		}
	}

	protected boolean isNew(@Nonnull IChallenge challenge) {
		Version version = Challenges.getInstance().getVersion();
		Version since = Version.getAnnotatedSince(challenge);
		return since.isNewerOrEqualThan(version);
	}

	protected int countNewChallenges() {
		return (int) challenges.stream().filter(this::isNew).count();
	}

	public abstract int[] getSlots();

	public abstract void executeClickAction(@Nonnull IChallenge challenge, @Nonnull MenuClickInfo info, int itemIndex);

	public void onPreChallengePageClicking(@Nonnull MenuClickInfo clickInfo, @Nonnegative int page) {

	}


	private class ChallengeMenuPosition implements MenuPosition {

		private final int page;

		public ChallengeMenuPosition(int page) {
			this.page = page;
		}

		@Override
		public void handleClick(@Nonnull MenuClickInfo info) {

			if (info.getSlot() == getNavigationSlots(page)[0]) {
				SoundSample.CLICK.play(info.getPlayer());
				if (page <= startPage || info.isShiftClick()) {
					if (page == 0) {
						onLeaveClick.accept(info.getPlayer());
					} else {
						open(info.getPlayer(), 0);
					}
				} else {
					open(info.getPlayer(), page - 1);
				}
				return;
			} else if (page < startPage) {
				onPreChallengePageClicking(info, page);

				return;
			} else if (info.getSlot() == getNavigationSlots(page)[1]) {
				SoundSample.CLICK.play(info.getPlayer());
				if (page < (inventories.size() - startPage))
					open(info.getPlayer(), page + 1);
				return;
			}

			int itemIndex = 0;
			int index = 0;
			for (int i : getSlots()) {
				if (i == info.getSlot()) break;
				if ((i + 9) == info.getSlot()) {
					itemIndex = 1;
					break;
				} else if ((i + 18) == info.getSlot()) {
					itemIndex = 2;
					break;
				}
				index++;
			}

			if (index == getSlots().length) { // No possible bound slot was clicked
				SoundSample.CLICK.play(info.getPlayer());
				return;
			}

			int offset = (page - startPage) * getSlots().length;
			index += offset;

			if (index >= challenges.size()) { // No bound slot was clicked
				SoundSample.CLICK.play(info.getPlayer());
				return;
			}

			IChallenge challenge = challenges.get(index);

			if (playNoPermissionsEffect(info.getPlayer())) return;

			try {
				executeClickAction(challenge, info, itemIndex);
				updateItem(challenge);
			} catch (Exception ex) {
				Logger.error("An exception occurred while handling click on {}", challenge.getClass().getName(), ex);
			}

		}

		private boolean playNoPermissionsEffect(@Nonnull Player player) {
			MenuManager menuManager = Challenges.getInstance().getMenuManager();
			if (!menuManager.permissionToManageGUI()) return false;
			if (mayManageSettings(player)) return false;
			menuManager.playNoPermissionsEffect(player);
			return true;
		}

		private boolean mayManageSettings(@Nonnull Player player) {
			return player.hasPermission("challenges.manage");
		}

	}

}