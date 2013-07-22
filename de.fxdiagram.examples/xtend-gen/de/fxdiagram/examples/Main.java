package de.fxdiagram.examples;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.lib.simple.NestedDiagramNode;
import de.fxdiagram.lib.simple.SimpleNode;
import javafx.application.Application;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Main extends Application {
  public static void main(final String... args) {
    Application.launch(args);
  }
  
  public void start(final Stage it) {
    it.setTitle("FX Diagram Demo");
    Scene _createScene = this.createScene();
    it.setScene(_createScene);
    it.show();
  }
  
  public Scene createScene() {
    Scene _xblockexpression = null;
    {
      XRoot _xRoot = new XRoot();
      final XRoot root = _xRoot;
      Scene _scene = new Scene(root, 1024, 768);
      final Scene scene = _scene;
      PerspectiveCamera _perspectiveCamera = new PerspectiveCamera();
      scene.setCamera(_perspectiveCamera);
      final XRootDiagram diagram = root.getDiagram();
      diagram.activate();
      NestedDiagramNode _nestedDiagramNode = new NestedDiagramNode("source");
      final Procedure1<NestedDiagramNode> _function = new Procedure1<NestedDiagramNode>() {
          public void apply(final NestedDiagramNode it) {
            it.setLayoutX(280);
            it.setLayoutY(170);
            it.setWidth(80);
            it.setHeight(30);
          }
        };
      final NestedDiagramNode source = ObjectExtensions.<NestedDiagramNode>operator_doubleArrow(_nestedDiagramNode, _function);
      diagram.addNode(source);
      SimpleNode _simpleNode = new SimpleNode("target");
      final Procedure1<SimpleNode> _function_1 = new Procedure1<SimpleNode>() {
          public void apply(final SimpleNode it) {
            it.setLayoutX(280);
            it.setLayoutY(280);
            it.setWidth(80);
            it.setHeight(30);
          }
        };
      final SimpleNode target = ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode, _function_1);
      diagram.addNode(target);
      XConnection _xConnection = new XConnection(source, target);
      final XConnection connection = _xConnection;
      XConnectionLabel _xConnectionLabel = new XConnectionLabel(connection);
      final XConnectionLabel connectionLabel = _xConnectionLabel;
      Text _text = connectionLabel.getText();
      _text.setText("label");
      diagram.addConnection(connection);
      SimpleNode _simpleNode_1 = new SimpleNode("target2");
      final Procedure1<SimpleNode> _function_2 = new Procedure1<SimpleNode>() {
          public void apply(final SimpleNode it) {
            it.setLayoutX(400);
            it.setLayoutY(240);
            it.setWidth(80);
            it.setHeight(30);
          }
        };
      final SimpleNode target2 = ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode_1, _function_2);
      diagram.addNode(target2);
      XConnection _xConnection_1 = new XConnection(source, target2);
      final XConnection connection2 = _xConnection_1;
      XConnectionLabel _xConnectionLabel_1 = new XConnectionLabel(connection2);
      final XConnectionLabel connectionLabel2 = _xConnectionLabel_1;
      Text _text_1 = connectionLabel2.getText();
      _text_1.setText("label2");
      diagram.addConnection(connection2);
      XConnection _xConnection_2 = new XConnection(target, target2);
      final XConnection connection3 = _xConnection_2;
      XConnectionLabel _xConnectionLabel_2 = new XConnectionLabel(connection3);
      final XConnectionLabel connectionLabel3 = _xConnectionLabel_2;
      Text _text_2 = connectionLabel3.getText();
      _text_2.setText("label3");
      diagram.addConnection(connection3);
      _xblockexpression = (scene);
    }
    return _xblockexpression;
  }
}
