package de.fxdiagram.pde;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.lib.chooser.CoverFlowChoice;
import de.fxdiagram.lib.chooser.NodeChooser;
import de.fxdiagram.pde.PluginDiagramConfig;
import de.fxdiagram.pde.PluginUtil;
import de.fxdiagram.xtext.glue.FXDiagramView;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor;
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import java.util.List;
import java.util.function.Consumer;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class AddPluginHandler extends AbstractHandler {
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    Object _xblockexpression = null;
    {
      IWorkbench _workbench = PlatformUI.getWorkbench();
      IWorkbenchWindow _activeWorkbenchWindow = _workbench.getActiveWorkbenchWindow();
      final IWorkbenchPage page = _activeWorkbenchWindow.getActivePage();
      final IViewPart view = page.findView("org.eclipse.xtext.glue.FXDiagramView");
      if ((view instanceof FXDiagramView)) {
        XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
        XDiagramConfig _configByID = _instance.getConfigByID("de.fxdiagram.pde.PluginDiagramConfig");
        final PluginDiagramConfig config = ((PluginDiagramConfig) _configByID);
        AbstractMapping<?> _mappingByID = config.getMappingByID("pluginNode");
        final NodeMapping<IPluginModelBase> nodeMapping = ((NodeMapping<IPluginModelBase>) _mappingByID);
        final XRoot root = ((FXDiagramView)view).getRoot();
        XDiagram _diagram = root.getDiagram();
        Scene _scene = root.getScene();
        double _width = _scene.getWidth();
        double _multiply = (0.5 * _width);
        Scene _scene_1 = root.getScene();
        double _height = _scene_1.getHeight();
        double _multiply_1 = (0.5 * _height);
        final Point2D center = _diagram.sceneToLocal(_multiply, _multiply_1);
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
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
}
