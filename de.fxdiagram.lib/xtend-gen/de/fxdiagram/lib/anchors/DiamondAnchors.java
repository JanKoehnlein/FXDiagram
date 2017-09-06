package de.fxdiagram.lib.anchors;

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
    if ((bounds == null)) {
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
          return this.getCrossing(center.getX(), bounds.getMaxY(), bounds.getMaxX(), center.getY(), x, y, center.getX(), center.getY());
        } else {
          return this.getCrossing(center.getX(), bounds.getMinY(), bounds.getMaxX(), center.getY(), x, y, center.getX(), center.getY());
        }
      } else {
        double _y_5 = center.getY();
        boolean _lessThan_6 = (_y_5 < y);
        if (_lessThan_6) {
          return this.getCrossing(center.getX(), bounds.getMaxY(), bounds.getMinX(), center.getY(), x, y, center.getX(), center.getY());
        } else {
          return this.getCrossing(center.getX(), bounds.getMinY(), bounds.getMinX(), center.getY(), x, y, center.getX(), center.getY());
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
    if ((bounds == null)) {
      return null;
    }
    final Point2D center = BoundsExtensions.center(bounds);
    if (side != null) {
      switch (side) {
        case TOP:
          double _x = center.getX();
          boolean _lessThan = (x < _x);
          if (_lessThan) {
            return this.getCrossing(bounds.getMinX(), center.getY(), center.getX(), bounds.getMinY(), x, y, x, (y + 10));
          } else {
            return this.getCrossing(bounds.getMaxX(), center.getY(), center.getX(), bounds.getMinY(), x, y, x, (y + 10));
          }
        case BOTTOM:
          double _x_1 = center.getX();
          boolean _lessThan_1 = (x < _x_1);
          if (_lessThan_1) {
            return this.getCrossing(bounds.getMinX(), center.getY(), center.getX(), bounds.getMaxY(), x, y, x, (y + 10));
          } else {
            return this.getCrossing(bounds.getMaxX(), center.getY(), center.getX(), bounds.getMaxY(), x, y, x, (y + 10));
          }
        case LEFT:
          double _y = center.getY();
          boolean _lessThan_2 = (y < _y);
          if (_lessThan_2) {
            return this.getCrossing(bounds.getMinX(), center.getY(), center.getX(), bounds.getMinY(), x, y, (x + 10), y);
          } else {
            return this.getCrossing(bounds.getMinX(), center.getY(), center.getX(), bounds.getMaxY(), x, y, (x + 10), y);
          }
        case RIGHT:
          double _y_1 = center.getY();
          boolean _lessThan_3 = (y < _y_1);
          if (_lessThan_3) {
            return this.getCrossing(bounds.getMaxX(), center.getY(), center.getX(), bounds.getMinY(), x, y, (x + 10), y);
          } else {
            return this.getCrossing(bounds.getMaxX(), center.getY(), center.getX(), bounds.getMaxY(), x, y, (x + 10), y);
          }
        default:
          break;
      }
    }
    return null;
  }
}
