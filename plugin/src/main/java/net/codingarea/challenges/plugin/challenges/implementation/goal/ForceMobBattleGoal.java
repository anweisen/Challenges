package net.codingarea.challenges.plugin.challenges.implementation.goal;

import net.anweisen.utilities.common.annotations.Since;
import net.anweisen.utilities.common.config.Document;
import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.challenges.type.abstraction.ForceBattleGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.0
 */
@Since("2.2.0")
public class ForceMobBattleGoal extends ForceBattleGoal<EntityType> {

    public ForceMobBattleGoal() {
        super(MenuType.GOAL, Message.forName("menu-force-mob-battle-goal-settings"));
    }

    @Override
    protected EntityType[] getTargetsPossibleToFind() {
        List<EntityType> entityTypes = new ArrayList<>(Arrays.asList(EntityType.values()));
        entityTypes.removeIf(entityType -> !entityType.isSpawnable());
        entityTypes.removeIf(entityType -> !entityType.isAlive());
        Utils.removeEnums(entityTypes, "ILLUSIONER", "ARMOR_STAND", "ZOMBIE_HORSE", "GIANT");

        return entityTypes.toArray(new EntityType[0]);
    }

    @Override
    protected Message getLeaderboardTitleMessage() {
        return Message.forName("force-mob-battle-leaderboard");
    }

    @NotNull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.BOW, Message.forName("item-force-mob-battle-goal"));
    }

    @Override
    public void handleDisplayStandUpdate(@NotNull Player player, @NotNull ArmorStand armorStand) {
        EntityType mob = currentTarget.get(player.getUniqueId());
        if (mob == null) {
            armorStand.setCustomNameVisible(false);
        } else {
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(getTargetName(mob));
        }
    }

    @Override
    public EntityType getTargetFromDocument(Document document, String key) {
        return document.getEnum(key, EntityType.class);
    }

    @Override
    public List<EntityType> getListFromDocument(Document document, String key) {
        return document.getEnumList(key, EntityType.class);
    }

    @Override
    protected Message getNewTargetMessage() {
        return Message.forName("force-mob-battle-killed");
    }

    @Override
    protected Message getTargetFoundMessage() {
        return Message.forName("force-mob-battle-new-mob");
    }

    @Override
    public boolean isSmall() {
        return false;
    }

    @Override
    public String getTargetName(EntityType target) {
        return StringUtils.getEnumName(target);
    }

    @Override
    public Object getTargetMessageReplacement(EntityType target) {
        return target;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onKill(@Nonnull EntityDeathEvent event) {
        if(!shouldExecuteEffect()) return;
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        if (killer == null) return;
        if(ignorePlayer(killer)) return;
        if (currentTarget.get(killer.getUniqueId()) == null) return;
        if(entity.getType() == currentTarget.get(killer.getUniqueId())) {
            handleTargetFound(killer);
        }
    }

}
