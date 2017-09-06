package de.fxdiagram.core.anchors;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.command.EmptyTransition;
import de.fxdiagram.core.extensions.TransitionExtensions;
import java.util.List;
import java.util.function.Consumer;
import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@FinalFieldsConstructor
@SuppressWarnings("all")
public class ConnectionMemento {
  public static class MorphCommand extends AbstractAnimationCommand {
    private XConnection connection;
    
    private ConnectionMemento from;
    
    private ConnectionMemento to;
    
    public MorphCommand(final XConnection connection, final ConnectionMemento from) {
      this.connection = connection;
      this.from = from;
      ConnectionMemento _connectionMemento = new ConnectionMemento(connection);
      this.to = _connectionMemento;
    }
    
    @Override
    public Animation createExecuteAnimation(final CommandContext context) {
      EmptyTransition _emptyTransition = new EmptyTransition();
      final Procedure1<EmptyTransition> _function = (EmptyTransition it) -> {
        final EventHandler<ActionEvent> _function_1 = (ActionEvent it_1) -> {
          final Consumer<XControlPoint> _function_2 = (XControlPoint it_2) -> {
            MoveBehavior _behavior = it_2.<MoveBehavior>getBehavior(MoveBehavior.class);
            if (_behavior!=null) {
              _behavior.reset();
            }
          };
          this.connection.getControlPoints().forEach(_function_2);
        };
        it.setOnFinished(_function_1);
      };
      return ObjectExtensions.<EmptyTransition>operator_doubleArrow(_emptyTransition, _function);
    }
    
    @Override
    public Animation createUndoAnimation(final CommandContext context) {
      return TransitionExtensions.createMorphTransition(this.connection, this.to, this.from, context.getDefaultUndoDuration());
    }
    
    @Override
    public Animation createRedoAnimation(final CommandContext context) {
      return TransitionExtensions.createMorphTransition(this.connection, this.from, this.to, context.getDefaultUndoDuration());
    }
  }
  
  private final XConnection connection;
  
  private final XConnection.Kind kind;
  
  private final List<XControlPoint> controlPoints;
  
  public ConnectionMemento(final XConnection connection) {
    this.connection = connection;
    this.kind = connection.getKind();
    this.controlPoints = CollectionLiterals.<XControlPoint>newArrayList();
    final Function1<XControlPoint, XControlPoint> _function = (XControlPoint orig) -> {
      XControlPoint _xControlPoint = new XControlPoint();
      final Procedure1<XControlPoint> _function_1 = (XControlPoint it) -> {
        it.setType(orig.getType());
        it.setLayoutX(orig.getLayoutX());
        it.setLayoutY(orig.getLayoutY());
        it.setSide(orig.getSide());
        it.setManuallyPlaced(orig.getManuallyPlaced());
      };
      return ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint, _function_1);
    };
    List<XControlPoint> _map = ListExtensions.<XControlPoint, XControlPoint>map(connection.getControlPoints(), _function);
    Iterables.<XControlPoint>addAll(this.controlPoints, _map);
  }
  
  public boolean hasChanged() {
    XConnection.Kind _kind = this.connection.getKind();
    boolean _notEquals = (!Objects.equal(this.kind, _kind));
    if (_notEquals) {
      return true;
    }
    int _size = this.controlPoints.size();
    int _size_1 = this.connection.getControlPoints().size();
    boolean _notEquals_1 = (_size != _size_1);
    if (_notEquals_1) {
      return true;
    }
    for (int i = 0; (i < this.controlPoints.size()); i++) {
      {
        final XControlPoint thisCP = this.controlPoints.get(i);
        final XControlPoint thatCP = this.connection.getControlPoints().get(i);
        if (((((!Objects.equal(thisCP.getType(), thatCP.getType())) || (thisCP.getLayoutX() != thatCP.getLayoutX())) || (thisCP.getLayoutY() != thatCP.getLayoutY())) || (!Objects.equal(thisCP.getSide(), thatCP.getSide())))) {
          return true;
        }
      }
    }
    return false;
  }
  
  public List<Point2D> getPoints() {
    final Function1<XControlPoint, Point2D> _function = (XControlPoint it) -> {
      double _layoutX = it.getLayoutX();
      double _layoutY = it.getLayoutY();
      return new Point2D(_layoutX, _layoutY);
    };
    return IterableExtensions.<Point2D>toList(ListExtensions.<XControlPoint, Point2D>map(this.controlPoints, _function));
  }
  
  public ConnectionMemento.MorphCommand createChangeCommand() {
    ConnectionMemento.MorphCommand _xifexpression = null;
    boolean _hasChanged = this.hasChanged();
    if (_hasChanged) {
      _xifexpression = new ConnectionMemento.MorphCommand(this.connection, this);
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  public ConnectionMemento(final XConnection connection, final XConnection.Kind kind, final List<XControlPoint> controlPoints) {
    super();
    this.connection = connection;
    this.kind = kind;
    this.controlPoints = controlPoints;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.connection== null) ? 0 : this.connection.hashCode());
    result = prime * result + ((this.kind== null) ? 0 : this.kind.hashCode());
    result = prime * result + ((this.controlPoints== null) ? 0 : this.controlPoints.hashCode());
    return result;
  }
  
  @Override
  @Pure
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ConnectionMemento other = (ConnectionMemento) obj;
    if (this.connection == null) {
      if (other.connection != null)
        return false;
    } else if (!this.connection.equals(other.connection))
      return false;
    if (this.kind == null) {
      if (other.kind != null)
        return false;
    } else if (!this.kind.equals(other.kind))
      return false;
    if (this.controlPoints == null) {
      if (other.controlPoints != null)
        return false;
    } else if (!this.controlPoints.equals(other.controlPoints))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("connection", this.connection);
    b.add("kind", this.kind);
    b.add("controlPoints", this.controlPoints);
    return b.toString();
  }
  
  @Pure
  public XConnection getConnection() {
    return this.connection;
  }
  
  @Pure
  public XConnection.Kind getKind() {
    return this.kind;
  }
  
  @Pure
  public List<XControlPoint> getControlPoints() {
    return this.controlPoints;
  }
}
