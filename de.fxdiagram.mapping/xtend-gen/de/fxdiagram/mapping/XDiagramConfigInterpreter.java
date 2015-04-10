package de.fxdiagram.mapping;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import de.fxdiagram.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.AbstractNodeMappingCall;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.ConnectionMappingCall;
import de.fxdiagram.mapping.DiagramMapping;
import de.fxdiagram.mapping.DiagramMappingCall;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.InterpreterContext;
import de.fxdiagram.mapping.MultiConnectionMappingCall;
import de.fxdiagram.mapping.MultiNodeMappingCall;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.NodeMappingCall;
import de.fxdiagram.mapping.XDiagramConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Executes an {@link XDiagramConfig} on a given domain object.
 */
@SuppressWarnings("all")
public class XDiagramConfigInterpreter {
  public <T extends Object, U extends Object> XDiagram createDiagram(final T diagramObject, final DiagramMappingCall<T, U> diagramMappingCall, final InterpreterContext context) {
    XDiagram _xblockexpression = null;
    {
      final DiagramMapping<T> diagramMapping = diagramMappingCall.getDiagramMapping();
      boolean _isApplicable = diagramMapping.isApplicable(diagramObject);
      boolean _not = (!_isApplicable);
      if (_not) {
        return null;
      }
      final IMappedElementDescriptor<T> descriptor = this.<T>getDescriptor(diagramObject, diagramMapping);
      final XDiagram diagram = diagramMapping.createDiagram(descriptor);
      final InterpreterContext newContext = new InterpreterContext(diagram);
      boolean _isOnDemand = diagramMappingCall.isOnDemand();
      if (_isOnDemand) {
        final Procedure1<XDiagram> _function = (XDiagram it) -> {
          final Function1<T, Object> _function_1 = (T domainObject) -> {
            Object _xblockexpression_1 = null;
            {
              this.<T>populateDiagram(diagramMapping, domainObject, newContext);
              newContext.applyChanges();
              XRoot _root = CoreExtensions.getRoot(diagram);
              final CommandStack commandStack = _root.getCommandStack();
              newContext.executeCommands(commandStack);
              _xblockexpression_1 = null;
            }
            return _xblockexpression_1;
          };
          descriptor.<Object>withDomainObject(_function_1);
        };
        diagram.setContentsInitializer(_function);
      } else {
        this.<T>populateDiagram(diagramMapping, diagramObject, newContext);
        context.addSubContext(newContext);
      }
      _xblockexpression = diagram;
    }
    return _xblockexpression;
  }
  
