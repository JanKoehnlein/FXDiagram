package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XRoot;
import eu.hansolo.enzo.radialmenu.Symbol;
import javafx.scene.input.KeyEvent;

/**
 * Some action that can happen on an {@link XDiagram}.
 * 
 * Usually triggered by the diagram's menu or a keystroke.
 */
@SuppressWarnings("all")
public interface DiagramAction {
  public abstract boolean matches(final KeyEvent event);
  
  public abstract Symbol.Type getSymbol();
  
  public abstract String getTooltip();
  
  public abstract void perform(final XRoot root);
}
