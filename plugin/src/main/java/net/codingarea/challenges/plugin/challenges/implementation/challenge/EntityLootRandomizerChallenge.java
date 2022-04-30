package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.bukkit.utils.item.ItemUtils;
import net.codingarea.challenges.plugin.challenges.type.abstraction.RandomizerSetting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EntityLootRandomizerChallenge extends RandomizerSetting {
    protected final Map<Material, Material> randomization = new HashMap<>();

    public EntityLootRandomizerChallenge() {
        super(MenuType.CHALLENGES);
    }

    @NotNull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.HOPPER_MINECART, Message.forName("entity-loot-randomizer-challenge"));
    }

    @Override
    protected void reloadRandomization() {
        List<Material> from = new ArrayList<>(Arrays.asList(Material.values()));
        from.removeIf(material -> !material.isItem() || !ItemUtils.isObtainableInSurvival(material));
        random.shuffle(from);

        List<Material> to = new ArrayList<>(Arrays.asList(Material.values()));
        to.removeIf(material -> !material.isItem() || !ItemUtils.isObtainableInSurvival(material));
        random.shuffle(to);

        while (!from.isEmpty()) {
            Material item = from.remove(0);
            Material result = to.remove(0);

            randomization.put(item, result);
        }
    }

    @Override
    protected void onDisable() {
        randomization.clear();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if(!isEnabled()) return;
        if(!event.getEntityType().isAlive() || event.getEntityType().equals(EntityType.PLAYER)) return;
        if(event.getDrops().size() == 0) return;

        Iterator<ItemStack> drops = event.getDrops().iterator();
        List<ItemStack> newDrops = new ArrayList<>();

        while(drops.hasNext()) {
            ItemStack drop = drops.next();
            Material result = randomization.get(drop.getType());
            if(result == null) continue;
            newDrops.add(new ItemBuilder(result).amount(drop.getAmount()).build());
            drops.remove();
        }

        event.getDrops().addAll(newDrops);
    }
}
