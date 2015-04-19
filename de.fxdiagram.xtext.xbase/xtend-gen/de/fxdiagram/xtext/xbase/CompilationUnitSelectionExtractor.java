package de.fxdiagram.xtext.xbase;

import de.fxdiagram.eclipse.selection.ISelectionExtractor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.xtext.util.PolymorphicDispatcher;

@SuppressWarnings("all")
public class CompilationUnitSelectionExtractor implements ISelectionExtractor {
  @Override
  public boolean addSelectedElement(final IWorkbenchPart activePart, final ISelectionExtractor.Acceptor acceptor) {
    if ((activePart instanceof CompilationUnitEditor)) {
      ISelectionProvider _selectionProvider = ((CompilationUnitEditor)activePart).getSelectionProvider();
      ISelection _selection = _selectionProvider.getSelection();
      final ITextSelection selection = ((ITextSelection) _selection);
      PolymorphicDispatcher<IJavaElement> _createForSingleTarget = PolymorphicDispatcher.<IJavaElement>createForSingleTarget("getElementAt", 1, 1, activePart);
      int _offset = selection.getOffset();
      final IJavaElement javaElement = _createForSingleTarget.invoke(Integer.valueOf(_offset));
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
}
