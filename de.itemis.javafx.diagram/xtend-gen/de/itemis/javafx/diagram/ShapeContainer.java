package de.itemis.javafx.diagram;

import de.itemis.javafx.diagram.AnchorPoints;
import de.itemis.javafx.diagram.Diagram;
import de.itemis.javafx.diagram.behavior.AddRapidButtonBehavior;
import de.itemis.javafx.diagram.behavior.SelectionBehavior;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Light.Distant;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3;

@SuppressWarnings("all")
public class ShapeContainer extends Group {
  private Node node;
  
  private Diagram diagram;
  
  private DropShadow selectionEffect;
  
  private Lighting mouseOverEffect;
  
  private AnchorPoints anchorPoints;
  
  private SelectionBehavior selectionBehavior;
  
  private AddRapidButtonBehavior rapidButtonBehavior;
  
  public void setNode(final Node node) {
    this.node = node;
    ObservableList<Node> _children = this.getChildren();
    _children.add(node);
    SelectionBehavior _selectionBehavior = new SelectionBehavior(this);
    this.selectionBehavior = _selectionBehavior;
    AnchorPoints _anchorPoints = new AnchorPoints(this);
    this.anchorPoints = _anchorPoints;
    AddRapidButtonBehavior _addRapidButtonBehavior = new AddRapidButtonBehavior(this);
    this.rapidButtonBehavior = _addRapidButtonBehavior;
  }
  
  public void setDiagram(final Diagram diagram) {
    this.diagram = diagram;
    final Procedure3<ObservableValue<? extends Boolean>,Boolean,Boolean> _function = new Procedure3<ObservableValue<? extends Boolean>,Boolean,Boolean>() {
        public void apply(final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue) {
          if ((newValue).booleanValue()) {
            DropShadow _selectionEffect = ShapeContainer.this.getSelectionEffect();
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
    BooleanProperty _selectedProperty = this.selectionBehavior.getSelectedProperty();
    _selectedProperty.addListener(selectionListener);
    this.selectionBehavior.activate(diagram);
    this.rapidButtonBehavior.activate(diagram);
    final Procedure1<MouseEvent> _function_1 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          DropShadow _selectionEffect = ShapeContainer.this.getSelectionEffect();
          Lighting _mouseOverEffect = ShapeContainer.this.getMouseOverEffect();
          _selectionEffect.setInput(_mouseOverEffect);
          Effect _xifexpression = null;
          boolean _isSelected = ShapeContainer.this.isSelected();
          if (_isSelected) {
            DropShadow _selectionEffect_1 = ShapeContainer.this.getSelectionEffect();
            _xifexpression = _selectionEffect_1;
          } else {
            Lighting _mouseOverEffect_1 = ShapeContainer.this.getMouseOverEffect();
            _xifexpression = _mouseOverEffect_1;
          }
          ShapeContainer.this.setEffect(_xifexpression);
        }
      };
    this.setOnMouseEntered(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_1.apply(arg0);
        }
    });
    final Procedure1<MouseEvent> _function_2 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          DropShadow _selectionEffect = ShapeContainer.this.getSelectionEffect();
          _selectionEffect.setInput(null);
          DropShadow _xifexpression = null;
          boolean _isSelected = ShapeContainer.this.isSelected();
          if (_isSelected) {
            DropShadow _selectionEffect_1 = ShapeContainer.this.getSelectionEffect();
            _xifexpression = _selectionEffect_1;
          } else {
            _xifexpression = null;
          }
          ShapeContainer.this.setEffect(_xifexpression);
        }
      };
    this.setOnMouseExited(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_2.apply(arg0);
        }
    });
  }
  
  public boolean isSelected() {
    BooleanProperty _selectedProperty = this.selectionBehavior.getSelectedProperty();
    boolean _get = _selectedProperty.get();
    return _get;
  }
  
  public void setSelected(final boolean isSelected) {
    BooleanProperty _selectedProperty = this.selectionBehavior.getSelectedProperty();
    _selectedProperty.set(isSelected);
  }
  
  public SelectionBehavior getSelectionBehavior() {
    return this.selectionBehavior;
  }
  
  protected DropShadow getSelectionEffect() {
    DropShadow _xblockexpression = null;
    {
      boolean _equals = ObjectExtensions.operator_equals(this.selectionEffect, null);
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
  
  protected Lighting getMouseOverEffect() {
    Lighting _xblockexpression = null;
    {
      boolean _equals = ObjectExtensions.operator_equals(this.mouseOverEffect, null);
      if (_equals) {
        Lighting _lighting = new Lighting();
        final Procedure1<Lighting> _function = new Procedure1<Lighting>() {
            public void apply(final Lighting it) {
              Distant _distant = new Distant();
              final Procedure1<Distant> _function = new Procedure1<Distant>() {
                  public void apply(final Distant it) {
                    it.setElevation(48);
                    int _minus = (-135);
                    it.setAzimuth(_minus);
                  }
                };
              Distant _doubleArrow = ObjectExtensions.<Distant>operator_doubleArrow(_distant, _function);
              it.setLight(_doubleArrow);
              it.setSurfaceScale(0.1);
            }
          };
        Lighting _doubleArrow = ObjectExtensions.<Lighting>operator_doubleArrow(_lighting, _function);
        this.mouseOverEffect = _doubleArrow;
      }
      _xblockexpression = (this.mouseOverEffect);
    }
    return _xblockexpression;
  }
  
  public AnchorPoints getAnchorPoints() {
    return this.anchorPoints;
  }
  
  public Node getNode() {
    return this.node;
  }
}
