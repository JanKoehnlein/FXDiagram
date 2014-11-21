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
import de.fxdiagram.pde.BundleDependency;
import de.fxdiagram.pde.BundleDescriptor;
import de.fxdiagram.pde.BundleDescriptorProvider;
import de.fxdiagram.pde.BundleDiagramConfig;
import de.fxdiagram.pde.BundleUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.util.Duration;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddDependencyPathAction extends RapidButtonAction {
  private boolean isInverse;
  
  public AddDependencyPathAction(final boolean isInverse) {
    this.isInverse = isInverse;
  }
  
  public boolean isEnabled(final XNode host) {
    DomainObjectDescriptor _domainObject = host.getDomainObject();
    final Function1<BundleDescription, Boolean> _function = new Function1<BundleDescription, Boolean>() {
      public Boolean apply(final BundleDescription it) {
        boolean _xifexpression = false;
        if (AddDependencyPathAction.this.isInverse) {
          Iterable<BundleDescription> _dependentBundles = BundleUtil.getDependentBundles(it);
          boolean _isEmpty = IterableExtensions.isEmpty(_dependentBundles);
          _xifexpression = (!_isEmpty);
        } else {
          Iterable<BundleDescription> _dependencyBundles = BundleUtil.getDependencyBundles(it);
          boolean _isEmpty_1 = IterableExtensions.isEmpty(_dependencyBundles);
          _xifexpression = (!_isEmpty_1);
        }
        return Boolean.valueOf(_xifexpression);
      }
    };
    return (((BundleDescriptor) _domainObject).<Boolean>withDomainObject(_function)).booleanValue();
  }
  
  public void perform(final RapidButton button) {
    XNode _host = button.getHost();
    DomainObjectDescriptor _domainObject = _host.getDomainObject();
    final BundleDescriptor descriptor = ((BundleDescriptor) _domainObject);
    final Function1<BundleDescription, Object> _function = new Function1<BundleDescription, Object>() {
      public Object apply(final BundleDescription it) {
        return AddDependencyPathAction.this.doPerform(button, it);
      }
    };
    descriptor.<Object>withDomainObject(_function);
  }
  
  public Object doPerform(final RapidButton button, final BundleDescription hostBundle) {
    Object _xblockexpression = null;
    {
      XNode _host = button.getHost();
      final XRoot root = CoreExtensions.getRoot(_host);
      final BundleDescriptorProvider provider = root.<BundleDescriptorProvider>getDomainObjectProvider(BundleDescriptorProvider.class);
      XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
      XDiagramConfig _configByID = _instance.getConfigByID("de.fxdiagram.pde.BundleDiagramConfig");
      final BundleDiagramConfig config = ((BundleDiagramConfig) _configByID);
      final XNode host = button.getHost();
      AbstractChoiceGraphics _xifexpression = null;
      Side _position = button.getPosition();
      boolean _isVertical = _position.isVertical();
      if (_isVertical) {
        _xifexpression = new CarusselChoice();
      } else {
        _xifexpression = new CoverFlowChoice();
      }
      final AbstractChoiceGraphics choiceGraphics = _xifexpression;
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
          ObservableList<XNode> _nodes = diagram.getNodes();
          final Function1<XNode, DomainObjectDescriptor> _function = new Function1<XNode, DomainObjectDescriptor>() {
            public DomainObjectDescriptor apply(final XNode it) {
              return it.getDomainObject();
            }
          };
          final Map<DomainObjectDescriptor, XNode> descriptor2node = IterableExtensions.<DomainObjectDescriptor, XNode>toMap(_nodes, _function);
          DomainObjectDescriptor _domainObject = choice.getDomainObject();
          descriptor2node.put(_domainObject, choice);
          ObservableList<XConnection> _connections = diagram.getConnections();
          final Function1<XConnection, DomainObjectDescriptor> _function_1 = new Function1<XConnection, DomainObjectDescriptor>() {
            public DomainObjectDescriptor apply(final XConnection it) {
              return it.getDomainObject();
            }
          };
          final Map<DomainObjectDescriptor, XConnection> descriptor2connection = IterableExtensions.<DomainObjectDescriptor, XConnection>toMap(_connections, _function_1);
          DomainObjectDescriptor _domainObject_1 = choice.getDomainObject();
          final Function1<BundleDescription, ArrayList<BundleDependency>> _function_2 = new Function1<BundleDescription, ArrayList<BundleDependency>>() {
            public ArrayList<BundleDependency> apply(final BundleDescription chosenBundle) {
              ArrayList<BundleDependency> _xifexpression = null;
              if (AddDependencyPathAction.this.isInverse) {
                _xifexpression = BundleUtil.getAllBundleDependencies(chosenBundle, hostBundle);
              } else {
                _xifexpression = BundleUtil.getAllBundleDependencies(hostBundle, chosenBundle);
              }
              return _xifexpression;
            }
          };
          ArrayList<BundleDependency> _withDomainObject = ((BundleDescriptor) _domainObject_1).<ArrayList<BundleDependency>>withDomainObject(_function_2);
          final Procedure1<BundleDependency> _function_3 = new Procedure1<BundleDependency>() {
            public void apply(final BundleDependency bundleDependency) {
              BundleDescription _owner = bundleDependency.getOwner();
              NodeMapping<BundleDescription> _pluginNode = config.getPluginNode();
              IMappedElementDescriptor<BundleDescription> _createMappedElementDescriptor = provider.<BundleDescription>createMappedElementDescriptor(_owner, _pluginNode);
              final BundleDescriptor owner = ((BundleDescriptor) _createMappedElementDescriptor);
              XNode sourceNode = descriptor2node.get(owner);
              boolean _equals = Objects.equal(sourceNode, null);
              if (_equals) {
                NodeMapping<BundleDescription> _pluginNode_1 = config.getPluginNode();
                XNode _createNode = _pluginNode_1.createNode(owner);
                sourceNode = _createNode;
                additionalShapes.add(sourceNode);
                descriptor2node.put(owner, sourceNode);
              }
              BundleDescription _dependency = bundleDependency.getDependency();
              NodeMapping<BundleDescription> _pluginNode_2 = config.getPluginNode();
              IMappedElementDescriptor<BundleDescription> _createMappedElementDescriptor_1 = provider.<BundleDescription>createMappedElementDescriptor(_dependency, _pluginNode_2);
              final BundleDescriptor dependency = ((BundleDescriptor) _createMappedElementDescriptor_1);
              XNode targetNode = descriptor2node.get(dependency);
              boolean _equals_1 = Objects.equal(targetNode, null);
              if (_equals_1) {
                NodeMapping<BundleDescription> _pluginNode_3 = config.getPluginNode();
                XNode _createNode_1 = _pluginNode_3.createNode(dependency);
                targetNode = _createNode_1;
                additionalShapes.add(targetNode);
                descriptor2node.put(dependency, targetNode);
              }
              ConnectionMapping<BundleDependency> _dependencyConnection = config.getDependencyConnection();
              final IMappedElementDescriptor<BundleDependency> connectionDescriptor = provider.<BundleDependency>createMappedElementDescriptor(bundleDependency, _dependencyConnection);
              XConnection connection = descriptor2connection.get(connectionDescriptor);
              boolean _equals_2 = Objects.equal(connection, null);
              if (_equals_2) {
                ConnectionMapping<BundleDependency> _dependencyConnection_1 = config.getDependencyConnection();
                XConnection _createConnection = _dependencyConnection_1.createConnection(connectionDescriptor);
                connection = _createConnection;
                connection.setSource(sourceNode);
                connection.setTarget(targetNode);
                additionalShapes.add(connection);
                descriptor2connection.put(connectionDescriptor, connection);
              }
            }
          };
          IterableExtensions.<BundleDependency>forEach(_withDomainObject, _function_3);
          return additionalShapes;
        }
        
        protected void nodeChosen(final XNode choice) {
          super.nodeChosen(choice);
          boolean _notEquals = (!Objects.equal(choice, null));
          if (_notEquals) {
            XRoot _root = this.getRoot();
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
      if (this.isInverse) {
        final ChooserConnectionProvider _function = new ChooserConnectionProvider() {
          public XConnection getConnection(final XNode source, final XNode target, final DomainObjectDescriptor choiceInfo) {
            return new XConnection(target, source);
          }
        };
        chooser.setConnectionProvider(_function);
      }
      HashSet<BundleDescription> _xifexpression_1 = null;
      if (this.isInverse) {
        _xifexpression_1 = BundleUtil.getAllDependentBundles(hostBundle);
      } else {
        _xifexpression_1 = BundleUtil.getAllDependencyBundles(hostBundle);
      }
      final HashSet<BundleDescription> candidates = _xifexpression_1;
      final Procedure1<BundleDescription> _function_1 = new Procedure1<BundleDescription>() {
        public void apply(final BundleDescription it) {
          NodeMapping<BundleDescription> _pluginNode = config.getPluginNode();
          IMappedElementDescriptor<BundleDescription> _createMappedElementDescriptor = provider.<BundleDescription>createMappedElementDescriptor(it, _pluginNode);
          final BundleDescriptor candidate = ((BundleDescriptor) _createMappedElementDescriptor);
          NodeMapping<BundleDescription> _pluginNode_1 = config.getPluginNode();
          XNode _createNode = _pluginNode_1.createNode(candidate);
          chooser.addChoice(_createNode);
        }
      };
      IterableExtensions.<BundleDescription>forEach(candidates, _function_1);
      root.setCurrentTool(chooser);
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
}
