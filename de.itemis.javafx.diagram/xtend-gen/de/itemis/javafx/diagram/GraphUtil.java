package de.itemis.javafx.diagram;

import com.google.common.base.Objects;
import de.itemis.javafx.diagram.ShapeContainer;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

@SuppressWarnings("all")
public class GraphUtil {
  public static ShapeContainer getTargetShape(final MouseEvent event) {
    ShapeContainer _xifexpression = null;
    EventTarget _target = event.getTarget();
    if ((_target instanceof Node)) {
      EventTarget _target_1 = event.getTarget();
      ShapeContainer _containerShape = GraphUtil.getContainerShape(((Node) _target_1));
      _xifexpression = _containerShape;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  public static ShapeContainer getContainerShape(final Node it) {
    ShapeContainer _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(it,null)) {
        _matched=true;
        _switchResult = null;
      }
    }
    if (!_matched) {
      if (it instanceof ShapeContainer) {
        final ShapeContainer _shapeContainer = (ShapeContainer)it;
        _matched=true;
        _switchResult = _shapeContainer;
      }
    }
    if (!_matched) {
      Parent _parent = it.getParent();
      ShapeContainer _containerShape = GraphUtil.getContainerShape(_parent);
      _switchResult = _containerShape;
    }
    return _switchResult;
  }
}
