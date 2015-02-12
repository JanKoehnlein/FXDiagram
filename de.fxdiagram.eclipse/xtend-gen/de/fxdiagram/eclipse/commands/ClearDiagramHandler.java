package de.fxdiagram.eclipse.commands;

import de.fxdiagram.eclipse.FXDiagramView;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("all")
public class ClearDiagramHandler extends AbstractHandler {
  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    Object _xblockexpression = null;
    {
      IWorkbench _workbench = PlatformUI.getWorkbench();
      IWorkbenchWindow _activeWorkbenchWindow = _workbench.getActiveWorkbenchWindow();
      IWorkbenchPage _activePage = _activeWorkbenchWindow.getActivePage();
      final IViewPart view = _activePage.findView("de.fxdiagram.eclipse.FXDiagramView");
      if ((view instanceof FXDiagramView)) {
        ((FXDiagramView)view).clear();
      }
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
}
