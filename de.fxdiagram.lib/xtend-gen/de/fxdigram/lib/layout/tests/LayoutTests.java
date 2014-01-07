package de.fxdigram.lib.layout.tests;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.lib.simple.DiagramScaler;
import de.fxdiagram.lib.simple.SimpleNode;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
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
  private XDiagram nestedDiagram;
  
  private XDiagram diagram;
  
  public static void main(final String... args) {
    Application.launch();
  }
  
  public void start(final Stage stage) throws Exception {
    XDiagram _xDiagram = new XDiagram();
    final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
      public void apply(final XDiagram it) {
        final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
          public void apply(final XDiagram it) {
            ObservableList<XNode> _nodes = it.getNodes();
            SimpleNode _simpleNode = new SimpleNode("Foo");
            final Procedure1<SimpleNode> _function = new Procedure1<SimpleNode>() {
              public void apply(final SimpleNode it) {
                it.relocate((-100), (-100));
                it.setWidth(65);
                it.setHeight(40);
              }
            };
            SimpleNode _doubleArrow = ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode, _function);
            _nodes.add(_doubleArrow);
            ObservableList<XNode> _nodes_1 = it.getNodes();
            SimpleNode _simpleNode_1 = new SimpleNode("Bar");
            final Procedure1<SimpleNode> _function_1 = new Procedure1<SimpleNode>() {
              public void apply(final SimpleNode it) {
                it.relocate(100, 100);
              }
            };
            SimpleNode _doubleArrow_1 = ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode_1, _function_1);
            _nodes_1.add(_doubleArrow_1);
          }
        };
        it.setContentsInitializer(_function);
        new DiagramScaler(LayoutTests.this.nestedDiagram);
      }
    };
    XDiagram _doubleArrow = ObjectExtensions.<XDiagram>operator_doubleArrow(_xDiagram, _function);
    this.nestedDiagram = _doubleArrow;
    XRoot _xRoot = new XRoot();
    XDiagram _diagram = _xRoot.getDiagram();
    this.diagram = _diagram;
    Scene _scene = new Scene(this.diagram, 1024, 768);
    stage.setScene(_scene);
    StackPane _stackPane = new StackPane();
    final StackPane rectangleBorderPane = _stackPane;
    final Procedure1<XDiagram> _function_1 = new Procedure1<XDiagram>() {
      public void apply(final XDiagram it) {
        it.activate();
        ObservableList<XNode> _nodes = it.getNodes();
        XNode _xNode = new XNode("");
        final Procedure1<XNode> _function = new Procedure1<XNode>() {
          public void apply(final XNode it) {
          }
        };
        XNode _doubleArrow = ObjectExtensions.<XNode>operator_doubleArrow(_xNode, _function);
        _nodes.add(_doubleArrow);
        LayoutTests.this.nestedDiagram.activate();
      }
    };
    ObjectExtensions.<XDiagram>operator_doubleArrow(
      this.diagram, _function_1);
    stage.show();
    ObservableList<XNode> _nodes = this.nestedDiagram.getNodes();
    final Procedure1<XNode> _function_2 = new Procedure1<XNode>() {
      public void apply(final XNode it) {
        LayoutTests.this.printLayoutGeometry(it);
      }
    };
    IterableExtensions.<XNode>forEach(_nodes, _function_2);
    this.printLayoutGeometry(this.nestedDiagram);
    this.printLayoutGeometry(rectangleBorderPane);
    ObservableList<XNode> _nodes_1 = this.nestedDiagram.getNodes();
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
      double _minWidth = it.minWidth((-1));
      String _plus = ("MinSize : " + Double.valueOf(_minWidth));
      String _plus_1 = (_plus + " x ");
      double _minHeight = it.minHeight((-1));
      String _plus_2 = (_plus_1 + Double.valueOf(_minHeight));
      InputOutput.<String>println(_plus_2);
      double _minWidth_1 = it.minWidth((-1));
      String _plus_3 = ("PrefSize: " + Double.valueOf(_minWidth_1));
      String _plus_4 = (_plus_3 + " x ");
      double _minHeight_1 = it.minHeight((-1));
      String _plus_5 = (_plus_4 + Double.valueOf(_minHeight_1));
      InputOutput.<String>println(_plus_5);
      double _minWidth_2 = it.minWidth((-1));
      String _plus_6 = ("MaxSize : " + Double.valueOf(_minWidth_2));
      String _plus_7 = (_plus_6 + " x ");
      double _minHeight_2 = it.minHeight((-1));
      String _plus_8 = (_plus_7 + Double.valueOf(_minHeight_2));
      String _println = InputOutput.<String>println(_plus_8);
      _xblockexpression = (_println);
    }
    return _xblockexpression;
  }
}
