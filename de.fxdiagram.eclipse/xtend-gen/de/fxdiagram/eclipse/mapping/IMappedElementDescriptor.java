package de.fxdiagram.eclipse.mapping;

import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.eclipse.mapping.AbstractMapping;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

/**
 * A {@link DomainObjectDescriptor} that describes a domain object that can be mapped
 * to a diagram element as described by an {@link AbstractMapping}.
 * 
 * Each domain object can only appear once with the same mapping in the same diagram.
 */
@SuppressWarnings("all")
public interface IMappedElementDescriptor<T extends Object> extends DomainObjectDescriptor {
  public abstract AbstractMapping<T> getMapping();
  
  public abstract <U extends Object> U withDomainObject(final Function1<? super T, ? extends U> lambda);
  
  public abstract IEditorPart openInEditor(final boolean select);
}
