package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDiagramContainer;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.DirtyState;
import de.fxdiagram.core.behavior.ReconcileBehavior;
import de.fxdiagram.core.command.AbstractCommand;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.LazyCommand;
import de.fxdiagram.core.command.ParallelAnimationCommand;
import de.fxdiagram.core.command.SequentialAnimationCommand;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ReconcileAction implements DiagramAction {
  @FinalFieldsConstructor
  public static class UpdateDirtyStateCommand extends AbstractCommand {
    private final XDiagram diagram;
    
    private final boolean isShow;
    
    @Override
    public void execute(final CommandContext context) {
      final ReconcileBehavior diagramReconcileBehavior = this.diagram.<ReconcileBehavior>getBehavior(ReconcileBehavior.class);
      if (diagramReconcileBehavior!=null) {
        DirtyState _dirtyState = diagramReconcileBehavior.getDirtyState();
        diagramReconcileBehavior.showDirtyState(_dirtyState);
      }
      final ArrayList<XDomainObjectShape> allShapes = CollectionLiterals.<XDomainObjectShape>newArrayList();
      ObservableList<XConnection> _connections = this.diagram.getConnections();
      Iterables.<XDomainObjectShape>addAll(allShapes, _connections);
      ObservableList<XNode> _nodes = this.diagram.getNodes();
      Iterables.<XDomainObjectShape>addAll(allShapes, _nodes);
      final Consumer<XDomainObjectShape> _function = (XDomainObjectShape it) -> {
        final ReconcileBehavior behavior = it.<ReconcileBehavior>getBehavior(ReconcileBehavior.class);
        boolean _notEquals = (!Objects.equal(behavior, null));
        if (_notEquals) {
          if (this.isShow) {
            DirtyState _dirtyState_1 = behavior.getDirtyState();
            behavior.showDirtyState(_dirtyState_1);
          } else {
            behavior.hideDirtyState();
          }
        }
      };
      allShapes.forEach(_function);
    }
    
    @Override
    public void undo(final CommandContext context) {
      this.execute(context);
    }
    
    @Override
    public void redo(final CommandContext context) {
      this.execute(context);
    }
    
    public UpdateDirtyStateCommand(final XDiagram diagram, final boolean isShow) {
      super();
      this.diagram = diagram;
      this.isShow = isShow;
    }
  }
  
  @Override
  public boolean matches(final KeyEvent event) {
    KeyCode _code = event.getCode();
    return Objects.equal(_code, KeyCode.F5);
  }
  
  @Override
  public SymbolType getSymbol() {
    return SymbolType.TOOL;
  }
  
  @Override
  public String getTooltip() {
    return "Reconcile with model";
  }
  
  @Override
  public void perform(final XRoot root) {
    final XDiagram diagram = root.getDiagram();
    SequentialAnimationCommand _sequentialAnimationCommand = new SequentialAnimationCommand();
    final Procedure1<SequentialAnimationCommand> _function = (SequentialAnimationCommand it) -> {
      ReconcileAction.UpdateDirtyStateCommand _updateDirtyStateCommand = new ReconcileAction.UpdateDirtyStateCommand(diagram, false);
      it.operator_add(_updateDirtyStateCommand);
      LazyCommand _reconcileCommand = this.getReconcileCommand(root);
      it.operator_add(_reconcileCommand);
      LazyCommand _reconcileCommand_1 = this.getReconcileCommand(root);
      it.operator_add(_reconcileCommand_1);
      ReconcileAction.UpdateDirtyStateCommand _updateDirtyStateCommand_1 = new ReconcileAction.UpdateDirtyStateCommand(diagram, true);
      it.operator_add(_updateDirtyStateCommand_1);
    };
    final SequentialAnimationCommand command = ObjectExtensions.<SequentialAnimationCommand>operator_doubleArrow(_sequentialAnimationCommand, _function);
    CommandStack _commandStack = root.getCommandStack();
    _commandStack.execute(command);
  }
  
  protected LazyCommand getReconcileCommand(final XRoot root) {
    final LazyCommand _function = new LazyCommand() {
      @Override
      protected AnimationCommand createDelegate() {
        final XDiagram diagram = root.getDiagram();
        final HashMultimap<XDiagram, XShape> deleteShapes = HashMultimap.<XDiagram, XShape>create();
        final HashMultimap<XDiagram, XShape> addShapes = HashMultimap.<XDiagram, XShape>create();
        final ArrayList<AnimationCommand> commands = CollectionLiterals.<AnimationCommand>newArrayList();
        final ReconcileBehavior.UpdateAcceptor acceptor = new ReconcileBehavior.UpdateAcceptor() {
          final ReconcileBehavior.UpdateAcceptor _this = this;
          @Override
          public void add(final XShape shape, final XDiagram diagram) {
            boolean _notEquals = (!Objects.equal(diagram, null));
            if (_notEquals) {
              addShapes.put(diagram, shape);
            }
          }
          
          @Override
          public void delete(final XShape shape, final XDiagram diagram) {
            boolean _notEquals = (!Objects.equal(diagram, null));
            if (_notEquals) {
              deleteShapes.put(diagram, shape);
            }
            if ((shape instanceof XNode)) {
              ObservableList<XConnection> _outgoingConnections = ((XNode)shape).getOutgoingConnections();
              ObservableList<XConnection> _incomingConnections = ((XNode)shape).getIncomingConnections();
              Iterable<XConnection> _plus = Iterables.<XConnection>concat(_outgoingConnections, _incomingConnections);
              final Consumer<XConnection> _function = (XConnection it) -> {
                XDiagram _diagram = CoreExtensions.getDiagram(it);
                this.delete(it, _diagram);
              };
              _plus.forEach(_function);
            }
            if ((shape instanceof XDiagramContainer)) {
              XDiagram _innerDiagram = ((XDiagramContainer)shape).getInnerDiagram();
              ObservableList<XNode> _nodes = _innerDiagram.getNodes();
              XDiagram _innerDiagram_1 = ((XDiagramContainer)shape).getInnerDiagram();
              ObservableList<XConnection> _connections = _innerDiagram_1.getConnections();
              Iterable<XDomainObjectShape> _plus_1 = Iterables.<XDomainObjectShape>concat(_nodes, _connections);
              final Consumer<XDomainObjectShape> _function_1 = (XDomainObjectShape it) -> {
                XDiagram _diagram = CoreExtensions.getDiagram(it);
                this.delete(it, _diagram);
              };
              _plus_1.forEach(_function_1);
            }
          }
          
          @Override
          public void morph(final AnimationCommand command) {
            commands.add(command);
          }
        };
        final ReconcileBehavior diagramReconcileBehavior = diagram.<ReconcileBehavior>getBehavior(ReconcileBehavior.class);
        boolean _notEquals = (!Objects.equal(diagramReconcileBehavior, null));
        if (_notEquals) {
          diagramReconcileBehavior.reconcile(acceptor);
        }
        final ArrayList<XShape> allShapes = CollectionLiterals.<XShape>newArrayList();
        ObservableList<XConnection> _connections = diagram.getConnections();
        Iterables.<XShape>addAll(allShapes, _connections);
        ObservableList<XNode> _nodes = diagram.getNodes();
        Iterables.<XShape>addAll(allShapes, _nodes);
        Collection<XShape> _values = deleteShapes.values();
        Iterables.removeAll(allShapes, _values);
        final Consumer<XShape> _function = (XShape it) -> {
          ReconcileBehavior _behavior = it.<ReconcileBehavior>getBehavior(ReconcileBehavior.class);
          if (_behavior!=null) {
            _behavior.reconcile(acceptor);
          }
        };
        allShapes.forEach(_function);
        Set<XDiagram> _keySet = deleteShapes.keySet();
        final Consumer<XDiagram> _function_1 = (XDiagram it) -> {
          Set<XShape> _get = deleteShapes.get(it);
          AddRemoveCommand _newRemoveCommand = AddRemoveCommand.newRemoveCommand(it, ((XShape[])Conversions.unwrapArray(_get, XShape.class)));
          commands.add(_newRemoveCommand);
        };
        _keySet.forEach(_function_1);
        Set<XDiagram> _keySet_1 = addShapes.keySet();
        final Consumer<XDiagram> _function_2 = (XDiagram it) -> {
          Set<XShape> _get = addShapes.get(it);
          AddRemoveCommand _newAddCommand = AddRemoveCommand.newAddCommand(it, ((XShape[])Conversions.unwrapArray(_get, XShape.class)));
          commands.add(_newAddCommand);
        };
        _keySet_1.forEach(_function_2);
        ParallelAnimationCommand _parallelAnimationCommand = new ParallelAnimationCommand();
        final Procedure1<ParallelAnimationCommand> _function_3 = (ParallelAnimationCommand it) -> {
          it.operator_add(commands);
        };
        return ObjectExtensions.<ParallelAnimationCommand>operator_doubleArrow(_parallelAnimationCommand, _function_3);
      }
    };
    return _function;
  }
}
