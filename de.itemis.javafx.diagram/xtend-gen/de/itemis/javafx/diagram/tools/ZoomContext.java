package de.itemis.javafx.diagram.tools;

import javafx.geometry.Point2D;

@SuppressWarnings("all")
public class ZoomContext {
  private double _previousScale = 1;
  
  public double getPreviousScale() {
    return this._previousScale;
  }
  
  public void setPreviousScale(final double previousScale) {
    this._previousScale = previousScale;
  }
  
  private Point2D _pivotInDiagram;
  
  public Point2D getPivotInDiagram() {
    return this._pivotInDiagram;
  }
  
  public void setPivotInDiagram(final Point2D pivotInDiagram) {
    this._pivotInDiagram = pivotInDiagram;
  }
  
  public ZoomContext(final Point2D pivotInDiagram) {
    this.setPivotInDiagram(pivotInDiagram);
  }
}
