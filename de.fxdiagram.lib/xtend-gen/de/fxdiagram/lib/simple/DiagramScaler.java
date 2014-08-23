package de.fxdiagram.lib.simple;

import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.BoundsExtensions;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class DiagramScaler implements XActivatable {
  private ChangeListener<Bounds> boundsInLocalListener;
  
  private ChangeListener<Number> layoutListener;
  
  private XDiagram diagram;
  
  private ListChangeListener<XNode> listChangeListener;
  
  public DiagramScaler(final XDiagram diagram) {
    this.diagram = diagram;
    final ChangeListener<Bounds> _function = new ChangeListener<Bounds>() {
      public void changed(final ObservableValue<? extends Bounds> prop, final Bounds oldVal, final Bounds newVal) {
        DiagramScaler.this.scaleToFit();
      }
    };
    this.boundsInLocalListener = _function;
    final ChangeListener<Number> _function_1 = new ChangeListener<Number>() {
      public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
        DiagramScaler.this.scaleToFit();
      }
    };
    this.layoutListener = _function_1;
    final ListChangeListener<XNode> _function_2 = new ListChangeListener<XNode>() {
      public void onChanged(final ListChangeListener.Change<? extends XNode> change) {
        while (change.next()) {
          {
            boolean _wasAdded = change.wasAdded();
            if (_wasAdded) {
              List<? extends XNode> _addedSubList = change.getAddedSubList();
              final Procedure1<XNode> _function = new Procedure1<XNode>() {
                public void apply(final XNode it) {
                  ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = it.boundsInLocalProperty();
                  _boundsInLocalProperty.addListener(DiagramScaler.this.boundsInLocalListener);
                  DoubleProperty _layoutXProperty = it.layoutXProperty();
                  _layoutXProperty.addListener(DiagramScaler.this.layoutListener);
                  DoubleProperty _layoutYProperty = it.layoutYProperty();
                  _layoutYProperty.addListener(DiagramScaler.this.layoutListener);
                }
              };
              IterableExtensions.forEach(_addedSubList, _function);
            }
            boolean _wasRemoved = change.wasRemoved();
            if (_wasRemoved) {
              List<? extends XNode> _removed = change.getRemoved();
              final Procedure1<XNode> _function_1 = new Procedure1<XNode>() {
                public void apply(final XNode it) {
                  ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = it.boundsInLocalProperty();
                  _boundsInLocalProperty.removeListener(DiagramScaler.this.boundsInLocalListener);
                  DoubleProperty _layoutXProperty = it.layoutXProperty();
                  _layoutXProperty.removeListener(DiagramScaler.this.layoutListener);
                  DoubleProperty _layoutYProperty = it.layoutYProperty();
                  _layoutYProperty.removeListener(DiagramScaler.this.layoutListener);
                }
              };
              IterableExtensions.forEach(_removed, _function_1);
            }
          }
        }
      }
    };
    this.listChangeListener = _function_2;
  }
  
  public void scaleToFit() {
    ObservableList<XNode> _nodes = this.diagram.getNodes();
    boolean _isEmpty = _nodes.isEmpty();
    if (_isEmpty) {
      this.diagram.setScaleX(1);
      this.diagram.setScaleY(1);
      this.diagram.setClip(null);
    } else {
      ObservableList<XNode> _nodes_1 = this.diagram.getNodes();
      final Function1<XNode, BoundingBox> _function = new Function1<XNode, BoundingBox>() {
        public BoundingBox apply(final XNode it) {
          Bounds _layoutBounds = it.getLayoutBounds();
          double _layoutX = it.getLayoutX();
          double _layoutY = it.getLayoutY();
          return BoundsExtensions.translate(_layoutBounds, _layoutX, _layoutY);
        }
      };
      List<BoundingBox> _map = ListExtensions.<XNode, BoundingBox>map(_nodes_1, _function);
      final Function2<BoundingBox, BoundingBox, BoundingBox> _function_1 = new Function2<BoundingBox, BoundingBox, BoundingBox>() {
        public BoundingBox apply(final BoundingBox b0, final BoundingBox b1) {
          return BoundsExtensions.operator_plus(b0, b1);
        }
      };
      final BoundingBox myBounds = IterableExtensions.<BoundingBox>reduce(_map, _function_1);
      double _xifexpression = (double) 0;
      double _width = myBounds.getWidth();
      boolean _notEquals = (_width != 0);
      if (_notEquals) {
        double _width_1 = this.getWidth();
        double _width_2 = myBounds.getWidth();
        double _divide = (_width_1 / _width_2);
        _xifexpression = Math.min(1, _divide);
      } else {
        _xifexpression = 1;
      }
      final double newScaleX = _xifexpression;
      double _xifexpression_1 = (double) 0;
      double _height = myBounds.getHeight();
      boolean _notEquals_1 = (_height != 0);
      if (_notEquals_1) {
        double _height_1 = this.getHeight();
        double _height_2 = myBounds.getHeight();
        double _divide_1 = (_height_1 / _height_2);
        _xifexpression_1 = Math.min(1, _divide_1);
      } else {
        _xifexpression_1 = 1;
      }
      final double newScaleY = _xifexpression_1;
      final double newScale = Math.min(newScaleX, newScaleY);
      this.diagram.setScaleX(newScale);
      this.diagram.setScaleY(newScale);
      Rectangle _rectangle = new Rectangle();
      final Procedure1<Rectangle> _function_2 = new Procedure1<Rectangle>() {
        public void apply(final Rectangle it) {
          DiagramScaler.this.fit(it, newScale, newScaleX, newScaleY, myBounds);
        }
      };
      Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_2);
      this.diagram.setClip(_doubleArrow);
    }
  }
  
  protected void fit(final Rectangle it, final double newScale, final double newScaleX, final double newScaleY, final Bounds allNodesBounds) {
    it.setArcWidth((22 / newScale));
    it.setArcHeight((22 / newScale));
    double _minX = allNodesBounds.getMinX();
    it.setX(_minX);
    double _width = allNodesBounds.getWidth();
    it.setWidth(_width);
    if ((newScaleX > newScaleY)) {
      double _width_1 = this.getWidth();
      double _divide = (_width_1 / newScale);
      double _width_2 = allNodesBounds.getWidth();
      final double delta = (_divide - _width_2);
      double _x = it.getX();
      double _minus = (_x - (0.5 * delta));
      it.setX(_minus);
      double _width_3 = it.getWidth();
      double _plus = (_width_3 + delta);
      it.setWidth(_plus);
    }
    double _minY = allNodesBounds.getMinY();
    it.setY(_minY);
    double _height = allNodesBounds.getHeight();
    it.setHeight(_height);
    if ((newScaleY > newScaleX)) {
      double _height_1 = this.getHeight();
      double _divide_1 = (_height_1 / newScale);
      double _height_2 = allNodesBounds.getHeight();
      final double delta_1 = (_divide_1 - _height_2);
      double _y = it.getY();
      double _minus_1 = (_y - (0.5 * delta_1));
      it.setY(_minus_1);
      double _height_3 = it.getHeight();
      double _plus_1 = (_height_3 + delta_1);
      it.setHeight(_plus_1);
    }
  }
  
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.widthProperty.addListener(this.layoutListener);
      this.heightProperty.addListener(this.layoutListener);
      ObservableList<XNode> _nodes = this.diagram.getNodes();
      final Procedure1<XNode> _function = new Procedure1<XNode>() {
        public void apply(final XNode it) {
          ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = it.boundsInLocalProperty();
          _boundsInLocalProperty.addListener(DiagramScaler.this.boundsInLocalListener);
          DoubleProperty _layoutXProperty = it.layoutXProperty();
          _layoutXProperty.addListener(DiagramScaler.this.layoutListener);
          DoubleProperty _layoutYProperty = it.layoutYProperty();
          _layoutYProperty.addListener(DiagramScaler.this.layoutListener);
        }
      };
      IterableExtensions.<XNode>forEach(_nodes, _function);
      ObservableList<XNode> _nodes_1 = this.diagram.getNodes();
      _nodes_1.addListener(this.listChangeListener);
      Group _buttonLayer = this.diagram.getButtonLayer();
      ReadOnlyObjectProperty<Bounds> _layoutBoundsProperty = _buttonLayer.layoutBoundsProperty();
      _layoutBoundsProperty.addListener(this.boundsInLocalListener);
      this.scaleToFit();
      this.isActiveProperty.set(true);
    }
  }
  
  public void deactivate() {
    boolean _isActive = this.getIsActive();
    if (_isActive) {
      this.widthProperty.removeListener(this.layoutListener);
      this.heightProperty.removeListener(this.layoutListener);
      this.diagram.setClip(null);
      this.diagram.setScaleX(1);
      this.diagram.setScaleY(1);
      Group _buttonLayer = this.diagram.getButtonLayer();
      ReadOnlyObjectProperty<Bounds> _layoutBoundsProperty = _buttonLayer.layoutBoundsProperty();
      _layoutBoundsProperty.removeListener(this.boundsInLocalListener);
      ObservableList<XNode> _nodes = this.diagram.getNodes();
      _nodes.removeListener(this.listChangeListener);
      ObservableList<XNode> _nodes_1 = this.diagram.getNodes();
      final Procedure1<XNode> _function = new Procedure1<XNode>() {
        public void apply(final XNode it) {
          ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = it.boundsInLocalProperty();
          _boundsInLocalProperty.removeListener(DiagramScaler.this.boundsInLocalListener);
          DoubleProperty _layoutXProperty = it.layoutXProperty();
          _layoutXProperty.removeListener(DiagramScaler.this.layoutListener);
          DoubleProperty _layoutYProperty = it.layoutYProperty();
          _layoutYProperty.removeListener(DiagramScaler.this.layoutListener);
        }
      };
      IterableExtensions.<XNode>forEach(_nodes_1, _function);
      this.isActiveProperty.set(false);
    }
  }
  
  private SimpleDoubleProperty widthProperty = new SimpleDoubleProperty(this, "width",_initWidth());
  
  private static final double _initWidth() {
    return 80;
  }
  
  public double getWidth() {
    return this.widthProperty.get();
  }
  
  public void setWidth(final double width) {
    this.widthProperty.set(width);
  }
  
  public DoubleProperty widthProperty() {
    return this.widthProperty;
  }
  
  private SimpleDoubleProperty heightProperty = new SimpleDoubleProperty(this, "height",_initHeight());
  
  private static final double _initHeight() {
    return 60;
  }
  
  public double getHeight() {
    return this.heightProperty.get();
  }
  
  public void setHeight(final double height) {
    this.heightProperty.set(height);
  }
  
  public DoubleProperty heightProperty() {
    return this.heightProperty;
  }
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
}
