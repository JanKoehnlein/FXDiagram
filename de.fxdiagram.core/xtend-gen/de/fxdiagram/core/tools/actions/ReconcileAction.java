package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
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
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import java.util.ArrayList;
import java.util.HashSet;
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
    
    @Override
    public void execute(final CommandContext context) {
      final ArrayList<XDomainObjectShape> allShapes = CollectionLiterals.<XDomainObjectShape>newArrayList();
      ObservableList<XConnection> _connections = this.diagram.getConnections();
      Iterables.<XDomainObjectShape>addAll(allShapes, _connections);
      ObservableList<XNode> _nodes = this.diagram.getNodes();
      Iterables.<XDomainObjectShape>addAll(allShapes, _nodes);
      final Consumer<XDomainObjectShape> _function = (XDomainObjectShape it) -> {
        final ReconcileBehavior behavior = it.<ReconcileBehavior>getBehavior(ReconcileBehavior.class);
        boolean _notEquals = (!Objects.equal(behavior, null));
        if (_notEquals) {
          DirtyState _dirtyState = behavior.getDirtyState();
          behavior.showDirtyState(_dirtyState);
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
    
    public UpdateDirtyStateCommand(final XDiagram diagram) {
      super();
      this.diagram = diagram;
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
    final LazyCommand _function = new LazyCommand() {
      @Override
      protected AnimationCommand createDelegate() {
        SequentialAnimationCommand _xblockexpression = null;
        {
          final HashSet<XShape> deleteShapes = CollectionLiterals.<XShape>newHashSet();
          final HashSet<XShape> addShapes = CollectionLiterals.<XShape>newHashSet();
          final ArrayList<AnimationCommand> commands = CollectionLiterals.<AnimationCommand>newArrayList();
          final ReconcileBehavior.UpdateAcceptor acceptor = new ReconcileBehavior.UpdateAcceptor() {
            @Override
            public void add(final XShape shape) {
              addShapes.add(shape);
            }
            
            @Override
            public void delete(final XShape shape) {
              deleteShapes.add(shape);
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
          } else {
            final ArrayList<XDomainObjectShape> allShapes = CollectionLiterals.<XDomainObjectShape>newArrayList();
            ObservableList<XConnection> _connections = diagram.getConnections();
            Iterables.<XDomainObjectShape>addAll(allShapes, _connections);
            ObservableList<XNode> _nodes = diagram.getNodes();
            Iterables.<XDomainObjectShape>addAll(allShapes, _nodes);
            final Consumer<XDomainObjectShape> _function = (XDomainObjectShape it) -> {
              ReconcileBehavior _behavior = it.<ReconcileBehavior>getBehavior(ReconcileBehavior.class);
              if (_behavior!=null) {
                _behavior.reconcile(acceptor);
              }
            };
            allShapes.forEach(_function);
          }
          boolean _isEmpty = deleteShapes.isEmpty();
          boolean _not = (!_isEmpty);
          if (_not) {
            AddRemoveCommand _newRemoveCommand = AddRemoveCommand.newRemoveCommand(diagram, ((XShape[])Conversions.unwrapArray(deleteShapes, XShape.class)));
            commands.add(_newRemoveCommand);
          }
          boolean _isEmpty_1 = addShapes.isEmpty();
          boolean _not_1 = (!_isEmpty_1);
          if (_not_1) {
            AddRemoveCommand _newAddCommand = AddRemoveCommand.newAddCommand(diagram, ((XShape[])Conversions.unwrapArray(addShapes, XShape.class)));
            commands.add(_newAddCommand);
          }
          SequentialAnimationCommand _sequentialAnimationCommand = new SequentialAnimationCommand();
          final Procedure1<SequentialAnimationCommand> _function_1 = (SequentialAnimationCommand it) -> {
            ParallelAnimationCommand _parallelAnimationCommand = new ParallelAnimationCommand();
            final Procedure1<ParallelAnimationCommand> _function_2 = (ParallelAnimationCommand it_1) -> {
              it_1.operator_add(commands);
            };
            ParallelAnimationCommand _doubleArrow = ObjectExtensions.<ParallelAnimationCommand>operator_doubleArrow(_parallelAnimationCommand, _function_2);
            it.operator_add(_doubleArrow);
            ReconcileAction.UpdateDirtyStateCommand _updateDirtyStateCommand = new ReconcileAction.UpdateDirtyStateCommand(diagram);
            it.operator_add(_updateDirtyStateCommand);
          };
          _xblockexpression = ObjectExtensions.<SequentialAnimationCommand>operator_doubleArrow(_sequentialAnimationCommand, _function_1);
        }
        return _xblockexpression;
      }
    };
    final LazyCommand lazyCommand = _function;
    CommandStack _commandStack = root.getCommandStack();
    _commandStack.execute(lazyCommand);
  }
}
