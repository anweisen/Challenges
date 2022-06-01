package net.codingarea.challenges.plugin.challenges.implementation.goal;

import net.anweisen.utilities.common.annotations.Since;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.challenges.type.abstraction.ForceBattleGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.0
 */
@Since("2.2.0")
public class ForceAdvancementBattleGoal extends ForceBattleGoal<Advancement> {

    public ForceAdvancementBattleGoal() {
        super(MenuType.GOAL, Message.forName("menu-force-advancement-battle-goal-settings"));
    }

    private void resetAdvancementProgress(Player player, Advancement advancement) {
        AdvancementProgress progress = player.getAdvancementProgress(advancement);
        progress.getAwardedCriteria().forEach(progress::revokeCriteria);
    }

    @NotNull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.EXPERIENCE_BOTTLE, Message.forName("item-force-advancement-battle-goal"));
    }

    @Override
    protected Advancement[] getTargetsPossibleToFind() {
        List<Advancement> advancements = new ArrayList<>();
        Bukkit.getServer().advancementIterator().forEachRemaining(advancement -> {
            String string = advancement.getKey().toString();
            if (!string.contains("minecraft:recipes/") && !string.endsWith("root")) {
                advancements.add(advancement);
            }
        });
        return advancements.toArray(new Advancement[0]);
    }

    @Override
    public void handleDisplayStandUpdate(@NotNull Player player, @NotNull ArmorStand armorStand) {
        Advancement advancement = currentTarget.get(player.getUniqueId());
        if (advancement == null) {
            armorStand.setCustomNameVisible(false);
        } else {
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(getTargetName(advancement));
        }
    }

    @Override
    public Advancement getTargetFromDocument(Document document, String key) {
        String advancementKey = document.getString(key);
        NamespacedKey namespacedKey = NamespacedKey.minecraft(advancementKey);
        return Bukkit.getAdvancement(namespacedKey);
    }

    @Override
    public List<Advancement> getListFromDocument(Document document, String key) {
        List<String> advancementKeys = document.getStringList(key);
        List<Advancement> advancements = new ArrayList<>();
        for (String advancementKey : advancementKeys) {
            advancements.add(Bukkit.getAdvancement(NamespacedKey.minecraft(advancementKey)));
        }
        return advancements;
    }

    @Override
    protected Message getNewTargetMessage() {
        return Message.forName("force-advancement-battle-new-advancement");
    }

    @Override
    protected Message getTargetFoundMessage() {
        return Message.forName("force-advancement-battle-completed");
    }

    @Override
    public Object getTargetMessageReplacement(Advancement target) {
        String replace = target.getKey().getKey().replace("/", ".");
        String corrected = correctAdvancementKeys(replace);
        TranslatableComponent component = new TranslatableComponent("advancements." + corrected + ".title");
        TranslatableComponent translatableComponent = new TranslatableComponent("advancements." + corrected + ".description");
        translatableComponent.setColor(ChatColor.GREEN);
        HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(translatableComponent).create());
        component.setHoverEvent(event);
        return component;
    }

    @Override
    public String getTargetName(Advancement target) {
        String replace = target.getKey().getKey().replace("/", ".");
        return new TranslatableComponent("advancements." + correctAdvancementKeys(replace) + ".title").toPlainText();
    }

    private String correctAdvancementKeys(String s) {
        return s.replace("bred_all_animals", "breed_all_animals").replace("obtain_netherite_hoe", "netherite_hoe"); // mc sucks
    }

    @Override
    protected Message getLeaderboardTitleMessage() {
        return Message.forName("force-advancement-battle-leaderboard");
    }

    @Override
    public boolean isSmall() {
        return false;
    }

    @Override
    public void setRandomTarget(Player player) {
        super.setRandomTarget(player);
        resetAdvancementProgress(player, currentTarget.get(player.getUniqueId()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        if (!shouldExecuteEffect()) return;
        if (ignorePlayer(event.getPlayer())) return;
        Player player = event.getPlayer();
        if(event.getAdvancement() == currentTarget.get(player.getUniqueId())) {
            handleTargetFound(player);
        }
    }

}
