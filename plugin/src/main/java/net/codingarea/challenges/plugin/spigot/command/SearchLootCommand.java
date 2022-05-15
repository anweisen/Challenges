package net.codingarea.challenges.plugin.spigot.command;

import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.challenges.implementation.challenge.EntityLootRandomizerChallenge;
import net.codingarea.challenges.plugin.challenges.type.abstraction.AbstractChallenge;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.utils.bukkit.command.Completer;
import net.codingarea.challenges.plugin.utils.bukkit.command.SenderCommand;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.1.4
 */
public class SearchLootCommand implements SenderCommand, Completer {
    @Override
    public void onCommand(@Nonnull CommandSender sender, @Nonnull String[] args) throws Exception {

        if (args.length == 0) {
            Message.forName("syntax").send(sender, Prefix.CHALLENGES, "searchloot <entity>");
            return;
        }

        EntityLootRandomizerChallenge lootRandomizerInstance = AbstractChallenge.getFirstInstance(EntityLootRandomizerChallenge.class);
        if(!lootRandomizerInstance.isEnabled()) {
            Message.forName("command-searchloot-disabled").send(sender, Prefix.CHALLENGES);
            return;
        }

        String input = String.join("_", args).toUpperCase();
        EntityType entityType = Utils.getEntityType(input);

        if (entityType == null) {
            Message.forName("no-such-entity").send(sender, Prefix.CHALLENGES);
            return;
        }
        if (!entityType.isAlive()) {
            Message.forName("not-alive").send(sender, Prefix.CHALLENGES, StringUtils.getEnumName(entityType));
            return;
        }

        LootTable lootTable;
        try {
            lootTable = LootTables.valueOf(entityType.name()).getLootTable();
        } catch (IllegalArgumentException exception) {
            Message.forName("no-loot").send(sender, Prefix.CHALLENGES, StringUtils.getEnumName(entityType));
            return;
        }

        Optional<EntityType> optionalEntity = lootRandomizerInstance.getEntityForLootTable(lootTable);

        if(optionalEntity.isPresent()) {
            Message.forName("command-searchloot-result").send(sender, Prefix.CHALLENGES, StringUtils.getEnumName(entityType), StringUtils.getEnumName(optionalEntity.get()));
        } else {
            Message.forName("command-searchloot-nothing").send(sender, Prefix.CHALLENGES, StringUtils.getEnumName(entityType));
        }

    }

    @Nullable
    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull String[] args) {
        EntityLootRandomizerChallenge instance = AbstractChallenge.getFirstInstance(EntityLootRandomizerChallenge.class);
        return args.length != 1 ? null :
                instance.getLootableEntities().stream()
                        .map(material -> material.name().toLowerCase())
                        .collect(Collectors.toList()
                        );
    }
}
