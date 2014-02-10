package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.Behavior;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class BehaviorProvider {
  public static <T extends Behavior> void triggerBehavior(final XRoot root, final Class<T> type, final Function1<? super T,? extends Boolean> exec) {
    XDiagram _diagram = root.getDiagram();
    ObservableList<XNode> _nodes = _diagram.getNodes();
    final Function1<XNode,Boolean> _function = new Function1<XNode,Boolean>() {
      public Boolean apply(final XNode it) {
        return Boolean.valueOf(it.getSelected());
      }
    };
    final Iterable<XNode> selectedNodes = IterableExtensions.<XNode>filter(_nodes, _function);
    int _size = IterableExtensions.size(selectedNodes);
    boolean _equals = (_size == 1);
    if (_equals) {
      XNode _head = IterableExtensions.<XNode>head(selectedNodes);
      final T behavior = _head.<T>getBehavior(type);
      boolean _notEquals = (!Objects.equal(behavior, null));
      if (_notEquals) {
        Boolean _apply = exec.apply(behavior);
        if ((_apply).booleanValue()) {
          return;
        }
      }
    }
    XDiagram _diagram_1 = root.getDiagram();
    final T behavior_1 = _diagram_1.<T>getBehavior(type);
    boolean _notEquals_1 = (!Objects.equal(behavior_1, null));
    if (_notEquals_1) {
      exec.apply(behavior_1);
    }
  }
}
