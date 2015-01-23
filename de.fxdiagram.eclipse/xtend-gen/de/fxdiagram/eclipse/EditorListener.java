package de.fxdiagram.eclipse;

import de.fxdiagram.eclipse.FXDiagramView;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;

@SuppressWarnings("all")
public class EditorListener implements IPartListener2 {
  private FXDiagramView view;
  
  public EditorListener(final FXDiagramView view) {
    this.view = view;
  }
  
  @Override
  public void partActivated(final IWorkbenchPartReference partRef) {
  }
  
  @Override
  public void partBroughtToTop(final IWorkbenchPartReference partRef) {
  }
  
  @Override
  public void partClosed(final IWorkbenchPartReference partRef) {
    this.view.deregister(partRef);
  }
  
  @Override
  public void partDeactivated(final IWorkbenchPartReference partRef) {
  }
  
  @Override
  public void partHidden(final IWorkbenchPartReference partRef) {
  }
  
  @Override
  public void partInputChanged(final IWorkbenchPartReference partRef) {
  }
  
  @Override
  public void partOpened(final IWorkbenchPartReference partRef) {
  }
  
  @Override
  public void partVisible(final IWorkbenchPartReference partRef) {
  }
}
