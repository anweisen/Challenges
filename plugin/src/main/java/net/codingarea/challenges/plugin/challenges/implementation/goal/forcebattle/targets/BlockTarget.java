package net.codingarea.challenges.plugin.challenges.implementation.goal.forcebattle.targets;

import net.anweisen.utilities.bukkit.utils.item.ItemUtils;
import net.codingarea.challenges.plugin.challenges.implementation.goal.forcebattle.ExtremeForceBattleGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.bukkit.misc.BukkitStringUtils;
import net.codingarea.challenges.plugin.utils.misc.EntityUtils;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public class BlockTarget extends ForceTarget<Material> {

    protected BlockTarget(Material target) {
        super(target);
    }

    @Override
    public boolean check(Player player) {
        return EntityUtils.isStandingOnBlock(player, target);
    }

    public static List<Material> getPossibleBlocks() {
        List<Material> materials = new ArrayList<>(Arrays.asList(Material.values()));
        materials.removeIf(material -> !material.isBlock());
        materials.removeIf(material -> !ItemUtils.isObtainableInSurvival(material));
        return materials;
    }

    @Override
    public void updateDisplayStand(@NotNull Player player, @NotNull ArmorStand armorStand) {
        ItemStack helmet = armorStand.getEquipment().getHelmet();
        if(helmet == null || helmet.getType() != target) {
            armorStand.getEquipment().setHelmet(new ItemStack(target));
        }
    }

    @Override
    public Object toMessage() {
        return target;
    }

    @Override
    public String getName() {
        return BukkitStringUtils.getItemName(target).toPlainText();
    }

    @Override
    public Message getNewTargetMessage() {
        return Message.forName("force-block-battle-new-block");
    }

    @Override
    public Message getCompletedMessage() {
        return Message.forName("force-block-battle-found");
    }

    @Override
    public ExtremeForceBattleGoal.TargetType getType() {
        return ExtremeForceBattleGoal.TargetType.BLOCK;
    }

    @Override
    public String toString() {
        return target.name();
    }

}
