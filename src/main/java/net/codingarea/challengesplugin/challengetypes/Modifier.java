package net.codingarea.challengesplugin.challengetypes;

import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import net.codingarea.challengesplugin.utils.animation.AnimationSound;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public abstract class Modifier extends AbstractChallenge {

    protected AnimationSound sound = AnimationSound.STANDARD_SOUND;

    protected int value = 1;
    protected int maxValue = 2;
    protected int minValue = 1; // minValue should be 1 or higher!

    public Modifier(MenuType menu) {
        super(menu);
    }

    public Modifier(MenuType menu, int maxValue) {
        super(menu);
        this.maxValue = maxValue;
    }

    public Modifier(MenuType menu, int maxValue, int minValue) {
        this(menu, maxValue);
        this.minValue = minValue;
    }
    public Modifier(MenuType menu, int maxValue, int minValue, int defaultValue) {
        this(menu, maxValue, minValue);
        this.value = defaultValue;
    }

    public abstract void onMenuClick(ChallengeEditEvent event);

    @Override
    public void setValues(int value) {
        if (value > maxValue) value = maxValue;
        if (value < minValue) value = minValue;
        this.value = value;
        onMenuClick(new ChallengeEditEvent(null, null, null));
    }

    @Override
    public int toValue() {
        return value;
    }

    @Override
    public void handleClick(ChallengeEditEvent event) {
        if (sound != null) sound.play(event.getPlayer());
        if (event.wasRightClick()) {
            if ((value - 1) >= minValue) {
                value--;
            } else {
                value = maxValue;
            }
        } else {
            if (!((value + 1) > maxValue)) {
                value++;
            } else {
                value = minValue;
            }
        }
        onMenuClick(event);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public @NotNull ItemStack getActivationItem() {
        return new ItemBuilder(Material.STONE_BUTTON, "§8" + value).setAmount(value).build();
    }
}
