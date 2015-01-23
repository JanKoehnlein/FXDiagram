package de.fxdiagram.pde;

import de.fxdiagram.pde.BundleUtil;
import de.fxdiagram.pde.HandlerHelper;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

@SuppressWarnings("all")
public class ConsiderFragmentDependeniesHandler extends AbstractHandler {
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    boolean _isWidgetChecked = HandlerHelper.isWidgetChecked(event);
    return Boolean.valueOf(BundleUtil.setConsiderFragments(_isWidgetChecked));
  }
}
