package de.fxdiagram.lib.chooser;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.lib.chooser.AbstractBaseChooser;
import de.fxdiagram.lib.chooser.ChoiceGraphics;
import javafx.geometry.Point2D;

@SuppressWarnings("all")
public class NodeChooser extends AbstractBaseChooser {
  private XDiagram diagram;
  
  private Point2D position;
  
  public NodeChooser(final XDiagram diagram, final Point2D position, final ChoiceGraphics graphics, final boolean isVertical) {
    super(graphics, isVertical);
    this.diagram = diagram;
    this.position = position;
  }
  
  @Override
  public XRoot getRoot() {
    return CoreExtensions.getRoot(this.diagram);
  }
  
  @Override
  public XDiagram getDiagram() {
    return this.diagram;
  }
  
  @Override
  public Point2D getPosition() {
    return this.position;
  }
}
