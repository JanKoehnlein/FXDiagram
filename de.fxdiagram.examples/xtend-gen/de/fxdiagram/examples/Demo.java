package de.fxdiagram.examples;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.services.ResourceDescriptor;
import de.fxdiagram.core.services.ResourceHandle;
import de.fxdiagram.core.services.ResourceProvider;
import de.fxdiagram.examples.BrickBreakerNode;
import de.fxdiagram.examples.LazyExampleDiagram;
import de.fxdiagram.examples.ecore.EClassDescriptor;
import de.fxdiagram.examples.ecore.EClassNode;
import de.fxdiagram.examples.ecore.EcoreDomainObjectProvider;
import de.fxdiagram.examples.java.JavaModelProvider;
import de.fxdiagram.examples.java.JavaTypeDescriptor;
import de.fxdiagram.examples.java.JavaTypeNode;
import de.fxdiagram.examples.lcars.LcarsDiagram;
import de.fxdiagram.examples.lcars.LcarsModelProvider;
import de.fxdiagram.examples.login.LoginNode;
import de.fxdiagram.examples.neonsign.NeonSignNode;
import de.fxdiagram.examples.slides.eclipsecon.IntroductionSlideDeck;
import de.fxdiagram.examples.slides.eclipsecon.SummarySlideDeck;
import de.fxdiagram.lib.media.BrowserNode;
import de.fxdiagram.lib.media.ImageNode;
import de.fxdiagram.lib.media.MovieNode;
import de.fxdiagram.lib.media.RecursiveImageNode;
import de.fxdiagram.lib.simple.LevelOfDetailDiagramNode;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import de.fxdiagram.lib.simple.SimpleNode;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class Demo extends Application {
  private XRoot root;
  
  private ResourceProvider resourceProvider;
  
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
      this.root = _xRoot;
      Class<? extends Demo> _class = this.getClass();
      ClassLoader _classLoader = _class.getClassLoader();
      this.root.setClassLoader(_classLoader);
      final Scene scene = new Scene(this.root, 1024, 768);
      PerspectiveCamera _perspectiveCamera = new PerspectiveCamera();
      scene.setCamera(_perspectiveCamera);
      this.root.activate();
      final XDiagram diagram = new XDiagram();
      this.root.setDiagram(diagram);
      Class<? extends Demo> _class_1 = this.getClass();
      ClassLoader _classLoader_1 = _class_1.getClassLoader();
      ResourceProvider _resourceProvider = new ResourceProvider(_classLoader_1);
      this.resourceProvider = _resourceProvider;
      ObservableList<DomainObjectProvider> _domainObjectProviders = this.root.getDomainObjectProviders();
      EcoreDomainObjectProvider _ecoreDomainObjectProvider = new EcoreDomainObjectProvider();
      JavaModelProvider _javaModelProvider = new JavaModelProvider();
      LcarsModelProvider _lcarsModelProvider = new LcarsModelProvider();
      Iterables.<DomainObjectProvider>addAll(_domainObjectProviders, Collections.<DomainObjectProvider>unmodifiableList(Lists.<DomainObjectProvider>newArrayList(_ecoreDomainObjectProvider, _javaModelProvider, _lcarsModelProvider, this.resourceProvider)));
      final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
        public void apply(final XDiagram it) {
          ObservableList<XNode> _nodes = it.getNodes();
          IntroductionSlideDeck _introductionSlideDeck = new IntroductionSlideDeck();
          _nodes.add(_introductionSlideDeck);
          ObservableList<XNode> _nodes_1 = it.getNodes();
          OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode("Basic");
          final Procedure1<OpenableDiagramNode> _function = new Procedure1<OpenableDiagramNode>() {
            public void apply(final OpenableDiagramNode it) {
              LazyExampleDiagram _lazyExampleDiagram = new LazyExampleDiagram("");
              it.setInnerDiagram(_lazyExampleDiagram);
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
              Bounds _layoutBounds = node.getLayoutBounds();
              double _width = _layoutBounds.getWidth();
              double _divide = (_width / 2);
              double _minus = (((i).intValue() * deltaX) - _divide);
              node.setLayoutX(_minus);
              Bounds _layoutBounds_1 = node.getLayoutBounds();
              double _height = _layoutBounds_1.getHeight();
              double _divide_1 = (_height / 2);
              double _minus_1 = (((i).intValue() * deltaY) - _divide_1);
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
              XNode _get = _nodes.get(((i).intValue() + 1));
              XConnection _xConnection = new XConnection(node, _get);
              _connections.add(_xConnection);
            }
          };
          IterableExtensions.<XNode>forEach(_subList, _function_3);
        }
      };
      ObjectExtensions.<XDiagram>operator_doubleArrow(diagram, _function);
      this.warmUpLayouter();
      final Runnable _function_1 = new Runnable() {
        public void run() {
          diagram.centerDiagram(true);
        }
      };
      Platform.runLater(_function_1);
      _xblockexpression = scene;
    }
    return _xblockexpression;
  }
  
  public OpenableDiagramNode newGalleryDiagramNode() {
    OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode("Gallery");
    final Procedure1<OpenableDiagramNode> _function = new Procedure1<OpenableDiagramNode>() {
      public void apply(final OpenableDiagramNode it) {
        XDiagram _xDiagram = new XDiagram();
        final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
          public void apply(final XDiagram it) {
            final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
              public void apply(final XDiagram it) {
                ObservableList<XNode> _nodes = it.getNodes();
                SimpleNode _simpleNode = new SimpleNode("Simple");
                _nodes.add(_simpleNode);
                ObservableList<XNode> _nodes_1 = it.getNodes();
                OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode("Openable");
                final Procedure1<OpenableDiagramNode> _function = new Procedure1<OpenableDiagramNode>() {
                  public void apply(final OpenableDiagramNode it) {
                    LazyExampleDiagram _lazyExampleDiagram = new LazyExampleDiagram("(n)");
                    it.setInnerDiagram(_lazyExampleDiagram);
                  }
                };
                OpenableDiagramNode _doubleArrow = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode, _function);
                _nodes_1.add(_doubleArrow);
                ObservableList<XNode> _nodes_2 = it.getNodes();
                LevelOfDetailDiagramNode _levelOfDetailDiagramNode = new LevelOfDetailDiagramNode("Embedded");
                final Procedure1<LevelOfDetailDiagramNode> _function_1 = new Procedure1<LevelOfDetailDiagramNode>() {
                  public void apply(final LevelOfDetailDiagramNode it) {
                    LazyExampleDiagram _lazyExampleDiagram = new LazyExampleDiagram("(e)");
                    it.setInnerDiagram(_lazyExampleDiagram);
                  }
                };
                LevelOfDetailDiagramNode _doubleArrow_1 = ObjectExtensions.<LevelOfDetailDiagramNode>operator_doubleArrow(_levelOfDetailDiagramNode, _function_1);
                _nodes_2.add(_doubleArrow_1);
                ObservableList<XNode> _nodes_3 = it.getNodes();
                NeonSignNode _newNeonSignNode = Demo.this.newNeonSignNode();
                _nodes_3.add(_newNeonSignNode);
                ObservableList<XNode> _nodes_4 = it.getNodes();
                JavaTypeNode _newJavaTypeNode = Demo.this.newJavaTypeNode();
                _nodes_4.add(_newJavaTypeNode);
                ObservableList<XNode> _nodes_5 = it.getNodes();
                EClassNode _newEClassNode = Demo.this.newEClassNode();
                _nodes_5.add(_newEClassNode);
                ObservableList<XNode> _nodes_6 = it.getNodes();
                LoginNode _newLoginNode = Demo.this.newLoginNode();
                _nodes_6.add(_newLoginNode);
                ObservableList<XNode> _nodes_7 = it.getNodes();
                RecursiveImageNode _newRecursiveImageNode = Demo.this.newRecursiveImageNode();
                _nodes_7.add(_newRecursiveImageNode);
                ObservableList<XNode> _nodes_8 = it.getNodes();
                ImageNode _newImageNode = Demo.this.newImageNode();
                _nodes_8.add(_newImageNode);
                ObservableList<XNode> _nodes_9 = it.getNodes();
                MovieNode _newMovieNode = Demo.this.newMovieNode();
                _nodes_9.add(_newMovieNode);
                ObservableList<XNode> _nodes_10 = it.getNodes();
                BrowserNode _newBrowserNode = Demo.this.newBrowserNode();
                _nodes_10.add(_newBrowserNode);
                ObservableList<XNode> _nodes_11 = it.getNodes();
                BrickBreakerNode _newBrickBreakerNode = Demo.this.newBrickBreakerNode();
                _nodes_11.add(_newBrickBreakerNode);
                ObservableList<XNode> _nodes_12 = it.getNodes();
                OpenableDiagramNode _newLcarsDiagramNode = Demo.this.newLcarsDiagramNode();
                _nodes_12.add(_newLcarsDiagramNode);
              }
            };
            it.setContentsInitializer(_function);
          }
        };
        XDiagram _doubleArrow = ObjectExtensions.<XDiagram>operator_doubleArrow(_xDiagram, _function);
        it.setInnerDiagram(_doubleArrow);
      }
    };
    return ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode, _function);
  }
  
  public OpenableDiagramNode newLcarsDiagramNode() {
    OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode("LCARS");
    final Procedure1<OpenableDiagramNode> _function = new Procedure1<OpenableDiagramNode>() {
      public void apply(final OpenableDiagramNode it) {
        LcarsDiagram _lcarsDiagram = new LcarsDiagram();
        it.setInnerDiagram(_lcarsDiagram);
      }
    };
    return ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode, _function);
  }
  
  protected OpenableDiagramNode openableDiagram(final String name, final XNode node) {
    OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode(name);
    final Procedure1<OpenableDiagramNode> _function = new Procedure1<OpenableDiagramNode>() {
      public void apply(final OpenableDiagramNode it) {
        XDiagram _xDiagram = new XDiagram();
        final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
          public void apply(final XDiagram it) {
            ObservableList<XNode> _nodes = it.getNodes();
            _nodes.add(node);
          }
        };
        XDiagram _doubleArrow = ObjectExtensions.<XDiagram>operator_doubleArrow(_xDiagram, _function);
        it.setInnerDiagram(_doubleArrow);
      }
    };
    return ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode, _function);
  }
  
  public LoginNode newLoginNode() {
    return new LoginNode("Login");
  }
  
  public EClassNode newEClassNode() {
    EClassNode _xblockexpression = null;
    {
      final EcoreDomainObjectProvider provider = this.root.<EcoreDomainObjectProvider>getDomainObjectProvider(EcoreDomainObjectProvider.class);
      EClassDescriptor _createEClassDescriptor = provider.createEClassDescriptor(EcorePackage.Literals.ECLASS);
      _xblockexpression = new EClassNode(_createEClassDescriptor);
    }
    return _xblockexpression;
  }
  
  public JavaTypeNode newJavaTypeNode() {
    JavaTypeNode _xblockexpression = null;
    {
      final JavaModelProvider provider = this.root.<JavaModelProvider>getDomainObjectProvider(JavaModelProvider.class);
      JavaTypeDescriptor _createJavaTypeDescriptor = provider.createJavaTypeDescriptor(Button.class);
      _xblockexpression = new JavaTypeNode(_createJavaTypeDescriptor);
    }
    return _xblockexpression;
  }
  
  public NeonSignNode newNeonSignNode() {
    return new NeonSignNode("NeonSign");
  }
  
  public MovieNode newMovieNode() {
    ResourceDescriptor _newResource = this.newResource("Movie", "media/Usability.mp4");
    MovieNode _movieNode = new MovieNode(_newResource);
    final Procedure1<MovieNode> _function = new Procedure1<MovieNode>() {
      public void apply(final MovieNode it) {
        it.setWidth(640);
        it.setHeight(360);
      }
    };
    return ObjectExtensions.<MovieNode>operator_doubleArrow(_movieNode, _function);
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
    return ObjectExtensions.<BrowserNode>operator_doubleArrow(_browserNode, _function);
  }
  
  public BrickBreakerNode newBrickBreakerNode() {
    BrickBreakerNode _brickBreakerNode = new BrickBreakerNode("BrickBreaker");
    final Procedure1<BrickBreakerNode> _function = new Procedure1<BrickBreakerNode>() {
      public void apply(final BrickBreakerNode it) {
        it.setWidth(640);
        it.setHeight(480);
      }
    };
    return ObjectExtensions.<BrickBreakerNode>operator_doubleArrow(_brickBreakerNode, _function);
  }
  
  public RecursiveImageNode newRecursiveImageNode() {
    ResourceDescriptor _newResource = this.newResource("laptop", "media/laptop.jpg");
    RecursiveImageNode _recursiveImageNode = new RecursiveImageNode(_newResource);
    final Procedure1<RecursiveImageNode> _function = new Procedure1<RecursiveImageNode>() {
      public void apply(final RecursiveImageNode it) {
        it.setX(0);
        it.setY((-3));
        it.setScale(0.6);
        it.setWidth(80);
        it.setHeight(60);
      }
    };
    return ObjectExtensions.<RecursiveImageNode>operator_doubleArrow(_recursiveImageNode, _function);
  }
  
  public ImageNode newImageNode() {
    ResourceDescriptor _newResource = this.newResource("seltsam", "media/seltsam.jpg");
    ImageNode _imageNode = new ImageNode(_newResource);
    final Procedure1<ImageNode> _function = new Procedure1<ImageNode>() {
      public void apply(final ImageNode it) {
        it.setWidth(100);
      }
    };
    return ObjectExtensions.<ImageNode>operator_doubleArrow(_imageNode, _function);
  }
  
  protected void warmUpLayouter() {
    final Task<Void> _function = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        Void _xblockexpression = null;
        {
          new Layouter();
          _xblockexpression = null;
        }
        return _xblockexpression;
      }
    };
    final Task<Void> task = _function;
    task.run();
  }
  
  protected ResourceDescriptor newResource(final String name, final String relativePath) {
    Class<? extends Demo> _class = this.getClass();
    ResourceHandle _resourceHandle = new ResourceHandle(name, _class, relativePath);
    return this.resourceProvider.createResourceDescriptor(_resourceHandle);
  }
}
