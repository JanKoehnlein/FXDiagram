package de.fxdiagram.eclipse.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import de.fxdiagram.eclipse.FXDiagramView;

@SuppressWarnings("all")
public class LinkWithEditorHandler extends AbstractHandler {
  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    Object _xblockexpression = null;
    {
      final IWorkbenchPart view = HandlerUtil.getActivePart(event);
      if ((view instanceof FXDiagramView)) {
        Command _command = event.getCommand();
        final boolean oldState = HandlerUtil.toggleCommandState(_command);
        ((FXDiagramView)view).setLinkWithEditor((!oldState));
      }
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
}
