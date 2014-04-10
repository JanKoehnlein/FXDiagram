package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import java.util.List;

@SuppressWarnings("all")
public interface XDiagramConfig {
  public abstract <T extends Object> List<? extends AbstractMapping<T>> getMappings(final T domainObject);
}
