package de.fxdiagram.mapping.shapes;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.behavior.ConnectAllBehavior;
import de.fxdiagram.mapping.reconcile.DiagramReconcileBehavior;

@ModelNode
@SuppressWarnings("all")
public class BaseDiagram<T extends Object> extends XDiagram {
  public BaseDiagram(final IMappedElementDescriptor<T> domainObjectDescriptor) {
    super(domainObjectDescriptor);
  }
  
  @Override
  public void postLoad() {
    IMappedElementDescriptor<T> _domainObjectDescriptor = this.getDomainObjectDescriptor();
    AbstractMapping<T> _mapping = null;
    if (_domainObjectDescriptor!=null) {
      _mapping=_domainObjectDescriptor.getMapping();
    }
    XDiagramConfig _config = null;
    if (_mapping!=null) {
      _config=_mapping.getConfig();
    }
    if (_config!=null) {
      _config.initialize(this);
    }
  }
  
  @Override
  public IMappedElementDescriptor<T> getDomainObjectDescriptor() {
    DomainObjectDescriptor _domainObjectDescriptor = super.getDomainObjectDescriptor();
    return ((IMappedElementDescriptor<T>) _domainObjectDescriptor);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    DiagramReconcileBehavior<T> _diagramReconcileBehavior = new DiagramReconcileBehavior<T>(this);
    this.addBehavior(_diagramReconcileBehavior);
    ConnectAllBehavior<Object> _connectAllBehavior = new ConnectAllBehavior<Object>(this);
    this.addBehavior(_connectAllBehavior);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public BaseDiagram() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
  
  public String toString() {
    return ToString.toString(this);
  }
}
