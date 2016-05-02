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
import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

/**
 * Common superclass of {@link XNode}, {@link XConnection} and {@link XControlPoint}.
 * 
 * <ul>
 * <li>Handles activation and FX node creation.</li>
 * <li>Allows composing {@link Behavior}s.</li>
 * <li>Supports selection.</li>
 * </ul>
 */
@Logging
@SuppressWarnings("all")
public abstract class XShape extends Parent implements XActivatable {
  private ObjectProperty<Node> nodeProperty = new SimpleObjectProperty<Node>(this, "node");
  
  private ObservableMap<Class<? extends Behavior>, Behavior> behaviors = FXCollections.<Class<? extends Behavior>, Behavior>observableHashMap();
  
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
          this.addNodeAsChild(newNode);
        }
      }
      _xblockexpression = this.nodeProperty.get();
    }
    return _xblockexpression;
  }
  
  protected boolean addNodeAsChild(final Node newNode) {
    ObservableList<Node> _children = this.getChildren();
    return _children.add(newNode);
  }
  
  public ObjectProperty<Node> nodeProperty() {
    return this.nodeProperty;
  }
  
  /**
   * Override this to create your FX nodes for the non-active shape, e.g. as used by
   * node choosers.
   * There is no connection to any scene graph yet, so {@link CoreExtensions} will return
   * <code>null</code>.
   */
  protected abstract Node createNode();
  
  /**
   * Override this to update your FX node, e.g. on connection or on activation.
   * You are now connected to the scenegraph, and can safely use {@link CoreExtensions}
   * to access the diagram/root.
   * 
   * This method may be called multiple times.
   */
  public void initializeGraphics() {
    Node _node = this.getNode();
    boolean _equals = Objects.equal(_node, null);
    if (_equals) {
      XShape.LOG.severe("Node is null");
    }
  }
  
  /**
   * Don't override this unless you know what you're doing.
   * Override {@link #doActivate} instead.
   */
  @Override
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      try {
        this.isActiveProperty.set(true);
        this.initializeGraphics();
        this.doActivate();
        InitializingListener<Boolean> _initializingListener = new InitializingListener<Boolean>();
        final Procedure1<InitializingListener<Boolean>> _function = (InitializingListener<Boolean> it) -> {
          final Procedure1<Boolean> _function_1 = (Boolean it_1) -> {
            this.selectionFeedback((it_1).booleanValue());
          };
          it.setSet(_function_1);
        };
        InitializingListener<Boolean> _doubleArrow = ObjectExtensions.<InitializingListener<Boolean>>operator_doubleArrow(_initializingListener, _function);
        CoreExtensions.<Boolean>addInitializingListener(this.selectedProperty, _doubleArrow);
        InitializingMapListener<Class<? extends Behavior>, Behavior> _initializingMapListener = new InitializingMapListener<Class<? extends Behavior>, Behavior>();
        final Procedure1<InitializingMapListener<Class<? extends Behavior>, Behavior>> _function_1 = (InitializingMapListener<Class<? extends Behavior>, Behavior> it) -> {
          final Procedure2<Class<? extends Behavior>, Behavior> _function_2 = (Class<? extends Behavior> key, Behavior value) -> {
            value.activate();
          };
          it.setPut(_function_2);
        };
        InitializingMapListener<Class<? extends Behavior>, Behavior> _doubleArrow_1 = ObjectExtensions.<InitializingMapListener<Class<? extends Behavior>, Behavior>>operator_doubleArrow(_initializingMapListener, _function_1);
        CoreExtensions.<Class<? extends Behavior>, Behavior>addInitializingListener(this.behaviors, _doubleArrow_1);
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception exc = (Exception)_t;
          String _message = exc.getMessage();
          XShape.LOG.severe(_message);
          exc.printStackTrace();
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
  }
  
  /**
   * Override this to add behavior and register event listeners on activation.
   */
  protected abstract void doActivate();
  
  public <T extends Behavior> T getBehavior(final Class<T> key) {
    Behavior _get = this.behaviors.get(key);
    return ((T) _get);
  }
  
  public void addBehavior(final Behavior behavior) {
    Class<? extends Behavior> _behaviorKey = behavior.getBehaviorKey();
    this.behaviors.put(_behaviorKey, behavior);
    boolean _isActive = this.getIsActive();
    if (_isActive) {
      behavior.activate();
    }
  }
  
  public Behavior removeBehavior(final Class<? extends Behavior> key) {
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
  
  /**
   * The 'real' bounds for layout calculation, i.e. without any scaling caused by selection.
   */
  public Bounds getSnapBounds() {
    return this.getBoundsInLocal();
  }
  
  public Dimension2D getAutoLayoutDimension() {
    Bounds _snapBounds = this.getSnapBounds();
    double _width = _snapBounds.getWidth();
    Bounds _snapBounds_1 = this.getSnapBounds();
    double _height = _snapBounds_1.getHeight();
    return new Dimension2D(_width, _height);
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
