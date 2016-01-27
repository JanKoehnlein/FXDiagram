package de.fxdiagram.core.auxlines;

import de.fxdiagram.core.XShape;
import javafx.scene.Node;

@SuppressWarnings("all")
public abstract class AuxiliaryLine {
  private double position;
  
  private XShape[] relatedShapes;
  
  public AuxiliaryLine(final double position, final XShape[] relatedShapes) {
    this.position = position;
    this.relatedShapes = relatedShapes;
  }
  
  public double getPosition() {
    return this.position;
  }
  
  public XShape[] getRelatedShapes() {
    return this.relatedShapes;
  }
  
  public abstract Node createNode();
}
