package net.codingarea.challenges.plugin.management.challenges.entities;

import javax.annotation.Nonnull;
import net.anweisen.utilities.common.config.Document;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public interface GamestateSaveable {

  String getUniqueName();
  void writeGameState(@Nonnull Document document);
  void loadGameState(@Nonnull Document document);

}
