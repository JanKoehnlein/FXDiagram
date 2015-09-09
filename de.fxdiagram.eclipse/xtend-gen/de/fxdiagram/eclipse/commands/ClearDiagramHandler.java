package de.fxdiagram.eclipse.commands;

import de.fxdiagram.eclipse.FXDiagramView;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

@SuppressWarnings("all")
public class ClearDiagramHandler extends AbstractHandler {
  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    Object _xblockexpression = null;
    {
      final IWorkbenchPart view = HandlerUtil.getActivePart(event);
      if ((view instanceof FXDiagramView)) {
        ((FXDiagramView)view).clear();
      }
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
}
