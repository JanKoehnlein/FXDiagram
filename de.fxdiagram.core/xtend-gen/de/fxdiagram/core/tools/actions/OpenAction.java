package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.OpenBehavior;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.Symbol;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class OpenAction implements DiagramAction {
  @Override
  public boolean matches(final KeyEvent it) {
    boolean _or = false;
    KeyCode _code = it.getCode();
    boolean _equals = Objects.equal(_code, KeyCode.PERIOD);
    if (_equals) {
      _or = true;
    } else {
      KeyCode _code_1 = it.getCode();
      boolean _equals_1 = Objects.equal(_code_1, KeyCode.ENTER);
      _or = _equals_1;
    }
    return _or;
  }
  
  @Override
  public Symbol.Type getSymbol() {
    return null;
  }
  
  @Override
  public String getTooltip() {
    return "Close node";
  }
  
  @Override
  public void perform(final XRoot root) {
    XDiagram _diagram = root.getDiagram();
    ObservableList<XNode> _nodes = _diagram.getNodes();
    final Function1<XNode, Boolean> _function = (XNode it) -> {
      return Boolean.valueOf(it.getSelected());
    };
    final Iterable<XNode> selectedNodes = IterableExtensions.<XNode>filter(_nodes, _function);
    int _size = IterableExtensions.size(selectedNodes);
    boolean _equals = (_size == 1);
    if (_equals) {
      XNode _head = IterableExtensions.<XNode>head(selectedNodes);
      OpenBehavior _behavior = _head.<OpenBehavior>getBehavior(OpenBehavior.class);
      if (_behavior!=null) {
        _behavior.open();
      }
    }
  }
}
