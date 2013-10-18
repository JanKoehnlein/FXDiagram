package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.NavigationBehavior;
import de.fxdiagram.core.tools.actions.DiagramAction;

@SuppressWarnings("all")
public class NavigatePreviousAction implements DiagramAction {
  public void perform(final XRoot root) {
    XDiagram _diagram = root.getDiagram();
    NavigationBehavior _behavior = _diagram.<NavigationBehavior>getBehavior(NavigationBehavior.class);
    if (_behavior!=null) {
      _behavior.previous();
    }
  }
}
