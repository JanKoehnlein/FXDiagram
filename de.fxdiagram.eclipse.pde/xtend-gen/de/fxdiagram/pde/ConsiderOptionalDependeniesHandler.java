package de.fxdiagram.pde;

import de.fxdiagram.pde.BundleUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ToolItem;

@SuppressWarnings("all")
public class ConsiderOptionalDependeniesHandler extends AbstractHandler {
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    boolean _xblockexpression = false;
    {
      Object _trigger = event.getTrigger();
      final boolean selected = ((ToolItem) ((Event) _trigger).widget).getSelection();
      _xblockexpression = BundleUtil.setConsiderOptional(selected);
    }
    return Boolean.valueOf(_xblockexpression);
  }
}
