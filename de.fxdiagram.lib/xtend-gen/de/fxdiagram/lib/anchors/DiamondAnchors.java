package de.fxdiagram.lib.anchors;

import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.AbstractAnchors;
import de.fxdiagram.core.extensions.BoundsExtensions;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Side;

@SuppressWarnings("all")
public class DiamondAnchors extends AbstractAnchors {
  private final static int CORNER_DELTA = 4;
  
  public DiamondAnchors(final XNode host) {
    super(host);
  }
  
  @Override
  public Point2D getAnchor(final double x, final double y) {
    final Bounds bounds = this.getBoundsInRoot();
    boolean _equals = Objects.equal(bounds, null);
    if (_equals) {
      return null;
    }
    final Point2D center = BoundsExtensions.center(bounds);
    double _x = center.getX();
    double _minus = (_x - x);
    double _abs = Math.abs(_minus);
    boolean _lessThan = (_abs < DiamondAnchors.CORNER_DELTA);
    if (_lessThan) {
      double _y = center.getY();
      boolean _lessThan_1 = (_y < y);
      if (_lessThan_1) {
        double _x_1 = center.getX();
        double _maxY = bounds.getMaxY();
        return new Point2D(_x_1, _maxY);
      } else {
        double _x_2 = center.getX();
        double _minY = bounds.getMinY();
        return new Point2D(_x_2, _minY);
      }
    }
    double _y_1 = center.getY();
    double _minus_1 = (_y_1 - y);
    double _abs_1 = Math.abs(_minus_1);
    boolean _lessThan_2 = (_abs_1 < DiamondAnchors.CORNER_DELTA);
    if (_lessThan_2) {
      double _x_3 = center.getX();
      boolean _lessThan_3 = (_x_3 < x);
      if (_lessThan_3) {
        double _maxX = bounds.getMaxX();
        double _y_2 = center.getY();
        return new Point2D(_maxX, _y_2);
      } else {
        double _minX = bounds.getMinX();
        double _y_3 = center.getY();
        return new Point2D(_minX, _y_3);
      }
    } else {
      double _x_4 = center.getX();
      boolean _lessThan_4 = (_x_4 < x);
      if (_lessThan_4) {
        double _y_4 = center.getY();
        boolean _lessThan_5 = (_y_4 < y);
        if (_lessThan_5) {
          double _x_5 = center.getX();
          double _maxY_1 = bounds.getMaxY();
          double _maxX_1 = bounds.getMaxX();
          double _y_5 = center.getY();
          double _x_6 = center.getX();
          double _y_6 = center.getY();
          return this.getCrossing(_x_5, _maxY_1, _maxX_1, _y_5, x, y, _x_6, _y_6);
        } else {
          double _x_7 = center.getX();
          double _minY_1 = bounds.getMinY();
          double _maxX_2 = bounds.getMaxX();
          double _y_7 = center.getY();
          double _x_8 = center.getX();
          double _y_8 = center.getY();
          return this.getCrossing(_x_7, _minY_1, _maxX_2, _y_7, x, y, _x_8, _y_8);
        }
      } else {
        double _y_9 = center.getY();
        boolean _lessThan_6 = (_y_9 < y);
        if (_lessThan_6) {
          double _x_9 = center.getX();
          double _maxY_2 = bounds.getMaxY();
          double _minX_1 = bounds.getMinX();
          double _y_10 = center.getY();
          double _x_10 = center.getX();
          double _y_11 = center.getY();
          return this.getCrossing(_x_9, _maxY_2, _minX_1, _y_10, x, y, _x_10, _y_11);
        } else {
          double _x_11 = center.getX();
          double _minY_2 = bounds.getMinY();
          double _minX_2 = bounds.getMinX();
          double _y_12 = center.getY();
          double _x_12 = center.getX();
          double _y_13 = center.getY();
          return this.getCrossing(_x_11, _minY_2, _minX_2, _y_12, x, y, _x_12, _y_13);
        }
      }
    }
  }
  
  protected Point2D getCrossing(final double xa, final double ya, final double xb, final double yb, final double xc, final double yc, final double xd, final double yd) {
    final double lambda = ((((ya - yc) * (xb - xa)) - ((xa - xc) * (yb - ya))) / (((yd - yc) * (xb - xa)) - ((xd - xc) * (yb - ya))));
    return new Point2D((xc + (lambda * (xd - xc))), (yc + (lambda * (yd - yc))));
  }
  
  @Override
  public Point2D getManhattanAnchor(final double x, final double y, final Side side) {
    final Bounds bounds = this.getBoundsInRoot();
    boolean _equals = Objects.equal(bounds, null);
    if (_equals) {
      return null;
    }
    final Point2D center = BoundsExtensions.center(bounds);
    if (side != null) {
      switch (side) {
        case TOP:
          double _x = center.getX();
          boolean _lessThan = (x < _x);
          if (_lessThan) {
            double _minX = bounds.getMinX();
            double _y = center.getY();
            double _x_1 = center.getX();
            double _minY = bounds.getMinY();
            return this.getCrossing(_minX, _y, _x_1, _minY, x, y, x, (y + 10));
          } else {
            double _maxX = bounds.getMaxX();
            double _y_1 = center.getY();
            double _x_2 = center.getX();
            double _minY_1 = bounds.getMinY();
            return this.getCrossing(_maxX, _y_1, _x_2, _minY_1, x, y, x, (y + 10));
          }
        case BOTTOM:
          double _x_3 = center.getX();
          boolean _lessThan_1 = (x < _x_3);
          if (_lessThan_1) {
            double _minX_1 = bounds.getMinX();
            double _y_2 = center.getY();
            double _x_4 = center.getX();
            double _maxY = bounds.getMaxY();
            return this.getCrossing(_minX_1, _y_2, _x_4, _maxY, x, y, x, (y + 10));
          } else {
            double _maxX_1 = bounds.getMaxX();
            double _y_3 = center.getY();
            double _x_5 = center.getX();
            double _maxY_1 = bounds.getMaxY();
            return this.getCrossing(_maxX_1, _y_3, _x_5, _maxY_1, x, y, x, (y + 10));
          }
        case LEFT:
          double _y_4 = center.getY();
          boolean _lessThan_2 = (y < _y_4);
          if (_lessThan_2) {
            double _minX_2 = bounds.getMinX();
            double _y_5 = center.getY();
            double _x_6 = center.getX();
            double _minY_2 = bounds.getMinY();
            return this.getCrossing(_minX_2, _y_5, _x_6, _minY_2, x, y, (x + 10), y);
          } else {
            double _minX_3 = bounds.getMinX();
            double _y_6 = center.getY();
            double _x_7 = center.getX();
            double _maxY_2 = bounds.getMaxY();
            return this.getCrossing(_minX_3, _y_6, _x_7, _maxY_2, x, y, (x + 10), y);
          }
        case RIGHT:
          double _y_7 = center.getY();
          boolean _lessThan_3 = (y < _y_7);
          if (_lessThan_3) {
            double _maxX_2 = bounds.getMaxX();
            double _y_8 = center.getY();
            double _x_8 = center.getX();
            double _minY_3 = bounds.getMinY();
            return this.getCrossing(_maxX_2, _y_8, _x_8, _minY_3, x, y, (x + 10), y);
          } else {
            double _maxX_3 = bounds.getMaxX();
            double _y_9 = center.getY();
            double _x_9 = center.getX();
            double _maxY_3 = bounds.getMaxY();
            return this.getCrossing(_maxX_3, _y_9, _x_9, _maxY_3, x, y, (x + 10), y);
          }
        default:
          break;
      }
    }
    return null;
  }
}
