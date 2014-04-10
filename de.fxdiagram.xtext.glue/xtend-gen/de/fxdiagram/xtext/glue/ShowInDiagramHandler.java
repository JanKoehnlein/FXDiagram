package de.fxdiagram.xtext.glue;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import de.fxdiagram.xtext.glue.FXDiagramView;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import java.util.List;
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
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public abstract class ShowInDiagramHandler extends AbstractHandler {
  private final static Logger LOG = Logger.getLogger(ShowInDiagramHandler.class);
  
  @Inject
  private EObjectAtOffsetHelper eObjectAtOffsetHelper;
  
  @Inject
  private IWorkbench workbench;
  
  private XDiagramConfig diagramConfig;
  
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    try {
      final XtextEditor editor = EditorUtils.getActiveXtextEditor(event);
      boolean _notEquals = (!Objects.equal(editor, null));
      if (_notEquals) {
        ISelectionProvider _selectionProvider = editor.getSelectionProvider();
        ISelection _selection = _selectionProvider.getSelection();
        final ITextSelection selection = ((ITextSelection) _selection);
        IXtextDocument _document = editor.getDocument();
        final IUnitOfWork<Object,XtextResource> _function = new IUnitOfWork<Object,XtextResource>() {
          public Object exec(final XtextResource it) throws Exception {
            Object _xblockexpression = null;
            {
              int _offset = selection.getOffset();
              final EObject selectedElement = ShowInDiagramHandler.this.eObjectAtOffsetHelper.resolveElementAt(it, _offset);
              boolean _notEquals = (!Objects.equal(selectedElement, null));
              if (_notEquals) {
                XDiagramConfig _diagramConfig = ShowInDiagramHandler.this.getDiagramConfig();
                final List<? extends AbstractMapping<EObject>> mappings = _diagramConfig.<EObject>getMappings(selectedElement);
                boolean _isEmpty = mappings.isEmpty();
                boolean _not = (!_isEmpty);
                if (_not) {
                  IWorkbenchWindow _activeWorkbenchWindow = ShowInDiagramHandler.this.workbench.getActiveWorkbenchWindow();
                  IWorkbenchPage _activePage = _activeWorkbenchWindow.getActivePage();
                  final IViewPart view = _activePage.showView("org.eclipse.xtext.glue.FXDiagramView");
                  if ((view instanceof FXDiagramView)) {
                    XDiagramConfig _diagramConfig_1 = ShowInDiagramHandler.this.getDiagramConfig();
                    ((FXDiagramView)view).addConfig(_diagramConfig_1);
                    AbstractMapping<EObject> _head = IterableExtensions.head(mappings);
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
  
  protected XDiagramConfig getDiagramConfig() {
    XDiagramConfig _elvis = null;
    if (this.diagramConfig != null) {
      _elvis = this.diagramConfig;
    } else {
      XDiagramConfig _createDiagramConfig = this.createDiagramConfig();
      XDiagramConfig _diagramConfig = this.diagramConfig = _createDiagramConfig;
      _elvis = _diagramConfig;
    }
    return _elvis;
  }
  
  protected abstract XDiagramConfig createDiagramConfig();
}
