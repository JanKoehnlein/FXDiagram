package de.fxdiagram.eclipse.xtext;

import com.google.common.base.Objects;
import de.fxdiagram.eclipse.changes.IChangeSource;
import de.fxdiagram.eclipse.changes.ModelChangeBroker;
import de.fxdiagram.eclipse.selection.ISelectionExtractor;
import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.model.IXtextModelListener;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class XtextSelectionExtractor implements ISelectionExtractor, IChangeSource {
  private final Map<XtextEditor, IXtextModelListener> editor2listener = CollectionLiterals.<XtextEditor, IXtextModelListener>newHashMap();
  
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
  
  @Override
  public void addChangeListener(final IWorkbenchPart part, final ModelChangeBroker broker) {
    if ((part instanceof XtextEditor)) {
      final IXtextModelListener _function = (XtextResource it) -> {
        Display _default = Display.getDefault();
        final Runnable _function_1 = () -> {
          broker.partChanged(part);
        };
        _default.asyncExec(_function_1);
      };
      final IXtextModelListener listener = _function;
      IXtextDocument _document = ((XtextEditor)part).getDocument();
      _document.addModelListener(listener);
      this.editor2listener.put(((XtextEditor)part), listener);
    }
  }
  
  @Override
  public void removeChangeListener(final IWorkbenchPart part, final ModelChangeBroker broker) {
    if ((part instanceof XtextEditor)) {
      final IXtextModelListener listener = this.editor2listener.remove(part);
      boolean _notEquals = (!Objects.equal(listener, null));
      if (_notEquals) {
        IXtextDocument _document = ((XtextEditor)part).getDocument();
        _document.removeModelListener(listener);
      }
    }
  }
}
