package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.tools.actions.DiagramAction;

@SuppressWarnings("all")
public class ExitAction implements DiagramAction {
  public void perform(final XRoot root) {
    System.exit(0);
  }
}
