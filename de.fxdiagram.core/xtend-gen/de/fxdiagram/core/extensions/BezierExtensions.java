package de.fxdiagram.core.extensions;

import de.fxdiagram.core.extensions.Point2DExtensions;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.QuadCurve;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

/**
 * De-Casteljau-algorithm
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
      _xblockexpression = new Point2D(_linear, _linear_1);
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
      _xblockexpression = new Point2D(_linear, _linear_1);
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
      _xblockexpression = new Point2D((bdx - acx), (bdy - acy));
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
      _xblockexpression = new Point2D((bcx - abx), (bcy - aby));
    }
    return _xblockexpression;
  }
  
  public static List<CubicCurve> splitAt(final CubicCurve c, final double t) {
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
    final double splitX = Point2DExtensions.linear(acx, bdx, t);
    final double splitY = Point2DExtensions.linear(acy, bdy, t);
    double _startX_1 = c.getStartX();
    double _startY_1 = c.getStartY();
    CubicCurve _cubicCurve = new CubicCurve(_startX_1, _startY_1, abx, aby, acx, acy, splitX, splitY);
    double _endX_1 = c.getEndX();
    double _endY_1 = c.getEndY();
    CubicCurve _cubicCurve_1 = new CubicCurve(splitX, splitY, bdx, bdy, cdx, cdy, _endX_1, _endY_1);
    return Collections.<CubicCurve>unmodifiableList(CollectionLiterals.<CubicCurve>newArrayList(_cubicCurve, _cubicCurve_1));
  }
  
  public static List<QuadCurve> splitAt(final QuadCurve c, final double t) {
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
    final double splitX = Point2DExtensions.linear(abx, bcx, t);
    final double splitY = Point2DExtensions.linear(aby, bcy, t);
    double _startX_1 = c.getStartX();
    double _startY_1 = c.getStartY();
    QuadCurve _quadCurve = new QuadCurve(_startX_1, _startY_1, abx, aby, splitX, splitY);
    double _endX_1 = c.getEndX();
    double _endY_1 = c.getEndY();
    QuadCurve _quadCurve_1 = new QuadCurve(splitX, splitY, bcx, bcy, _endX_1, _endY_1);
    return Collections.<QuadCurve>unmodifiableList(CollectionLiterals.<QuadCurve>newArrayList(_quadCurve, _quadCurve_1));
  }
}
