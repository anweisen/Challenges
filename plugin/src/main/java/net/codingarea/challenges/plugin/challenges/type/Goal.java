package net.codingarea.challenges.plugin.challenges.type;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.management.menu.event.MenuClickEvent;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.naming.OperationNotSupportedException;
import java.util.Collection;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class Goal extends AbstractChallenge {

	private boolean enabled = false;

	public Goal() {
		super(MenuType.GOAL);
	}

	@Nonnull
	public SoundSample getStartSound() {
		return SoundSample.DRAGON_BREATH;
	}

	protected void onEnable() {
	}

	protected void onDisable() {
	}

	@Override
	public void handleClick(@Nonnull MenuClickEvent event) {
		setEnabled(!enabled);
		SoundSample.playEnablingSound(event.getPlayer(), enabled);
	}

	public final void setEnabled(boolean enabled) {
		if (this.enabled == enabled) return;
		this.enabled = enabled;

		if (Challenges.getInstance().getChallengeManager().getCurrentGoal() != this && enabled) {
			Challenges.getInstance().getChallengeManager().setCurrentGoal(this);
		} else if (Challenges.getInstance().getChallengeManager().getCurrentGoal() == this && !enabled) {
			Challenges.getInstance().getChallengeManager().setCurrentGoal(null);
		}

		if (enabled) onEnable();
		else onDisable();

		updateItems();
	}

	@Override
	public void loadSettings(@Nonnull Document document) {
		setEnabled(document.getBoolean("enabled", enabled));
	}

	@Override
	public void writeSettings(@Nonnull Document document) {
		document.set("enabled", enabled);
	}

	@Override
	public final boolean isEnabled() {
		return enabled;
	}

	public abstract void getWinnersOnEnd(@Nonnull List<Player> winners);

}
