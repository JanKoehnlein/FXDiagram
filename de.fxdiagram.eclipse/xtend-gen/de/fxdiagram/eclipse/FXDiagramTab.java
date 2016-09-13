package de.fxdiagram.eclipse;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.behavior.ReconcileBehavior;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.LazyCommand;
import de.fxdiagram.core.command.ParallelAnimationCommand;
import de.fxdiagram.core.command.SelectAndRevealCommand;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.extensions.InitializingListener;
import de.fxdiagram.core.layout.LayoutParameters;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.eclipse.ClearDiagramCommand;
import de.fxdiagram.eclipse.FXDiagramView;
import de.fxdiagram.eclipse.actions.EclipseSaveAction;
import de.fxdiagram.eclipse.changes.IChangeListener;
import de.fxdiagram.eclipse.changes.ModelChangeBroker;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.EntryCall;
import de.fxdiagram.mapping.execution.InterpreterContext;
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter;
import de.fxdiagram.swtfx.SwtToFXGestureConverter;
import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@Logging
@SuppressWarnings("all")
public class FXDiagramTab {
  private final CTabItem tab;
  
  private final FXCanvas canvas;
  
  private final SwtToFXGestureConverter gestureConverter;
  
  private final XRoot root;
  
  private final XDiagramConfigInterpreter configInterpreter = new XDiagramConfigInterpreter();
  
  private boolean isLinkWithEditor;
  
  private boolean dontSave = false;
  
  private IChangeListener changeListener;
  
