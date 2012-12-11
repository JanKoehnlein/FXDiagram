package de.itemis.javafx.diagram;

import de.itemis.javafx.diagram.MyNode;
import de.itemis.javafx.diagram.XConnection;
import de.itemis.javafx.diagram.XDiagram;
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
    final XDiagram diagram = this.createDiagram();
    Group _rootPane = diagram.getRootPane();
    Scene _scene = new Scene(_rootPane, 400, 400);
    final Scene scene = _scene;
    diagram.activate();
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  
  public XDiagram createDiagram() {
    XDiagram _xblockexpression = null;
    {
      XDiagram _xDiagram = new XDiagram();
      final XDiagram diagram = _xDiagram;
      MyNode _myNode = new MyNode("source");
      final MyNode source = _myNode;
      MyNode _myNode_1 = new MyNode("target");
      final MyNode target = _myNode_1;
      diagram.addNode(source);
      diagram.addNode(target);
      XConnection _xConnection = new XConnection(source, target);
      diagram.addConnection(_xConnection);
      _xblockexpression = (diagram);
    }
    return _xblockexpression;
  }
}
