package de.fxdiagram.examples;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionKind;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.UriExtensions;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.core.tools.AbstractChooser;
import de.fxdiagram.examples.BrickBreakerNode;
import de.fxdiagram.examples.ecore.EClassNode;
import de.fxdiagram.examples.java.JavaTypeNode;
import de.fxdiagram.examples.lcars.LcarsDiagram;
import de.fxdiagram.examples.login.LoginNode;
import de.fxdiagram.examples.neonsign.NeonSignNode;
import de.fxdiagram.examples.slides.IntroductionSlideDeck;
import de.fxdiagram.examples.slides.SummarySlideDeck;
import de.fxdiagram.lib.media.BrowserNode;
import de.fxdiagram.lib.media.ImageNode;
import de.fxdiagram.lib.media.MovieNode;
import de.fxdiagram.lib.media.RecursiveImageNode;
import de.fxdiagram.lib.simple.AddRapidButtonBehavior;
import de.fxdiagram.lib.simple.LevelOfDetailDiagramNode;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import de.fxdiagram.lib.simple.SimpleNode;
import java.net.URL;
import java.util.List;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.eclipse.emf.ecore.EcorePackage.Literals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class Demo extends Application {
  public static void main(final String... args) {
    Application.launch(args);
  }
  
  public void start(final Stage it) {
    it.setTitle("FX Diagram Demo");
    Scene _createScene = this.createScene();
    it.setScene(_createScene);
    it.setFullScreen(true);
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
          ObservableList<XNode> _nodes = it.getNodes();
          IntroductionSlideDeck _introductionSlideDeck = new IntroductionSlideDeck();
          _nodes.add(_introductionSlideDeck);
          ObservableList<XNode> _nodes_1 = it.getNodes();
          OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode("Basic");
          final Procedure1<OpenableDiagramNode> _function = new Procedure1<OpenableDiagramNode>() {
            public void apply(final OpenableDiagramNode it) {
              XDiagram _createBasicDiagram = Demo.this.createBasicDiagram("");
              it.setInnerDiagram(_createBasicDiagram);
            }
          };
          OpenableDiagramNode _doubleArrow = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode, _function);
          _nodes_1.add(_doubleArrow);
          ObservableList<XNode> _nodes_2 = it.getNodes();
          OpenableDiagramNode _openableDiagramNode_1 = new OpenableDiagramNode("JavaFX");
          final Procedure1<OpenableDiagramNode> _function_1 = new Procedure1<OpenableDiagramNode>() {
            public void apply(final OpenableDiagramNode it) {
              XDiagram _xDiagram = new XDiagram();
              final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
                public void apply(final XDiagram it) {
                  final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
                    public void apply(final XDiagram it) {
                      ObservableList<XNode> _nodes = it.getNodes();
                      LoginNode _newLoginNode = Demo.this.newLoginNode();
                      _nodes.add(_newLoginNode);
                      ObservableList<XNode> _nodes_1 = it.getNodes();
                      RecursiveImageNode _newRecursiveImageNode = Demo.this.newRecursiveImageNode();
                      _nodes_1.add(_newRecursiveImageNode);
                      ObservableList<XNode> _nodes_2 = it.getNodes();
                      ImageNode _newImageNode = Demo.this.newImageNode();
                      _nodes_2.add(_newImageNode);
                      ObservableList<XNode> _nodes_3 = it.getNodes();
                      MovieNode _newMovieNode = Demo.this.newMovieNode();
                      _nodes_3.add(_newMovieNode);
                      ObservableList<XNode> _nodes_4 = it.getNodes();
                      BrowserNode _newBrowserNode = Demo.this.newBrowserNode();
                      _nodes_4.add(_newBrowserNode);
                      ObservableList<XNode> _nodes_5 = it.getNodes();
                      BrickBreakerNode _newBrickBreakerNode = Demo.this.newBrickBreakerNode();
                      _nodes_5.add(_newBrickBreakerNode);
                    }
                  };
                  it.setContentsInitializer(_function);
                }
              };
              XDiagram _doubleArrow = ObjectExtensions.<XDiagram>operator_doubleArrow(_xDiagram, _function);
              it.setInnerDiagram(_doubleArrow);
            }
          };
          OpenableDiagramNode _doubleArrow_1 = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode_1, _function_1);
          _nodes_2.add(_doubleArrow_1);
          ObservableList<XNode> _nodes_3 = it.getNodes();
          NeonSignNode _newNeonSignNode = Demo.this.newNeonSignNode();
          OpenableDiagramNode _openableDiagram = Demo.this.openableDiagram("Xtend", _newNeonSignNode);
          _nodes_3.add(_openableDiagram);
          ObservableList<XNode> _nodes_4 = it.getNodes();
          JavaTypeNode _newJavaTypeNode = Demo.this.newJavaTypeNode();
          OpenableDiagramNode _openableDiagram_1 = Demo.this.openableDiagram("JavaFX Explorer", _newJavaTypeNode);
          _nodes_4.add(_openableDiagram_1);
          ObservableList<XNode> _nodes_5 = it.getNodes();
          EClassNode _newEClassNode = Demo.this.newEClassNode();
          OpenableDiagramNode _openableDiagram_2 = Demo.this.openableDiagram("Ecore Explorer", _newEClassNode);
          _nodes_5.add(_openableDiagram_2);
          ObservableList<XNode> _nodes_6 = it.getNodes();
          OpenableDiagramNode _newLcarsDiagramNode = Demo.this.newLcarsDiagramNode();
          _nodes_6.add(_newLcarsDiagramNode);
          ObservableList<XNode> _nodes_7 = it.getNodes();
          SimpleNode _simpleNode = new SimpleNode("Eclipse");
          _nodes_7.add(_simpleNode);
          ObservableList<XNode> _nodes_8 = it.getNodes();
          SummarySlideDeck _summarySlideDeck = new SummarySlideDeck();
          _nodes_8.add(_summarySlideDeck);
          double _width = scene.getWidth();
          ObservableList<XNode> _nodes_9 = it.getNodes();
          int _size = _nodes_9.size();
          int _plus = (_size + 2);
          final double deltaX = (_width / _plus);
          double _height = scene.getHeight();
          ObservableList<XNode> _nodes_10 = it.getNodes();
          int _size_1 = _nodes_10.size();
          int _plus_1 = (_size_1 + 2);
          final double deltaY = (_height / _plus_1);
          ObservableList<XNode> _nodes_11 = it.getNodes();
          final Procedure2<XNode,Integer> _function_2 = new Procedure2<XNode,Integer>() {
            public void apply(final XNode node, final Integer i) {
              double _multiply = ((i).intValue() * deltaX);
              Bounds _layoutBounds = node.getLayoutBounds();
              double _width = _layoutBounds.getWidth();
              double _divide = (_width / 2);
              double _minus = (_multiply - _divide);
              node.setLayoutX(_minus);
              double _multiply_1 = ((i).intValue() * deltaY);
              Bounds _layoutBounds_1 = node.getLayoutBounds();
              double _height = _layoutBounds_1.getHeight();
              double _divide_1 = (_height / 2);
              double _minus_1 = (_multiply_1 - _divide_1);
              node.setLayoutY(_minus_1);
            }
          };
          IterableExtensions.<XNode>forEach(_nodes_11, _function_2);
          ObservableList<XNode> _nodes_12 = it.getNodes();
          ObservableList<XNode> _nodes_13 = it.getNodes();
          int _size_2 = _nodes_13.size();
          int _minus = (_size_2 - 1);
          List<XNode> _subList = _nodes_12.subList(0, _minus);
          final Procedure2<XNode,Integer> _function_3 = new Procedure2<XNode,Integer>() {
            public void apply(final XNode node, final Integer i) {
              ObservableList<XConnection> _connections = it.getConnections();
              ObservableList<XNode> _nodes = it.getNodes();
              int _plus = ((i).intValue() + 1);
              XNode _get = _nodes.get(_plus);
              XConnection _xConnection = new XConnection(node, _get);
              _connections.add(_xConnection);
            }
          };
          IterableExtensions.<XNode>forEach(_subList, _function_3);
        }
      };
      ObjectExtensions.<XDiagram>operator_doubleArrow(diagram, _function);
      this.warmUpLayouter();
      root.centerDiagram();
      _xblockexpression = (scene);
    }
    return _xblockexpression;
  }
  
  public OpenableDiagramNode newLcarsDiagramNode() {
    OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode("LCARS");
    final Procedure1<OpenableDiagramNode> _function = new Procedure1<OpenableDiagramNode>() {
      public void apply(final OpenableDiagramNode it) {
        LcarsDiagram _lcarsDiagram = new LcarsDiagram();
        it.setInnerDiagram(_lcarsDiagram);
      }
    };
    OpenableDiagramNode _doubleArrow = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode, _function);
    return _doubleArrow;
  }
  
  public XDiagram createBasicDiagram(final String nameSuffix) {
    XDiagram _xDiagram = new XDiagram();
    final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
      public void apply(final XDiagram it) {
        final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
          public void apply(final XDiagram it) {
            final SimpleNode simple = Demo.this.newSimpleNode(nameSuffix);
            final OpenableDiagramNode openable = Demo.this.newOpenableBasicDiagramNode(nameSuffix);
            final LevelOfDetailDiagramNode levelOfDetail = Demo.this.newEmbeddedBasicDiagram(nameSuffix);
            ObservableList<XNode> _nodes = it.getNodes();
            final Procedure1<SimpleNode> _function = new Procedure1<SimpleNode>() {
              public void apply(final SimpleNode it) {
                it.setLayoutX(75);
                it.setLayoutY(50);
              }
            };
            SimpleNode _doubleArrow = ObjectExtensions.<SimpleNode>operator_doubleArrow(simple, _function);
            _nodes.add(_doubleArrow);
            ObservableList<XNode> _nodes_1 = it.getNodes();
            final Procedure1<OpenableDiagramNode> _function_1 = new Procedure1<OpenableDiagramNode>() {
              public void apply(final OpenableDiagramNode it) {
                it.setLayoutX(350);
                it.setLayoutY(150);
              }
            };
            OpenableDiagramNode _doubleArrow_1 = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(openable, _function_1);
            _nodes_1.add(_doubleArrow_1);
            ObservableList<XNode> _nodes_2 = it.getNodes();
            final Procedure1<LevelOfDetailDiagramNode> _function_2 = new Procedure1<LevelOfDetailDiagramNode>() {
              public void apply(final LevelOfDetailDiagramNode it) {
                it.setLayoutX(50);
                it.setLayoutY(300);
              }
            };
            LevelOfDetailDiagramNode _doubleArrow_2 = ObjectExtensions.<LevelOfDetailDiagramNode>operator_doubleArrow(levelOfDetail, _function_2);
            _nodes_2.add(_doubleArrow_2);
            ObservableList<XConnection> _connections = it.getConnections();
            XConnection _xConnection = new XConnection(simple, openable);
            final Procedure1<XConnection> _function_3 = new Procedure1<XConnection>() {
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
            XConnection _doubleArrow_3 = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function_3);
            _connections.add(_doubleArrow_3);
            ObservableList<XConnection> _connections_1 = it.getConnections();
            XConnection _xConnection_1 = new XConnection(openable, levelOfDetail);
            final Procedure1<XConnection> _function_4 = new Procedure1<XConnection>() {
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
            XConnection _doubleArrow_4 = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection_1, _function_4);
            _connections_1.add(_doubleArrow_4);
            ObservableList<XConnection> _connections_2 = it.getConnections();
            XConnection _xConnection_2 = new XConnection(simple, levelOfDetail);
            final Procedure1<XConnection> _function_5 = new Procedure1<XConnection>() {
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
            XConnection _doubleArrow_5 = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection_2, _function_5);
            _connections_2.add(_doubleArrow_5);
          }
        };
        it.setContentsInitializer(_function);
      }
    };
    XDiagram _doubleArrow = ObjectExtensions.<XDiagram>operator_doubleArrow(_xDiagram, _function);
    return _doubleArrow;
  }
  
  protected OpenableDiagramNode openableDiagram(final String name, final XNode node) {
    OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode(name);
    final Procedure1<OpenableDiagramNode> _function = new Procedure1<OpenableDiagramNode>() {
      public void apply(final OpenableDiagramNode it) {
        XDiagram _xDiagram = new XDiagram();
        final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
          public void apply(final XDiagram it) {
            final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
              public void apply(final XDiagram it) {
                ObservableList<XNode> _nodes = it.getNodes();
                _nodes.add(node);
              }
            };
            it.setContentsInitializer(_function);
          }
        };
        XDiagram _doubleArrow = ObjectExtensions.<XDiagram>operator_doubleArrow(_xDiagram, _function);
        it.setInnerDiagram(_doubleArrow);
      }
    };
    OpenableDiagramNode _doubleArrow = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode, _function);
    return _doubleArrow;
  }
  
  protected void addRapidButtons(final XNode node, final String nameSuffix) {
    AddRapidButtonBehavior<XNode> _addRapidButtonBehavior = new AddRapidButtonBehavior<XNode>(node);
    final Procedure1<AddRapidButtonBehavior<XNode>> _function = new Procedure1<AddRapidButtonBehavior<XNode>>() {
      public void apply(final AddRapidButtonBehavior<XNode> it) {
        final Procedure1<AbstractChooser> _function = new Procedure1<AbstractChooser>() {
          public void apply(final AbstractChooser it) {
            IntegerRange _upTo = new IntegerRange(5, 20);
            for (final Integer i : _upTo) {
              String _plus = (" " + i);
              SimpleNode _newSimpleNode = Demo.this.newSimpleNode(_plus);
              it.addChoice(_newSimpleNode);
            }
            SimpleNode _newSimpleNode_1 = Demo.this.newSimpleNode(nameSuffix);
            it.addChoice(_newSimpleNode_1);
            OpenableDiagramNode _newOpenableBasicDiagramNode = Demo.this.newOpenableBasicDiagramNode(nameSuffix);
            it.addChoice(_newOpenableBasicDiagramNode);
            LevelOfDetailDiagramNode _newEmbeddedBasicDiagram = Demo.this.newEmbeddedBasicDiagram(nameSuffix);
            it.addChoice(_newEmbeddedBasicDiagram);
            MovieNode _newMovieNode = Demo.this.newMovieNode();
            it.addChoice(_newMovieNode);
            BrowserNode _newBrowserNode = Demo.this.newBrowserNode();
            it.addChoice(_newBrowserNode);
            BrickBreakerNode _newBrickBreakerNode = Demo.this.newBrickBreakerNode();
            it.addChoice(_newBrickBreakerNode);
            IntegerRange _upTo_1 = new IntegerRange(1, 4);
            for (final Integer i_1 : _upTo_1) {
              String _plus_1 = (" " + i_1);
              SimpleNode _newSimpleNode_2 = Demo.this.newSimpleNode(_plus_1);
              it.addChoice(_newSimpleNode_2);
            }
            SimpleNode _newSimpleNode_3 = Demo.this.newSimpleNode(nameSuffix);
            it.addChoice(_newSimpleNode_3);
          }
        };
        it.setChoiceInitializer(_function);
      }
    };
    AddRapidButtonBehavior<XNode> _doubleArrow = ObjectExtensions.<AddRapidButtonBehavior<XNode>>operator_doubleArrow(_addRapidButtonBehavior, _function);
    node.addBehavior(_doubleArrow);
  }
  
  public SimpleNode newSimpleNode(final String nameSuffix) {
    String _plus = ("Node" + nameSuffix);
    SimpleNode _simpleNode = new SimpleNode(_plus);
    final Procedure1<SimpleNode> _function = new Procedure1<SimpleNode>() {
      public void apply(final SimpleNode it) {
        boolean _isEmpty = nameSuffix.isEmpty();
        boolean _not = (!_isEmpty);
        if (_not) {
          Demo.this.addRapidButtons(it, nameSuffix);
        }
      }
    };
    SimpleNode _doubleArrow = ObjectExtensions.<SimpleNode>operator_doubleArrow(_simpleNode, _function);
    return _doubleArrow;
  }
  
  public OpenableDiagramNode newOpenableBasicDiagramNode(final String nameSuffix) {
    String _plus = ("Nested" + nameSuffix);
    OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode(_plus);
    final Procedure1<OpenableDiagramNode> _function = new Procedure1<OpenableDiagramNode>() {
      public void apply(final OpenableDiagramNode it) {
        String _plus = (nameSuffix + " (nested)");
        XDiagram _createBasicDiagram = Demo.this.createBasicDiagram(_plus);
        it.setInnerDiagram(_createBasicDiagram);
        Demo.this.addRapidButtons(it, nameSuffix);
      }
    };
    OpenableDiagramNode _doubleArrow = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode, _function);
    return _doubleArrow;
  }
  
  public LevelOfDetailDiagramNode newEmbeddedBasicDiagram(final String nameSuffix) {
    String _plus = ("Embedded" + nameSuffix);
    LevelOfDetailDiagramNode _levelOfDetailDiagramNode = new LevelOfDetailDiagramNode(_plus);
    final Procedure1<LevelOfDetailDiagramNode> _function = new Procedure1<LevelOfDetailDiagramNode>() {
      public void apply(final LevelOfDetailDiagramNode it) {
        String _plus = (nameSuffix + " (embedded)");
        XDiagram _createBasicDiagram = Demo.this.createBasicDiagram(_plus);
        it.setInnerDiagram(_createBasicDiagram);
        Demo.this.addRapidButtons(it, nameSuffix);
      }
    };
    LevelOfDetailDiagramNode _doubleArrow = ObjectExtensions.<LevelOfDetailDiagramNode>operator_doubleArrow(_levelOfDetailDiagramNode, _function);
    return _doubleArrow;
  }
  
  public LoginNode newLoginNode() {
    LoginNode _loginNode = new LoginNode();
    return _loginNode;
  }
  
  public EClassNode newEClassNode() {
    EClassNode _eClassNode = new EClassNode(Literals.ECLASS);
    return _eClassNode;
  }
  
  public JavaTypeNode newJavaTypeNode() {
    JavaTypeNode _javaTypeNode = new JavaTypeNode(Button.class);
    return _javaTypeNode;
  }
  
  public NeonSignNode newNeonSignNode() {
    NeonSignNode _neonSignNode = new NeonSignNode();
    return _neonSignNode;
  }
  
  public MovieNode newMovieNode() {
    MovieNode _movieNode = new MovieNode("Movie");
    final Procedure1<MovieNode> _function = new Procedure1<MovieNode>() {
      public void apply(final MovieNode it) {
        try {
          String _uRI = UriExtensions.toURI(Demo.this, "media/Usability.mp4");
          URL _uRL = new URL(_uRI);
          it.setMovieUrl(_uRL);
          it.setWidth(640);
          it.setHeight(360);
          MediaPlayer _player = it.getPlayer();
          Duration _minutes = Duration.minutes(2);
          _player.seek(_minutes);
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    };
    MovieNode _doubleArrow = ObjectExtensions.<MovieNode>operator_doubleArrow(_movieNode, _function);
    return _doubleArrow;
  }
  
  public BrowserNode newBrowserNode() {
    BrowserNode _browserNode = new BrowserNode("Browser");
    final Procedure1<BrowserNode> _function = new Procedure1<BrowserNode>() {
      public void apply(final BrowserNode it) {
        try {
          it.setWidth(120);
          it.setHeight(160);
          URL _uRL = new URL("http://koehnlein.blogspot.de/");
          it.setPageUrl(_uRL);
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    };
    BrowserNode _doubleArrow = ObjectExtensions.<BrowserNode>operator_doubleArrow(_browserNode, _function);
    return _doubleArrow;
  }
  
  public BrickBreakerNode newBrickBreakerNode() {
    BrickBreakerNode _brickBreakerNode = new BrickBreakerNode();
    final Procedure1<BrickBreakerNode> _function = new Procedure1<BrickBreakerNode>() {
      public void apply(final BrickBreakerNode it) {
        it.setWidth(640);
        it.setHeight(480);
      }
    };
    BrickBreakerNode _doubleArrow = ObjectExtensions.<BrickBreakerNode>operator_doubleArrow(_brickBreakerNode, _function);
    return _doubleArrow;
  }
  
  public RecursiveImageNode newRecursiveImageNode() {
    ImageCache _get = ImageCache.get();
    Image _image = _get.getImage(this, "media/laptop.jpg");
    int _minus = (-3);
    RecursiveImageNode _recursiveImageNode = new RecursiveImageNode("Recursive Laptop", _image, 0, _minus, 0.6);
    final Procedure1<RecursiveImageNode> _function = new Procedure1<RecursiveImageNode>() {
      public void apply(final RecursiveImageNode it) {
        it.setWidth(80);
        it.setHeight(60);
      }
    };
    RecursiveImageNode _doubleArrow = ObjectExtensions.<RecursiveImageNode>operator_doubleArrow(_recursiveImageNode, _function);
    return _doubleArrow;
  }
  
  public ImageNode newImageNode() {
    ImageNode _imageNode = new ImageNode("seltsam");
    final Procedure1<ImageNode> _function = new Procedure1<ImageNode>() {
      public void apply(final ImageNode it) {
        ImageCache _get = ImageCache.get();
        Image _image = _get.getImage(Demo.this, "media/seltsam.jpg");
        it.setImage(_image);
        it.setWidth(100);
      }
    };
    ImageNode _doubleArrow = ObjectExtensions.<ImageNode>operator_doubleArrow(_imageNode, _function);
    return _doubleArrow;
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
}
