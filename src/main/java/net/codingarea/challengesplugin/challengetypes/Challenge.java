package net.codingarea.challengesplugin.challengetypes;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.utils.AnimationUtil.AnimationSound;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public abstract class Challenge extends GeneralChallenge {

    protected AnimationSound activationSound = AnimationSound.ON_SOUND;
    protected AnimationSound deactivationSound = AnimationSound.OFF_SOUND;

    protected boolean enabled;

    protected int nextActionInSeconds = -1;

    public abstract void onEnable(ChallengeEditEvent event);
    public abstract void onDisable(ChallengeEditEvent event);

    public abstract void onTimeActivation();

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

    /**
     * @return returns if onTimeActivation() was executed
     */
    public boolean handleSecond() {

        nextActionInSeconds--;

        if (!(nextActionInSeconds <= 0)) return false;

        onTimeActivation();
        return true;

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

    public int getNextActionInSeconds() {
        return nextActionInSeconds;
    }

    public void setNextActionInSeconds(int nextActionInSeconds) {
        this.nextActionInSeconds = nextActionInSeconds;
    }

    /**
     * @return returns if onTimeActivation() was executed
     */
    public final boolean handleOnSecond() {

        if (nextActionInSeconds == -1) return false;

        return handleSecond();

    }

}
