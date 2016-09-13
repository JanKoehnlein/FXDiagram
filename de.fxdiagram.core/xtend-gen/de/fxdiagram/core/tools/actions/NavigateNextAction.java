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
    return (Objects.equal(it.getCode(), KeyCode.RIGHT) || Objects.equal(it.getCode(), KeyCode.PAGE_DOWN));
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
