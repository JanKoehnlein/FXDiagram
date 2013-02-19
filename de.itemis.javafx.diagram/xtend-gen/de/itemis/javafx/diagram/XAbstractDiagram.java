package de.itemis.javafx.diagram;

import de.itemis.javafx.diagram.XActivatable;
import de.itemis.javafx.diagram.XConnection;
import de.itemis.javafx.diagram.XConnectionLabel;
import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.XRapidButton;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public abstract class XAbstractDiagram extends Pane implements XActivatable {
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
  
  private boolean isActive;
  
  public abstract Group getNodeLayer();
  
  public abstract Group getButtonLayer();
  
  public abstract Group getConnectionLayer();
  
  public void activate() {
    boolean _not = (!this.isActive);
    if (_not) {
      this.doActivate();
    }
    this.isActive = true;
  }
  
  public void doActivate() {
    final Procedure1<XNode> _function = new Procedure1<XNode>() {
        public void apply(final XNode it) {
          it.activate();
        }
      };
    IterableExtensions.<XNode>forEach(this.nodes, _function);
    final Procedure1<XConnection> _function_1 = new Procedure1<XConnection>() {
        public void apply(final XConnection it) {
          it.activate();
        }
      };
    IterableExtensions.<XConnection>forEach(this.connections, _function_1);
    final Procedure1<XRapidButton> _function_2 = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton it) {
          it.activate();
        }
      };
    IterableExtensions.<XRapidButton>forEach(this.buttons, _function_2);
  }
  
  public void addNode(final XNode node) {
    Group _nodeLayer = this.getNodeLayer();
    ObservableList<Node> _children = _nodeLayer.getChildren();
    _children.add(node);
    this.internalAddNode(node);
    if (this.isActive) {
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
    boolean _notEquals = ObjectExtensions.operator_notEquals(_label, null);
    if (_notEquals) {
      Group _connectionLayer_1 = this.getConnectionLayer();
      ObservableList<Node> _children_1 = _connectionLayer_1.getChildren();
      XConnectionLabel _label_1 = connection.getLabel();
      _children_1.add(_label_1);
    }
    this.internalAddConnection(connection);
    if (this.isActive) {
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
    if (this.isActive) {
      button.activate();
    }
  }
  
  public boolean internalAddButton(final XRapidButton button) {
    boolean _add = this.buttons.add(button);
    return _add;
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
  
  protected boolean isActive() {
    return this.isActive;
  }
}
