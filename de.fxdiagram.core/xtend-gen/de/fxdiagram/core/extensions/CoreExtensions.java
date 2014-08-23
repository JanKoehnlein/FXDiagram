package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.extensions.InitializingListListener;
import de.fxdiagram.core.extensions.InitializingListener;
import de.fxdiagram.core.extensions.InitializingMapListener;
import de.fxdiagram.core.extensions.TransformExtensions;
import java.util.Map;
import java.util.Set;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventTarget;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class CoreExtensions {
  public static boolean isRootDiagram(final Node node) {
    boolean _xblockexpression = false;
    {
      boolean _matched = false;
      if (!_matched) {
        if (node instanceof XDiagram) {
          _matched=true;
          return ((XDiagram)node).getIsRootDiagram();
        }
      }
      _xblockexpression = false;
    }
    return _xblockexpression;
  }
  
  public static Point2D localToRootDiagram(final Node node, final double x, final double y) {
    Point2D _point2D = new Point2D(x, y);
    return CoreExtensions.localToRootDiagram(node, _point2D);
  }
  
  public static Point2D localToRootDiagram(final Node node, final Point2D point) {
    Point2D _xblockexpression = null;
    {
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(node, null)) {
          _matched=true;
          return null;
        }
      }
      if (!_matched) {
        if (node instanceof XDiagram) {
          _matched=true;
          boolean _isRootDiagram = ((XDiagram)node).getIsRootDiagram();
          if (_isRootDiagram) {
            return point;
          }
        }
      }
      Parent _parent = node.getParent();
      Point2D _localToParent = node.localToParent(point);
      _xblockexpression = CoreExtensions.localToRootDiagram(_parent, _localToParent);
    }
    return _xblockexpression;
  }
  
  public static Bounds localToRootDiagram(final Node node, final Bounds bounds) {
    Bounds _xblockexpression = null;
    {
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(node, null)) {
          _matched=true;
          return null;
        }
      }
      if (!_matched) {
        if (node instanceof XDiagram) {
          _matched=true;
          boolean _isRootDiagram = ((XDiagram)node).getIsRootDiagram();
          if (_isRootDiagram) {
            return bounds;
          }
        }
      }
      Parent _parent = node.getParent();
      Bounds _localToParent = node.localToParent(bounds);
      _xblockexpression = CoreExtensions.localToRootDiagram(_parent, _localToParent);
    }
    return _xblockexpression;
  }
  
  public static Point2D localToDiagram(final Node node, final double x, final double y) {
    Point2D _point2D = new Point2D(x, y);
    return CoreExtensions.localToDiagram(node, _point2D);
  }
  
  public static Point2D localToDiagram(final Node node, final Point2D point) {
    Point2D _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(node, null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (node instanceof XDiagram) {
        _matched=true;
        _switchResult = point;
      }
    }
    if (!_matched) {
      Parent _parent = node.getParent();
      Point2D _localToParent = node.localToParent(point);
      _switchResult = CoreExtensions.localToDiagram(_parent, _localToParent);
    }
    return _switchResult;
  }
  
  public static Bounds localToDiagram(final Node node, final Bounds bounds) {
    Bounds _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(node, null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (node instanceof XDiagram) {
        _matched=true;
        _switchResult = bounds;
      }
    }
    if (!_matched) {
      Parent _parent = node.getParent();
      Bounds _localToParent = node.localToParent(bounds);
      _switchResult = CoreExtensions.localToDiagram(_parent, _localToParent);
    }
    return _switchResult;
  }
  
  public static Transform localToDiagramTransform(final Node node) {
    Object _xblockexpression = null;
    {
      final Affine transform = new Affine();
      Node currentNode = node;
      while ((!Objects.equal(currentNode.getParent(), null))) {
        {
          Transform _localToParentTransform = currentNode.getLocalToParentTransform();
          TransformExtensions.leftMultiply(transform, _localToParentTransform);
          Parent _parent = currentNode.getParent();
          currentNode = _parent;
          if ((currentNode instanceof XDiagram)) {
            return transform;
          }
        }
      }
      _xblockexpression = null;
    }
    return ((Transform)_xblockexpression);
  }
  
  public static XDiagram getDiagram(final Node it) {
    XDiagram _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(it, null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (it instanceof XDiagram) {
        _matched=true;
        _switchResult = ((XDiagram)it);
      }
    }
    if (!_matched) {
      Parent _parent = it.getParent();
      _switchResult = CoreExtensions.getDiagram(_parent);
    }
    return _switchResult;
  }
  
  public static XDiagram getRootDiagram(final Node it) {
    XDiagram _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(it, null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (it instanceof XDiagram) {
        _matched=true;
        XDiagram _xifexpression = null;
        boolean _isRootDiagram = ((XDiagram)it).getIsRootDiagram();
        if (_isRootDiagram) {
          _xifexpression = ((XDiagram)it);
        } else {
          XDiagram _parentDiagram = ((XDiagram)it).getParentDiagram();
          _xifexpression = CoreExtensions.getRootDiagram(_parentDiagram);
        }
        _switchResult = _xifexpression;
      }
    }
    if (!_matched) {
      Parent _parent = it.getParent();
      _switchResult = CoreExtensions.getRootDiagram(_parent);
    }
    return _switchResult;
  }
  
  public static XRoot getRoot(final Node it) {
    XRoot _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(it, null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (it instanceof XRoot) {
        _matched=true;
        _switchResult = ((XRoot)it);
      }
    }
    if (!_matched) {
      Parent _parent = it.getParent();
      _switchResult = CoreExtensions.getRoot(_parent);
    }
    return _switchResult;
  }
  
  public static XShape getTargetShape(final MouseEvent event) {
    XShape _xifexpression = null;
    EventTarget _target = event.getTarget();
    if ((_target instanceof Node)) {
      EventTarget _target_1 = event.getTarget();
      _xifexpression = CoreExtensions.getContainerShape(((Node) _target_1));
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  public static XShape getContainerShape(final Node it) {
    XShape _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(it, null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (it instanceof XShape) {
        _matched=true;
        _switchResult = ((XShape)it);
      }
    }
    if (!_matched) {
      Parent _parent = it.getParent();
      _switchResult = CoreExtensions.getContainerShape(_parent);
    }
    return _switchResult;
  }
  
  public static Iterable<? extends Node> getAllChildren(final Parent node) {
    Iterable<? extends Node> _allChildrenInternal = CoreExtensions.getAllChildrenInternal(node);
    return IterableExtensions.toSet(_allChildrenInternal);
  }
  
  protected static Iterable<? extends Node> getAllChildrenInternal(final Parent node) {
    ObservableList<Node> _childrenUnmodifiable = node.getChildrenUnmodifiable();
    ObservableList<Node> _childrenUnmodifiable_1 = node.getChildrenUnmodifiable();
    Iterable<Parent> _filter = Iterables.<Parent>filter(_childrenUnmodifiable_1, Parent.class);
    final Function1<Parent, Iterable<? extends Node>> _function = new Function1<Parent, Iterable<? extends Node>>() {
      public Iterable<? extends Node> apply(final Parent it) {
        return CoreExtensions.getAllChildren(it);
      }
    };
    Iterable<Iterable<? extends Node>> _map = IterableExtensions.<Parent, Iterable<? extends Node>>map(_filter, _function);
    Iterable<Node> _flatten = Iterables.<Node>concat(_map);
    return Iterables.<Node>concat(_childrenUnmodifiable, _flatten);
  }
  
  public static <T extends Object, U extends Object> void addInitializingListener(final ObservableMap<T, U> map, final InitializingMapListener<T, U> mapListener) {
    Set<Map.Entry<T, U>> _entrySet = map.entrySet();
    final Procedure1<Map.Entry<T, U>> _function = new Procedure1<Map.Entry<T, U>>() {
      public void apply(final Map.Entry<T, U> it) {
        Procedure2<? super T, ? super U> _put = mapListener.getPut();
        T _key = it.getKey();
        U _value = it.getValue();
        _put.apply(_key, _value);
      }
    };
    IterableExtensions.<Map.Entry<T, U>>forEach(_entrySet, _function);
    map.addListener(mapListener);
  }
  
  public static <T extends Object> void addInitializingListener(final ObservableList<T> list, final InitializingListListener<T> listListener) {
    list.addListener(listListener);
    int _size = list.size();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
    for (final Integer i : _doubleDotLessThan) {
      Procedure1<? super T> _add = listListener.getAdd();
      T _get = list.get((i).intValue());
      _add.apply(_get);
    }
  }
  
  public static <T extends Object> void addInitializingListener(final ObservableValue<T> value, final InitializingListener<T> listener) {
    value.addListener(listener);
    T _value = value.getValue();
    boolean _notEquals = (!Objects.equal(_value, null));
    if (_notEquals) {
      Procedure1<? super T> _set = listener.getSet();
      T _value_1 = value.getValue();
      _set.apply(_value_1);
    }
  }
  
  public static <T extends Object, U extends Object> void removeInitializingListener(final ObservableMap<T, U> map, final InitializingMapListener<T, U> mapListener) {
    map.removeListener(mapListener);
    final Set<Map.Entry<T, U>> entries = map.entrySet();
    int _size = entries.size();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final Map.Entry<T, U> entry = ((Map.Entry<T, U>[])Conversions.unwrapArray(entries, Map.Entry.class))[(i).intValue()];
        Procedure2<? super T, ? super U> _remove = mapListener.getRemove();
        T _key = entry.getKey();
        U _value = entry.getValue();
        _remove.apply(_key, _value);
      }
    }
  }
  
  public static <T extends Object> void removeInitializingListener(final ObservableList<T> list, final InitializingListListener<T> listListener) {
    final Procedure1<T> _function = new Procedure1<T>() {
      public void apply(final T it) {
        Procedure1<? super T> _remove = listListener.getRemove();
        _remove.apply(it);
      }
    };
    IterableExtensions.<T>forEach(list, _function);
    list.removeListener(listListener);
  }
  
  public static <T extends Object> void removeInitializingListener(final ObservableValue<T> value, final InitializingListener<T> listener) {
    T _value = value.getValue();
    boolean _notEquals = (!Objects.equal(_value, null));
    if (_notEquals) {
      Procedure1<? super T> _unset = listener.getUnset();
      T _value_1 = value.getValue();
      _unset.apply(_value_1);
    }
    value.removeListener(listener);
  }
}
