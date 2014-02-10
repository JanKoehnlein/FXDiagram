package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagramChildrenListener;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.auxlines.AuxiliaryLinesSupport;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.DiagramNavigationBehavior;
import de.fxdiagram.core.behavior.NavigationBehavior;
import de.fxdiagram.core.extensions.AccumulativeTransform2D;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.InitializingListListener;
import de.fxdiagram.core.extensions.InitializingListener;
import de.fxdiagram.core.extensions.InitializingMapListener;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.ModelLoad;
import de.fxdiagram.core.model.XModelProvider;
import java.util.HashMap;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@ModelNode({ "nodes", "connections", "parentDiagram" })
@SuppressWarnings("all")
public class XDiagram extends Group implements XActivatable, XModelProvider {
  private Group nodeLayer = new Group();
  
  private Group buttonLayer = new Group();
  
  private Procedure1<? super XDiagram> contentsInitializer;
  
  private AuxiliaryLinesSupport auxiliaryLinesSupport;
  
  private ObservableMap<Class<? extends Behavior>,Behavior> behaviors = FXCollections.<Class<? extends Behavior>, Behavior>observableHashMap();
  
  private AccumulativeTransform2D canvasTransform;
  
  private boolean needsCentering = true;
  
  public XDiagram() {
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.nodeLayer);
    ObservableList<Node> _children_1 = this.getChildren();
    _children_1.add(this.buttonLayer);
    this.isRootDiagramProperty.set(true);
    ReadOnlyObjectProperty<Parent> _parentProperty = this.parentProperty();
    final ChangeListener<Parent> _function = new ChangeListener<Parent>() {
      public void changed(final ObservableValue<? extends Parent> property, final Parent oldValue, final Parent newValue) {
        XDiagram _diagram = CoreExtensions.getDiagram(newValue);
        XDiagram.this.parentDiagramProperty.set(_diagram);
        XDiagram _parentDiagram = XDiagram.this.getParentDiagram();
        boolean _equals = Objects.equal(_parentDiagram, null);
        XDiagram.this.isRootDiagramProperty.set(_equals);
      }
    };
    _parentProperty.addListener(_function);
    final ChangeListener<Boolean> _function_1 = new ChangeListener<Boolean>() {
      public void changed(final ObservableValue<? extends Boolean> property, final Boolean oldValue, final Boolean newValue) {
        if ((newValue).booleanValue()) {
          ObservableList<Node> _children = XDiagram.this.nodeLayer.getChildren();
          ObservableList<XConnection> _connections = XDiagram.this.getConnections();
          Iterables.<Node>addAll(_children, _connections);
        } else {
          ObservableList<Node> _children_1 = XDiagram.this.nodeLayer.getChildren();
          ObservableList<XConnection> _connections_1 = XDiagram.this.getConnections();
          Iterables.removeAll(_children_1, _connections_1);
        }
      }
    };
    this.isRootDiagramProperty.addListener(_function_1);
    AccumulativeTransform2D _accumulativeTransform2D = new AccumulativeTransform2D();
    this.canvasTransform = _accumulativeTransform2D;
    ObservableList<Transform> _transforms = this.getTransforms();
    Transform _transform = this.canvasTransform.getTransform();
    _transforms.setAll(_transform);
    ObservableList<Transform> _transforms_1 = this.getTransforms();
    final ListChangeListener<Transform> _function_2 = new ListChangeListener<Transform>() {
      public void onChanged(final ListChangeListener.Change<? extends Transform> change) {
        throw new IllegalStateException("Illegal attempt to change the transforms of an XDiagram");
      }
    };
    _transforms_1.addListener(_function_2);
  }
  
  public AccumulativeTransform2D getCanvasTransform() {
    return this.canvasTransform;
  }
  
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.doActivate();
      this.isActiveProperty.set(true);
    }
  }
  
  public void doActivate() {
    ObservableList<XNode> _nodes = this.getNodes();
    XDiagramChildrenListener<XNode> _xDiagramChildrenListener = new XDiagramChildrenListener<XNode>(this, this.nodeLayer);
    CoreExtensions.<XNode>addInitializingListener(_nodes, _xDiagramChildrenListener);
    ObservableList<XConnection> _connections = this.getConnections();
    Group _connectionLayer = this.getConnectionLayer();
    XDiagramChildrenListener<XConnection> _xDiagramChildrenListener_1 = new XDiagramChildrenListener<XConnection>(this, _connectionLayer);
    CoreExtensions.<XConnection>addInitializingListener(_connections, _xDiagramChildrenListener_1);
    ObservableList<XRapidButton> _buttons = this.getButtons();
    XDiagramChildrenListener<XRapidButton> _xDiagramChildrenListener_2 = new XDiagramChildrenListener<XRapidButton>(this, this.buttonLayer);
    CoreExtensions.<XRapidButton>addInitializingListener(_buttons, _xDiagramChildrenListener_2);
    InitializingListener<ArrowHead> _initializingListener = new InitializingListener<ArrowHead>();
    final Procedure1<InitializingListener<ArrowHead>> _function = new Procedure1<InitializingListener<ArrowHead>>() {
      public void apply(final InitializingListener<ArrowHead> it) {
        final Procedure1<ArrowHead> _function = new Procedure1<ArrowHead>() {
          public void apply(final ArrowHead it) {
            Group _connectionLayer = XDiagram.this.getConnectionLayer();
            ObservableList<Node> _children = _connectionLayer.getChildren();
            boolean _contains = _children.contains(it);
            boolean _not = (!_contains);
            if (_not) {
              Group _connectionLayer_1 = XDiagram.this.getConnectionLayer();
              ObservableList<Node> _children_1 = _connectionLayer_1.getChildren();
              _children_1.add(it);
            }
          }
        };
        it.setSet(_function);
        final Procedure1<Node> _function_1 = new Procedure1<Node>() {
          public void apply(final Node it) {
            Group _connectionLayer = XDiagram.this.getConnectionLayer();
            ObservableList<Node> _children = _connectionLayer.getChildren();
            _children.remove(it);
          }
        };
        it.setUnset(_function_1);
      }
    };
    final InitializingListener<ArrowHead> arrowHeadListener = ObjectExtensions.<InitializingListener<ArrowHead>>operator_doubleArrow(_initializingListener, _function);
    InitializingListListener<XConnectionLabel> _initializingListListener = new InitializingListListener<XConnectionLabel>();
    final Procedure1<InitializingListListener<XConnectionLabel>> _function_1 = new Procedure1<InitializingListListener<XConnectionLabel>>() {
      public void apply(final InitializingListListener<XConnectionLabel> it) {
        final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
          public void apply(final XConnectionLabel it) {
            Group _connectionLayer = XDiagram.this.getConnectionLayer();
            ObservableList<Node> _children = _connectionLayer.getChildren();
            boolean _contains = _children.contains(it);
            boolean _not = (!_contains);
            if (_not) {
              Group _connectionLayer_1 = XDiagram.this.getConnectionLayer();
              ObservableList<Node> _children_1 = _connectionLayer_1.getChildren();
              _children_1.add(it);
            }
          }
        };
        it.setAdd(_function);
        final Procedure1<Node> _function_1 = new Procedure1<Node>() {
          public void apply(final Node it) {
            Group _connectionLayer = XDiagram.this.getConnectionLayer();
            ObservableList<Node> _children = _connectionLayer.getChildren();
            _children.remove(it);
          }
        };
        it.setRemove(_function_1);
      }
    };
    final InitializingListListener<XConnectionLabel> labelListener = ObjectExtensions.<InitializingListListener<XConnectionLabel>>operator_doubleArrow(_initializingListListener, _function_1);
    Group _connectionLayer_1 = this.getConnectionLayer();
    ObservableList<Node> _children = _connectionLayer_1.getChildren();
    InitializingListListener<Node> _initializingListListener_1 = new InitializingListListener<Node>();
    final Procedure1<InitializingListListener<Node>> _function_2 = new Procedure1<InitializingListListener<Node>>() {
      public void apply(final InitializingListListener<Node> it) {
        final Procedure1<Object> _function = new Procedure1<Object>() {
          public void apply(final Object it) {
            if ((it instanceof XConnection)) {
              ListProperty<XConnectionLabel> _labelsProperty = ((XConnection)it).labelsProperty();
              CoreExtensions.<XConnectionLabel>addInitializingListener(_labelsProperty, labelListener);
              ObjectProperty<ArrowHead> _sourceArrowHeadProperty = ((XConnection)it).sourceArrowHeadProperty();
              CoreExtensions.<ArrowHead>addInitializingListener(_sourceArrowHeadProperty, arrowHeadListener);
              ObjectProperty<ArrowHead> _targetArrowHeadProperty = ((XConnection)it).targetArrowHeadProperty();
              CoreExtensions.<ArrowHead>addInitializingListener(_targetArrowHeadProperty, arrowHeadListener);
            }
          }
        };
        it.setAdd(_function);
        final Procedure1<Object> _function_1 = new Procedure1<Object>() {
          public void apply(final Object it) {
            if ((it instanceof XConnection)) {
              ListProperty<XConnectionLabel> _labelsProperty = ((XConnection)it).labelsProperty();
              CoreExtensions.<XConnectionLabel>removeInitializingListener(_labelsProperty, labelListener);
              ObjectProperty<ArrowHead> _sourceArrowHeadProperty = ((XConnection)it).sourceArrowHeadProperty();
              CoreExtensions.<ArrowHead>removeInitializingListener(_sourceArrowHeadProperty, arrowHeadListener);
              ObjectProperty<ArrowHead> _targetArrowHeadProperty = ((XConnection)it).targetArrowHeadProperty();
              CoreExtensions.<ArrowHead>removeInitializingListener(_targetArrowHeadProperty, arrowHeadListener);
            }
          }
        };
        it.setRemove(_function_1);
      }
    };
    InitializingListListener<Node> _doubleArrow = ObjectExtensions.<InitializingListListener<Node>>operator_doubleArrow(_initializingListListener_1, _function_2);
    CoreExtensions.<Node>addInitializingListener(_children, _doubleArrow);
    AuxiliaryLinesSupport _auxiliaryLinesSupport = new AuxiliaryLinesSupport(this);
    this.auxiliaryLinesSupport = _auxiliaryLinesSupport;
    if (this.contentsInitializer!=null) {
      this.contentsInitializer.apply(this);
    }
    NavigationBehavior _behavior = this.<NavigationBehavior>getBehavior(NavigationBehavior.class);
    boolean _equals = Objects.equal(_behavior, null);
    if (_equals) {
      DiagramNavigationBehavior _diagramNavigationBehavior = new DiagramNavigationBehavior(this);
      this.addBehavior(_diagramNavigationBehavior);
    }
    InitializingMapListener<Class<? extends Behavior>,Behavior> _initializingMapListener = new InitializingMapListener<Class<? extends Behavior>, Behavior>();
    final Procedure1<InitializingMapListener<Class<? extends Behavior>,Behavior>> _function_3 = new Procedure1<InitializingMapListener<Class<? extends Behavior>,Behavior>>() {
      public void apply(final InitializingMapListener<Class<? extends Behavior>,Behavior> it) {
        final Procedure2<Class<? extends Behavior>,Behavior> _function = new Procedure2<Class<? extends Behavior>,Behavior>() {
          public void apply(final Class<? extends Behavior> key, final Behavior value) {
            value.activate();
          }
        };
        it.setPut(_function);
        final Procedure2<Class<? extends Behavior>,Behavior> _function_1 = new Procedure2<Class<? extends Behavior>,Behavior>() {
          public void apply(final Class<? extends Behavior> key, final Behavior value) {
          }
        };
        it.setRemove(_function_1);
      }
    };
    InitializingMapListener<Class<? extends Behavior>,Behavior> _doubleArrow_1 = ObjectExtensions.<InitializingMapListener<Class<? extends Behavior>,Behavior>>operator_doubleArrow(_initializingMapListener, _function_3);
    CoreExtensions.<Class<? extends Behavior>, Behavior>addInitializingListener(this.behaviors, _doubleArrow_1);
  }
  
  public void centerDiagram(final boolean useForce) {
    boolean _or = false;
    if (this.needsCentering) {
      _or = true;
    } else {
      _or = useForce;
    }
    if (_or) {
      this.layout();
      Bounds _layoutBounds = this.getLayoutBounds();
      double _width = _layoutBounds.getWidth();
      Bounds _layoutBounds_1 = this.getLayoutBounds();
      double _height = _layoutBounds_1.getHeight();
      double _multiply = (_width * _height);
      boolean _greaterThan = (_multiply > 1);
      if (_greaterThan) {
        Scene _scene = this.getScene();
        double _width_1 = _scene.getWidth();
        Bounds _layoutBounds_2 = this.getLayoutBounds();
        double _width_2 = _layoutBounds_2.getWidth();
        double _divide = (_width_1 / _width_2);
        Scene _scene_1 = this.getScene();
        double _height_1 = _scene_1.getHeight();
        Bounds _layoutBounds_3 = this.getLayoutBounds();
        double _height_2 = _layoutBounds_3.getHeight();
        double _divide_1 = (_height_1 / _height_2);
        double _min = Math.min(_divide, _divide_1);
        final double scale = Math.min(1, _min);
        this.canvasTransform.setScale(scale);
        Bounds _boundsInLocal = this.getBoundsInLocal();
        Bounds _localToScene = this.localToScene(_boundsInLocal);
        final Point2D centerInScene = BoundsExtensions.center(_localToScene);
        Scene _scene_2 = this.getScene();
        double _width_3 = _scene_2.getWidth();
        double _multiply_1 = (0.5 * _width_3);
        double _x = centerInScene.getX();
        double _minus = (_multiply_1 - _x);
        Scene _scene_3 = this.getScene();
        double _height_3 = _scene_3.getHeight();
        double _multiply_2 = (0.5 * _height_3);
        double _y = centerInScene.getY();
        double _minus_1 = (_multiply_2 - _y);
        this.canvasTransform.setTranslate(_minus, _minus_1);
      }
    }
    this.needsCentering = false;
  }
  
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
  
  protected void addArrowHead(final Property<? extends Node> property, final ChangeListener<? super Node> listener) {
    boolean _and = false;
    Node _value = property.getValue();
    boolean _notEquals = (!Objects.equal(_value, null));
    if (!_notEquals) {
      _and = false;
    } else {
      Group _connectionLayer = this.getConnectionLayer();
      ObservableList<Node> _children = _connectionLayer.getChildren();
      Node _value_1 = property.getValue();
      boolean _contains = _children.contains(_value_1);
      boolean _not = (!_contains);
      _and = _not;
    }
    if (_and) {
      Group _connectionLayer_1 = this.getConnectionLayer();
      ObservableList<Node> _children_1 = _connectionLayer_1.getChildren();
      Node _value_2 = property.getValue();
      _children_1.add(_value_2);
    }
    property.addListener(listener);
  }
  
  public AuxiliaryLinesSupport getAuxiliaryLinesSupport() {
    return this.auxiliaryLinesSupport;
  }
  
  public Iterable<XShape> getAllShapes() {
    Iterable<? extends Node> _allChildren = CoreExtensions.getAllChildren(this);
    return Iterables.<XShape>filter(_allChildren, XShape.class);
  }
  
  public Procedure1<? super XDiagram> setContentsInitializer(final Procedure1<? super XDiagram> contentsInitializer) {
    return this.contentsInitializer = contentsInitializer;
  }
  
  public Group getNodeLayer() {
    return this.nodeLayer;
  }
  
  public Group getConnectionLayer() {
    Group _xifexpression = null;
    boolean _isRootDiagram = this.getIsRootDiagram();
    if (_isRootDiagram) {
      _xifexpression = this.nodeLayer;
    } else {
      XDiagram _parentDiagram = this.getParentDiagram();
      _xifexpression = _parentDiagram.nodeLayer;
    }
    return _xifexpression;
  }
  
  public Group getButtonLayer() {
    return this.buttonLayer;
  }
  
  /**
   * Automatically generated by @ModelNode. Used in model deserialization.
   */
  public XDiagram(final ModelLoad modelLoad) {
  }
  
  public void populate(final ModelElement modelElement) {
    modelElement.addProperty(nodesProperty, XNode.class);
    modelElement.addProperty(connectionsProperty, XConnection.class);
    modelElement.addProperty(parentDiagramProperty, XDiagram.class);
  }
  
  private SimpleListProperty<XNode> nodesProperty = new SimpleListProperty<XNode>(this, "nodes",_initNodes());
  
  private static final ObservableList<XNode> _initNodes() {
    ObservableList<XNode> _observableArrayList = FXCollections.<XNode>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<XNode> getNodes() {
    return this.nodesProperty.get();
  }
  
  public ListProperty<XNode> nodesProperty() {
    return this.nodesProperty;
  }
  
  private SimpleListProperty<XConnection> connectionsProperty = new SimpleListProperty<XConnection>(this, "connections",_initConnections());
  
  private static final ObservableList<XConnection> _initConnections() {
    ObservableList<XConnection> _observableArrayList = FXCollections.<XConnection>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<XConnection> getConnections() {
    return this.connectionsProperty.get();
  }
  
  public ListProperty<XConnection> connectionsProperty() {
    return this.connectionsProperty;
  }
  
  private SimpleListProperty<XRapidButton> buttonsProperty = new SimpleListProperty<XRapidButton>(this, "buttons",_initButtons());
  
  private static final ObservableList<XRapidButton> _initButtons() {
    ObservableList<XRapidButton> _observableArrayList = FXCollections.<XRapidButton>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<XRapidButton> getButtons() {
    return this.buttonsProperty.get();
  }
  
  public ListProperty<XRapidButton> buttonsProperty() {
    return this.buttonsProperty;
  }
  
  private SimpleObjectProperty<ObservableMap<Node,Pos>> fixedButtonsProperty = new SimpleObjectProperty<ObservableMap<Node, Pos>>(this, "fixedButtons",_initFixedButtons());
  
  private static final ObservableMap<Node,Pos> _initFixedButtons() {
    HashMap<Node,Pos> _newHashMap = CollectionLiterals.<Node, Pos>newHashMap();
    ObservableMap<Node,Pos> _observableMap = FXCollections.<Node, Pos>observableMap(_newHashMap);
    return _observableMap;
  }
  
  public ObservableMap<Node,Pos> getFixedButtons() {
    return this.fixedButtonsProperty.get();
  }
  
  public void setFixedButtons(final ObservableMap<Node,Pos> fixedButtons) {
    this.fixedButtonsProperty.set(fixedButtons);
  }
  
  public ObjectProperty<ObservableMap<Node,Pos>> fixedButtonsProperty() {
    return this.fixedButtonsProperty;
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
  
  private ReadOnlyBooleanWrapper isRootDiagramProperty = new ReadOnlyBooleanWrapper(this, "isRootDiagram");
  
  public boolean getIsRootDiagram() {
    return this.isRootDiagramProperty.get();
  }
  
  public ReadOnlyBooleanProperty isRootDiagramProperty() {
    return this.isRootDiagramProperty.getReadOnlyProperty();
  }
  
  private SimpleObjectProperty<Paint> backgroundPaintProperty = new SimpleObjectProperty<Paint>(this, "backgroundPaint",_initBackgroundPaint());
  
  private static final Paint _initBackgroundPaint() {
    return Color.WHITE;
  }
  
  public Paint getBackgroundPaint() {
    return this.backgroundPaintProperty.get();
  }
  
  public void setBackgroundPaint(final Paint backgroundPaint) {
    this.backgroundPaintProperty.set(backgroundPaint);
  }
  
  public ObjectProperty<Paint> backgroundPaintProperty() {
    return this.backgroundPaintProperty;
  }
  
  private SimpleObjectProperty<Paint> foregroundPaintProperty = new SimpleObjectProperty<Paint>(this, "foregroundPaint",_initForegroundPaint());
  
  private static final Paint _initForegroundPaint() {
    return Color.BLACK;
  }
  
  public Paint getForegroundPaint() {
    return this.foregroundPaintProperty.get();
  }
  
  public void setForegroundPaint(final Paint foregroundPaint) {
    this.foregroundPaintProperty.set(foregroundPaint);
  }
  
  public ObjectProperty<Paint> foregroundPaintProperty() {
    return this.foregroundPaintProperty;
  }
  
  private SimpleObjectProperty<Paint> connectionPaintProperty = new SimpleObjectProperty<Paint>(this, "connectionPaint",_initConnectionPaint());
  
  private static final Paint _initConnectionPaint() {
    Color _gray = Color.gray(0.2);
    return _gray;
  }
  
  public Paint getConnectionPaint() {
    return this.connectionPaintProperty.get();
  }
  
  public void setConnectionPaint(final Paint connectionPaint) {
    this.connectionPaintProperty.set(connectionPaint);
  }
  
  public ObjectProperty<Paint> connectionPaintProperty() {
    return this.connectionPaintProperty;
  }
  
  private ReadOnlyObjectWrapper<XDiagram> parentDiagramProperty = new ReadOnlyObjectWrapper<XDiagram>(this, "parentDiagram");
  
  public XDiagram getParentDiagram() {
    return this.parentDiagramProperty.get();
  }
  
  public ReadOnlyObjectProperty<XDiagram> parentDiagramProperty() {
    return this.parentDiagramProperty.getReadOnlyProperty();
  }
}
