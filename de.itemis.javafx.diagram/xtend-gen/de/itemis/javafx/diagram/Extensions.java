package de.itemis.javafx.diagram;

import com.google.common.base.Objects;
import de.itemis.javafx.diagram.XNode;
import javafx.event.EventTarget;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

@SuppressWarnings("all")
public class Extensions {
  public static Point2D localToRoot(final Node node, final double x, final double y) {
    Scene _scene = node.getScene();
    Parent _root = _scene.getRoot();
    Point2D _localToScene = node.localToScene(x, y);
    Point2D _parentToLocal = _root.parentToLocal(_localToScene);
    return _parentToLocal;
  }
  
  public static Point2D localToRoot(final Node node, final Point2D point) {
    Scene _scene = node.getScene();
    Parent _root = _scene.getRoot();
    Point2D _localToScene = node.localToScene(point);
    Point2D _parentToLocal = _root.parentToLocal(_localToScene);
    return _parentToLocal;
  }
  
  public static Bounds localToRoot(final Node node, final Bounds bounds) {
    Scene _scene = node.getScene();
    Parent _root = _scene.getRoot();
    Bounds _localToScene = node.localToScene(bounds);
    Bounds _parentToLocal = _root.parentToLocal(_localToScene);
    return _parentToLocal;
  }
  
  public static XNode getTargetShape(final MouseEvent event) {
    XNode _xifexpression = null;
    EventTarget _target = event.getTarget();
    if ((_target instanceof Node)) {
      EventTarget _target_1 = event.getTarget();
      XNode _containerShape = Extensions.getContainerShape(((Node) _target_1));
      _xifexpression = _containerShape;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  public static XNode getContainerShape(final Node it) {
    XNode _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(it,null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (it instanceof XNode) {
        final XNode _xNode = (XNode)it;
        _matched=true;
        _switchResult = _xNode;
      }
    }
    if (!_matched) {
      Parent _parent = it.getParent();
      XNode _containerShape = Extensions.getContainerShape(_parent);
      _switchResult = _containerShape;
    }
    return _switchResult;
  }
}
