package de.fxdigram.lib.layout.tests;

import de.fxdiagram.core.XNestedDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.lib.shapes.SimpleNode;
import java.util.List;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LayoutTests extends Application {
  private XNestedDiagram nestedDiagram;
  
  private XRootDiagram diagram;
  
  public static void main(final String... args) {
    Application.launch();
  }
  
  public void start(final Stage stage) throws Exception {
    XNestedDiagram _xNestedDiagram = new XNestedDiagram();
    final Procedure1<XNestedDiagram> _function = new Procedure1<XNestedDiagram>() {
        public void apply(final XNestedDiagram it) {
          final Procedure1<XNestedDiagram> _function = new Procedure1<XNestedDiagram>() {
              public void apply(final XNestedDiagram it) {
                SimpleNode _simpleNode = new SimpleNode("Foo");
                final Procedure1<SimpleNode> _function = new Procedure1<SimpleNode>() {
                    public void apply(final SimpleNode it) {
                      int _minus = (-100);
                      int _minus_1 = (-100);
                      it.relocate(_minus, _minus_1);
                      it.setWidth(65);
                      it.setHeight(40);
                    }
                  };
                SimpleNode _doubleArrow = ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode, _function);
                it.addNode(_doubleArrow);
                SimpleNode _simpleNode_1 = new SimpleNode("Bar");
                final Procedure1<SimpleNode> _function_1 = new Procedure1<SimpleNode>() {
                    public void apply(final SimpleNode it) {
                      it.relocate(100, 100);
                    }
                  };
                SimpleNode _doubleArrow_1 = ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode_1, _function_1);
                it.addNode(_doubleArrow_1);
                it.scaleToFit();
              }
            };
          it.setContentsInitializer(_function);
        }
      };
    XNestedDiagram _doubleArrow = ObjectExtensions.<XNestedDiagram>operator_doubleArrow(_xNestedDiagram, _function);
    this.nestedDiagram = _doubleArrow;
    XRootDiagram _xRootDiagram = new XRootDiagram();
    this.diagram = _xRootDiagram;
    Scene _scene = new Scene(this.diagram, 1024, 768);
    stage.setScene(_scene);
    StackPane _stackPane = new StackPane();
    final StackPane rectangleBorderPane = _stackPane;
    final Procedure1<XRootDiagram> _function_1 = new Procedure1<XRootDiagram>() {
        public void apply(final XRootDiagram it) {
          it.activate();
          XNode _xNode = new XNode();
          final Procedure1<XNode> _function = new Procedure1<XNode>() {
              public void apply(final XNode it) {
                final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
                    public void apply(final StackPane it) {
                      ObservableList<Node> _children = it.getChildren();
                      Group _group = new Group();
                      final Procedure1<Group> _function = new Procedure1<Group>() {
                          public void apply(final Group it) {
                            ObservableList<Node> _children = it.getChildren();
                            _children.add(LayoutTests.this.nestedDiagram);
                          }
                        };
                      Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
                      _children.add(_doubleArrow);
                    }
                  };
                StackPane _doubleArrow = ObjectExtensions.<StackPane>operator_doubleArrow(rectangleBorderPane, _function);
                it.setNode(_doubleArrow);
              }
            };
          XNode _doubleArrow = ObjectExtensions.<XNode>operator_doubleArrow(_xNode, _function);
          it.addNode(_doubleArrow);
          LayoutTests.this.nestedDiagram.activate();
        }
      };
    ObjectExtensions.<XRootDiagram>operator_doubleArrow(
      this.diagram, _function_1);
    stage.show();
    List<XNode> _nodes = this.nestedDiagram.getNodes();
    final Procedure1<XNode> _function_2 = new Procedure1<XNode>() {
        public void apply(final XNode it) {
          LayoutTests.this.printLayoutGeometry(it);
        }
      };
    IterableExtensions.<XNode>forEach(_nodes, _function_2);
    this.printLayoutGeometry(this.nestedDiagram);
    this.printLayoutGeometry(rectangleBorderPane);
    List<XNode> _nodes_1 = this.nestedDiagram.getNodes();
    final Procedure1<XNode> _function_3 = new Procedure1<XNode>() {
        public void apply(final XNode it) {
          LayoutTests.this.printSizes(it);
        }
      };
    IterableExtensions.<XNode>forEach(_nodes_1, _function_3);
    this.printSizes(this.nestedDiagram);
    this.printSizes(rectangleBorderPane);
    this.nestedDiagram.getNodeLayer();
  }
  
  public String printLayoutGeometry(final Node it) {
    String _plus = (it + ": ");
    double _layoutX = it.getLayoutX();
    String _plus_1 = (_plus + Double.valueOf(_layoutX));
    String _plus_2 = (_plus_1 + " ");
    double _layoutY = it.getLayoutY();
    String _plus_3 = (_plus_2 + Double.valueOf(_layoutY));
    String _plus_4 = (_plus_3 + " ");
    Bounds _layoutBounds = it.getLayoutBounds();
    String _plus_5 = (_plus_4 + _layoutBounds);
    String _println = InputOutput.<String>println(_plus_5);
    return _println;
  }
  
  public String printSizes(final Node it) {
    String _xblockexpression = null;
    {
      int _minus = (-1);
      double _minWidth = it.minWidth(_minus);
      String _plus = ("MinSize : " + Double.valueOf(_minWidth));
      String _plus_1 = (_plus + " x ");
      int _minus_1 = (-1);
      double _minHeight = it.minHeight(_minus_1);
      String _plus_2 = (_plus_1 + Double.valueOf(_minHeight));
      InputOutput.<String>println(_plus_2);
      int _minus_2 = (-1);
      double _minWidth_1 = it.minWidth(_minus_2);
      String _plus_3 = ("PrefSize: " + Double.valueOf(_minWidth_1));
      String _plus_4 = (_plus_3 + " x ");
      int _minus_3 = (-1);
      double _minHeight_1 = it.minHeight(_minus_3);
      String _plus_5 = (_plus_4 + Double.valueOf(_minHeight_1));
      InputOutput.<String>println(_plus_5);
      int _minus_4 = (-1);
      double _minWidth_2 = it.minWidth(_minus_4);
      String _plus_6 = ("MaxSize : " + Double.valueOf(_minWidth_2));
      String _plus_7 = (_plus_6 + " x ");
      int _minus_5 = (-1);
      double _minHeight_2 = it.minHeight(_minus_5);
      String _plus_8 = (_plus_7 + Double.valueOf(_minHeight_2));
      String _println = InputOutput.<String>println(_plus_8);
      _xblockexpression = (_println);
    }
    return _xblockexpression;
  }
}
