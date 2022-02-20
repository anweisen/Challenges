package net.codingarea.challenges.plugin.challenges.custom.settings.action;

import java.util.Map;
import net.anweisen.utilities.common.collection.IRandom;
import org.bukkit.entity.Entity;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public interface IChallengeAction {

  IRandom random = IRandom.create();

  void execute(Entity entity, Map<String, String[]> subActions);

}
