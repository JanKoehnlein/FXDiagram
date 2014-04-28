package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractMapping;

@SuppressWarnings("all")
public interface XDiagramConfig {
  public abstract <T extends Object> Iterable<? extends AbstractMapping<T>> getMappings(final T domainObject);
  
  public abstract AbstractMapping<?> getMappingByID(final String mappingID);
  
  public abstract String getID();
}
