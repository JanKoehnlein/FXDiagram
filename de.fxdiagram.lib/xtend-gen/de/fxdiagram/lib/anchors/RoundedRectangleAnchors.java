package de.fxdiagram.lib.anchors;

import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.RectangleAnchors;
import de.fxdiagram.core.extensions.CoreExtensions;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
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
      boolean _equals = Objects.equal(rectAnchor, null);
      if (_equals) {
        return null;
      }
      Node _node = this.host.getNode();
      Node _node_1 = this.host.getNode();
      Bounds _layoutBounds = _node_1.getLayoutBounds();
      final Bounds boundsInRootDiagram = CoreExtensions.localToRootDiagram(_node, _layoutBounds);
      boolean _equals_1 = Objects.equal(boundsInRootDiagram, null);
      if (_equals_1) {
        return null;
      }
      Node _node_2 = this.host.getNode();
      BoundingBox _boundingBox = new BoundingBox(0, 0, this.radiusX, this.radiusY);
      final Bounds radiusBounds = CoreExtensions.localToRootDiagram(_node_2, _boundingBox);
      double _width = radiusBounds.getWidth();
      double _height = radiusBounds.getHeight();
      final Dimension2D radiusInRootDiagram = new Dimension2D(_width, _height);
      Point2D _xifexpression = null;
      boolean _and = false;
      double _x = rectAnchor.getX();
      double _minX = boundsInRootDiagram.getMinX();
      double _width_1 = radiusInRootDiagram.getWidth();
      double _plus = (_minX + _width_1);
      boolean _lessThan = (_x < _plus);
      if (!_lessThan) {
        _and = false;
      } else {
        double _y = rectAnchor.getY();
        double _minY = boundsInRootDiagram.getMinY();
        double _height_1 = radiusInRootDiagram.getHeight();
        double _plus_1 = (_minY + _height_1);
        boolean _lessThan_1 = (_y < _plus_1);
        _and = _lessThan_1;
      }
      if (_and) {
        double _x_1 = rectAnchor.getX();
        double _y_1 = rectAnchor.getY();
        double _minX_1 = boundsInRootDiagram.getMinX();
        double _width_2 = radiusInRootDiagram.getWidth();
        double _plus_2 = (_minX_1 + _width_2);
        double _minY_1 = boundsInRootDiagram.getMinY();
        double _height_2 = radiusInRootDiagram.getHeight();
        double _plus_3 = (_minY_1 + _height_2);
        _xifexpression = this.getPointOnCircle(_x_1, _y_1, _plus_2, _plus_3, radiusInRootDiagram);
      } else {
        Point2D _xifexpression_1 = null;
        boolean _and_1 = false;
        double _x_2 = rectAnchor.getX();
        double _maxX = boundsInRootDiagram.getMaxX();
        double _width_3 = radiusInRootDiagram.getWidth();
        double _minus = (_maxX - _width_3);
        boolean _greaterThan = (_x_2 > _minus);
        if (!_greaterThan) {
          _and_1 = false;
        } else {
          double _y_2 = rectAnchor.getY();
          double _minY_2 = boundsInRootDiagram.getMinY();
          double _height_3 = radiusInRootDiagram.getHeight();
          double _plus_4 = (_minY_2 + _height_3);
          boolean _lessThan_2 = (_y_2 < _plus_4);
          _and_1 = _lessThan_2;
        }
        if (_and_1) {
          double _x_3 = rectAnchor.getX();
          double _y_3 = rectAnchor.getY();
          double _maxX_1 = boundsInRootDiagram.getMaxX();
          double _width_4 = radiusInRootDiagram.getWidth();
          double _minus_1 = (_maxX_1 - _width_4);
          double _minY_3 = boundsInRootDiagram.getMinY();
          double _height_4 = radiusInRootDiagram.getHeight();
          double _plus_5 = (_minY_3 + _height_4);
          _xifexpression_1 = this.getPointOnCircle(_x_3, _y_3, _minus_1, _plus_5, radiusInRootDiagram);
        } else {
          Point2D _xifexpression_2 = null;
          boolean _and_2 = false;
          double _x_4 = rectAnchor.getX();
          double _minX_2 = boundsInRootDiagram.getMinX();
          double _width_5 = radiusInRootDiagram.getWidth();
          double _plus_6 = (_minX_2 + _width_5);
          boolean _lessThan_3 = (_x_4 < _plus_6);
          if (!_lessThan_3) {
            _and_2 = false;
          } else {
            double _y_4 = rectAnchor.getY();
            double _maxY = boundsInRootDiagram.getMaxY();
            double _height_5 = radiusInRootDiagram.getHeight();
            double _minus_2 = (_maxY - _height_5);
            boolean _greaterThan_1 = (_y_4 > _minus_2);
            _and_2 = _greaterThan_1;
          }
          if (_and_2) {
            double _x_5 = rectAnchor.getX();
            double _y_5 = rectAnchor.getY();
            double _minX_3 = boundsInRootDiagram.getMinX();
            double _width_6 = radiusInRootDiagram.getWidth();
            double _plus_7 = (_minX_3 + _width_6);
            double _maxY_1 = boundsInRootDiagram.getMaxY();
            double _height_6 = radiusInRootDiagram.getHeight();
            double _minus_3 = (_maxY_1 - _height_6);
            _xifexpression_2 = this.getPointOnCircle(_x_5, _y_5, _plus_7, _minus_3, radiusInRootDiagram);
          } else {
            Point2D _xifexpression_3 = null;
            boolean _and_3 = false;
            double _x_6 = rectAnchor.getX();
            double _maxX_2 = boundsInRootDiagram.getMaxX();
            double _width_7 = radiusInRootDiagram.getWidth();
            double _minus_4 = (_maxX_2 - _width_7);
            boolean _greaterThan_2 = (_x_6 > _minus_4);
            if (!_greaterThan_2) {
              _and_3 = false;
            } else {
              double _y_6 = rectAnchor.getY();
              double _maxY_2 = boundsInRootDiagram.getMaxY();
              double _height_7 = radiusInRootDiagram.getHeight();
              double _minus_5 = (_maxY_2 - _height_7);
              boolean _greaterThan_3 = (_y_6 > _minus_5);
              _and_3 = _greaterThan_3;
            }
            if (_and_3) {
              double _x_7 = rectAnchor.getX();
              double _y_7 = rectAnchor.getY();
              double _maxX_3 = boundsInRootDiagram.getMaxX();
              double _width_8 = radiusInRootDiagram.getWidth();
              double _minus_6 = (_maxX_3 - _width_8);
              double _maxY_3 = boundsInRootDiagram.getMaxY();
              double _height_8 = radiusInRootDiagram.getHeight();
              double _minus_7 = (_maxY_3 - _height_8);
              _xifexpression_3 = this.getPointOnCircle(_x_7, _y_7, _minus_6, _minus_7, radiusInRootDiagram);
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
}
