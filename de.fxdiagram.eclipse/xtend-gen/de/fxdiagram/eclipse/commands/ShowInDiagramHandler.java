package de.fxdiagram.eclipse.commands;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import de.fxdiagram.eclipse.FXDiagramView;
import de.fxdiagram.eclipse.mapping.MappingCall;
import de.fxdiagram.eclipse.mapping.XDiagramConfig;
import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ShowInDiagramHandler extends AbstractHandler {
  private final static Logger LOG = Logger.getLogger(ShowInDiagramHandler.class);
  
  @Inject
  private EObjectAtOffsetHelper eObjectAtOffsetHelper;
  
  @Inject
  private IWorkbench workbench;
  
  @Override
  public boolean isEnabled() {
    return super.isEnabled();
  }
  
  @Override
  public void setEnabled(final Object evaluationContext) {
    super.setEnabled(evaluationContext);
  }
  
  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    try {
      final XtextEditor editor = EditorUtils.getActiveXtextEditor(event);
      boolean _notEquals = (!Objects.equal(editor, null));
      if (_notEquals) {
        ISelectionProvider _selectionProvider = editor.getSelectionProvider();
        ISelection _selection = _selectionProvider.getSelection();
        final ITextSelection selection = ((ITextSelection) _selection);
        IXtextDocument _document = editor.getDocument();
        final IUnitOfWork<Object, XtextResource> _function = new IUnitOfWork<Object, XtextResource>() {
          @Override
          public Object exec(final XtextResource it) throws Exception {
            Object _xblockexpression = null;
            {
              int _offset = selection.getOffset();
              final EObject selectedElement = ShowInDiagramHandler.this.eObjectAtOffsetHelper.resolveElementAt(it, _offset);
              boolean _notEquals = (!Objects.equal(selectedElement, null));
              if (_notEquals) {
                XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
                Iterable<? extends XDiagramConfig> _configurations = _instance.getConfigurations();
                final Function1<XDiagramConfig, Iterable<? extends MappingCall<?, EObject>>> _function = new Function1<XDiagramConfig, Iterable<? extends MappingCall<?, EObject>>>() {
                  @Override
                  public Iterable<? extends MappingCall<?, EObject>> apply(final XDiagramConfig it) {
                    return it.<EObject>getEntryCalls(selectedElement);
                  }
                };
                Iterable<Iterable<? extends MappingCall<?, EObject>>> _map = IterableExtensions.map(_configurations, _function);
                final Iterable<MappingCall<?, EObject>> mappingCalls = Iterables.<MappingCall<?, EObject>>concat(_map);
                boolean _isEmpty = IterableExtensions.isEmpty(mappingCalls);
                boolean _not = (!_isEmpty);
                if (_not) {
                  IWorkbenchWindow _activeWorkbenchWindow = ShowInDiagramHandler.this.workbench.getActiveWorkbenchWindow();
                  IWorkbenchPage _activePage = _activeWorkbenchWindow.getActivePage();
                  final IViewPart view = _activePage.showView("de.fxdiagram.eclipse.FXDiagramView");
                  if ((view instanceof FXDiagramView)) {
                    MappingCall<?, EObject> _head = IterableExtensions.<MappingCall<?, EObject>>head(mappingCalls);
                    ((FXDiagramView)view).<EObject>revealElement(selectedElement, _head, editor);
                  }
                }
              }
              _xblockexpression = null;
            }
            return _xblockexpression;
          }
        };
        _document.<Object>readOnly(_function);
      }
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception exc = (Exception)_t;
        ShowInDiagramHandler.LOG.error("Error opening element in diagram", exc);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return null;
  }
}
