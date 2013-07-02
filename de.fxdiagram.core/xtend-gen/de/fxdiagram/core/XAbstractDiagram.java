package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * A diagram is a group, as such not resizable from the outside.
 */
@SuppressWarnings("all")
public abstract class XAbstractDiagram extends Parent implements XActivatable {
  private List<XNode> nodes = new Function0<List<XNode>>() {
    public List<XNode> apply() {
      ArrayList<XNode> _newArrayList = CollectionLiterals.<XNode>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private List<XConnection> connections = new Function0<List<XConnection>>() {
    public List<XConnection> apply() {
      ArrayList<XConnection> _newArrayList = CollectionLiterals.<XConnection>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private List<XRapidButton> buttons = new Function0<List<XRapidButton>>() {
    public List<XRapidButton> apply() {
      ArrayList<XRapidButton> _newArrayList = CollectionLiterals.<XRapidButton>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  public abstract Group getNodeLayer();
  
  public abstract Group getButtonLayer();
  
  public abstract Group getConnectionLayer();
  
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
    Iterable<Node> _plus = Iterables.<Node>concat(this.nodes, this.connections);
    Iterable<Node> _plus_1 = Iterables.<Node>concat(_plus, this.buttons);
    final Procedure1<Node> _function = new Procedure1<Node>() {
        public void apply(final Node it) {
          ((XActivatable)it).activate();
        }
      };
    IterableExtensions.<Node>forEach(_plus_1, _function);
  }
  
  public void addNode(final XNode node) {
    Group _nodeLayer = this.getNodeLayer();
    ObservableList<Node> _children = _nodeLayer.getChildren();
    _children.add(node);
    this.internalAddNode(node);
    boolean _isActive = this.getIsActive();
    if (_isActive) {
      node.activate();
    }
  }
  
  public boolean internalAddNode(final XNode node) {
    boolean _add = this.nodes.add(node);
    return _add;
  }
  
  public void addConnection(final XConnection connection) {
    Group _connectionLayer = this.getConnectionLayer();
    ObservableList<Node> _children = _connectionLayer.getChildren();
    _children.add(connection);
    XConnectionLabel _label = connection.getLabel();
    boolean _notEquals = (!Objects.equal(_label, null));
    if (_notEquals) {
      Group _connectionLayer_1 = this.getConnectionLayer();
      ObservableList<Node> _children_1 = _connectionLayer_1.getChildren();
      XConnectionLabel _label_1 = connection.getLabel();
      _children_1.add(_label_1);
    }
    this.internalAddConnection(connection);
    boolean _isActive = this.getIsActive();
    if (_isActive) {
      connection.activate();
    }
  }
  
  public boolean internalAddConnection(final XConnection connection) {
    boolean _add = this.connections.add(connection);
    return _add;
  }
  
  public void addButton(final XRapidButton button) {
    Group _buttonLayer = this.getButtonLayer();
    ObservableList<Node> _children = _buttonLayer.getChildren();
    _children.add(button);
    this.internalAddButton(button);
    boolean _isActive = this.getIsActive();
    if (_isActive) {
      button.activate();
    }
  }
  
  public boolean internalAddButton(final XRapidButton button) {
    boolean _add = this.buttons.add(button);
    return _add;
  }
  
  public boolean removeNode(final XNode node) {
    boolean _xblockexpression = false;
    {
      Group _nodeLayer = this.getNodeLayer();
      ObservableList<Node> _children = _nodeLayer.getChildren();
      _children.remove(node);
      boolean _internalRemoveNode = this.internalRemoveNode(node);
      _xblockexpression = (_internalRemoveNode);
    }
    return _xblockexpression;
  }
  
  public boolean internalRemoveNode(final XNode node) {
    boolean _remove = this.nodes.remove(node);
    return _remove;
  }
  
  public boolean removeConnection(final XConnection connection) {
    boolean _xblockexpression = false;
    {
      Group _connectionLayer = this.getConnectionLayer();
      ObservableList<Node> _children = _connectionLayer.getChildren();
      _children.remove(connection);
      XConnectionLabel _label = connection.getLabel();
      boolean _notEquals = (!Objects.equal(_label, null));
      if (_notEquals) {
        Group _connectionLayer_1 = this.getConnectionLayer();
        ObservableList<Node> _children_1 = _connectionLayer_1.getChildren();
        XConnectionLabel _label_1 = connection.getLabel();
        _children_1.remove(_label_1);
      }
      boolean _internalRemoveConnection = this.internalRemoveConnection(connection);
      _xblockexpression = (_internalRemoveConnection);
    }
    return _xblockexpression;
  }
  
  public boolean internalRemoveConnection(final XConnection connection) {
    boolean _remove = this.connections.remove(connection);
    return _remove;
  }
  
  public boolean removeButton(final XRapidButton button) {
    boolean _xblockexpression = false;
    {
      Group _buttonLayer = this.getButtonLayer();
      ObservableList<Node> _children = _buttonLayer.getChildren();
      _children.remove(button);
      boolean _internalRemoveButton = this.internalRemoveButton(button);
      _xblockexpression = (_internalRemoveButton);
    }
    return _xblockexpression;
  }
  
  public boolean internalRemoveButton(final XRapidButton button) {
    boolean _remove = this.buttons.remove(button);
    return _remove;
  }
  
  public List<XNode> getNodes() {
    return this.nodes;
  }
  
  public List<XRapidButton> getButtons() {
    return this.buttons;
  }
  
  public List<XConnection> getConnections() {
    return this.connections;
  }
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
    
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
    
  }
}
