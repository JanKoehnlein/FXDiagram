package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.tools.actions.DiagramAction;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class SelectAllAction implements DiagramAction {
  public void perform(final XRootDiagram diagram) {
    Iterable<? extends XShape> _allShapes = diagram.getAllShapes();
    final Procedure1<XShape> _function = new Procedure1<XShape>() {
      public void apply(final XShape it) {
        boolean _isSelectable = it.isSelectable();
        if (_isSelectable) {
          it.setSelected(true);
        }
      }
    };
    IterableExtensions.forEach(_allShapes, _function);
  }
}
