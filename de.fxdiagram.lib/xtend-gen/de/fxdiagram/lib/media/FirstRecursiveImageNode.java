package de.fxdiagram.lib.media;

import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.lib.media.RecursiveImageNode;
import java.util.Deque;
import java.util.LinkedList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class FirstRecursiveImageNode extends XNode {
  private RecursiveImageNode recursiveImageNode;
  
  private Deque<Group> panes = new Function0<Deque<Group>>() {
    public Deque<Group> apply() {
      LinkedList<Group> _linkedList = new LinkedList<Group>();
      return _linkedList;
    }
  }.apply();
  
  public FirstRecursiveImageNode(final RecursiveImageNode parent) {
    this.recursiveImageNode = parent;
    final Group group = parent.createPane();
    this.setNode(group);
    this.panes.push(group);
  }
  
  public void doActivate() {
    super.doActivate();
    DoubleProperty _layoutXProperty = this.layoutXProperty();
    DoubleProperty _xProperty = this.recursiveImageNode.xProperty();
    _layoutXProperty.bindBidirectional(_xProperty);
    DoubleProperty _layoutYProperty = this.layoutYProperty();
    DoubleProperty _yProperty = this.recursiveImageNode.yProperty();
    _layoutYProperty.bindBidirectional(_yProperty);
    DoubleProperty _scaleXProperty = this.scaleXProperty();
    DoubleProperty _scaleProperty = this.recursiveImageNode.scaleProperty();
    _scaleXProperty.bind(_scaleProperty);
    DoubleProperty _scaleYProperty = this.scaleYProperty();
    DoubleProperty _scaleProperty_1 = this.recursiveImageNode.scaleProperty();
    _scaleYProperty.bind(_scaleProperty_1);
    this.updateChildPanes();
    XRoot _root = CoreExtensions.getRoot(this);
    DoubleProperty _diagramScaleProperty = _root.diagramScaleProperty();
    final ChangeListener<Number> _function = new ChangeListener<Number>() {
      public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
        FirstRecursiveImageNode.this.updateChildPanes();
      }
    };
    _diagramScaleProperty.addListener(_function);
  }
  
  public void selectionFeedback(final boolean isSelected) {
  }
  
  public void updateChildPanes() {
    boolean _isEmpty = this.panes.isEmpty();
    boolean _not = (!_isEmpty);
    boolean _while = _not;
    while (_while) {
      {
        final Group child = this.panes.pop();
        final Group parent = this.panes.peek();
        Bounds _boundsInLocal = child.getBoundsInLocal();
        final Bounds bounds = child.localToScene(_boundsInLocal);
        double _width = bounds.getWidth();
        double _height = bounds.getHeight();
        final double area = (_width * _height);
        boolean _lessEqualsThan = (area <= 10);
        if (_lessEqualsThan) {
          boolean _notEquals = (!Objects.equal(parent, null));
          if (_notEquals) {
            ObservableList<Node> _children = parent.getChildren();
            _children.remove(child);
          } else {
            this.panes.push(child);
            return;
          }
        } else {
          boolean _greaterThan = (area > 500);
          if (_greaterThan) {
            final Group grandChild = this.createScaledPane();
            ObservableList<Node> _children_1 = child.getChildren();
            _children_1.add(grandChild);
            this.panes.push(child);
            this.panes.push(grandChild);
          } else {
            this.panes.push(child);
            return;
          }
        }
      }
      boolean _isEmpty_1 = this.panes.isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      _while = _not_1;
    }
  }
  
  public Group createScaledPane() {
    Group _createPane = this.recursiveImageNode.createPane();
    final Procedure1<Group> _function = new Procedure1<Group>() {
      public void apply(final Group it) {
        DoubleProperty _scaleXProperty = it.scaleXProperty();
        DoubleProperty _scaleProperty = FirstRecursiveImageNode.this.recursiveImageNode.scaleProperty();
        _scaleXProperty.bind(_scaleProperty);
        DoubleProperty _scaleYProperty = it.scaleYProperty();
        DoubleProperty _scaleProperty_1 = FirstRecursiveImageNode.this.recursiveImageNode.scaleProperty();
        _scaleYProperty.bind(_scaleProperty_1);
        DoubleProperty _layoutXProperty = it.layoutXProperty();
        DoubleProperty _xProperty = FirstRecursiveImageNode.this.recursiveImageNode.xProperty();
        _layoutXProperty.bind(_xProperty);
        DoubleProperty _layoutYProperty = it.layoutYProperty();
        DoubleProperty _yProperty = FirstRecursiveImageNode.this.recursiveImageNode.yProperty();
        _layoutYProperty.bind(_yProperty);
      }
    };
    Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_createPane, _function);
    return _doubleArrow;
  }
}
