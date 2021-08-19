package net.codingarea.challenges.plugin.challenges.custom;

import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class SubSettingsBuilder {

//	private final SubSettingsBuilder parent;
//	private final List<Map<String, Material>> subSettings = new ArrayList<>();
	private final TreeMap<String, ItemStack> settings = new TreeMap<>();

	private SubSettingsBuilder() {
//		this.parent = null;
	}

	private SubSettingsBuilder(SubSettingsBuilder parent) {
//		this.parent = parent;
	}

	public SubSettingsBuilder addSetting(String key, ItemStack value) {
		settings.put(key, value);
		return this;
	}

	public SubSettingsBuilder fill(Consumer<SubSettingsBuilder> actions) {
		actions.accept(this);
		return this;
	}

	public boolean hasSettings() {
		return !settings.isEmpty();
	}

//	public boolean hasSubSettings() {
//		return !subSettings.isEmpty();
//	}
//
//	private void addSubSetting(SubSettingsBuilder builder) {
//		subSettings.add(builder.getSettings());
//	}

	public Map<String, ItemStack> getSettings() {
		return settings;
	}

//	public List<Map<String, Material>> getSubSettings() {
//		return subSettings;
//	}

//	public SubSettingsBuilder build() {

//		if (parent != null) {
//			parent.addSubSetting(this);
//		}
//
//		return parent != null ? parent : this;
//	}
//
//	public SubSettingsBuilder createSubSetting() {
//		return new SubSettingsBuilder(this);
//	}

	public static SubSettingsBuilder create() {
		return new SubSettingsBuilder();
	}

}
