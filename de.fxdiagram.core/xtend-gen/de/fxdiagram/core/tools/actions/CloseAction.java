package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.CloseBehavior;
import de.fxdiagram.core.tools.actions.DiagramAction;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class CloseAction implements DiagramAction {
  public void perform(final XRoot root) {
    XDiagram _diagram = root.getDiagram();
    ObservableList<XNode> _nodes = _diagram.getNodes();
    final Function1<XNode,Boolean> _function = new Function1<XNode,Boolean>() {
      public Boolean apply(final XNode it) {
        boolean _selected = it.getSelected();
        return Boolean.valueOf(_selected);
      }
    };
    final Iterable<XNode> selectedNodes = IterableExtensions.<XNode>filter(_nodes, _function);
    int _size = IterableExtensions.size(selectedNodes);
    boolean _equals = (_size == 1);
    if (_equals) {
      XNode _head = IterableExtensions.<XNode>head(selectedNodes);
      final CloseBehavior closeBehavior = _head.<CloseBehavior>getBehavior(CloseBehavior.class);
      boolean _notEquals = (!Objects.equal(closeBehavior, null));
      if (_notEquals) {
        closeBehavior.close();
        return;
      }
    }
    XDiagram _diagram_1 = root.getDiagram();
    CloseBehavior _behavior = _diagram_1.<CloseBehavior>getBehavior(CloseBehavior.class);
    if (_behavior!=null) {
      _behavior.close();
    }
  }
}
