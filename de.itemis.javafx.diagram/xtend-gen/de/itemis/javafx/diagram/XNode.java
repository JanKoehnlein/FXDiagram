package de.itemis.javafx.diagram;

import de.itemis.javafx.diagram.Activateable;
import de.itemis.javafx.diagram.AnchorPoints;
import de.itemis.javafx.diagram.XDiagram;
import de.itemis.javafx.diagram.behavior.MoveBehavior;
import de.itemis.javafx.diagram.behavior.SelectionBehavior;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XNode extends Group implements Activateable {
  private Node node;
  
  private Effect mouseOverEffect;
  
  private Effect originalEffect;
  
  private AnchorPoints anchorPoints;
  
  private SelectionBehavior selectionBehavior;
  
  private MoveBehavior moveBehavior;
  
  private XDiagram _diagram;
  
  public XDiagram getDiagram() {
    return this._diagram;
  }
  
  public void setDiagram(final XDiagram diagram) {
    this._diagram = diagram;
  }
  
  public XNode() {
    InnerShadow _innerShadow = new InnerShadow();
    this.mouseOverEffect = _innerShadow;
  }
  
  public void setNode(final Node node) {
    this.node = node;
    ObservableList<Node> _children = this.getChildren();
    _children.add(node);
  }
  
  public void activate() {
    SelectionBehavior _selectionBehavior = new SelectionBehavior(this);
    this.selectionBehavior = _selectionBehavior;
    MoveBehavior _moveBehavior = new MoveBehavior(this);
    this.moveBehavior = _moveBehavior;
    AnchorPoints _anchorPoints = new AnchorPoints(this);
    this.anchorPoints = _anchorPoints;
    this.selectionBehavior.activate();
    this.moveBehavior.activate();
    final Procedure1<MouseEvent> _function = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          Effect _effect = XNode.this.node.getEffect();
          XNode.this.originalEffect = _effect;
          XNode.this.node.setEffect(XNode.this.mouseOverEffect);
        }
      };
    this.setOnMouseEntered(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function.apply(arg0);
        }
    });
    final Procedure1<MouseEvent> _function_1 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          XNode.this.node.setEffect(XNode.this.originalEffect);
        }
      };
    this.setOnMouseExited(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_1.apply(arg0);
        }
    });
  }
  
  public SelectionBehavior getSelectionBehavior() {
    return this.selectionBehavior;
  }
  
  public MoveBehavior getMoveBehavior() {
    return this.moveBehavior;
  }
  
  public AnchorPoints getAnchorPoints() {
    return this.anchorPoints;
  }
  
  public Node getNode() {
    return this.node;
  }
}
