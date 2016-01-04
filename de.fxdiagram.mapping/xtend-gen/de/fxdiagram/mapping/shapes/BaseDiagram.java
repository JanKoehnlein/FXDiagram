package de.fxdiagram.mapping.shapes;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.reconcile.DiagramReconcileBehavior;
import de.fxdiagram.mapping.shapes.BaseShapeInitializer;

@ModelNode
@SuppressWarnings("all")
public class BaseDiagram<T extends Object> extends XDiagram {
  public BaseDiagram() {
    BaseShapeInitializer.initializeLazily(this);
  }
  
  public BaseDiagram(final IMappedElementDescriptor<T> domainObjectDescriptor) {
    super(domainObjectDescriptor);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    DiagramReconcileBehavior<T> _diagramReconcileBehavior = new DiagramReconcileBehavior<T>(this);
    this.addBehavior(_diagramReconcileBehavior);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
  
  public String toString() {
    return ToString.toString(this);
  }
}
