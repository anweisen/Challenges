package net.codingarea.challenges.plugin.management.menu.generator.implementation.custom;

import net.codingarea.challenges.plugin.management.menu.generator.ChooseItemGenerator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.TreeMap;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class SubSettingMenuGenerator extends ChooseItemGenerator {

	private final IParentCustomGenerator parent;
	private final String title;

	public SubSettingMenuGenerator(IParentCustomGenerator parent, TreeMap<String, ItemStack> map, String title) {
		super(map);
		this.title = title;
		this.parent = parent;
	}

	@Override
	public String[] getSubTitles(int page) {
		return new String[]{ "Create", title };
	}

	@Override
	public void onItemClick(Player player, String key) {
		parent.accept(player, key);
	}

	@Override
	public void onBackToMenuItemClick(Player player) {
		parent.decline(player);
	}

	public IParentCustomGenerator getParent() {
		return parent;
	}

}
