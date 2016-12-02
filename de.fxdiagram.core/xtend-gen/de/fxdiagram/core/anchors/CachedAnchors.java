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
import javafx.scene.Parent;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class CachedAnchors {
  private XNode host;
  
  private Map<Side, Point2D> side2point = CollectionLiterals.<Side, Point2D>newHashMap();
  
  private Map<Side, Point2D> side2pointUnselected = CollectionLiterals.<Side, Point2D>newHashMap();
  
  private Bounds bounds;
  
  public CachedAnchors(final XNode host) {
    this.host = host;
    Node _node = host.getNode();
    Bounds _boundsInLocal = _node.getBoundsInLocal();
    Bounds _localToRootDiagram = CoreExtensions.localToRootDiagram(host, _boundsInLocal);
    this.bounds = _localToRootDiagram;
    Anchors _anchors = null;
    if (host!=null) {
      _anchors=host.getAnchors();
    }
    boolean _not = (!(_anchors instanceof ManhattanAnchors));
    if (_not) {
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
      Parent _parent = host.getParent();
      Bounds _snapBounds = host.getSnapBounds();
      final Bounds snapBounds = CoreExtensions.localToRootDiagram(_parent, _snapBounds);
      final Point2D snapCenter = BoundsExtensions.center(snapBounds);
      double _x_2 = snapCenter.getX();
      double _minY_1 = snapBounds.getMinY();
      Point2D _point2D_4 = new Point2D(_x_2, _minY_1);
      this.side2pointUnselected.put(Side.TOP, _point2D_4);
      double _x_3 = snapCenter.getX();
      double _maxY_1 = snapBounds.getMaxY();
      Point2D _point2D_5 = new Point2D(_x_3, _maxY_1);
      this.side2pointUnselected.put(Side.BOTTOM, _point2D_5);
      double _minX_1 = snapBounds.getMinX();
      double _y_2 = snapCenter.getY();
      Point2D _point2D_6 = new Point2D(_minX_1, _y_2);
      this.side2pointUnselected.put(Side.LEFT, _point2D_6);
      double _maxX_1 = snapBounds.getMaxX();
      double _y_3 = snapCenter.getY();
      Point2D _point2D_7 = new Point2D(_maxX_1, _y_3);
      this.side2pointUnselected.put(Side.RIGHT, _point2D_7);
    }
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
  
  public Point2D getUnselected(final Side side) {
    Point2D _xblockexpression = null;
    {
      final Anchors anchors = this.host.getAnchors();
      Point2D _xifexpression = null;
      if ((anchors instanceof ManhattanAnchors)) {
        _xifexpression = ((ManhattanAnchors)anchors).getDefaultSnapAnchor(side);
      } else {
        _xifexpression = this.side2pointUnselected.get(side);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public Point2D get(final Side side) {
    Point2D _xblockexpression = null;
    {
      final Anchors anchors = this.host.getAnchors();
      Point2D _xifexpression = null;
      if ((anchors instanceof ManhattanAnchors)) {
        _xifexpression = ((ManhattanAnchors)anchors).getDefaultAnchor(side);
      } else {
        _xifexpression = this.side2point.get(side);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
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
