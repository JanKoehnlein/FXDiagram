package de.fxdiagram.lib.anchors;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.RectangleAnchors;
import de.fxdiagram.core.extensions.CoreExtensions;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Node;

@SuppressWarnings("all")
public class RoundedRectangleAnchors extends RectangleAnchors {
  private double radiusX;
  
  private double radiusY;
  
  public RoundedRectangleAnchors(final XNode host, final double radiusX, final double radiusY) {
    super(host);
    this.radiusX = radiusX;
    this.radiusY = radiusY;
  }
  
  @Override
  public Point2D getAnchor(final double x, final double y) {
    Point2D _xblockexpression = null;
    {
      final Point2D rectAnchor = super.getAnchor(x, y);
      if ((rectAnchor == null)) {
        return null;
      }
      final Bounds boundsInRootDiagram = this.getBoundsInRoot();
      if ((boundsInRootDiagram == null)) {
        return null;
      }
      Node _node = this.host.getNode();
      BoundingBox _boundingBox = new BoundingBox(0, 0, this.radiusX, this.radiusY);
      final Bounds radiusBounds = CoreExtensions.localToRootDiagram(_node, _boundingBox);
      double _width = radiusBounds.getWidth();
      double _height = radiusBounds.getHeight();
      final Dimension2D radiusInRootDiagram = new Dimension2D(_width, _height);
      Point2D _xifexpression = null;
      if (((rectAnchor.getX() < (boundsInRootDiagram.getMinX() + radiusInRootDiagram.getWidth())) && 
        (rectAnchor.getY() < (boundsInRootDiagram.getMinY() + radiusInRootDiagram.getHeight())))) {
        double _x = rectAnchor.getX();
        double _y = rectAnchor.getY();
        double _minX = boundsInRootDiagram.getMinX();
        double _width_1 = radiusInRootDiagram.getWidth();
        double _plus = (_minX + _width_1);
        double _minY = boundsInRootDiagram.getMinY();
        double _height_1 = radiusInRootDiagram.getHeight();
        double _plus_1 = (_minY + _height_1);
        _xifexpression = this.getPointOnCircle(_x, _y, _plus, _plus_1, radiusInRootDiagram);
      } else {
        Point2D _xifexpression_1 = null;
        if (((rectAnchor.getX() > (boundsInRootDiagram.getMaxX() - radiusInRootDiagram.getWidth())) && 
          (rectAnchor.getY() < (boundsInRootDiagram.getMinY() + radiusInRootDiagram.getHeight())))) {
          double _x_1 = rectAnchor.getX();
          double _y_1 = rectAnchor.getY();
          double _maxX = boundsInRootDiagram.getMaxX();
          double _width_2 = radiusInRootDiagram.getWidth();
          double _minus = (_maxX - _width_2);
          double _minY_1 = boundsInRootDiagram.getMinY();
          double _height_2 = radiusInRootDiagram.getHeight();
          double _plus_2 = (_minY_1 + _height_2);
          _xifexpression_1 = this.getPointOnCircle(_x_1, _y_1, _minus, _plus_2, radiusInRootDiagram);
        } else {
          Point2D _xifexpression_2 = null;
          if (((rectAnchor.getX() < (boundsInRootDiagram.getMinX() + radiusInRootDiagram.getWidth())) && 
            (rectAnchor.getY() > (boundsInRootDiagram.getMaxY() - radiusInRootDiagram.getHeight())))) {
            double _x_2 = rectAnchor.getX();
            double _y_2 = rectAnchor.getY();
            double _minX_1 = boundsInRootDiagram.getMinX();
            double _width_3 = radiusInRootDiagram.getWidth();
            double _plus_3 = (_minX_1 + _width_3);
            double _maxY = boundsInRootDiagram.getMaxY();
            double _height_3 = radiusInRootDiagram.getHeight();
            double _minus_1 = (_maxY - _height_3);
            _xifexpression_2 = this.getPointOnCircle(_x_2, _y_2, _plus_3, _minus_1, radiusInRootDiagram);
          } else {
            Point2D _xifexpression_3 = null;
            if (((rectAnchor.getX() > (boundsInRootDiagram.getMaxX() - radiusInRootDiagram.getWidth())) && 
              (rectAnchor.getY() > (boundsInRootDiagram.getMaxY() - radiusInRootDiagram.getHeight())))) {
              double _x_3 = rectAnchor.getX();
              double _y_3 = rectAnchor.getY();
              double _maxX_1 = boundsInRootDiagram.getMaxX();
              double _width_4 = radiusInRootDiagram.getWidth();
              double _minus_2 = (_maxX_1 - _width_4);
              double _maxY_1 = boundsInRootDiagram.getMaxY();
              double _height_4 = radiusInRootDiagram.getHeight();
              double _minus_3 = (_maxY_1 - _height_4);
              _xifexpression_3 = this.getPointOnCircle(_x_3, _y_3, _minus_2, _minus_3, radiusInRootDiagram);
            } else {
              _xifexpression_3 = rectAnchor;
            }
            _xifexpression_2 = _xifexpression_3;
          }
          _xifexpression_1 = _xifexpression_2;
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public Point2D getPointOnCircle(final double x, final double y, final double centerX, final double centerY, final Dimension2D radius) {
    Point2D _xblockexpression = null;
    {
      final double angle = Math.atan2((y - centerY), (x - centerX));
      double _cos = Math.cos(angle);
      double _width = radius.getWidth();
      double _multiply = (_cos * _width);
      double _plus = (centerX + _multiply);
      double _sin = Math.sin(angle);
      double _height = radius.getHeight();
      double _multiply_1 = (_sin * _height);
      double _plus_1 = (centerY + _multiply_1);
      _xblockexpression = new Point2D(_plus, _plus_1);
    }
    return _xblockexpression;
  }
  
  @Override
  public Point2D getManhattanAnchor(final double x, final double y, final Side side) {
    final Point2D rectAnchor = super.getManhattanAnchor(x, y, side);
    if ((rectAnchor == null)) {
      return null;
    }
    final Bounds bounds = this.getBoundsInRoot();
    Node _node = this.host.getNode();
    BoundingBox _boundingBox = new BoundingBox(0, 0, this.radiusX, this.radiusY);
    final Bounds radius = CoreExtensions.localToRootDiagram(_node, _boundingBox);
    if (side != null) {
      switch (side) {
        case TOP:
          if (((bounds.getMinX() < x) && (x < (bounds.getMinX() + radius.getWidth())))) {
            double _minY = bounds.getMinY();
            double _height = radius.getHeight();
            double _plus = (_minY + _height);
            double _minX = bounds.getMinX();
            double _width = radius.getWidth();
            double _plus_1 = (_minX + _width);
            double _minus = (_plus_1 - x);
            double _y = this.getY(_minus, radius);
            double _minus_1 = (_plus - _y);
            return new Point2D(x, _minus_1);
          } else {
            if ((((bounds.getMaxX() - radius.getWidth()) < x) && (x < bounds.getMaxX()))) {
              double _minY_1 = bounds.getMinY();
              double _height_1 = radius.getHeight();
              double _plus_2 = (_minY_1 + _height_1);
              double _maxX = bounds.getMaxX();
              double _width_1 = radius.getWidth();
              double _minus_2 = (_maxX - _width_1);
              double _minus_3 = (_minus_2 - x);
              double _y_1 = this.getY(_minus_3, radius);
              double _minus_4 = (_plus_2 - _y_1);
              return new Point2D(x, _minus_4);
            }
          }
          break;
        case BOTTOM:
          if (((bounds.getMinX() < x) && (x < (bounds.getMinX() + radius.getWidth())))) {
            double _maxY = bounds.getMaxY();
            double _height_2 = radius.getHeight();
            double _minus_5 = (_maxY - _height_2);
            double _minX_1 = bounds.getMinX();
            double _width_2 = radius.getWidth();
            double _plus_3 = (_minX_1 + _width_2);
            double _minus_6 = (_plus_3 - x);
            double _y_2 = this.getY(_minus_6, radius);
            double _plus_4 = (_minus_5 + _y_2);
            return new Point2D(x, _plus_4);
          } else {
            if ((((bounds.getMaxX() - radius.getWidth()) < x) && (x < bounds.getMaxX()))) {
              double _maxY_1 = bounds.getMaxY();
              double _height_3 = radius.getHeight();
              double _minus_7 = (_maxY_1 - _height_3);
              double _maxX_1 = bounds.getMaxX();
              double _width_3 = radius.getWidth();
              double _minus_8 = (_maxX_1 - _width_3);
              double _minus_9 = (_minus_8 - x);
              double _y_3 = this.getY(_minus_9, radius);
              double _plus_5 = (_minus_7 + _y_3);
              return new Point2D(x, _plus_5);
            }
          }
          break;
        case LEFT:
          if (((bounds.getMinY() < y) && (y < (bounds.getMinY() + radius.getHeight())))) {
            double _minX_2 = bounds.getMinX();
            double _width_4 = radius.getWidth();
            double _plus_6 = (_minX_2 + _width_4);
            double _minY_2 = bounds.getMinY();
            double _height_4 = radius.getHeight();
            double _plus_7 = (_minY_2 + _height_4);
            double _minus_10 = (_plus_7 - y);
            double _x = this.getX(_minus_10, radius);
            double _minus_11 = (_plus_6 - _x);
            return new Point2D(_minus_11, y);
          } else {
            if ((((bounds.getMaxY() - radius.getHeight()) < y) && (y < bounds.getMaxY()))) {
              double _minX_3 = bounds.getMinX();
              double _width_5 = radius.getWidth();
              double _plus_8 = (_minX_3 + _width_5);
              double _maxY_2 = bounds.getMaxY();
              double _height_5 = radius.getHeight();
              double _minus_12 = (_maxY_2 - _height_5);
              double _minus_13 = (_minus_12 - y);
              double _x_1 = this.getX(_minus_13, radius);
              double _minus_14 = (_plus_8 - _x_1);
              return new Point2D(_minus_14, y);
            }
          }
          break;
        case RIGHT:
          if (((bounds.getMinY() < y) && (y < (bounds.getMinY() + radius.getHeight())))) {
            double _maxX_2 = bounds.getMaxX();
            double _width_6 = radius.getWidth();
            double _minus_15 = (_maxX_2 - _width_6);
            double _minY_3 = bounds.getMinY();
            double _height_6 = radius.getHeight();
            double _plus_9 = (_minY_3 + _height_6);
            double _minus_16 = (_plus_9 - y);
            double _x_2 = this.getX(_minus_16, radius);
            double _plus_10 = (_minus_15 + _x_2);
            return new Point2D(_plus_10, y);
          } else {
            if ((((bounds.getMaxY() - radius.getHeight()) < y) && (y < bounds.getMaxY()))) {
              double _maxX_3 = bounds.getMaxX();
              double _width_7 = radius.getWidth();
              double _minus_17 = (_maxX_3 - _width_7);
              double _maxY_3 = bounds.getMaxY();
              double _height_7 = radius.getHeight();
              double _minus_18 = (_maxY_3 - _height_7);
              double _minus_19 = (_minus_18 - y);
              double _x_3 = this.getX(_minus_19, radius);
              double _plus_11 = (_minus_17 + _x_3);
              return new Point2D(_plus_11, y);
            }
          }
          break;
        default:
          break;
      }
    }
    return rectAnchor;
  }
  
  protected double getX(final double y, final Bounds radius) {
    double _width = radius.getWidth();
    double _height = radius.getHeight();
    double _height_1 = radius.getHeight();
    double _multiply = (_height * _height_1);
    double _divide = ((y * y) / _multiply);
    double _minus = (1 - _divide);
    double _sqrt = Math.sqrt(_minus);
    return (_width * _sqrt);
  }
  
  protected double getY(final double x, final Bounds radius) {
    double _height = radius.getHeight();
    double _width = radius.getWidth();
    double _width_1 = radius.getWidth();
    double _multiply = (_width * _width_1);
    double _divide = ((x * x) / _multiply);
    double _minus = (1 - _divide);
    double _sqrt = Math.sqrt(_minus);
    return (_height * _sqrt);
  }
}
