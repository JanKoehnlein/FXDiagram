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
      final double abx = Point2DExtensions.linear(c.getStartX(), c.getControlX1(), t);
      final double aby = Point2DExtensions.linear(c.getStartY(), c.getControlY1(), t);
      final double bcx = Point2DExtensions.linear(c.getControlX1(), c.getControlX2(), t);
      final double bcy = Point2DExtensions.linear(c.getControlY1(), c.getControlY2(), t);
      final double cdx = Point2DExtensions.linear(c.getControlX2(), c.getEndX(), t);
      final double cdy = Point2DExtensions.linear(c.getControlY2(), c.getEndY(), t);
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
      final double abx = Point2DExtensions.linear(c.getStartX(), c.getControlX(), t);
      final double aby = Point2DExtensions.linear(c.getStartY(), c.getControlY(), t);
      final double bcx = Point2DExtensions.linear(c.getControlX(), c.getEndX(), t);
      final double bcy = Point2DExtensions.linear(c.getControlY(), c.getEndY(), t);
      double _linear = Point2DExtensions.linear(abx, bcx, t);
      double _linear_1 = Point2DExtensions.linear(aby, bcy, t);
      _xblockexpression = new Point2D(_linear, _linear_1);
    }
    return _xblockexpression;
  }
  
  public static Point2D derivativeAt(final CubicCurve c, final double t) {
    Point2D _xblockexpression = null;
    {
      final double abx = Point2DExtensions.linear(c.getStartX(), c.getControlX1(), t);
      final double aby = Point2DExtensions.linear(c.getStartY(), c.getControlY1(), t);
      final double bcx = Point2DExtensions.linear(c.getControlX1(), c.getControlX2(), t);
      final double bcy = Point2DExtensions.linear(c.getControlY1(), c.getControlY2(), t);
      final double cdx = Point2DExtensions.linear(c.getControlX2(), c.getEndX(), t);
      final double cdy = Point2DExtensions.linear(c.getControlY2(), c.getEndY(), t);
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
      final double abx = Point2DExtensions.linear(c.getStartX(), c.getControlX(), t);
      final double aby = Point2DExtensions.linear(c.getStartY(), c.getControlY(), t);
      final double bcx = Point2DExtensions.linear(c.getControlX(), c.getEndX(), t);
      final double bcy = Point2DExtensions.linear(c.getControlY(), c.getEndY(), t);
      _xblockexpression = new Point2D((bcx - abx), (bcy - aby));
    }
    return _xblockexpression;
  }
  
  public static List<CubicCurve> splitAt(final CubicCurve c, final double t) {
    final double abx = Point2DExtensions.linear(c.getStartX(), c.getControlX1(), t);
    final double aby = Point2DExtensions.linear(c.getStartY(), c.getControlY1(), t);
    final double bcx = Point2DExtensions.linear(c.getControlX1(), c.getControlX2(), t);
    final double bcy = Point2DExtensions.linear(c.getControlY1(), c.getControlY2(), t);
    final double cdx = Point2DExtensions.linear(c.getControlX2(), c.getEndX(), t);
    final double cdy = Point2DExtensions.linear(c.getControlY2(), c.getEndY(), t);
    final double acx = Point2DExtensions.linear(abx, bcx, t);
    final double acy = Point2DExtensions.linear(aby, bcy, t);
    final double bdx = Point2DExtensions.linear(bcx, cdx, t);
    final double bdy = Point2DExtensions.linear(bcy, cdy, t);
    final double splitX = Point2DExtensions.linear(acx, bdx, t);
    final double splitY = Point2DExtensions.linear(acy, bdy, t);
    double _startX = c.getStartX();
    double _startY = c.getStartY();
    CubicCurve _cubicCurve = new CubicCurve(_startX, _startY, abx, aby, acx, acy, splitX, splitY);
    double _endX = c.getEndX();
    double _endY = c.getEndY();
    CubicCurve _cubicCurve_1 = new CubicCurve(splitX, splitY, bdx, bdy, cdx, cdy, _endX, _endY);
    return Collections.<CubicCurve>unmodifiableList(CollectionLiterals.<CubicCurve>newArrayList(_cubicCurve, _cubicCurve_1));
  }
  
  public static List<QuadCurve> splitAt(final QuadCurve c, final double t) {
    final double abx = Point2DExtensions.linear(c.getStartX(), c.getControlX(), t);
    final double aby = Point2DExtensions.linear(c.getStartY(), c.getControlY(), t);
    final double bcx = Point2DExtensions.linear(c.getControlX(), c.getEndX(), t);
    final double bcy = Point2DExtensions.linear(c.getControlY(), c.getEndY(), t);
    final double splitX = Point2DExtensions.linear(abx, bcx, t);
    final double splitY = Point2DExtensions.linear(aby, bcy, t);
    double _startX = c.getStartX();
    double _startY = c.getStartY();
    QuadCurve _quadCurve = new QuadCurve(_startX, _startY, abx, aby, splitX, splitY);
    double _endX = c.getEndX();
    double _endY = c.getEndY();
    QuadCurve _quadCurve_1 = new QuadCurve(splitX, splitY, bcx, bcy, _endX, _endY);
    return Collections.<QuadCurve>unmodifiableList(CollectionLiterals.<QuadCurve>newArrayList(_quadCurve, _quadCurve_1));
  }
}
