package net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl;

import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.ChallengeTrigger;
import net.codingarea.challenges.plugin.challenges.type.helper.SubSettingsHelper;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class ConsumeItemTrigger extends ChallengeTrigger {

	public ConsumeItemTrigger(String name) {
		super(name, SubSettingsBuilder.createChooseMultipleItem(SubSettingsHelper.ITEM).fill(builder -> {
			for (Material material : Material.values()) {
				if (material.isEdible()) {
					builder.addSetting(material.name(), new ItemBuilder(material, DefaultItem.getItemPrefix() + StringUtils
							.getEnumName(material)).build());
				}
			}
		}));
	}

	@Override
	public Material getMaterial() {
		return Material.COOKED_BEEF;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onConsumeItem(PlayerItemConsumeEvent event) {
		createData()
				.entity(event.getPlayer())
				.event(event)
				.data(
						SubSettingsHelper.ITEM, SubSettingsHelper.ANY, event.getItem().getType().name())
				.execute();
	}

}
