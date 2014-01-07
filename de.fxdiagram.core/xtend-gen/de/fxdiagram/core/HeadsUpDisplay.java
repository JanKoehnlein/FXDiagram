package de.fxdiagram.core;

import com.google.common.base.Objects;
import java.util.Map;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
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
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class HeadsUpDisplay extends Group {
  private Map<Node,Pos> alignments = CollectionLiterals.<Node, Pos>newHashMap();
  
  private ChangeListener<Number> sceneListener;
  
  public HeadsUpDisplay() {
    final ChangeListener<Number> _function = new ChangeListener<Number>() {
      public void changed(final ObservableValue<? extends Number> property, final Number oldVlaue, final Number newValue) {
        ObservableList<Node> _children = HeadsUpDisplay.this.getChildren();
        final Procedure1<Node> _function = new Procedure1<Node>() {
          public void apply(final Node it) {
            HeadsUpDisplay.this.place(it);
          }
        };
        IterableExtensions.<Node>forEach(_children, _function);
      }
    };
    this.sceneListener = _function;
    ReadOnlyObjectProperty<Scene> _sceneProperty = this.sceneProperty();
    final ChangeListener<Scene> _function_1 = new ChangeListener<Scene>() {
      public void changed(final ObservableValue<? extends Scene> property, final Scene oldVal, final Scene newVal) {
        ReadOnlyDoubleProperty _widthProperty = null;
        if (oldVal!=null) {
          _widthProperty=oldVal.widthProperty();
        }
        if (_widthProperty!=null) {
          _widthProperty.removeListener(HeadsUpDisplay.this.sceneListener);
        }
        ReadOnlyDoubleProperty _heightProperty = null;
        if (oldVal!=null) {
          _heightProperty=oldVal.heightProperty();
        }
        if (_heightProperty!=null) {
          _heightProperty.removeListener(HeadsUpDisplay.this.sceneListener);
        }
        ReadOnlyDoubleProperty _widthProperty_1 = null;
        if (newVal!=null) {
          _widthProperty_1=newVal.widthProperty();
        }
        if (_widthProperty_1!=null) {
          _widthProperty_1.addListener(HeadsUpDisplay.this.sceneListener);
        }
        ReadOnlyDoubleProperty _heightProperty_1 = null;
        if (newVal!=null) {
          _heightProperty_1=newVal.heightProperty();
        }
        if (_heightProperty_1!=null) {
          _heightProperty_1.addListener(HeadsUpDisplay.this.sceneListener);
        }
      }
    };
    _sceneProperty.addListener(_function_1);
  }
  
  public void add(final Node child, final Pos pos) {
    ObservableList<Node> _children = this.getChildren();
    _children.add(child);
    this.alignments.put(child, pos);
    this.place(child);
    ReadOnlyObjectProperty<Bounds> _boundsInParentProperty = child.boundsInParentProperty();
    final ChangeListener<Bounds> _function = new ChangeListener<Bounds>() {
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
    _boundsInParentProperty.addListener(_function);
  }
  
  protected void place(final Node child) {
    Pos _elvis = null;
    Pos _get = this.alignments.get(child);
    if (_get != null) {
      _elvis = _get;
    } else {
      _elvis = ObjectExtensions.<Pos>operator_elvis(_get, Pos.CENTER);
    }
    final Pos pos = _elvis;
    final Bounds bounds = child.getBoundsInParent();
    double _switchResult = (double) 0;
    HPos _hpos = pos.getHpos();
    final HPos _switchValue = _hpos;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HPos.LEFT)) {
        _matched=true;
        _switchResult = 0;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HPos.RIGHT)) {
        _matched=true;
        Scene _scene = child.getScene();
        double _width = _scene.getWidth();
        double _width_1 = bounds.getWidth();
        double _minus = (_width - _width_1);
        _switchResult = _minus;
      }
    }
    if (!_matched) {
      Scene _scene_1 = child.getScene();
      double _width_2 = _scene_1.getWidth();
      double _width_3 = bounds.getWidth();
      double _minus_1 = (_width_2 - _width_3);
      double _multiply = (0.5 * _minus_1);
      _switchResult = _multiply;
    }
    child.setLayoutX(_switchResult);
    double _switchResult_1 = (double) 0;
    VPos _vpos = pos.getVpos();
    final VPos _switchValue_1 = _vpos;
    boolean _matched_1 = false;
    if (!_matched_1) {
      if (Objects.equal(_switchValue_1,VPos.TOP)) {
        _matched_1=true;
        _switchResult_1 = 0;
      }
    }
    if (!_matched_1) {
      if (Objects.equal(_switchValue_1,VPos.BOTTOM)) {
        _matched_1=true;
        Scene _scene_2 = child.getScene();
        double _height = _scene_2.getHeight();
        double _height_1 = bounds.getHeight();
        double _minus_2 = (_height - _height_1);
        _switchResult_1 = _minus_2;
      }
    }
    if (!_matched_1) {
      Scene _scene_3 = child.getScene();
      double _height_2 = _scene_3.getHeight();
      double _height_3 = bounds.getHeight();
      double _minus_3 = (_height_2 - _height_3);
      double _multiply_1 = (0.5 * _minus_3);
      _switchResult_1 = _multiply_1;
    }
    child.setLayoutY(_switchResult_1);
  }
}
