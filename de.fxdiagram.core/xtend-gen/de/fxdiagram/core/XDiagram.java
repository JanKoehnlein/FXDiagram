package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDomainObjectOwner;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.auxlines.AuxiliaryLinesSupport;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.DiagramNavigationBehavior;
import de.fxdiagram.core.behavior.NavigationBehavior;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.InitializingListListener;
import de.fxdiagram.core.extensions.InitializingMapListener;
import de.fxdiagram.core.extensions.Point2DExtensions;
import de.fxdiagram.core.layout.LayoutParameters;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.model.XModelProvider;
import de.fxdiagram.core.viewport.ViewportTransform;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

/**
 * A diagram with {@link XNode}s and {@link XConnection}s.
 * 
 * Being added to the {@link #nodes} or {@link #connections}, {@link XShapes} will get
 * automatically be activated.
 * 
 * To find the {@link XDiagram} of an {@link XShape}, use the {@link CoreExtensions}.
 * 
 * Diagrams can be nested ({@link parentDiagram}).
 * An optional {@link contentsInitializer} allows lazy population of the diagram.
 * Just like an {@link XShape}, an {@link XDiagram} can be customized with composable
 * {@link Behavior}s.
 * 
 * A {@link viewportTransform} stores the current viewport of the diagram.
 */
@Logging
@ModelNode({ "nodes", "connections", "parentDiagram", "domainObjectDescriptor", "layoutParameters" })
@SuppressWarnings("all")
public class XDiagram extends Group implements XActivatable, XDomainObjectOwner, XModelProvider {
  private Group nodeLayer = new Group();
  
  private Group buttonLayer = new Group();
  
  private Procedure1<? super XDiagram> contentsInitializer;
  
  private AuxiliaryLinesSupport auxiliaryLinesSupport;
  
  private ObservableMap<Class<? extends Behavior>, Behavior> behaviors = FXCollections.<Class<? extends Behavior>, Behavior>observableHashMap();
  
  private ViewportTransform viewportTransform;
  
  private boolean needsCentering = true;
  
