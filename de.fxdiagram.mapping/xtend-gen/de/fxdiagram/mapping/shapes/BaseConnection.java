package de.fxdiagram.mapping.shapes;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.behavior.ConnectionReconcileBehavior;
import de.fxdiagram.mapping.shapes.BaseShapeInitializer;

/**
 * Base implementation for a {@link XConnection} that belongs to an {@link IMappedElementDescriptor}.
 * 
 * If the descriptor is an {@link AbstractXtextDescriptor}, members are automatically injected using
 * the Xtext language's injector.
 */
@ModelNode
@SuppressWarnings("all")
public class BaseConnection<T extends Object> extends XConnection {
  public BaseConnection() {
    BaseShapeInitializer.initializeLazily(this);
  }
  
  public BaseConnection(final IMappedElementDescriptor<T> descriptor) {
    super(descriptor);
  }
  
  @Override
  public DomainObjectDescriptor getDomainObjectDescriptor() {
    DomainObjectDescriptor _domainObjectDescriptor = super.getDomainObjectDescriptor();
    return ((IMappedElementDescriptor<T>) _domainObjectDescriptor);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    ConnectionReconcileBehavior<Object> _connectionReconcileBehavior = new ConnectionReconcileBehavior<Object>(this);
    this.addBehavior(_connectionReconcileBehavior);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
