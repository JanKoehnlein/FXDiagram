package de.itemis.javafx.diagram.example;

import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.example.AddRapidButtonBehavior;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
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
public class MyNode extends XNode {
  private AddRapidButtonBehavior rapidButtonBehavior;
  
  public MyNode(final String name) {
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
        public void apply(final StackPane it) {
          ObservableList<Node> _children = it.getChildren();
          Rectangle _rectangle = new Rectangle();
          final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
              public void apply(final Rectangle it) {
                it.setWidth(80);
                it.setHeight(30);
                LinearGradient _createFill = MyNode.this.createFill();
                it.setFill(_createFill);
                Color _gray = Color.gray(0.5);
                it.setStroke(_gray);
                it.setStrokeWidth(1.2);
                it.setArcWidth(12);
                it.setArcHeight(12);
              }
            };
          Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
          _children.add(_doubleArrow);
          ObservableList<Node> _children_1 = it.getChildren();
          Label _label = new Label();
          final Procedure1<Label> _function_1 = new Procedure1<Label>() {
              public void apply(final Label it) {
                it.setText(name);
              }
            };
          Label _doubleArrow_1 = ObjectExtensions.<Label>operator_doubleArrow(_label, _function_1);
          _children_1.add(_doubleArrow_1);
        }
      };
    StackPane _doubleArrow = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
    this.setNode(_doubleArrow);
    Node _node = this.getNode();
    InnerShadow _innerShadow = new InnerShadow();
    final Procedure1<InnerShadow> _function_1 = new Procedure1<InnerShadow>() {
        public void apply(final InnerShadow it) {
          it.setRadius(7);
        }
      };
    InnerShadow _doubleArrow_1 = ObjectExtensions.<InnerShadow>operator_doubleArrow(_innerShadow, _function_1);
    _node.setEffect(_doubleArrow_1);
  }
  
  public void activate() {
    super.activate();
    AddRapidButtonBehavior _addRapidButtonBehavior = new AddRapidButtonBehavior(this);
    this.rapidButtonBehavior = _addRapidButtonBehavior;
    this.rapidButtonBehavior.activate();
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