  protected <T extends Object> void populateDiagram(final DiagramMapping<T> diagramMapping, final T diagramObject, final InterpreterContext context) {
    List<AbstractNodeMappingCall<?, T>> _nodes = diagramMapping.getNodes();
    final Consumer<AbstractNodeMappingCall<?, T>> _function = (AbstractNodeMappingCall<?, T> it) -> {
      this.execute(it, diagramObject, context, true);
    };
    _nodes.forEach(_function);
    List<AbstractConnectionMappingCall<?, T>> _connections = diagramMapping.getConnections();
    final Consumer<AbstractConnectionMappingCall<?, T>> _function_1 = (AbstractConnectionMappingCall<?, T> it) -> {
      final Procedure1<XConnection> _function_2 = (XConnection it_1) -> {
      };
      this.execute(it, diagramObject, _function_2, context, true);
    };
    _connections.forEach(_function_1);
    List<ConnectionMapping<?>> _eagerConnections = diagramMapping.getEagerConnections();
    boolean _isEmpty = _eagerConnections.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      List<ConnectionMapping<?>> _eagerConnections_1 = diagramMapping.getEagerConnections();
      final HashSet<ConnectionMapping<?>> eagerConnections = new HashSet<ConnectionMapping<?>>(_eagerConnections_1);
      List<AbstractNodeMappingCall<?, T>> _nodes_1 = diagramMapping.getNodes();
      final Consumer<AbstractNodeMappingCall<?, T>> _function_2 = (AbstractNodeMappingCall<?, T> it) -> {
        this.<T>connectNodesEagerly(it, diagramObject, eagerConnections, context);
      };
      _nodes_1.forEach(_function_2);
    }
  }
  
  protected <T extends Object> void connectNodesEagerly(final AbstractNodeMappingCall<?, T> it, final T diagramObject, final Set<ConnectionMapping<?>> eagerConnections, final InterpreterContext context) {
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
          final Function1<AbstractConnectionMappingCall<?, Object>, Boolean> _function = (AbstractConnectionMappingCall<?, Object> it_1) -> {
            boolean _and = false;
            boolean _isOnDemand = it_1.isOnDemand();
            if (!_isOnDemand) {
              _and = false;
            } else {
              AbstractMapping<?> _mapping = it_1.getMapping();
              boolean _contains = eagerConnections.contains(_mapping);
              _and = _contains;
            }
            return Boolean.valueOf(_and);
          };
          Iterable<AbstractConnectionMappingCall<?, Object>> _filter = IterableExtensions.<AbstractConnectionMappingCall<?, Object>>filter(_incoming, _function);
          final Consumer<AbstractConnectionMappingCall<?, Object>> _function_1 = (AbstractConnectionMappingCall<?, Object> it_1) -> {
            final Procedure1<XConnection> _function_2 = (XConnection it_2) -> {
              it_2.setTarget(node);
            };
            this.execute(it_1, nodeObject, _function_2, context, false);
          };
          _filter.forEach(_function_1);
          List<AbstractConnectionMappingCall<?, Object>> _outgoing = nodeMappingCasted.getOutgoing();
          final Function1<AbstractConnectionMappingCall<?, Object>, Boolean> _function_2 = (AbstractConnectionMappingCall<?, Object> it_1) -> {
            boolean _and = false;
            boolean _isOnDemand = it_1.isOnDemand();
            if (!_isOnDemand) {
              _and = false;
            } else {
              AbstractMapping<?> _mapping = it_1.getMapping();
              boolean _contains = eagerConnections.contains(_mapping);
              _and = _contains;
            }
            return Boolean.valueOf(_and);
          };
          Iterable<AbstractConnectionMappingCall<?, Object>> _filter_1 = IterableExtensions.<AbstractConnectionMappingCall<?, Object>>filter(_outgoing, _function_2);
          final Consumer<AbstractConnectionMappingCall<?, Object>> _function_3 = (AbstractConnectionMappingCall<?, Object> it_1) -> {
            final Procedure1<XConnection> _function_4 = (XConnection it_2) -> {
              it_2.setSource(node);
            };
            this.execute(it_1, nodeObject, _function_4, context, false);
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
      XDiagramConfig _config = nodeMapping.getConfig();
      _config.initialize(node);
      context.addNode(node);
      List<AbstractConnectionMappingCall<?, T>> _incoming = nodeMapping.getIncoming();
      final Consumer<AbstractConnectionMappingCall<?, T>> _function = (AbstractConnectionMappingCall<?, T> it) -> {
        boolean _isOnDemand = it.isOnDemand();
        boolean _not = (!_isOnDemand);
        if (_not) {
          final Procedure1<XConnection> _function_1 = (XConnection it_1) -> {
            it_1.setTarget(node);
          };
          this.execute(it, nodeObject, _function_1, context, true);
        }
      };
      _incoming.forEach(_function);
      List<AbstractConnectionMappingCall<?, T>> _outgoing = nodeMapping.getOutgoing();
      final Consumer<AbstractConnectionMappingCall<?, T>> _function_1 = (AbstractConnectionMappingCall<?, T> it) -> {
        boolean _isOnDemand = it.isOnDemand();
        boolean _not = (!_isOnDemand);
        if (_not) {
          final Procedure1<XConnection> _function_2 = (XConnection it_1) -> {
            it_1.setSource(node);
          };
          this.execute(it, nodeObject, _function_2, context, true);
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
      XDiagramConfig _config = connectionMappingCasted.getConfig();
      _config.initialize(connection);
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
  
  public <T extends Object, U extends Object> Iterable<XConnection> execute(final AbstractConnectionMappingCall<T, U> connectionMappingCall, final U domainArgument, final Procedure1<? super XConnection> initializer, final InterpreterContext context, final boolean isCreateEndpointsOnDemand) {
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
    final XDiagram result = this.<T, U>createDiagram(diagramObject, diagramMappingCall, context);
    return result;
  }
  
  public <T extends Object> IMappedElementDescriptor<T> getDescriptor(final T domainObject, final AbstractMapping<T> mapping) {
    XDiagramConfig _config = mapping.getConfig();
    IMappedElementDescriptorProvider _domainObjectProvider = _config.getDomainObjectProvider();
    return _domainObjectProvider.<T>createMappedElementDescriptor(domainObject, mapping);
  }
}
