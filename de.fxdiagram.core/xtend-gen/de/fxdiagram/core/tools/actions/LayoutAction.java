package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.tools.actions.DiagramAction;
import javafx.util.Duration;

@SuppressWarnings("all")
public class LayoutAction implements DiagramAction {
  public void perform(final XRoot root) {
    Layouter _layouter = new Layouter();
    XDiagram _diagram = root.getDiagram();
    Duration _millis = Duration.millis(1500);
    _layouter.layout(_diagram, _millis);
  }
}
