package net.codingarea.challenges.plugin.challenges.custom.settings.condition;

import net.codingarea.challenges.plugin.challenges.custom.settings.ChallengeExecutionData;
import org.bukkit.event.Listener;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public interface IChallengeCondition extends Listener {

	default ChallengeExecutionData createData() {
		return new ChallengeExecutionData(this);
	}

}
