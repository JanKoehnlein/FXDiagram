package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.tools.actions.DiagramAction;

@SuppressWarnings("all")
public class RedoAction implements DiagramAction {
  public void perform(final XRoot root) {
    CommandStack _commandStack = root.getCommandStack();
    _commandStack.redo();
  }
}
