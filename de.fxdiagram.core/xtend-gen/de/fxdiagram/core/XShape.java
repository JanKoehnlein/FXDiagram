package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.InitializingListener;
import de.fxdiagram.core.extensions.InitializingMapListener;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@Logging
@SuppressWarnings("all")
public abstract class XShape extends Parent implements XActivatable {
  private ObjectProperty<Node> nodeProperty = new SimpleObjectProperty<Node>(this, "node");
  
  private ObservableMap<Class<? extends Behavior>,Behavior> behaviors = FXCollections.<Class<? extends Behavior>, Behavior>observableHashMap();
  
  public Node getNode() {
    Node _xblockexpression = null;
    {
      Node _get = this.nodeProperty.get();
      boolean _equals = Objects.equal(_get, null);
      if (_equals) {
        final Node newNode = this.createNode();
        boolean _notEquals = (!Objects.equal(newNode, null));
        if (_notEquals) {
          this.nodeProperty.set(newNode);
          ObservableList<Node> _children = this.getChildren();
          _children.add(newNode);
        }
      }
      _xblockexpression = this.nodeProperty.get();
    }
    return _xblockexpression;
  }
  
  public ObjectProperty<Node> nodeProperty() {
    return this.nodeProperty;
  }
  
  protected abstract Node createNode();
  
  public void initializeGraphics() {
    Node _node = this.getNode();
    boolean _equals = Objects.equal(_node, null);
    if (_equals) {
      XShape.LOG.severe("Node is null");
    }
  }
  
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.initializeGraphics();
      this.doActivate();
      this.isActiveProperty.set(true);
      InitializingListener<Boolean> _initializingListener = new InitializingListener<Boolean>();
      final Procedure1<InitializingListener<Boolean>> _function = new Procedure1<InitializingListener<Boolean>>() {
        public void apply(final InitializingListener<Boolean> it) {
          final Procedure1<Boolean> _function = new Procedure1<Boolean>() {
            public void apply(final Boolean it) {
              XShape.this.selectionFeedback((it).booleanValue());
              if ((it).booleanValue()) {
                XShape.this.toFront();
              }
            }
          };
          it.setSet(_function);
        }
      };
      InitializingListener<Boolean> _doubleArrow = ObjectExtensions.<InitializingListener<Boolean>>operator_doubleArrow(_initializingListener, _function);
      CoreExtensions.<Boolean>addInitializingListener(this.selectedProperty, _doubleArrow);
      InitializingMapListener<Class<? extends Behavior>,Behavior> _initializingMapListener = new InitializingMapListener<Class<? extends Behavior>, Behavior>();
      final Procedure1<InitializingMapListener<Class<? extends Behavior>,Behavior>> _function_1 = new Procedure1<InitializingMapListener<Class<? extends Behavior>,Behavior>>() {
        public void apply(final InitializingMapListener<Class<? extends Behavior>,Behavior> it) {
          final Procedure2<Class<? extends Behavior>,Behavior> _function = new Procedure2<Class<? extends Behavior>,Behavior>() {
            public void apply(final Class<? extends Behavior> key, final Behavior value) {
              value.activate();
            }
          };
          it.setPut(_function);
        }
      };
      InitializingMapListener<Class<? extends Behavior>,Behavior> _doubleArrow_1 = ObjectExtensions.<InitializingMapListener<Class<? extends Behavior>,Behavior>>operator_doubleArrow(_initializingMapListener, _function_1);
      CoreExtensions.<Class<? extends Behavior>, Behavior>addInitializingListener(this.behaviors, _doubleArrow_1);
    }
  }
  
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
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.XShape");
    ;
  
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
