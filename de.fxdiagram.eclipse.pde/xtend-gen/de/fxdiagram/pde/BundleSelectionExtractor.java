package de.fxdiagram.pde;

import com.google.common.collect.Iterators;
import de.fxdiagram.eclipse.commands.ISelectionExtractor;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
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
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class BundleSelectionExtractor implements ISelectionExtractor {
  @Override
  public void addSelectedElement(final IWorkbenchPart activePart, final ISelectionExtractor.Acceptor acceptor) {
    IWorkbenchPartSite _site = activePart.getSite();
    ISelectionProvider _selectionProvider = _site.getSelectionProvider();
    final ISelection selection = _selectionProvider.getSelection();
    if ((selection instanceof IStructuredSelection)) {
      Iterator _iterator = ((IStructuredSelection)selection).iterator();
      Iterator<IAdaptable> _filter = Iterators.<IAdaptable>filter(_iterator, IAdaptable.class);
      final Function1<IAdaptable, IProject> _function = (IAdaptable it) -> {
        Object _adapter = it.getAdapter(IProject.class);
        return ((IProject) _adapter);
      };
      Iterator<IProject> _map = IteratorExtensions.<IAdaptable, IProject>map(_filter, _function);
      final Function1<IProject, BundleDescription> _function_1 = (IProject it) -> {
        IPluginModelBase _findModel = PluginRegistry.findModel(it);
        return _findModel.getBundleDescription();
      };
      Iterator<BundleDescription> _map_1 = IteratorExtensions.<IProject, BundleDescription>map(_map, _function_1);
      Iterator<BundleDescription> _filterNull = IteratorExtensions.<BundleDescription>filterNull(_map_1);
      Set<BundleDescription> _set = IteratorExtensions.<BundleDescription>toSet(_filterNull);
      final Consumer<BundleDescription> _function_2 = (BundleDescription it) -> {
        acceptor.accept(it);
      };
      _set.forEach(_function_2);
    }
  }
}
