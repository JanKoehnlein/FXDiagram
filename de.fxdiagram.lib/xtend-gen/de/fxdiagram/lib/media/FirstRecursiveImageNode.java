package de.fxdiagram.lib.media;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.lib.media.RecursiveImageNode;
import java.util.Deque;
import java.util.LinkedList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class FirstRecursiveImageNode extends XNode {
  private RecursiveImageNode recursiveImageNode;
  
  private Deque<Group> panes = new LinkedList<Group>();
  
  public FirstRecursiveImageNode(final RecursiveImageNode parent) {
    super((parent.getName() + "_"));
    this.recursiveImageNode = parent;
  }
  
  @Override
  protected Node createNode() {
    Group _xblockexpression = null;
    {
      final Group pane = this.recursiveImageNode.createPane();
      this.panes.push(pane);
      _xblockexpression = pane;
    }
    return _xblockexpression;
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    this.layoutXProperty().bindBidirectional(this.recursiveImageNode.xProperty());
    this.layoutYProperty().bindBidirectional(this.recursiveImageNode.yProperty());
    this.scaleXProperty().bind(this.recursiveImageNode.scaleProperty());
    this.scaleYProperty().bind(this.recursiveImageNode.scaleProperty());
    this.updateChildPanes();
    final ChangeListener<Number> _function = (ObservableValue<? extends Number> prop, Number oldVal, Number newVal) -> {
      this.updateChildPanes();
    };
    CoreExtensions.getDiagram(this).getViewportTransform().scaleProperty().addListener(_function);
  }
  
  @Override
  public void selectionFeedback(final boolean isSelected) {
  }
  
  public void updateChildPanes() {
    while ((!this.panes.isEmpty())) {
      {
        final Group child = this.panes.pop();
        final Group parent = this.panes.peek();
        final Bounds bounds = child.localToScene(child.getBoundsInLocal());
        double _width = bounds.getWidth();
        double _height = bounds.getHeight();
        final double area = (_width * _height);
        if ((area <= 10)) {
          if ((parent != null)) {
            ObservableList<Node> _children = parent.getChildren();
            _children.remove(child);
          } else {
            this.panes.push(child);
            return;
          }
        } else {
          if ((area > 500)) {
            final Group grandChild = this.createScaledPane();
            ObservableList<Node> _children_1 = child.getChildren();
            _children_1.add(grandChild);
            this.panes.push(child);
            this.panes.push(grandChild);
          } else {
            this.panes.push(child);
            return;
          }
        }
      }
    }
  }
  
  public Group createScaledPane() {
    Group _createPane = this.recursiveImageNode.createPane();
    final Procedure1<Group> _function = (Group it) -> {
      it.scaleXProperty().bind(this.recursiveImageNode.scaleProperty());
      it.scaleYProperty().bind(this.recursiveImageNode.scaleProperty());
      it.layoutXProperty().bind(this.recursiveImageNode.xProperty());
      it.layoutYProperty().bind(this.recursiveImageNode.yProperty());
    };
    return ObjectExtensions.<Group>operator_doubleArrow(_createPane, _function);
  }
}
