package net.codingarea.challenges.plugin.challenges.custom.settings;

import java.util.function.Supplier;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public abstract class AbstractChallengeSetting implements IChallengeSetting {

  private final String name;
  private final SubSettingsBuilder subSettingsBuilder;

  public AbstractChallengeSetting(String name, SubSettingsBuilder subSettingsBuilder) {
    this.name = name;
    this.subSettingsBuilder = subSettingsBuilder.build();
  }

  public AbstractChallengeSetting(String name) {
    this(name, SubSettingsBuilder.createEmpty());
  }

  public AbstractChallengeSetting(String name, Supplier<SubSettingsBuilder> builderSupplier) {
    this(name, builderSupplier.get());
  }

  public String getMessageSuffix() {
    return name.toLowerCase();
  }

  @Override
  public SubSettingsBuilder getSubSettingsBuilder() {
    return subSettingsBuilder;
  }

  @Override
  public String getName() {
    return name;
  }

}
