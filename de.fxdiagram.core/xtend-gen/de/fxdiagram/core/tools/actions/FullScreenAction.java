package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

@SuppressWarnings("all")
public class FullScreenAction implements DiagramAction {
  @Override
  public boolean matches(final KeyEvent it) {
    return ((it.isShortcutDown() && it.isShiftDown()) && Objects.equal(it.getCode(), KeyCode.F));
  }
  
  @Override
  public SymbolType getSymbol() {
    return null;
  }
  
  @Override
  public String getTooltip() {
    return "Toggle full screen mode";
  }
  
  @Override
  public void perform(final XRoot root) {
    Scene _scene = root.getScene();
    final Window window = _scene.getWindow();
    if ((window instanceof Stage)) {
      ((Stage)window).setFullScreen(true);
      return;
    }
  }
}
