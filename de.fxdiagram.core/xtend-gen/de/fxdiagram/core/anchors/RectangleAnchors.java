package de.fxdiagram.core.anchors;

import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.AbstractAnchors;
import de.fxdiagram.core.anchors.NearestPointFinder;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Side;

@SuppressWarnings("all")
public class RectangleAnchors extends AbstractAnchors {
  public RectangleAnchors(final XNode host) {
    super(host);
  }
  
  @Override
  public Point2D getAnchor(final double x, final double y) {
    Point2D _xblockexpression = null;
    {
      final Bounds boundsInRootDiagram = this.getBoundsInRoot();
      boolean _equals = Objects.equal(boundsInRootDiagram, null);
      if (_equals) {
        return null;
      }
      double _minX = boundsInRootDiagram.getMinX();
      double _maxX = boundsInRootDiagram.getMaxX();
      double _plus = (_minX + _maxX);
      final double centerX = (0.5 * _plus);
      double _minY = boundsInRootDiagram.getMinY();
      double _maxY = boundsInRootDiagram.getMaxY();
      double _plus_1 = (_minY + _maxY);
      final double centerY = (0.5 * _plus_1);
      final NearestPointFinder finder = new NearestPointFinder(x, y);
      double _abs = Math.abs((centerY - y));
      boolean _greaterThan = (_abs > NumberExpressionExtensions.EPSILON);
      if (_greaterThan) {
        final double xTop = this.getXIntersection(boundsInRootDiagram.getMinY(), centerX, centerY, x, y);
        if (((xTop >= boundsInRootDiagram.getMinX()) && (xTop <= boundsInRootDiagram.getMaxX()))) {
          finder.addCandidate(xTop, boundsInRootDiagram.getMinY());
        }
        final double xBottom = this.getXIntersection(boundsInRootDiagram.getMaxY(), centerX, centerY, x, y);
        if (((xBottom >= boundsInRootDiagram.getMinX()) && (xBottom <= boundsInRootDiagram.getMaxX()))) {
          finder.addCandidate(xBottom, boundsInRootDiagram.getMaxY());
        }
      }
      double _abs_1 = Math.abs((centerX - x));
      boolean _greaterThan_1 = (_abs_1 > NumberExpressionExtensions.EPSILON);
      if (_greaterThan_1) {
        final double yLeft = this.getYIntersection(boundsInRootDiagram.getMinX(), centerX, centerY, x, y);
        if (((yLeft >= boundsInRootDiagram.getMinY()) && (yLeft <= boundsInRootDiagram.getMaxY()))) {
          finder.addCandidate(boundsInRootDiagram.getMinX(), yLeft);
        }
        final double yRight = this.getYIntersection(boundsInRootDiagram.getMaxX(), centerX, centerY, x, y);
        if (((yRight >= boundsInRootDiagram.getMinY()) && (yRight <= boundsInRootDiagram.getMaxY()))) {
          finder.addCandidate(boundsInRootDiagram.getMaxX(), yRight);
        }
      }
      _xblockexpression = finder.getCurrentNearest();
    }
    return _xblockexpression;
  }
  
  protected double getXIntersection(final double yIntersection, final double centerX, final double centerY, final double pointX, final double pointY) {
    double _xblockexpression = (double) 0;
    {
      final double t = ((yIntersection - centerY) / (pointY - centerY));
      _xblockexpression = (((pointX - centerX) * t) + centerX);
    }
    return _xblockexpression;
  }
  
  protected double getYIntersection(final double xIntersection, final double centerX, final double centerY, final double pointX, final double pointY) {
    double _xblockexpression = (double) 0;
    {
      final double t = ((xIntersection - centerX) / (pointX - centerX));
      _xblockexpression = (((pointY - centerY) * t) + centerY);
    }
    return _xblockexpression;
  }
  
  @Override
  public Point2D getManhattanAnchor(final double x, final double y, final Side side) {
    final Bounds bounds = this.getBoundsInRoot();
    if (side != null) {
      switch (side) {
        case TOP:
          double _max = Math.max(Math.min(x, bounds.getMaxX()), bounds.getMinX());
          double _minY = bounds.getMinY();
          return new Point2D(_max, _minY);
        case BOTTOM:
          double _max_1 = Math.max(Math.min(x, bounds.getMaxX()), bounds.getMinX());
          double _maxY = bounds.getMaxY();
          return new Point2D(_max_1, _maxY);
        case LEFT:
          double _minX = bounds.getMinX();
          double _max_2 = Math.max(Math.min(y, bounds.getMaxY()), bounds.getMinY());
          return new Point2D(_minX, _max_2);
        case RIGHT:
          double _maxX = bounds.getMaxX();
          double _max_3 = Math.max(Math.min(y, bounds.getMaxY()), bounds.getMinY());
          return new Point2D(_maxX, _max_3);
        default:
          break;
      }
    }
    return null;
  }
}
