package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.NavigationBehavior;
import de.fxdiagram.core.tools.actions.BehaviorProvider;
import de.fxdiagram.core.tools.actions.DiagramAction;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class NavigatePreviousAction implements DiagramAction {
  public void perform(final XRoot root) {
    final Function1<NavigationBehavior,Boolean> _function = new Function1<NavigationBehavior,Boolean>() {
      public Boolean apply(final NavigationBehavior it) {
        return Boolean.valueOf(it.previous());
      }
    };
    BehaviorProvider.<NavigationBehavior>triggerBehavior(root, NavigationBehavior.class, _function);
  }
}
