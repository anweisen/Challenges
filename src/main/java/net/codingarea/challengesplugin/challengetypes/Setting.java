package net.codingarea.challengesplugin.challengetypes;

import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.utils.AnimationUtil.AnimationSound;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public abstract class Setting extends GeneralChallenge {

	protected AnimationSound activationSound = AnimationSound.ON_SOUND;
	protected AnimationSound deactivationSound = AnimationSound.OFF_SOUND;

	protected boolean enabled;

	public abstract void onEnable(ChallengeEditEvent event);
	public abstract void onDisable(ChallengeEditEvent event);

	@Override
	public void setValues(int value) {
		enabled = value == 1;
	}

	@Override
	public int toValue() {
		return enabled ? 1 : 0;
	}

	@Override
	public void handleClick(ChallengeEditEvent event) {
		if (enabled) {
			enabled = false;
			if (deactivationSound != null) deactivationSound.play(event.getPlayer());
			onDisable(event);
		} else {
			enabled = true;
			if (activationSound != null) activationSound.play(event.getPlayer());
			onEnable(event);
		}
	}

	@Override
	public ItemStack getActivationItem() {
		return Challenges.getInstance().getItemManager().getActivationItem(enabled);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
