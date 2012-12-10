package de.itemis.javafx.diagram;

import com.google.common.base.Objects;
import de.itemis.javafx.diagram.AnchorPoints;
import de.itemis.javafx.diagram.DragContext;
import de.itemis.javafx.diagram.RapidButton;
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
  
  private RapidButton rapidButton = new Function0<RapidButton>() {
    public RapidButton apply() {
      RapidButton _rapidButton = new RapidButton("R");
      return _rapidButton;
    }
  }.apply();
  
  public AnchorPoints setNode(final Node node) {
    AnchorPoints _xblockexpression = null;
    {
      this.node = node;
      ObservableList<Node> _children = this.getChildren();
      _children.add(node);
      ObservableList<Node> _children_1 = this.getChildren();
      _children_1.add(this.rapidButton);
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
              boolean _isShortcutDown = it.isShortcutDown();
              _and = (_and_1 && _isShortcutDown);
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
      final Procedure1<MouseEvent> _function_4 = new Procedure1<MouseEvent>() {
          public void apply(final MouseEvent it) {
            ShapeContainer.this.rapidButton.show();
          }
        };
      node.setOnMouseEntered(new EventHandler<MouseEvent>() {
          public void handle(MouseEvent arg0) {
            _function_4.apply(arg0);
          }
      });
      final Procedure1<MouseEvent> _function_5 = new Procedure1<MouseEvent>() {
          public void apply(final MouseEvent it) {
            ShapeContainer.this.rapidButton.fade();
          }
        };
      node.setOnMouseExited(new EventHandler<MouseEvent>() {
          public void handle(MouseEvent arg0) {
            _function_5.apply(arg0);
          }
      });
      AnchorPoints _anchorPoints = new AnchorPoints(this);
      AnchorPoints _anchorPoints_1 = this.anchorPoints = _anchorPoints;
      _xblockexpression = (_anchorPoints_1);
    }
    return _xblockexpression;
  }
  
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
  
  protected Effect getSelectionEffect() {
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
  
  public DragContext mousePressed(final MouseEvent it) {
    double _screenX = it.getScreenX();
    double _screenY = it.getScreenY();
    double _translateX = this.getTranslateX();
    double _translateY = this.getTranslateY();
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
    this.setTranslateX(_plus);
    double _initialY = this.dragContext.getInitialY();
    double _mouseAnchorY = this.dragContext.getMouseAnchorY();
    double _minus_1 = (_initialY - _mouseAnchorY);
    double _screenY = it.getScreenY();
    double _plus_1 = (_minus_1 + _screenY);
    this.setTranslateY(_plus_1);
  }
  
  public AnchorPoints getAnchorPoints() {
    return this.anchorPoints;
  }
  
  public Node getNode() {
    return this.node;
  }
}
