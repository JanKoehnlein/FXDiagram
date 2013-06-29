package de.fxdiagram.lib.example;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.lib.shapes.MovieNode;
import de.fxdiagram.lib.shapes.NestedDiagramNode;
import de.fxdiagram.lib.shapes.RecursiveImageNode;
import de.fxdiagram.lib.shapes.SimpleNode;
import java.net.URL;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Main extends Application {
  public static void main(final String... args) {
    Application.launch(args);
  }
  
  public void start(final Stage primaryStage) {
    primaryStage.setTitle("FX Diagram Demo");
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
      PerspectiveCamera _perspectiveCamera = new PerspectiveCamera();
      scene.setCamera(_perspectiveCamera);
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
      connectionLabel.setText("label");
      diagram.addConnection(connection);
      XNode _xNode = new XNode();
      final Procedure1<XNode> _function_2 = new Procedure1<XNode>() {
          public void apply(final XNode it) {
            ImageView _imageView = new ImageView();
            final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
                public void apply(final ImageView it) {
                  Image _image = new Image("media/seltsam.jpg", true);
                  it.setImage(_image);
                  it.setPreserveRatio(true);
                  it.setFitWidth(100);
                  it.setFitHeight(75);
                }
              };
            ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
            it.setNode(_doubleArrow);
            it.setLayoutX(100);
            it.setLayoutY(100);
          }
        };
      final XNode image = ObjectExtensions.<XNode>operator_doubleArrow(_xNode, _function_2);
      diagram.addNode(image);
      Class<? extends Main> _class = this.getClass();
      ClassLoader _classLoader = _class.getClassLoader();
      URL _resource = _classLoader.getResource("media/ScreenFlow.mp4");
      MovieNode _movieNode = new MovieNode(_resource);
      final Procedure1<MovieNode> _function_3 = new Procedure1<MovieNode>() {
          public void apply(final MovieNode it) {
            it.setWidth(160);
            it.setHeight(90);
            MediaView _view = it.getView();
            Rectangle2D _rectangle2D = new Rectangle2D(0, 60, 640, 360);
            _view.setViewport(_rectangle2D);
            it.setLayoutX(100);
            it.setLayoutY(200);
          }
        };
      final MovieNode movie = ObjectExtensions.<MovieNode>operator_doubleArrow(_movieNode, _function_3);
      diagram.addNode(movie);
      Image _image = new Image("media/seltsam.jpg", true);
      RecursiveImageNode _recursiveImageNode = new RecursiveImageNode(_image, 10, 0, 0.5);
      final Procedure1<RecursiveImageNode> _function_4 = new Procedure1<RecursiveImageNode>() {
          public void apply(final RecursiveImageNode it) {
            it.setWidth(120);
            it.setHeight(90);
          }
        };
      final RecursiveImageNode recursive = ObjectExtensions.<RecursiveImageNode>operator_doubleArrow(_recursiveImageNode, _function_4);
      diagram.addNode(recursive);
      _xblockexpression = (scene);
    }
    return _xblockexpression;
  }
}
