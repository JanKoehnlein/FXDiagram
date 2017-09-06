package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.extensions.InitializingListListener;
import de.fxdiagram.core.extensions.InitializingListener;
import de.fxdiagram.core.extensions.InitializingMapListener;
import de.fxdiagram.core.extensions.TransformExtensions;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class CoreExtensions {
  public static <T extends Object> void safeAdd(final Collection<? super T> collection, final T element) {
    if (((!Objects.equal(element, null)) && (!collection.contains(element)))) {
      collection.add(element);
    }
  }
  
  public static <T extends Object> void safeAdd(final Collection<? super T> collection, final Iterable<T> elements) {
    final Consumer<T> _function = (T it) -> {
      CoreExtensions.<T>safeAdd(collection, it);
    };
    elements.forEach(_function);
  }
  
  public static <T extends Object> boolean safeDelete(final Collection<? super T> list, final T element) {
    boolean _xifexpression = false;
    if (((!Objects.equal(element, null)) && list.contains(element))) {
      _xifexpression = list.remove(element);
    }
    return _xifexpression;
  }
  
  public static <T extends Object> void setSafely(final Property<T> property, final T newValue) {
    boolean _isBound = property.isBound();
    boolean _not = (!_isBound);
    if (_not) {
      property.setValue(newValue);
    }
  }
  
  public static boolean isRootDiagram(final Node node) {
    boolean _xblockexpression = false;
    {
      boolean _matched = false;
      if (node instanceof XDiagram) {
        _matched=true;
        return ((XDiagram)node).getIsRootDiagram();
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
      if (Objects.equal(node, null)) {
        _matched=true;
        return null;
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
      _xblockexpression = CoreExtensions.localToRootDiagram(node.getParent(), node.localToParent(point));
    }
    return _xblockexpression;
  }
  
  public static Bounds localToRootDiagram(final Node node, final Bounds bounds) {
    Bounds _xblockexpression = null;
    {
      boolean _matched = false;
      if (Objects.equal(node, null)) {
        _matched=true;
        return null;
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
      _xblockexpression = CoreExtensions.localToRootDiagram(node.getParent(), node.localToParent(bounds));
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
    if (Objects.equal(node, null)) {
      _matched=true;
      _switchResult = null;
    }
    if (!_matched) {
      if (node instanceof XDiagram) {
        _matched=true;
        _switchResult = point;
      }
    }
    if (!_matched) {
      _switchResult = CoreExtensions.localToDiagram(node.getParent(), node.localToParent(point));
    }
    return _switchResult;
  }
  
  public static Bounds localToDiagram(final Node node, final Bounds bounds) {
    Bounds _switchResult = null;
    boolean _matched = false;
    if (Objects.equal(node, null)) {
      _matched=true;
      _switchResult = null;
    }
    if (!_matched) {
      if (node instanceof XDiagram) {
        _matched=true;
        _switchResult = bounds;
      }
    }
    if (!_matched) {
      _switchResult = CoreExtensions.localToDiagram(node.getParent(), node.localToParent(bounds));
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
          TransformExtensions.leftMultiply(transform, currentNode.getLocalToParentTransform());
          currentNode = currentNode.getParent();
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
    if (Objects.equal(it, null)) {
      _matched=true;
      _switchResult = null;
    }
    if (!_matched) {
      if (it instanceof XDiagram) {
        _matched=true;
        _switchResult = ((XDiagram)it);
      }
    }
    if (!_matched) {
      if (it instanceof XConnection) {
        _matched=true;
        final XDiagram sourceDiagram = CoreExtensions.getDiagram(((XConnection)it).getSource());
        ObservableList<XConnection> _connections = null;
        if (sourceDiagram!=null) {
          _connections=sourceDiagram.getConnections();
        }
        boolean _contains = false;
        if (_connections!=null) {
          _contains=_connections.contains(it);
        }
        if (_contains) {
          return sourceDiagram;
        }
        final XDiagram targetDiagram = CoreExtensions.getDiagram(((XConnection)it).getTarget());
        ObservableList<XConnection> _connections_1 = null;
        if (targetDiagram!=null) {
          _connections_1=targetDiagram.getConnections();
        }
        boolean _contains_1 = false;
        if (_connections_1!=null) {
          _contains_1=_connections_1.contains(it);
        }
        if (_contains_1) {
          return targetDiagram;
        }
        return CoreExtensions.getDiagram(((XConnection)it).getParent());
      }
    }
    if (!_matched) {
      _switchResult = CoreExtensions.getDiagram(it.getParent());
    }
    return _switchResult;
  }
  
  public static XDiagram getRootDiagram(final Node it) {
    XDiagram _switchResult = null;
    boolean _matched = false;
    if (Objects.equal(it, null)) {
      _matched=true;
      _switchResult = null;
    }
    if (!_matched) {
      if (it instanceof XDiagram) {
        _matched=true;
        XDiagram _xifexpression = null;
        boolean _isRootDiagram = ((XDiagram)it).getIsRootDiagram();
        if (_isRootDiagram) {
          _xifexpression = ((XDiagram)it);
        } else {
          _xifexpression = CoreExtensions.getRootDiagram(((XDiagram)it).getParent());
        }
        _switchResult = _xifexpression;
      }
    }
    if (!_matched) {
      _switchResult = CoreExtensions.getRootDiagram(it.getParent());
    }
    return _switchResult;
  }
  
  public static XRoot getRoot(final Node it) {
    XRoot _switchResult = null;
    boolean _matched = false;
    if (Objects.equal(it, null)) {
      _matched=true;
      _switchResult = null;
    }
    if (!_matched) {
      if (it instanceof XRoot) {
        _matched=true;
        _switchResult = ((XRoot)it);
      }
    }
    if (!_matched) {
      _switchResult = CoreExtensions.getRoot(it.getParent());
    }
    return _switchResult;
  }
  
  public static XShape getContainerShape(final Node it) {
    XShape _switchResult = null;
    boolean _matched = false;
    if (Objects.equal(it, null)) {
      _matched=true;
      _switchResult = null;
    }
    if (!_matched) {
      if (it instanceof XShape) {
        _matched=true;
        _switchResult = ((XShape)it);
      }
    }
    if (!_matched) {
      _switchResult = CoreExtensions.getContainerShape(it.getParent());
    }
    return _switchResult;
  }
  
  public static Iterable<? extends Node> getAllChildren(final Parent node) {
    return IterableExtensions.toSet(CoreExtensions.getAllChildrenInternal(node));
  }
  
  protected static Iterable<? extends Node> getAllChildrenInternal(final Parent node) {
    ObservableList<Node> _childrenUnmodifiable = node.getChildrenUnmodifiable();
    final Function1<Parent, Iterable<? extends Node>> _function = (Parent it) -> {
      return CoreExtensions.getAllChildren(it);
    };
    Iterable<Node> _flatten = Iterables.<Node>concat(IterableExtensions.<Parent, Iterable<? extends Node>>map(Iterables.<Parent>filter(node.getChildrenUnmodifiable(), Parent.class), _function));
    return Iterables.<Node>concat(_childrenUnmodifiable, _flatten);
  }
  
  public static <T extends Object, U extends Object> void addInitializingListener(final ObservableMap<T, U> map, final InitializingMapListener<T, U> mapListener) {
    final Consumer<Map.Entry<T, U>> _function = (Map.Entry<T, U> it) -> {
      mapListener.getPut().apply(it.getKey(), it.getValue());
    };
    map.entrySet().forEach(_function);
    map.addListener(mapListener);
  }
  
  public static <T extends Object> void addInitializingListener(final ObservableList<T> list, final InitializingListListener<T> listListener) {
    list.addListener(listListener);
    int _size = list.size();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
    for (final Integer i : _doubleDotLessThan) {
      listListener.getAdd().apply(list.get((i).intValue()));
    }
  }
  
  public static <T extends Object> void addInitializingListener(final ObservableValue<T> value, final InitializingListener<T> listener) {
    value.addListener(listener);
    T _value = value.getValue();
    boolean _notEquals = (!Objects.equal(_value, null));
    if (_notEquals) {
      listener.getSet().apply(value.getValue());
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
        mapListener.getRemove().apply(entry.getKey(), entry.getValue());
      }
    }
  }
  
  public static <T extends Object> void removeInitializingListener(final ObservableList<T> list, final InitializingListListener<T> listListener) {
    final Consumer<T> _function = (T it) -> {
      listListener.getRemove().apply(it);
    };
    list.forEach(_function);
    list.removeListener(listListener);
  }
  
  public static <T extends Object> void removeInitializingListener(final ObservableValue<T> value, final InitializingListener<T> listener) {
    T _value = value.getValue();
    boolean _notEquals = (!Objects.equal(_value, null));
    if (_notEquals) {
      listener.getUnset().apply(value.getValue());
    }
    value.removeListener(listener);
  }
}
