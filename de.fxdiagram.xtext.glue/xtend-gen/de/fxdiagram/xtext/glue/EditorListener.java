package de.fxdiagram.xtext.glue;

import de.fxdiagram.xtext.glue.FXDiagramView;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;

@SuppressWarnings("all")
public class EditorListener implements IPartListener2 {
  private FXDiagramView view;
  
  public EditorListener(final FXDiagramView view) {
    this.view = view;
  }
  
  public void partActivated(final IWorkbenchPartReference partRef) {
  }
  
  public void partBroughtToTop(final IWorkbenchPartReference partRef) {
  }
  
  public void partClosed(final IWorkbenchPartReference partRef) {
    this.view.deregister(partRef);
  }
  
  public void partDeactivated(final IWorkbenchPartReference partRef) {
  }
  
  public void partHidden(final IWorkbenchPartReference partRef) {
  }
  
  public void partInputChanged(final IWorkbenchPartReference partRef) {
  }
  
  public void partOpened(final IWorkbenchPartReference partRef) {
  }
  
  public void partVisible(final IWorkbenchPartReference partRef) {
  }
}
