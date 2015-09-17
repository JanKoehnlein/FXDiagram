package de.fxdiagram.mapping;

import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.mapping.AbstractLabelMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.shapes.BaseConnectionLabel;

@SuppressWarnings("all")
public class ConnectionLabelMapping<T extends Object> extends AbstractLabelMapping<T> {
  public ConnectionLabelMapping(final XDiagramConfig config, final String id) {
    super(config, id, id);
  }
  
  @Override
  public XConnectionLabel createLabel(final IMappedElementDescriptor<T> descriptor) {
    return new BaseConnectionLabel<T>(descriptor);
  }
}
