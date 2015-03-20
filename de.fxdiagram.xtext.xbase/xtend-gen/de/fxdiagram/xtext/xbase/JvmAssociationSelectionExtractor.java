package de.fxdiagram.xtext.xbase;

import com.google.common.base.Objects;
import de.fxdiagram.eclipse.commands.ISelectionExtractor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class JvmAssociationSelectionExtractor implements ISelectionExtractor {
  @Override
  public void addSelectedElement(final IWorkbenchPart activePart, final ISelectionExtractor.Acceptor acceptor) {
    if ((activePart instanceof XtextEditor)) {
      ISelectionProvider _selectionProvider = ((XtextEditor)activePart).getSelectionProvider();
      ISelection _selection = _selectionProvider.getSelection();
      final ITextSelection selection = ((ITextSelection) _selection);
      IXtextDocument _document = ((XtextEditor)activePart).getDocument();
      final IUnitOfWork<Object, XtextResource> _function = (XtextResource it) -> {
        Object _xblockexpression = null;
        {
          try {
            IResourceServiceProvider _resourceServiceProvider = it.getResourceServiceProvider();
            final IJvmModelAssociations associations = _resourceServiceProvider.<IJvmModelAssociations>get(IJvmModelAssociations.class);
            IResourceServiceProvider _resourceServiceProvider_1 = it.getResourceServiceProvider();
            final EObjectAtOffsetHelper eObjectAtOffsetHelper = _resourceServiceProvider_1.<EObjectAtOffsetHelper>get(EObjectAtOffsetHelper.class);
            int _offset = selection.getOffset();
            final EObject selectedElement = eObjectAtOffsetHelper.resolveElementAt(it, _offset);
            boolean _notEquals = (!Objects.equal(selectedElement, null));
            if (_notEquals) {
              final EObject primary = associations.getPrimaryJvmElement(selectedElement);
              boolean _notEquals_1 = (!Objects.equal(primary, null));
              if (_notEquals_1) {
                acceptor.accept(primary);
              }
            }
          } catch (final Throwable _t) {
            if (_t instanceof Exception) {
              final Exception exc = (Exception)_t;
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
          _xblockexpression = null;
        }
        return _xblockexpression;
      };
      _document.<Object>readOnly(_function);
    }
  }
}
