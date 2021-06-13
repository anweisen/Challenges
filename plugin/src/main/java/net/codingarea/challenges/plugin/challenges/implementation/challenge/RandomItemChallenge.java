package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.common.annotations.Since;
import net.anweisen.utilities.common.collection.IRandom;
import net.codingarea.challenges.plugin.challenges.type.abstraction.TimedChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class RandomItemChallenge extends TimedChallenge {

	private final IRandom random = IRandom.create();

	public RandomItemChallenge() {
		super(MenuType.CHALLENGES, 1,60, 30, false);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.BEACON, Message.forName("item-random-item-challenge"));
	}

	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return Message.forName("item-time-seconds-description").asArray(getValue());
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChallengeSecondsValueChangeTitle(this, getValue());
	}

	@Override
	protected int getSecondsUntilNextActivation() {
		return getValue();
	}

	@Override
	protected void onTimeActivation() {
		restartTimer();

		Material[] materials = Arrays.stream(Material.values())
				.filter(Material::isItem)
				.toArray(Material[]::new);

		broadcastFiltered(player -> InventoryUtils.giveItem(player.getInventory(), player.getLocation(), new ItemStack(random.choose(materials))));

	}

}
