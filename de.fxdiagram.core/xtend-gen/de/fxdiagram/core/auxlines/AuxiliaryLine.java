package de.fxdiagram.core.auxlines;

import de.fxdiagram.core.XNode;
import javafx.scene.Node;

@SuppressWarnings("all")
public abstract class AuxiliaryLine {
  private double position;
  
  private XNode[] relatedNodes;
  
  public AuxiliaryLine(final double position, final XNode[] relatedNodes) {
    this.position = position;
    this.relatedNodes = relatedNodes;
  }
  
  public double getPosition() {
    return this.position;
  }
  
  public XNode[] getRelatedNodes() {
    return this.relatedNodes;
  }
  
  public abstract Node createNode();
}
