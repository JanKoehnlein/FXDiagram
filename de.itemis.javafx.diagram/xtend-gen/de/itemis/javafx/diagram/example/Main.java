package de.itemis.javafx.diagram.example;

import de.itemis.javafx.diagram.XConnection;
import de.itemis.javafx.diagram.XRootDiagram;
import de.itemis.javafx.diagram.example.MyContainerNode;
import javafx.application.Application;
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
      XRootDiagram _xRootDiagram = new XRootDiagram();
      final XRootDiagram diagram = _xRootDiagram;
      Scene _scene = new Scene(diagram, 640, 480);
      final Scene scene = _scene;
      diagram.activate();
      MyContainerNode _myContainerNode = new MyContainerNode("source");
      final MyContainerNode source = _myContainerNode;
      MyContainerNode _myContainerNode_1 = new MyContainerNode("target");
      final MyContainerNode target = _myContainerNode_1;
      XConnection _xConnection = new XConnection(source, target);
      final XConnection connection = _xConnection;
      source.setLayoutX(280);
      source.setLayoutY(170);
      target.setLayoutX(280);
      target.setLayoutY(280);
      diagram.addNode(source);
      diagram.addNode(target);
      diagram.addConnection(connection);
      _xblockexpression = (scene);
    }
    return _xblockexpression;
  }
}
