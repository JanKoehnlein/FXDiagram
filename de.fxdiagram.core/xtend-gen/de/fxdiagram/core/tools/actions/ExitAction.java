package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@SuppressWarnings("all")
public class ExitAction implements DiagramAction {
  @Override
  public boolean matches(final KeyEvent it) {
    return (it.isShortcutDown() && Objects.equal(it.getCode(), KeyCode.Q));
  }
  
  @Override
  public SymbolType getSymbol() {
    return SymbolType.EJECT;
  }
  
  @Override
  public String getTooltip() {
    return "Exit FXDiagram";
  }
  
  @Override
  public void perform(final XRoot root) {
    System.exit(0);
  }
}
