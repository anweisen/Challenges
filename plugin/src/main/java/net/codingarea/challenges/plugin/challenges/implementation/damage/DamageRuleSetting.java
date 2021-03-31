package net.codingarea.challenges.plugin.challenges.implementation.damage;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class DamageRuleSetting extends Setting {

	private final List<DamageCause> causes;

	private final String name;
	private final ItemBuilder preset;

	public DamageRuleSetting(@Nonnull ItemBuilder preset, @Nonnull String name, @Nonnull DamageCause... causes) {
		super(MenuType.DAMAGE, true);
		this.causes = Arrays.asList(causes);
		this.name = name;
		this.preset = preset;
	}

	@Nonnull
	@Override
	public String getName() {
		return super.getName() + name;
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return preset.clone().applyFormat(Message.forName("item-damage-rule-" + name).asItemDescription());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onDamage(@Nonnull EntityDamageEvent event) {
		if (ChallengeAPI.isWorldInUse()) return;
		if (isEnabled()) return;
		if (!(event.getEntity() instanceof Player)) return;
		if (!causes.contains(event.getCause())) return;
		event.setCancelled(true);
	}

}