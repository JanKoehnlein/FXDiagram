package de.fxdiagram.pde;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.buttons.RapidButtonAction;
import de.fxdiagram.lib.chooser.AbstractChoiceGraphics;
import de.fxdiagram.lib.chooser.CarusselChoice;
import de.fxdiagram.lib.chooser.ChooserConnectionProvider;
import de.fxdiagram.lib.chooser.ConnectedNodeChooser;
import de.fxdiagram.lib.chooser.CoverFlowChoice;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.InterpreterContext;
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter;
import de.fxdiagram.pde.BundleDependency;
import de.fxdiagram.pde.BundleDescriptor;
import de.fxdiagram.pde.BundleDiagramConfig;
import de.fxdiagram.pde.BundleUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import javafx.geometry.Side;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
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
        boolean _isEmpty = IterableExtensions.isEmpty(BundleUtil.getDependentBundles(it));
        _xifexpression = (!_isEmpty);
      } else {
        boolean _isEmpty_1 = IterableExtensions.isEmpty(BundleUtil.getDependencyBundles(it));
        _xifexpression = (!_isEmpty_1);
      }
      return Boolean.valueOf(_xifexpression);
    };
    return (((BundleDescriptor) _domainObjectDescriptor).<Boolean>withDomainObject(_function)).booleanValue();
  }
  
  @Override
  public void perform(final RapidButton button) {
    DomainObjectDescriptor _domainObjectDescriptor = button.getHost().getDomainObjectDescriptor();
    final BundleDescriptor descriptor = ((BundleDescriptor) _domainObjectDescriptor);
    final Function1<BundleDescription, Object> _function = (BundleDescription it) -> {
      return this.doPerform(button, it);
    };
    descriptor.<Object>withDomainObject(_function);
  }
  
  public Object doPerform(final RapidButton button, final BundleDescription hostBundle) {
    Object _xblockexpression = null;
    {
      final XRoot root = CoreExtensions.getRoot(button.getHost());
      XDiagramConfig _configByID = XDiagramConfig.Registry.getInstance().getConfigByID("de.fxdiagram.pde.BundleDiagramConfig");
      final BundleDiagramConfig config = ((BundleDiagramConfig) _configByID);
      final XNode host = button.getHost();
      AbstractChoiceGraphics _xifexpression = null;
      boolean _isVertical = button.getPosition().isVertical();
      if (_isVertical) {
        _xifexpression = new CarusselChoice();
      } else {
        _xifexpression = new CoverFlowChoice();
      }
      final AbstractChoiceGraphics choiceGraphics = _xifexpression;
      final XDiagramConfigInterpreter interpreter = new XDiagramConfigInterpreter();
      Side _position = button.getPosition();
      final ConnectedNodeChooser chooser = new ConnectedNodeChooser(host, _position, choiceGraphics) {
        @Override
        public Iterable<? extends XShape> getAdditionalShapesToAdd(final XNode choice, final DomainObjectDescriptor choiceInfo) {
          try {
            Iterable<XConnection> _filter = Iterables.<XConnection>filter(super.getAdditionalShapesToAdd(choice, choiceInfo), XConnection.class);
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
            final Consumer<BundleDependency> _function_1 = (BundleDependency bundleDependency) -> {
              context.setIsCreateConnections(false);
              final XNode source = interpreter.<BundleDescription>createNode(bundleDependency.getOwner(), config.getPluginNode(), context);
              final XNode target = interpreter.<BundleDescription>createNode(bundleDependency.getDependency(), config.getPluginNode(), context);
              context.setIsCreateConnections(true);
              final Procedure1<XConnection> _function_2 = (XConnection it_1) -> {
                it_1.setSource(source);
                it_1.setTarget(target);
              };
              interpreter.<BundleDependency>createConnection(bundleDependency, config.getDependencyConnection(), _function_2, context);
            };
            ((BundleDescriptor) _domainObjectDescriptor).<ArrayList<BundleDependency>>withDomainObject(_function).forEach(_function_1);
            return context.getAddedShapes();
          } catch (final Throwable _t) {
            if (_t instanceof NoSuchElementException) {
              final NoSuchElementException exc = (NoSuchElementException)_t;
              return Collections.<XShape>unmodifiableList(CollectionLiterals.<XShape>newArrayList());
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
        }
        
        @Override
        protected void nodeChosen(final XNode choice) {
          super.nodeChosen(choice);
          if ((choice != null)) {
            this.getRoot().getCommandStack().execute(
              new Layouter().createLayoutCommand(CoreExtensions.getRoot(host).getDiagram(), DurationExtensions.millis(500)));
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
        final XNode candidateNode = interpreter.<BundleDescription>createNode(it, config.getPluginNode(), context);
        chooser.addChoice(candidateNode);
      };
      candidates.forEach(_function_2);
      root.setCurrentTool(chooser);
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
}
