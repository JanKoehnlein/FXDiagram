package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.tools.actions.DiagramAction;
import javafx.util.Duration;

@SuppressWarnings("all")
public class LayoutAction implements DiagramAction {
  public void perform(final XRoot root) {
    Layouter _layouter = new Layouter();
    XRootDiagram _diagram = root.getDiagram();
    Duration _seconds = Duration.seconds(2);
    _layouter.layout(_diagram, _seconds);
  }
}
