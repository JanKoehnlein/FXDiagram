package de.fxdiagram.core.command;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.anchors.ConnectionMemento;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.anchors.ManhattanRouter;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.extensions.TransitionExtensions;
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.util.Duration;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

/**
 * Will not remove anchor points.
 */
@FinalFieldsConstructor
@SuppressWarnings("all")
public class ResetConnectionCommand extends AbstractAnimationCommand {
  private final XConnection connection;
  
  private ConnectionMemento from;
  
  private ConnectionMemento to;
  
  protected ConnectionMemento createToMemento() {
    XConnection.Kind _kind = this.connection.getKind();
    if (_kind != null) {
      switch (_kind) {
        case RECTILINEAR:
          final ArrayList<XControlPoint> newPoints = CollectionLiterals.<XControlPoint>newArrayList();
          ConnectionRouter _connectionRouter = this.connection.getConnectionRouter();
          ManhattanRouter _manhattanRouter = _connectionRouter.getManhattanRouter();
          ArrayList<XControlPoint> _defaultPoints = _manhattanRouter.getDefaultPoints();
          Iterables.<XControlPoint>addAll(newPoints, _defaultPoints);
          XConnection.Kind _kind_1 = this.connection.getKind();
          return new ConnectionMemento(this.connection, _kind_1, newPoints);
        default:
          throw new UnsupportedOperationException("Implementation missing");
      }
    } else {
      throw new UnsupportedOperationException("Implementation missing");
    }
  }
  
  @Override
  public Animation createExecuteAnimation(final CommandContext context) {
    Transition _xblockexpression = null;
    {
      ConnectionMemento _connectionMemento = new ConnectionMemento(this.connection);
      this.from = _connectionMemento;
      ConnectionMemento _createToMemento = this.createToMemento();
      this.to = _createToMemento;
      Duration _executeDuration = this.getExecuteDuration(context);
      _xblockexpression = TransitionExtensions.createMorphTransition(this.connection, this.from, this.to, _executeDuration);
    }
    return _xblockexpression;
  }
  
  @Override
  public Animation createUndoAnimation(final CommandContext context) {
    Duration _executeDuration = this.getExecuteDuration(context);
    return TransitionExtensions.createMorphTransition(this.connection, this.to, this.from, _executeDuration);
  }
  
  @Override
  public Animation createRedoAnimation(final CommandContext context) {
    Duration _executeDuration = this.getExecuteDuration(context);
    return TransitionExtensions.createMorphTransition(this.connection, this.from, this.to, _executeDuration);
  }
  
  public ResetConnectionCommand(final XConnection connection) {
    super();
    this.connection = connection;
  }
}
