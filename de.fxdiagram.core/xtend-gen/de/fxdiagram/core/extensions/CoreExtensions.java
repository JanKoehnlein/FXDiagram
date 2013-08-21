package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.extensions.TransformExtensions;
import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class CoreExtensions {
  public static boolean isRootDiagram(final Node node) {
    boolean _xblockexpression = false;
    {
      boolean _matched = false;
      if (!_matched) {
        if (node instanceof XDiagram) {
          final XDiagram _xDiagram = (XDiagram)node;
          _matched=true;
          return _xDiagram.getIsRootDiagram();
        }
      }
      _xblockexpression = (false);
    }
    return _xblockexpression;
  }
  
  public static Point2D localToRootDiagram(final Node node, final double x, final double y) {
    Point2D _point2D = new Point2D(x, y);
    Point2D _localToRootDiagram = CoreExtensions.localToRootDiagram(node, _point2D);
    return _localToRootDiagram;
  }
  
  public static Point2D localToRootDiagram(final Node node, final Point2D point) {
    Point2D _xblockexpression = null;
    {
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(node,null)) {
          _matched=true;
          return null;
        }
      }
      if (!_matched) {
        if (node instanceof XDiagram) {
          final XDiagram _xDiagram = (XDiagram)node;
          _matched=true;
          boolean _isRootDiagram = _xDiagram.getIsRootDiagram();
          if (_isRootDiagram) {
            return point;
          }
        }
      }
      Parent _parent = node.getParent();
      Point2D _localToParent = node.localToParent(point);
      Point2D _localToRootDiagram = CoreExtensions.localToRootDiagram(_parent, _localToParent);
      _xblockexpression = (_localToRootDiagram);
    }
    return _xblockexpression;
  }
  
  public static Bounds localToRootDiagram(final Node node, final Bounds bounds) {
    Bounds _xblockexpression = null;
    {
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(node,null)) {
          _matched=true;
          return null;
        }
      }
      if (!_matched) {
        if (node instanceof XDiagram) {
          final XDiagram _xDiagram = (XDiagram)node;
          _matched=true;
          boolean _isRootDiagram = _xDiagram.getIsRootDiagram();
          if (_isRootDiagram) {
            return bounds;
          }
        }
      }
      Parent _parent = node.getParent();
      Bounds _localToParent = node.localToParent(bounds);
      Bounds _localToRootDiagram = CoreExtensions.localToRootDiagram(_parent, _localToParent);
      _xblockexpression = (_localToRootDiagram);
    }
    return _xblockexpression;
  }
  
  public static Point2D localToDiagram(final Node node, final double x, final double y) {
    Point2D _point2D = new Point2D(x, y);
    Point2D _localToDiagram = CoreExtensions.localToDiagram(node, _point2D);
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
      if (node instanceof XDiagram) {
        final XDiagram _xDiagram = (XDiagram)node;
        _matched=true;
        _switchResult = point;
      }
    }
    if (!_matched) {
      Parent _parent = node.getParent();
      Point2D _localToParent = node.localToParent(point);
      Point2D _localToDiagram = CoreExtensions.localToDiagram(_parent, _localToParent);
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
      if (node instanceof XDiagram) {
        final XDiagram _xDiagram = (XDiagram)node;
        _matched=true;
        _switchResult = bounds;
      }
    }
    if (!_matched) {
      Parent _parent = node.getParent();
      Bounds _localToParent = node.localToParent(bounds);
      Bounds _localToDiagram = CoreExtensions.localToDiagram(_parent, _localToParent);
      _switchResult = _localToDiagram;
    }
    return _switchResult;
  }
  
  public static Transform localToDiagramTransform(final Node node) {
    Transform _xblockexpression = null;
    {
      Affine _affine = new Affine();
      final Affine transform = _affine;
      Node currentNode = node;
      Parent _parent = currentNode.getParent();
      boolean _notEquals = (!Objects.equal(_parent, null));
      boolean _while = _notEquals;
      while (_while) {
        {
          Transform _localToParentTransform = currentNode.getLocalToParentTransform();
          TransformExtensions.leftMultiply(transform, _localToParentTransform);
          Parent _parent_1 = currentNode.getParent();
          currentNode = _parent_1;
          if ((currentNode instanceof XDiagram)) {
            return transform;
          }
        }
        Parent _parent_1 = currentNode.getParent();
        boolean _notEquals_1 = (!Objects.equal(_parent_1, null));
        _while = _notEquals_1;
      }
      _xblockexpression = (null);
    }
    return _xblockexpression;
  }
  
  public static XDiagram getDiagram(final Node it) {
    XDiagram _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(it,null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (it instanceof XDiagram) {
        final XDiagram _xDiagram = (XDiagram)it;
        _matched=true;
        _switchResult = _xDiagram;
      }
    }
    if (!_matched) {
      Parent _parent = it.getParent();
      XDiagram _diagram = CoreExtensions.getDiagram(_parent);
      _switchResult = _diagram;
    }
    return _switchResult;
  }
  
  public static XDiagram getRootDiagram(final Node it) {
    XDiagram _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(it,null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (it instanceof XDiagram) {
        final XDiagram _xDiagram = (XDiagram)it;
        _matched=true;
        XDiagram _xifexpression = null;
        boolean _isRootDiagram = _xDiagram.getIsRootDiagram();
        if (_isRootDiagram) {
          _xifexpression = _xDiagram;
        } else {
          XDiagram _parentDiagram = _xDiagram.getParentDiagram();
          XDiagram _rootDiagram = CoreExtensions.getRootDiagram(_parentDiagram);
          _xifexpression = _rootDiagram;
        }
        _switchResult = _xifexpression;
      }
    }
    if (!_matched) {
      Parent _parent = it.getParent();
      XDiagram _rootDiagram = CoreExtensions.getRootDiagram(_parent);
      _switchResult = _rootDiagram;
    }
    return _switchResult;
  }
  
  public static XRoot getRoot(final Node it) {
    XRoot _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(it,null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (it instanceof XRoot) {
        final XRoot _xRoot = (XRoot)it;
        _matched=true;
        _switchResult = _xRoot;
      }
    }
    if (!_matched) {
      Parent _parent = it.getParent();
      XRoot _root = CoreExtensions.getRoot(_parent);
      _switchResult = _root;
    }
    return _switchResult;
  }
  
  public static XShape getTargetShape(final MouseEvent event) {
    XShape _xifexpression = null;
    EventTarget _target = event.getTarget();
    if ((_target instanceof Node)) {
      EventTarget _target_1 = event.getTarget();
      XShape _containerShape = CoreExtensions.getContainerShape(((Node) _target_1));
      _xifexpression = _containerShape;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  public static XShape getContainerShape(final Node it) {
    XShape _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(it,null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (it instanceof XShape) {
        final XShape _xShape = (XShape)it;
        _matched=true;
        _switchResult = _xShape;
      }
    }
    if (!_matched) {
      Parent _parent = it.getParent();
      XShape _containerShape = CoreExtensions.getContainerShape(_parent);
      _switchResult = _containerShape;
    }
    return _switchResult;
  }
  
  public static XRapidButton getTargetButton(final MouseEvent event) {
    XRapidButton _xifexpression = null;
    EventTarget _target = event.getTarget();
    if ((_target instanceof Node)) {
      EventTarget _target_1 = event.getTarget();
      XRapidButton _containerButton = CoreExtensions.getContainerButton(((Node) _target_1));
      _xifexpression = _containerButton;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  public static XRapidButton getContainerButton(final Node it) {
    XRapidButton _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(it,null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (it instanceof XRapidButton) {
        final XRapidButton _xRapidButton = (XRapidButton)it;
        _matched=true;
        _switchResult = _xRapidButton;
      }
    }
    if (!_matched) {
      Parent _parent = it.getParent();
      XRapidButton _containerButton = CoreExtensions.getContainerButton(_parent);
      _switchResult = _containerButton;
    }
    return _switchResult;
  }
  
  public static Iterable<? extends Node> getAllChildren(final Parent node) {
    ObservableList<Node> _childrenUnmodifiable = node.getChildrenUnmodifiable();
    ObservableList<Node> _childrenUnmodifiable_1 = node.getChildrenUnmodifiable();
    Iterable<Parent> _filter = Iterables.<Parent>filter(_childrenUnmodifiable_1, Parent.class);
    final Function1<Parent,Iterable<? extends Node>> _function = new Function1<Parent,Iterable<? extends Node>>() {
      public Iterable<? extends Node> apply(final Parent it) {
        Iterable<? extends Node> _allChildren = CoreExtensions.getAllChildren(it);
        return _allChildren;
      }
    };
    Iterable<Iterable<? extends Node>> _map = IterableExtensions.<Parent, Iterable<? extends Node>>map(_filter, _function);
    Iterable<Node> _flatten = Iterables.<Node>concat(_map);
    Iterable<Node> _plus = Iterables.<Node>concat(_childrenUnmodifiable, _flatten);
    return _plus;
  }
}