  public FXDiagramTab(final FXDiagramView view, final CTabFolder tabFolder, final XRoot root) {
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
    this.root = root;
    Scene _scene = new Scene(root);
    final Procedure1<Scene> _function_1 = (Scene it) -> {
      PerspectiveCamera _perspectiveCamera = new PerspectiveCamera();
      it.setCamera(_perspectiveCamera);
      root.activate();
    };
    Scene _doubleArrow = ObjectExtensions.<Scene>operator_doubleArrow(_scene, _function_1);
    this.canvas.setScene(_doubleArrow);
    final DisposeListener _function_2 = (DisposeEvent it) -> {
      this.gestureConverter.dispose();
      ModelChangeBroker _modelChangeBroker_1 = view.getModelChangeBroker();
      _modelChangeBroker_1.removeListener(this.changeListener);
      view.removeTab(this.tab);
    };
    this.tab.addDisposeListener(_function_2);
    StringProperty _fileNameProperty = root.fileNameProperty();
    InitializingListener<String> _initializingListener = new InitializingListener<String>();
    final Procedure1<InitializingListener<String>> _function_3 = (InitializingListener<String> it) -> {
      final Procedure1<String> _function_4 = (String it_1) -> {
        boolean _isDisposed = this.tab.isDisposed();
        boolean _not = (!_isDisposed);
        if (_not) {
          String _xifexpression = null;
          boolean _needsSave = root.getNeedsSave();
          if (_needsSave) {
            _xifexpression = "*";
          } else {
            _xifexpression = "";
          }
          String _tabName = this.getTabName();
          String _plus = (_xifexpression + _tabName);
          this.tab.setText(_plus);
        }
      };
      it.setSet(_function_4);
    };
    InitializingListener<String> _doubleArrow_1 = ObjectExtensions.<InitializingListener<String>>operator_doubleArrow(_initializingListener, _function_3);
    CoreExtensions.<String>addInitializingListener(_fileNameProperty, _doubleArrow_1);
    BooleanProperty _needsSaveProperty = root.needsSaveProperty();
    final ChangeListener<Boolean> _function_4 = (ObservableValue<? extends Boolean> p, Boolean o, Boolean n) -> {
      boolean _isDisposed = this.tab.isDisposed();
      boolean _not = (!_isDisposed);
      if (_not) {
        String _xifexpression = null;
        boolean _needsSave = root.getNeedsSave();
        if (_needsSave) {
          _xifexpression = "*";
        } else {
          _xifexpression = "";
        }
        String _tabName = this.getTabName();
        String _plus = (_xifexpression + _tabName);
        this.tab.setText(_plus);
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
  
  public boolean confirmClose() {
    if (((!this.root.getNeedsSave()) || this.dontSave)) {
      return true;
    }
    CTabFolder _parent = this.tab.getParent();
    Shell _shell = _parent.getShell();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\'");
    String _tabName = this.getTabName();
    _builder.append(_tabName, "");
    _builder.append("\' has been modified.");
    _builder.newLineIfNotEmpty();
    _builder.append("Save changes?");
    _builder.newLine();
    final MessageDialog dialog = new MessageDialog(_shell, 
      "Save diagram", 
      null, _builder.toString(), 
      MessageDialog.QUESTION_WITH_CANCEL, 
      new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.CANCEL_LABEL }, 
      SWT.NONE);
    int _open = dialog.open();
    switch (_open) {
      case 0:
        EclipseSaveAction _eclipseSaveAction = new EclipseSaveAction();
        _eclipseSaveAction.doSave(this.root);
        return true;
      case 1:
        this.dontSave = true;
        return true;
      default:
        return false;
    }
  }
  
  protected String getTabName() {
    String _fileName = this.root.getFileName();
    String[] _split = null;
    if (_fileName!=null) {
      String _quote = Pattern.quote(File.separator);
      _split=_fileName.split(_quote);
    }
    String _last = null;
    if (((Iterable<String>)Conversions.doWrapArray(_split))!=null) {
      _last=IterableExtensions.<String>last(((Iterable<String>)Conversions.doWrapArray(_split)));
    }
    final String fileName = _last;
    boolean _equals = Objects.equal(fileName, null);
    if (_equals) {
      return "Untitled";
    }
    final int dotPos = fileName.lastIndexOf(".");
    if ((dotPos >= 0)) {
      return fileName.substring(0, dotPos);
    } else {
      return fileName;
    }
  }
  
  public XRoot getRoot() {
    return this.root;
  }
  
  public CTabItem getCTabItem() {
    return this.tab;
  }
  
  public <T extends Object> void revealElement(final T element, final EntryCall<? super T> entryCall, final IEditorPart editor) {
    final Runnable _function = () -> {
      try {
        Scene _scene = this.canvas.getScene();
        double _width = _scene.getWidth();
        boolean _equals = (_width == 0);
        if (_equals) {
          Scene _scene_1 = this.canvas.getScene();
          ReadOnlyDoubleProperty _widthProperty = _scene_1.widthProperty();
          final ChangeListener<Number> _function_1 = new ChangeListener<Number>() {
            @Override
            public void changed(final ObservableValue<? extends Number> p, final Number o, final Number n) {
              Scene _scene = FXDiagramTab.this.canvas.getScene();
              ReadOnlyDoubleProperty _widthProperty = _scene.widthProperty();
              _widthProperty.removeListener(this);
              FXDiagramTab.this.<T>revealElement(element, entryCall, editor);
            }
          };
          _widthProperty.addListener(_function_1);
        } else {
          Scene _scene_2 = this.canvas.getScene();
          double _height = _scene_2.getHeight();
          boolean _equals_1 = (_height == 0);
          if (_equals_1) {
            Scene _scene_3 = this.canvas.getScene();
            ReadOnlyDoubleProperty _heightProperty = _scene_3.heightProperty();
            final ChangeListener<Number> _function_2 = new ChangeListener<Number>() {
              @Override
              public void changed(final ObservableValue<? extends Number> p, final Number o, final Number n) {
                Scene _scene = FXDiagramTab.this.canvas.getScene();
                ReadOnlyDoubleProperty _heightProperty = _scene.heightProperty();
                _heightProperty.removeListener(this);
                FXDiagramTab.this.<T>revealElement(element, entryCall, editor);
              }
            };
            _heightProperty.addListener(_function_2);
          } else {
            this.<T>doRevealElement(element, entryCall, editor);
          }
        }
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception exc = (Exception)_t;
          exc.printStackTrace();
          Display _current = Display.getCurrent();
          Shell _activeShell = _current.getActiveShell();
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("Error showing element in FXDiagram:");
          _builder.newLine();
          String _message = exc.getMessage();
          _builder.append(_message, "");
          _builder.newLineIfNotEmpty();
          _builder.append("See log for details.");
          _builder.newLine();
          MessageDialog.openError(_activeShell, "Error", _builder.toString());
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    };
    Platform.runLater(_function);
  }
  
  protected <T extends Object> void doRevealElement(final T element, final EntryCall<? super T> entryCall, final IEditorPart editor) {
    XDiagram _diagram = this.root.getDiagram();
    final InterpreterContext interpreterContext = new InterpreterContext(_diagram);
    entryCall.execute(element, this.configInterpreter, interpreterContext);
    CommandStack _commandStack = this.root.getCommandStack();
    interpreterContext.executeCommands(_commandStack);
    XDiagram _diagram_1 = interpreterContext.getDiagram();
    final XDomainObjectShape centerShape = this.<T, Object>findShape(_diagram_1, element);
    CommandStack _commandStack_1 = this.root.getCommandStack();
    ParallelAnimationCommand _parallelAnimationCommand = new ParallelAnimationCommand();
    final Procedure1<ParallelAnimationCommand> _function = (ParallelAnimationCommand it) -> {
      boolean _needsLayoutCommand = interpreterContext.needsLayoutCommand();
      if (_needsLayoutCommand) {
        Layouter _layouter = new Layouter();
        XDiagram _diagram_2 = this.root.getDiagram();
        LayoutParameters _layoutParameters = _diagram_2.getLayoutParameters();
        XDiagram _diagram_3 = interpreterContext.getDiagram();
        Duration _millis = DurationExtensions.millis(500);
        LazyCommand _createLayoutCommand = _layouter.createLayoutCommand(_layoutParameters, _diagram_3, _millis, centerShape);
        it.operator_add(_createLayoutCommand);
      }
      final Function1<XShape, Boolean> _function_1 = (XShape it_1) -> {
        return Boolean.valueOf(Objects.equal(it_1, centerShape));
      };
      SelectAndRevealCommand _selectAndRevealCommand = new SelectAndRevealCommand(this.root, _function_1);
      it.operator_add(_selectAndRevealCommand);
    };
    ParallelAnimationCommand _doubleArrow = ObjectExtensions.<ParallelAnimationCommand>operator_doubleArrow(_parallelAnimationCommand, _function);
    _commandStack_1.execute(_doubleArrow);
  }
  
  public void clear() {
    XDiagram _diagram = this.root.getDiagram();
    DomainObjectDescriptor _domainObjectDescriptor = _diagram.getDomainObjectDescriptor();
    if ((_domainObjectDescriptor instanceof IMappedElementDescriptor<?>)) {
      CommandStack _commandStack = this.root.getCommandStack();
      XDiagram _diagram_1 = this.root.getDiagram();
      XDiagram _diagram_2 = this.root.getDiagram();
      Iterable<XShape> _allShapes = _diagram_2.getAllShapes();
      AddRemoveCommand _newRemoveCommand = AddRemoveCommand.newRemoveCommand(_diagram_1, ((XShape[])Conversions.unwrapArray(_allShapes, XShape.class)));
      _commandStack.execute(_newRemoveCommand);
    } else {
      CommandStack _commandStack_1 = this.root.getCommandStack();
      ClearDiagramCommand _clearDiagramCommand = new ClearDiagramCommand();
      _commandStack_1.execute(_clearDiagramCommand);
    }
  }
  
  public boolean setFocus() {
    return this.canvas.setFocus();
  }
  
  protected <T extends Object, U extends Object> XDomainObjectShape findShape(final XDiagram diagram, final T domainObject) {
    XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
    Iterable<? extends XDiagramConfig> _configurations = _instance.getConfigurations();
    final Function1<XDiagramConfig, Iterable<? extends AbstractMapping<?>>> _function = (XDiagramConfig it) -> {
      return it.getMappings();
    };
    Iterable<Iterable<? extends AbstractMapping<?>>> _map = IterableExtensions.map(_configurations, _function);
    Iterable<AbstractMapping<?>> _flatten = Iterables.<AbstractMapping<?>>concat(_map);
    final Function1<AbstractMapping<?>, XDomainObjectShape> _function_1 = (AbstractMapping<?> it) -> {
      XDomainObjectShape _xblockexpression = null;
      {
        XDiagramConfig _config = it.getConfig();
        IMappedElementDescriptorProvider _domainObjectProvider = _config.getDomainObjectProvider();
        final IMappedElementDescriptor<Object> descriptor = _domainObjectProvider.<Object>createMappedElementDescriptor(domainObject, it);
        ObservableList<XNode> _nodes = diagram.getNodes();
        ObservableList<XConnection> _connections = diagram.getConnections();
        Iterable<XDomainObjectShape> _plus = Iterables.<XDomainObjectShape>concat(_nodes, _connections);
        final Function1<XDomainObjectShape, Boolean> _function_2 = (XDomainObjectShape it_1) -> {
          DomainObjectDescriptor _domainObjectDescriptor = it_1.getDomainObjectDescriptor();
          return Boolean.valueOf(Objects.equal(_domainObjectDescriptor, descriptor));
        };
        _xblockexpression = IterableExtensions.<XDomainObjectShape>findFirst(_plus, _function_2);
      }
      return _xblockexpression;
    };
    Iterable<XDomainObjectShape> _map_1 = IterableExtensions.<AbstractMapping<?>, XDomainObjectShape>map(_flatten, _function_1);
    Iterable<XDomainObjectShape> _filterNull = IterableExtensions.<XDomainObjectShape>filterNull(_map_1);
    return IterableExtensions.<XDomainObjectShape>head(_filterNull);
  }
  
  public void setLinkWithEditor(final boolean linkWithEditor) {
    this.isLinkWithEditor = linkWithEditor;
    this.refreshUpdateState();
  }
  
  protected void refreshUpdateState() {
    XDiagram _diagram = this.root.getDiagram();
    final ReconcileBehavior behavior = _diagram.<ReconcileBehavior>getBehavior(ReconcileBehavior.class);
    boolean _notEquals = (!Objects.equal(behavior, null));
    if (_notEquals) {
      this.refreshDirtyState(behavior);
    }
    final ArrayList<XShape> allShapes = CollectionLiterals.<XShape>newArrayList();
    XDiagram _diagram_1 = this.root.getDiagram();
    ObservableList<XNode> _nodes = _diagram_1.getNodes();
    Iterables.<XShape>addAll(allShapes, _nodes);
    XDiagram _diagram_2 = this.root.getDiagram();
    ObservableList<XConnection> _connections = _diagram_2.getConnections();
    Iterables.<XShape>addAll(allShapes, _connections);
    final Consumer<XShape> _function = (XShape it) -> {
      ReconcileBehavior _behavior = it.<ReconcileBehavior>getBehavior(ReconcileBehavior.class);
      if (_behavior!=null) {
        this.refreshDirtyState(_behavior);
      }
    };
    allShapes.forEach(_function);
  }
  
  protected void refreshDirtyState(final ReconcileBehavior behavior) {
    Display _default = Display.getDefault();
    final Runnable _function = () -> {
      if (this.isLinkWithEditor) {
        DirtyState _dirtyState = behavior.getDirtyState();
        behavior.showDirtyState(_dirtyState);
      } else {
        behavior.hideDirtyState();
      }
    };
    _default.asyncExec(_function);
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.eclipse.FXDiagramTab");
    ;
}
