package de.fxdiagram.core.anchors;

import javafx.geometry.Point2D;

@SuppressWarnings("all")
public class NearestPointFinder {
  private double refX;
  
  private double refY;
  
  private double currentDistanceSquared = Double.MAX_VALUE;
  
  private Point2D currentNearest;
  
  public NearestPointFinder(final double refX, final double refY) {
    this.refX = refX;
    this.refY = refY;
  }
  
  protected Point2D getCurrentNearest() {
    return this.currentNearest;
  }
  
  public Point2D addCandidate(final Point2D point) {
    Point2D _xblockexpression = null;
    {
      double _x = point.getX();
      double _minus = (_x - this.refX);
      double _x_1 = point.getX();
      double _minus_1 = (_x_1 - this.refX);
      double _multiply = (_minus * _minus_1);
      double _y = point.getY();
      double _minus_2 = (_y - this.refY);
      double _y_1 = point.getY();
      double _minus_3 = (_y_1 - this.refY);
      double _multiply_1 = (_minus_2 * _minus_3);
      final double distanceSquared = (_multiply + _multiply_1);
      Point2D _xifexpression = null;
      if ((distanceSquared < this.currentDistanceSquared)) {
        Point2D _xblockexpression_1 = null;
        {
          this.currentDistanceSquared = distanceSquared;
          _xblockexpression_1 = this.currentNearest = point;
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public Point2D addCandidate(final double px, final double py) {
    Point2D _xblockexpression = null;
    {
      final double distanceSquared = (((px - this.refX) * (px - this.refX)) + ((py - this.refY) * (py - this.refY)));
      Point2D _xifexpression = null;
      if ((distanceSquared < this.currentDistanceSquared)) {
        Point2D _xblockexpression_1 = null;
        {
          this.currentDistanceSquared = distanceSquared;
          Point2D _point2D = new Point2D(px, py);
          _xblockexpression_1 = this.currentNearest = _point2D;
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
}
