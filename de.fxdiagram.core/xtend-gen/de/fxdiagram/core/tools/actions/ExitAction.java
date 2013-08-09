package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.tools.actions.DiagramAction;

@SuppressWarnings("all")
public class ExitAction implements DiagramAction {
  public void perform(final XRootDiagram rootDiagram) {
    System.exit(0);
  }
}
