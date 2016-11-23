package de.fxdiagram.core.layout;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.anchors.ConnectionMemento;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.extensions.TransitionExtensions;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.util.Duration;

@SuppressWarnings("all")
public class ConnectionRelayoutCommand extends AbstractAnimationCommand {
  private XConnection connection;
  
  private ConnectionMemento from;
  
  private ConnectionMemento to;
  
  public ConnectionRelayoutCommand(final XConnection connection, final XConnection.Kind toKind, final List<XControlPoint> toPoints) {
    this.connection = connection;
    ConnectionMemento _connectionMemento = new ConnectionMemento(connection, toKind, toPoints);
    this.to = _connectionMemento;
  }
  
  @Override
  public Animation createExecuteAnimation(final CommandContext context) {
    Transition _xblockexpression = null;
    {
      ConnectionMemento _connectionMemento = new ConnectionMemento(this.connection);
      this.from = _connectionMemento;
      Duration _executeDuration = this.getExecuteDuration(context);
      _xblockexpression = TransitionExtensions.createMorphTransition(this.connection, this.from, this.to, _executeDuration);
    }
    return _xblockexpression;
  }
  
  @Override
  public Animation createUndoAnimation(final CommandContext context) {
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return TransitionExtensions.createMorphTransition(this.connection, this.to, this.from, _defaultUndoDuration);
  }
  
  @Override
  public Animation createRedoAnimation(final CommandContext context) {
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return TransitionExtensions.createMorphTransition(this.connection, this.from, this.to, _defaultUndoDuration);
  }
}
