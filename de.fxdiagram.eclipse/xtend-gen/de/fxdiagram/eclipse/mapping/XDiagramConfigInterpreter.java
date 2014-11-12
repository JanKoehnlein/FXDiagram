package de.fxdiagram.eclipse.mapping;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.eclipse.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.eclipse.mapping.AbstractMapping;
import de.fxdiagram.eclipse.mapping.AbstractNodeMappingCall;
import de.fxdiagram.eclipse.mapping.ConnectionMapping;
import de.fxdiagram.eclipse.mapping.ConnectionMappingCall;
import de.fxdiagram.eclipse.mapping.DiagramMapping;
import de.fxdiagram.eclipse.mapping.DiagramMappingCall;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.eclipse.mapping.InterpreterContext;
import de.fxdiagram.eclipse.mapping.MultiConnectionMappingCall;
import de.fxdiagram.eclipse.mapping.MultiNodeMappingCall;
import de.fxdiagram.eclipse.mapping.NodeMapping;
import de.fxdiagram.eclipse.mapping.NodeMappingCall;
import de.fxdiagram.eclipse.mapping.XDiagramConfig;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XDiagramConfigInterpreter {
  public <T extends Object> XDiagram createDiagram(final T diagramObject, final DiagramMapping<T> diagramMapping, final InterpreterContext context) {
    XDiagram _xblockexpression = null;
    {
      boolean _isApplicable = diagramMapping.isApplicable(diagramObject);
      boolean _not = (!_isApplicable);
      if (_not) {
        return null;
      }
      IMappedElementDescriptor<T> _descriptor = this.<T>getDescriptor(diagramObject, diagramMapping);
      final XDiagram diagram = diagramMapping.createDiagram(_descriptor);
      context.diagram = diagram;
      List<AbstractNodeMappingCall<?, T>> _nodes = diagramMapping.getNodes();
      final Consumer<AbstractNodeMappingCall<?, T>> _function = new Consumer<AbstractNodeMappingCall<?, T>>() {
        public void accept(final AbstractNodeMappingCall<?, T> it) {
          XDiagramConfigInterpreter.this.execute(it, diagramObject, context, true);
        }
      };
      _nodes.forEach(_function);
      List<AbstractConnectionMappingCall<?, T>> _connections = diagramMapping.getConnections();
      final Consumer<AbstractConnectionMappingCall<?, T>> _function_1 = new Consumer<AbstractConnectionMappingCall<?, T>>() {
        public void accept(final AbstractConnectionMappingCall<?, T> it) {
          final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
            public void apply(final XConnection it) {
            }
          };
          XDiagramConfigInterpreter.this.execute(it, diagramObject, _function, context, true);
        }
      };
      _connections.forEach(_function_1);
      List<AbstractNodeMappingCall<?, T>> _nodes_1 = diagramMapping.getNodes();
      final Consumer<AbstractNodeMappingCall<?, T>> _function_2 = new Consumer<AbstractNodeMappingCall<?, T>>() {
        public void accept(final AbstractNodeMappingCall<?, T> it) {
          XDiagramConfigInterpreter.this.<T>connectNodesEagerly(it, diagramObject, context);
        }
      };
      _nodes_1.forEach(_function_2);
      _xblockexpression = diagram;
    }
    return _xblockexpression;
  }
  
  protected <T extends Object> void connectNodesEagerly(final AbstractNodeMappingCall<?, T> it, final T diagramObject, final InterpreterContext context) {
    final Iterable<?> nodeObjects = this.select(it, diagramObject);
    NodeMapping<?> _nodeMapping = it.getNodeMapping();
    final NodeMapping<Object> nodeMappingCasted = ((NodeMapping<Object>) _nodeMapping);
    for (final Object nodeObject : nodeObjects) {
      {
        final IMappedElementDescriptor<Object> descriptor = this.<Object>getDescriptor(nodeObject, nodeMappingCasted);
        final XNode node = context.<Object>getNode(descriptor);
        boolean _notEquals = (!Objects.equal(node, null));
        if (_notEquals) {
          List<AbstractConnectionMappingCall<?, Object>> _incoming = nodeMappingCasted.getIncoming();
          final Function1<AbstractConnectionMappingCall<?, Object>, Boolean> _function = new Function1<AbstractConnectionMappingCall<?, Object>, Boolean>() {
            public Boolean apply(final AbstractConnectionMappingCall<?, Object> it) {
              return Boolean.valueOf(it.isLazy());
            }
          };
          Iterable<AbstractConnectionMappingCall<?, Object>> _filter = IterableExtensions.<AbstractConnectionMappingCall<?, Object>>filter(_incoming, _function);
          final Consumer<AbstractConnectionMappingCall<?, Object>> _function_1 = new Consumer<AbstractConnectionMappingCall<?, Object>>() {
            public void accept(final AbstractConnectionMappingCall<?, Object> it) {
              final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
                public void apply(final XConnection it) {
                  it.setTarget(node);
                }
              };
              XDiagramConfigInterpreter.this.execute(it, nodeObject, _function, context, false);
            }
          };
          _filter.forEach(_function_1);
          List<AbstractConnectionMappingCall<?, Object>> _outgoing = nodeMappingCasted.getOutgoing();
          final Function1<AbstractConnectionMappingCall<?, Object>, Boolean> _function_2 = new Function1<AbstractConnectionMappingCall<?, Object>, Boolean>() {
            public Boolean apply(final AbstractConnectionMappingCall<?, Object> it) {
              return Boolean.valueOf(it.isLazy());
            }
          };
          Iterable<AbstractConnectionMappingCall<?, Object>> _filter_1 = IterableExtensions.<AbstractConnectionMappingCall<?, Object>>filter(_outgoing, _function_2);
          final Consumer<AbstractConnectionMappingCall<?, Object>> _function_3 = new Consumer<AbstractConnectionMappingCall<?, Object>>() {
            public void accept(final AbstractConnectionMappingCall<?, Object> it) {
              final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
                public void apply(final XConnection it) {
                  it.setSource(node);
                }
              };
              XDiagramConfigInterpreter.this.execute(it, nodeObject, _function, context, false);
            }
          };
          _filter_1.forEach(_function_3);
        }
      }
    }
  }
  
  protected <T extends Object> XNode createNode(final T nodeObject, final NodeMapping<T> nodeMapping, final InterpreterContext context, final boolean isCreateOnDemand) {
    boolean _isApplicable = nodeMapping.isApplicable(nodeObject);
    if (_isApplicable) {
      final IMappedElementDescriptor<T> descriptor = this.<T>getDescriptor(nodeObject, nodeMapping);
      final XNode existingNode = context.<Object>getNode(descriptor);
      boolean _or = false;
      boolean _notEquals = (!Objects.equal(existingNode, null));
      if (_notEquals) {
        _or = true;
      } else {
        _or = (!isCreateOnDemand);
      }
      if (_or) {
        return existingNode;
      }
      final XNode node = nodeMapping.createNode(descriptor);
      context.addNode(node);
      List<AbstractConnectionMappingCall<?, T>> _incoming = nodeMapping.getIncoming();
      final Consumer<AbstractConnectionMappingCall<?, T>> _function = new Consumer<AbstractConnectionMappingCall<?, T>>() {
        public void accept(final AbstractConnectionMappingCall<?, T> it) {
          boolean _isLazy = it.isLazy();
          boolean _not = (!_isLazy);
          if (_not) {
            final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
              public void apply(final XConnection it) {
                it.setTarget(node);
              }
            };
            XDiagramConfigInterpreter.this.execute(it, nodeObject, _function, context, true);
          }
        }
      };
      _incoming.forEach(_function);
      List<AbstractConnectionMappingCall<?, T>> _outgoing = nodeMapping.getOutgoing();
      final Consumer<AbstractConnectionMappingCall<?, T>> _function_1 = new Consumer<AbstractConnectionMappingCall<?, T>>() {
        public void accept(final AbstractConnectionMappingCall<?, T> it) {
          boolean _isLazy = it.isLazy();
          boolean _not = (!_isLazy);
          if (_not) {
            final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
              public void apply(final XConnection it) {
                it.setSource(node);
              }
            };
            XDiagramConfigInterpreter.this.execute(it, nodeObject, _function, context, true);
          }
        }
      };
      _outgoing.forEach(_function_1);
      if ((node instanceof OpenableDiagramNode)) {
        DiagramMappingCall<?, T> _nestedDiagram = nodeMapping.getNestedDiagram();
        XDiagram _execute = null;
        if (_nestedDiagram!=null) {
          _execute=this.execute(_nestedDiagram, nodeObject, context);
        }
        ((OpenableDiagramNode)node).setInnerDiagram(_execute);
      }
      return node;
    } else {
      return null;
    }
  }
  
  protected <T extends Object> XConnection createConnection(final T connectionObject, final ConnectionMapping<T> connectionMapping, final InterpreterContext context) {
    boolean _isApplicable = connectionMapping.isApplicable(connectionObject);
    if (_isApplicable) {
      final ConnectionMapping<T> connectionMappingCasted = ((ConnectionMapping<T>) connectionMapping);
      final IMappedElementDescriptor<T> descriptor = this.<T>getDescriptor(connectionObject, connectionMappingCasted);
      final XConnection existingConnection = context.<Object>getConnection(descriptor);
      boolean _notEquals = (!Objects.equal(existingConnection, null));
      if (_notEquals) {
        return existingConnection;
      }
      final XConnection connection = connectionMappingCasted.createConnection(descriptor);
      return connection;
    } else {
      return null;
    }
  }
  
  public <T extends Object, U extends Object> Iterable<T> select(final AbstractNodeMappingCall<T, U> nodeMappingCall, final U domainArgument) {
    if ((nodeMappingCall instanceof NodeMappingCall<?, ?>)) {
      final NodeMappingCall<T, U> nodeMappingCallCasted = ((NodeMappingCall<T, U>) nodeMappingCall);
      Function1<? super U, ? extends T> _selector = nodeMappingCallCasted.getSelector();
      final T nodeObject = ((Function1<? super Object, ? extends T>) ((Function1<? super Object, ? extends T>)_selector)).apply(domainArgument);
      return Collections.<T>unmodifiableList(CollectionLiterals.<T>newArrayList(nodeObject));
    } else {
      if ((nodeMappingCall instanceof MultiNodeMappingCall<?, ?>)) {
        final MultiNodeMappingCall<T, U> nodeMappingCallCasted_1 = ((MultiNodeMappingCall<T, U>) nodeMappingCall);
        Function1<? super U, ? extends Iterable<? extends T>> _selector_1 = nodeMappingCallCasted_1.getSelector();
        return ((Function1<? super Object, ? extends Iterable<T>>) ((Function1<? super Object, ? extends Iterable<T>>)_selector_1)).apply(domainArgument);
      }
    }
    return null;
  }
  
  public <T extends Object, U extends Object> Iterable<XNode> execute(final AbstractNodeMappingCall<T, U> nodeMappingCall, final U domainArgument, final InterpreterContext context, final boolean isCreateNodeOnDemand) {
    final Iterable<T> nodeObjects = this.<T, U>select(nodeMappingCall, domainArgument);
    final ArrayList<XNode> result = CollectionLiterals.<XNode>newArrayList();
    for (final T nodeObject : nodeObjects) {
      NodeMapping<T> _nodeMapping = nodeMappingCall.getNodeMapping();
      XNode _createNode = this.<T>createNode(nodeObject, _nodeMapping, context, isCreateNodeOnDemand);
      result.add(_createNode);
    }
    return result;
  }
  
  public <T extends Object, U extends Object> Iterable<T> select(final AbstractConnectionMappingCall<T, U> connectionMappingCall, final U domainArgument) {
    if ((connectionMappingCall instanceof ConnectionMappingCall<?, ?>)) {
      final ConnectionMappingCall<T, U> connectionMappingCasted = ((ConnectionMappingCall<T, U>) connectionMappingCall);
      Function1<? super U, ? extends T> _selector = connectionMappingCasted.getSelector();
      final T connectionObject = ((Function1<? super Object, ? extends T>) ((Function1<? super Object, ? extends T>)_selector)).apply(domainArgument);
      return Collections.<T>unmodifiableList(CollectionLiterals.<T>newArrayList(connectionObject));
    } else {
      if ((connectionMappingCall instanceof MultiConnectionMappingCall<?, ?>)) {
        final MultiConnectionMappingCall<T, U> connectionMappingCasted_1 = ((MultiConnectionMappingCall<T, U>) connectionMappingCall);
        Function1<? super U, ? extends Iterable<? extends T>> _selector_1 = connectionMappingCasted_1.getSelector();
        return ((Function1<? super Object, ? extends Iterable<T>>) ((Function1<? super Object, ? extends Iterable<T>>)_selector_1)).apply(domainArgument);
      }
    }
    return null;
  }
  
  protected <T extends Object, U extends Object> Iterable<XConnection> execute(final AbstractConnectionMappingCall<T, U> connectionMappingCall, final U domainArgument, final Procedure1<? super XConnection> initializer, final InterpreterContext context, final boolean isCreateEndpointsOnDemand) {
    final Iterable<T> connectionObjects = this.<T, U>select(connectionMappingCall, domainArgument);
    final ArrayList<XConnection> result = CollectionLiterals.<XConnection>newArrayList();
    for (final T connectionObject : connectionObjects) {
      {
        ConnectionMapping<T> _connectionMapping = connectionMappingCall.getConnectionMapping();
        final XConnection connection = this.<T>createConnection(connectionObject, _connectionMapping, context);
        result.add(connection);
        initializer.apply(connection);
        ConnectionMapping<T> _connectionMapping_1 = connectionMappingCall.getConnectionMapping();
        this.<T>createEndpoints(_connectionMapping_1, connectionObject, connection, context, isCreateEndpointsOnDemand);
        boolean _and = false;
        XNode _source = connection.getSource();
        boolean _notEquals = (!Objects.equal(_source, null));
        if (!_notEquals) {
          _and = false;
        } else {
          XNode _target = connection.getTarget();
          boolean _notEquals_1 = (!Objects.equal(_target, null));
          _and = _notEquals_1;
        }
        if (_and) {
          context.addConnection(connection);
        }
      }
    }
    return result;
  }
  
  protected <T extends Object> void createEndpoints(final ConnectionMapping<T> connectionMapping, final T connectionObject, final XConnection connection, final InterpreterContext context, final boolean isCreateEndpointsOnDemand) {
    boolean _and = false;
    XNode _source = connection.getSource();
    boolean _equals = Objects.equal(_source, null);
    if (!_equals) {
      _and = false;
    } else {
      NodeMappingCall<?, T> _source_1 = connectionMapping.getSource();
      boolean _notEquals = (!Objects.equal(_source_1, null));
      _and = _notEquals;
    }
    if (_and) {
      NodeMappingCall<?, T> _source_2 = connectionMapping.getSource();
      Iterable<XNode> _execute = null;
      if (_source_2!=null) {
        _execute=this.execute(_source_2, connectionObject, context, isCreateEndpointsOnDemand);
      }
      XNode _head = IterableExtensions.<XNode>head(_execute);
      connection.setSource(_head);
    }
    boolean _and_1 = false;
    XNode _target = connection.getTarget();
    boolean _equals_1 = Objects.equal(_target, null);
    if (!_equals_1) {
      _and_1 = false;
    } else {
      NodeMappingCall<?, T> _target_1 = connectionMapping.getTarget();
      boolean _notEquals_1 = (!Objects.equal(_target_1, null));
      _and_1 = _notEquals_1;
    }
    if (_and_1) {
      NodeMappingCall<?, T> _target_2 = connectionMapping.getTarget();
      Iterable<XNode> _execute_1 = null;
      if (_target_2!=null) {
        _execute_1=this.execute(_target_2, connectionObject, context, isCreateEndpointsOnDemand);
      }
      XNode _head_1 = IterableExtensions.<XNode>head(_execute_1);
      connection.setTarget(_head_1);
    }
  }
  
  public <T extends Object, U extends Object> XDiagram execute(final DiagramMappingCall<T, U> diagramMappingCall, final U domainArgument, final InterpreterContext context) {
    Function1<? super U, ? extends T> _selector = diagramMappingCall.getSelector();
    final T diagramObject = _selector.apply(domainArgument);
    DiagramMapping<T> _diagramMapping = diagramMappingCall.getDiagramMapping();
    InterpreterContext _interpreterContext = new InterpreterContext();
    final XDiagram result = this.<T>createDiagram(diagramObject, _diagramMapping, _interpreterContext);
    return result;
  }
  
  public <T extends Object> IMappedElementDescriptor<T> getDescriptor(final T domainObject, final AbstractMapping<T> mapping) {
    XDiagramConfig _config = mapping.getConfig();
    IMappedElementDescriptorProvider _domainObjectProvider = _config.getDomainObjectProvider();
    return _domainObjectProvider.<T>createMappedElementDescriptor(domainObject, mapping);
  }
}
