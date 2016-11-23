package de.fxdiagram.core.behavior;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.ConnectionMemento;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.ParallelAnimationCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class NodeMoveBehavior extends MoveBehavior<XNode> {
  private List<ConnectionMemento> connectionMementi;
  
  public NodeMoveBehavior(final XNode host) {
    super(host);
  }
  
  @Override
  public void startDrag(final double screenX, final double screenY) {
    ArrayList<ConnectionMemento> _newArrayList = CollectionLiterals.<ConnectionMemento>newArrayList();
    this.connectionMementi = _newArrayList;
    XNode _host = this.getHost();
    ObservableList<XConnection> _outgoingConnections = _host.getOutgoingConnections();
    XNode _host_1 = this.getHost();
    ObservableList<XConnection> _incomingConnections = _host_1.getIncomingConnections();
    Iterable<XConnection> _plus = Iterables.<XConnection>concat(_outgoingConnections, _incomingConnections);
    final Function1<XConnection, ConnectionMemento> _function = (XConnection it) -> {
      return new ConnectionMemento(it);
    };
    Iterable<ConnectionMemento> _map = IterableExtensions.<XConnection, ConnectionMemento>map(_plus, _function);
    Iterables.<ConnectionMemento>addAll(this.connectionMementi, _map);
    super.startDrag(screenX, screenY);
  }
  
  @Override
  protected AnimationCommand createMoveCommand() {
    final Function1<ConnectionMemento, ConnectionMemento.MorphCommand> _function = (ConnectionMemento it) -> {
      return it.createChangeCommand();
    };
    List<ConnectionMemento.MorphCommand> _map = ListExtensions.<ConnectionMemento, ConnectionMemento.MorphCommand>map(this.connectionMementi, _function);
    Iterable<ConnectionMemento.MorphCommand> _filterNull = IterableExtensions.<ConnectionMemento.MorphCommand>filterNull(_map);
    final List<ConnectionMemento.MorphCommand> connectionCommands = IterableExtensions.<ConnectionMemento.MorphCommand>toList(_filterNull);
    final AnimationCommand moveCommand = super.createMoveCommand();
    boolean _isEmpty = connectionCommands.isEmpty();
    if (_isEmpty) {
      return moveCommand;
    } else {
      ParallelAnimationCommand _parallelAnimationCommand = new ParallelAnimationCommand();
      final Procedure1<ParallelAnimationCommand> _function_1 = (ParallelAnimationCommand sac) -> {
        sac.operator_add(moveCommand);
        final Consumer<ConnectionMemento.MorphCommand> _function_2 = (ConnectionMemento.MorphCommand it) -> {
          sac.operator_add(it);
        };
        connectionCommands.forEach(_function_2);
      };
      return ObjectExtensions.<ParallelAnimationCommand>operator_doubleArrow(_parallelAnimationCommand, _function_1);
    }
  }
}
