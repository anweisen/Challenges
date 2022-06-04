package net.codingarea.challenges.plugin.challenges.implementation.goal;

import net.anweisen.utilities.bukkit.utils.item.ItemUtils;
import net.anweisen.utilities.common.annotations.Since;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.challenges.type.abstraction.ForceBattleGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.policy.TimerPolicy;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.utils.bukkit.misc.BukkitStringUtils;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
public class ForceBlockBattleGoal extends ForceBattleGoal<Material> {

    public ForceBlockBattleGoal() {
        super(MenuType.GOAL, Message.forName("menu-force-block-battle-goal-settings"));

        registerSetting("give-block", new BooleanSubSetting(
                () -> new ItemBuilder(Material.CHEST, Message.forName("item-force-block-battle-goal-give-block")),
                false
        ));
    }

    @Override
    protected Material[] getTargetsPossibleToFind() {
        List<Material> materials = new ArrayList<>(Arrays.asList(Material.values()));
        materials.removeIf(material -> !material.isBlock());
        materials.removeIf(material -> !ItemUtils.isObtainableInSurvival(material));
        materials.removeIf(material -> material.name().contains("WALL"));
        return materials.toArray(new Material[0]);
    }

    @Override
    public Material getTargetFromDocument(Document document, String key) {
        return document.getEnum(key, Material.class);
    }

    @Override
    public List<Material> getListFromDocument(Document document, String key) {
        return document.getEnumList(key, Material.class);
    }

    @Override
    public Message getLeaderboardTitleMessage() {
        return Message.forName("force-block-battle-leaderboard");
    }

    @NotNull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.GRASS_BLOCK, Message.forName("item-force-block-battle-goal"));
    }

    @Override
    public String getTargetName(Material target) {
        return BukkitStringUtils.getItemName(target).toPlainText();
    }

    @Override
    public Object getTargetMessageReplacement(Material target) {
        return target;
    }

    @Override
    protected Message getNewTargetMessage() {
        return Message.forName("force-block-battle-new-block");
    }

    @Override
    protected Message getTargetFoundMessage() {
        return Message.forName("force-block-battle-found");
    }

    @Override
    public boolean isSmall() {
        return true;
    }

    @Override
    public void handleJokerUse(Player player) {
        super.handleJokerUse(player);
        if (giveBlockOnSkip()) {
            InventoryUtils.dropOrGiveItem(player.getInventory(), player.getLocation(), currentTarget.get(player.getUniqueId()));
        }
    }

    @ScheduledTask(ticks = 5, async = false, timerPolicy = TimerPolicy.STARTED)
    public void checkBlocks() {
        if(!shouldExecuteEffect()) return;
        broadcastFiltered(player -> {
            if(isStandingOnBlock(player, currentTarget.get(player.getUniqueId()))) {
                handleTargetFound(player);
            }
        });
    }

    protected boolean isStandingOnBlock(@Nonnull Player player, Material block) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -1; y <= 1; y++) {
                    Material type = player.getLocation().add(x, y, z).getBlock().getType();
                    if (type == block) return true;
                }
            }
        }
        return false;
    }

    @Override
    public void handleDisplayStandUpdate(@NotNull Player player, @NotNull ArmorStand armorStand) {
        Material block = currentTarget.get(player.getUniqueId());
        if (block == null) {
            block = Material.AIR;
        }

        ItemStack helmet = armorStand.getEquipment().getHelmet();
        if (helmet == null || helmet.getType() != block) {
            armorStand.getEquipment().setHelmet(new ItemStack(block));
        }
    }

    private boolean giveBlockOnSkip() {
        return getSetting("give-block").getAsBoolean();
    }

}
