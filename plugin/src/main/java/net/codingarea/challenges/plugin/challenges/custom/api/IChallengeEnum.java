package net.codingarea.challenges.plugin.challenges.custom.api;

import net.codingarea.challenges.plugin.challenges.custom.SubSettingsBuilder;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface IChallengeEnum {

	SubSettingsBuilder getSubSettingsBuilder();
	String name();
	Material getMaterial();
	String getMessage();
	IChallengeEnum[] getValues();

	default LinkedHashMap<String, ItemStack> getMenuItems() {
		LinkedHashMap<String, ItemStack> map = new LinkedHashMap<>();

		for (IChallengeEnum value : getValues()) {
			map.put(value.name(), new ItemBuilder(value.getMaterial(), Message.forName(value.getMessage())).build());
		}

		return map;
	}

}
