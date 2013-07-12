package de.fxdiagram.core;

import de.fxdiagram.core.XRootDiagram;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;

@SuppressWarnings("all")
public class XRoot extends Group {
  private XRootDiagram diagram;
  
  public XRoot() {
    XRootDiagram _xRootDiagram = new XRootDiagram(this);
    this.diagram = _xRootDiagram;
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.diagram);
  }
  
  public XRootDiagram getDiagram() {
    return this.diagram;
  }
}
