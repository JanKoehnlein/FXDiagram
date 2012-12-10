package de.itemis.javafx.diagram;

import de.itemis.javafx.diagram.Connection;
import de.itemis.javafx.diagram.ShapeContainer;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class Diagram {
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
  
  private List<ShapeContainer> shapes = new Function0<List<ShapeContainer>>() {
    public List<ShapeContainer> apply() {
      ArrayList<ShapeContainer> _newArrayList = CollectionLiterals.<ShapeContainer>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private List<Connection> connections = new Function0<List<Connection>>() {
    public List<Connection> apply() {
      ArrayList<Connection> _newArrayList = CollectionLiterals.<Connection>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  public Diagram() {
    ObservableList<Node> _children = this.rootPane.getChildren();
    _children.add(this.nodeLayer);
    ObservableList<Node> _children_1 = this.rootPane.getChildren();
    _children_1.add(this.connectionLayer);
    ObservableList<Node> _children_2 = this.rootPane.getChildren();
    _children_2.add(this.buttonLayer);
  }
  
  public boolean addShape(final ShapeContainer shape) {
    boolean _xblockexpression = false;
    {
      ObservableList<Node> _children = this.nodeLayer.getChildren();
      _children.add(shape);
      shape.setDiagram(this);
      boolean _add = this.shapes.add(shape);
      _xblockexpression = (_add);
    }
    return _xblockexpression;
  }
  
  public boolean addConnection(final Connection connection) {
    boolean _xblockexpression = false;
    {
      ObservableList<Node> _children = this.connectionLayer.getChildren();
      _children.add(connection);
      boolean _add = this.connections.add(connection);
      _xblockexpression = (_add);
    }
    return _xblockexpression;
  }
  
  public boolean addButton(final Node button) {
    ObservableList<Node> _children = this.buttonLayer.getChildren();
    boolean _add = _children.add(button);
    return _add;
  }
  
  public List<ShapeContainer> getShapes() {
    return this.shapes;
  }
  
  public List<Connection> getConnections() {
    return this.connections;
  }
  
  public Group getRootPane() {
    return this.rootPane;
  }
}
