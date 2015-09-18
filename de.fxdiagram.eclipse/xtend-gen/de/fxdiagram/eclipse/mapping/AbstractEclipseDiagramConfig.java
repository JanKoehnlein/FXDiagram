package de.fxdiagram.eclipse.mapping;

import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.mapping.behavior.OpenElementInEditorBehavior;
import de.fxdiagram.mapping.execution.AbstractDiagramConfig;

@SuppressWarnings("all")
public abstract class AbstractEclipseDiagramConfig extends AbstractDiagramConfig {
  @Override
  public void initialize(final XDomainObjectShape shape) {
    OpenElementInEditorBehavior _openElementInEditorBehavior = new OpenElementInEditorBehavior(shape);
    shape.addBehavior(_openElementInEditorBehavior);
  }
}
