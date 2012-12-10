package de.itemis.javafx.diagram;

import com.google.common.base.Objects;
import de.itemis.javafx.diagram.AnchorPoints;
import de.itemis.javafx.diagram.DragContext;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3;

@SuppressWarnings("all")
public class ShapeContainer extends Group {
  private Node node;
  
  private BooleanProperty isSelected = new Function0<BooleanProperty>() {
    public BooleanProperty apply() {
      SimpleBooleanProperty _simpleBooleanProperty = new SimpleBooleanProperty();
      return _simpleBooleanProperty;
    }
  }.apply();
  
  private Effect selectionEffect;
  
  private AnchorPoints anchorPoints;
  
  private DragContext dragContext;
  
  public BooleanProperty getSelectedProperty() {
    return this.isSelected;
  }
  
  public boolean isSelected() {
    boolean _get = this.isSelected.get();
    return _get;
  }
  
  public void setSelected(final boolean isSelected) {
    this.isSelected.set(isSelected);
  }
  
  public Effect getSelectionEffect() {
    Effect _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.selectionEffect, null);
      if (_equals) {
        DropShadow _dropShadow = new DropShadow();
        final Procedure1<DropShadow> _function = new Procedure1<DropShadow>() {
            public void apply(final DropShadow it) {
              it.setOffsetX(5.0);
              it.setOffsetY(5.0);
            }
          };
        DropShadow _doubleArrow = ObjectExtensions.<DropShadow>operator_doubleArrow(_dropShadow, _function);
        this.selectionEffect = _doubleArrow;
      }
      _xblockexpression = (this.selectionEffect);
    }
    return _xblockexpression;
  }
  
  public ShapeContainer(final Node node) {
    this.node = node;
    ObservableList<Node> _children = this.getChildren();
    _children.add(node);
    final Procedure3<ObservableValue<? extends Boolean>,Boolean,Boolean> _function = new Procedure3<ObservableValue<? extends Boolean>,Boolean,Boolean>() {
        public void apply(final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue) {
          if ((newValue).booleanValue()) {
            Effect _selectionEffect = ShapeContainer.this.getSelectionEffect();
            ShapeContainer.this.setEffect(_selectionEffect);
          } else {
            ShapeContainer.this.setEffect(null);
          }
        }
      };
    final ChangeListener<Boolean> selectionListener = new ChangeListener<Boolean>() {
        public void changed(ObservableValue<? extends Boolean> arg0,Boolean arg1,Boolean arg2) {
          _function.apply(arg0,arg1,arg2);
        }
    };
    this.isSelected.addListener(selectionListener);
    final Procedure1<MouseEvent> _function_1 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          ShapeContainer.this.mousePressed(it);
          ShapeContainer.this.isSelected.set(true);
        }
      };
    node.setOnMousePressed(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_1.apply(arg0);
        }
    });
    final Procedure1<MouseEvent> _function_2 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          boolean _and = false;
          boolean _and_1 = false;
          double _mouseAnchorX = ShapeContainer.this.dragContext.getMouseAnchorX();
          double _screenX = it.getScreenX();
          boolean _equals = (_mouseAnchorX == _screenX);
          if (!_equals) {
            _and_1 = false;
          } else {
            double _mouseAnchorY = ShapeContainer.this.dragContext.getMouseAnchorY();
            double _screenY = it.getScreenY();
            boolean _equals_1 = (_mouseAnchorY == _screenY);
            _and_1 = (_equals && _equals_1);
          }
          if (!_and_1) {
            _and = false;
          } else {
            boolean _isControlDown = it.isControlDown();
            _and = (_and_1 && _isControlDown);
          }
          if (_and) {
            boolean _isWasSeleceted = ShapeContainer.this.dragContext.isWasSeleceted();
            boolean _not = (!_isWasSeleceted);
            ShapeContainer.this.isSelected.set(_not);
          }
        }
      };
    node.setOnMouseReleased(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_2.apply(arg0);
        }
    });
    final Procedure1<MouseEvent> _function_3 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          ShapeContainer.this.mouseDragged(it);
        }
      };
    node.setOnMouseDragged(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_3.apply(arg0);
        }
    });
    AnchorPoints _anchorPoints = new AnchorPoints(node);
    this.anchorPoints = _anchorPoints;
  }
  
  public DragContext mousePressed(final MouseEvent it) {
    double _screenX = it.getScreenX();
    double _screenY = it.getScreenY();
    double _translateX = this.node.getTranslateX();
    double _translateY = this.node.getTranslateY();
    boolean _get = this.isSelected.get();
    DragContext _dragContext = new DragContext(_screenX, _screenY, _translateX, _translateY, _get);
    DragContext _dragContext_1 = this.dragContext = _dragContext;
    return _dragContext_1;
  }
  
  public void mouseDragged(final MouseEvent it) {
    double _initialX = this.dragContext.getInitialX();
    double _mouseAnchorX = this.dragContext.getMouseAnchorX();
    double _minus = (_initialX - _mouseAnchorX);
    double _screenX = it.getScreenX();
    double _plus = (_minus + _screenX);
    this.node.setTranslateX(_plus);
    double _initialY = this.dragContext.getInitialY();
    double _mouseAnchorY = this.dragContext.getMouseAnchorY();
    double _minus_1 = (_initialY - _mouseAnchorY);
    double _screenY = it.getScreenY();
    double _plus_1 = (_minus_1 + _screenY);
    this.node.setTranslateY(_plus_1);
  }
  
  public AnchorPoints getAnchorPoints() {
    return this.anchorPoints;
  }
}
