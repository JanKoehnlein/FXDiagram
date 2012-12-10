package de.itemis.javafx.diagram;

import de.itemis.javafx.diagram.Connection;
import de.itemis.javafx.diagram.Diagram;
import de.itemis.javafx.diagram.ShapeContainer;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Main extends Application {
  public static void main(final String... args) {
    Application.launch(args);
  }
  
  public void start(final Stage primaryStage) {
    primaryStage.setTitle("Hello World!");
    final ShapeContainer source = this.newShapeNode("source");
    final ShapeContainer target = this.newShapeNode("target");
    Diagram _diagram = new Diagram();
    final Diagram diagram = _diagram;
    diagram.addShape(source);
    diagram.addShape(target);
    Connection _connection = new Connection(source, target);
    diagram.addConnection(_connection);
    Group _rootPane = diagram.getRootPane();
    Scene _scene = new Scene(_rootPane, 300, 250);
    primaryStage.setScene(_scene);
    primaryStage.show();
  }
  
  protected ShapeContainer newShapeNode(final String name) {
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
        public void apply(final StackPane it) {
          ObservableList<Node> _children = it.getChildren();
          Rectangle _rectangle = new Rectangle();
          final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
              public void apply(final Rectangle it) {
                it.setWidth(100);
                it.setHeight(30);
                LinearGradient _createFill = Main.this.createFill();
                it.setFill(_createFill);
                it.setStroke(Color.BLACK);
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
    ShapeContainer _shapeContainer = new ShapeContainer(_doubleArrow);
    return _shapeContainer;
  }
  
  public LinearGradient createFill() {
    LinearGradient _xblockexpression = null;
    {
      Stop _stop = new Stop(0, Color.LIGHTGRAY);
      Stop _stop_1 = new Stop(1, Color.DARKGRAY);
      final ArrayList<Stop> stops = CollectionLiterals.<Stop>newArrayList(_stop, _stop_1);
      LinearGradient _linearGradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
      _xblockexpression = (_linearGradient);
    }
    return _xblockexpression;
  }
}
