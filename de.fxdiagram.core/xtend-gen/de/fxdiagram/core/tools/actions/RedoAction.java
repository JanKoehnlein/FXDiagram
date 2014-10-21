package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.Symbol;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@SuppressWarnings("all")
public class RedoAction implements DiagramAction {
  public boolean matches(final KeyEvent it) {
    boolean _and = false;
    boolean _and_1 = false;
    boolean _isShortcutDown = it.isShortcutDown();
    if (!_isShortcutDown) {
      _and_1 = false;
    } else {
      boolean _isShiftDown = it.isShiftDown();
      _and_1 = _isShiftDown;
    }
    if (!_and_1) {
      _and = false;
    } else {
      KeyCode _code = it.getCode();
      boolean _equals = Objects.equal(_code, KeyCode.Y);
      _and = _equals;
    }
    return _and;
  }
  
  public Symbol.Type getSymbol() {
    return Symbol.Type.FORWARD;
  }
  
  public String getTooltip() {
    return "Redo";
  }
  
  public void perform(final XRoot root) {
    CommandStack _commandStack = root.getCommandStack();
    _commandStack.redo();
  }
}
