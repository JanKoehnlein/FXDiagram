package de.fxdiagram.core;

import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.behavior.MoveBehavior;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

@SuppressWarnings("all")
public abstract class XShape extends Parent implements XActivatable {
  protected boolean setNode(final Node node) {
    boolean _xblockexpression = false;
    {
      this.nodeProperty.set(node);
      ObservableList<Node> _children = this.getChildren();
      boolean _setAll = _children.setAll(node);
      _xblockexpression = (_setAll);
    }
    return _xblockexpression;
  }
  
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.doActivate();
    }
    this.isActiveProperty.set(true);
  }
  
  protected abstract void doActivate();
  
  public boolean isSelectable() {
    boolean _isActive = this.getIsActive();
    return _isActive;
  }
  
  public void select(final MouseEvent it) {
    this.setSelected(true);
  }
  
  public void toggleSelect(final MouseEvent it) {
    boolean _isShortcutDown = it.isShortcutDown();
    if (_isShortcutDown) {
      boolean _selected = this.getSelected();
      boolean _not = (!_selected);
      this.setSelected(_not);
    }
  }
  
  public abstract MoveBehavior getMoveBehavior();
  
  private ReadOnlyObjectWrapper<Node> nodeProperty = new ReadOnlyObjectWrapper<Node>(this, "node");
  
  public Node getNode() {
    return this.nodeProperty.get();
    
  }
  
  public ReadOnlyObjectProperty<Node> nodeProperty() {
    return this.nodeProperty.getReadOnlyProperty();
    
  }
  
  private SimpleBooleanProperty selectedProperty = new SimpleBooleanProperty(this, "selected");
  
  public boolean getSelected() {
    return this.selectedProperty.get();
    
  }
  
  public void setSelected(final boolean selected) {
    this.selectedProperty.set(selected);
    
  }
  
  public BooleanProperty selectedProperty() {
    return this.selectedProperty;
    
  }
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
    
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
    
  }
}
