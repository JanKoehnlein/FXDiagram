package de.itemis.javafx.diagram.example;

import de.itemis.javafx.diagram.XAbstractDiagram;
import de.itemis.javafx.diagram.XNestedDiagram;
import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.behavior.LevelOfDetailBehavior;
import de.itemis.javafx.diagram.example.AddRapidButtonBehavior;
import de.itemis.javafx.diagram.example.MyNode;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class MyContainerNode extends XNode {
  private AddRapidButtonBehavior rapidButtonBehavior;
  
  private LevelOfDetailBehavior levelOfDetailBehavior;
  
  private Node label;
  
  private XNestedDiagram innerDiagram;
  
  public MyContainerNode(final String name) {
    Label _label = new Label();
    final Procedure1<Label> _function = new Procedure1<Label>() {
        public void apply(final Label it) {
          it.setText(name);
        }
      };
    Label _doubleArrow = ObjectExtensions.<Label>operator_doubleArrow(_label, _function);
    this.label = _doubleArrow;
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function_1 = new Procedure1<StackPane>() {
        public void apply(final StackPane it) {
          Rectangle _rectangle = new Rectangle();
          final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
              public void apply(final Rectangle it) {
                it.setWidth(80);
                it.setHeight(30);
                LinearGradient _createFill = MyContainerNode.this.createFill();
                it.setFill(_createFill);
                Color _gray = Color.gray(0.5);
                it.setStroke(_gray);
                it.setStrokeWidth(1.2);
                it.setArcWidth(12);
                it.setArcHeight(12);
              }
            };
          final Rectangle rectangle = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
          ObservableList<Node> _children = it.getChildren();
          _children.add(rectangle);
          ObservableList<Node> _children_1 = it.getChildren();
          _children_1.add(MyContainerNode.this.label);
          Rectangle _rectangle_1 = new Rectangle();
          final Procedure1<Rectangle> _function_1 = new Procedure1<Rectangle>() {
              public void apply(final Rectangle it) {
                double _width = rectangle.getWidth();
                double _strokeWidth = rectangle.getStrokeWidth();
                double _plus = (_width + _strokeWidth);
                it.setWidth(_plus);
                double _height = rectangle.getHeight();
                double _strokeWidth_1 = rectangle.getStrokeWidth();
                double _plus_1 = (_height + _strokeWidth_1);
                it.setHeight(_plus_1);
              }
            };
          Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle_1, _function_1);
          it.setClip(_doubleArrow);
        }
      };
    StackPane _doubleArrow_1 = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function_1);
    this.setNode(_doubleArrow_1);
    Node _node = this.getNode();
    InnerShadow _innerShadow = new InnerShadow();
    final Procedure1<InnerShadow> _function_2 = new Procedure1<InnerShadow>() {
        public void apply(final InnerShadow it) {
          it.setRadius(7);
        }
      };
    InnerShadow _doubleArrow_2 = ObjectExtensions.<InnerShadow>operator_doubleArrow(_innerShadow, _function_2);
    _node.setEffect(_doubleArrow_2);
  }
  
  public void activate() {
    super.activate();
    XNestedDiagram _xNestedDiagram = new XNestedDiagram();
    this.innerDiagram = _xNestedDiagram;
    XAbstractDiagram _diagram = this.getDiagram();
    this.innerDiagram.setParentDiagram(_diagram);
    this.innerDiagram.activate();
    final Procedure1<XNestedDiagram> _function = new Procedure1<XNestedDiagram>() {
        public void apply(final XNestedDiagram it) {
          MyNode _myNode = new MyNode("Inner");
          it.addNode(_myNode);
        }
      };
    ObjectExtensions.<XNestedDiagram>operator_doubleArrow(
      this.innerDiagram, _function);
    Node _node = this.getNode();
    ObservableList<Node> _children = ((StackPane) _node).getChildren();
    _children.add(this.innerDiagram);
    AddRapidButtonBehavior _addRapidButtonBehavior = new AddRapidButtonBehavior(this);
    this.rapidButtonBehavior = _addRapidButtonBehavior;
    this.rapidButtonBehavior.activate();
    Node _node_1 = this.getNode();
    LevelOfDetailBehavior _levelOfDetailBehavior = new LevelOfDetailBehavior(this, ((Pane) _node_1), this.label);
    this.levelOfDetailBehavior = _levelOfDetailBehavior;
    this.levelOfDetailBehavior.addChildForThreshold(10000.0, this.innerDiagram);
    this.levelOfDetailBehavior.activate();
  }
  
  protected LinearGradient createFill() {
    LinearGradient _xblockexpression = null;
    {
      Color _gray = Color.gray(0.6);
      Stop _stop = new Stop(0, _gray);
      Color _gray_1 = Color.gray(0.9);
      Stop _stop_1 = new Stop(1, _gray_1);
      final ArrayList<Stop> stops = CollectionLiterals.<Stop>newArrayList(_stop, _stop_1);
      LinearGradient _linearGradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
      _xblockexpression = (_linearGradient);
    }
    return _xblockexpression;
  }
}
