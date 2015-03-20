package de.fxdiagram.eclipse.xtext;

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

@SuppressWarnings("all")
public class XtextSelectionExtractor implements ISelectionExtractor {
  @Override
  public void addSelectedElement(final IWorkbenchPart activePart, final ISelectionExtractor.Acceptor acceptor) {
    if ((activePart instanceof XtextEditor)) {
      ISelectionProvider _selectionProvider = ((XtextEditor)activePart).getSelectionProvider();
      ISelection _selection = _selectionProvider.getSelection();
      final ITextSelection selection = ((ITextSelection) _selection);
      IXtextDocument _document = ((XtextEditor)activePart).getDocument();
      final IUnitOfWork<Object, XtextResource> _function = new IUnitOfWork<Object, XtextResource>() {
        @Override
        public Object exec(final XtextResource it) throws Exception {
          Object _xblockexpression = null;
          {
            IResourceServiceProvider _resourceServiceProvider = it.getResourceServiceProvider();
            final EObjectAtOffsetHelper eObjectAtOffsetHelper = _resourceServiceProvider.<EObjectAtOffsetHelper>get(EObjectAtOffsetHelper.class);
            int _offset = selection.getOffset();
            final EObject selectedElement = eObjectAtOffsetHelper.resolveElementAt(it, _offset);
            acceptor.accept(selectedElement);
            _xblockexpression = null;
          }
          return _xblockexpression;
        }
      };
      _document.<Object>readOnly(_function);
    }
  }
}
