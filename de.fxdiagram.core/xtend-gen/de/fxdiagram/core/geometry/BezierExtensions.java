package de.fxdiagram.core.geometry;

import de.fxdiagram.core.geometry.Point2DExtensions;
import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.QuadCurve;

/**
 * De Casteljau algorithm
 */
@SuppressWarnings("all")
public class BezierExtensions {
  public static Point2D at(final CubicCurve c, final double t) {
    Point2D _xblockexpression = null;
    {
      double _startX = c.getStartX();
      double _controlX1 = c.getControlX1();
      final double abx = Point2DExtensions.linear(_startX, _controlX1, t);
      double _startY = c.getStartY();
      double _controlY1 = c.getControlY1();
      final double aby = Point2DExtensions.linear(_startY, _controlY1, t);
      double _controlX1_1 = c.getControlX1();
      double _controlX2 = c.getControlX2();
      final double bcx = Point2DExtensions.linear(_controlX1_1, _controlX2, t);
      double _controlY1_1 = c.getControlY1();
      double _controlY2 = c.getControlY2();
      final double bcy = Point2DExtensions.linear(_controlY1_1, _controlY2, t);
      double _controlX2_1 = c.getControlX2();
      double _endX = c.getEndX();
      final double cdx = Point2DExtensions.linear(_controlX2_1, _endX, t);
      double _controlY2_1 = c.getControlY2();
      double _endY = c.getEndY();
      final double cdy = Point2DExtensions.linear(_controlY2_1, _endY, t);
      final double acx = Point2DExtensions.linear(abx, bcx, t);
      final double acy = Point2DExtensions.linear(aby, bcy, t);
      final double bdx = Point2DExtensions.linear(bcx, cdx, t);
      final double bdy = Point2DExtensions.linear(bcy, cdy, t);
      double _linear = Point2DExtensions.linear(acx, bdx, t);
      double _linear_1 = Point2DExtensions.linear(acy, bdy, t);
      Point2D _point2D = new Point2D(_linear, _linear_1);
      _xblockexpression = (_point2D);
    }
    return _xblockexpression;
  }
  
  public static Point2D at(final QuadCurve c, final double t) {
    Point2D _xblockexpression = null;
    {
      double _startX = c.getStartX();
      double _controlX = c.getControlX();
      final double abx = Point2DExtensions.linear(_startX, _controlX, t);
      double _startY = c.getStartY();
      double _controlY = c.getControlY();
      final double aby = Point2DExtensions.linear(_startY, _controlY, t);
      double _controlX_1 = c.getControlX();
      double _endX = c.getEndX();
      final double bcx = Point2DExtensions.linear(_controlX_1, _endX, t);
      double _controlY_1 = c.getControlY();
      double _endY = c.getEndY();
      final double bcy = Point2DExtensions.linear(_controlY_1, _endY, t);
      double _linear = Point2DExtensions.linear(abx, bcx, t);
      double _linear_1 = Point2DExtensions.linear(aby, bcy, t);
      Point2D _point2D = new Point2D(_linear, _linear_1);
      _xblockexpression = (_point2D);
    }
    return _xblockexpression;
  }
  
  public static Point2D derivativeAt(final CubicCurve c, final double t) {
    Point2D _xblockexpression = null;
    {
      double _startX = c.getStartX();
      double _controlX1 = c.getControlX1();
      final double abx = Point2DExtensions.linear(_startX, _controlX1, t);
      double _startY = c.getStartY();
      double _controlY1 = c.getControlY1();
      final double aby = Point2DExtensions.linear(_startY, _controlY1, t);
      double _controlX1_1 = c.getControlX1();
      double _controlX2 = c.getControlX2();
      final double bcx = Point2DExtensions.linear(_controlX1_1, _controlX2, t);
      double _controlY1_1 = c.getControlY1();
      double _controlY2 = c.getControlY2();
      final double bcy = Point2DExtensions.linear(_controlY1_1, _controlY2, t);
      double _controlX2_1 = c.getControlX2();
      double _endX = c.getEndX();
      final double cdx = Point2DExtensions.linear(_controlX2_1, _endX, t);
      double _controlY2_1 = c.getControlY2();
      double _endY = c.getEndY();
      final double cdy = Point2DExtensions.linear(_controlY2_1, _endY, t);
      final double acx = Point2DExtensions.linear(abx, bcx, t);
      final double acy = Point2DExtensions.linear(aby, bcy, t);
      final double bdx = Point2DExtensions.linear(bcx, cdx, t);
      final double bdy = Point2DExtensions.linear(bcy, cdy, t);
      double _minus = (bdx - acx);
      double _minus_1 = (bdy - acy);
      Point2D _point2D = new Point2D(_minus, _minus_1);
      _xblockexpression = (_point2D);
    }
    return _xblockexpression;
  }
  
  public static Point2D derivativeAt(final QuadCurve c, final double t) {
    Point2D _xblockexpression = null;
    {
      double _startX = c.getStartX();
      double _controlX = c.getControlX();
      final double abx = Point2DExtensions.linear(_startX, _controlX, t);
      double _startY = c.getStartY();
      double _controlY = c.getControlY();
      final double aby = Point2DExtensions.linear(_startY, _controlY, t);
      double _controlX_1 = c.getControlX();
      double _endX = c.getEndX();
      final double bcx = Point2DExtensions.linear(_controlX_1, _endX, t);
      double _controlY_1 = c.getControlY();
      double _endY = c.getEndY();
      final double bcy = Point2DExtensions.linear(_controlY_1, _endY, t);
      double _minus = (bcx - abx);
      double _minus_1 = (bcy - aby);
      Point2D _point2D = new Point2D(_minus, _minus_1);
      _xblockexpression = (_point2D);
    }
    return _xblockexpression;
  }
}
