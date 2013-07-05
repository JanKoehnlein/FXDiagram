package de.fxdiagram.lib.example;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.lib.shapes.BrickBreakerNode;
import de.fxdiagram.lib.shapes.BrowserNode;
import de.fxdiagram.lib.shapes.ImageNode;
import de.fxdiagram.lib.shapes.JavaTypeNode;
import de.fxdiagram.lib.shapes.MovieNode;
import de.fxdiagram.lib.shapes.NestedDiagramNode;
import de.fxdiagram.lib.shapes.RecursiveImageNode;
import de.fxdiagram.lib.shapes.SimpleNode;
import java.net.URL;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import org.eclipse.xtext.xbase.lib.Exceptions;
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
      XRootDiagram _xRootDiagram = new XRootDiagram();
      final XRootDiagram diagram = _xRootDiagram;
      Scene _scene = new Scene(diagram, 1024, 768);
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
      ImageNode _imageNode = new ImageNode();
      final Procedure1<ImageNode> _function_2 = new Procedure1<ImageNode>() {
          public void apply(final ImageNode it) {
            Image _image = new Image("media/seltsam.jpg", true);
            it.setImage(_image);
            it.setLayoutX(100);
            it.setLayoutY(100);
            it.setWidth(100);
          }
        };
      final ImageNode image = ObjectExtensions.<ImageNode>operator_doubleArrow(_imageNode, _function_2);
      diagram.addNode(image);
      MovieNode _movieNode = new MovieNode();
      final Procedure1<MovieNode> _function_3 = new Procedure1<MovieNode>() {
          public void apply(final MovieNode it) {
            Class<? extends Main> _class = Main.this.getClass();
            ClassLoader _classLoader = _class.getClassLoader();
            URL _resource = _classLoader.getResource("media/ScreenFlow.mp4");
            it.setMovieUrl(_resource);
            it.setWidth(640);
            it.setHeight(360);
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
      BrowserNode _browserNode = new BrowserNode();
      final Procedure1<BrowserNode> _function_5 = new Procedure1<BrowserNode>() {
          public void apply(final BrowserNode it) {
            try {
              it.setWidth(120);
              it.setHeight(160);
              it.setLayoutX(100);
              it.setLayoutY(500);
              URL _uRL = new URL("http://koehnlein.blogspot.de/");
              it.setPageUrl(_uRL);
            } catch (Throwable _e) {
              throw Exceptions.sneakyThrow(_e);
            }
          }
        };
      final BrowserNode browser = ObjectExtensions.<BrowserNode>operator_doubleArrow(_browserNode, _function_5);
      diagram.addNode(browser);
      BrickBreakerNode _brickBreakerNode = new BrickBreakerNode();
      final Procedure1<BrickBreakerNode> _function_6 = new Procedure1<BrickBreakerNode>() {
          public void apply(final BrickBreakerNode it) {
            it.setWidth(640);
            it.setHeight(480);
            it.setLayoutX(500);
            it.setLayoutY(100);
          }
        };
      final BrickBreakerNode brickBreakerNode = ObjectExtensions.<BrickBreakerNode>operator_doubleArrow(_brickBreakerNode, _function_6);
      diagram.addNode(brickBreakerNode);
      JavaTypeNode _javaTypeNode = new JavaTypeNode();
      final Procedure1<JavaTypeNode> _function_7 = new Procedure1<JavaTypeNode>() {
          public void apply(final JavaTypeNode it) {
            it.setJavaType(Button.class);
            it.setWidth(160);
            it.setHeight(120);
            it.setLayoutX(500);
            it.setLayoutY(200);
          }
        };
      final JavaTypeNode javaTypeNode = ObjectExtensions.<JavaTypeNode>operator_doubleArrow(_javaTypeNode, _function_7);
      diagram.addNode(javaTypeNode);
      _xblockexpression = (scene);
    }
    return _xblockexpression;
  }
}
