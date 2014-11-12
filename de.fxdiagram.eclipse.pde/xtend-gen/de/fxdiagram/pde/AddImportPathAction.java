package de.fxdiagram.pde;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.LazyCommand;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.eclipse.mapping.AbstractMapping;
import de.fxdiagram.eclipse.mapping.ConnectionMapping;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor;
import de.fxdiagram.eclipse.mapping.NodeMapping;
import de.fxdiagram.eclipse.mapping.XDiagramConfig;
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.buttons.RapidButtonAction;
import de.fxdiagram.lib.chooser.AbstractChoiceGraphics;
import de.fxdiagram.lib.chooser.CarusselChoice;
import de.fxdiagram.lib.chooser.ChooserConnectionProvider;
import de.fxdiagram.lib.chooser.ConnectedNodeChooser;
import de.fxdiagram.lib.chooser.CoverFlowChoice;
import de.fxdiagram.pde.PluginDependencyPath;
import de.fxdiagram.pde.PluginDescriptor;
import de.fxdiagram.pde.PluginDescriptorProvider;
import de.fxdiagram.pde.PluginUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.util.Duration;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginImport;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class AddImportPathAction extends RapidButtonAction {
  private boolean isImport;
  
  public AddImportPathAction(final boolean isImport) {
    this.isImport = isImport;
  }
  
  public void perform(final RapidButton button) {
    XNode _host = button.getHost();
    DomainObjectDescriptor _domainObject = _host.getDomainObject();
    final PluginDescriptor descriptor = ((PluginDescriptor) _domainObject);
    final Function1<IPluginModelBase, Object> _function = new Function1<IPluginModelBase, Object>() {
      public Object apply(final IPluginModelBase it) {
        return AddImportPathAction.this.doPerform(button, it);
      }
    };
    descriptor.<Object>withDomainObject(_function);
  }
  
  public Object doPerform(final RapidButton button, final IPluginModelBase it) {
    Object _xblockexpression = null;
    {
      ArrayList<PluginDependencyPath> _xifexpression = null;
      if (this.isImport) {
        _xifexpression = PluginUtil.getAllImportPaths(it);
      } else {
        _xifexpression = PluginUtil.getAllDependentsPaths(it);
      }
      final ArrayList<PluginDependencyPath> paths = _xifexpression;
      XNode _host = button.getHost();
      final XRoot root = CoreExtensions.getRoot(_host);
      final PluginDescriptorProvider provider = root.<PluginDescriptorProvider>getDomainObjectProvider(PluginDescriptorProvider.class);
      XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
      final XDiagramConfig diagramConfig = _instance.getConfigByID("de.fxdiagram.pde.PluginDiagramConfig");
      AbstractMapping<?> _mappingByID = diagramConfig.getMappingByID("pluginNode");
      final NodeMapping<IPluginModelBase> pluginNodeMapping = ((NodeMapping<IPluginModelBase>) _mappingByID);
      AbstractMapping<?> _mappingByID_1 = diagramConfig.getMappingByID("importConnection");
      final ConnectionMapping<IPluginImport> importConnectionMapping = ((ConnectionMapping<IPluginImport>) _mappingByID_1);
      final XNode host = button.getHost();
      AbstractChoiceGraphics _xifexpression_1 = null;
      Side _position = button.getPosition();
      boolean _isVertical = _position.isVertical();
      if (_isVertical) {
        _xifexpression_1 = new CarusselChoice();
      } else {
        _xifexpression_1 = new CoverFlowChoice();
      }
      final AbstractChoiceGraphics choiceGraphics = _xifexpression_1;
      Side _position_1 = button.getPosition();
      final ConnectedNodeChooser chooser = new ConnectedNodeChooser(host, _position_1, choiceGraphics) {
        public Iterable<? extends XShape> getAdditionalShapesToAdd(final XNode choice, final DomainObjectDescriptor choiceInfo) {
          Iterable<? extends XShape> _additionalShapesToAdd = super.getAdditionalShapesToAdd(choice, choiceInfo);
          Iterable<XConnection> _filter = Iterables.<XConnection>filter(_additionalShapesToAdd, XConnection.class);
          for (final XConnection it : _filter) {
            this.removeConnection(it);
          }
          final XDiagram diagram = CoreExtensions.getDiagram(host);
          final LinkedHashSet<XShape> additionalShapes = CollectionLiterals.<XShape>newLinkedHashSet();
          DomainObjectDescriptor _domainObject = choice.getDomainObject();
          final Function1<IPluginModelBase, Object> _function = new Function1<IPluginModelBase, Object>() {
            public Object apply(final IPluginModelBase chosenPlugin) {
              Object _xblockexpression = null;
              {
                final Function1<PluginDependencyPath, Boolean> _function = new Function1<PluginDependencyPath, Boolean>() {
                  public Boolean apply(final PluginDependencyPath it) {
                    List<? extends PluginDependencyPath.PathElement> _elements = it.getElements();
                    PluginDependencyPath.PathElement _last = IterableExtensions.last(_elements);
                    IPluginModelBase _plugin = _last.getPlugin();
                    return Boolean.valueOf(Objects.equal(_plugin, chosenPlugin));
                  }
                };
                Iterable<PluginDependencyPath> _filter = IterableExtensions.<PluginDependencyPath>filter(paths, _function);
                final Consumer<PluginDependencyPath> _function_1 = new Consumer<PluginDependencyPath>() {
                  public void accept(final PluginDependencyPath path) {
                    XNode source = host;
                    List<? extends PluginDependencyPath.PathElement> _elements = path.getElements();
                    for (final PluginDependencyPath.PathElement pathElement : _elements) {
                      {
                        IPluginModelBase _plugin = pathElement.getPlugin();
                        IMappedElementDescriptor<IPluginModelBase> _createMappedElementDescriptor = provider.<IPluginModelBase>createMappedElementDescriptor(_plugin, pluginNodeMapping);
                        final PluginDescriptor midDescriptor = ((PluginDescriptor) _createMappedElementDescriptor);
                        ObservableList<XNode> _nodes = diagram.getNodes();
                        Iterable<XShape> _plus = Iterables.<XShape>concat(_nodes, additionalShapes);
                        Iterable<XShape> _plus_1 = Iterables.<XShape>concat(_plus, Collections.<XNode>unmodifiableList(CollectionLiterals.<XNode>newArrayList(choice)));
                        Iterable<XNode> _filter = Iterables.<XNode>filter(_plus_1, XNode.class);
                        final Function1<XNode, Boolean> _function = new Function1<XNode, Boolean>() {
                          public Boolean apply(final XNode it) {
                            DomainObjectDescriptor _domainObject = it.getDomainObject();
                            return Boolean.valueOf(Objects.equal(_domainObject, midDescriptor));
                          }
                        };
                        XNode target = IterableExtensions.<XNode>findFirst(_filter, _function);
                        boolean _equals = Objects.equal(target, null);
                        if (_equals) {
                          XNode _createNode = pluginNodeMapping.createNode(midDescriptor);
                          target = _createNode;
                          additionalShapes.add(target);
                        }
                        IPluginImport _pluginImport = pathElement.getPluginImport();
                        final IMappedElementDescriptor<IPluginImport> connectionDescriptor = provider.<IPluginImport>createMappedElementDescriptor(_pluginImport, importConnectionMapping);
                        ObservableList<XConnection> _connections = diagram.getConnections();
                        Iterable<XShape> _plus_2 = Iterables.<XShape>concat(_connections, additionalShapes);
                        Iterable<XConnection> _filter_1 = Iterables.<XConnection>filter(_plus_2, XConnection.class);
                        final Function1<XConnection, Boolean> _function_1 = new Function1<XConnection, Boolean>() {
                          public Boolean apply(final XConnection it) {
                            DomainObjectDescriptor _domainObject = it.getDomainObject();
                            return Boolean.valueOf(Objects.equal(_domainObject, connectionDescriptor));
                          }
                        };
                        XConnection connection = IterableExtensions.<XConnection>findFirst(_filter_1, _function_1);
                        boolean _equals_1 = Objects.equal(connection, null);
                        if (_equals_1) {
                          XConnection _createConnection = importConnectionMapping.createConnection(connectionDescriptor);
                          connection = _createConnection;
                          if (AddImportPathAction.this.isImport) {
                            connection.setSource(source);
                            connection.setTarget(target);
                          } else {
                            connection.setSource(target);
                            connection.setTarget(source);
                          }
                          additionalShapes.add(connection);
                        }
                        source = target;
                      }
                    }
                  }
                };
                _filter.forEach(_function_1);
                _xblockexpression = null;
              }
              return _xblockexpression;
            }
          };
          ((PluginDescriptor) _domainObject).<Object>withDomainObject(_function);
          return additionalShapes;
        }
        
        protected void nodeChosen(final XNode choice) {
          super.nodeChosen(choice);
          boolean _notEquals = (!Objects.equal(choice, null));
          if (_notEquals) {
            XRoot _root = CoreExtensions.getRoot(host);
            CommandStack _commandStack = _root.getCommandStack();
            Layouter _layouter = new Layouter();
            XRoot _root_1 = CoreExtensions.getRoot(host);
            XDiagram _diagram = _root_1.getDiagram();
            Duration _millis = DurationExtensions.millis(500);
            LazyCommand _createLayoutCommand = _layouter.createLayoutCommand(LayoutType.DOT, _diagram, _millis);
            _commandStack.execute(_createLayoutCommand);
          }
        }
      };
      if ((!this.isImport)) {
        final ChooserConnectionProvider _function = new ChooserConnectionProvider() {
          public XConnection getConnection(final XNode source, final XNode target, final DomainObjectDescriptor choiceInfo) {
            return new XConnection(target, source);
          }
        };
        chooser.setConnectionProvider(_function);
      }
      final Function1<PluginDependencyPath, IPluginModelBase> _function_1 = new Function1<PluginDependencyPath, IPluginModelBase>() {
        public IPluginModelBase apply(final PluginDependencyPath it) {
          List<? extends PluginDependencyPath.PathElement> _elements = it.getElements();
          PluginDependencyPath.PathElement _last = IterableExtensions.last(_elements);
          return _last.getPlugin();
        }
      };
      List<IPluginModelBase> _map = ListExtensions.<PluginDependencyPath, IPluginModelBase>map(paths, _function_1);
      Set<IPluginModelBase> _set = IterableExtensions.<IPluginModelBase>toSet(_map);
      final Function1<IPluginModelBase, String> _function_2 = new Function1<IPluginModelBase, String>() {
        public String apply(final IPluginModelBase it) {
          IPluginBase _pluginBase = it.getPluginBase();
          return _pluginBase.getId();
        }
      };
      List<IPluginModelBase> _sortBy = IterableExtensions.<IPluginModelBase, String>sortBy(_set, _function_2);
      final Consumer<IPluginModelBase> _function_3 = new Consumer<IPluginModelBase>() {
        public void accept(final IPluginModelBase it) {
          IMappedElementDescriptor<IPluginModelBase> _createMappedElementDescriptor = provider.<IPluginModelBase>createMappedElementDescriptor(it, pluginNodeMapping);
          final PluginDescriptor leafDescriptor = ((PluginDescriptor) _createMappedElementDescriptor);
          XNode _createNode = pluginNodeMapping.createNode(leafDescriptor);
          chooser.addChoice(_createNode);
        }
      };
      _sortBy.forEach(_function_3);
      root.setCurrentTool(chooser);
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
}
