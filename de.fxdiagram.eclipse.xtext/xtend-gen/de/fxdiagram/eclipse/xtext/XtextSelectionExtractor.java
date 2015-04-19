package de.fxdiagram.eclipse.xtext;

import de.fxdiagram.eclipse.selection.ISelectionExtractor;
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

@SuppressWarnings("all")
public class XtextSelectionExtractor implements ISelectionExtractor {
  @Override
  public boolean addSelectedElement(final IWorkbenchPart activePart, final ISelectionExtractor.Acceptor acceptor) {
    if ((activePart instanceof XtextEditor)) {
      ISelectionProvider _selectionProvider = ((XtextEditor)activePart).getSelectionProvider();
      ISelection _selection = _selectionProvider.getSelection();
      final ITextSelection selection = ((ITextSelection) _selection);
      IXtextDocument _document = ((XtextEditor)activePart).getDocument();
      final IUnitOfWork<Boolean, XtextResource> _function = (XtextResource it) -> {
        boolean _xblockexpression = false;
        {
          IResourceServiceProvider _resourceServiceProvider = it.getResourceServiceProvider();
          final EObjectAtOffsetHelper eObjectAtOffsetHelper = _resourceServiceProvider.<EObjectAtOffsetHelper>get(EObjectAtOffsetHelper.class);
          int _offset = selection.getOffset();
          final EObject selectedElement = eObjectAtOffsetHelper.resolveElementAt(it, _offset);
          _xblockexpression = acceptor.accept(selectedElement);
        }
        return Boolean.valueOf(_xblockexpression);
      };
      return (_document.<Boolean>readOnly(_function)).booleanValue();
    }
    return false;
  }
}
