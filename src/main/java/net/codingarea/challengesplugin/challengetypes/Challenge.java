package net.codingarea.challengesplugin.challengetypes;

import net.codingarea.challengesplugin.manager.ItemManager;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.animation.AnimationSound;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public abstract class Challenge extends AbstractChallenge {

    protected AnimationSound activationSound = AnimationSound.ON_SOUND;
    protected AnimationSound deactivationSound = AnimationSound.OFF_SOUND;

    protected boolean enabled;

    protected int nextActionInSeconds = -1;

    public Challenge(MenuType menu) {
        super(menu);
    }

    public abstract void onEnable(ChallengeEditEvent event);
    public abstract void onDisable(ChallengeEditEvent event);
    public abstract void onTimeActivation();

    @Override
    public void setValues(int value) {
        boolean before = enabled;
        enabled = value != 0;
        if (enabled && !before) onEnable(new ChallengeEditEvent(null, null, null));
        else if (!enabled && before) onDisable(new ChallengeEditEvent(null, null, null));
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
    public final void handleTimerSecond() {
        nextActionInSeconds--;
        if (nextActionInSeconds == 0) onTimeActivation();
    }

    @Override
    public ItemStack getActivationItem() {
        return ItemManager.getActivationItem(enabled);
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public final int getNextActionInSeconds() {
        return nextActionInSeconds;
    }

    public final void setNextActionInSeconds(int nextActionInSeconds) {
        this.nextActionInSeconds = nextActionInSeconds;
    }


}
