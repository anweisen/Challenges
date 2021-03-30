package net.codingarea.challenges.plugin.management.challenges;

import net.anweisen.utilities.bukkit.core.BukkitModule;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.implementation.damage.DamageRuleSetting;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.Optional;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class ModuleChallengeLoader {

	protected final BukkitModule plugin;

	public ModuleChallengeLoader(@Nonnull BukkitModule plugin) {
		this.plugin = plugin;
	}

	public final void registerWithCommand(@Nonnull IChallenge challenge, @Nonnull String... commandNames) {
		try {

			Challenges.getInstance().getChallengeManager().register(challenge);
			Challenges.getInstance().getScheduler().register(challenge);

			if (challenge instanceof CommandExecutor) {
				plugin.registerCommand((CommandExecutor) challenge, commandNames);
			}
			if (challenge instanceof Listener) {
				plugin.registerListener((Listener) challenge);
			}

		} catch (Exception ex) {
			Logger.severe("Could not register challenge " + challenge.getClass().getSimpleName(), ex);
		}
	}

	public final void register(@Nonnull IChallenge challenge) {
		registerWithCommand(challenge);
	}

	public final void registerWithCommand(@Nonnull Class<? extends IChallenge> classOfChallenge, @Nonnull String[] commandNames, @Nonnull Class<?>[] parameterClasses, @Nonnull Object... parameters) {
		try {

			Constructor<? extends IChallenge> constructor = classOfChallenge.getDeclaredConstructor(parameterClasses);
			IChallenge challenge = constructor.newInstance(parameters);

			registerWithCommand(challenge, commandNames);

		} catch (Throwable ex) {
			Logger.severe("Could not create challenge " + classOfChallenge.getSimpleName(), ex);
		}
	}

	public final void register(@Nonnull Class<? extends IChallenge> classOfChallenge, @Nonnull Class<?>[] parameterClasses, @Nonnull Object... parameters) {
		registerWithCommand(classOfChallenge, new String[0], parameterClasses, parameters);
	}

	public final void register(@Nonnull Class<? extends IChallenge> classOfChallenge, @Nonnull Object... parameters) {

		Class<?>[] parameterClasses = new Class[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			parameterClasses[i] = Optional.ofNullable(parameters[i]).<Class<?>>map(Object::getClass).orElse(Object.class);
		}

		register(classOfChallenge, parameterClasses, parameters);

	}

	public final void registerWithCommand(@Nonnull Class<? extends IChallenge> classOfChallenge, @Nonnull String... commandNames) {
		registerWithCommand(classOfChallenge, commandNames, new Class[0]);
	}


	public final void registerDamageRule(@Nonnull String name, @Nonnull Material material, @Nonnull DamageCause... causes) {
		registerDamageRule(name, new ItemBuilder(material), causes);
	}

	public final void registerDamageRule(@Nonnull String name, @Nonnull ItemBuilder preset, @Nonnull DamageCause... causes) {
		register(DamageRuleSetting.class, new Class[] { ItemBuilder.class, String.class, DamageCause[].class }, preset, name, causes);
	}

}
