package net.codingarea.challenges.plugin.challenges.type;

import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface Goal extends IChallenge {

	void setEnabled(boolean enabled);

	@Nonnull
	SoundSample getStartSound();

	void getWinnersOnEnd(@Nonnull List<Player> winners);

}
