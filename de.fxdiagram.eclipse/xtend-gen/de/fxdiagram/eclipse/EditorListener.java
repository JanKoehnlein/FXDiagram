package de.fxdiagram.eclipse;

import de.fxdiagram.eclipse.FXDiagramView;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class EditorListener implements IPartListener2 {
  private final FXDiagramView view;
  
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
  
  public EditorListener(final FXDiagramView view) {
    super();
    this.view = view;
  }
}
