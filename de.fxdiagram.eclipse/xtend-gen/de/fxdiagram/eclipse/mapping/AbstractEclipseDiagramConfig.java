package de.fxdiagram.eclipse.mapping;

import de.fxdiagram.core.XShape;
import de.fxdiagram.eclipse.behavior.OpenElementInEditorBehavior;
import de.fxdiagram.mapping.AbstractDiagramConfig;

@SuppressWarnings("all")
public abstract class AbstractEclipseDiagramConfig extends AbstractDiagramConfig {
  @Override
  public void initialize(final XShape shape) {
    OpenElementInEditorBehavior _openElementInEditorBehavior = new OpenElementInEditorBehavior(shape);
    shape.addBehavior(_openElementInEditorBehavior);
  }
}
