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
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.buttons.RapidButtonAction;
import de.fxdiagram.lib.chooser.AbstractChoiceGraphics;
import de.fxdiagram.lib.chooser.CarusselChoice;
import de.fxdiagram.lib.chooser.ChooserConnectionProvider;
import de.fxdiagram.lib.chooser.ConnectedNodeChooser;
import de.fxdiagram.lib.chooser.CoverFlowChoice;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.InterpreterContext;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.XDiagramConfigInterpreter;
import de.fxdiagram.pde.BundleDependency;
import de.fxdiagram.pde.BundleDescriptor;
import de.fxdiagram.pde.BundleDiagramConfig;
import de.fxdiagram.pde.BundleUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;
import javafx.geometry.Side;
import javafx.util.Duration;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddDependencyPathAction extends RapidButtonAction {
  private boolean isInverse;
  
  public AddDependencyPathAction(final boolean isInverse) {
    this.isInverse = isInverse;
  }
  
  @Override
  public boolean isEnabled(final XNode host) {
    DomainObjectDescriptor _domainObjectDescriptor = host.getDomainObjectDescriptor();
    final Function1<BundleDescription, Boolean> _function = (BundleDescription it) -> {
      boolean _xifexpression = false;
      if (this.isInverse) {
        Iterable<BundleDescription> _dependentBundles = BundleUtil.getDependentBundles(it);
        boolean _isEmpty = IterableExtensions.isEmpty(_dependentBundles);
        _xifexpression = (!_isEmpty);
      } else {
        Iterable<BundleDescription> _dependencyBundles = BundleUtil.getDependencyBundles(it);
        boolean _isEmpty_1 = IterableExtensions.isEmpty(_dependencyBundles);
        _xifexpression = (!_isEmpty_1);
      }
      return Boolean.valueOf(_xifexpression);
    };
    return (((BundleDescriptor) _domainObjectDescriptor).<Boolean>withDomainObject(_function)).booleanValue();
  }
  
  @Override
  public void perform(final RapidButton button) {
    XNode _host = button.getHost();
    DomainObjectDescriptor _domainObjectDescriptor = _host.getDomainObjectDescriptor();
    final BundleDescriptor descriptor = ((BundleDescriptor) _domainObjectDescriptor);
    final Function1<BundleDescription, Object> _function = (BundleDescription it) -> {
      return this.doPerform(button, it);
    };
    descriptor.<Object>withDomainObject(_function);
  }
  
  public Object doPerform(final RapidButton button, final BundleDescription hostBundle) {
    Object _xblockexpression = null;
    {
      XNode _host = button.getHost();
      final XRoot root = CoreExtensions.getRoot(_host);
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
      final XDiagramConfigInterpreter interpreter = new XDiagramConfigInterpreter();
      Side _position_1 = button.getPosition();
      final ConnectedNodeChooser chooser = new ConnectedNodeChooser(host, _position_1, choiceGraphics) {
        @Override
        public Iterable<? extends XShape> getAdditionalShapesToAdd(final XNode choice, final DomainObjectDescriptor choiceInfo) {
          Iterable<? extends XShape> _additionalShapesToAdd = super.getAdditionalShapesToAdd(choice, choiceInfo);
          Iterable<XConnection> _filter = Iterables.<XConnection>filter(_additionalShapesToAdd, XConnection.class);
          for (final XConnection it : _filter) {
            this.removeConnection(it);
          }
          final XDiagram diagram = CoreExtensions.getDiagram(host);
          final InterpreterContext context = new InterpreterContext(diagram);
          context.addNode(choice);
          DomainObjectDescriptor _domainObjectDescriptor = choice.getDomainObjectDescriptor();
          final Function1<BundleDescription, ArrayList<BundleDependency>> _function = (BundleDescription chosenBundle) -> {
            ArrayList<BundleDependency> _xifexpression = null;
            if (AddDependencyPathAction.this.isInverse) {
              _xifexpression = BundleUtil.getAllBundleDependencies(chosenBundle, hostBundle);
            } else {
              _xifexpression = BundleUtil.getAllBundleDependencies(hostBundle, chosenBundle);
            }
            return _xifexpression;
          };
          ArrayList<BundleDependency> _withDomainObject = ((BundleDescriptor) _domainObjectDescriptor).<ArrayList<BundleDependency>>withDomainObject(_function);
          final Consumer<BundleDependency> _function_1 = (BundleDependency bundleDependency) -> {
            context.setIsCreateConnections(false);
            BundleDescription _owner = bundleDependency.getOwner();
            NodeMapping<BundleDescription> _pluginNode = config.getPluginNode();
            final XNode source = interpreter.<BundleDescription>createNode(_owner, _pluginNode, context);
            BundleDescription _dependency = bundleDependency.getDependency();
            NodeMapping<BundleDescription> _pluginNode_1 = config.getPluginNode();
            final XNode target = interpreter.<BundleDescription>createNode(_dependency, _pluginNode_1, context);
            context.setIsCreateConnections(true);
            ConnectionMapping<BundleDependency> _dependencyConnection = config.getDependencyConnection();
            final Procedure1<XConnection> _function_2 = (XConnection it_1) -> {
              it_1.setSource(source);
              it_1.setTarget(target);
            };
            interpreter.<BundleDependency>createConnection(bundleDependency, _dependencyConnection, _function_2, context);
          };
          _withDomainObject.forEach(_function_1);
          return context.getAddedShapes();
        }
        
        @Override
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
        final ChooserConnectionProvider _function = (XNode source, XNode target, DomainObjectDescriptor choiceInfo) -> {
          return new XConnection(target, source);
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
      XDiagram _diagram = CoreExtensions.getDiagram(host);
      InterpreterContext _interpreterContext = new InterpreterContext(_diagram);
      final Procedure1<InterpreterContext> _function_1 = (InterpreterContext it) -> {
        it.setIsCreateConnections(false);
        it.setIsCreateDuplicateNodes(true);
      };
      final InterpreterContext context = ObjectExtensions.<InterpreterContext>operator_doubleArrow(_interpreterContext, _function_1);
      final Consumer<BundleDescription> _function_2 = (BundleDescription it) -> {
        NodeMapping<BundleDescription> _pluginNode = config.getPluginNode();
        final XNode candidateNode = interpreter.<BundleDescription>createNode(it, _pluginNode, context);
        chooser.addChoice(candidateNode);
      };
      candidates.forEach(_function_2);
      root.setCurrentTool(chooser);
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
}
