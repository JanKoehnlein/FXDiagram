package de.fxdiagram.core.anchors;

import com.google.common.base.Objects;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class DiamondAnchors implements Anchors {
  private final static int CORNER_DELTA = 4;
  
  private final Shape shape;
  
  @Override
  public Point2D getAnchor(final double x, final double y) {
    Bounds _boundsInLocal = this.shape.getBoundsInLocal();
    final Bounds bounds = CoreExtensions.localToRootDiagram(this.shape, _boundsInLocal);
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
  
  public DiamondAnchors(final Shape shape) {
    super();
    this.shape = shape;
  }
}
