package de.fxdiagram.xtext.xbase;

import de.fxdiagram.eclipse.changes.IChangeSource;
import de.fxdiagram.eclipse.changes.ModelChangeBroker;
import de.fxdiagram.eclipse.selection.ISelectionExtractor;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.xtext.util.PolymorphicDispatcher;

@SuppressWarnings("all")
public class JavaEditorSelectionExtractor implements ISelectionExtractor, IChangeSource {
  private int numJavaEditors = 0;
  
  private IElementChangedListener listener;
  
  @Override
  public boolean addSelectedElement(final IWorkbenchPart activePart, final ISelectionExtractor.Acceptor acceptor) {
    if ((activePart instanceof JavaEditor)) {
      ISelection _selection = ((JavaEditor)activePart).getSelectionProvider().getSelection();
      final ITextSelection selection = ((ITextSelection) _selection);
      final IJavaElement javaElement = PolymorphicDispatcher.<IJavaElement>createForSingleTarget("getElementAt", 1, 1, activePart).invoke(Integer.valueOf(selection.getOffset()));
      int _xifexpression = (int) 0;
      if ((javaElement instanceof PackageDeclaration)) {
        _xifexpression = IJavaElement.PACKAGE_FRAGMENT;
      } else {
        _xifexpression = IJavaElement.TYPE;
      }
      final IJavaElement javaElementToShow = javaElement.getAncestor(_xifexpression);
      return acceptor.accept(javaElementToShow);
    }
    return false;
  }
  
  @Override
  public void addChangeListener(final IWorkbenchPart part, final ModelChangeBroker broker) {
    if ((part instanceof JavaEditor)) {
      int _plusPlus = this.numJavaEditors++;
      boolean _equals = (_plusPlus == 0);
      if (_equals) {
        final IElementChangedListener _function = (ElementChangedEvent it) -> {
          broker.partChanged(part);
        };
        this.listener = _function;
        JavaCore.addElementChangedListener(this.listener);
      }
    }
  }
  
  @Override
  public void removeChangeListener(final IWorkbenchPart part, final ModelChangeBroker broker) {
    if ((part instanceof JavaEditor)) {
      this.numJavaEditors--;
      if ((this.numJavaEditors == 0)) {
        JavaCore.removeElementChangedListener(this.listener);
      }
    }
  }
}
