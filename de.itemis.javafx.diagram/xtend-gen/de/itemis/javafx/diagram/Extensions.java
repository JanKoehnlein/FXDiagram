package de.itemis.javafx.diagram;

import com.google.common.base.Objects;
import de.itemis.javafx.diagram.XAbstractDiagram;
import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.XRootDiagram;
import java.util.logging.Logger;
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
  
  public static Point2D localToDiagram(final Node node, final double x, final double y) {
    Point2D _point2D = new Point2D(x, y);
    Point2D _localToDiagram = Extensions.localToDiagram(node, _point2D);
    return _localToDiagram;
  }
  
  public static Point2D localToDiagram(final Node node, final Point2D point) {
    Point2D _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(node,null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (node instanceof XAbstractDiagram) {
        final XAbstractDiagram _xAbstractDiagram = (XAbstractDiagram)node;
        _matched=true;
        _switchResult = point;
      }
    }
    if (!_matched) {
      Parent _parent = node.getParent();
      Point2D _localToParent = node.localToParent(point);
      Point2D _localToDiagram = Extensions.localToDiagram(_parent, _localToParent);
      _switchResult = _localToDiagram;
    }
    return _switchResult;
  }
  
  public static Bounds localToDiagram(final Node node, final Bounds bounds) {
    Bounds _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(node,null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (node instanceof XAbstractDiagram) {
        final XAbstractDiagram _xAbstractDiagram = (XAbstractDiagram)node;
        _matched=true;
        _switchResult = bounds;
      }
    }
    if (!_matched) {
      Parent _parent = node.getParent();
      Bounds _localToParent = node.localToParent(bounds);
      Bounds _localToDiagram = Extensions.localToDiagram(_parent, _localToParent);
      _switchResult = _localToDiagram;
    }
    return _switchResult;
  }
  
  public static XAbstractDiagram getDiagram(final Node it) {
    XAbstractDiagram _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(it,null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (it instanceof XAbstractDiagram) {
        final XAbstractDiagram _xAbstractDiagram = (XAbstractDiagram)it;
        _matched=true;
        _switchResult = _xAbstractDiagram;
      }
    }
    if (!_matched) {
      Parent _parent = it.getParent();
      XAbstractDiagram _diagram = Extensions.getDiagram(_parent);
      _switchResult = _diagram;
    }
    return _switchResult;
  }
  
  public static XRootDiagram getRootDiagram(final Node it) {
    XRootDiagram _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(it,null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (it instanceof XRootDiagram) {
        final XRootDiagram _xRootDiagram = (XRootDiagram)it;
        _matched=true;
        _switchResult = _xRootDiagram;
      }
    }
    if (!_matched) {
      Parent _parent = it.getParent();
      XRootDiagram _rootDiagram = Extensions.getRootDiagram(_parent);
      _switchResult = _rootDiagram;
    }
    return _switchResult;
  }
  
  public static XNode getTargetNode(final MouseEvent event) {
    XNode _xifexpression = null;
    EventTarget _target = event.getTarget();
    if ((_target instanceof Node)) {
      EventTarget _target_1 = event.getTarget();
      XNode _containerNode = Extensions.getContainerNode(((Node) _target_1));
      _xifexpression = _containerNode;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  public static XNode getContainerNode(final Node it) {
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
      XNode _containerNode = Extensions.getContainerNode(_parent);
      _switchResult = _containerNode;
    }
    return _switchResult;
  }
  
  public static Logger getLogger(final Object it) {
    Class<? extends Object> _class = it.getClass();
    String _canonicalName = _class.getCanonicalName();
    Logger _logger = Logger.getLogger(_canonicalName);
    return _logger;
  }
}
