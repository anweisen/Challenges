package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.spigot.events.PlayerPickupItemEvent;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.EntityUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class PickupItemLaunchChallenge extends SettingModifier {

    public PickupItemLaunchChallenge() {
        super(MenuType.CHALLENGES, 1, 5, 2);
    }

    @NotNull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.BOW, Message.forName("item-pickup-launch-challenge"));
    }

    @Nullable
    @Override
    protected String[] getSettingsDescription() {
        return Message.forName("item-pickup-launcher-description").asArray(getValue());
    }

    @Override
    public void playValueChangeTitle() {
        ChallengeHelper.playChangeChallengeValueTitle(this, Message.forName("subtitle-pickup-launcher-description").asString(getValue()));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerPickUpItem(@Nonnull PlayerPickupItemEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;

        Vector velocityToAdd = new Vector(0, getValue() / 2, 0);
        Vector newVelocity = EntityUtils.getSucceedingVelocity(event.getPlayer().getVelocity()).add(velocityToAdd);
        event.getPlayer().setVelocity(newVelocity);
    }

}
