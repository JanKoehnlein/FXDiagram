package de.fxdiagram.pde;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;

@SuppressWarnings("all")
class HandlerHelper {
  public static boolean isWidgetChecked(final ExecutionEvent event) {
    boolean _xblockexpression = false;
    {
      Object _trigger = event.getTrigger();
      final Widget widget = ((Event) _trigger).widget;
      boolean _switchResult = false;
      boolean _matched = false;
      if (!_matched) {
        if (widget instanceof MenuItem) {
          _matched=true;
          _switchResult = ((MenuItem)widget).getSelection();
        }
      }
      if (!_matched) {
        if (widget instanceof ToolItem) {
          _matched=true;
          _switchResult = ((ToolItem)widget).getSelection();
        }
      }
      if (!_matched) {
        _switchResult = false;
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
}
