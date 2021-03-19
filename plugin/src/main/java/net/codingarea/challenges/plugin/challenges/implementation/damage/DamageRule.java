package net.codingarea.challenges.plugin.challenges.implementation.damage;

import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class DamageRule extends Setting {

	private final DamageCause[] cause;

	private final String name;
	private final Material material;

	public DamageRule(Material material, String name, DamageCause... cause) {
		super(MenuType.DAMAGE, true);
		this.cause = cause;
		this.name = name;
		this.material = material;
	}

	@Nonnull
	@Override
	public String getName() {
		return super.getName() + name;
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(material, Message.forName("item-damage-rule"));
	}

}