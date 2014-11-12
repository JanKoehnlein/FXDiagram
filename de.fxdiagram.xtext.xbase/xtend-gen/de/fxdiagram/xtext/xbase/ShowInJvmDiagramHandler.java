package de.fxdiagram.xtext.xbase;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import de.fxdiagram.eclipse.FXDiagramView;
import de.fxdiagram.eclipse.mapping.MappingCall;
import de.fxdiagram.eclipse.mapping.XDiagramConfig;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.GrammarUtil;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.util.PolymorphicDispatcher;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ShowInJvmDiagramHandler extends AbstractHandler {
  public static class Action {
    @Inject
    private EObjectAtOffsetHelper eObjectAtOffsetHelper;
    
    @Inject
    @Extension
    private IJvmModelAssociations _iJvmModelAssociations;
    
    public Object run(final XtextEditor editor) {
      Object _xblockexpression = null;
      {
        ISelectionProvider _selectionProvider = editor.getSelectionProvider();
        ISelection _selection = _selectionProvider.getSelection();
        final ITextSelection selection = ((ITextSelection) _selection);
        IXtextDocument _document = editor.getDocument();
        final IUnitOfWork<Object, XtextResource> _function = new IUnitOfWork<Object, XtextResource>() {
          public Object exec(final XtextResource it) throws Exception {
            Object _xblockexpression = null;
            {
              int _offset = selection.getOffset();
              final EObject selectedElement = Action.this.eObjectAtOffsetHelper.resolveElementAt(it, _offset);
              boolean _notEquals = (!Objects.equal(selectedElement, null));
              if (_notEquals) {
                final EObject primary = Action.this._iJvmModelAssociations.getPrimaryJvmElement(selectedElement);
                boolean _notEquals_1 = (!Objects.equal(primary, null));
                if (_notEquals_1) {
                  ShowInJvmDiagramHandler.showElement(primary, editor);
                } else {
                  ShowInJvmDiagramHandler.showElement(selectedElement, editor);
                }
              }
              _xblockexpression = null;
            }
            return _xblockexpression;
          }
        };
        _xblockexpression = _document.<Object>readOnly(_function);
      }
      return _xblockexpression;
    }
  }
  
  private final static Logger LOG = Logger.getLogger(ShowInJvmDiagramHandler.class);
  
  public void setEnabled(final Object evaluationContext) {
    if ((evaluationContext instanceof IEvaluationContext)) {
      final Object editor = ((IEvaluationContext)evaluationContext).getVariable(ISources.ACTIVE_EDITOR_NAME);
      if ((editor instanceof XtextEditor)) {
        final IGrammarAccess languageService = this.<IGrammarAccess>getLanguageService(((XtextEditor)editor), IGrammarAccess.class);
        Grammar _grammar = languageService.getGrammar();
        List<Grammar> _allUsedGrammars = GrammarUtil.allUsedGrammars(_grammar);
        final Function1<Grammar, Boolean> _function = new Function1<Grammar, Boolean>() {
          public Boolean apply(final Grammar it) {
            String _name = it.getName();
            return Boolean.valueOf(Objects.equal(_name, "org.eclipse.xtext.xbase.Xbase"));
          }
        };
        final boolean usesXbase = IterableExtensions.<Grammar>exists(_allUsedGrammars, _function);
        this.setBaseEnabled(usesXbase);
      } else {
        this.setBaseEnabled((editor instanceof CompilationUnitEditor));
      }
      return;
    }
    super.setEnabled(evaluationContext);
  }
  
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    try {
      final IEditorPart editor = HandlerUtil.getActiveEditor(event);
      if ((editor instanceof XtextEditor)) {
        ShowInJvmDiagramHandler.Action _languageService = null;
        if (((XtextEditor)editor)!=null) {
          _languageService=this.<ShowInJvmDiagramHandler.Action>getLanguageService(((XtextEditor)editor), ShowInJvmDiagramHandler.Action.class);
        }
        final ShowInJvmDiagramHandler.Action action = _languageService;
        action.run(((XtextEditor)editor));
      } else {
        if ((editor instanceof CompilationUnitEditor)) {
          ISelectionProvider _selectionProvider = ((CompilationUnitEditor)editor).getSelectionProvider();
          ISelection _selection = _selectionProvider.getSelection();
          final ITextSelection selection = ((ITextSelection) _selection);
          PolymorphicDispatcher<IJavaElement> _createForSingleTarget = PolymorphicDispatcher.<IJavaElement>createForSingleTarget("getElementAt", 1, 1, editor);
          int _offset = selection.getOffset();
          final IJavaElement javaElement = _createForSingleTarget.invoke(Integer.valueOf(_offset));
          int _xifexpression = (int) 0;
          if ((javaElement instanceof PackageDeclaration)) {
            _xifexpression = IJavaElement.PACKAGE_FRAGMENT;
          } else {
            _xifexpression = IJavaElement.TYPE;
          }
          final IJavaElement javaElementToShow = javaElement.getAncestor(_xifexpression);
          ShowInJvmDiagramHandler.showElement(javaElementToShow, editor);
        }
      }
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception exc = (Exception)_t;
        ShowInJvmDiagramHandler.LOG.error("Error opening element in diagram", exc);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return null;
  }
  
  protected <T extends Object> T getLanguageService(final XtextEditor editor, final Class<T> service) {
    IXtextDocument _document = editor.getDocument();
    final IUnitOfWork<URI, XtextResource> _function = new IUnitOfWork<URI, XtextResource>() {
      public URI exec(final XtextResource it) throws Exception {
        return it.getURI();
      }
    };
    final URI uri = _document.<URI>readOnly(_function);
    final IResourceServiceProvider resourceServiceProvider = IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(uri);
    return resourceServiceProvider.<T>get(service);
  }
  
  protected static XDiagramConfig getDiagramConfig() {
    XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
    return _instance.getConfigByID("de.fxdiagram.xtext.xbase.JvmClassDiagramConfig");
  }
  
  protected static void showElement(final Object element, final IEditorPart editor) {
    try {
      boolean _notEquals = (!Objects.equal(element, null));
      if (_notEquals) {
        XDiagramConfig _diagramConfig = ShowInJvmDiagramHandler.getDiagramConfig();
        final Iterable<? extends MappingCall<?, Object>> mappings = _diagramConfig.<Object>getEntryCalls(element);
        boolean _isEmpty = IterableExtensions.isEmpty(mappings);
        boolean _not = (!_isEmpty);
        if (_not) {
          IWorkbenchPartSite _site = editor.getSite();
          IWorkbenchPage _page = _site.getPage();
          IWorkbenchWindow _workbenchWindow = _page.getWorkbenchWindow();
          final IWorkbench workbench = _workbenchWindow.getWorkbench();
          IWorkbenchWindow _activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
          IWorkbenchPage _activePage = _activeWorkbenchWindow.getActivePage();
          final IViewPart view = _activePage.showView("de.fxdiagram.eclipse.FXDiagramView");
          if ((view instanceof FXDiagramView)) {
            MappingCall<?, Object> _head = IterableExtensions.head(mappings);
            ((FXDiagramView)view).<Object>revealElement(element, _head, editor);
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
