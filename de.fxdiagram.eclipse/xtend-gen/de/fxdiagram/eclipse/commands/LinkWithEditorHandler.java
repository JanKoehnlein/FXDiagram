package de.fxdiagram.eclipse.commands;

import de.fxdiagram.eclipse.FXDiagramView;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

@SuppressWarnings("all")
public class LinkWithEditorHandler extends AbstractHandler {
  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    Object _xblockexpression = null;
    {
      final IWorkbenchPart view = HandlerUtil.getActivePart(event);
      if ((view instanceof FXDiagramView)) {
        final boolean oldState = HandlerUtil.toggleCommandState(event.getCommand());
        ((FXDiagramView)view).setLinkWithEditor((!oldState));
      }
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
}
