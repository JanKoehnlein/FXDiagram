package de.fxdiagram.eclipse.xtext;

import de.fxdiagram.eclipse.changes.IChangeSource;
import de.fxdiagram.eclipse.changes.ModelChangeBroker;
import de.fxdiagram.eclipse.selection.ISelectionExtractor;
import java.util.Map;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextModelListener;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class XtextSelectionExtractor implements ISelectionExtractor, IChangeSource {
  private final Map<XtextEditor, IXtextModelListener> editor2listener = CollectionLiterals.<XtextEditor, IXtextModelListener>newHashMap();
  
  @Override
  public boolean addSelectedElement(final IWorkbenchPart activePart, final ISelectionExtractor.Acceptor acceptor) {
    if ((activePart instanceof XtextEditor)) {
      ISelection _selection = ((XtextEditor)activePart).getSelectionProvider().getSelection();
      final ITextSelection selection = ((ITextSelection) _selection);
      final IUnitOfWork<Boolean, XtextResource> _function = (XtextResource it) -> {
        boolean _xblockexpression = false;
        {
          final EObjectAtOffsetHelper eObjectAtOffsetHelper = it.getResourceServiceProvider().<EObjectAtOffsetHelper>get(EObjectAtOffsetHelper.class);
          final EObject selectedElement = eObjectAtOffsetHelper.resolveElementAt(it, selection.getOffset());
          _xblockexpression = acceptor.accept(selectedElement);
        }
        return Boolean.valueOf(_xblockexpression);
      };
      return (((XtextEditor)activePart).getDocument().<Boolean>readOnly(_function)).booleanValue();
    }
    return false;
  }
  
  @Override
  public void addChangeListener(final IWorkbenchPart part, final ModelChangeBroker broker) {
    if ((part instanceof XtextEditor)) {
      final IXtextModelListener _function = (XtextResource it) -> {
        final Runnable _function_1 = () -> {
          broker.partChanged(part);
        };
        Display.getDefault().asyncExec(_function_1);
      };
      final IXtextModelListener listener = _function;
      ((XtextEditor)part).getDocument().addModelListener(listener);
      this.editor2listener.put(((XtextEditor)part), listener);
    }
  }
  
  @Override
  public void removeChangeListener(final IWorkbenchPart part, final ModelChangeBroker broker) {
    if ((part instanceof XtextEditor)) {
      final IXtextModelListener listener = this.editor2listener.remove(part);
      if ((listener != null)) {
        ((XtextEditor)part).getDocument().removeModelListener(listener);
      }
    }
  }
}
