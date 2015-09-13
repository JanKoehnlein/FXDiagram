package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.CommandStack;
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
    boolean _and = false;
    boolean _isShortcutDown = it.isShortcutDown();
    if (!_isShortcutDown) {
      _and = false;
    } else {
      KeyCode _code = it.getCode();
      boolean _equals = Objects.equal(_code, KeyCode.L);
      _and = _equals;
    }
    return _and;
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
    Layouter _layouter = new Layouter();
    XDiagram _diagram = root.getDiagram();
    Duration _millis = Duration.millis(1000);
    final LazyCommand layoutCommand = _layouter.createLayoutCommand(this.layoutType, _diagram, _millis);
    CommandStack _commandStack = root.getCommandStack();
    _commandStack.execute(layoutCommand);
  }
}
