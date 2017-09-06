package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import javafx.scene.input.KeyEvent;

@SuppressWarnings("all")
public class UndoAction implements DiagramAction {
  @Override
  public boolean matches(final KeyEvent it) {
    return ((it.isShortcutDown() && (!it.isShiftDown())) && Objects.equal(it.getCode().toString().toLowerCase(), "z"));
  }
  
  @Override
  public SymbolType getSymbol() {
    return SymbolType.REWIND;
  }
  
  @Override
  public String getTooltip() {
    return "Undo";
  }
  
  @Override
  public void perform(final XRoot root) {
    root.getCommandStack().undo();
  }
}
