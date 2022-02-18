package net.codingarea.challenges.plugin.challenges.custom.settings;

import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public interface IChallengeParam {

	SubSettingsBuilder getSubSettingsBuilder();
	String name();
	Material getMaterial();
	String getMessage();
	IChallengeParam[] getValues();

	default LinkedHashMap<String, ItemStack> getMenuItems() {
		LinkedHashMap<String, ItemStack> map = new LinkedHashMap<>();

		for (IChallengeParam value : getValues()) {
			map.put(value.name(), new ItemBuilder(value.getMaterial(), Message.forName(value.getMessage())).hideAttributes().build());
		}

		return map;
	}

}
