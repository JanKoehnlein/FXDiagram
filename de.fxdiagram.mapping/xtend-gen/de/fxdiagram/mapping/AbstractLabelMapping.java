package de.fxdiagram.mapping;

import de.fxdiagram.core.XLabel;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.execution.XDiagramConfig;

/**
 * Base class for label mappings.
 * 
 * As labels are integral parts of nodes and connections, the create method is always called
 * from within a transaction and can therefore provide the domain object itself.
 */
@SuppressWarnings("all")
public abstract class AbstractLabelMapping<T extends Object> extends AbstractMapping<T> {
  public AbstractLabelMapping(final XDiagramConfig config, final String id, final String displayName) {
    super(config, id, displayName);
  }
  
  public abstract XLabel createLabel(final IMappedElementDescriptor<T> labelDescriptor, final T labelElement);
}
