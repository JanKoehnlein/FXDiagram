package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.LazyCommand;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

@SuppressWarnings("all")
public class LayoutAction implements DiagramAction {
  private LayoutType layoutType;
  
  public LayoutAction(final LayoutType layoutType) {
    this.layoutType = layoutType;
  }
  
  @Override
  public boolean matches(final KeyEvent it) {
    return (it.isShortcutDown() && Objects.equal(it.getCode(), KeyCode.L));
  }
  
  @Override
  public SymbolType getSymbol() {
    return SymbolType.GRAPH;
  }
  
  @Override
  public String getTooltip() {
    return "Layout diagram";
  }
  
  @Override
  public void perform(final XRoot root) {
    final LazyCommand layoutCommand = new Layouter().createLayoutCommand(root.getDiagram(), Duration.millis(1000));
    root.getCommandStack().execute(layoutCommand);
  }
}
