package de.itemis.javafx.diagram.example;

import de.itemis.javafx.diagram.XConnection;
import de.itemis.javafx.diagram.XRootDiagram;
import de.itemis.javafx.diagram.example.MyContainerNode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

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
      final Procedure1<MyContainerNode> _function = new Procedure1<MyContainerNode>() {
          public void apply(final MyContainerNode it) {
            it.setLayoutX(280);
            it.setLayoutY(170);
          }
        };
      final MyContainerNode source = ObjectExtensions.<MyContainerNode>operator_doubleArrow(_myContainerNode, _function);
      MyContainerNode _myContainerNode_1 = new MyContainerNode("target");
      final Procedure1<MyContainerNode> _function_1 = new Procedure1<MyContainerNode>() {
          public void apply(final MyContainerNode it) {
            it.setLayoutX(280);
            it.setLayoutY(280);
          }
        };
      final MyContainerNode target = ObjectExtensions.<MyContainerNode>operator_doubleArrow(_myContainerNode_1, _function_1);
      XConnection _xConnection = new XConnection(source, target);
      final XConnection connection = _xConnection;
      diagram.addNode(source);
      diagram.addNode(target);
      diagram.addConnection(connection);
      _xblockexpression = (scene);
    }
    return _xblockexpression;
  }
}
