package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.CloseBehavior;
import de.fxdiagram.core.tools.actions.BehaviorProvider;
import de.fxdiagram.core.tools.actions.DiagramAction;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class CloseAction implements DiagramAction {
  public void perform(final XRoot root) {
    final Function1<CloseBehavior,Boolean> _function = new Function1<CloseBehavior,Boolean>() {
      public Boolean apply(final CloseBehavior it) {
        boolean _xblockexpression = false;
        {
          it.close();
          _xblockexpression = (true);
        }
        return Boolean.valueOf(_xblockexpression);
      }
    };
    BehaviorProvider.<CloseBehavior>triggerBehavior(root, CloseBehavior.class, _function);
  }
}
