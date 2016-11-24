package de.fxdiagram.core.anchors;

import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.anchors.ManhattanAnchors;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import java.util.Map;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class CachedAnchors {
  private XNode host;
  
  private Map<Side, Point2D> side2point = CollectionLiterals.<Side, Point2D>newHashMap();
  
  private Bounds bounds;
  
  public CachedAnchors(final XNode host) {
    this.host = host;
    Node _node = host.getNode();
    Bounds _boundsInLocal = _node.getBoundsInLocal();
    Bounds _localToRootDiagram = CoreExtensions.localToRootDiagram(host, _boundsInLocal);
    this.bounds = _localToRootDiagram;
    final Point2D center = BoundsExtensions.center(this.bounds);
    double _x = center.getX();
    double _minY = this.bounds.getMinY();
    Point2D _point2D = new Point2D(_x, _minY);
    this.side2point.put(Side.TOP, _point2D);
    double _x_1 = center.getX();
    double _maxY = this.bounds.getMaxY();
    Point2D _point2D_1 = new Point2D(_x_1, _maxY);
    this.side2point.put(Side.BOTTOM, _point2D_1);
    double _minX = this.bounds.getMinX();
    double _y = center.getY();
    Point2D _point2D_2 = new Point2D(_minX, _y);
    this.side2point.put(Side.LEFT, _point2D_2);
    double _maxX = this.bounds.getMaxX();
    double _y_1 = center.getY();
    Point2D _point2D_3 = new Point2D(_maxX, _y_1);
    this.side2point.put(Side.RIGHT, _point2D_3);
  }
  
  public Point2D get(final XControlPoint referencePoint, final Side side) {
    Point2D _xblockexpression = null;
    {
      final Anchors anchors = this.host.getAnchors();
      Point2D _xifexpression = null;
      if ((anchors instanceof ManhattanAnchors)) {
        double _layoutX = referencePoint.getLayoutX();
        double _layoutY = referencePoint.getLayoutY();
        _xifexpression = ((ManhattanAnchors)anchors).getManhattanAnchor(_layoutX, _layoutY, side);
      } else {
        _xifexpression = this.get(side);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public Point2D get(final Side side) {
    return this.side2point.get(side);
  }
  
  public boolean contains(final Point2D point) {
    return this.bounds.contains(point);
  }
  
  public double getWidth() {
    return this.bounds.getWidth();
  }
  
  public double getHeight() {
    return this.bounds.getHeight();
  }
  
  public Bounds getBounds() {
    return this.bounds;
  }
}
