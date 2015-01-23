package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.NavigationBehavior;
import de.fxdiagram.core.tools.actions.BehaviorProvider;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.Symbol;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class NavigatePreviousAction implements DiagramAction {
  public boolean matches(final KeyEvent it) {
    boolean _or = false;
    KeyCode _code = it.getCode();
    boolean _equals = Objects.equal(_code, KeyCode.LEFT);
    if (_equals) {
      _or = true;
    } else {
      KeyCode _code_1 = it.getCode();
      boolean _equals_1 = Objects.equal(_code_1, KeyCode.PAGE_UP);
      _or = _equals_1;
    }
    return _or;
  }
  
  public Symbol.Type getSymbol() {
    return null;
  }
  
  public String getTooltip() {
    return "Previous";
  }
  
  public void perform(final XRoot root) {
    final Function1<NavigationBehavior, Boolean> _function = new Function1<NavigationBehavior, Boolean>() {
      public Boolean apply(final NavigationBehavior it) {
        return Boolean.valueOf(it.previous());
      }
    };
    BehaviorProvider.<NavigationBehavior>triggerBehavior(root, NavigationBehavior.class, _function);
  }
}
