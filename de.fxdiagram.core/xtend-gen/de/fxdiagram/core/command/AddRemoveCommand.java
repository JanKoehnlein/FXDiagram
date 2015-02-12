package de.fxdiagram.core.command;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddRemoveCommand extends AbstractAnimationCommand {
  private boolean isAdd;
  
  private XDiagram diagram;
  
  private List<? extends XShape> shapes;
  
  private Map<XConnection, Pair<XNode, XNode>> connectedNodesMap = CollectionLiterals.<XConnection, Pair<XNode, XNode>>newHashMap();
  
  public static AddRemoveCommand newAddCommand(final XDiagram diagram, final XShape... shapes) {
    return new AddRemoveCommand(true, diagram, shapes);
  }
  
  public static AddRemoveCommand newRemoveCommand(final XDiagram diagram, final XShape... shapes) {
    return new AddRemoveCommand(false, diagram, shapes);
  }
  
  protected AddRemoveCommand(final boolean isAdd, final XDiagram diagram, final XShape... shapes) {
    this.isAdd = isAdd;
    this.diagram = diagram;
    this.shapes = ((List<? extends XShape>)Conversions.doWrapArray(shapes));
  }
  
  @Override
  public Animation createExecuteAnimation(final CommandContext context) {
    Iterable<XNode> _filter = Iterables.<XNode>filter(this.shapes, XNode.class);
    final Consumer<XNode> _function = new Consumer<XNode>() {
      @Override
      public void accept(final XNode it) {
        if (AddRemoveCommand.this.isAdd) {
          ObservableList<XNode> _nodes = AddRemoveCommand.this.diagram.getNodes();
          boolean _contains = _nodes.contains(it);
          boolean _not = (!_contains);
          if (_not) {
            ObservableList<XNode> _nodes_1 = AddRemoveCommand.this.diagram.getNodes();
            _nodes_1.add(it);
          }
        } else {
          ObservableList<XNode> _nodes_2 = AddRemoveCommand.this.diagram.getNodes();
          _nodes_2.remove(it);
        }
      }
    };
    _filter.forEach(_function);
    Iterable<XConnection> _filter_1 = Iterables.<XConnection>filter(this.shapes, XConnection.class);
    final Consumer<XConnection> _function_1 = new Consumer<XConnection>() {
      @Override
      public void accept(final XConnection it) {
        XNode _source = it.getSource();
        XNode _target = it.getTarget();
        Pair<XNode, XNode> _mappedTo = Pair.<XNode, XNode>of(_source, _target);
        AddRemoveCommand.this.connectedNodesMap.put(it, _mappedTo);
        if (AddRemoveCommand.this.isAdd) {
          ObservableList<XConnection> _connections = AddRemoveCommand.this.diagram.getConnections();
          boolean _contains = _connections.contains(it);
          boolean _not = (!_contains);
          if (_not) {
            ObservableList<XConnection> _connections_1 = AddRemoveCommand.this.diagram.getConnections();
            _connections_1.add(it);
          }
        } else {
          ObservableList<XConnection> _connections_2 = AddRemoveCommand.this.diagram.getConnections();
          _connections_2.remove(it);
        }
      }
    };
    _filter_1.forEach(_function_1);
    return null;
  }
  
  @Override
  public Animation createUndoAnimation(final CommandContext context) {
    ParallelTransition _xifexpression = null;
    if (this.isAdd) {
      _xifexpression = this.remove(context);
    } else {
      _xifexpression = this.add(context);
    }
    return _xifexpression;
  }
  
  @Override
  public Animation createRedoAnimation(final CommandContext context) {
    ParallelTransition _xifexpression = null;
    if (this.isAdd) {
      _xifexpression = this.add(context);
    } else {
      _xifexpression = this.remove(context);
    }
    return _xifexpression;
  }
  
  protected ParallelTransition remove(@Extension final CommandContext context) {
    ParallelTransition _parallelTransition = new ParallelTransition();
    final Procedure1<ParallelTransition> _function = new Procedure1<ParallelTransition>() {
      @Override
      public void apply(final ParallelTransition it) {
        ObservableList<Animation> _children = it.getChildren();
        final Function1<XShape, Animation> _function = new Function1<XShape, Animation>() {
          @Override
          public Animation apply(final XShape it) {
            Duration _defaultUndoDuration = context.getDefaultUndoDuration();
            return AddRemoveCommand.this.disappear(it, _defaultUndoDuration);
          }
        };
        List<Animation> _map = ListExtensions.map(AddRemoveCommand.this.shapes, _function);
        Iterables.<Animation>addAll(_children, _map);
        final EventHandler<ActionEvent> _function_1 = new EventHandler<ActionEvent>() {
          @Override
          public void handle(final ActionEvent it) {
            Iterable<XConnection> _filter = Iterables.<XConnection>filter(AddRemoveCommand.this.shapes, XConnection.class);
            final Consumer<XConnection> _function = new Consumer<XConnection>() {
              @Override
              public void accept(final XConnection it) {
                ObservableList<XConnection> _connections = AddRemoveCommand.this.diagram.getConnections();
                _connections.remove(it);
              }
            };
            _filter.forEach(_function);
            Iterable<XNode> _filter_1 = Iterables.<XNode>filter(AddRemoveCommand.this.shapes, XNode.class);
            final Consumer<XNode> _function_1 = new Consumer<XNode>() {
              @Override
              public void accept(final XNode it) {
                ObservableList<XNode> _nodes = AddRemoveCommand.this.diagram.getNodes();
                _nodes.remove(it);
              }
            };
            _filter_1.forEach(_function_1);
          }
        };
        it.setOnFinished(_function_1);
      }
    };
    return ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
  }
  
  protected ParallelTransition add(@Extension final CommandContext context) {
    ParallelTransition _xblockexpression = null;
    {
      Iterable<XNode> _filter = Iterables.<XNode>filter(this.shapes, XNode.class);
      final Consumer<XNode> _function = new Consumer<XNode>() {
        @Override
        public void accept(final XNode it) {
          ObservableList<XNode> _nodes = AddRemoveCommand.this.diagram.getNodes();
          _nodes.add(it);
        }
      };
      _filter.forEach(_function);
      Iterable<XConnection> _filter_1 = Iterables.<XConnection>filter(this.shapes, XConnection.class);
      final Consumer<XConnection> _function_1 = new Consumer<XConnection>() {
        @Override
        public void accept(final XConnection it) {
          final Pair<XNode, XNode> nodes = AddRemoveCommand.this.connectedNodesMap.get(it);
          XNode _key = nodes.getKey();
          it.setSource(_key);
          XNode _value = nodes.getValue();
          it.setTarget(_value);
          ObservableList<XConnection> _connections = AddRemoveCommand.this.diagram.getConnections();
          _connections.add(it);
        }
      };
      _filter_1.forEach(_function_1);
      ParallelTransition _parallelTransition = new ParallelTransition();
      final Procedure1<ParallelTransition> _function_2 = new Procedure1<ParallelTransition>() {
        @Override
        public void apply(final ParallelTransition it) {
          ObservableList<Animation> _children = it.getChildren();
          final Function1<XShape, Animation> _function = new Function1<XShape, Animation>() {
            @Override
            public Animation apply(final XShape it) {
              Duration _defaultUndoDuration = context.getDefaultUndoDuration();
              return AddRemoveCommand.this.appear(it, _defaultUndoDuration);
            }
          };
          List<Animation> _map = ListExtensions.map(AddRemoveCommand.this.shapes, _function);
          Iterables.<Animation>addAll(_children, _map);
        }
      };
      _xblockexpression = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function_2);
    }
    return _xblockexpression;
  }
  
  protected Animation appear(final XShape node, final Duration duration) {
    FadeTransition _fadeTransition = new FadeTransition();
    final Procedure1<FadeTransition> _function = new Procedure1<FadeTransition>() {
      @Override
      public void apply(final FadeTransition it) {
        it.setNode(node);
        it.setFromValue(0);
        it.setToValue(1);
        it.setCycleCount(1);
        it.setDuration(duration);
      }
    };
    return ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function);
  }
  
  protected Animation disappear(final XShape node, final Duration duration) {
    FadeTransition _fadeTransition = new FadeTransition();
    final Procedure1<FadeTransition> _function = new Procedure1<FadeTransition>() {
      @Override
      public void apply(final FadeTransition it) {
        it.setNode(node);
        it.setFromValue(1);
        it.setToValue(0);
        it.setCycleCount(1);
        it.setDuration(duration);
      }
    };
    return ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function);
  }
}
