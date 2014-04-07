package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.BaseMapping;
import java.util.List;

@SuppressWarnings("all")
public interface XDiagramConfig {
  public abstract <T extends Object> List<? extends BaseMapping<T>> getMappings(final T domainObject);
}
