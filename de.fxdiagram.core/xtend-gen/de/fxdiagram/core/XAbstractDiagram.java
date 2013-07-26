package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XDiagramChildrenListener;
import de.fxdiagram.core.XNestedDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.auxlines.AuxiliaryLinesSupport;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * A diagram is a group, as such not resizable from the outside.
 */
@SuppressWarnings("all")
public abstract class XAbstractDiagram extends Parent implements XActivatable {
  public abstract Group getNodeLayer();
  
  public abstract Group getConnectionLayer();
  
  public abstract Group getButtonLayer();
  
  private AuxiliaryLinesSupport auxiliaryLinesSupport;
  
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.doActivate();
    }
    this.isActiveProperty.set(true);
  }
  
  public void doActivate() {
    this.isActiveProperty.set(true);
    final ChangeListener<XConnectionLabel> _function = new ChangeListener<XConnectionLabel>() {
      public void changed(final ObservableValue<? extends XConnectionLabel> prop, final XConnectionLabel oldVal, final XConnectionLabel newVal) {
        boolean _notEquals = (!Objects.equal(oldVal, null));
        if (_notEquals) {
          Group _connectionLayer = XAbstractDiagram.this.getConnectionLayer();
          ObservableList<Node> _children = _connectionLayer.getChildren();
          _children.remove(oldVal);
        }
        boolean _notEquals_1 = (!Objects.equal(newVal, null));
        if (_notEquals_1) {
          Group _connectionLayer_1 = XAbstractDiagram.this.getConnectionLayer();
          ObservableList<Node> _children_1 = _connectionLayer_1.getChildren();
          _children_1.add(newVal);
        }
      }
    };
    final ChangeListener<XConnectionLabel> labelListener = _function;
    ObservableList<XNode> _nodes = this.getNodes();
    Group _nodeLayer = this.getNodeLayer();
    XDiagramChildrenListener<XNode> _xDiagramChildrenListener = new XDiagramChildrenListener<XNode>(this, _nodeLayer);
    _nodes.addListener(_xDiagramChildrenListener);
    ObservableList<XConnection> _connections = this.getConnections();
    Group _connectionLayer = this.getConnectionLayer();
    XDiagramChildrenListener<XConnection> _xDiagramChildrenListener_1 = new XDiagramChildrenListener<XConnection>(this, _connectionLayer);
    _connections.addListener(_xDiagramChildrenListener_1);
    ObservableList<XRapidButton> _buttons = this.getButtons();
    Group _buttonLayer = this.getButtonLayer();
    XDiagramChildrenListener<XRapidButton> _xDiagramChildrenListener_2 = new XDiagramChildrenListener<XRapidButton>(this, _buttonLayer);
    _buttons.addListener(_xDiagramChildrenListener_2);
    Group _connectionLayer_1 = this.getConnectionLayer();
    ObservableList<Node> _children = _connectionLayer_1.getChildren();
    final ListChangeListener<Node> _function_1 = new ListChangeListener<Node>() {
      public void onChanged(final Change<? extends Node> change) {
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
                  XConnectionLabel _label = it.getLabel();
                  boolean _notEquals = (!Objects.equal(_label, null));
                  if (_notEquals) {
                    Group _connectionLayer = XAbstractDiagram.this.getConnectionLayer();
                    ObservableList<Node> _children = _connectionLayer.getChildren();
                    XConnectionLabel _label_1 = it.getLabel();
                    _children.add(_label_1);
                  }
                  ObjectProperty<XConnectionLabel> _labelProperty = it.labelProperty();
                  _labelProperty.addListener(labelListener);
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
                  XConnectionLabel _label = it.getLabel();
                  boolean _notEquals = (!Objects.equal(_label, null));
                  if (_notEquals) {
                    Group _connectionLayer = XAbstractDiagram.this.getConnectionLayer();
                    ObservableList<Node> _children = _connectionLayer.getChildren();
                    XConnectionLabel _label_1 = it.getLabel();
                    _children.remove(_label_1);
                  }
                  ObjectProperty<XConnectionLabel> _labelProperty = it.labelProperty();
                  _labelProperty.removeListener(labelListener);
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
    _children.addListener(_function_1);
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
  }
  
  public AuxiliaryLinesSupport getAuxiliaryLinesSupport() {
    return this.auxiliaryLinesSupport;
  }
  
  public Iterable<XShape> getAllShapes() {
    ObservableList<XNode> _nodes = this.getNodes();
    ObservableList<XConnection> _connections = this.getConnections();
    Iterable<XShape> _plus = Iterables.<XShape>concat(_nodes, _connections);
    ObservableList<XConnection> _connections_1 = this.getConnections();
    final Function1<XConnection,XConnectionLabel> _function = new Function1<XConnection,XConnectionLabel>() {
      public XConnectionLabel apply(final XConnection it) {
        XConnectionLabel _label = it.getLabel();
        return _label;
      }
    };
    List<XConnectionLabel> _map = ListExtensions.<XConnection, XConnectionLabel>map(_connections_1, _function);
    Iterable<XConnectionLabel> _filterNull = IterableExtensions.<XConnectionLabel>filterNull(_map);
    Iterable<XShape> _plus_1 = Iterables.<XShape>concat(_plus, _filterNull);
    ObservableList<XConnection> _connections_2 = this.getConnections();
    final Function1<XConnection,ObservableList<XControlPoint>> _function_1 = new Function1<XConnection,ObservableList<XControlPoint>>() {
      public ObservableList<XControlPoint> apply(final XConnection it) {
        ObservableList<XControlPoint> _controlPoints = it.getControlPoints();
        return _controlPoints;
      }
    };
    List<ObservableList<XControlPoint>> _map_1 = ListExtensions.<XConnection, ObservableList<XControlPoint>>map(_connections_2, _function_1);
    Iterable<XControlPoint> _flatten = Iterables.<XControlPoint>concat(_map_1);
    Iterable<XShape> _plus_2 = Iterables.<XShape>concat(_plus_1, _flatten);
    ObservableList<XNestedDiagram> _subDiagrams = this.getSubDiagrams();
    final Function1<XNestedDiagram,Iterable<XShape>> _function_2 = new Function1<XNestedDiagram,Iterable<XShape>>() {
      public Iterable<XShape> apply(final XNestedDiagram it) {
        Iterable<XShape> _allShapes = it.getAllShapes();
        return _allShapes;
      }
    };
    List<Iterable<XShape>> _map_2 = ListExtensions.<XNestedDiagram, Iterable<XShape>>map(_subDiagrams, _function_2);
    Iterable<XShape> _flatten_1 = Iterables.<XShape>concat(_map_2);
    Iterable<XShape> _plus_3 = Iterables.<XShape>concat(_plus_2, _flatten_1);
    return _plus_3;
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
  
  private SimpleListProperty<XNestedDiagram> subDiagramsProperty = new SimpleListProperty<XNestedDiagram>(this, "subDiagrams",_initSubDiagrams());
  
  private static final ObservableList<XNestedDiagram> _initSubDiagrams() {
    ObservableList<XNestedDiagram> _observableArrayList = FXCollections.<XNestedDiagram>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<XNestedDiagram> getSubDiagrams() {
    return this.subDiagramsProperty.get();
    
  }
  
  public ListProperty<XNestedDiagram> subDiagramsProperty() {
    return this.subDiagramsProperty;
    
  }
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
    
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
    
  }
}
