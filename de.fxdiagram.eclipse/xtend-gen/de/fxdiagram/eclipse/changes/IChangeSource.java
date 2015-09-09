package de.fxdiagram.eclipse.changes;

import de.fxdiagram.eclipse.changes.ModelChangeBroker;
import org.eclipse.ui.IWorkbenchPart;

@SuppressWarnings("all")
public interface IChangeSource {
  public abstract void addChangeListener(final IWorkbenchPart part, final ModelChangeBroker broker);
  
  public abstract void removeChangeListener(final IWorkbenchPart part, final ModelChangeBroker broker);
}
