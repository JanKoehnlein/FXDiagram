package de.fxdiagram.examples;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.services.ClassLoaderProvider;
import de.fxdiagram.core.services.ResourceDescriptor;
import de.fxdiagram.core.tools.actions.CenterAction;
import de.fxdiagram.core.tools.actions.CloseAction;
import de.fxdiagram.core.tools.actions.DeleteAction;
import de.fxdiagram.core.tools.actions.DiagramAction;
import de.fxdiagram.core.tools.actions.DiagramActionRegistry;
import de.fxdiagram.core.tools.actions.ExitAction;
import de.fxdiagram.core.tools.actions.ExportSvgAction;
import de.fxdiagram.core.tools.actions.FullScreenAction;
import de.fxdiagram.core.tools.actions.LayoutAction;
import de.fxdiagram.core.tools.actions.LoadAction;
import de.fxdiagram.core.tools.actions.NavigateNextAction;
import de.fxdiagram.core.tools.actions.NavigatePreviousAction;
import de.fxdiagram.core.tools.actions.OpenAction;
import de.fxdiagram.core.tools.actions.RedoAction;
import de.fxdiagram.core.tools.actions.RevealAction;
import de.fxdiagram.core.tools.actions.SaveAction;
import de.fxdiagram.core.tools.actions.SelectAllAction;
import de.fxdiagram.core.tools.actions.UndoAction;
import de.fxdiagram.core.tools.actions.ZoomToFitAction;
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
import de.fxdiagram.examples.slides.democamp.DemoCampIntroSlides;
import de.fxdiagram.examples.slides.democamp.DemoCampSummarySlides;
import de.fxdiagram.lib.actions.UndoRedoPlayerAction;
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
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

/**
 * Application to demonstarte the capabilities of FXDiagram in standalone (non-OSGi) mode.
 * I used this for various talks on FXDiagram that's why it also contains some slides.
 * 
 * Have a look at {@link #createScene()} as a starting point if you want to create your own
 * FXDiagram application.
 */
@SuppressWarnings("all")
public class Demo extends Application {
  private XRoot root;
  
  private ClassLoaderProvider classLoaderProvider;
  
  public static void main(final String... args) {
    Application.launch(args);
  }
  
  @Override
  public void start(final Stage it) {
    it.setTitle("FXDiagram Demo");
    Scene _createScene = this.createScene();
    it.setScene(_createScene);
    it.show();
  }
  
