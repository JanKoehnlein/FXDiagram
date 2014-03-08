package de.fxdiagram.annotations.test;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;

@ModelNode({ "domainObject" })
@SuppressWarnings("all")
public class Modeltest3 extends XNode {
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(domainObjectProperty(), DomainObjectDescriptor.class);
  }
}
