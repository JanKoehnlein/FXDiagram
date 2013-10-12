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
import de.fxdiagram.core.extensions.CoreExtensions;
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
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
    final ChangeListener<Node> labelListener = _function;
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
    final Procedure1<Change<? extends Node>> _function_1 = new Procedure1<Change<? extends Node>>() {
      public void apply(final Change<? extends Node> change) {
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
                  ObjectProperty<XConnectionLabel> _labelProperty = it.labelProperty();
                  XDiagram.this.addConnectionDecoration(_labelProperty, labelListener);
                  ObjectProperty<ArrowHead> _targetArrowHeadProperty = it.targetArrowHeadProperty();
                  XDiagram.this.addConnectionDecoration(_targetArrowHeadProperty, labelListener);
                  ObjectProperty<ArrowHead> _sourceArrowHeadProperty = it.sourceArrowHeadProperty();
                  XDiagram.this.addConnectionDecoration(_sourceArrowHeadProperty, labelListener);
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
                  ObjectProperty<XConnectionLabel> _labelProperty = it.labelProperty();
                  XDiagram.this.removeConnectionDecoration(_labelProperty, labelListener);
                  ObjectProperty<ArrowHead> _targetArrowHeadProperty = it.targetArrowHeadProperty();
                  XDiagram.this.removeConnectionDecoration(_targetArrowHeadProperty, labelListener);
                  ObjectProperty<ArrowHead> _sourceArrowHeadProperty = it.sourceArrowHeadProperty();
                  XDiagram.this.removeConnectionDecoration(_sourceArrowHeadProperty, labelListener);
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
    final Procedure1<Change<? extends Node>> listChangeListener = _function_1;
    Group _connectionLayer_1 = this.getConnectionLayer();
    ObservableList<Node> _children = _connectionLayer_1.getChildren();
    _children.addListener(new ListChangeListener<Node>() {
        public void onChanged(Change<? extends Node> c) {
          listChangeListener.apply(c);
        }
    });
    ObservableList<XNode> _nodes_1 = this.getNodes();
    ObservableList<XConnection> _connections_1 = this.getConnections();
    Iterable<XShape> _plus = Iterables.<XShape>concat(_nodes_1, _connections_1);
    ObservableList<XRapidButton> _buttons_1 = this.getButtons();
    Iterable<Parent> _plus_1 = Iterables.<Parent>concat(_plus, _buttons_1);
    final Procedure1<Parent> _function_2 = new Procedure1<Parent>() {
      public void apply(final Parent it) {
        ((XActivatable)it).activate();
      }
    };
    IterableExtensions.<Parent>forEach(_plus_1, _function_2);
    AuxiliaryLinesSupport _auxiliaryLinesSupport = new AuxiliaryLinesSupport(this);
    this.auxiliaryLinesSupport = _auxiliaryLinesSupport;
    if (this.contentsInitializer!=null) {
      this.contentsInitializer.apply(this);
    }
  }
  
  protected void addConnectionDecoration(final Property<? extends Node> property, final ChangeListener<? super Node> listener) {
    Node _value = property.getValue();
    boolean _notEquals = (!Objects.equal(_value, null));
    if (_notEquals) {
      Group _connectionLayer = this.getConnectionLayer();
      ObservableList<Node> _children = _connectionLayer.getChildren();
      Node _value_1 = property.getValue();
      _children.add(_value_1);
    }
    property.addListener(listener);
  }
  
  protected void removeConnectionDecoration(final Property<? extends Node> property, final ChangeListener<? super Node> listener) {
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
