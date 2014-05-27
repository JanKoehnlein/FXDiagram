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
import org.eclipse.xtext.xbase.lib.IterableExtensions;
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
  
  public Animation createExecuteAnimation(final CommandContext context) {
    final Procedure1<XShape> _function = new Procedure1<XShape>() {
      public void apply(final XShape it) {
        boolean _matched = false;
        if (!_matched) {
          if (it instanceof XNode) {
            _matched=true;
            if (AddRemoveCommand.this.isAdd) {
              ObservableList<XNode> _nodes = AddRemoveCommand.this.diagram.getNodes();
              boolean _contains = _nodes.contains(it);
              boolean _not = (!_contains);
              if (_not) {
                ObservableList<XNode> _nodes_1 = AddRemoveCommand.this.diagram.getNodes();
                _nodes_1.add(((XNode)it));
              }
            } else {
              ObservableList<XNode> _nodes_2 = AddRemoveCommand.this.diagram.getNodes();
              _nodes_2.remove(((XNode)it));
            }
          }
        }
        if (!_matched) {
          if (it instanceof XConnection) {
            _matched=true;
            XNode _source = ((XConnection)it).getSource();
            XNode _target = ((XConnection)it).getTarget();
            Pair<XNode, XNode> _mappedTo = Pair.<XNode, XNode>of(_source, _target);
            AddRemoveCommand.this.connectedNodesMap.put(((XConnection)it), _mappedTo);
            if (AddRemoveCommand.this.isAdd) {
              ObservableList<XConnection> _connections = AddRemoveCommand.this.diagram.getConnections();
              boolean _contains = _connections.contains(it);
              boolean _not = (!_contains);
              if (_not) {
                ObservableList<XConnection> _connections_1 = AddRemoveCommand.this.diagram.getConnections();
                _connections_1.add(((XConnection)it));
              }
            } else {
              ObservableList<XConnection> _connections_2 = AddRemoveCommand.this.diagram.getConnections();
              _connections_2.remove(((XConnection)it));
            }
          }
        }
      }
    };
    IterableExtensions.forEach(this.shapes, _function);
    return null;
  }
  
  public Animation createUndoAnimation(final CommandContext context) {
    ParallelTransition _xifexpression = null;
    if (this.isAdd) {
      _xifexpression = this.add(context);
    } else {
      _xifexpression = this.remove(context);
    }
    return _xifexpression;
  }
  
  public Animation createRedoAnimation(final CommandContext context) {
    ParallelTransition _xifexpression = null;
    if (this.isAdd) {
      _xifexpression = this.remove(context);
    } else {
      _xifexpression = this.add(context);
    }
    return _xifexpression;
  }
  
  protected ParallelTransition add(@Extension final CommandContext context) {
    ParallelTransition _parallelTransition = new ParallelTransition();
    final Procedure1<ParallelTransition> _function = new Procedure1<ParallelTransition>() {
      public void apply(final ParallelTransition it) {
        ObservableList<Animation> _children = it.getChildren();
        final Function1<XShape, Animation> _function = new Function1<XShape, Animation>() {
          public Animation apply(final XShape it) {
            Duration _defaultUndoDuration = context.getDefaultUndoDuration();
            return AddRemoveCommand.this.disappear(it, _defaultUndoDuration);
          }
        };
        List<Animation> _map = ListExtensions.map(AddRemoveCommand.this.shapes, _function);
        Iterables.<Animation>addAll(_children, _map);
        final EventHandler<ActionEvent> _function_1 = new EventHandler<ActionEvent>() {
          public void handle(final ActionEvent it) {
            final Procedure1<XShape> _function = new Procedure1<XShape>() {
              public void apply(final XShape it) {
                boolean _matched = false;
                if (!_matched) {
                  if (it instanceof XNode) {
                    _matched=true;
                    ObservableList<XNode> _nodes = AddRemoveCommand.this.diagram.getNodes();
                    _nodes.remove(((XNode)it));
                  }
                }
                if (!_matched) {
                  if (it instanceof XConnection) {
                    _matched=true;
                    ObservableList<XConnection> _connections = AddRemoveCommand.this.diagram.getConnections();
                    _connections.remove(((XConnection)it));
                  }
                }
              }
            };
            IterableExtensions.forEach(AddRemoveCommand.this.shapes, _function);
          }
        };
        it.setOnFinished(_function_1);
      }
    };
    return ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
  }
  
  protected ParallelTransition remove(@Extension final CommandContext context) {
    ParallelTransition _xblockexpression = null;
    {
      final Procedure1<XShape> _function = new Procedure1<XShape>() {
        public void apply(final XShape it) {
          boolean _matched = false;
          if (!_matched) {
            if (it instanceof XNode) {
              _matched=true;
              ObservableList<XNode> _nodes = AddRemoveCommand.this.diagram.getNodes();
              _nodes.add(((XNode)it));
            }
          }
          if (!_matched) {
            if (it instanceof XConnection) {
              _matched=true;
              final Pair<XNode, XNode> nodes = AddRemoveCommand.this.connectedNodesMap.get(it);
              XNode _key = nodes.getKey();
              ((XConnection)it).setSource(_key);
              XNode _value = nodes.getValue();
              ((XConnection)it).setTarget(_value);
              ObservableList<XConnection> _connections = AddRemoveCommand.this.diagram.getConnections();
              _connections.add(((XConnection)it));
            }
          }
        }
      };
      IterableExtensions.forEach(this.shapes, _function);
      ParallelTransition _parallelTransition = new ParallelTransition();
      final Procedure1<ParallelTransition> _function_1 = new Procedure1<ParallelTransition>() {
        public void apply(final ParallelTransition it) {
          ObservableList<Animation> _children = it.getChildren();
          final Function1<XShape, Animation> _function = new Function1<XShape, Animation>() {
            public Animation apply(final XShape it) {
              Duration _defaultUndoDuration = context.getDefaultUndoDuration();
              return AddRemoveCommand.this.appear(it, _defaultUndoDuration);
            }
          };
          List<Animation> _map = ListExtensions.map(AddRemoveCommand.this.shapes, _function);
          Iterables.<Animation>addAll(_children, _map);
        }
      };
      _xblockexpression = ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function_1);
    }
    return _xblockexpression;
  }
  
  protected Animation appear(final XShape node, final Duration duration) {
    FadeTransition _fadeTransition = new FadeTransition();
    final Procedure1<FadeTransition> _function = new Procedure1<FadeTransition>() {
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
