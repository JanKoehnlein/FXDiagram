package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.Symbol;
import java.util.function.Consumer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@SuppressWarnings("all")
public class SelectAllAction implements DiagramAction {
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
  
  public Symbol.Type getSymbol() {
    return Symbol.Type.SELECTION1;
  }
  
  public String getTooltip() {
    return "Select all";
  }
  
  public void perform(final XRoot root) {
    XDiagram _diagram = root.getDiagram();
    Iterable<XShape> _allShapes = _diagram.getAllShapes();
    final Consumer<XShape> _function = new Consumer<XShape>() {
      public void accept(final XShape it) {
        boolean _isSelectable = it.isSelectable();
        if (_isSelectable) {
          it.setSelected(true);
        }
      }
    };
    _allShapes.forEach(_function);
  }
}
