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
import de.fxdiagram.core.behavior.ReconcileBehavior;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.LazyCommand;
import de.fxdiagram.core.command.ParallelAnimationCommand;
import de.fxdiagram.core.command.SelectAndRevealCommand;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.extensions.InitializingListener;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.eclipse.ClearDiagramCommand;
import de.fxdiagram.eclipse.FXDiagramView;
import de.fxdiagram.eclipse.actions.EclipseSaveAction;
import de.fxdiagram.eclipse.changes.IChangeListener;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
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
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swt.FXCanvas;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
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
    view.getModelChangeBroker().addListener(this.changeListener);
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
      view.getModelChangeBroker().removeListener(this.changeListener);
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
    root.needsSaveProperty().addListener(_function_4);
    this.canvas.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(final FocusEvent e) {
        IBindingService _service = view.getSite().<IBindingService>getService(IBindingService.class);
        ((IBindingService) _service).setKeyFilterEnabled(false);
      }
      
      @Override
      public void focusLost(final FocusEvent e) {
        IBindingService _service = view.getSite().<IBindingService>getService(IBindingService.class);
        ((IBindingService) _service).setKeyFilterEnabled(true);
      }
    });
  }
  
  public boolean confirmClose() {
    if (((!this.root.getNeedsSave()) || this.dontSave)) {
      return true;
    }
    Shell _shell = this.tab.getParent().getShell();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Diagram \'");
    String _tabName = this.getTabName();
    _builder.append(_tabName);
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
        new EclipseSaveAction().doSave(this.root);
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
      _split=_fileName.split(Pattern.quote(File.separator));
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
        double _width = this.canvas.getScene().getWidth();
        boolean _equals = (_width == 0);
        if (_equals) {
          final ChangeListener<Number> _function_1 = new ChangeListener<Number>() {
            @Override
            public void changed(final ObservableValue<? extends Number> p, final Number o, final Number n) {
              FXDiagramTab.this.canvas.getScene().widthProperty().removeListener(this);
              FXDiagramTab.this.<T>revealElement(element, entryCall, editor);
            }
          };
          this.canvas.getScene().widthProperty().addListener(_function_1);
        } else {
          double _height = this.canvas.getScene().getHeight();
          boolean _equals_1 = (_height == 0);
          if (_equals_1) {
            final ChangeListener<Number> _function_2 = new ChangeListener<Number>() {
              @Override
              public void changed(final ObservableValue<? extends Number> p, final Number o, final Number n) {
                FXDiagramTab.this.canvas.getScene().heightProperty().removeListener(this);
                FXDiagramTab.this.<T>revealElement(element, entryCall, editor);
              }
            };
            this.canvas.getScene().heightProperty().addListener(_function_2);
          } else {
            this.<T>doRevealElement(element, entryCall, editor);
          }
        }
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception exc = (Exception)_t;
          exc.printStackTrace();
          Shell _activeShell = Display.getCurrent().getActiveShell();
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("Error showing element in FXDiagram:");
          _builder.newLine();
          String _message = exc.getMessage();
          _builder.append(_message);
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
    interpreterContext.executeCommands(this.root.getCommandStack());
    final XDomainObjectShape centerShape = this.<T, Object>findShape(interpreterContext.getDiagram(), element);
    CommandStack _commandStack = this.root.getCommandStack();
    ParallelAnimationCommand _parallelAnimationCommand = new ParallelAnimationCommand();
    final Procedure1<ParallelAnimationCommand> _function = (ParallelAnimationCommand it) -> {
      boolean _needsLayoutCommand = interpreterContext.needsLayoutCommand();
      if (_needsLayoutCommand) {
        LazyCommand _createLayoutCommand = new Layouter().createLayoutCommand(this.root.getDiagram().getLayoutParameters(), interpreterContext.getDiagram(), DurationExtensions.millis(500), centerShape);
        it.operator_add(_createLayoutCommand);
      }
      final Function1<XShape, Boolean> _function_1 = (XShape it_1) -> {
        return Boolean.valueOf(Objects.equal(it_1, centerShape));
      };
      SelectAndRevealCommand _selectAndRevealCommand = new SelectAndRevealCommand(this.root, _function_1);
      it.operator_add(_selectAndRevealCommand);
    };
    ParallelAnimationCommand _doubleArrow = ObjectExtensions.<ParallelAnimationCommand>operator_doubleArrow(_parallelAnimationCommand, _function);
    _commandStack.execute(_doubleArrow);
  }
  
  public void clear() {
    DomainObjectDescriptor _domainObjectDescriptor = this.root.getDiagram().getDomainObjectDescriptor();
    if ((_domainObjectDescriptor instanceof IMappedElementDescriptor<?>)) {
      this.root.getCommandStack().execute(AddRemoveCommand.newRemoveCommand(this.root.getDiagram(), ((XShape[])Conversions.unwrapArray(this.root.getDiagram().getAllShapes(), XShape.class))));
    } else {
      CommandStack _commandStack = this.root.getCommandStack();
      ClearDiagramCommand _clearDiagramCommand = new ClearDiagramCommand();
      _commandStack.execute(_clearDiagramCommand);
    }
  }
  
  public boolean setFocus() {
    return this.canvas.setFocus();
  }
  
  protected <T extends Object, U extends Object> XDomainObjectShape findShape(final XDiagram diagram, final T domainObject) {
    final Function1<XDiagramConfig, Iterable<? extends AbstractMapping<?>>> _function = (XDiagramConfig it) -> {
      return it.getMappings();
    };
    final Function1<AbstractMapping<?>, XDomainObjectShape> _function_1 = (AbstractMapping<?> it) -> {
      XDomainObjectShape _xblockexpression = null;
      {
        final IMappedElementDescriptor<Object> descriptor = it.getConfig().getDomainObjectProvider().<Object>createMappedElementDescriptor(domainObject, it);
        ObservableList<XNode> _nodes = diagram.getNodes();
        ObservableList<XConnection> _connections = diagram.getConnections();
        final Function1<XDomainObjectShape, Boolean> _function_2 = (XDomainObjectShape it_1) -> {
          DomainObjectDescriptor _domainObjectDescriptor = it_1.getDomainObjectDescriptor();
          return Boolean.valueOf(Objects.equal(_domainObjectDescriptor, descriptor));
        };
        _xblockexpression = IterableExtensions.<XDomainObjectShape>findFirst(Iterables.<XDomainObjectShape>concat(_nodes, _connections), _function_2);
      }
      return _xblockexpression;
    };
    return IterableExtensions.<XDomainObjectShape>head(IterableExtensions.<XDomainObjectShape>filterNull(IterableExtensions.<AbstractMapping<?>, XDomainObjectShape>map(Iterables.<AbstractMapping<?>>concat(IterableExtensions.map(XDiagramConfig.Registry.getInstance().getConfigurations(), _function)), _function_1)));
  }
  
  public void setLinkWithEditor(final boolean linkWithEditor) {
    this.isLinkWithEditor = linkWithEditor;
    this.refreshUpdateState();
  }
  
  protected void refreshUpdateState() {
    final ReconcileBehavior behavior = this.root.getDiagram().<ReconcileBehavior>getBehavior(ReconcileBehavior.class);
    boolean _notEquals = (!Objects.equal(behavior, null));
    if (_notEquals) {
      this.refreshDirtyState(behavior);
    }
    final ArrayList<XShape> allShapes = CollectionLiterals.<XShape>newArrayList();
    ObservableList<XNode> _nodes = this.root.getDiagram().getNodes();
    Iterables.<XShape>addAll(allShapes, _nodes);
    ObservableList<XConnection> _connections = this.root.getDiagram().getConnections();
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
    final Runnable _function = () -> {
      if (this.isLinkWithEditor) {
        behavior.showDirtyState(behavior.getDirtyState());
      } else {
        behavior.hideDirtyState();
      }
    };
    Display.getDefault().asyncExec(_function);
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.eclipse.FXDiagramTab");
    ;
}
