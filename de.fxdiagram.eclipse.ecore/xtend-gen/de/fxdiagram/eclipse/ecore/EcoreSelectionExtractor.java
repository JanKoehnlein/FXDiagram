package de.fxdiagram.eclipse.ecore;

import com.google.common.base.Objects;
import de.fxdiagram.eclipse.changes.IChangeSource;
import de.fxdiagram.eclipse.changes.ModelChangeBroker;
import de.fxdiagram.eclipse.selection.ISelectionExtractor;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;

@SuppressWarnings("all")
public class EcoreSelectionExtractor implements ISelectionExtractor, IChangeSource {
  @FinalFieldsConstructor
  public static class FXDiagramAdapter extends AdapterImpl {
    private final IWorkbenchPart part;
    
    private final ModelChangeBroker broker;
    
    @Override
    public void notifyChanged(final Notification msg) {
      this.broker.partChanged(this.part);
    }
    
    @Override
    public boolean isAdapterForType(final Object type) {
      return Objects.equal(type, EcoreSelectionExtractor.FXDiagramAdapter.class);
    }
    
    public FXDiagramAdapter(final IWorkbenchPart part, final ModelChangeBroker broker) {
      super();
      this.part = part;
      this.broker = broker;
    }
  }
  
  @Override
  public boolean addSelectedElement(final IWorkbenchPart activePart, final ISelectionExtractor.Acceptor acceptor) {
    boolean _xifexpression = false;
    if ((activePart instanceof IEditingDomainProvider)) {
      boolean _xblockexpression = false;
      {
        IWorkbenchPartSite _site = null;
        if (activePart!=null) {
          _site=activePart.getSite();
        }
        ISelectionProvider _selectionProvider = null;
        if (_site!=null) {
          _selectionProvider=_site.getSelectionProvider();
        }
        ISelection _selection = null;
        if (_selectionProvider!=null) {
          _selection=_selectionProvider.getSelection();
        }
        final ISelection selection = _selection;
        boolean _xifexpression_1 = false;
        if ((selection instanceof IStructuredSelection)) {
          _xifexpression_1 = acceptor.accept(((IStructuredSelection)selection).getFirstElement());
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  @Override
  public void addChangeListener(final IWorkbenchPart part, final ModelChangeBroker broker) {
    if ((part instanceof IEditingDomainProvider)) {
      EList<Adapter> _eAdapters = ((IEditingDomainProvider)part).getEditingDomain().getResourceSet().eAdapters();
      EcoreSelectionExtractor.FXDiagramAdapter _fXDiagramAdapter = new EcoreSelectionExtractor.FXDiagramAdapter(part, broker);
      _eAdapters.add(_fXDiagramAdapter);
    }
  }
  
  @Override
  public void removeChangeListener(final IWorkbenchPart part, final ModelChangeBroker broker) {
    if ((part instanceof IEditingDomainProvider)) {
      final EList<Adapter> adapters = ((IEditingDomainProvider)part).getEditingDomain().getResourceSet().eAdapters();
      final Adapter adapter = EcoreUtil.getAdapter(adapters, EcoreSelectionExtractor.FXDiagramAdapter.class);
      adapters.remove(adapter);
    }
  }
}
