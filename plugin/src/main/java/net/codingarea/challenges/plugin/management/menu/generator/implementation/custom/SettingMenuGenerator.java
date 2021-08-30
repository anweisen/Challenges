package net.codingarea.challenges.plugin.management.menu.generator.implementation.custom;

import net.codingarea.challenges.plugin.challenges.custom.SubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.api.IChallengeEnum;
import net.codingarea.challenges.plugin.management.menu.generator.ChooseItemGenerator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.function.Function;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class SettingMenuGenerator extends ChooseItemGenerator implements IParentCustomGenerator {

	private final IParentCustomGenerator parent;
	private final String key;
	private final String title;
	private final Function<String, IChallengeEnum> enumGetter;
	private IChallengeEnum setting;
	private SubSettingsBuilder subSettingsBuilder;
	private ArrayList<String> subSettings;

	public SettingMenuGenerator(IParentCustomGenerator parent, String key, String title, LinkedHashMap<String, ItemStack> items, Function<String, IChallengeEnum> enumGetter) {
		super(items);
		this.parent = parent;
		this.key = key;
		this.title = title;
		this.enumGetter = enumGetter;
		this.subSettings = new ArrayList<>(Collections.singletonList(key));
	}

	@Override
	public String[] getSubTitles(int page) {
		return new String[]{ "Create", title };
	}

	@Override
	public int[] getNavigationSlots(int page) {
		return MainMenuGenerator.NAVIGATION_SLOTS;
	}

	public IParentCustomGenerator getParent() {
		return parent;
	}

	@Override
	public void accept(Player player, String... data) {
		if (data.length > 0) {
			subSettings.addAll(Arrays.asList(data));

			if (!openSubSettingsMenu(player)) {
				parent.accept(player, subSettings.toArray(new String[0]));
			}

		} else {
			parent.accept(player, key, setting.name());
		}
	}

	@Override
	public void onItemClick(Player player, String key) {
		this.setting = enumGetter.apply(key);
		this.subSettingsBuilder = setting.getSubSettingsBuilder();

		if (subSettings.size() == 1) subSettings.add(setting.name());

		if (!openSubSettingsMenu(player)) {
			parent.accept(player, this.key, setting.name());
		}

	}

	private boolean openSubSettingsMenu(Player player) {

		if (subSettingsBuilder != null && subSettingsBuilder.hasSettings()) {
			SubSettingMenuGenerator generator = new SubSettingMenuGenerator(this, subSettingsBuilder.getSettings(), title);
			generator.open(player, 0);
			subSettingsBuilder = subSettingsBuilder.getChild();

			return true;
		}
		return false;
	}

	@Override
	public void onBackToMenuItemClick(Player player) {
		parent.decline(player);
	}

	@Override
	public void decline(Player player) {
		if (setting != null) this.subSettings = new ArrayList<>(Arrays.asList(key, setting.name()));
		open(player, 0);
	}

}
