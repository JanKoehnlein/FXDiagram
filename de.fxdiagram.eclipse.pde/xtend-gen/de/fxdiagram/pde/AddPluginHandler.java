package de.fxdiagram.pde;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.eclipse.FXDiagramView;
import de.fxdiagram.eclipse.mapping.AbstractMapping;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.eclipse.mapping.NodeMapping;
import de.fxdiagram.eclipse.mapping.XDiagramConfig;
import de.fxdiagram.lib.chooser.CoverFlowChoice;
import de.fxdiagram.lib.chooser.NodeChooser;
import de.fxdiagram.pde.PluginDiagramConfig;
import de.fxdiagram.pde.PluginUtil;
import java.util.List;
import java.util.function.Consumer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class AddPluginHandler extends AbstractHandler {
  private EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
    public void handle(final MouseEvent it) {
      int _clickCount = it.getClickCount();
      boolean _equals = (_clickCount == 2);
      if (_equals) {
        final IViewPart view = AddPluginHandler.this.getDiagramView();
        if ((view instanceof FXDiagramView)) {
          XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
          XDiagramConfig _configByID = _instance.getConfigByID("de.fxdiagram.pde.PluginDiagramConfig");
          final PluginDiagramConfig config = ((PluginDiagramConfig) _configByID);
          AbstractMapping<?> _mappingByID = config.getMappingByID("pluginNode");
          final NodeMapping<IPluginModelBase> nodeMapping = ((NodeMapping<IPluginModelBase>) _mappingByID);
          final XRoot root = ((FXDiagramView)view).getRoot();
          XDiagram _diagram = root.getDiagram();
          double _sceneX = it.getSceneX();
          double _sceneY = it.getSceneY();
          final Point2D center = _diagram.sceneToLocal(_sceneX, _sceneY);
          XDiagram _diagram_1 = root.getDiagram();
          CoverFlowChoice _coverFlowChoice = new CoverFlowChoice();
          final NodeChooser nodeChooser = new NodeChooser(_diagram_1, center, _coverFlowChoice, false);
          IPluginModelBase[] _allPlugins = PluginUtil.allPlugins();
          final Consumer<IPluginModelBase> _function = new Consumer<IPluginModelBase>() {
            public void accept(final IPluginModelBase it) {
              IMappedElementDescriptorProvider _domainObjectProvider = config.getDomainObjectProvider();
              final IMappedElementDescriptor<IPluginModelBase> descriptor = _domainObjectProvider.<IPluginModelBase>createMappedElementDescriptor(it, nodeMapping);
              XNode _createNode = nodeMapping.createNode(descriptor);
              nodeChooser.addChoice(_createNode);
            }
          };
          ((List<IPluginModelBase>)Conversions.doWrapArray(_allPlugins)).forEach(_function);
          root.setCurrentTool(nodeChooser);
        }
      }
    }
  };
  
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    Object _xblockexpression = null;
    {
      Object _trigger = event.getTrigger();
      final boolean selected = ((ToolItem) ((Event) _trigger).widget).getSelection();
      final IViewPart view = this.getDiagramView();
      if ((view instanceof FXDiagramView)) {
        if (selected) {
          XRoot _root = ((FXDiagramView)view).getRoot();
          _root.<MouseEvent>addEventHandler(MouseEvent.MOUSE_CLICKED, this.mouseHandler);
        } else {
          XRoot _root_1 = ((FXDiagramView)view).getRoot();
          _root_1.<MouseEvent>removeEventHandler(MouseEvent.MOUSE_CLICKED, this.mouseHandler);
        }
      }
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
  
  protected IViewPart getDiagramView() {
    IWorkbench _workbench = PlatformUI.getWorkbench();
    IWorkbenchWindow _activeWorkbenchWindow = null;
    if (_workbench!=null) {
      _activeWorkbenchWindow=_workbench.getActiveWorkbenchWindow();
    }
    IWorkbenchPage _activePage = null;
    if (_activeWorkbenchWindow!=null) {
      _activePage=_activeWorkbenchWindow.getActivePage();
    }
    final IWorkbenchPage page = _activePage;
    IViewPart _findView = null;
    if (page!=null) {
      _findView=page.findView("de.fxdiagram.eclipse.FXDiagramView");
    }
    final IViewPart view = _findView;
    return view;
  }
}
