package de.fxdiagram.core.anchors;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.anchors.NearestPointFinder;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;

@SuppressWarnings("all")
public class RectangleAnchors implements Anchors {
  protected XNode host;
  
  public RectangleAnchors(final XNode host) {
    this.host = host;
  }
  
  public Point2D getAnchor(final double x, final double y) {
    Point2D _xblockexpression = null;
    {
      Node _node = this.host.getNode();
      Node _node_1 = this.host.getNode();
      Bounds _layoutBounds = _node_1.getLayoutBounds();
      final Bounds boundsInRootDiagram = CoreExtensions.localToRootDiagram(_node, _layoutBounds);
      double _minX = boundsInRootDiagram.getMinX();
      double _maxX = boundsInRootDiagram.getMaxX();
      double _plus = (_minX + _maxX);
      final double centerX = (0.5 * _plus);
      double _minY = boundsInRootDiagram.getMinY();
      double _maxY = boundsInRootDiagram.getMaxY();
      double _plus_1 = (_minY + _maxY);
      final double centerY = (0.5 * _plus_1);
      NearestPointFinder _nearestPointFinder = new NearestPointFinder(x, y);
      final NearestPointFinder finder = _nearestPointFinder;
      double _minus = (centerY - y);
      double _abs = Math.abs(_minus);
      boolean _greaterThan = (_abs > NumberExpressionExtensions.EPSILON);
      if (_greaterThan) {
        double _minY_1 = boundsInRootDiagram.getMinY();
        final double xTop = this.getXIntersection(_minY_1, centerX, centerY, x, y);
        boolean _and = false;
        double _minX_1 = boundsInRootDiagram.getMinX();
        boolean _greaterEqualsThan = (xTop >= _minX_1);
        if (!_greaterEqualsThan) {
          _and = false;
        } else {
          double _maxX_1 = boundsInRootDiagram.getMaxX();
          boolean _lessEqualsThan = (xTop <= _maxX_1);
          _and = (_greaterEqualsThan && _lessEqualsThan);
        }
        if (_and) {
          double _minY_2 = boundsInRootDiagram.getMinY();
          finder.addCandidate(xTop, _minY_2);
        }
        double _maxY_1 = boundsInRootDiagram.getMaxY();
        final double xBottom = this.getXIntersection(_maxY_1, centerX, centerY, x, y);
        boolean _and_1 = false;
        double _minX_2 = boundsInRootDiagram.getMinX();
        boolean _greaterEqualsThan_1 = (xBottom >= _minX_2);
        if (!_greaterEqualsThan_1) {
          _and_1 = false;
        } else {
          double _maxX_2 = boundsInRootDiagram.getMaxX();
          boolean _lessEqualsThan_1 = (xBottom <= _maxX_2);
          _and_1 = (_greaterEqualsThan_1 && _lessEqualsThan_1);
        }
        if (_and_1) {
          double _maxY_2 = boundsInRootDiagram.getMaxY();
          finder.addCandidate(xBottom, _maxY_2);
        }
      }
      double _minus_1 = (centerX - x);
      double _abs_1 = Math.abs(_minus_1);
      boolean _greaterThan_1 = (_abs_1 > NumberExpressionExtensions.EPSILON);
      if (_greaterThan_1) {
        double _minX_3 = boundsInRootDiagram.getMinX();
        final double yLeft = this.getYIntersection(_minX_3, centerX, centerY, x, y);
        boolean _and_2 = false;
        double _minY_3 = boundsInRootDiagram.getMinY();
        boolean _greaterEqualsThan_2 = (yLeft >= _minY_3);
        if (!_greaterEqualsThan_2) {
          _and_2 = false;
        } else {
          double _maxY_3 = boundsInRootDiagram.getMaxY();
          boolean _lessEqualsThan_2 = (yLeft <= _maxY_3);
          _and_2 = (_greaterEqualsThan_2 && _lessEqualsThan_2);
        }
        if (_and_2) {
          double _minX_4 = boundsInRootDiagram.getMinX();
          finder.addCandidate(_minX_4, yLeft);
        }
        double _maxX_3 = boundsInRootDiagram.getMaxX();
        final double yRight = this.getYIntersection(_maxX_3, centerX, centerY, x, y);
        boolean _and_3 = false;
        double _minY_4 = boundsInRootDiagram.getMinY();
        boolean _greaterEqualsThan_3 = (yRight >= _minY_4);
        if (!_greaterEqualsThan_3) {
          _and_3 = false;
        } else {
          double _maxY_4 = boundsInRootDiagram.getMaxY();
          boolean _lessEqualsThan_3 = (yRight <= _maxY_4);
          _and_3 = (_greaterEqualsThan_3 && _lessEqualsThan_3);
        }
        if (_and_3) {
          double _maxX_4 = boundsInRootDiagram.getMaxX();
          finder.addCandidate(_maxX_4, yRight);
        }
      }
      Point2D _currentNearest = finder.getCurrentNearest();
      _xblockexpression = (_currentNearest);
    }
    return _xblockexpression;
  }
  
  protected double getXIntersection(final double yIntersection, final double centerX, final double centerY, final double pointX, final double pointY) {
    double _xblockexpression = (double) 0;
    {
      double _minus = (yIntersection - centerY);
      double _minus_1 = (pointY - centerY);
      final double t = (_minus / _minus_1);
      double _minus_2 = (pointX - centerX);
      double _multiply = (_minus_2 * t);
      double _plus = (_multiply + centerX);
      _xblockexpression = (_plus);
    }
    return _xblockexpression;
  }
  
  protected double getYIntersection(final double xIntersection, final double centerX, final double centerY, final double pointX, final double pointY) {
    double _xblockexpression = (double) 0;
    {
      double _minus = (xIntersection - centerX);
      double _minus_1 = (pointX - centerX);
      final double t = (_minus / _minus_1);
      double _minus_2 = (pointY - centerY);
      double _multiply = (_minus_2 * t);
      double _plus = (_multiply + centerY);
      _xblockexpression = (_plus);
    }
    return _xblockexpression;
  }
}
