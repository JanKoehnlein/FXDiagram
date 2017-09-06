package de.fxdiagram.core.anchors;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.anchors.ManhattanAnchors;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;

@FinalFieldsConstructor
@SuppressWarnings("all")
public abstract class AbstractAnchors implements Anchors, ManhattanAnchors {
  protected final XNode host;
  
  protected Point2D getDefaultAnchor(final Bounds bounds, final Side side) {
    Point2D _xblockexpression = null;
    {
      final Point2D center = BoundsExtensions.center(bounds);
      Point2D _switchResult = null;
      if (side != null) {
        switch (side) {
          case TOP:
            double _x = center.getX();
            double _minY = bounds.getMinY();
            _switchResult = new Point2D(_x, _minY);
            break;
          case BOTTOM:
            double _x_1 = center.getX();
            double _maxY = bounds.getMaxY();
            _switchResult = new Point2D(_x_1, _maxY);
            break;
          case LEFT:
            double _minX = bounds.getMinX();
            double _y = center.getY();
            _switchResult = new Point2D(_minX, _y);
            break;
          case RIGHT:
            double _maxX = bounds.getMaxX();
            double _y_1 = center.getY();
            _switchResult = new Point2D(_maxX, _y_1);
            break;
          default:
            break;
        }
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
  
  protected Bounds getBoundsInRoot() {
    return CoreExtensions.localToRootDiagram(this.host.getNode(), this.host.getNode().getLayoutBounds());
  }
  
  protected Bounds getSnapBoundsInRoot() {
    return CoreExtensions.localToRootDiagram(this.host, this.host.getSnapBounds());
  }
  
  @Override
  public Point2D getDefaultAnchor(final Side side) {
    return this.getDefaultAnchor(this.getBoundsInRoot(), side);
  }
  
  @Override
  public Point2D getDefaultSnapAnchor(final Side side) {
    return this.getDefaultAnchor(this.getSnapBoundsInRoot(), side);
  }
  
  public AbstractAnchors(final XNode host) {
    super();
    this.host = host;
  }
}
