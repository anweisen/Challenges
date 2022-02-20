package net.codingarea.challenges.plugin.management.menu.generator.implementation.custom;

import java.util.List;
import javax.annotation.Nonnull;
import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.bukkit.utils.menu.MenuClickInfo;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.CustomChallenge;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.management.menu.InventoryTitleManager;
import net.codingarea.challenges.plugin.management.menu.generator.ChallengeMenuGenerator;
import net.codingarea.challenges.plugin.management.menu.info.ChallengeMenuClickInfo;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 *
 */
public class MainMenuGenerator extends ChallengeMenuGenerator {

	public static final int[] SLOTS = { 10, 11, 12, 13, 14, 15, 16 };
	public static final int[] NAVIGATION_SLOTS = { 36, 44 };
	public static final int SIZE = 5*9;
	public static final int VIEW_SLOT = 21;
	public static final int CREATE_SLOT = 23;

	public MainMenuGenerator() {
		super(1);
	}

	@Override
	protected String getTitle(int page) {
		return page != 0 ? super.getTitle(page - 1) : InventoryTitleManager.getTitle(getMenuType(), "Menu");
	}

	@Override
	public void executeClickAction(@Nonnull IChallenge challenge, @Nonnull MenuClickInfo info, int itemIndex) {
		if (itemIndex == 0 || itemIndex == 2) {
			challenge.handleClick(new ChallengeMenuClickInfo(info, itemIndex == 0));
		} else if (challenge instanceof CustomChallenge) {
			InfoMenuGenerator infoMenuGenerator = new InfoMenuGenerator((CustomChallenge) challenge);
			infoMenuGenerator.open(info.getPlayer(), 0);
			SoundSample.CLICK.play(info.getPlayer());
		}
	}

	@Override
	public void generatePage(@Nonnull Inventory inventory, int page) {
		if (page == 0) {
			inventory.setItem(VIEW_SLOT, new ItemBuilder(Material.BOOK, Message.forName("custom-main-view-challenges")).build());
			inventory.setItem(CREATE_SLOT, new ItemBuilder(Material.WRITABLE_BOOK, Message.forName("custom-main-create-challenge")).build());
		}
	}

	@Override
	public void onPreChallengePageClicking(@Nonnull MenuClickInfo clickInfo, int page) {
		if (clickInfo.getSlot() == VIEW_SLOT) {
			if (Challenges.getInstance().getCustomChallengesLoader().getCustomChallenges().size() == 0) {
				Message.forName("custom-not-loaded").send(clickInfo.getPlayer(), Prefix.CUSTOM);
				return;
			}
			open(clickInfo.getPlayer(), 1);
			SoundSample.PLOP.play(clickInfo.getPlayer());
		} else if (clickInfo.getSlot() == CREATE_SLOT) {
			if (Challenges.getInstance().getCustomChallengesLoader().getCustomChallenges().size() > 100) {
				Message.forName("custom-limit").send(clickInfo.getPlayer(), Prefix.CUSTOM);
				SoundSample.BASS_OFF.play(clickInfo.getPlayer());
				return;
			}
			new InfoMenuGenerator().open(clickInfo.getPlayer(), 0);
			SoundSample.PLING.play(clickInfo.getPlayer());
		}
	}

	@Override
	public void setSettingsItems(@Nonnull Inventory inventory, @Nonnull IChallenge challenge, int topSlot) {

		ItemBuilder displayItem = getDisplayItemBuilder(challenge);
		if (challenge instanceof CustomChallenge) {
			CustomChallenge customChallenge = (CustomChallenge) challenge;

			// ADDING CONDITION INFO
			if (customChallenge.getCondition() != null) {
				displayItem.appendLore(" ");
				List<String> conditionDisplay = InfoMenuGenerator
						.getSubSettingsDisplay(customChallenge.getCondition().getSubSettingsBuilder(),
								customChallenge.getSubConditions());

				String conditionName = Message.forName(customChallenge.getCondition().getMessage()).asItemDescription()
						.getName();
				displayItem.appendLore(Message.forName("custom-info-condition").asString() + " " + conditionName);
				displayItem.appendLore(conditionDisplay);
			}

			// ADDING ACTION INFO
			if (customChallenge.getAction() != null) {
				displayItem.appendLore(" ");
				List<String> actionDisplay = InfoMenuGenerator
						.getSubSettingsDisplay(customChallenge.getAction().getSubSettingsBuilder(),
								customChallenge.getSubActions());

				String actionName = Message.forName(customChallenge.getAction().getMessage()).asItemDescription()
						.getName();
				displayItem.appendLore(Message.forName("custom-info-action").asString() + " " + actionName);
				displayItem.appendLore(actionDisplay);
			}
		}

		inventory.setItem(getSlots()[topSlot], displayItem.setName(Message.forName("item-prefix").asString() + displayItem.getName()).build());
		inventory.setItem(getSlots()[topSlot] + 9, DefaultItem.customize().build());
		inventory.setItem(getSlots()[topSlot] + 18, getSettingsItem(challenge));
	}

	@Override
	public int[] getSlots() {
		return SLOTS;
	}

	@Override
	public int getSize() {
		return SIZE;
	}

	@Override
	public int[] getNavigationSlots(int page) {
		return page == 0 ? new int[]{NAVIGATION_SLOTS[0]} : NAVIGATION_SLOTS;
	}

}