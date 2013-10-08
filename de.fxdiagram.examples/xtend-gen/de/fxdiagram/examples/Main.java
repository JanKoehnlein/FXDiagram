package de.fxdiagram.examples;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionKind;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.examples.BrickBreakerNode;
import de.fxdiagram.examples.java.JavaTypeNode;
import de.fxdiagram.examples.lcars.LcarsDiagram;
import de.fxdiagram.examples.login.LoginNode;
import de.fxdiagram.examples.neonsign.NeonSignNode;
import de.fxdiagram.lib.media.BrowserNode;
import de.fxdiagram.lib.media.ImageNode;
import de.fxdiagram.lib.media.MovieNode;
import de.fxdiagram.lib.media.RecursiveImageNode;
import de.fxdiagram.lib.simple.LevelOfDetailDiagramNode;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import de.fxdiagram.lib.simple.SimpleNode;
import java.net.URL;
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
  
  private int node_nr = 0;
  
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
      root.activate();
      XDiagram _xDiagram = new XDiagram();
      final XDiagram diagram = _xDiagram;
      root.setDiagram(diagram);
      final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
        public void apply(final XDiagram it) {
          LevelOfDetailDiagramNode _levelOfDetailDiagramNode = new LevelOfDetailDiagramNode("LOD diagram");
          final Procedure1<LevelOfDetailDiagramNode> _function = new Procedure1<LevelOfDetailDiagramNode>() {
            public void apply(final LevelOfDetailDiagramNode it) {
              XDiagram _createDummyDiagram = Main.this.createDummyDiagram();
              it.setInnerDiagram(_createDummyDiagram);
              it.setLayoutX(280);
              it.setLayoutY(170);
              it.setWidth(80);
              it.setHeight(30);
            }
          };
          final LevelOfDetailDiagramNode source = ObjectExtensions.<LevelOfDetailDiagramNode>operator_doubleArrow(_levelOfDetailDiagramNode, _function);
          ObservableList<XNode> _nodes = it.getNodes();
          _nodes.add(source);
          SimpleNode _simpleNode = new SimpleNode("Simple Node");
          final Procedure1<SimpleNode> _function_1 = new Procedure1<SimpleNode>() {
            public void apply(final SimpleNode it) {
              it.setLayoutX(280);
              it.setLayoutY(280);
              it.setWidth(80);
              it.setHeight(30);
            }
          };
          final SimpleNode target = ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode, _function_1);
          ObservableList<XNode> _nodes_1 = it.getNodes();
          _nodes_1.add(target);
          OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode("Openable");
          final Procedure1<OpenableDiagramNode> _function_2 = new Procedure1<OpenableDiagramNode>() {
            public void apply(final OpenableDiagramNode it) {
              XDiagram _createDummyDiagram = Main.this.createDummyDiagram();
              it.setInnerDiagram(_createDummyDiagram);
              it.setLayoutX(400);
              it.setLayoutY(240);
              it.setWidth(80);
              it.setHeight(30);
            }
          };
          final OpenableDiagramNode target2 = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode, _function_2);
          ObservableList<XNode> _nodes_2 = it.getNodes();
          _nodes_2.add(target2);
          ObservableList<XConnection> _connections = it.getConnections();
          XConnection _xConnection = new XConnection(source, target);
          final Procedure1<XConnection> _function_3 = new Procedure1<XConnection>() {
            public void apply(final XConnection it) {
              it.setKind(XConnectionKind.QUAD_CURVE);
              XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
              final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
                public void apply(final XConnectionLabel it) {
                  Text _text = it.getText();
                  _text.setText("quadratic");
                }
              };
              ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function);
            }
          };
          XConnection _doubleArrow = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function_3);
          _connections.add(_doubleArrow);
          ObservableList<XConnection> _connections_1 = it.getConnections();
          XConnection _xConnection_1 = new XConnection(source, target2);
          final Procedure1<XConnection> _function_4 = new Procedure1<XConnection>() {
            public void apply(final XConnection it) {
              it.setKind(XConnectionKind.CUBIC_CURVE);
              XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
              final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
                public void apply(final XConnectionLabel it) {
                  Text _text = it.getText();
                  _text.setText("cubic");
                }
              };
              ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function);
            }
          };
          XConnection _doubleArrow_1 = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection_1, _function_4);
          _connections_1.add(_doubleArrow_1);
          ObservableList<XConnection> _connections_2 = it.getConnections();
          XConnection _xConnection_2 = new XConnection(target, target2);
          final Procedure1<XConnection> _function_5 = new Procedure1<XConnection>() {
            public void apply(final XConnection it) {
              XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
              final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
                public void apply(final XConnectionLabel it) {
                  Text _text = it.getText();
                  _text.setText("polyline");
                }
              };
              ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function);
            }
          };
          XConnection _doubleArrow_2 = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection_2, _function_5);
          _connections_2.add(_doubleArrow_2);
          ObservableList<XNode> _nodes_3 = it.getNodes();
          ImageNode _imageNode = new ImageNode();
          final Procedure1<ImageNode> _function_6 = new Procedure1<ImageNode>() {
            public void apply(final ImageNode it) {
              Image _image = new Image("media/seltsam.jpg", true);
              it.setImage(_image);
              it.setLayoutX(100);
              it.setLayoutY(100);
              it.setWidth(100);
            }
          };
          ImageNode _doubleArrow_3 = ObjectExtensions.<ImageNode>operator_doubleArrow(_imageNode, _function_6);
          _nodes_3.add(_doubleArrow_3);
          ObservableList<XNode> _nodes_4 = it.getNodes();
          MovieNode _movieNode = new MovieNode("Movie");
          final Procedure1<MovieNode> _function_7 = new Procedure1<MovieNode>() {
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
          MovieNode _doubleArrow_4 = ObjectExtensions.<MovieNode>operator_doubleArrow(_movieNode, _function_7);
          _nodes_4.add(_doubleArrow_4);
          ObservableList<XNode> _nodes_5 = it.getNodes();
          ImageCache _get = ImageCache.get();
          Image _image = _get.getImage("media/seltsam.jpg");
          RecursiveImageNode _recursiveImageNode = new RecursiveImageNode(_image, 10, 0, 0.3);
          final Procedure1<RecursiveImageNode> _function_8 = new Procedure1<RecursiveImageNode>() {
            public void apply(final RecursiveImageNode it) {
              it.setWidth(120);
              it.setHeight(90);
            }
          };
          RecursiveImageNode _doubleArrow_5 = ObjectExtensions.<RecursiveImageNode>operator_doubleArrow(_recursiveImageNode, _function_8);
          _nodes_5.add(_doubleArrow_5);
          ObservableList<XNode> _nodes_6 = it.getNodes();
          BrowserNode _browserNode = new BrowserNode();
          final Procedure1<BrowserNode> _function_9 = new Procedure1<BrowserNode>() {
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
          BrowserNode _doubleArrow_6 = ObjectExtensions.<BrowserNode>operator_doubleArrow(_browserNode, _function_9);
          _nodes_6.add(_doubleArrow_6);
          ObservableList<XNode> _nodes_7 = it.getNodes();
          BrickBreakerNode _brickBreakerNode = new BrickBreakerNode();
          final Procedure1<BrickBreakerNode> _function_10 = new Procedure1<BrickBreakerNode>() {
            public void apply(final BrickBreakerNode it) {
              it.setWidth(640);
              it.setHeight(480);
              it.setLayoutX(500);
              it.setLayoutY(100);
            }
          };
          BrickBreakerNode _doubleArrow_7 = ObjectExtensions.<BrickBreakerNode>operator_doubleArrow(_brickBreakerNode, _function_10);
          _nodes_7.add(_doubleArrow_7);
          ObservableList<XNode> _nodes_8 = it.getNodes();
          JavaTypeNode _javaTypeNode = new JavaTypeNode();
          final Procedure1<JavaTypeNode> _function_11 = new Procedure1<JavaTypeNode>() {
            public void apply(final JavaTypeNode it) {
              it.setJavaType(Button.class);
              it.setWidth(160);
              it.setHeight(120);
              it.setLayoutX(500);
              it.setLayoutY(200);
            }
          };
          JavaTypeNode _doubleArrow_8 = ObjectExtensions.<JavaTypeNode>operator_doubleArrow(_javaTypeNode, _function_11);
          _nodes_8.add(_doubleArrow_8);
          ObservableList<XNode> _nodes_9 = it.getNodes();
          NeonSignNode _neonSignNode = new NeonSignNode();
          final Procedure1<NeonSignNode> _function_12 = new Procedure1<NeonSignNode>() {
            public void apply(final NeonSignNode it) {
              it.setLayoutX(500);
              it.setLayoutY(10);
              it.setWidth(80);
              it.setHeight(30);
            }
          };
          NeonSignNode _doubleArrow_9 = ObjectExtensions.<NeonSignNode>operator_doubleArrow(_neonSignNode, _function_12);
          _nodes_9.add(_doubleArrow_9);
          ObservableList<XNode> _nodes_10 = it.getNodes();
          OpenableDiagramNode _openableDiagramNode_1 = new OpenableDiagramNode("LCARS");
          final Procedure1<OpenableDiagramNode> _function_13 = new Procedure1<OpenableDiagramNode>() {
            public void apply(final OpenableDiagramNode it) {
              LcarsDiagram _lcarsDiagram = new LcarsDiagram();
              it.setInnerDiagram(_lcarsDiagram);
              it.setLayoutX(300);
              it.setLayoutY(300);
              it.setWidth(80);
              it.setHeight(30);
            }
          };
          OpenableDiagramNode _doubleArrow_10 = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode_1, _function_13);
          _nodes_10.add(_doubleArrow_10);
          ObservableList<XNode> _nodes_11 = it.getNodes();
          LoginNode _loginNode = new LoginNode();
          final Procedure1<LoginNode> _function_14 = new Procedure1<LoginNode>() {
            public void apply(final LoginNode it) {
              it.setLayoutX(200);
              it.setLayoutY(200);
              it.setWidth(80);
              it.setHeight(30);
            }
          };
          LoginNode _doubleArrow_11 = ObjectExtensions.<LoginNode>operator_doubleArrow(_loginNode, _function_14);
          _nodes_11.add(_doubleArrow_11);
        }
      };
      ObjectExtensions.<XDiagram>operator_doubleArrow(diagram, _function);
      this.warmUpLayouter();
      root.centerDiagram();
      _xblockexpression = (scene);
    }
    return _xblockexpression;
  }
  
  protected void warmUpLayouter() {
    final Task<Void> _function = new Task<Void>() {
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
    final Task<Void> task = _function;
    task.run();
  }
  
  protected XDiagram createDummyDiagram() {
    XDiagram _xDiagram = new XDiagram();
    final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
      public void apply(final XDiagram it) {
        final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
          public void apply(final XDiagram it) {
            ObservableList<XNode> _nodes = it.getNodes();
            String _plus = ("Inner " + Integer.valueOf(Main.this.node_nr));
            SimpleNode _simpleNode = new SimpleNode(_plus);
            final Procedure1<SimpleNode> _function = new Procedure1<SimpleNode>() {
              public void apply(final SimpleNode it) {
                it.relocate(0, 0);
              }
            };
            SimpleNode _doubleArrow = ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode, _function);
            _nodes.add(_doubleArrow);
            ObservableList<XNode> _nodes_1 = it.getNodes();
            String _plus_1 = ("Inner " + Integer.valueOf(Main.this.node_nr));
            String _plus_2 = (_plus_1 + Integer.valueOf(1));
            SimpleNode _simpleNode_1 = new SimpleNode(_plus_2);
            final Procedure1<SimpleNode> _function_1 = new Procedure1<SimpleNode>() {
              public void apply(final SimpleNode it) {
                it.relocate(100, 100);
              }
            };
            SimpleNode _doubleArrow_1 = ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode_1, _function_1);
            _nodes_1.add(_doubleArrow_1);
            ObservableList<XNode> _nodes_2 = it.getNodes();
            String _plus_3 = ("Nested " + Integer.valueOf(Main.this.node_nr));
            String _plus_4 = (_plus_3 + Integer.valueOf(2));
            LevelOfDetailDiagramNode _levelOfDetailDiagramNode = new LevelOfDetailDiagramNode(_plus_4);
            final Procedure1<LevelOfDetailDiagramNode> _function_2 = new Procedure1<LevelOfDetailDiagramNode>() {
              public void apply(final LevelOfDetailDiagramNode it) {
                XDiagram _createDummyDiagram = Main.this.createDummyDiagram();
                it.setInnerDiagram(_createDummyDiagram);
                it.relocate(50, 50);
              }
            };
            LevelOfDetailDiagramNode _doubleArrow_2 = ObjectExtensions.<LevelOfDetailDiagramNode>operator_doubleArrow(_levelOfDetailDiagramNode, _function_2);
            _nodes_2.add(_doubleArrow_2);
          }
        };
        it.setContentsInitializer(_function);
        int _plus = (Main.this.node_nr + 3);
        Main.this.node_nr = _plus;
      }
    };
    XDiagram _doubleArrow = ObjectExtensions.<XDiagram>operator_doubleArrow(_xDiagram, _function);
    return _doubleArrow;
  }
}
