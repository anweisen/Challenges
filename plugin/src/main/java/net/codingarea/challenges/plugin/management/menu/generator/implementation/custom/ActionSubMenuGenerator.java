package net.codingarea.challenges.plugin.management.menu.generator.implementation.custom;

import net.codingarea.challenges.plugin.challenges.custom.api.ChallengeAction;
import net.codingarea.challenges.plugin.management.menu.generator.ChooseItemGenerator;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class ActionSubMenuGenerator extends ChooseItemGenerator {

	private final IParentCustomGenerator parent;

	public ActionSubMenuGenerator(IParentCustomGenerator parent, ChallengeAction action) {
		super(action.getSubSettingsBuilder().getSettings());
		this.parent = parent;
	}

	@Override
	public String[] getSubTitles(int page) {
		return new String[]{ "Create", "Action" };
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
