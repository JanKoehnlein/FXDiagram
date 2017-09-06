package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.OpenBehavior;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class OpenAction implements DiagramAction {
  @Override
  public boolean matches(final KeyEvent it) {
    return (Objects.equal(it.getCode(), KeyCode.PERIOD) || Objects.equal(it.getCode(), KeyCode.ENTER));
  }
  
  @Override
  public SymbolType getSymbol() {
    return null;
  }
  
  @Override
  public String getTooltip() {
    return "Close node";
  }
  
  @Override
  public void perform(final XRoot root) {
    final Function1<XNode, Boolean> _function = (XNode it) -> {
      return Boolean.valueOf(it.getSelected());
    };
    final Iterable<XNode> selectedNodes = IterableExtensions.<XNode>filter(root.getDiagram().getNodes(), _function);
    int _size = IterableExtensions.size(selectedNodes);
    boolean _equals = (_size == 1);
    if (_equals) {
      OpenBehavior _behavior = IterableExtensions.<XNode>head(selectedNodes).<OpenBehavior>getBehavior(OpenBehavior.class);
      if (_behavior!=null) {
        _behavior.open();
      }
    }
  }
}
