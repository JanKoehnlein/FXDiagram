package de.itemis.javafx.diagram;

import de.itemis.javafx.diagram.XAbstractDiagram;
import de.itemis.javafx.diagram.tools.SelectionTool;
import de.itemis.javafx.diagram.tools.ZoomTool;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class XRootDiagram extends XAbstractDiagram {
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
  
  public XRootDiagram() {
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.nodeLayer);
    ObservableList<Node> _children_1 = this.getChildren();
    _children_1.add(this.connectionLayer);
    ObservableList<Node> _children_2 = this.getChildren();
    _children_2.add(this.buttonLayer);
  }
  
  public void doActivate() {
    super.doActivate();
    new ZoomTool(this);
    new SelectionTool(this);
  }
  
  public Group getNodeLayer() {
    return this.nodeLayer;
  }
  
  public Group getConnectionLayer() {
    return this.connectionLayer;
  }
  
  public Group getButtonLayer() {
    return this.buttonLayer;
  }
}
