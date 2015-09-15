package de.fxdiagram.mapping.shapes;

import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.shapes.BaseShapeInitializer;

@SuppressWarnings("all")
public class BaseNodeLabel<T extends Object> extends XLabel {
  public BaseNodeLabel() {
    BaseShapeInitializer.initializeLazily(this);
  }
  
  public BaseNodeLabel(final IMappedElementDescriptor<T> descriptor) {
    super(descriptor);
  }
  
  @Override
  public IMappedElementDescriptor<T> getDomainObjectDescriptor() {
    DomainObjectDescriptor _domainObjectDescriptor = super.getDomainObjectDescriptor();
    return ((IMappedElementDescriptor<T>) _domainObjectDescriptor);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
  }
  
  @Override
  public String getType() {
    IMappedElementDescriptor<T> _domainObjectDescriptor = this.getDomainObjectDescriptor();
    AbstractMapping<T> _mapping = _domainObjectDescriptor.getMapping();
    return _mapping.getID();
  }
}
