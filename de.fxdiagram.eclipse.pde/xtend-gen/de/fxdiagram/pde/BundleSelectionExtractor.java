package de.fxdiagram.pde;

import com.google.common.collect.Iterators;
import de.fxdiagram.eclipse.commands.ISelectionExtractor;
import java.util.Iterator;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class BundleSelectionExtractor implements ISelectionExtractor {
  @Override
  public boolean addSelectedElement(final IWorkbenchPart activePart, final ISelectionExtractor.Acceptor acceptor) {
    IWorkbenchPartSite _site = activePart.getSite();
    ISelectionProvider _selectionProvider = _site.getSelectionProvider();
    final ISelection selection = _selectionProvider.getSelection();
    if ((selection instanceof IStructuredSelection)) {
      Iterator _iterator = ((IStructuredSelection)selection).iterator();
      Iterator<IAdaptable> _filter = Iterators.<IAdaptable>filter(_iterator, IAdaptable.class);
      final Function1<IAdaptable, BundleDescription> _function = (IAdaptable it) -> {
        Object _adapter = it.getAdapter(IProject.class);
        IPluginModelBase _findModel = null;
        if (((IProject) _adapter)!=null) {
          Object _adapter_1 = it.getAdapter(IProject.class);
          _findModel=PluginRegistry.findModel(((IProject) _adapter_1));
        }
        BundleDescription _bundleDescription = null;
        if (_findModel!=null) {
          _bundleDescription=_findModel.getBundleDescription();
        }
        return _bundleDescription;
      };
      Iterator<BundleDescription> _map = IteratorExtensions.<IAdaptable, BundleDescription>map(_filter, _function);
      Iterator<BundleDescription> _filterNull = IteratorExtensions.<BundleDescription>filterNull(_map);
      Set<BundleDescription> _set = IteratorExtensions.<BundleDescription>toSet(_filterNull);
      final Function1<BundleDescription, Boolean> _function_1 = (BundleDescription it) -> {
        return Boolean.valueOf(acceptor.accept(it));
      };
      Iterable<Boolean> _map_1 = IterableExtensions.<BundleDescription, Boolean>map(_set, _function_1);
      final Function2<Boolean, Boolean, Boolean> _function_2 = (Boolean $0, Boolean $1) -> {
        boolean _or = false;
        if (($0).booleanValue()) {
          _or = true;
        } else {
          _or = ($1).booleanValue();
        }
        return Boolean.valueOf(_or);
      };
      return (boolean) IterableExtensions.<Boolean>reduce(_map_1, _function_2);
    }
    return false;
  }
}
