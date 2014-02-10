package de.fxdiagram.core;

import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.behavior.Behavior;
import java.util.Collection;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public abstract class XShape extends Parent implements XActivatable {
  private ObservableMap<Class<? extends Behavior>,Behavior> behaviors = FXCollections.<Class<? extends Behavior>, Behavior>observableHashMap();
  
  protected boolean setNode(final Node node) {
    boolean _xblockexpression = false;
    {
      this.nodeProperty.set(node);
      ObservableList<Node> _children = this.getChildren();
      _xblockexpression = _children.setAll(node);
    }
    return _xblockexpression;
  }
  
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.activatePreview();
      this.doActivate();
      this.isActiveProperty.set(true);
      final ChangeListener<Boolean> _function = new ChangeListener<Boolean>() {
        public void changed(final ObservableValue<? extends Boolean> property, final Boolean oldVlaue, final Boolean newValue) {
          XShape.this.selectionFeedback((newValue).booleanValue());
          if ((newValue).booleanValue()) {
            XShape.this.toFront();
          }
        }
      };
      this.selectedProperty.addListener(_function);
      Collection<Behavior> _values = this.behaviors.values();
      final Procedure1<Behavior> _function_1 = new Procedure1<Behavior>() {
        public void apply(final Behavior it) {
          it.activate();
        }
      };
      IterableExtensions.<Behavior>forEach(_values, _function_1);
      final MapChangeListener<Class<? extends Behavior>,Behavior> _function_2 = new MapChangeListener<Class<? extends Behavior>,Behavior>() {
        public void onChanged(final MapChangeListener.Change<? extends Class<? extends Behavior>,? extends Behavior> change) {
          boolean _isActive = XShape.this.getIsActive();
          if (_isActive) {
            boolean _wasAdded = change.wasAdded();
            if (_wasAdded) {
              Behavior _valueAdded = change.getValueAdded();
              _valueAdded.activate();
            }
          }
        }
      };
      final MapChangeListener<Class<? extends Behavior>,Behavior> behaviorActivator = _function_2;
      this.behaviors.addListener(behaviorActivator);
    }
  }
  
  public void activatePreview() {
    boolean _isPreviewActive = this.getIsPreviewActive();
    boolean _not = (!_isPreviewActive);
    if (_not) {
      this.doActivatePreview();
      this.isPreviewActiveProperty.set(true);
    }
  }
  
  protected abstract void doActivatePreview();
  
  protected abstract void doActivate();
  
  public <T extends Behavior> T getBehavior(final Class<T> key) {
    Behavior _get = this.behaviors.get(key);
    return ((T) _get);
  }
  
  public Behavior addBehavior(final Behavior behavior) {
    Class<? extends Behavior> _behaviorKey = behavior.getBehaviorKey();
    return this.behaviors.put(_behaviorKey, behavior);
  }
  
  public Behavior removeBehavior(final String key) {
    return this.behaviors.remove(key);
  }
  
  public void selectionFeedback(final boolean isSelected) {
  }
  
  public boolean isSelectable() {
    return this.getIsActive();
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
  
  public Bounds getSnapBounds() {
    return this.getBoundsInLocal();
  }
  
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
  
  private ReadOnlyBooleanWrapper isPreviewActiveProperty = new ReadOnlyBooleanWrapper(this, "isPreviewActive");
  
  public boolean getIsPreviewActive() {
    return this.isPreviewActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isPreviewActiveProperty() {
    return this.isPreviewActiveProperty.getReadOnlyProperty();
  }
}
