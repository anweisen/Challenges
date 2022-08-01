package net.codingarea.challenges.plugin.challenges.implementation.goal.forcebattle.targets;

import net.codingarea.challenges.plugin.challenges.implementation.goal.forcebattle.ExtremeForceBattleGoal;
import net.codingarea.challenges.plugin.content.Message;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.3
 */
public abstract class ForceTarget<T> {
    protected final T target;

    protected ForceTarget(T target) {
        this.target = target;
    }

    public abstract boolean check(Player player);

    public abstract void updateDisplayStand(@NotNull Player player, @NotNull ArmorStand armorStand);
    public abstract Object toMessage();
    public abstract String getName();
    public abstract Message getNewTargetMessage();
    public abstract Message getCompletedMessage();
    public abstract ExtremeForceBattleGoal.TargetType getType();
    public abstract Message getScoreboardDisplayMessage();

    @Override
    public String toString() {
        return target.toString();
    }

    public T getTarget() {
        return target;
    }

    protected void resetDisplayStand(@NotNull ArmorStand armorStand) {
        armorStand.getEquipment().setHelmet(null);
    }

}