  /**
   * Sets up the scene with an {@link XRoot} and configures its services and actions.
   * Then sets up the initial diagram.
   */
  public Scene createScene() {
    Scene _xblockexpression = null;
    {
      XRoot _xRoot = new XRoot();
      this.root = _xRoot;
      final Scene scene = new Scene(this.root, 1024, 768);
      PerspectiveCamera _perspectiveCamera = new PerspectiveCamera();
      scene.setCamera(_perspectiveCamera);
      this.root.activate();
      final XDiagram diagram = new XDiagram();
      this.root.setRootDiagram(diagram);
      ClassLoaderProvider _classLoaderProvider = new ClassLoaderProvider();
      final Procedure1<ClassLoaderProvider> _function = (ClassLoaderProvider it) -> {
        Class<? extends Demo> _class = this.getClass();
        ClassLoader _classLoader = _class.getClassLoader();
        it.setRootClassLoader(_classLoader);
      };
      ClassLoaderProvider _doubleArrow = ObjectExtensions.<ClassLoaderProvider>operator_doubleArrow(_classLoaderProvider, _function);
      this.classLoaderProvider = _doubleArrow;
      ObservableList<DomainObjectProvider> _domainObjectProviders = this.root.getDomainObjectProviders();
      EcoreDomainObjectProvider _ecoreDomainObjectProvider = new EcoreDomainObjectProvider();
      JavaModelProvider _javaModelProvider = new JavaModelProvider();
      LcarsModelProvider _lcarsModelProvider = new LcarsModelProvider();
      Iterables.<DomainObjectProvider>addAll(_domainObjectProviders, Collections.<DomainObjectProvider>unmodifiableList(CollectionLiterals.<DomainObjectProvider>newArrayList(_ecoreDomainObjectProvider, _javaModelProvider, _lcarsModelProvider, this.classLoaderProvider)));
      DiagramActionRegistry _diagramActionRegistry = this.root.getDiagramActionRegistry();
      CenterAction _centerAction = new CenterAction();
      ExitAction _exitAction = new ExitAction();
      DeleteAction _deleteAction = new DeleteAction();
      LayoutAction _layoutAction = new LayoutAction(LayoutType.DOT);
      ExportSvgAction _exportSvgAction = new ExportSvgAction();
      RedoAction _redoAction = new RedoAction();
      UndoAction _undoAction = new UndoAction();
      RevealAction _revealAction = new RevealAction();
      LoadAction _loadAction = new LoadAction();
      SaveAction _saveAction = new SaveAction();
      SelectAllAction _selectAllAction = new SelectAllAction();
      ZoomToFitAction _zoomToFitAction = new ZoomToFitAction();
      NavigatePreviousAction _navigatePreviousAction = new NavigatePreviousAction();
      NavigateNextAction _navigateNextAction = new NavigateNextAction();
      OpenAction _openAction = new OpenAction();
      CloseAction _closeAction = new CloseAction();
      FullScreenAction _fullScreenAction = new FullScreenAction();
      UndoRedoPlayerAction _undoRedoPlayerAction = new UndoRedoPlayerAction();
      _diagramActionRegistry.operator_add(Collections.<DiagramAction>unmodifiableList(CollectionLiterals.<DiagramAction>newArrayList(_centerAction, _exitAction, _deleteAction, _layoutAction, _exportSvgAction, _redoAction, _undoAction, _revealAction, _loadAction, _saveAction, _selectAllAction, _zoomToFitAction, _navigatePreviousAction, _navigateNextAction, _openAction, _closeAction, _fullScreenAction, _undoRedoPlayerAction)));
      final Procedure1<XDiagram> _function_1 = (XDiagram it) -> {
        ObservableList<XNode> _nodes = it.getNodes();
        DemoCampIntroSlides _demoCampIntroSlides = new DemoCampIntroSlides();
        _nodes.add(_demoCampIntroSlides);
        ObservableList<XNode> _nodes_1 = it.getNodes();
        OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode("Basic");
        final Procedure1<OpenableDiagramNode> _function_2 = (OpenableDiagramNode it_1) -> {
          LazyExampleDiagram _lazyExampleDiagram = new LazyExampleDiagram("");
          it_1.setInnerDiagram(_lazyExampleDiagram);
        };
        OpenableDiagramNode _doubleArrow_1 = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode, _function_2);
        _nodes_1.add(_doubleArrow_1);
        ObservableList<XNode> _nodes_2 = it.getNodes();
        OpenableDiagramNode _openableDiagramNode_1 = new OpenableDiagramNode("JavaFX");
        final Procedure1<OpenableDiagramNode> _function_3 = (OpenableDiagramNode it_1) -> {
          XDiagram _xDiagram = new XDiagram();
          final Procedure1<XDiagram> _function_4 = (XDiagram it_2) -> {
            ObservableList<XNode> _nodes_3 = it_2.getNodes();
            LoginNode _newLoginNode = this.newLoginNode();
            _nodes_3.add(_newLoginNode);
            ObservableList<XNode> _nodes_4 = it_2.getNodes();
            RecursiveImageNode _newRecursiveImageNode = this.newRecursiveImageNode();
            _nodes_4.add(_newRecursiveImageNode);
            ObservableList<XNode> _nodes_5 = it_2.getNodes();
            ImageNode _newImageNode = this.newImageNode();
            _nodes_5.add(_newImageNode);
            ObservableList<XNode> _nodes_6 = it_2.getNodes();
            MovieNode _newMovieNode = this.newMovieNode();
            _nodes_6.add(_newMovieNode);
            ObservableList<XNode> _nodes_7 = it_2.getNodes();
            BrowserNode _newBrowserNode = this.newBrowserNode();
            _nodes_7.add(_newBrowserNode);
            ObservableList<XNode> _nodes_8 = it_2.getNodes();
            BrickBreakerNode _newBrickBreakerNode = this.newBrickBreakerNode();
            _nodes_8.add(_newBrickBreakerNode);
            it_2.setLayoutOnActivate(LayoutType.DOT);
          };
          XDiagram _doubleArrow_2 = ObjectExtensions.<XDiagram>operator_doubleArrow(_xDiagram, _function_4);
          it_1.setInnerDiagram(_doubleArrow_2);
        };
        OpenableDiagramNode _doubleArrow_2 = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode_1, _function_3);
        _nodes_2.add(_doubleArrow_2);
        ObservableList<XNode> _nodes_3 = it.getNodes();
        NeonSignNode _newNeonSignNode = this.newNeonSignNode();
        OpenableDiagramNode _openableDiagram = this.openableDiagram("Xtend", _newNeonSignNode);
        _nodes_3.add(_openableDiagram);
        ObservableList<XNode> _nodes_4 = it.getNodes();
        JavaTypeNode _newJavaTypeNode = this.newJavaTypeNode();
        OpenableDiagramNode _openableDiagram_1 = this.openableDiagram("JavaFX Explorer", _newJavaTypeNode);
        _nodes_4.add(_openableDiagram_1);
        ObservableList<XNode> _nodes_5 = it.getNodes();
        EClassNode _newEClassNode = this.newEClassNode();
        OpenableDiagramNode _openableDiagram_2 = this.openableDiagram("Ecore Explorer", _newEClassNode);
        _nodes_5.add(_openableDiagram_2);
        ObservableList<XNode> _nodes_6 = it.getNodes();
        DemoCampSummarySlides _demoCampSummarySlides = new DemoCampSummarySlides();
        _nodes_6.add(_demoCampSummarySlides);
        double _width = scene.getWidth();
        ObservableList<XNode> _nodes_7 = it.getNodes();
        int _size = _nodes_7.size();
        int _plus = (_size + 2);
        final double deltaX = (_width / _plus);
        double _height = scene.getHeight();
        ObservableList<XNode> _nodes_8 = it.getNodes();
        int _size_1 = _nodes_8.size();
        int _plus_1 = (_size_1 + 2);
        final double deltaY = (_height / _plus_1);
        ObservableList<XNode> _nodes_9 = it.getNodes();
        final Procedure2<XNode, Integer> _function_4 = (XNode node, Integer i) -> {
          Bounds _layoutBounds = node.getLayoutBounds();
          double _width_1 = _layoutBounds.getWidth();
          double _divide = (_width_1 / 2);
          double _minus = (((i).intValue() * deltaX) - _divide);
          node.setLayoutX(_minus);
          Bounds _layoutBounds_1 = node.getLayoutBounds();
          double _height_1 = _layoutBounds_1.getHeight();
          double _divide_1 = (_height_1 / 2);
          double _minus_1 = (((i).intValue() * deltaY) - _divide_1);
          node.setLayoutY(_minus_1);
        };
        IterableExtensions.<XNode>forEach(_nodes_9, _function_4);
        ObservableList<XNode> _nodes_10 = it.getNodes();
        ObservableList<XNode> _nodes_11 = it.getNodes();
        int _size_2 = _nodes_11.size();
        int _minus = (_size_2 - 1);
        List<XNode> _subList = _nodes_10.subList(0, _minus);
        final Procedure2<XNode, Integer> _function_5 = (XNode node, Integer i) -> {
          ObservableList<XConnection> _connections = it.getConnections();
          ObservableList<XNode> _nodes_12 = it.getNodes();
          XNode _get = _nodes_12.get(((i).intValue() + 1));
          XConnection _xConnection = new XConnection(node, _get);
          _connections.add(_xConnection);
        };
        IterableExtensions.<XNode>forEach(_subList, _function_5);
      };
      ObjectExtensions.<XDiagram>operator_doubleArrow(diagram, _function_1);
      this.warmUpLayouter();
      final Runnable _function_2 = () -> {
        diagram.centerDiagram(true);
      };
      Platform.runLater(_function_2);
      _xblockexpression = scene;
    }
    return _xblockexpression;
  }
  
  public OpenableDiagramNode newGalleryDiagramNode() {
    OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode("Gallery");
    final Procedure1<OpenableDiagramNode> _function = (OpenableDiagramNode it) -> {
      XDiagram _xDiagram = new XDiagram();
      final Procedure1<XDiagram> _function_1 = (XDiagram it_1) -> {
        final Procedure1<XDiagram> _function_2 = (XDiagram it_2) -> {
          ObservableList<XNode> _nodes = it_2.getNodes();
          SimpleNode _simpleNode = new SimpleNode("Simple");
          _nodes.add(_simpleNode);
          ObservableList<XNode> _nodes_1 = it_2.getNodes();
          OpenableDiagramNode _openableDiagramNode_1 = new OpenableDiagramNode("Openable");
          final Procedure1<OpenableDiagramNode> _function_3 = (OpenableDiagramNode it_3) -> {
            LazyExampleDiagram _lazyExampleDiagram = new LazyExampleDiagram("(n)");
            it_3.setInnerDiagram(_lazyExampleDiagram);
          };
          OpenableDiagramNode _doubleArrow = ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode_1, _function_3);
          _nodes_1.add(_doubleArrow);
          ObservableList<XNode> _nodes_2 = it_2.getNodes();
          LevelOfDetailDiagramNode _levelOfDetailDiagramNode = new LevelOfDetailDiagramNode("Embedded");
          final Procedure1<LevelOfDetailDiagramNode> _function_4 = (LevelOfDetailDiagramNode it_3) -> {
            LazyExampleDiagram _lazyExampleDiagram = new LazyExampleDiagram("(e)");
            it_3.setInnerDiagram(_lazyExampleDiagram);
          };
          LevelOfDetailDiagramNode _doubleArrow_1 = ObjectExtensions.<LevelOfDetailDiagramNode>operator_doubleArrow(_levelOfDetailDiagramNode, _function_4);
          _nodes_2.add(_doubleArrow_1);
          ObservableList<XNode> _nodes_3 = it_2.getNodes();
          NeonSignNode _newNeonSignNode = this.newNeonSignNode();
          _nodes_3.add(_newNeonSignNode);
          ObservableList<XNode> _nodes_4 = it_2.getNodes();
          JavaTypeNode _newJavaTypeNode = this.newJavaTypeNode();
          _nodes_4.add(_newJavaTypeNode);
          ObservableList<XNode> _nodes_5 = it_2.getNodes();
          EClassNode _newEClassNode = this.newEClassNode();
          _nodes_5.add(_newEClassNode);
          ObservableList<XNode> _nodes_6 = it_2.getNodes();
          LoginNode _newLoginNode = this.newLoginNode();
          _nodes_6.add(_newLoginNode);
          ObservableList<XNode> _nodes_7 = it_2.getNodes();
          RecursiveImageNode _newRecursiveImageNode = this.newRecursiveImageNode();
          _nodes_7.add(_newRecursiveImageNode);
          ObservableList<XNode> _nodes_8 = it_2.getNodes();
          ImageNode _newImageNode = this.newImageNode();
          _nodes_8.add(_newImageNode);
          ObservableList<XNode> _nodes_9 = it_2.getNodes();
          MovieNode _newMovieNode = this.newMovieNode();
          _nodes_9.add(_newMovieNode);
          ObservableList<XNode> _nodes_10 = it_2.getNodes();
          BrowserNode _newBrowserNode = this.newBrowserNode();
          _nodes_10.add(_newBrowserNode);
          ObservableList<XNode> _nodes_11 = it_2.getNodes();
          BrickBreakerNode _newBrickBreakerNode = this.newBrickBreakerNode();
          _nodes_11.add(_newBrickBreakerNode);
          ObservableList<XNode> _nodes_12 = it_2.getNodes();
          OpenableDiagramNode _newLcarsDiagramNode = this.newLcarsDiagramNode();
          _nodes_12.add(_newLcarsDiagramNode);
        };
        it_1.setContentsInitializer(_function_2);
      };
      XDiagram _doubleArrow = ObjectExtensions.<XDiagram>operator_doubleArrow(_xDiagram, _function_1);
      it.setInnerDiagram(_doubleArrow);
    };
    return ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode, _function);
  }
  
  public OpenableDiagramNode newLcarsDiagramNode() {
    OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode("LCARS");
    final Procedure1<OpenableDiagramNode> _function = (OpenableDiagramNode it) -> {
      LcarsDiagram _lcarsDiagram = new LcarsDiagram();
      it.setInnerDiagram(_lcarsDiagram);
    };
    return ObjectExtensions.<OpenableDiagramNode>operator_doubleArrow(_openableDiagramNode, _function);
  }
  
  protected OpenableDiagramNode openableDiagram(final String name, final XNode node) {
    OpenableDiagramNode _openableDiagramNode = new OpenableDiagramNode(name);
    final Procedure1<OpenableDiagramNode> _function = (OpenableDiagramNode it) -> {
      XDiagram _xDiagram = new XDiagram();
      final Procedure1<XDiagram> _function_1 = (XDiagram it_1) -> {
        ObservableList<XNode> _nodes = it_1.getNodes();
        _nodes.add(node);
      };
      XDiagram _doubleArrow = ObjectExtensions.<XDiagram>operator_doubleArrow(_xDiagram, _function_1);
      it.setInnerDiagram(_doubleArrow);
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
    ResourceDescriptor _newResource = this.newResource("Movie", "media/quirk.mp4");
    MovieNode _movieNode = new MovieNode(_newResource);
    final Procedure1<MovieNode> _function = (MovieNode it) -> {
      it.setWidth(640);
      it.setHeight(360);
    };
    return ObjectExtensions.<MovieNode>operator_doubleArrow(_movieNode, _function);
  }
  
  public BrowserNode newBrowserNode() {
    BrowserNode _browserNode = new BrowserNode("Browser");
    final Procedure1<BrowserNode> _function = (BrowserNode it) -> {
      try {
        it.setWidth(120);
        it.setHeight(160);
        URL _uRL = new URL("http://koehnlein.blogspot.de/");
        it.setPageUrl(_uRL);
      } catch (Throwable _e) {
        throw Exceptions.sneakyThrow(_e);
      }
    };
    return ObjectExtensions.<BrowserNode>operator_doubleArrow(_browserNode, _function);
  }
  
  public BrickBreakerNode newBrickBreakerNode() {
    BrickBreakerNode _brickBreakerNode = new BrickBreakerNode("BrickBreaker");
    final Procedure1<BrickBreakerNode> _function = (BrickBreakerNode it) -> {
      it.setWidth(640);
      it.setHeight(480);
    };
    return ObjectExtensions.<BrickBreakerNode>operator_doubleArrow(_brickBreakerNode, _function);
  }
  
  public RecursiveImageNode newRecursiveImageNode() {
    ResourceDescriptor _newResource = this.newResource("laptop", "media/laptop.jpg");
    RecursiveImageNode _recursiveImageNode = new RecursiveImageNode(_newResource);
    final Procedure1<RecursiveImageNode> _function = (RecursiveImageNode it) -> {
      it.setX(0);
      it.setY((-3));
      it.setScale(0.6);
      it.setWidth(80);
      it.setHeight(60);
    };
    return ObjectExtensions.<RecursiveImageNode>operator_doubleArrow(_recursiveImageNode, _function);
  }
  
  public ImageNode newImageNode() {
    ResourceDescriptor _newResource = this.newResource("seltsam", "media/seltsam.jpg");
    ImageNode _imageNode = new ImageNode(_newResource);
    final Procedure1<ImageNode> _function = (ImageNode it) -> {
      it.setWidth(100);
    };
    return ObjectExtensions.<ImageNode>operator_doubleArrow(_imageNode, _function);
  }
  
  protected void warmUpLayouter() {
    final Task<Void> _function = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        Object _xblockexpression = null;
        {
          new Layouter();
          _xblockexpression = null;
        }
        return ((Void)_xblockexpression);
      }
    };
    final Task<Void> task = _function;
    task.run();
  }
  
  protected ResourceDescriptor newResource(final String name, final String relativePath) {
    Class<? extends Demo> _class = this.getClass();
    return this.classLoaderProvider.createResourceDescriptor(name, _class, relativePath);
  }
}
