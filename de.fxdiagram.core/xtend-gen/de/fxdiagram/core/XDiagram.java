package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
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
import de.fxdiagram.core.extensions.CoreExtensions;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.MapChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XDiagram extends Group implements XActivatable {
  private Group nodeLayer = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private Group buttonLayer = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private XDiagram parentDiagram;
  
  private Procedure1<? super XDiagram> contentsInitializer;
  
  private AuxiliaryLinesSupport auxiliaryLinesSupport;
  
  private ObservableMap<Class<? extends Behavior>,Behavior> behaviors = new Function0<ObservableMap<Class<? extends Behavior>,Behavior>>() {
    public ObservableMap<Class<? extends Behavior>,Behavior> apply() {
      ObservableMap<Class<? extends Behavior>,Behavior> _observableHashMap = FXCollections.<Class<? extends Behavior>, Behavior>observableHashMap();
      return _observableHashMap;
    }
  }.apply();
  
  public XDiagram() {
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.nodeLayer);
    ObservableList<Node> _children_1 = this.getChildren();
    _children_1.add(this.buttonLayer);
    ReadOnlyObjectProperty<Parent> _parentProperty = this.parentProperty();
    final ChangeListener<Parent> _function = new ChangeListener<Parent>() {
      public void changed(final ObservableValue<? extends Parent> property, final Parent oldValue, final Parent newValue) {
        XDiagram _diagram = CoreExtensions.getDiagram(newValue);
        XDiagram.this.parentDiagram = _diagram;
        boolean _equals = Objects.equal(XDiagram.this.parentDiagram, null);
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
  }
  
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.isActiveProperty.set(true);
      this.doActivate();
    }
    Collection<Behavior> _values = this.behaviors.values();
    final Procedure1<Behavior> _function = new Procedure1<Behavior>() {
      public void apply(final Behavior it) {
        it.activate();
      }
    };
    IterableExtensions.<Behavior>forEach(_values, _function);
    final MapChangeListener<Class<? extends Behavior>,Behavior> _function_1 = new MapChangeListener<Class<? extends Behavior>,Behavior>() {
      public void onChanged(final Change<? extends Class<? extends Behavior>,? extends Behavior> change) {
        boolean _isActive = XDiagram.this.getIsActive();
        if (_isActive) {
          boolean _wasAdded = change.wasAdded();
          if (_wasAdded) {
            Behavior _valueAdded = change.getValueAdded();
            _valueAdded.activate();
          }
        }
      }
    };
    final MapChangeListener<Class<? extends Behavior>,Behavior> behaviorActivator = _function_1;
    this.behaviors.addListener(behaviorActivator);
  }
  
  public void doActivate() {
    final ChangeListener<Node> _function = new ChangeListener<Node>() {
      public void changed(final ObservableValue<? extends Node> prop, final Node oldVal, final Node newVal) {
        boolean _notEquals = (!Objects.equal(oldVal, null));
        if (_notEquals) {
          Group _connectionLayer = XDiagram.this.getConnectionLayer();
          ObservableList<Node> _children = _connectionLayer.getChildren();
          _children.remove(oldVal);
        }
        boolean _notEquals_1 = (!Objects.equal(newVal, null));
        if (_notEquals_1) {
          Group _connectionLayer_1 = XDiagram.this.getConnectionLayer();
          ObservableList<Node> _children_1 = _connectionLayer_1.getChildren();
          _children_1.add(newVal);
        }
      }
    };
    final ChangeListener<Node> arrowHeadListener = _function;
    final ListChangeListener<Node> _function_1 = new ListChangeListener<Node>() {
      public void onChanged(final javafx.collections.ListChangeListener.Change<? extends Node> change) {
        boolean _next = change.next();
        boolean _while = _next;
        while (_while) {
          {
            boolean _wasAdded = change.wasAdded();
            if (_wasAdded) {
              Group _connectionLayer = XDiagram.this.getConnectionLayer();
              ObservableList<Node> _children = _connectionLayer.getChildren();
              List<? extends Node> _addedSubList = change.getAddedSubList();
              Iterables.<Node>addAll(_children, _addedSubList);
            }
            boolean _wasRemoved = change.wasRemoved();
            if (_wasRemoved) {
              Group _connectionLayer_1 = XDiagram.this.getConnectionLayer();
              ObservableList<Node> _children_1 = _connectionLayer_1.getChildren();
              List<? extends Node> _removed = change.getRemoved();
              Iterables.removeAll(_children_1, _removed);
            }
          }
          boolean _next_1 = change.next();
          _while = _next_1;
        }
      }
    };
    final ListChangeListener<? super XConnectionLabel> labelListener = _function_1;
    ObservableList<XNode> _nodes = this.getNodes();
    XDiagramChildrenListener<XNode> _xDiagramChildrenListener = new XDiagramChildrenListener<XNode>(this, this.nodeLayer);
    _nodes.addListener(_xDiagramChildrenListener);
    ObservableList<XConnection> _connections = this.getConnections();
    Group _connectionLayer = this.getConnectionLayer();
    XDiagramChildrenListener<XConnection> _xDiagramChildrenListener_1 = new XDiagramChildrenListener<XConnection>(this, _connectionLayer);
    _connections.addListener(_xDiagramChildrenListener_1);
    ObservableList<XRapidButton> _buttons = this.getButtons();
    XDiagramChildrenListener<XRapidButton> _xDiagramChildrenListener_2 = new XDiagramChildrenListener<XRapidButton>(this, this.buttonLayer);
    _buttons.addListener(_xDiagramChildrenListener_2);
    Group _connectionLayer_1 = this.getConnectionLayer();
    ObservableList<Node> _children = _connectionLayer_1.getChildren();
    final ListChangeListener<Node> _function_2 = new ListChangeListener<Node>() {
      public void onChanged(final javafx.collections.ListChangeListener.Change<? extends Node> change) {
        boolean _next = change.next();
        boolean _while = _next;
        while (_while) {
          {
            boolean _wasAdded = change.wasAdded();
            if (_wasAdded) {
              List<? extends Node> _addedSubList = change.getAddedSubList();
              final Iterable<XConnection> addedConnections = Iterables.<XConnection>filter(_addedSubList, XConnection.class);
              final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
                public void apply(final XConnection it) {
                  ObservableList<XConnectionLabel> _labels = it.getLabels();
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
                  IterableExtensions.<XConnectionLabel>forEach(_labels, _function);
                  ListProperty<XConnectionLabel> _labelsProperty = it.labelsProperty();
                  _labelsProperty.addListener(labelListener);
                  ObjectProperty<ArrowHead> _targetArrowHeadProperty = it.targetArrowHeadProperty();
                  XDiagram.this.addArrowHead(_targetArrowHeadProperty, arrowHeadListener);
                  ObjectProperty<ArrowHead> _sourceArrowHeadProperty = it.sourceArrowHeadProperty();
                  XDiagram.this.addArrowHead(_sourceArrowHeadProperty, arrowHeadListener);
                }
              };
              IterableExtensions.<XConnection>forEach(addedConnections, _function);
            }
            boolean _wasRemoved = change.wasRemoved();
            if (_wasRemoved) {
              List<? extends Node> _removed = change.getRemoved();
              final Iterable<XConnection> removedConnections = Iterables.<XConnection>filter(_removed, XConnection.class);
              final Procedure1<XConnection> _function_1 = new Procedure1<XConnection>() {
                public void apply(final XConnection it) {
                  Group _connectionLayer = XDiagram.this.getConnectionLayer();
                  ObservableList<Node> _children = _connectionLayer.getChildren();
                  ObservableList<XConnectionLabel> _labels = it.getLabels();
                  _children.removeAll(_labels);
                  ListProperty<XConnectionLabel> _labelsProperty = it.labelsProperty();
                  _labelsProperty.removeListener(labelListener);
                  ObjectProperty<ArrowHead> _targetArrowHeadProperty = it.targetArrowHeadProperty();
                  XDiagram.this.removeArrowHead(_targetArrowHeadProperty, arrowHeadListener);
                  ObjectProperty<ArrowHead> _sourceArrowHeadProperty = it.sourceArrowHeadProperty();
                  XDiagram.this.removeArrowHead(_sourceArrowHeadProperty, arrowHeadListener);
                }
              };
              IterableExtensions.<XConnection>forEach(removedConnections, _function_1);
            }
          }
          boolean _next_1 = change.next();
          _while = _next_1;
        }
      }
    };
    _children.addListener(_function_2);
    ObservableList<XNode> _nodes_1 = this.getNodes();
    ObservableList<XConnection> _connections_1 = this.getConnections();
    Iterable<XShape> _plus = Iterables.<XShape>concat(_nodes_1, _connections_1);
    ObservableList<XRapidButton> _buttons_1 = this.getButtons();
    Iterable<Parent> _plus_1 = Iterables.<Parent>concat(_plus, _buttons_1);
    final Procedure1<Parent> _function_3 = new Procedure1<Parent>() {
      public void apply(final Parent it) {
        ((XActivatable)it).activate();
      }
    };
    IterableExtensions.<Parent>forEach(_plus_1, _function_3);
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
  }
  
  public <T extends Behavior> T getBehavior(final Class<T> key) {
    Behavior _get = this.behaviors.get(key);
    return ((T) _get);
  }
  
  public Behavior addBehavior(final Behavior behavior) {
    Class<? extends Behavior> _behaviorKey = behavior.getBehaviorKey();
    Behavior _put = this.behaviors.put(_behaviorKey, behavior);
    return _put;
  }
  
  public Behavior removeBehavior(final String key) {
    Behavior _remove = this.behaviors.remove(key);
    return _remove;
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
      _and = (_notEquals && _not);
    }
    if (_and) {
      Group _connectionLayer_1 = this.getConnectionLayer();
      ObservableList<Node> _children_1 = _connectionLayer_1.getChildren();
      Node _value_2 = property.getValue();
      _children_1.add(_value_2);
    }
    property.addListener(listener);
  }
  
  protected void removeArrowHead(final Property<? extends Node> property, final ChangeListener<? super Node> listener) {
    Node _value = property.getValue();
    boolean _notEquals = (!Objects.equal(_value, null));
    if (_notEquals) {
      Group _connectionLayer = this.getConnectionLayer();
      ObservableList<Node> _children = _connectionLayer.getChildren();
      Node _value_1 = property.getValue();
      _children.remove(_value_1);
    }
    property.removeListener(listener);
  }
  
  public AuxiliaryLinesSupport getAuxiliaryLinesSupport() {
    return this.auxiliaryLinesSupport;
  }
  
  public Iterable<XShape> getAllShapes() {
    Iterable<? extends Node> _allChildren = CoreExtensions.getAllChildren(this);
    Iterable<XShape> _filter = Iterables.<XShape>filter(_allChildren, XShape.class);
    return _filter;
  }
  
  public Procedure1<? super XDiagram> setContentsInitializer(final Procedure1<? super XDiagram> contentsInitializer) {
    Procedure1<? super XDiagram> _contentsInitializer = this.contentsInitializer = contentsInitializer;
    return _contentsInitializer;
  }
  
  public XDiagram getParentDiagram() {
    return this.parentDiagram;
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
      _xifexpression = this.parentDiagram.nodeLayer;
    }
    return _xifexpression;
  }
  
  public Group getButtonLayer() {
    return this.buttonLayer;
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
}
