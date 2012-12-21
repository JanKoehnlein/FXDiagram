package de.itemis.javafx.diagram.example;

import de.itemis.javafx.diagram.XConnection;
import de.itemis.javafx.diagram.XDiagram;
import de.itemis.javafx.diagram.example.MyNode;
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
    Scene _createScene = this.createScene();
    primaryStage.setScene(_createScene);
    primaryStage.show();
  }
  
  public Scene createScene() {
    Scene _xblockexpression = null;
    {
      XDiagram _xDiagram = new XDiagram();
      final XDiagram diagram = _xDiagram;
      Group _rootPane = diagram.getRootPane();
      Scene _scene = new Scene(_rootPane, 400, 400);
      final Scene scene = _scene;
      diagram.activate();
      MyNode _myNode = new MyNode("source");
      final MyNode source = _myNode;
      MyNode _myNode_1 = new MyNode("target");
      final MyNode target = _myNode_1;
      diagram.addNode(source);
      diagram.addNode(target);
      XConnection _xConnection = new XConnection(source, target);
      diagram.addConnection(_xConnection);
      _xblockexpression = (scene);
    }
    return _xblockexpression;
  }
}
