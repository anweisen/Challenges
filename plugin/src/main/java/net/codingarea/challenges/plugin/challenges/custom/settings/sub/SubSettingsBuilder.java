package net.codingarea.challenges.plugin.challenges.custom.settings.sub;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.ChooseItemSubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.EmptySubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.ValueSubSettingsBuilder;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.custom.IParentCustomGenerator;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public abstract class SubSettingsBuilder {

	private final SubSettingsBuilder parent;
	private SubSettingsBuilder child;

	protected SubSettingsBuilder() {
		parent = null;
	}

	protected SubSettingsBuilder(SubSettingsBuilder parent) {
		this.parent = parent;
	}

	public abstract boolean open(Player player, IParentCustomGenerator parentGenerator, String title);
	public abstract List<String> getDisplay(Map<String, String> activated);
	public abstract boolean hasSettings();

	public SubSettingsBuilder getParent() {
		return parent;
	}

	public SubSettingsBuilder getChild() {
		return child;
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

	public static ChooseItemSubSettingsBuilder createChooseItem(String key) {
		return new ChooseItemSubSettingsBuilder(key);
	}

	public static EmptySubSettingsBuilder createEmpty() {
		return new EmptySubSettingsBuilder();
	}

	public static ValueSubSettingsBuilder createValueItem() {
		return new ValueSubSettingsBuilder();
	}

}
