package net.codingarea.challenges.plugin.challenges.custom;

import org.bukkit.inventory.ItemStack;

import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class SubSettingsBuilder {

	private final TreeMap<String, ItemStack> settings = new TreeMap<>();
	private final SubSettingsBuilder parent;
	private SubSettingsBuilder child;

	private SubSettingsBuilder() {
		parent = null;
	}

	private SubSettingsBuilder(SubSettingsBuilder parent) {
		this.parent = parent;
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

	public TreeMap<String, ItemStack> getSettings() {
		return settings;
	}

	public SubSettingsBuilder getParent() {
		return parent;
	}

	public SubSettingsBuilder getChild() {
		return child;
	}

	public SubSettingsBuilder build() {
		return parent == null ? this : parent.build();
	}

	public SubSettingsBuilder createChild() {
		this.child = new SubSettingsBuilder(this);
		return child;
	}

	public static SubSettingsBuilder create() {
		return new SubSettingsBuilder();
	}

}
