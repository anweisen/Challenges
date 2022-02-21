package net.codingarea.challenges.plugin.challenges.custom.settings.sub;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.ChooseItemSubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.ChooseMultipleItemSubSettingBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.EmptySubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.ValueSubSettingsBuilder;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.custom.IParentCustomGenerator;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public abstract class SubSettingsBuilder {

	private final String key;
	private SubSettingsBuilder parent;
	private SubSettingsBuilder child;

	protected SubSettingsBuilder(String key) {
		this.key = key;
		parent = null;
	}

	protected SubSettingsBuilder(String key, SubSettingsBuilder parent) {
		this.key = key;
		this.parent = parent;
	}

	public abstract boolean open(Player player, IParentCustomGenerator parentGenerator, String title);
	public abstract List<String> getDisplay(Map<String, String[]> activated);
	public abstract boolean hasSettings();

	public SubSettingsBuilder getParent() {
		return parent;
	}

	public SubSettingsBuilder getChild() {
		return child;
	}

	public String getKey() {
		return key;
	}

	public List<SubSettingsBuilder> getAllChilds() {
		LinkedList<SubSettingsBuilder> childs = new LinkedList<>(Collections.singleton(this));

		SubSettingsBuilder last = this;

		while (last != null) {
			if (last.getChild() != null) {
				childs.add(last.getChild());
			}
			last = last.getChild();
		}

		return childs;
	}

	/**
	 * @return the first parent that was created.
	 * Only required if first builder has one ore more childs.
	 */
	public SubSettingsBuilder build() {
		return parent == null ? this : parent.build();
	}

	/**
	 * Sets the highest parent of a child as the child of this builder.
	 * @param child one of the child builders that are added.
	 * @return the highest parent of that child
	 */
	public SubSettingsBuilder addChild(SubSettingsBuilder child) {
		this.child = child.setParent(this);
		return child;
	}

	public SubSettingsBuilder setParent(SubSettingsBuilder parent) {

		SubSettingsBuilder parentBuilder = getParent();
		if (parentBuilder != null) {
			return parentBuilder.setParent(parent);
		} else {
			this.parent = parent;
			return this;
		}

	}

	public ChooseItemSubSettingsBuilder createChooseItemChild(String key) {
		ChooseItemSubSettingsBuilder builder = new ChooseItemSubSettingsBuilder(key, this);
		this.child = builder;
		return builder;
	}

	public ValueSubSettingsBuilder createValueChild() {
		ValueSubSettingsBuilder builder = new ValueSubSettingsBuilder(this);
		this.child = builder;
		return builder;
	}

	public ChooseMultipleItemSubSettingBuilder createChooseMultipleChild(String key) {
		ChooseMultipleItemSubSettingBuilder builder = new ChooseMultipleItemSubSettingBuilder(key, this);
		this.child = builder;
		return builder;
	}

	public static ChooseItemSubSettingsBuilder createChooseItem(String key) {
		return new ChooseItemSubSettingsBuilder(key);
	}

	public static ValueSubSettingsBuilder createValueItem() {
		return new ValueSubSettingsBuilder();
	}

	public static ChooseMultipleItemSubSettingBuilder createChooseMultipleItem(String key) {
		return new ChooseMultipleItemSubSettingBuilder(key);
	}

	public static EmptySubSettingsBuilder createEmpty() {
		return new EmptySubSettingsBuilder();
	}

}
