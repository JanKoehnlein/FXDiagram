package de.fxdiagram.pde;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.eclipse.FXDiagramView;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.eclipse.mapping.NodeMapping;
import de.fxdiagram.eclipse.mapping.XDiagramConfig;
import de.fxdiagram.lib.chooser.CoverFlowChoice;
import de.fxdiagram.lib.chooser.NodeChooser;
import de.fxdiagram.pde.BundleDiagramConfig;
import de.fxdiagram.pde.BundleUtil;
import de.fxdiagram.pde.HandlerHelper;
import java.util.List;
import java.util.function.Consumer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("all")
public class AddBundleHandler extends AbstractHandler {
  private EventHandler<MouseEvent> mouseHandler = ((EventHandler<MouseEvent>) (MouseEvent it) -> {
    int _clickCount = it.getClickCount();
    boolean _equals = (_clickCount == 2);
    if (_equals) {
      final IViewPart view = this.getDiagramView();
      if ((view instanceof FXDiagramView)) {
        XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
        XDiagramConfig _configByID = _instance.getConfigByID("de.fxdiagram.pde.BundleDiagramConfig");
        final BundleDiagramConfig config = ((BundleDiagramConfig) _configByID);
        final XRoot root = ((FXDiagramView)view).getCurrentRoot();
        XDiagram _diagram = root.getDiagram();
        double _sceneX = it.getSceneX();
        double _sceneY = it.getSceneY();
        final Point2D center = _diagram.sceneToLocal(_sceneX, _sceneY);
        XDiagram _diagram_1 = root.getDiagram();
        CoverFlowChoice _coverFlowChoice = new CoverFlowChoice();
        final NodeChooser nodeChooser = new NodeChooser(_diagram_1, center, _coverFlowChoice, false);
        List<BundleDescription> _allBundles = BundleUtil.allBundles();
        final Consumer<BundleDescription> _function = (BundleDescription it_1) -> {
          IMappedElementDescriptorProvider _domainObjectProvider = config.getDomainObjectProvider();
          NodeMapping<BundleDescription> _pluginNode = config.getPluginNode();
          final IMappedElementDescriptor<BundleDescription> descriptor = _domainObjectProvider.<BundleDescription>createMappedElementDescriptor(it_1, _pluginNode);
          NodeMapping<BundleDescription> _pluginNode_1 = config.getPluginNode();
          XNode _createNode = _pluginNode_1.createNode(descriptor);
          nodeChooser.addChoice(_createNode);
        };
        _allBundles.forEach(_function);
        root.setCurrentTool(nodeChooser);
      }
    }
  });
  
  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    Object _xblockexpression = null;
    {
      final IViewPart view = this.getDiagramView();
      if ((view instanceof FXDiagramView)) {
        boolean _isWidgetChecked = HandlerHelper.isWidgetChecked(event);
        if (_isWidgetChecked) {
          ((FXDiagramView)view).<MouseEvent>addGlobalEventHandler(MouseEvent.MOUSE_CLICKED, this.mouseHandler);
        } else {
          ((FXDiagramView)view).<MouseEvent>removeGlobalEventHandler(MouseEvent.MOUSE_CLICKED, this.mouseHandler);
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