  public XDiagram(final DomainObjectDescriptor descriptor) {
    this.domainObjectDescriptorProperty.set(descriptor);
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.nodeLayer);
    ObservableList<Node> _children_1 = this.getChildren();
    _children_1.add(this.buttonLayer);
    ViewportTransform _viewportTransform = new ViewportTransform();
    this.viewportTransform = _viewportTransform;
    this.getTransforms().setAll(this.viewportTransform.getTransform());
    final ListChangeListener<Transform> _function = (ListChangeListener.Change<? extends Transform> change) -> {
      throw new IllegalStateException("Illegal attempt to change the transforms of an XDiagram");
    };
    this.getTransforms().addListener(_function);
  }
  
  public XDiagram() {
    this(null);
  }
  
  public ViewportTransform getViewportTransform() {
    return this.viewportTransform;
  }
  
  @Override
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      try {
        this.isActiveProperty.set(true);
        this.doActivate();
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception exc = (Exception)_t;
          XDiagram.LOG.severe(exc.getMessage());
          exc.printStackTrace();
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
  }
  
  public void doActivate() {
    final ChangeListener<Boolean> _function = (ObservableValue<? extends Boolean> p, Boolean o, Boolean n) -> {
      if ((n).booleanValue()) {
        final Consumer<XConnection> _function_1 = (XConnection it) -> {
          CoreExtensions.<XConnection>safeAdd(this.nodeLayer.getChildren(), it);
          CoreExtensions.<ArrowHead>safeAdd(this.nodeLayer.getChildren(), it.getSourceArrowHead());
          CoreExtensions.<ArrowHead>safeAdd(this.nodeLayer.getChildren(), it.getTargetArrowHead());
          CoreExtensions.<XConnectionLabel>safeAdd(this.nodeLayer.getChildren(), it.getLabels());
        };
        this.getConnections().forEach(_function_1);
      } else {
        final Consumer<XConnection> _function_2 = (XConnection it) -> {
          ObservableList<Node> _children = this.nodeLayer.getChildren();
          _children.remove(it);
          ObservableList<Node> _children_1 = this.nodeLayer.getChildren();
          ArrowHead _sourceArrowHead = it.getSourceArrowHead();
          _children_1.remove(_sourceArrowHead);
          ObservableList<Node> _children_2 = this.nodeLayer.getChildren();
          ArrowHead _targetArrowHead = it.getTargetArrowHead();
          _children_2.remove(_targetArrowHead);
          ObservableList<Node> _children_3 = this.nodeLayer.getChildren();
          ObservableList<XConnectionLabel> _labels = it.getLabels();
          Iterables.removeAll(_children_3, _labels);
        };
        this.getConnections().forEach(_function_2);
      }
    };
    this.isRootDiagramProperty().addListener(_function);
    if (this.contentsInitializer!=null) {
      this.contentsInitializer.apply(this);
    }
    ObservableList<XNode> _nodes = this.getNodes();
    InitializingListListener<XNode> _initializingListListener = new InitializingListListener<XNode>();
    final Procedure1<InitializingListListener<XNode>> _function_1 = (InitializingListListener<XNode> it) -> {
      final Procedure1<XNode> _function_2 = (XNode it_1) -> {
        it_1.initializeGraphics();
        ObservableList<Node> _children = this.getNodeLayer().getChildren();
        _children.add(it_1);
        boolean _isActive = this.getIsActive();
        if (_isActive) {
          it_1.activate();
        }
      };
      it.setAdd(_function_2);
      final Procedure1<XNode> _function_3 = (XNode it_1) -> {
        CoreExtensions.<XNode>safeDelete(this.getNodeLayer().getChildren(), it_1);
      };
      it.setRemove(_function_3);
    };
    InitializingListListener<XNode> _doubleArrow = ObjectExtensions.<InitializingListListener<XNode>>operator_doubleArrow(_initializingListListener, _function_1);
    CoreExtensions.<XNode>addInitializingListener(_nodes, _doubleArrow);
    ObservableList<XConnection> _connections = this.getConnections();
    InitializingListListener<XConnection> _initializingListListener_1 = new InitializingListListener<XConnection>();
    final Procedure1<InitializingListListener<XConnection>> _function_2 = (InitializingListListener<XConnection> it) -> {
      final Procedure1<XConnection> _function_3 = (XConnection it_1) -> {
        final ObservableList<Node> clChildren = this.getConnectionLayer().getChildren();
        CoreExtensions.<XConnection>safeAdd(clChildren, it_1);
        it_1.initializeGraphics();
        CoreExtensions.<ArrowHead>safeAdd(clChildren, it_1.getSourceArrowHead());
        CoreExtensions.<ArrowHead>safeAdd(clChildren, it_1.getTargetArrowHead());
        final Consumer<XConnectionLabel> _function_4 = (XConnectionLabel it_2) -> {
          CoreExtensions.<XConnectionLabel>safeAdd(clChildren, it_2);
        };
        it_1.getLabels().forEach(_function_4);
        boolean _isActive = this.getIsActive();
        if (_isActive) {
          it_1.activate();
        }
      };
      it.setAdd(_function_3);
      final Procedure1<XConnection> _function_4 = (XConnection it_1) -> {
        final ObservableList<Node> clChildren = this.getConnectionLayer().getChildren();
        CoreExtensions.<XConnection>safeDelete(clChildren, it_1);
        CoreExtensions.<ArrowHead>safeDelete(clChildren, it_1.getSourceArrowHead());
        CoreExtensions.<ArrowHead>safeDelete(clChildren, it_1.getTargetArrowHead());
        final Consumer<XConnectionLabel> _function_5 = (XConnectionLabel it_2) -> {
          CoreExtensions.<XConnectionLabel>safeDelete(clChildren, it_2);
        };
        it_1.getLabels().forEach(_function_5);
      };
      it.setRemove(_function_4);
    };
    InitializingListListener<XConnection> _doubleArrow_1 = ObjectExtensions.<InitializingListListener<XConnection>>operator_doubleArrow(_initializingListListener_1, _function_2);
    CoreExtensions.<XConnection>addInitializingListener(_connections, _doubleArrow_1);
    AuxiliaryLinesSupport _xifexpression = null;
    boolean _and = false;
    boolean _isRootDiagram = this.getIsRootDiagram();
    boolean _not = (!_isRootDiagram);
    if (!_not) {
      _and = false;
    } else {
      XDiagram _parentDiagram = this.getParentDiagram();
      AuxiliaryLinesSupport _auxiliaryLinesSupport = null;
      if (_parentDiagram!=null) {
        _auxiliaryLinesSupport=_parentDiagram.auxiliaryLinesSupport;
      }
      boolean _notEquals = (!Objects.equal(_auxiliaryLinesSupport, null));
      _and = _notEquals;
    }
    if (_and) {
      _xifexpression = this.getParentDiagram().auxiliaryLinesSupport;
    } else {
      _xifexpression = new AuxiliaryLinesSupport(this);
    }
    this.auxiliaryLinesSupport = _xifexpression;
    if ((this.getLayoutOnActivate() && (!Objects.equal(this.getLayoutParameters(), null)))) {
      final Consumer<XNode> _function_3 = (XNode it) -> {
        it.getNode().autosize();
      };
      this.getNodes().forEach(_function_3);
      final Consumer<XConnection> _function_4 = (XConnection it) -> {
        it.getNode();
        final Consumer<XConnectionLabel> _function_5 = (XConnectionLabel it_1) -> {
          it_1.getNode().autosize();
        };
        it.getLabels().forEach(_function_5);
      };
      this.getConnections().forEach(_function_4);
      new Layouter().layout(this.getLayoutParameters(), this, null);
      this.setLayoutOnActivate(false);
    }
    NavigationBehavior _behavior = this.<NavigationBehavior>getBehavior(NavigationBehavior.class);
    boolean _equals = Objects.equal(_behavior, null);
    if (_equals) {
      DiagramNavigationBehavior _diagramNavigationBehavior = new DiagramNavigationBehavior(this);
      this.addBehavior(_diagramNavigationBehavior);
    }
    InitializingMapListener<Class<? extends Behavior>, Behavior> _initializingMapListener = new InitializingMapListener<Class<? extends Behavior>, Behavior>();
    final Procedure1<InitializingMapListener<Class<? extends Behavior>, Behavior>> _function_5 = (InitializingMapListener<Class<? extends Behavior>, Behavior> it) -> {
      final Procedure2<Class<? extends Behavior>, Behavior> _function_6 = (Class<? extends Behavior> key, Behavior value) -> {
        value.activate();
      };
      it.setPut(_function_6);
    };
    InitializingMapListener<Class<? extends Behavior>, Behavior> _doubleArrow_2 = ObjectExtensions.<InitializingMapListener<Class<? extends Behavior>, Behavior>>operator_doubleArrow(_initializingMapListener, _function_5);
    CoreExtensions.<Class<? extends Behavior>, Behavior>addInitializingListener(this.behaviors, _doubleArrow_2);
  }
  
  public void centerDiagram(final boolean useForce) {
    if ((this.needsCentering || useForce)) {
      this.layout();
      double _width = this.getLayoutBounds().getWidth();
      double _height = this.getLayoutBounds().getHeight();
      double _multiply = (_width * _height);
      boolean _greaterThan = (_multiply > 1);
      if (_greaterThan) {
        double _width_1 = this.getScene().getWidth();
        double _width_2 = this.getLayoutBounds().getWidth();
        double _divide = (_width_1 / _width_2);
        double _height_1 = this.getScene().getHeight();
        double _height_2 = this.getLayoutBounds().getHeight();
        double _divide_1 = (_height_1 / _height_2);
        final double scale = Math.min(1, Math.min(_divide, _divide_1));
        this.viewportTransform.setScale(scale);
        final Point2D centerInScene = BoundsExtensions.center(this.localToScene(this.getBoundsInLocal()));
        double _width_3 = this.getScene().getWidth();
        double _multiply_1 = (0.5 * _width_3);
        double _x = centerInScene.getX();
        double _minus = (_multiply_1 - _x);
        double _height_3 = this.getScene().getHeight();
        double _multiply_2 = (0.5 * _height_3);
        double _y = centerInScene.getY();
        double _minus_1 = (_multiply_2 - _y);
        this.viewportTransform.setTranslate(_minus, _minus_1);
      }
    }
    this.needsCentering = false;
  }
  
  public <T extends Behavior> T getBehavior(final Class<T> key) {
    Behavior _get = this.behaviors.get(key);
    return ((T) _get);
  }
  
  public Behavior addBehavior(final Behavior behavior) {
    return this.behaviors.put(behavior.getBehaviorKey(), behavior);
  }
  
  public Behavior removeBehavior(final String key) {
    return this.behaviors.remove(key);
  }
  
  protected void addArrowHead(final Property<? extends Node> property, final ChangeListener<? super Node> listener) {
    if (((!Objects.equal(property.getValue(), null)) && (!this.getConnectionLayer().getChildren().contains(property.getValue())))) {
      ObservableList<Node> _children = this.getConnectionLayer().getChildren();
      Node _value = property.getValue();
      _children.add(_value);
    }
    property.addListener(listener);
  }
  
  public AuxiliaryLinesSupport getAuxiliaryLinesSupport() {
    return this.auxiliaryLinesSupport;
  }
  
  public Iterable<XShape> getAllShapes() {
    return Iterables.<XShape>filter(CoreExtensions.getAllChildren(this), XShape.class);
  }
  
  public Procedure1<? super XDiagram> setContentsInitializer(final Procedure1<? super XDiagram> contentsInitializer) {
    return this.contentsInitializer = contentsInitializer;
  }
  
  public Group getNodeLayer() {
    return this.nodeLayer;
  }
  
  public Group getConnectionLayer() {
    boolean _and = false;
    boolean _isRootDiagram = this.getIsRootDiagram();
    boolean _not = (!_isRootDiagram);
    if (!_not) {
      _and = false;
    } else {
      XDiagram _parentDiagram = this.getParentDiagram();
      Group _connectionLayer = null;
      if (_parentDiagram!=null) {
        _connectionLayer=_parentDiagram.getConnectionLayer();
      }
      boolean _notEquals = (!Objects.equal(_connectionLayer, null));
      _and = _notEquals;
    }
    if (_and) {
      return this.getParentDiagram().getConnectionLayer();
    }
    return this.nodeLayer;
  }
  
  public Group getButtonLayer() {
    Group _xblockexpression = null;
    {
      boolean _and = false;
      boolean _isRootDiagram = this.getIsRootDiagram();
      boolean _not = (!_isRootDiagram);
      if (!_not) {
        _and = false;
      } else {
        XDiagram _parentDiagram = this.getParentDiagram();
        Group _buttonLayer = null;
        if (_parentDiagram!=null) {
          _buttonLayer=_parentDiagram.buttonLayer;
        }
        boolean _notEquals = (!Objects.equal(_buttonLayer, null));
        _and = _notEquals;
      }
      if (_and) {
        return this.getParentDiagram().buttonLayer;
      }
      _xblockexpression = this.buttonLayer;
    }
    return _xblockexpression;
  }
  
  public Point2D getSnappedPosition(final Point2D newPositionInDiagram) {
    Point2D _xifexpression = null;
    boolean _gridEnabled = this.getGridEnabled();
    if (_gridEnabled) {
      _xifexpression = Point2DExtensions.snapToGrid(newPositionInDiagram, this.getGridSize());
    } else {
      _xifexpression = newPositionInDiagram;
    }
    return _xifexpression;
  }
  
  public Point2D getSnappedPosition(final Point2D newPositionInDiagram, final XShape shape, final boolean useGrid, final boolean useAuxLines) {
    if (useGrid) {
      Point2D _localToParent = shape.localToParent(BoundsExtensions.center(shape.getLayoutBounds()));
      double _layoutX = shape.getLayoutX();
      double _layoutY = shape.getLayoutY();
      Point2D _point2D = new Point2D(_layoutX, _layoutY);
      final Point2D hostCenterDelta = Point2DExtensions.operator_minus(_localToParent, _point2D);
      final Point2D newCenterPosition = Point2DExtensions.operator_plus(newPositionInDiagram, hostCenterDelta);
      final Point2D snappedCenter = Point2DExtensions.snapToGrid(newCenterPosition, this.getGridSize());
      final Point2D correction = Point2DExtensions.operator_minus(snappedCenter, newCenterPosition);
      return Point2DExtensions.operator_plus(newPositionInDiagram, correction);
    } else {
      if (useAuxLines) {
        return this.auxiliaryLinesSupport.getSnappedPosition(shape, newPositionInDiagram);
      }
    }
    return newPositionInDiagram;
  }
  
  public Point2D getSnappedPosition(final Point2D newPositionInDiagram, final XShape shape) {
    return this.getSnappedPosition(newPositionInDiagram, shape, this.getGridEnabled(), true);
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.XDiagram");
    ;
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(nodesProperty, XNode.class);
    modelElement.addProperty(connectionsProperty, XConnection.class);
    modelElement.addProperty(parentDiagramProperty, XDiagram.class);
    modelElement.addProperty(domainObjectDescriptorProperty, DomainObjectDescriptor.class);
    modelElement.addProperty(layoutParametersProperty, LayoutParameters.class);
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
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
  
  private SimpleObjectProperty<ObservableMap<Node, Pos>> fixedButtonsProperty = new SimpleObjectProperty<ObservableMap<Node, Pos>>(this, "fixedButtons",_initFixedButtons());
  
  private static final ObservableMap<Node, Pos> _initFixedButtons() {
    ObservableMap<Node, Pos> _observableMap = FXCollections.<Node, Pos>observableMap(CollectionLiterals.<Node, Pos>newHashMap());
    return _observableMap;
  }
  
  public ObservableMap<Node, Pos> getFixedButtons() {
    return this.fixedButtonsProperty.get();
  }
  
  public void setFixedButtons(final ObservableMap<Node, Pos> fixedButtons) {
    this.fixedButtonsProperty.set(fixedButtons);
  }
  
  public ObjectProperty<ObservableMap<Node, Pos>> fixedButtonsProperty() {
    return this.fixedButtonsProperty;
  }
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
  
  private SimpleBooleanProperty layoutOnActivateProperty = new SimpleBooleanProperty(this, "layoutOnActivate");
  
  public boolean getLayoutOnActivate() {
    return this.layoutOnActivateProperty.get();
  }
  
  public void setLayoutOnActivate(final boolean layoutOnActivate) {
    this.layoutOnActivateProperty.set(layoutOnActivate);
  }
  
  public BooleanProperty layoutOnActivateProperty() {
    return this.layoutOnActivateProperty;
  }
  
  private SimpleObjectProperty<LayoutParameters> layoutParametersProperty = new SimpleObjectProperty<LayoutParameters>(this, "layoutParameters",_initLayoutParameters());
  
  private static final LayoutParameters _initLayoutParameters() {
    LayoutParameters _layoutParameters = new LayoutParameters();
    return _layoutParameters;
  }
  
  public LayoutParameters getLayoutParameters() {
    return this.layoutParametersProperty.get();
  }
  
  public void setLayoutParameters(final LayoutParameters layoutParameters) {
    this.layoutParametersProperty.set(layoutParameters);
  }
  
  public ObjectProperty<LayoutParameters> layoutParametersProperty() {
    return this.layoutParametersProperty;
  }
  
  private SimpleBooleanProperty isRootDiagramProperty = new SimpleBooleanProperty(this, "isRootDiagram",_initIsRootDiagram());
  
  private static final boolean _initIsRootDiagram() {
    return true;
  }
  
  public boolean getIsRootDiagram() {
    return this.isRootDiagramProperty.get();
  }
  
  public void setIsRootDiagram(final boolean isRootDiagram) {
    this.isRootDiagramProperty.set(isRootDiagram);
  }
  
  public BooleanProperty isRootDiagramProperty() {
    return this.isRootDiagramProperty;
  }
  
  private SimpleObjectProperty<XDiagram> parentDiagramProperty = new SimpleObjectProperty<XDiagram>(this, "parentDiagram");
  
  public XDiagram getParentDiagram() {
    return this.parentDiagramProperty.get();
  }
  
  public void setParentDiagram(final XDiagram parentDiagram) {
    this.parentDiagramProperty.set(parentDiagram);
  }
  
  public ObjectProperty<XDiagram> parentDiagramProperty() {
    return this.parentDiagramProperty;
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
  
  private ReadOnlyObjectWrapper<DomainObjectDescriptor> domainObjectDescriptorProperty = new ReadOnlyObjectWrapper<DomainObjectDescriptor>(this, "domainObjectDescriptor");
  
  public DomainObjectDescriptor getDomainObjectDescriptor() {
    return this.domainObjectDescriptorProperty.get();
  }
  
  public ReadOnlyObjectProperty<DomainObjectDescriptor> domainObjectDescriptorProperty() {
    return this.domainObjectDescriptorProperty.getReadOnlyProperty();
  }
  
  private SimpleBooleanProperty gridEnabledProperty = new SimpleBooleanProperty(this, "gridEnabled",_initGridEnabled());
  
  private static final boolean _initGridEnabled() {
    return false;
  }
  
  public boolean getGridEnabled() {
    return this.gridEnabledProperty.get();
  }
  
  public void setGridEnabled(final boolean gridEnabled) {
    this.gridEnabledProperty.set(gridEnabled);
  }
  
  public BooleanProperty gridEnabledProperty() {
    return this.gridEnabledProperty;
  }
  
  private SimpleDoubleProperty gridSizeProperty = new SimpleDoubleProperty(this, "gridSize",_initGridSize());
  
  private static final double _initGridSize() {
    return 10;
  }
  
  public double getGridSize() {
    return this.gridSizeProperty.get();
  }
  
  public void setGridSize(final double gridSize) {
    this.gridSizeProperty.set(gridSize);
  }
  
  public DoubleProperty gridSizeProperty() {
    return this.gridSizeProperty;
  }
}
