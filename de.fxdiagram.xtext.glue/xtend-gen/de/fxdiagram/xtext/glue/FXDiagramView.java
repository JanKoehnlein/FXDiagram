package de.fxdiagram.xtext.glue;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.services.ResourceProvider;
import de.fxdiagram.core.tools.actions.CenterAction;
import de.fxdiagram.core.tools.actions.DeleteAction;
import de.fxdiagram.core.tools.actions.DiagramAction;
import de.fxdiagram.core.tools.actions.DiagramActionRegistry;
import de.fxdiagram.core.tools.actions.ExportSvgAction;
import de.fxdiagram.core.tools.actions.FullScreenAction;
import de.fxdiagram.core.tools.actions.LayoutAction;
import de.fxdiagram.core.tools.actions.LoadAction;
import de.fxdiagram.core.tools.actions.NavigateNextAction;
import de.fxdiagram.core.tools.actions.NavigatePreviousAction;
import de.fxdiagram.core.tools.actions.RedoAction;
import de.fxdiagram.core.tools.actions.SaveAction;
import de.fxdiagram.core.tools.actions.SelectAllAction;
import de.fxdiagram.core.tools.actions.UndoAction;
import de.fxdiagram.core.tools.actions.ZoomToFitAction;
import de.fxdiagram.lib.actions.UndoRedoPlayerAction;
import de.fxdiagram.swtfx.SwtToFXGestureConverter;
import de.fxdiagram.xtext.glue.EditorListener;
import de.fxdiagram.xtext.glue.XtextDomainObjectProvider;
import de.fxdiagram.xtext.glue.mapping.BaseMapping;
import de.fxdiagram.xtext.glue.mapping.DiagramMapping;
import de.fxdiagram.xtext.glue.mapping.XDiagramProvider;
import java.util.Collections;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.embed.swt.FXCanvas;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.IXtextModelListener;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class FXDiagramView extends ViewPart {
  private FXCanvas canvas;
  
  private XRoot root;
  
  private SwtToFXGestureConverter gestureConverter;
  
  private Set<XtextEditor> contributingEditors = CollectionLiterals.<XtextEditor>newHashSet();
  
  private Set<XtextEditor> changedEditors = CollectionLiterals.<XtextEditor>newHashSet();
  
  private IPartListener2 listener;
  
  private final XtextDomainObjectProvider domainObjectProvider = new XtextDomainObjectProvider();
  
  private final XDiagramProvider diagramProvider = new XDiagramProvider(this.domainObjectProvider);
  
  public void createPartControl(final Composite parent) {
    FXCanvas _fXCanvas = new FXCanvas(parent, SWT.NONE);
    this.canvas = _fXCanvas;
    SwtToFXGestureConverter _swtToFXGestureConverter = new SwtToFXGestureConverter(this.canvas);
    this.gestureConverter = _swtToFXGestureConverter;
    Scene _createFxScene = this.createFxScene();
    this.canvas.setScene(_createFxScene);
  }
  
  protected Scene createFxScene() {
    XRoot _xRoot = new XRoot();
    final Procedure1<XRoot> _function = new Procedure1<XRoot>() {
      public void apply(final XRoot it) {
        Class<? extends XRoot> _class = it.getClass();
        ClassLoader _classLoader = _class.getClassLoader();
        it.setClassLoader(_classLoader);
        XDiagram _xDiagram = new XDiagram();
        it.setRootDiagram(_xDiagram);
        ObservableList<DomainObjectProvider> _domainObjectProviders = it.getDomainObjectProviders();
        Class<? extends XRoot> _class_1 = it.getClass();
        ClassLoader _classLoader_1 = _class_1.getClassLoader();
        ResourceProvider _resourceProvider = new ResourceProvider(_classLoader_1);
        Iterables.<DomainObjectProvider>addAll(_domainObjectProviders, Collections.<DomainObjectProvider>unmodifiableList(Lists.<DomainObjectProvider>newArrayList(_resourceProvider, FXDiagramView.this.domainObjectProvider)));
        DiagramActionRegistry _diagramActionRegistry = it.getDiagramActionRegistry();
        CenterAction _centerAction = new CenterAction();
        DeleteAction _deleteAction = new DeleteAction();
        LayoutAction _layoutAction = new LayoutAction(LayoutType.DOT);
        ExportSvgAction _exportSvgAction = new ExportSvgAction();
        UndoAction _undoAction = new UndoAction();
        RedoAction _redoAction = new RedoAction();
        LoadAction _loadAction = new LoadAction();
        SaveAction _saveAction = new SaveAction();
        SelectAllAction _selectAllAction = new SelectAllAction();
        ZoomToFitAction _zoomToFitAction = new ZoomToFitAction();
        NavigatePreviousAction _navigatePreviousAction = new NavigatePreviousAction();
        NavigateNextAction _navigateNextAction = new NavigateNextAction();
        FullScreenAction _fullScreenAction = new FullScreenAction();
        UndoRedoPlayerAction _undoRedoPlayerAction = new UndoRedoPlayerAction();
        _diagramActionRegistry.operator_add(Collections.<DiagramAction>unmodifiableList(Lists.<DiagramAction>newArrayList(_centerAction, _deleteAction, _layoutAction, _exportSvgAction, _undoAction, _redoAction, _loadAction, _saveAction, _selectAllAction, _zoomToFitAction, _navigatePreviousAction, _navigateNextAction, _fullScreenAction, _undoRedoPlayerAction)));
      }
    };
    XRoot _doubleArrow = ObjectExtensions.<XRoot>operator_doubleArrow(_xRoot, _function);
    XRoot _root = this.root = _doubleArrow;
    Scene _scene = new Scene(_root);
    final Procedure1<Scene> _function_1 = new Procedure1<Scene>() {
      public void apply(final Scene it) {
        PerspectiveCamera _perspectiveCamera = new PerspectiveCamera();
        it.setCamera(_perspectiveCamera);
        FXDiagramView.this.root.activate();
      }
    };
    return ObjectExtensions.<Scene>operator_doubleArrow(_scene, _function_1);
  }
  
  public void setFocus() {
    this.canvas.setFocus();
    this.setFxFocus();
  }
  
  protected void setFxFocus() {
  }
  
  public <T extends Object> void revealElement(final T element, final BaseMapping<T> mapping, final XtextEditor editor) {
    if ((mapping instanceof DiagramMapping<?>)) {
      this.register(editor);
      boolean _remove = this.changedEditors.remove(editor);
      if (_remove) {
        XDiagram _createDiagram = this.diagramProvider.<T>createDiagram(element, ((DiagramMapping<T>) mapping));
        this.root.setDiagram(_createDiagram);
        LayoutAction _layoutAction = new LayoutAction(LayoutType.DOT);
        _layoutAction.perform(this.root);
      }
    }
    final DomainObjectDescriptor descriptor = this.domainObjectProvider.createDescriptor(element);
    XDiagram _diagram = this.root.getDiagram();
    ObservableList<XNode> _nodes = _diagram.getNodes();
    final Procedure1<XNode> _function = new Procedure1<XNode>() {
      public void apply(final XNode it) {
        DomainObjectDescriptor _domainObject = it.getDomainObject();
        boolean _equals = Objects.equal(_domainObject, descriptor);
        it.setSelected(_equals);
      }
    };
    IterableExtensions.<XNode>forEach(_nodes, _function);
    XDiagram _diagram_1 = this.root.getDiagram();
    ObservableList<XConnection> _connections = _diagram_1.getConnections();
    final Procedure1<XConnection> _function_1 = new Procedure1<XConnection>() {
      public void apply(final XConnection it) {
        DomainObjectDescriptor _domainObject = it.getDomainObject();
        boolean _equals = Objects.equal(_domainObject, descriptor);
        it.setSelected(_equals);
      }
    };
    IterableExtensions.<XConnection>forEach(_connections, _function_1);
    CenterAction _centerAction = new CenterAction();
    _centerAction.perform(this.root);
  }
  
  public void register(final XtextEditor editor) {
    boolean _add = this.contributingEditors.add(editor);
    if (_add) {
      this.changedEditors.add(editor);
      IXtextDocument _document = editor.getDocument();
      final IXtextModelListener _function = new IXtextModelListener() {
        public void modelChanged(final XtextResource it) {
          FXDiagramView.this.changedEditors.add(editor);
        }
      };
      _document.addModelListener(_function);
    }
    boolean _equals = Objects.equal(this.listener, null);
    if (_equals) {
      EditorListener _editorListener = new EditorListener(this);
      this.listener = _editorListener;
      IWorkbenchPartSite _site = editor.getSite();
      IWorkbenchPage _page = _site.getPage();
      _page.addPartListener(this.listener);
    }
  }
  
  public boolean deregister(final IWorkbenchPartReference reference) {
    boolean _xblockexpression = false;
    {
      final IWorkbenchPart part = reference.getPart(false);
      boolean _xifexpression = false;
      boolean _notEquals = (!Objects.equal(part, null));
      if (_notEquals) {
        boolean _xblockexpression_1 = false;
        {
          this.changedEditors.remove(part);
          _xblockexpression_1 = this.contributingEditors.remove(part);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public void dispose() {
    this.gestureConverter.dispose();
    super.dispose();
  }
}
