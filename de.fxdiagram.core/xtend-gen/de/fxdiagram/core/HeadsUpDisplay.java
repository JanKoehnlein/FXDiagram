package de.fxdiagram.core;

import com.google.common.base.Objects;
import java.util.Map;
import java.util.function.Consumer;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class HeadsUpDisplay extends Group {
  private Map<Node, Pos> alignments = CollectionLiterals.<Node, Pos>newHashMap();
  
  private ChangeListener<Number> sceneListener;
  
  public HeadsUpDisplay() {
    final ChangeListener<Number> _function = (ObservableValue<? extends Number> property, Number oldVlaue, Number newValue) -> {
      final Consumer<Node> _function_1 = (Node it) -> {
        this.place(it);
      };
      this.getChildren().forEach(_function_1);
    };
    this.sceneListener = _function;
    final ChangeListener<Scene> _function_1 = (ObservableValue<? extends Scene> property, Scene oldVal, Scene newVal) -> {
      ReadOnlyDoubleProperty _widthProperty = null;
      if (oldVal!=null) {
        _widthProperty=oldVal.widthProperty();
      }
      if (_widthProperty!=null) {
        _widthProperty.removeListener(this.sceneListener);
      }
      ReadOnlyDoubleProperty _heightProperty = null;
      if (oldVal!=null) {
        _heightProperty=oldVal.heightProperty();
      }
      if (_heightProperty!=null) {
        _heightProperty.removeListener(this.sceneListener);
      }
      ReadOnlyDoubleProperty _widthProperty_1 = null;
      if (newVal!=null) {
        _widthProperty_1=newVal.widthProperty();
      }
      if (_widthProperty_1!=null) {
        _widthProperty_1.addListener(this.sceneListener);
      }
      ReadOnlyDoubleProperty _heightProperty_1 = null;
      if (newVal!=null) {
        _heightProperty_1=newVal.heightProperty();
      }
      if (_heightProperty_1!=null) {
        _heightProperty_1.addListener(this.sceneListener);
      }
    };
    this.sceneProperty().addListener(_function_1);
  }
  
  public void add(final Node child, final Pos pos) {
    boolean _contains = this.getChildren().contains(child);
    boolean _not = (!_contains);
    if (_not) {
      ObservableList<Node> _children = this.getChildren();
      _children.add(child);
      this.alignments.put(child, pos);
      this.place(child);
      final ChangeListener<Bounds> _function = new ChangeListener<Bounds>() {
        @Override
        public void changed(final ObservableValue<? extends Bounds> property, final Bounds oldValue, final Bounds newValue) {
          Parent _parent = child.getParent();
          boolean _notEquals = (!Objects.equal(_parent, HeadsUpDisplay.this));
          if (_notEquals) {
            property.removeListener(this);
          } else {
            HeadsUpDisplay.this.place(child);
          }
        }
      };
      child.boundsInParentProperty().addListener(_function);
    }
  }
  
  protected void place(final Node child) {
    Pos _elvis = null;
    Pos _get = this.alignments.get(child);
    if (_get != null) {
      _elvis = _get;
    } else {
      _elvis = Pos.CENTER;
    }
    final Pos pos = _elvis;
    final Bounds bounds = child.getBoundsInParent();
    double _switchResult = (double) 0;
    HPos _hpos = pos.getHpos();
    if (_hpos != null) {
      switch (_hpos) {
        case LEFT:
          _switchResult = 0;
          break;
        case RIGHT:
          double _width = child.getScene().getWidth();
          double _width_1 = bounds.getWidth();
          _switchResult = (_width - _width_1);
          break;
        default:
          double _width_2 = child.getScene().getWidth();
          double _width_3 = bounds.getWidth();
          double _minus = (_width_2 - _width_3);
          _switchResult = (0.5 * _minus);
          break;
      }
    } else {
      double _width_2 = child.getScene().getWidth();
      double _width_3 = bounds.getWidth();
      double _minus = (_width_2 - _width_3);
      _switchResult = (0.5 * _minus);
    }
    child.setLayoutX(_switchResult);
    double _switchResult_1 = (double) 0;
    VPos _vpos = pos.getVpos();
    if (_vpos != null) {
      switch (_vpos) {
        case TOP:
          _switchResult_1 = 0;
          break;
        case BOTTOM:
          double _height = child.getScene().getHeight();
          double _height_1 = bounds.getHeight();
          _switchResult_1 = (_height - _height_1);
          break;
        default:
          double _height_2 = child.getScene().getHeight();
          double _height_3 = bounds.getHeight();
          double _minus_1 = (_height_2 - _height_3);
          _switchResult_1 = (0.5 * _minus_1);
          break;
      }
    } else {
      double _height_2 = child.getScene().getHeight();
      double _height_3 = bounds.getHeight();
      double _minus_1 = (_height_2 - _height_3);
      _switchResult_1 = (0.5 * _minus_1);
    }
    child.setLayoutY(_switchResult_1);
  }
}
