package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.Behavior;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class BehaviorProvider {
  public static <T extends Behavior> void triggerBehavior(final XRoot root, final Class<T> type, final Function1<? super T, ? extends Boolean> exec) {
    final Function1<XNode, Boolean> _function = (XNode it) -> {
      return Boolean.valueOf(it.getSelected());
    };
    final Iterable<XNode> selectedNodes = IterableExtensions.<XNode>filter(root.getDiagram().getNodes(), _function);
    int _size = IterableExtensions.size(selectedNodes);
    boolean _equals = (_size == 1);
    if (_equals) {
      final T behavior = IterableExtensions.<XNode>head(selectedNodes).<T>getBehavior(type);
      boolean _notEquals = (!Objects.equal(behavior, null));
      if (_notEquals) {
        Boolean _apply = exec.apply(behavior);
        if ((_apply).booleanValue()) {
          return;
        }
      }
    }
    final T behavior_1 = root.getDiagram().<T>getBehavior(type);
    boolean _notEquals_1 = (!Objects.equal(behavior_1, null));
    if (_notEquals_1) {
      exec.apply(behavior_1);
    }
  }
}
