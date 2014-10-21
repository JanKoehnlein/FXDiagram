package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.Symbol;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@SuppressWarnings("all")
public class ExitAction implements DiagramAction {
  public boolean matches(final KeyEvent it) {
    boolean _and = false;
    boolean _isShortcutDown = it.isShortcutDown();
    if (!_isShortcutDown) {
      _and = false;
    } else {
      KeyCode _code = it.getCode();
      boolean _equals = Objects.equal(_code, KeyCode.Q);
      _and = _equals;
    }
    return _and;
  }
  
  public Symbol.Type getSymbol() {
    return Symbol.Type.EJECT;
  }
  
  public String getTooltip() {
    return "Exit FXDiagram";
  }
  
  public void perform(final XRoot root) {
    System.exit(0);
  }
}
