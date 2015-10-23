package de.fxdiagram.eclipse;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelLoad;
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
import de.fxdiagram.core.tools.actions.ReconcileAction;
import de.fxdiagram.core.tools.actions.RedoAction;
import de.fxdiagram.core.tools.actions.RevealAction;
import de.fxdiagram.core.tools.actions.SelectAllAction;
import de.fxdiagram.core.tools.actions.UndoAction;
import de.fxdiagram.core.tools.actions.ZoomToFitAction;
import de.fxdiagram.eclipse.FXDiagramTab;
import de.fxdiagram.eclipse.actions.EclipseLoadAction;
import de.fxdiagram.eclipse.actions.EclipseSaveAction;
import de.fxdiagram.eclipse.changes.ModelChangeBroker;
import de.fxdiagram.lib.actions.UndoRedoPlayerAction;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.DiagramMapping;
import de.fxdiagram.mapping.DiagramMappingCall;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.DiagramEntryCall;
import de.fxdiagram.mapping.execution.EntryCall;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swt.FXCanvas;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.State;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Embeds an {@link FXCanvas} with an {@link XRoot} in an eclipse {@link ViewPart}.
 * 
 * Uses {@link AbstractMapping} API to map domain objects to diagram elements.
 */
@Logging
@SuppressWarnings("all")
public class FXDiagramView extends ViewPart {
  private CTabFolder tabFolder;
  
  private Map<CTabItem, FXDiagramTab> tab2content = CollectionLiterals.<CTabItem, FXDiagramTab>newHashMap();
  
  private List<Pair<EventType<?>, EventHandler<?>>> globalEventHandlers = CollectionLiterals.<Pair<EventType<?>, EventHandler<?>>>newArrayList();
  
  @Accessors
  private boolean linkWithEditor;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private ModelChangeBroker modelChangeBroker;
  
  @Override
  public void createPartControl(final Composite parent) {
    CTabFolder _cTabFolder = new CTabFolder(parent, (SWT.BORDER + SWT.BOTTOM));
    this.tabFolder = _cTabFolder;
    Display _display = parent.getDisplay();
    Color _systemColor = _display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
    this.tabFolder.setBackground(_systemColor);
    IWorkbench _workbench = PlatformUI.getWorkbench();
    ModelChangeBroker _modelChangeBroker = new ModelChangeBroker(_workbench);
    this.modelChangeBroker = _modelChangeBroker;
    IWorkbenchPartSite _site = this.getSite();
    ICommandService _service = _site.<ICommandService>getService(ICommandService.class);
    final Command command = _service.getCommand("de.fxdiagram.eclipse.LinkWithEditor");
    Boolean _elvis = null;
    State _state = command.getState(RegistryToggleState.STATE_ID);
    Object _value = null;
    if (_state!=null) {
      _value=_state.getValue();
    }
    if (((Boolean) _value) != null) {
      _elvis = ((Boolean) _value);
    } else {
      _elvis = Boolean.valueOf(false);
    }
    this.linkWithEditor = (_elvis).booleanValue();
  }
  
  public FXDiagramTab createNewTab() {
    FXDiagramTab _xblockexpression = null;
    {
      XRoot _createRoot = this.createRoot();
      final FXDiagramTab diagramTab = new FXDiagramTab(this, this.tabFolder, _createRoot);
      CTabItem _cTabItem = diagramTab.getCTabItem();
      this.tab2content.put(_cTabItem, diagramTab);
      CTabItem _cTabItem_1 = diagramTab.getCTabItem();
      this.tabFolder.setSelection(_cTabItem_1);
      final Consumer<Pair<EventType<?>, EventHandler<?>>> _function = (Pair<EventType<?>, EventHandler<?>> it) -> {
        XRoot _root = diagramTab.getRoot();
        EventType<?> _key = it.getKey();
        EventHandler<?> _value = it.getValue();
        this.addEventHandlerWrapper(_root, ((EventType<? extends Event>) _key), _value);
      };
      this.globalEventHandlers.forEach(_function);
      diagramTab.setLinkWithEditor(this.linkWithEditor);
      _xblockexpression = diagramTab;
    }
    return _xblockexpression;
  }
  
