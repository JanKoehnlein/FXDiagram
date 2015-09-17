package de.fxdiagram.mapping;

import de.fxdiagram.mapping.AbstractLabelMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.shapes.BaseNodeLabel;

@SuppressWarnings("all")
public class NodeLabelMapping<T extends Object> extends AbstractLabelMapping<T> {
  public NodeLabelMapping(final XDiagramConfig config, final String id) {
    super(config, id, id);
  }
  
  @Override
  public BaseNodeLabel<T> createLabel(final IMappedElementDescriptor<T> descriptor) {
    return new BaseNodeLabel<T>(descriptor);
  }
}
