package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.bukkit.utils.item.ItemUtils;
import net.anweisen.utilities.common.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.abstraction.RandomizerSettingModifier;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.generator.categorised.SettingCategory;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.ListBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

@Since("2.1.4")
public class EntityLootRandomizerChallenge extends RandomizerSettingModifier {
    protected final Map<Material, Material> randomization = new HashMap<>();
    protected final Map<EntityType, LootTable> swappedLoot = new HashMap<>();
    private static final int
            RANDOMIZE_LOOT = 1,
            SWAP_LOOT = 2;

    public EntityLootRandomizerChallenge() {
        super(MenuType.CHALLENGES, 1, 2);
        setCategory(SettingCategory.RANDOMIZER);
    }

    @NotNull
    @Override
    public ItemBuilder createDisplayItem() {
        return new ItemBuilder(Material.FURNACE_MINECART, Message.forName("item-entity-loot-randomizer-challenge"));
    }

    @Nonnull
    @Override
    public ItemBuilder createSettingsItem() {
        if (getValue() == SWAP_LOOT) return DefaultItem.create(Material.CHEST, Message.forName("item-entity-loot-randomizer-challenge-swap_loot"));
        return DefaultItem.create(Material.DROPPER, Message.forName("item-entity-loot-randomizer-challenge-randomize_loot"));
    }

    @Override
    protected void reloadRandomization() {
        reloadRandomizedLoot();
        reloadSwappedLoot();
    }

    private void reloadRandomizedLoot() {
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

    private void reloadSwappedLoot() {
        List<EntityType> from = new ArrayList<>(Arrays.asList(EntityType.values()));
        from.removeIf(entityType -> !getEntitiesWithLoot().contains(entityType));
        random.shuffle(from);
        from.removeIf(Objects::isNull);

        List<EntityType> entityTypesToRemove = new ArrayList<>();
        List<LootTable> to = from.stream().map(entityType -> {
            if(entityType == null) return null;
            try {
                return LootTables.valueOf(entityType.name()).getLootTable();
            } catch (IllegalArgumentException exception) {
                entityTypesToRemove.add(entityType);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        from.removeAll(entityTypesToRemove);
        random.shuffle(to);

        while (!from.isEmpty()) {
            EntityType entityType = from.remove(0);
            LootTable lootTable = to.remove(0);

            swappedLoot.put(entityType, lootTable);
        }
    }

    private List<EntityType> getEntitiesWithLoot() {
        return new ListBuilder<>(EntityType.values())
                .removeIf(type -> !type.isSpawnable())
                .removeIf(type -> !type.isAlive())
                .remove(EntityType.ENDER_DRAGON)
                .remove(EntityType.GIANT)
                .remove(EntityType.ILLUSIONER)
                .build();
    }

    @Override
    protected void onDisable() {
        randomization.clear();
        swappedLoot.clear();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if(!isEnabled()) return;
        if(!event.getEntityType().isAlive() || event.getEntityType().equals(EntityType.PLAYER)) return;
        if(getValue() == RANDOMIZE_LOOT) {
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
        } else if(getValue() == SWAP_LOOT) {
            LivingEntity entity = event.getEntity();
            if(!getEntitiesWithLoot().contains(entity.getType())) return;
            event.getDrops().clear();
            if(!swappedLoot.containsKey(entity.getType())) return;

            LootTable lootTable = swappedLoot.get(entity.getType());
            LootContext.Builder builder = new LootContext.Builder(entity.getLocation())
                    .lootedEntity(entity);

            if(entity.getKiller() == null) {
                builder.lootingModifier(0);
            } else {
                builder.killer(entity.getKiller());
            }

            Collection<ItemStack> newDrops = lootTable.populateLoot(random.asRandom(), builder.build());
            event.getDrops().addAll(newDrops);
        }
    }
}
