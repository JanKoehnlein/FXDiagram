package de.itemis.javafx.diagram;

import com.google.common.collect.Iterables;
import de.itemis.javafx.diagram.Activateable;
import de.itemis.javafx.diagram.XConnection;
import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.behavior.RapidButton;
import de.itemis.javafx.diagram.tools.SelectionTool;
import de.itemis.javafx.diagram.tools.ZoomTool;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XDiagram {
  private Group rootPane = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private Group nodeLayer = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private Group connectionLayer = new Function0<Group>() {
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
  
  private List<RapidButton> buttons = new Function0<List<RapidButton>>() {
    public List<RapidButton> apply() {
      ArrayList<RapidButton> _newArrayList = CollectionLiterals.<RapidButton>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private boolean isActive;
  
  public XDiagram() {
    ObservableList<Node> _children = this.rootPane.getChildren();
    _children.add(this.nodeLayer);
    ObservableList<Node> _children_1 = this.rootPane.getChildren();
    _children_1.add(this.connectionLayer);
    ObservableList<Node> _children_2 = this.rootPane.getChildren();
    _children_2.add(this.buttonLayer);
  }
  
  public void addNode(final XNode node) {
    ObservableList<Node> _children = this.nodeLayer.getChildren();
    _children.add(node);
    this.nodes.add(node);
    node.setDiagram(this);
    if (this.isActive) {
      node.activate();
    }
  }
  
  public void addConnection(final XConnection connection) {
    ObservableList<Node> _children = this.connectionLayer.getChildren();
    _children.add(connection);
    this.connections.add(connection);
    if (this.isActive) {
      connection.activate();
    }
  }
  
  public void addButton(final RapidButton button) {
    ObservableList<Node> _children = this.buttonLayer.getChildren();
    _children.add(button);
    this.buttons.add(button);
    if (this.isActive) {
      button.activate();
    }
  }
  
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      Iterable<Object> _plus = Iterables.<Object>concat(this.nodes, this.connections);
      Iterable<Object> _plus_1 = Iterables.<Object>concat(_plus, this.buttons);
      final Procedure1<Object> _function = new Procedure1<Object>() {
          public void apply(final Object it) {
            ((Activateable)it).activate();
          }
        };
      IterableExtensions.<Object>forEach(_plus_1, _function);
      new ZoomTool(this);
      new SelectionTool(this);
      boolean _isActive = this.isActive = true;
      _xblockexpression = (_isActive);
    }
    return _xblockexpression;
  }
  
  public List<XNode> getShapes() {
    return this.nodes;
  }
  
  public List<XConnection> getConnections() {
    return this.connections;
  }
  
  public Group getRootPane() {
    return this.rootPane;
  }
}
