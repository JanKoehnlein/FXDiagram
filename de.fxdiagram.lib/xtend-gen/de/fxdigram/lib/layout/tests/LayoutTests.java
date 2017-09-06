package de.fxdigram.lib.layout.tests;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.lib.simple.DiagramScaler;
import de.fxdiagram.lib.simple.SimpleNode;
import java.util.function.Consumer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LayoutTests extends Application {
  private XDiagram nestedDiagram;
  
  private XDiagram diagram;
  
  public static void main(final String... args) {
    Application.launch();
  }
  
  @Override
  public void start(final Stage stage) throws Exception {
    XDiagram _xDiagram = new XDiagram();
    final Procedure1<XDiagram> _function = (XDiagram it) -> {
      final Procedure1<XDiagram> _function_1 = (XDiagram it_1) -> {
        ObservableList<XNode> _nodes = it_1.getNodes();
        SimpleNode _simpleNode = new SimpleNode("Foo");
        final Procedure1<SimpleNode> _function_2 = (SimpleNode it_2) -> {
          it_2.relocate((-100), (-100));
          it_2.setWidth(65);
          it_2.setHeight(40);
        };
        SimpleNode _doubleArrow = ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode, _function_2);
        _nodes.add(_doubleArrow);
        ObservableList<XNode> _nodes_1 = it_1.getNodes();
        SimpleNode _simpleNode_1 = new SimpleNode("Bar");
        final Procedure1<SimpleNode> _function_3 = (SimpleNode it_2) -> {
          it_2.relocate(100, 100);
        };
        SimpleNode _doubleArrow_1 = ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode_1, _function_3);
        _nodes_1.add(_doubleArrow_1);
      };
      it.setContentsInitializer(_function_1);
      new DiagramScaler(this.nestedDiagram);
    };
    XDiagram _doubleArrow = ObjectExtensions.<XDiagram>operator_doubleArrow(_xDiagram, _function);
    this.nestedDiagram = _doubleArrow;
    this.diagram = new XRoot().getDiagram();
    Scene _scene = new Scene(this.diagram, 1024, 768);
    stage.setScene(_scene);
    final StackPane rectangleBorderPane = new StackPane();
    final Procedure1<XDiagram> _function_1 = (XDiagram it) -> {
      it.activate();
      ObservableList<XNode> _nodes = it.getNodes();
      XNode _xNode = new XNode("");
      final Procedure1<XNode> _function_2 = (XNode it_1) -> {
      };
      XNode _doubleArrow_1 = ObjectExtensions.<XNode>operator_doubleArrow(_xNode, _function_2);
      _nodes.add(_doubleArrow_1);
      this.nestedDiagram.activate();
    };
    ObjectExtensions.<XDiagram>operator_doubleArrow(
      this.diagram, _function_1);
    stage.show();
    final Consumer<XNode> _function_2 = (XNode it) -> {
      this.printLayoutGeometry(it);
    };
    this.nestedDiagram.getNodes().forEach(_function_2);
    this.printLayoutGeometry(this.nestedDiagram);
    this.printLayoutGeometry(rectangleBorderPane);
    final Consumer<XNode> _function_3 = (XNode it) -> {
      this.printSizes(it);
    };
    this.nestedDiagram.getNodes().forEach(_function_3);
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
    return InputOutput.<String>println(_plus_5);
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
      _xblockexpression = InputOutput.<String>println(_plus_8);
    }
    return _xblockexpression;
  }
}
