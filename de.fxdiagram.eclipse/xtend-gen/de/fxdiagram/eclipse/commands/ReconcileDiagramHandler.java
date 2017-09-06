package de.fxdiagram.eclipse.commands;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.tools.actions.ReconcileAction;
import de.fxdiagram.eclipse.FXDiagramView;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

@SuppressWarnings("all")
public class ReconcileDiagramHandler extends AbstractHandler {
  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    Object _xblockexpression = null;
    {
      final IWorkbenchPart view = HandlerUtil.getActivePart(event);
      if ((view instanceof FXDiagramView)) {
        final XRoot root = ((FXDiagramView)view).getCurrentRoot();
        boolean _notEquals = (!Objects.equal(root, null));
        if (_notEquals) {
          new ReconcileAction().perform(((FXDiagramView)view).getCurrentRoot());
        }
      }
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
}
