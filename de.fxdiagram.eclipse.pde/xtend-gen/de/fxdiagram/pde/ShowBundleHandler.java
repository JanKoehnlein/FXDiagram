package de.fxdiagram.pde;

import com.google.common.collect.Iterators;
import de.fxdiagram.eclipse.FXDiagramView;
import de.fxdiagram.eclipse.mapping.MappingCall;
import de.fxdiagram.eclipse.mapping.XDiagramConfig;
import de.fxdiagram.pde.BundleDiagramConfig;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ShowBundleHandler extends AbstractHandler {
  public void setEnabled(final Object evaluationContext) {
    if ((evaluationContext instanceof IEvaluationContext)) {
      final Object selection = ((IEvaluationContext)evaluationContext).getVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME);
      Set<BundleDescription> _bundleDescriptions = this.getBundleDescriptions(selection);
      boolean _isEmpty = _bundleDescriptions.isEmpty();
      boolean _not = (!_isEmpty);
      this.setBaseEnabled(_not);
    } else {
      super.setEnabled(evaluationContext);
    }
  }
  
  protected Set<BundleDescription> getBundleDescriptions(final Object selection) {
    if ((selection instanceof IStructuredSelection)) {
      Iterator _iterator = ((IStructuredSelection)selection).iterator();
      Iterator<IAdaptable> _filter = Iterators.<IAdaptable>filter(_iterator, IAdaptable.class);
      final Function1<IAdaptable, IProject> _function = new Function1<IAdaptable, IProject>() {
        public IProject apply(final IAdaptable it) {
          Object _adapter = it.getAdapter(IProject.class);
          return ((IProject) _adapter);
        }
      };
      Iterator<IProject> _map = IteratorExtensions.<IAdaptable, IProject>map(_filter, _function);
      final Function1<IProject, BundleDescription> _function_1 = new Function1<IProject, BundleDescription>() {
        public BundleDescription apply(final IProject it) {
          IPluginModelBase _findModel = PluginRegistry.findModel(it);
          return _findModel.getBundleDescription();
        }
      };
      Iterator<BundleDescription> _map_1 = IteratorExtensions.<IProject, BundleDescription>map(_map, _function_1);
      Iterator<BundleDescription> _filterNull = IteratorExtensions.<BundleDescription>filterNull(_map_1);
      return IteratorExtensions.<BundleDescription>toSet(_filterNull);
    } else {
      return Collections.<BundleDescription>unmodifiableSet(CollectionLiterals.<BundleDescription>newHashSet());
    }
  }
  
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    try {
      Object _xblockexpression = null;
      {
        final ISelection selection = HandlerUtil.getActiveMenuSelection(event);
        final Set<BundleDescription> bundleDescriptions = this.getBundleDescriptions(selection);
        boolean _isEmpty = bundleDescriptions.isEmpty();
        boolean _not = (!_isEmpty);
        if (_not) {
          XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
          XDiagramConfig _configByID = _instance.getConfigByID("de.fxdiagram.pde.BundleDiagramConfig");
          final BundleDiagramConfig config = ((BundleDiagramConfig) _configByID);
          IWorkbench _workbench = PlatformUI.getWorkbench();
          IWorkbenchWindow _activeWorkbenchWindow = _workbench.getActiveWorkbenchWindow();
          final IWorkbenchPage page = _activeWorkbenchWindow.getActivePage();
          final IViewPart view = page.showView("de.fxdiagram.eclipse.FXDiagramView");
          if ((view instanceof FXDiagramView)) {
            final Procedure1<BundleDescription> _function = new Procedure1<BundleDescription>() {
              public void apply(final BundleDescription it) {
                Iterable<? extends MappingCall<?, BundleDescription>> _entryCalls = config.<BundleDescription>getEntryCalls(it);
                final MappingCall<?, BundleDescription> call = IterableExtensions.head(_entryCalls);
                IEditorPart _activeEditor = page.getActiveEditor();
                ((FXDiagramView)view).<BundleDescription>revealElement(it, call, _activeEditor);
              }
            };
            IterableExtensions.<BundleDescription>forEach(bundleDescriptions, _function);
          }
        }
        _xblockexpression = null;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
