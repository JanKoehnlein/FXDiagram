package de.fxdiagram.eclipse.commands;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.DirtyStateBehavior;
import de.fxdiagram.core.behavior.UpdateAcceptor;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.LazyCommand;
import de.fxdiagram.core.command.ParallelAnimationCommand;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.eclipse.FXDiagramView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class UpdateDiagramHandler extends AbstractHandler {
  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    Object _xblockexpression = null;
    {
      final IWorkbenchPart view = HandlerUtil.getActivePart(event);
      if ((view instanceof FXDiagramView)) {
        XRoot _currentRoot = ((FXDiagramView)view).getCurrentRoot();
        final XDiagram diagram = _currentRoot.getDiagram();
        final ArrayList<XShape> allShapes = CollectionLiterals.<XShape>newArrayList();
        ObservableList<XConnection> _connections = diagram.getConnections();
        Iterables.<XShape>addAll(allShapes, _connections);
        ObservableList<XNode> _nodes = diagram.getNodes();
        Iterables.<XShape>addAll(allShapes, _nodes);
        final LazyCommand _function = new LazyCommand() {
          @Override
          protected AnimationCommand createDelegate() {
            ParallelAnimationCommand _xblockexpression = null;
            {
              final HashSet<XShape> deleteShapes = CollectionLiterals.<XShape>newHashSet();
              final HashSet<XShape> addShapes = CollectionLiterals.<XShape>newHashSet();
              final ArrayList<AnimationCommand> commands = CollectionLiterals.<AnimationCommand>newArrayList();
              final UpdateAcceptor acceptor = new UpdateAcceptor() {
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
              final Consumer<XShape> _function = (XShape it) -> {
                DirtyStateBehavior _behavior = it.<DirtyStateBehavior>getBehavior(DirtyStateBehavior.class);
                if (_behavior!=null) {
                  _behavior.update(acceptor);
                }
              };
              allShapes.forEach(_function);
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
              ParallelAnimationCommand _parallelAnimationCommand = new ParallelAnimationCommand();
              final Procedure1<ParallelAnimationCommand> _function_1 = (ParallelAnimationCommand it) -> {
                it.operator_add(commands);
              };
              _xblockexpression = ObjectExtensions.<ParallelAnimationCommand>operator_doubleArrow(_parallelAnimationCommand, _function_1);
            }
            return _xblockexpression;
          }
        };
        final LazyCommand lazyCommand = _function;
        XRoot _root = CoreExtensions.getRoot(diagram);
        CommandStack _commandStack = _root.getCommandStack();
        _commandStack.execute(lazyCommand);
      }
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
}
