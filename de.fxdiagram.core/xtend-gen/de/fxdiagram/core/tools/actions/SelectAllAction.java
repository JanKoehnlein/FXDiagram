package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@SuppressWarnings("all")
public class SelectAllAction implements DiagramAction {
  @Override
  public boolean matches(final KeyEvent it) {
    boolean _and = false;
    boolean _isShortcutDown = it.isShortcutDown();
    if (!_isShortcutDown) {
      _and = false;
    } else {
      KeyCode _code = it.getCode();
      boolean _equals = Objects.equal(_code, KeyCode.A);
      _and = _equals;
    }
    return _and;
  }
  
  @Override
  public SymbolType getSymbol() {
    return SymbolType.SELECTION1;
  }
  
  @Override
  public String getTooltip() {
    return "Select all";
  }
  
  @Override
  public void perform(final XRoot root) {
    XDiagram _diagram = root.getDiagram();
    ObservableList<XConnection> _connections = _diagram.getConnections();
    XDiagram _diagram_1 = root.getDiagram();
    ObservableList<XNode> _nodes = _diagram_1.getNodes();
    Iterable<XDomainObjectShape> _plus = Iterables.<XDomainObjectShape>concat(_connections, _nodes);
    final Consumer<XDomainObjectShape> _function = (XDomainObjectShape it) -> {
      boolean _isSelectable = it.isSelectable();
      if (_isSelectable) {
        it.setSelected(true);
      }
    };
    _plus.forEach(_function);
  }
}
