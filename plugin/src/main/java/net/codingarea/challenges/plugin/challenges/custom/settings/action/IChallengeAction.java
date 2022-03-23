package net.codingarea.challenges.plugin.challenges.custom.settings.action;

import java.util.Map;
import net.anweisen.utilities.common.collection.IRandom;
import net.codingarea.challenges.plugin.challenges.custom.settings.ChallengeExecutionData;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public interface IChallengeAction {

  IRandom random = IRandom.create();

  void execute(ChallengeExecutionData executionData, Map<String, String[]> subActions);

}
