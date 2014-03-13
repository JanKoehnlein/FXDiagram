package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.CloseBehavior;
import de.fxdiagram.core.tools.actions.BehaviorProvider;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.Symbol;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class CloseAction implements DiagramAction {
  public boolean matches(final KeyEvent it) {
    KeyCode _code = it.getCode();
    return Objects.equal(_code, KeyCode.ESCAPE);
  }
  
  public Symbol.Type getSymbol() {
    return null;
  }
  
  public void perform(final XRoot root) {
    final Function1<CloseBehavior,Boolean> _function = new Function1<CloseBehavior,Boolean>() {
      public Boolean apply(final CloseBehavior it) {
        boolean _xblockexpression = false;
        {
          it.close();
          _xblockexpression = true;
        }
        return Boolean.valueOf(_xblockexpression);
      }
    };
    BehaviorProvider.<CloseBehavior>triggerBehavior(root, CloseBehavior.class, _function);
  }
}
