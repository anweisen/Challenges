package net.codingarea.challenges.plugin.management.menu.generator.implementation.custom;

import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public interface IParentCustomGenerator {

	void accept(Player player, String... data);

	void decline(Player player);

}