  public FXDiagramTab removeTab(final CTabItem tab) {
    return this.tab2content.remove(tab);
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
      EclipseSaveAction _eclipseSaveAction = new EclipseSaveAction();
      ReconcileAction _reconcileAction = new ReconcileAction();
      SelectAllAction _selectAllAction = new SelectAllAction();
      ZoomToFitAction _zoomToFitAction = new ZoomToFitAction();
      NavigatePreviousAction _navigatePreviousAction = new NavigatePreviousAction();
      NavigateNextAction _navigateNextAction = new NavigateNextAction();
      FullScreenAction _fullScreenAction = new FullScreenAction();
      _diagramActionRegistry.operator_add(
        Collections.<DiagramAction>unmodifiableList(CollectionLiterals.<DiagramAction>newArrayList(_centerAction, _deleteAction, _layoutAction, _exportSvgAction, _redoAction, _undoRedoPlayerAction, _undoAction, _revealAction, _eclipseLoadAction, _eclipseSaveAction, _reconcileAction, _selectAllAction, _zoomToFitAction, _navigatePreviousAction, _navigateNextAction, _fullScreenAction)));
    };
    return ObjectExtensions.<XRoot>operator_doubleArrow(_xRoot, _function);
  }
  
  public void setLinkWithEditor(final boolean linkWithEditor) {
    this.linkWithEditor = linkWithEditor;
    Collection<FXDiagramTab> _values = this.tab2content.values();
    final Consumer<FXDiagramTab> _function = (FXDiagramTab it) -> {
      it.setLinkWithEditor(linkWithEditor);
    };
    _values.forEach(_function);
  }
  
  private <T extends Event> void addEventHandlerWrapper(final XRoot root, final EventType<T> eventType, final EventHandler<?> handler) {
    root.<T>addEventHandler(eventType, ((EventHandler<? super T>) handler));
  }
  
  public <T extends Event> void addGlobalEventHandler(final EventType<T> eventType, final EventHandler<? super T> eventHandler) {
    Pair<EventType<?>, EventHandler<?>> _mappedTo = Pair.<EventType<?>, EventHandler<?>>of(eventType, eventHandler);
    this.globalEventHandlers.add(_mappedTo);
    Collection<FXDiagramTab> _values = this.tab2content.values();
    final Consumer<FXDiagramTab> _function = (FXDiagramTab it) -> {
      XRoot _root = it.getRoot();
      _root.<T>addEventHandler(eventType, eventHandler);
    };
    _values.forEach(_function);
  }
  
  public <T extends Event> void removeGlobalEventHandler(final EventType<T> eventType, final EventHandler<? super T> eventHandler) {
    Pair<EventType<T>, EventHandler<? super T>> _mappedTo = Pair.<EventType<T>, EventHandler<? super T>>of(eventType, eventHandler);
    this.globalEventHandlers.remove(_mappedTo);
    Collection<FXDiagramTab> _values = this.tab2content.values();
    final Consumer<FXDiagramTab> _function = (FXDiagramTab it) -> {
      XRoot _root = it.getRoot();
      _root.<T>removeEventHandler(eventType, eventHandler);
    };
    _values.forEach(_function);
  }
  
  protected FXDiagramTab getCurrentDiagramTab() {
    FXDiagramTab _xblockexpression = null;
    {
      final CTabItem currentTab = this.tabFolder.getSelection();
      FXDiagramTab _xifexpression = null;
      boolean _notEquals = (!Objects.equal(currentTab, null));
      if (_notEquals) {
        _xifexpression = this.tab2content.get(currentTab);
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public XRoot getCurrentRoot() {
    FXDiagramTab _elvis = null;
    FXDiagramTab _currentDiagramTab = this.getCurrentDiagramTab();
    if (_currentDiagramTab != null) {
      _elvis = _currentDiagramTab;
    } else {
      FXDiagramTab _createNewTab = this.createNewTab();
      _elvis = _createNewTab;
    }
    return _elvis.getRoot();
  }
  
  @Override
  public void setFocus() {
    FXDiagramTab _currentDiagramTab = this.getCurrentDiagramTab();
    if (_currentDiagramTab!=null) {
      _currentDiagramTab.setFocus();
    }
  }
  
  public void clear() {
    FXDiagramTab _currentDiagramTab = this.getCurrentDiagramTab();
    if (_currentDiagramTab!=null) {
      _currentDiagramTab.clear();
    }
  }
  
  public <T extends Object> void revealElement(final T element, final EntryCall<? super T> entryCall, final IEditorPart editor) {
    try {
      if ((entryCall instanceof DiagramEntryCall<?, ?>)) {
        final DiagramMappingCall<?, T> mappingCall = ((DiagramEntryCall<?, T>) entryCall).getMappingCall();
        Function1<? super T, ?> _selector = mappingCall.getSelector();
        final Object diagramElement = _selector.apply(element);
        XDiagramConfig _config = ((DiagramEntryCall<?, ?>)entryCall).getConfig();
        IMappedElementDescriptorProvider _domainObjectProvider = _config.getDomainObjectProvider();
        AbstractMapping<?> _mapping = mappingCall.getMapping();
        final IMappedElementDescriptor<Object> diagramDescriptor = _domainObjectProvider.<Object>createMappedElementDescriptor(diagramElement, _mapping);
        Set<Map.Entry<CTabItem, FXDiagramTab>> _entrySet = this.tab2content.entrySet();
        final Function1<Map.Entry<CTabItem, FXDiagramTab>, Boolean> _function = (Map.Entry<CTabItem, FXDiagramTab> it) -> {
          FXDiagramTab _value = it.getValue();
          XRoot _root = _value.getRoot();
          XDiagram _diagram = _root.getDiagram();
          DomainObjectDescriptor _domainObjectDescriptor = _diagram.getDomainObjectDescriptor();
          return Boolean.valueOf(Objects.equal(_domainObjectDescriptor, diagramDescriptor));
        };
        final Map.Entry<CTabItem, FXDiagramTab> tab = IterableExtensions.<Map.Entry<CTabItem, FXDiagramTab>>findFirst(_entrySet, _function);
        boolean _notEquals = (!Objects.equal(tab, null));
        if (_notEquals) {
          FXDiagramTab _value = tab.getValue();
          _value.<T>revealElement(element, entryCall, editor);
          CTabItem _key = tab.getKey();
          this.tabFolder.setSelection(_key);
          return;
        }
        final FXDiagramTab newTab = this.createNewTab();
        AbstractMapping<?> _mapping_1 = mappingCall.getMapping();
        final String filePath = ((DiagramMapping<Object>) _mapping_1).getDefaultFilePath(diagramElement);
        boolean _notEquals_1 = (!Objects.equal(filePath, null));
        if (_notEquals_1) {
          IWorkspace _workspace = ResourcesPlugin.getWorkspace();
          IWorkspaceRoot _root = _workspace.getRoot();
          Path _path = new Path(filePath);
          final IFile file = _root.getFile(_path);
          boolean _exists = file.exists();
          if (_exists) {
            ModelLoad _modelLoad = new ModelLoad();
            InputStream _contents = file.getContents();
            InputStreamReader _inputStreamReader = new InputStreamReader(_contents);
            final Object node = _modelLoad.load(_inputStreamReader);
            if ((node instanceof XRoot)) {
              final Runnable _function_1 = () -> {
                try {
                  XRoot _root_1 = newTab.getRoot();
                  ObservableList<DomainObjectProvider> _domainObjectProviders = ((XRoot)node).getDomainObjectProviders();
                  _root_1.replaceDomainObjectProviders(_domainObjectProviders);
                  XRoot _root_2 = newTab.getRoot();
                  XDiagram _diagram = ((XRoot)node).getDiagram();
                  _root_2.setRootDiagram(_diagram);
                  XRoot _root_3 = newTab.getRoot();
                  _root_3.setFileName(filePath);
                } catch (final Throwable _t) {
                  if (_t instanceof Exception) {
                    final Exception exc = (Exception)_t;
                    String _message = exc.getMessage();
                    FXDiagramView.LOG.severe(_message);
                    exc.printStackTrace();
                  } else {
                    throw Exceptions.sneakyThrow(_t);
                  }
                }
              };
              Platform.runLater(_function_1);
              return;
            }
          }
        }
        final Procedure1<FXDiagramTab> _function_2 = (FXDiagramTab it) -> {
          XRoot _root_1 = it.getRoot();
          _root_1.setFileName(filePath);
          it.<T>revealElement(element, entryCall, editor);
        };
        ObjectExtensions.<FXDiagramTab>operator_doubleArrow(newTab, _function_2);
        return;
      }
      FXDiagramTab _elvis = null;
      FXDiagramTab _currentDiagramTab = this.getCurrentDiagramTab();
      if (_currentDiagramTab != null) {
        _elvis = _currentDiagramTab;
      } else {
        FXDiagramTab _createNewTab = this.createNewTab();
        _elvis = _createNewTab;
      }
      _elvis.<T>revealElement(element, entryCall, editor);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.eclipse.FXDiagramView");
    ;
  
  @Pure
  public boolean isLinkWithEditor() {
    return this.linkWithEditor;
  }
  
  @Pure
  public ModelChangeBroker getModelChangeBroker() {
    return this.modelChangeBroker;
  }
}
