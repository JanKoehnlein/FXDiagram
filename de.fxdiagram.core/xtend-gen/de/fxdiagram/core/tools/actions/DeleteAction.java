package de.fxdiagram.core.tools.actions;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimaps;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDiagramContainer;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.ParallelAnimationCommand;
import de.fxdiagram.core.command.RemoveControlPointCommand;
import de.fxdiagram.core.command.ResetConnectionCommand;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class DeleteAction implements DiagramAction {
  @Override
  public boolean matches(final KeyEvent event) {
    return (Objects.equal(event.getCode(), KeyCode.DELETE) || Objects.equal(event.getCode(), KeyCode.BACK_SPACE));
  }
  
  @Override
  public SymbolType getSymbol() {
    return SymbolType.TRASH;
  }
  
  @Override
  public String getTooltip() {
    return "Delete selection";
  }
  
  @Override
  public void perform(final XRoot root) {
    final Set<XShape> elements = IterableExtensions.<XShape>toSet(root.getCurrentSelection());
    final Iterable<XNode> nodes = this.getAllContainedNodes(Iterables.<XNode>filter(elements, XNode.class));
    final Function1<XNode, ObservableList<XConnection>> _function = (XNode it) -> {
      return it.getIncomingConnections();
    };
    Iterable<XConnection> _flatten = Iterables.<XConnection>concat(IterableExtensions.<XNode, ObservableList<XConnection>>map(nodes, _function));
    final Function1<XNode, ObservableList<XConnection>> _function_1 = (XNode it) -> {
      return it.getOutgoingConnections();
    };
    Iterable<XConnection> _flatten_1 = Iterables.<XConnection>concat(IterableExtensions.<XNode, ObservableList<XConnection>>map(nodes, _function_1));
    Iterable<XConnection> _plus = Iterables.<XConnection>concat(_flatten, _flatten_1);
    final Function1<XShape, Boolean> _function_2 = (XShape it) -> {
      return Boolean.valueOf(((it instanceof XNode) || (it instanceof XConnection)));
    };
    Iterable<XShape> _filter = IterableExtensions.<XShape>filter(elements, _function_2);
    final Set<XShape> deleteThem = IterableExtensions.<XShape>toSet(Iterables.<XShape>concat(_plus, _filter));
    final Function<XControlPoint, XConnection> _function_3 = (XControlPoint it) -> {
      return this.getConnection(it);
    };
    final ImmutableListMultimap<XConnection, XControlPoint> connection2controlPoints = Multimaps.<XConnection, XControlPoint>index(Iterables.<XControlPoint>filter(elements, XControlPoint.class), _function_3);
    final ArrayList<AbstractAnimationCommand> connectionMorphCommands = CollectionLiterals.<AbstractAnimationCommand>newArrayList();
    final Consumer<XConnection> _function_4 = (XConnection connection) -> {
      boolean _contains = elements.contains(connection);
      boolean _not = (!_contains);
      if (_not) {
        final ImmutableList<XControlPoint> controlPoints = connection2controlPoints.get(connection);
        XConnection.Kind _kind = connection.getKind();
        boolean _equals = Objects.equal(_kind, XConnection.Kind.RECTILINEAR);
        if (_equals) {
          ResetConnectionCommand _resetConnectionCommand = new ResetConnectionCommand(connection);
          connectionMorphCommands.add(_resetConnectionCommand);
        } else {
          RemoveControlPointCommand _removeControlPointCommand = new RemoveControlPointCommand(connection, controlPoints);
          connectionMorphCommands.add(_removeControlPointCommand);
        }
      }
    };
    connection2controlPoints.keySet().forEach(_function_4);
    final Function<XShape, XDiagram> _function_5 = (XShape it) -> {
      return CoreExtensions.getDiagram(it);
    };
    final ImmutableListMultimap<XDiagram, XShape> diagram2shape = Multimaps.<XDiagram, XShape>index(deleteThem, _function_5);
    final Function1<XDiagram, AddRemoveCommand> _function_6 = (XDiagram diagram) -> {
      return AddRemoveCommand.newRemoveCommand(diagram, ((XShape[])Conversions.unwrapArray(diagram2shape.get(diagram), XShape.class)));
    };
    final Iterable<AddRemoveCommand> removeCommands = IterableExtensions.<XDiagram, AddRemoveCommand>map(diagram2shape.keySet(), _function_6);
    CommandStack _commandStack = root.getCommandStack();
    ParallelAnimationCommand _parallelAnimationCommand = new ParallelAnimationCommand();
    final Procedure1<ParallelAnimationCommand> _function_7 = (ParallelAnimationCommand it) -> {
      it.operator_add(removeCommands);
      boolean _isEmpty = connectionMorphCommands.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        it.operator_add(connectionMorphCommands);
      }
    };
    ParallelAnimationCommand _doubleArrow = ObjectExtensions.<ParallelAnimationCommand>operator_doubleArrow(_parallelAnimationCommand, _function_7);
    _commandStack.execute(_doubleArrow);
  }
  
  protected XConnection getConnection(final XControlPoint point) {
    Parent _parent = null;
    if (point!=null) {
      _parent=point.getParent();
    }
    Parent _parent_1 = null;
    if (_parent!=null) {
      _parent_1=_parent.getParent();
    }
    return ((XConnection) _parent_1);
  }
  
  protected Iterable<XNode> getAllContainedNodes(final Iterable<XNode> nodes) {
    final Function1<XDiagramContainer, Iterable<XNode>> _function = (XDiagramContainer it) -> {
      return this.getAllContainedNodes(it.getInnerDiagram().getNodes());
    };
    Iterable<XNode> _flatten = Iterables.<XNode>concat(IterableExtensions.<XDiagramContainer, Iterable<XNode>>map(Iterables.<XDiagramContainer>filter(nodes, XDiagramContainer.class), _function));
    return Iterables.<XNode>concat(nodes, _flatten);
  }
}
