package de.fxdiagram.core;

import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XRoot;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@Logging
@SuppressWarnings("all")
public class XRootDiagram extends XAbstractDiagram {
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
  
  private XRoot root;
  
  public XRootDiagram(final XRoot root) {
    this.root = root;
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.nodeLayer);
    ObservableList<Node> _children_1 = this.getChildren();
    _children_1.add(this.buttonLayer);
  }
  
  public void doActivate() {
    super.doActivate();
  }
  
  public Group getNodeLayer() {
    return this.nodeLayer;
  }
  
  public Group getConnectionLayer() {
    return this.nodeLayer;
  }
  
  public Group getButtonLayer() {
    return this.buttonLayer;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.XRootDiagram");
    ;
}
