package de.fxdiagram.examples;

import com.mongodb.DBObject;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.examples.BrickBreakerNode;
import de.fxdiagram.examples.java.JavaTypeNode;
import de.fxdiagram.examples.lcars.LcarsAccess;
import de.fxdiagram.examples.lcars.LcarsNode;
import de.fxdiagram.examples.neonsign.NeonSignNode;
import de.fxdiagram.lib.media.BrowserNode;
import de.fxdiagram.lib.media.ImageNode;
import de.fxdiagram.lib.media.MovieNode;
import de.fxdiagram.lib.media.RecursiveImageNode;
import de.fxdiagram.lib.simple.NestedDiagramNode;
import de.fxdiagram.lib.simple.SimpleNode;
import java.net.URL;
import java.util.List;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
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
      ObservableList<XNode> _nodes = diagram.getNodes();
      _nodes.add(source);
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
      ObservableList<XNode> _nodes_1 = diagram.getNodes();
      _nodes_1.add(target);
      XConnection _xConnection = new XConnection(source, target);
      final XConnection connection = _xConnection;
      XConnectionLabel _xConnectionLabel = new XConnectionLabel(connection);
      final XConnectionLabel connectionLabel = _xConnectionLabel;
      Text _text = connectionLabel.getText();
      _text.setText("label");
      ObservableList<XConnection> _connections = diagram.getConnections();
      _connections.add(connection);
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
      ObservableList<XNode> _nodes_2 = diagram.getNodes();
      _nodes_2.add(target2);
      XConnection _xConnection_1 = new XConnection(source, target2);
      final XConnection connection2 = _xConnection_1;
      XConnectionLabel _xConnectionLabel_1 = new XConnectionLabel(connection2);
      final XConnectionLabel connectionLabel2 = _xConnectionLabel_1;
      Text _text_1 = connectionLabel2.getText();
      _text_1.setText("label2");
      ObservableList<XConnection> _connections_1 = diagram.getConnections();
      _connections_1.add(connection2);
      XConnection _xConnection_2 = new XConnection(target, target2);
      final XConnection connection3 = _xConnection_2;
      XConnectionLabel _xConnectionLabel_2 = new XConnectionLabel(connection3);
      final XConnectionLabel connectionLabel3 = _xConnectionLabel_2;
      Text _text_2 = connectionLabel3.getText();
      _text_2.setText("label3");
      ObservableList<XConnection> _connections_2 = diagram.getConnections();
      _connections_2.add(connection3);
      ImageNode _imageNode = new ImageNode();
      final Procedure1<ImageNode> _function_3 = new Procedure1<ImageNode>() {
        public void apply(final ImageNode it) {
          Image _image = new Image("media/seltsam.jpg", true);
          it.setImage(_image);
          it.setLayoutX(100);
          it.setLayoutY(100);
          it.setWidth(100);
        }
      };
      final ImageNode image = ObjectExtensions.<ImageNode>operator_doubleArrow(_imageNode, _function_3);
      ObservableList<XNode> _nodes_3 = diagram.getNodes();
      _nodes_3.add(image);
      MovieNode _movieNode = new MovieNode();
      final Procedure1<MovieNode> _function_4 = new Procedure1<MovieNode>() {
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
      final MovieNode movie = ObjectExtensions.<MovieNode>operator_doubleArrow(_movieNode, _function_4);
      ObservableList<XNode> _nodes_4 = diagram.getNodes();
      _nodes_4.add(movie);
      ImageCache _get = ImageCache.get();
      Image _image = _get.getImage("media/seltsam.jpg");
      RecursiveImageNode _recursiveImageNode = new RecursiveImageNode(_image, 10, 0, 0.5);
      final Procedure1<RecursiveImageNode> _function_5 = new Procedure1<RecursiveImageNode>() {
        public void apply(final RecursiveImageNode it) {
          it.setWidth(120);
          it.setHeight(90);
        }
      };
      final RecursiveImageNode recursive = ObjectExtensions.<RecursiveImageNode>operator_doubleArrow(_recursiveImageNode, _function_5);
      ObservableList<XNode> _nodes_5 = diagram.getNodes();
      _nodes_5.add(recursive);
      BrowserNode _browserNode = new BrowserNode();
      final Procedure1<BrowserNode> _function_6 = new Procedure1<BrowserNode>() {
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
      final BrowserNode browser = ObjectExtensions.<BrowserNode>operator_doubleArrow(_browserNode, _function_6);
      ObservableList<XNode> _nodes_6 = diagram.getNodes();
      _nodes_6.add(browser);
      BrickBreakerNode _brickBreakerNode = new BrickBreakerNode();
      final Procedure1<BrickBreakerNode> _function_7 = new Procedure1<BrickBreakerNode>() {
        public void apply(final BrickBreakerNode it) {
          it.setWidth(640);
          it.setHeight(480);
          it.setLayoutX(500);
          it.setLayoutY(100);
        }
      };
      final BrickBreakerNode brickBreakerNode = ObjectExtensions.<BrickBreakerNode>operator_doubleArrow(_brickBreakerNode, _function_7);
      ObservableList<XNode> _nodes_7 = diagram.getNodes();
      _nodes_7.add(brickBreakerNode);
      JavaTypeNode _javaTypeNode = new JavaTypeNode();
      final Procedure1<JavaTypeNode> _function_8 = new Procedure1<JavaTypeNode>() {
        public void apply(final JavaTypeNode it) {
          it.setJavaType(Button.class);
          it.setWidth(160);
          it.setHeight(120);
          it.setLayoutX(500);
          it.setLayoutY(200);
        }
      };
      final JavaTypeNode javaTypeNode = ObjectExtensions.<JavaTypeNode>operator_doubleArrow(_javaTypeNode, _function_8);
      ObservableList<XNode> _nodes_8 = diagram.getNodes();
      _nodes_8.add(javaTypeNode);
      NeonSignNode _neonSignNode = new NeonSignNode();
      final Procedure1<NeonSignNode> _function_9 = new Procedure1<NeonSignNode>() {
        public void apply(final NeonSignNode it) {
          it.setLayoutX(500);
          it.setLayoutY(10);
          it.setWidth(80);
          it.setHeight(30);
        }
      };
      final NeonSignNode neonSignNode = ObjectExtensions.<NeonSignNode>operator_doubleArrow(_neonSignNode, _function_9);
      ObservableList<XNode> _nodes_9 = diagram.getNodes();
      _nodes_9.add(neonSignNode);
      LcarsAccess _get_1 = LcarsAccess.get();
      List<DBObject> _query = _get_1.query("name", "James T. Kirk");
      final DBObject kirk = _query.get(0);
      ObservableList<XNode> _nodes_10 = diagram.getNodes();
      LcarsNode _lcarsNode = new LcarsNode(kirk);
      final Procedure1<LcarsNode> _function_10 = new Procedure1<LcarsNode>() {
        public void apply(final LcarsNode it) {
          it.setWidth(120);
        }
      };
      LcarsNode _doubleArrow = ObjectExtensions.<LcarsNode>operator_doubleArrow(_lcarsNode, _function_10);
      _nodes_10.add(_doubleArrow);
      final Task<Void> _function_11 = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          Void _xblockexpression = null;
          {
            new Layouter();
            _xblockexpression = (null);
          }
          return _xblockexpression;
        }
      };
      final Task<Void> task = _function_11;
      task.run();
      _xblockexpression = (scene);
    }
    return _xblockexpression;
  }
}
