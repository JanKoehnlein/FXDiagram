package de.fxdiagram.lib.anchors;

import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class EllipseAnchors implements Anchors {
  private final XNode host;
  
  @Override
  public Point2D getAnchor(final double x, final double y) {
    Node _node = this.host.getNode();
    Node _node_1 = this.host.getNode();
    Bounds _boundsInLocal = _node_1.getBoundsInLocal();
    final Bounds bounds = CoreExtensions.localToRootDiagram(_node, _boundsInLocal);
    boolean _equals = Objects.equal(bounds, null);
    if (_equals) {
      return null;
    }
    final Point2D center = BoundsExtensions.center(bounds);
    double _y = center.getY();
    double _minus = (y - _y);
    double _x = center.getX();
    double _minus_1 = (x - _x);
    final double angle = Math.atan2(_minus, _minus_1);
    double _width = bounds.getWidth();
    double _multiply = (0.5 * _width);
    double _cos = Math.cos(angle);
    double _multiply_1 = (_multiply * _cos);
    double _height = bounds.getHeight();
    double _multiply_2 = (0.5 * _height);
    double _sin = Math.sin(angle);
    double _multiply_3 = (_multiply_2 * _sin);
    Point2D _point2D = new Point2D(_multiply_1, _multiply_3);
    return Point2DExtensions.operator_plus(center, _point2D);
  }
  
  public EllipseAnchors(final XNode host) {
    super();
    this.host = host;
  }
}
