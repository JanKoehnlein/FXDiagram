package de.fxdiagram.eclipse.mapping;

import de.fxdiagram.core.XDomainObjectOwner;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.mapping.AbstractDiagramConfig;
import de.fxdiagram.mapping.behavior.OpenElementInEditorBehavior;

@SuppressWarnings("all")
public abstract class AbstractEclipseDiagramConfig extends AbstractDiagramConfig {
  @Override
  public void initialize(final XDomainObjectOwner shape) {
    if ((shape instanceof XDomainObjectShape)) {
      OpenElementInEditorBehavior _openElementInEditorBehavior = new OpenElementInEditorBehavior(((XDomainObjectShape)shape));
      ((XDomainObjectShape)shape).addBehavior(_openElementInEditorBehavior);
    }
  }
}
