package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@SuppressWarnings("all")
public class RedoAction implements DiagramAction {
  @Override
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
      String _string = _code.toString();
      String _lowerCase = _string.toLowerCase();
      boolean _equals = Objects.equal(_lowerCase, "z");
      _and = _equals;
    }
    return _and;
  }
  
  @Override
  public SymbolType getSymbol() {
    return SymbolType.FORWARD;
  }
  
  @Override
  public String getTooltip() {
    return "Redo";
  }
  
  @Override
  public void perform(final XRoot root) {
    CommandStack _commandStack = root.getCommandStack();
    _commandStack.redo();
  }
}
