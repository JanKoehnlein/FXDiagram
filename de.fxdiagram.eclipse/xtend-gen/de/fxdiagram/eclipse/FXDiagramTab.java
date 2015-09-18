package de.fxdiagram.eclipse;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.behavior.ReconcileBehavior;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.LazyCommand;
import de.fxdiagram.core.command.ParallelAnimationCommand;
import de.fxdiagram.core.command.SelectAndRevealCommand;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.services.ClassLoaderProvider;
import de.fxdiagram.core.tools.actions.CenterAction;
import de.fxdiagram.core.tools.actions.DeleteAction;
import de.fxdiagram.core.tools.actions.DiagramAction;
import de.fxdiagram.core.tools.actions.DiagramActionRegistry;
import de.fxdiagram.core.tools.actions.ExportSvgAction;
import de.fxdiagram.core.tools.actions.FullScreenAction;
import de.fxdiagram.core.tools.actions.LayoutAction;
import de.fxdiagram.core.tools.actions.NavigateNextAction;
import de.fxdiagram.core.tools.actions.NavigatePreviousAction;
import de.fxdiagram.core.tools.actions.RedoAction;
import de.fxdiagram.core.tools.actions.RevealAction;
import de.fxdiagram.core.tools.actions.SaveAction;
import de.fxdiagram.core.tools.actions.SelectAllAction;
import de.fxdiagram.core.tools.actions.UndoAction;
import de.fxdiagram.core.tools.actions.ZoomToFitAction;
import de.fxdiagram.eclipse.ClearDiagramCommand;
import de.fxdiagram.eclipse.FXDiagramView;
import de.fxdiagram.eclipse.actions.EclipseLoadAction;
import de.fxdiagram.eclipse.changes.IChangeListener;
import de.fxdiagram.eclipse.changes.ModelChangeBroker;
import de.fxdiagram.lib.actions.UndoRedoPlayerAction;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.ConnectionMappingCall;
import de.fxdiagram.mapping.DiagramMappingCall;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.InterpreterContext;
import de.fxdiagram.mapping.MappingCall;
import de.fxdiagram.mapping.NodeMappingCall;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.XDiagramConfigInterpreter;
import de.fxdiagram.swtfx.SwtToFXGestureConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swt.FXCanvas;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.util.Duration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class FXDiagramTab {
  private final CTabItem tab;
  
  private final FXCanvas canvas;
  
  private final SwtToFXGestureConverter gestureConverter;
  
  private final XRoot root;
  
  private final XDiagramConfigInterpreter configInterpreter = new XDiagramConfigInterpreter();
  
  private boolean isLinkWithEditor;
  
  private IChangeListener changeListener;
  
  public FXDiagramTab(final FXDiagramView view, final CTabFolder tabFolder) {
    FXCanvas _fXCanvas = new FXCanvas(tabFolder, SWT.NONE);
    this.canvas = _fXCanvas;
    CTabItem _cTabItem = new CTabItem(tabFolder, SWT.CLOSE);
    this.tab = _cTabItem;
    this.tab.setControl(this.canvas);
    SwtToFXGestureConverter _swtToFXGestureConverter = new SwtToFXGestureConverter(this.canvas);
    this.gestureConverter = _swtToFXGestureConverter;
    final IChangeListener _function = (IWorkbenchPart it) -> {
      if ((it instanceof IEditorPart)) {
        this.refreshUpdateState();
      }
    };
    this.changeListener = _function;
    ModelChangeBroker _modelChangeBroker = view.getModelChangeBroker();
    _modelChangeBroker.addListener(this.changeListener);
    XRoot _createRoot = this.createRoot();
    this.root = _createRoot;
    String _name = this.root.getName();
    this.tab.setText(_name);
    Scene _scene = new Scene(this.root);
    final Procedure1<Scene> _function_1 = (Scene it) -> {
      PerspectiveCamera _perspectiveCamera = new PerspectiveCamera();
      it.setCamera(_perspectiveCamera);
      this.root.activate();
    };
    Scene _doubleArrow = ObjectExtensions.<Scene>operator_doubleArrow(_scene, _function_1);
    this.canvas.setScene(_doubleArrow);
    final DisposeListener _function_2 = (DisposeEvent it) -> {
      this.gestureConverter.dispose();
      ModelChangeBroker _modelChangeBroker_1 = view.getModelChangeBroker();
      _modelChangeBroker_1.removeListener(this.changeListener);
    };
    this.tab.addDisposeListener(_function_2);
    StringProperty _nameProperty = this.root.nameProperty();
    final ChangeListener<String> _function_3 = (ObservableValue<? extends String> p, String o, String n) -> {
      this.tab.setText(n);
    };
    _nameProperty.addListener(_function_3);
    BooleanProperty _needsSaveProperty = this.root.needsSaveProperty();
    final ChangeListener<Boolean> _function_4 = (ObservableValue<? extends Boolean> p, Boolean o, Boolean n) -> {
      if ((n).booleanValue()) {
        String _name_1 = this.root.getName();
        String _plus = ("*" + _name_1);
        this.tab.setText(_plus);
      } else {
        String _name_2 = this.root.getName();
        this.tab.setText(_name_2);
      }
    };
    _needsSaveProperty.addListener(_function_4);
    this.canvas.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(final FocusEvent e) {
        IWorkbenchPartSite _site = view.getSite();
        IBindingService _service = _site.<IBindingService>getService(IBindingService.class);
        ((IBindingService) _service).setKeyFilterEnabled(false);
      }
      
      @Override
      public void focusLost(final FocusEvent e) {
        IWorkbenchPartSite _site = view.getSite();
        IBindingService _service = _site.<IBindingService>getService(IBindingService.class);
        ((IBindingService) _service).setKeyFilterEnabled(true);
      }
    });
  }
  
  protected XRoot createRoot() {
    XRoot _xRoot = new XRoot();
    final Procedure1<XRoot> _function = (XRoot it) -> {
      XDiagram _xDiagram = new XDiagram();
      it.setRootDiagram(_xDiagram);
      ObservableList<DomainObjectProvider> _domainObjectProviders = it.getDomainObjectProviders();
      ClassLoaderProvider _classLoaderProvider = new ClassLoaderProvider();
      _domainObjectProviders.add(_classLoaderProvider);
      ObservableList<DomainObjectProvider> _domainObjectProviders_1 = it.getDomainObjectProviders();
      XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
      Iterable<? extends XDiagramConfig> _configurations = _instance.getConfigurations();
      final Function1<XDiagramConfig, IMappedElementDescriptorProvider> _function_1 = (XDiagramConfig it_1) -> {
        return it_1.getDomainObjectProvider();
      };
      Iterable<IMappedElementDescriptorProvider> _map = IterableExtensions.map(_configurations, _function_1);
      Set<IMappedElementDescriptorProvider> _set = IterableExtensions.<IMappedElementDescriptorProvider>toSet(_map);
      Iterables.<DomainObjectProvider>addAll(_domainObjectProviders_1, _set);
      DiagramActionRegistry _diagramActionRegistry = it.getDiagramActionRegistry();
      CenterAction _centerAction = new CenterAction();
      DeleteAction _deleteAction = new DeleteAction();
      LayoutAction _layoutAction = new LayoutAction(LayoutType.DOT);
      ExportSvgAction _exportSvgAction = new ExportSvgAction();
      RedoAction _redoAction = new RedoAction();
      UndoRedoPlayerAction _undoRedoPlayerAction = new UndoRedoPlayerAction();
      UndoAction _undoAction = new UndoAction();
      RevealAction _revealAction = new RevealAction();
      EclipseLoadAction _eclipseLoadAction = new EclipseLoadAction();
      SaveAction _saveAction = new SaveAction();
      SelectAllAction _selectAllAction = new SelectAllAction();
      ZoomToFitAction _zoomToFitAction = new ZoomToFitAction();
      NavigatePreviousAction _navigatePreviousAction = new NavigatePreviousAction();
      NavigateNextAction _navigateNextAction = new NavigateNextAction();
      FullScreenAction _fullScreenAction = new FullScreenAction();
      _diagramActionRegistry.operator_add(
        Collections.<DiagramAction>unmodifiableList(CollectionLiterals.<DiagramAction>newArrayList(_centerAction, _deleteAction, _layoutAction, _exportSvgAction, _redoAction, _undoRedoPlayerAction, _undoAction, _revealAction, _eclipseLoadAction, _saveAction, _selectAllAction, _zoomToFitAction, _navigatePreviousAction, _navigateNextAction, _fullScreenAction)));
    };
    return ObjectExtensions.<XRoot>operator_doubleArrow(_xRoot, _function);
  }
  
  public XRoot getRoot() {
    return this.root;
  }
  
  public CTabItem getCTabItem() {
    return this.tab;
  }
  
  public <T extends Object> void revealElement(final T element, final MappingCall<?, ? super T> mappingCall, final IEditorPart editor) {
    Scene _scene = this.canvas.getScene();
    double _width = _scene.getWidth();
    boolean _equals = (_width == 0);
    if (_equals) {
      Scene _scene_1 = this.canvas.getScene();
      ReadOnlyDoubleProperty _widthProperty = _scene_1.widthProperty();
      final ChangeListener<Number> _function = new ChangeListener<Number>() {
        @Override
        public void changed(final ObservableValue<? extends Number> p, final Number o, final Number n) {
          Scene _scene = FXDiagramTab.this.canvas.getScene();
          ReadOnlyDoubleProperty _widthProperty = _scene.widthProperty();
          _widthProperty.removeListener(this);
          FXDiagramTab.this.<T>revealElement(element, mappingCall, editor);
        }
      };
      _widthProperty.addListener(_function);
    } else {
      Scene _scene_2 = this.canvas.getScene();
      double _height = _scene_2.getHeight();
      boolean _equals_1 = (_height == 0);
      if (_equals_1) {
        Scene _scene_3 = this.canvas.getScene();
        ReadOnlyDoubleProperty _heightProperty = _scene_3.heightProperty();
        final ChangeListener<Number> _function_1 = new ChangeListener<Number>() {
          @Override
          public void changed(final ObservableValue<? extends Number> p, final Number o, final Number n) {
            Scene _scene = FXDiagramTab.this.canvas.getScene();
            ReadOnlyDoubleProperty _heightProperty = _scene.heightProperty();
            _heightProperty.removeListener(this);
            FXDiagramTab.this.<T>revealElement(element, mappingCall, editor);
          }
        };
        _heightProperty.addListener(_function_1);
      } else {
        this.<T>doRevealElement(element, mappingCall, editor);
      }
    }
  }
  
  protected <T extends Object> void doRevealElement(final T element, final MappingCall<?, ? super T> mappingCall, final IEditorPart editor) {
    XDiagram _diagram = this.root.getDiagram();
    final InterpreterContext interpreterContext = new InterpreterContext(_diagram);
    if ((mappingCall instanceof DiagramMappingCall<?, ?>)) {
      interpreterContext.setIsReplaceRootDiagram(true);
      this.configInterpreter.execute(((DiagramMappingCall<?, T>) mappingCall), element, interpreterContext);
      CommandStack _commandStack = this.root.getCommandStack();
      interpreterContext.executeCommands(_commandStack);
    } else {
      if ((mappingCall instanceof NodeMappingCall<?, ?>)) {
        this.configInterpreter.execute(((NodeMappingCall<?, T>) mappingCall), element, interpreterContext);
        CommandStack _commandStack_1 = this.root.getCommandStack();
        interpreterContext.executeCommands(_commandStack_1);
      } else {
        if ((mappingCall instanceof ConnectionMappingCall<?, ?>)) {
          AbstractMapping<?> _mapping = ((ConnectionMappingCall<?, ?>)mappingCall).getMapping();
          final ConnectionMapping<?> mapping = ((ConnectionMapping<?>) _mapping);
          boolean _and = false;
          NodeMappingCall<?, ?> _source = mapping.getSource();
          boolean _notEquals = (!Objects.equal(_source, null));
          if (!_notEquals) {
            _and = false;
          } else {
            NodeMappingCall<?, ?> _target = mapping.getTarget();
            boolean _notEquals_1 = (!Objects.equal(_target, null));
            _and = _notEquals_1;
          }
          if (_and) {
            final Procedure1<XConnection> _function = (XConnection it) -> {
            };
            this.configInterpreter.execute(((ConnectionMappingCall<?, T>) mappingCall), element, _function, interpreterContext);
            CommandStack _commandStack_2 = this.root.getCommandStack();
            interpreterContext.executeCommands(_commandStack_2);
          }
        }
      }
    }
    final IMappedElementDescriptor<T> descriptor = this.<T, Object>createMappedDescriptor(element);
    XDiagram _diagram_1 = interpreterContext.getDiagram();
    ObservableList<XNode> _nodes = _diagram_1.getNodes();
    XDiagram _diagram_2 = interpreterContext.getDiagram();
    ObservableList<XConnection> _connections = _diagram_2.getConnections();
    Iterable<XDomainObjectShape> _plus = Iterables.<XDomainObjectShape>concat(_nodes, _connections);
    final Function1<XDomainObjectShape, Boolean> _function_1 = (XDomainObjectShape it) -> {
      DomainObjectDescriptor _domainObjectDescriptor = it.getDomainObjectDescriptor();
      return Boolean.valueOf(Objects.equal(_domainObjectDescriptor, descriptor));
    };
    final XDomainObjectShape centerShape = IterableExtensions.<XDomainObjectShape>findFirst(_plus, _function_1);
    CommandStack _commandStack_3 = this.root.getCommandStack();
    ParallelAnimationCommand _parallelAnimationCommand = new ParallelAnimationCommand();
    final Procedure1<ParallelAnimationCommand> _function_2 = (ParallelAnimationCommand it) -> {
      boolean _needsLayoutCommand = interpreterContext.needsLayoutCommand();
      if (_needsLayoutCommand) {
        Layouter _layouter = new Layouter();
        XDiagram _diagram_3 = interpreterContext.getDiagram();
        Duration _millis = DurationExtensions.millis(500);
        LazyCommand _createLayoutCommand = _layouter.createLayoutCommand(LayoutType.DOT, _diagram_3, _millis, centerShape);
        it.operator_add(_createLayoutCommand);
      }
      final Function1<XShape, Boolean> _function_3 = (XShape it_1) -> {
        return Boolean.valueOf(Objects.equal(it_1, centerShape));
      };
      SelectAndRevealCommand _selectAndRevealCommand = new SelectAndRevealCommand(this.root, _function_3);
      it.operator_add(_selectAndRevealCommand);
    };
    ParallelAnimationCommand _doubleArrow = ObjectExtensions.<ParallelAnimationCommand>operator_doubleArrow(_parallelAnimationCommand, _function_2);
    _commandStack_3.execute(_doubleArrow);
  }
  
  public void clear() {
    CommandStack _commandStack = this.root.getCommandStack();
    ClearDiagramCommand _clearDiagramCommand = new ClearDiagramCommand();
    _commandStack.execute(_clearDiagramCommand);
  }
  
  public boolean setFocus() {
    return this.canvas.setFocus();
  }
  
  protected <T extends Object, U extends Object> IMappedElementDescriptor<T> createMappedDescriptor(final T domainObject) {
    IMappedElementDescriptor<T> _xblockexpression = null;
    {
      XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
      Iterable<? extends XDiagramConfig> _configurations = _instance.getConfigurations();
      final Function1<XDiagramConfig, Iterable<? extends AbstractMapping<T>>> _function = (XDiagramConfig it) -> {
        return it.<T>getMappings(domainObject);
      };
      Iterable<Iterable<? extends AbstractMapping<T>>> _map = IterableExtensions.map(_configurations, _function);
      Iterable<AbstractMapping<T>> _flatten = Iterables.<AbstractMapping<T>>concat(_map);
      final AbstractMapping<T> mapping = IterableExtensions.<AbstractMapping<T>>head(_flatten);
      XDiagramConfig _config = mapping.getConfig();
      IMappedElementDescriptorProvider _domainObjectProvider = _config.getDomainObjectProvider();
      _xblockexpression = _domainObjectProvider.<T>createMappedElementDescriptor(domainObject, mapping);
    }
    return _xblockexpression;
  }
  
  public void setLinkWithEditor(final boolean linkWithEditor) {
    this.isLinkWithEditor = linkWithEditor;
    this.refreshUpdateState();
  }
  
  protected void refreshUpdateState() {
    final ArrayList<XShape> allShapes = CollectionLiterals.<XShape>newArrayList();
    XDiagram _diagram = this.root.getDiagram();
    ObservableList<XNode> _nodes = _diagram.getNodes();
    Iterables.<XShape>addAll(allShapes, _nodes);
    XDiagram _diagram_1 = this.root.getDiagram();
    ObservableList<XConnection> _connections = _diagram_1.getConnections();
    Iterables.<XShape>addAll(allShapes, _connections);
    final Consumer<XShape> _function = (XShape it) -> {
      final ReconcileBehavior behavior = it.<ReconcileBehavior>getBehavior(ReconcileBehavior.class);
      boolean _notEquals = (!Objects.equal(behavior, null));
      if (_notEquals) {
        if (this.isLinkWithEditor) {
          DirtyState _dirtyState = behavior.getDirtyState();
          behavior.showDirtyState(_dirtyState);
        } else {
          behavior.hideDirtyState();
        }
      }
    };
    allShapes.forEach(_function);
  }
}
