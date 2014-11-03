package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public interface IMappedElementDescriptor<T extends Object> extends DomainObjectDescriptor {
  public abstract AbstractMapping<T> getMapping();
  
  public abstract <U extends Object> U withDomainObject(final Function1<? super T, ? extends U> lambda);
}
