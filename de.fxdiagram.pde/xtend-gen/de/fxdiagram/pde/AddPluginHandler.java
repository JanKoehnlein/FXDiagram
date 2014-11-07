package de.fxdiagram.pde;

import com.google.common.base.Objects;
import com.google.common.collect.Iterators;
import de.fxdiagram.pde.PluginDiagramConfig;
import de.fxdiagram.xtext.glue.FXDiagramView;
import de.fxdiagram.xtext.glue.mapping.MappingCall;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class AddPluginHandler extends AbstractHandler {
  public void setEnabled(final Object evaluationContext) {
    if ((evaluationContext instanceof IEvaluationContext)) {
      final Object selection = ((IEvaluationContext)evaluationContext).getVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME);
      Set<IPluginModelBase> _pluginBases = this.getPluginBases(selection);
      boolean _notEquals = (!Objects.equal(_pluginBases, null));
      this.setBaseEnabled(_notEquals);
    } else {
      super.setEnabled(evaluationContext);
    }
  }
  
  protected Set<IPluginModelBase> getPluginBases(final Object selection) {
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
      final Function1<IProject, IPluginModelBase> _function_1 = new Function1<IProject, IPluginModelBase>() {
        public IPluginModelBase apply(final IProject it) {
          return PluginRegistry.findModel(it);
        }
      };
      Iterator<IPluginModelBase> _map_1 = IteratorExtensions.<IProject, IPluginModelBase>map(_map, _function_1);
      return IteratorExtensions.<IPluginModelBase>toSet(_map_1);
    } else {
      return Collections.<IPluginModelBase>unmodifiableSet(CollectionLiterals.<IPluginModelBase>newHashSet());
    }
  }
  
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    Object _xblockexpression = null;
    {
      final ISelection selection = HandlerUtil.getActiveMenuSelection(event);
      final Set<IPluginModelBase> pluginBases = this.getPluginBases(selection);
      boolean _isEmpty = pluginBases.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
        XDiagramConfig _configByID = _instance.getConfigByID("de.fxdiagram.pde.PluginDiagramConfig");
        final PluginDiagramConfig config = ((PluginDiagramConfig) _configByID);
        IWorkbench _workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow _activeWorkbenchWindow = _workbench.getActiveWorkbenchWindow();
        final IWorkbenchPage page = _activeWorkbenchWindow.getActivePage();
        final IViewPart view = page.findView("org.eclipse.xtext.glue.FXDiagramView");
        if ((view instanceof FXDiagramView)) {
          final Consumer<IPluginModelBase> _function = new Consumer<IPluginModelBase>() {
            public void accept(final IPluginModelBase it) {
              Iterable<? extends MappingCall<?, IPluginModelBase>> _entryCalls = config.<IPluginModelBase>getEntryCalls(it);
              final MappingCall<?, IPluginModelBase> call = IterableExtensions.head(_entryCalls);
              IEditorPart _activeEditor = page.getActiveEditor();
              ((FXDiagramView)view).<IPluginModelBase>revealElement(it, call, _activeEditor);
            }
          };
          pluginBases.forEach(_function);
        }
      }
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
}
