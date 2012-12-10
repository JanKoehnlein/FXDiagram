package de.itemis.javafx.diagram;

import de.itemis.javafx.diagram.Connection;
import de.itemis.javafx.diagram.Diagram;
import de.itemis.javafx.diagram.MyNode;
import de.itemis.javafx.diagram.tools.SelectionTool;
import de.itemis.javafx.diagram.tools.ZoomTool;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SuppressWarnings("all")
public class Main extends Application {
  public static void main(final String... args) {
    Application.launch(args);
  }
  
  public void start(final Stage primaryStage) {
    primaryStage.setTitle("Diagram Demo");
    final Diagram diagram = this.getDiagram();
    Group _rootPane = diagram.getRootPane();
    Scene _scene = new Scene(_rootPane, 300, 250);
    final Scene scene = _scene;
    primaryStage.setScene(scene);
    new ZoomTool(diagram);
    new SelectionTool(diagram);
    primaryStage.show();
  }
  
  public Diagram getDiagram() {
    Diagram _xblockexpression = null;
    {
      Diagram _diagram = new Diagram();
      final Diagram diagram = _diagram;
      MyNode _myNode = new MyNode("source");
      final MyNode source = _myNode;
      MyNode _myNode_1 = new MyNode("target");
      final MyNode target = _myNode_1;
      diagram.addShape(source);
      diagram.addShape(target);
      Connection _connection = new Connection(source, target);
      diagram.addConnection(_connection);
      _xblockexpression = (diagram);
    }
    return _xblockexpression;
  }
}
