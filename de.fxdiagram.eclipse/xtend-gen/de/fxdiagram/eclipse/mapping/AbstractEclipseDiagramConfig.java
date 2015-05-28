package de.fxdiagram.eclipse.mapping;

import de.fxdiagram.core.XShape;
import de.fxdiagram.mapping.AbstractDiagramConfig;
import de.fxdiagram.mapping.behavior.OpenElementInEditorBehavior;

@SuppressWarnings("all")
public abstract class AbstractEclipseDiagramConfig extends AbstractDiagramConfig {
  @Override
  public void initialize(final XShape shape) {
    OpenElementInEditorBehavior _openElementInEditorBehavior = new OpenElementInEditorBehavior(shape);
    shape.addBehavior(_openElementInEditorBehavior);
  }
}
