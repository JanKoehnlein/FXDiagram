package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.NavigationBehavior;
import de.fxdiagram.core.tools.actions.BehaviorProvider;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class NavigateNextAction implements DiagramAction {
  @Override
  public boolean matches(final KeyEvent it) {
    boolean _or = false;
    KeyCode _code = it.getCode();
    boolean _equals = Objects.equal(_code, KeyCode.RIGHT);
    if (_equals) {
      _or = true;
    } else {
      KeyCode _code_1 = it.getCode();
      boolean _equals_1 = Objects.equal(_code_1, KeyCode.PAGE_DOWN);
      _or = _equals_1;
    }
    return _or;
  }
  
  @Override
  public SymbolType getSymbol() {
    return null;
  }
  
  @Override
  public String getTooltip() {
    return "Next";
  }
  
  @Override
  public void perform(final XRoot root) {
    final Function1<NavigationBehavior, Boolean> _function = (NavigationBehavior it) -> {
      return Boolean.valueOf(it.next());
    };
    BehaviorProvider.<NavigationBehavior>triggerBehavior(root, NavigationBehavior.class, _function);
  }
}
