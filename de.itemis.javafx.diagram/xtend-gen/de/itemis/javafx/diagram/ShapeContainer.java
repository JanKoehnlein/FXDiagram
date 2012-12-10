package de.itemis.javafx.diagram;

import de.itemis.javafx.diagram.AnchorPoints;
import de.itemis.javafx.diagram.Diagram;
import de.itemis.javafx.diagram.behavior.AddRapidButtonBehavior;
import de.itemis.javafx.diagram.behavior.SelectionBehavior;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.BorderPane;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3;

@SuppressWarnings("all")
public class ShapeContainer extends Group {
  private Node node;
  
  private Diagram diagram;
  
  private Effect selectionEffect;
  
  private AnchorPoints anchorPoints;
  
  private SelectionBehavior selectionBehavior;
  
  private AddRapidButtonBehavior rapidButtonBehavior;
  
  public AddRapidButtonBehavior setNode(final Node node) {
    AddRapidButtonBehavior _xblockexpression = null;
    {
      this.node = node;
      ObservableList<Node> _children = this.getChildren();
      _children.add(node);
      Insets _insets = new Insets(3, 3, 3, 3);
      BorderPane.setMargin(node, _insets);
      final Procedure3<ObservableValue<? extends Boolean>,Boolean,Boolean> _function = new Procedure3<ObservableValue<? extends Boolean>,Boolean,Boolean>() {
          public void apply(final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue) {
            if ((newValue).booleanValue()) {
              ShapeContainer.this.setEffect(ShapeContainer.this.selectionEffect);
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
      SelectionBehavior _selectionBehavior = new SelectionBehavior(this);
      this.selectionBehavior = _selectionBehavior;
      BooleanProperty _selectedProperty = this.selectionBehavior.getSelectedProperty();
      _selectedProperty.addListener(selectionListener);
      AnchorPoints _anchorPoints = new AnchorPoints(this);
      this.anchorPoints = _anchorPoints;
      AddRapidButtonBehavior _addRapidButtonBehavior = new AddRapidButtonBehavior(this);
      AddRapidButtonBehavior _rapidButtonBehavior = this.rapidButtonBehavior = _addRapidButtonBehavior;
      _xblockexpression = (_rapidButtonBehavior);
    }
    return _xblockexpression;
  }
  
  public void setDiagram(final Diagram diagram) {
    this.diagram = diagram;
    this.selectionBehavior.activate(diagram);
    this.rapidButtonBehavior.activate(diagram);
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
  
  protected Effect getSelectionEffect() {
    Effect _xblockexpression = null;
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
  
  public AnchorPoints getAnchorPoints() {
    return this.anchorPoints;
  }
  
  public Node getNode() {
    return this.node;
  }
}
