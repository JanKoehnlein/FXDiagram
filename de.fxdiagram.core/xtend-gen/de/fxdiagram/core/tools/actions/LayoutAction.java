package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.tools.actions.DiagramAction;
import javafx.util.Duration;

@SuppressWarnings("all")
public class LayoutAction implements DiagramAction {
  private LayoutType layoutType;
  
  public LayoutAction(final LayoutType layoutType) {
    this.layoutType = layoutType;
  }
  
  public void perform(final XRoot root) {
    Layouter _layouter = new Layouter();
    XDiagram _diagram = root.getDiagram();
    Duration _millis = Duration.millis(1000);
    _layouter.layout(this.layoutType, _diagram, _millis);
  }
}
