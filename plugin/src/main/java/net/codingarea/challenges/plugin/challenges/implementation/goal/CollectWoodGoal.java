package net.codingarea.challenges.plugin.challenges.implementation.goal;

import net.codingarea.challenges.plugin.challenges.type.CollectionGoal;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.spigot.events.PlayerInventoryClickEvent;
import net.codingarea.challenges.plugin.spigot.events.PlayerPickupItemEvent;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.ListBuilder;
import net.codingarea.challenges.plugin.utils.misc.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class CollectWoodGoal extends CollectionGoal {

	public CollectWoodGoal() {
		super(getWoodMaterials());
		System.out.println("--------------------------------------------------------------");
		System.out.println(Arrays.toString(getWoodMaterials()));
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.OAK_LOG, Message.forName("item-collect-wood-goal"));
	}

	private static Object[] getWoodMaterials() {
		return new ListBuilder<Material>().fill(builder -> {
			for (Material material : Material.values()) {
				if (isLog(material)) {
					builder.add(material);
				}
			}
		}).build().toArray();

	}

	private static boolean isLog(@Nonnull Material material) {
		return material.name().contains("LOG") && !material.name().contains("STRIPPED");
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPickupItem(@Nonnull PlayerPickupItemEvent event) {
		if (shouldExecuteEffect()) return;
		Material material = event.getItem().getItemStack().getType();
		Player player = event.getPlayer();
		handleCollect(player, material);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerInventoryClick(@Nonnull PlayerInventoryClickEvent event) {
		if (shouldExecuteEffect()) return;
		if (event.isCancelled()) return;
		if (event.getClickedInventory() == null) return;
		if (event.getClickedInventory().getHolder() != event.getPlayer()) return;
		if (event.getCurrentItem() == null) return;
		Player player = event.getPlayer();
		Material material = event.getCurrentItem().getType();
		handleCollect(player, material);
	}

	private void handleCollect(@Nonnull Player player, @Nonnull Material material) {
		collect(player, material, () -> {
			Message.forName("item-collected").broadcast(Prefix.CHALLENGES, StringUtils.getEnumName(material));
			SoundSample.PLING.play(player);
		});
	}

}