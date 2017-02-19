package de.fxdiagram.mapping.execution;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDiagramContainer;
import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.mapping.AbstractLabelMapping;
import de.fxdiagram.mapping.AbstractLabelMappingCall;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.AbstractNodeMappingCall;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.ConnectionMappingCall;
import de.fxdiagram.mapping.DiagramMapping;
import de.fxdiagram.mapping.DiagramMappingCall;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.LabelMappingCall;
import de.fxdiagram.mapping.MultiConnectionMappingCall;
import de.fxdiagram.mapping.MultiLabelMappingCall;
import de.fxdiagram.mapping.MultiNodeMappingCall;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.NodeMappingCall;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.InterpreterContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Executes an {@link XDiagramConfig} on a given domain object.
 */
@SuppressWarnings("all")
public class XDiagramConfigInterpreter {
  public <T extends Object, U extends Object> XDiagram createDiagram(final T diagramObject, final DiagramMapping<T> diagramMapping, final boolean isOnDemand, final InterpreterContext context) {
    XDiagram _xblockexpression = null;
    {
      boolean _isApplicable = diagramMapping.isApplicable(diagramObject);
      boolean _not = (!_isApplicable);
      if (_not) {
        return null;
      }
      final IMappedElementDescriptor<T> descriptor = this.<T>getDescriptor(diagramObject, diagramMapping);
      boolean _equals = Objects.equal(descriptor, null);
      if (_equals) {
        return null;
      }
      XDiagram _diagram = context.getDiagram();
      DomainObjectDescriptor _domainObjectDescriptor = _diagram.getDomainObjectDescriptor();
      final boolean replaceDiagram = (!Objects.equal(_domainObjectDescriptor, descriptor));
      context.setIsReplaceRootDiagram(replaceDiagram);
      XDiagram _xifexpression = null;
      if (replaceDiagram) {
        XDiagram _createDiagram = diagramMapping.createDiagram(descriptor);
        final Procedure1<XDiagram> _function = (XDiagram it) -> {
          XDiagramConfig _config = diagramMapping.getConfig();
          _config.initialize(it);
        };
        _xifexpression = ObjectExtensions.<XDiagram>operator_doubleArrow(_createDiagram, _function);
      } else {
        _xifexpression = context.getDiagram();
      }
      final XDiagram diagram = _xifexpression;
      InterpreterContext _xifexpression_1 = null;
      if (replaceDiagram) {
        _xifexpression_1 = new InterpreterContext(diagram, context);
      } else {
        _xifexpression_1 = context;
      }
      final InterpreterContext newContext = _xifexpression_1;
      if (isOnDemand) {
        diagram.setLayoutOnActivate(true);
        final Procedure1<XDiagram> _function_1 = (XDiagram it) -> {
          final Function1<T, Object> _function_2 = (T domainObject) -> {
            Object _xblockexpression_1 = null;
            {
              this.<T>populateDiagram(diagramMapping, domainObject, newContext);
              newContext.directlyApplyChanges();
              XRoot _root = CoreExtensions.getRoot(diagram);
              final CommandStack commandStack = _root.getCommandStack();
              newContext.executeCommands(commandStack);
              _xblockexpression_1 = null;
            }
            return _xblockexpression_1;
          };
          descriptor.<Object>withDomainObject(_function_2);
        };
        diagram.setContentsInitializer(_function_1);
      } else {
        this.<T>populateDiagram(diagramMapping, diagramObject, newContext);
      }
      _xblockexpression = diagram;
    }
    return _xblockexpression;
  }
  
  protected <T extends Object> void populateDiagram(final DiagramMapping<T> diagramMapping, final T diagramObject, final InterpreterContext context) {
    List<AbstractNodeMappingCall<?, T>> _nodes = diagramMapping.getNodes();
    final Consumer<AbstractNodeMappingCall<?, T>> _function = (AbstractNodeMappingCall<?, T> it) -> {
      this.execute(it, diagramObject, context);
    };
    _nodes.forEach(_function);
    List<AbstractConnectionMappingCall<?, T>> _connections = diagramMapping.getConnections();
    final Consumer<AbstractConnectionMappingCall<?, T>> _function_1 = (AbstractConnectionMappingCall<?, T> it) -> {
      final Procedure1<XConnection> _function_2 = (XConnection it_1) -> {
      };
      this.execute(it, diagramObject, _function_2, context);
    };
    _connections.forEach(_function_1);
    context.setIsCreateNodes(false);
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
    context.setIsCreateNodes(true);
  }
  
  public <T extends Object> void connectNodesEagerly(final AbstractNodeMappingCall<?, T> it, final T diagramObject, final Set<ConnectionMapping<?>> eagerConnections, final InterpreterContext context) {
    final Iterable<?> nodeObjects = this.select(it, diagramObject);
    NodeMapping<?> _nodeMapping = it.getNodeMapping();
    final NodeMapping<Object> nodeMappingCasted = ((NodeMapping<Object>) _nodeMapping);
    for (final Object nodeObject : nodeObjects) {
      {
        final IMappedElementDescriptor<Object> descriptor = this.<Object>getDescriptor(nodeObject, nodeMappingCasted);
        final XNode node = context.getNode(descriptor);
        boolean _notEquals = (!Objects.equal(node, null));
        if (_notEquals) {
          List<AbstractConnectionMappingCall<?, Object>> _incoming = nodeMappingCasted.getIncoming();
          final Function1<AbstractConnectionMappingCall<?, Object>, Boolean> _function = (AbstractConnectionMappingCall<?, Object> it_1) -> {
            return Boolean.valueOf((it_1.isOnDemand() && eagerConnections.contains(it_1.getMapping())));
          };
          Iterable<AbstractConnectionMappingCall<?, Object>> _filter = IterableExtensions.<AbstractConnectionMappingCall<?, Object>>filter(_incoming, _function);
          final Consumer<AbstractConnectionMappingCall<?, Object>> _function_1 = (AbstractConnectionMappingCall<?, Object> it_1) -> {
            final Procedure1<XConnection> _function_2 = (XConnection it_2) -> {
              it_2.setTarget(node);
            };
            this.execute(it_1, nodeObject, _function_2, context);
          };
          _filter.forEach(_function_1);
          List<AbstractConnectionMappingCall<?, Object>> _outgoing = nodeMappingCasted.getOutgoing();
          final Function1<AbstractConnectionMappingCall<?, Object>, Boolean> _function_2 = (AbstractConnectionMappingCall<?, Object> it_1) -> {
            return Boolean.valueOf((it_1.isOnDemand() && eagerConnections.contains(it_1.getMapping())));
          };
          Iterable<AbstractConnectionMappingCall<?, Object>> _filter_1 = IterableExtensions.<AbstractConnectionMappingCall<?, Object>>filter(_outgoing, _function_2);
          final Consumer<AbstractConnectionMappingCall<?, Object>> _function_3 = (AbstractConnectionMappingCall<?, Object> it_1) -> {
            final Procedure1<XConnection> _function_4 = (XConnection it_2) -> {
              it_2.setSource(node);
            };
            this.execute(it_1, nodeObject, _function_4, context);
          };
          _filter_1.forEach(_function_3);
        }
      }
    }
  }
  
  protected <T extends Object> XLabel createLabel(final T labelObject, final AbstractLabelMapping<T> labelMapping) {
    boolean _isApplicable = labelMapping.isApplicable(labelObject);
    if (_isApplicable) {
      final IMappedElementDescriptor<T> descriptor = this.<T>getDescriptor(labelObject, labelMapping);
      boolean _notEquals = (!Objects.equal(descriptor, null));
      if (_notEquals) {
        final XLabel label = labelMapping.createLabel(descriptor, labelObject);
        XDiagramConfig _config = labelMapping.getConfig();
        _config.initialize(label);
        return label;
      }
    }
    return null;
  }
  
  public <T extends Object, U extends Object> Iterable<T> select(final AbstractLabelMappingCall<T, U> labelMappingCall, final U domainArgument) {
    boolean _equals = Objects.equal(domainArgument, null);
    if (_equals) {
      return Collections.<T>unmodifiableList(CollectionLiterals.<T>newArrayList());
    }
    if ((labelMappingCall instanceof LabelMappingCall<?, ?>)) {
      final LabelMappingCall<T, U> labelMappingCallCasted = ((LabelMappingCall<T, U>) labelMappingCall);
      Function1<? super U, ? extends T> _selector = labelMappingCallCasted.getSelector();
      final T labelObject = ((Function1<? super Object, ? extends T>) ((Function1<? super Object, ? extends T>)_selector)).apply(domainArgument);
      boolean _equals_1 = Objects.equal(labelObject, null);
      if (_equals_1) {
        return Collections.<T>unmodifiableList(CollectionLiterals.<T>newArrayList());
      } else {
        return Collections.<T>unmodifiableList(CollectionLiterals.<T>newArrayList(labelObject));
      }
    } else {
      if ((labelMappingCall instanceof MultiLabelMappingCall<?, ?>)) {
        final MultiLabelMappingCall<T, U> labelMappingCallCasted_1 = ((MultiLabelMappingCall<T, U>) labelMappingCall);
        Function1<? super U, ? extends Iterable<? extends T>> _selector_1 = labelMappingCallCasted_1.getSelector();
        Iterable<T> _apply = ((Function1<? super Object, ? extends Iterable<T>>) ((Function1<? super Object, ? extends Iterable<T>>)_selector_1)).apply(domainArgument);
        return IterableExtensions.<T>filterNull(_apply);
      }
    }
    return null;
  }
  
  public <T extends Object, U extends Object> Iterable<? extends XLabel> execute(final AbstractLabelMappingCall<T, U> labelMappingCall, final U domainArgument) {
    final Iterable<T> labelObjects = this.<T, U>select(labelMappingCall, domainArgument);
    final ArrayList<XLabel> result = CollectionLiterals.<XLabel>newArrayList();
    for (final T labelObject : labelObjects) {
      {
        AbstractLabelMapping<T> _labelMapping = labelMappingCall.getLabelMapping();
        final XLabel label = this.<T>createLabel(labelObject, _labelMapping);
        boolean _notEquals = (!Objects.equal(label, null));
        if (_notEquals) {
          result.add(label);
        }
      }
    }
    return result;
  }
  
  public <T extends Object> XNode createNode(final T nodeObject, final NodeMapping<T> nodeMapping, final InterpreterContext context) {
    boolean _isApplicable = nodeMapping.isApplicable(nodeObject);
    if (_isApplicable) {
      final IMappedElementDescriptor<T> descriptor = this.<T>getDescriptor(nodeObject, nodeMapping);
      boolean _equals = Objects.equal(descriptor, null);
      if (_equals) {
        return null;
      }
      boolean _isCreateDuplicateNodes = context.isCreateDuplicateNodes();
      boolean _not = (!_isCreateDuplicateNodes);
      if (_not) {
        final XNode existingNode = context.getNode(descriptor);
        if (((!Objects.equal(existingNode, null)) || (!context.isCreateNodes()))) {
          return existingNode;
        }
      }
      final XNode node = nodeMapping.createNode(descriptor);
      XDiagramConfig _config = nodeMapping.getConfig();
      _config.initialize(node);
      context.addNode(node);
      boolean _isCreateConnections = context.isCreateConnections();
      if (_isCreateConnections) {
        List<AbstractConnectionMappingCall<?, T>> _incoming = nodeMapping.getIncoming();
        final Consumer<AbstractConnectionMappingCall<?, T>> _function = (AbstractConnectionMappingCall<?, T> it) -> {
          boolean _isOnDemand = it.isOnDemand();
          boolean _not_1 = (!_isOnDemand);
          if (_not_1) {
            final Procedure1<XConnection> _function_1 = (XConnection it_1) -> {
              it_1.setTarget(node);
            };
            this.execute(it, nodeObject, _function_1, context);
          }
        };
        _incoming.forEach(_function);
        List<AbstractConnectionMappingCall<?, T>> _outgoing = nodeMapping.getOutgoing();
        final Consumer<AbstractConnectionMappingCall<?, T>> _function_1 = (AbstractConnectionMappingCall<?, T> it) -> {
          boolean _isOnDemand = it.isOnDemand();
          boolean _not_1 = (!_isOnDemand);
          if (_not_1) {
            final Procedure1<XConnection> _function_2 = (XConnection it_1) -> {
              it_1.setSource(node);
            };
            this.execute(it, nodeObject, _function_2, context);
          }
        };
        _outgoing.forEach(_function_1);
      }
      List<AbstractLabelMappingCall<?, T>> _labels = nodeMapping.getLabels();
      final Consumer<AbstractLabelMappingCall<?, T>> _function_2 = (AbstractLabelMappingCall<?, T> it) -> {
        ObservableList<XLabel> _labels_1 = node.getLabels();
        Iterable<? extends XLabel> _execute = this.execute(it, nodeObject);
        Iterables.<XLabel>addAll(_labels_1, _execute);
      };
      _labels.forEach(_function_2);
      if ((node instanceof XDiagramContainer)) {
        DiagramMappingCall<?, T> _nestedDiagram = nodeMapping.getNestedDiagram();
        XDiagram _execute = null;
        if (_nestedDiagram!=null) {
          XDiagram _xDiagram = new XDiagram();
          InterpreterContext _interpreterContext = new InterpreterContext(_xDiagram, context);
          _execute=this.execute(_nestedDiagram, nodeObject, _interpreterContext);
        }
        ((XDiagramContainer)node).setInnerDiagram(_execute);
      }
      return node;
    } else {
      return null;
    }
  }
  
  public <T extends Object> XConnection createConnection(final T connectionObject, final ConnectionMapping<T> connectionMapping, final Procedure1<? super XConnection> initializer, final InterpreterContext context) {
    boolean _isApplicable = connectionMapping.isApplicable(connectionObject);
    if (_isApplicable) {
      final ConnectionMapping<T> connectionMappingCasted = ((ConnectionMapping<T>) connectionMapping);
      final IMappedElementDescriptor<T> descriptor = this.<T>getDescriptor(connectionObject, connectionMappingCasted);
      boolean _equals = Objects.equal(descriptor, null);
      if (_equals) {
        return null;
      }
      final XConnection existingConnection = context.getConnection(descriptor);
      if (((!Objects.equal(existingConnection, null)) || (!context.isCreateConnections()))) {
        return existingConnection;
      }
      final XConnection connection = connectionMappingCasted.createConnection(descriptor);
      XDiagramConfig _config = connectionMappingCasted.getConfig();
      _config.initialize(connection);
      List<AbstractLabelMappingCall<?, T>> _labels = connectionMapping.getLabels();
      final Consumer<AbstractLabelMappingCall<?, T>> _function = (AbstractLabelMappingCall<?, T> it) -> {
        ObservableList<XConnectionLabel> _labels_1 = connection.getLabels();
        Iterable<? extends XLabel> _execute = this.execute(it, connectionObject);
        Iterable<XConnectionLabel> _filter = Iterables.<XConnectionLabel>filter(_execute, XConnectionLabel.class);
        Iterables.<XConnectionLabel>addAll(_labels_1, _filter);
      };
      _labels.forEach(_function);
      initializer.apply(connection);
      this.<T>createEndpoints(connectionMapping, connectionObject, connection, context);
      if (((!Objects.equal(connection.getSource(), null)) && (!Objects.equal(connection.getTarget(), null)))) {
        context.addConnection(connection);
        return connection;
      }
    }
    return null;
  }
  
  public <T extends Object, U extends Object> Iterable<T> select(final AbstractNodeMappingCall<T, U> nodeMappingCall, final U domainArgument) {
    boolean _equals = Objects.equal(domainArgument, null);
    if (_equals) {
      return Collections.<T>unmodifiableList(CollectionLiterals.<T>newArrayList());
    }
    if ((nodeMappingCall instanceof NodeMappingCall<?, ?>)) {
      final NodeMappingCall<T, U> nodeMappingCallCasted = ((NodeMappingCall<T, U>) nodeMappingCall);
      Function1<? super U, ? extends T> _selector = nodeMappingCallCasted.getSelector();
      final T nodeObject = ((Function1<? super Object, ? extends T>) ((Function1<? super Object, ? extends T>)_selector)).apply(domainArgument);
      boolean _equals_1 = Objects.equal(nodeObject, null);
      if (_equals_1) {
        return Collections.<T>unmodifiableList(CollectionLiterals.<T>newArrayList());
      } else {
        return Collections.<T>unmodifiableList(CollectionLiterals.<T>newArrayList(nodeObject));
      }
    } else {
      if ((nodeMappingCall instanceof MultiNodeMappingCall<?, ?>)) {
        final MultiNodeMappingCall<T, U> nodeMappingCallCasted_1 = ((MultiNodeMappingCall<T, U>) nodeMappingCall);
        Function1<? super U, ? extends Iterable<? extends T>> _selector_1 = nodeMappingCallCasted_1.getSelector();
        Iterable<T> _apply = ((Function1<? super Object, ? extends Iterable<T>>) ((Function1<? super Object, ? extends Iterable<T>>)_selector_1)).apply(domainArgument);
        return IterableExtensions.<T>filterNull(_apply);
      }
    }
    return null;
  }
  
  public <T extends Object, U extends Object> Iterable<XNode> execute(final AbstractNodeMappingCall<T, U> nodeMappingCall, final U domainArgument, final InterpreterContext context) {
    final Iterable<T> nodeObjects = this.<T, U>select(nodeMappingCall, domainArgument);
    final ArrayList<XNode> result = CollectionLiterals.<XNode>newArrayList();
    for (final T nodeObject : nodeObjects) {
      NodeMapping<T> _nodeMapping = nodeMappingCall.getNodeMapping();
      XNode _createNode = this.<T>createNode(nodeObject, _nodeMapping, context);
      result.add(_createNode);
    }
    return result;
  }
  
  public <T extends Object, U extends Object> Iterable<T> select(final AbstractConnectionMappingCall<T, U> connectionMappingCall, final U domainArgument) {
    boolean _equals = Objects.equal(domainArgument, null);
    if (_equals) {
      return Collections.<T>unmodifiableList(CollectionLiterals.<T>newArrayList());
    }
    if ((connectionMappingCall instanceof ConnectionMappingCall<?, ?>)) {
      final ConnectionMappingCall<T, U> connectionMappingCasted = ((ConnectionMappingCall<T, U>) connectionMappingCall);
      Function1<? super U, ? extends T> _selector = connectionMappingCasted.getSelector();
      final T connectionObject = ((Function1<? super Object, ? extends T>) ((Function1<? super Object, ? extends T>)_selector)).apply(domainArgument);
      boolean _equals_1 = Objects.equal(connectionObject, null);
      if (_equals_1) {
        return Collections.<T>unmodifiableList(CollectionLiterals.<T>newArrayList());
      } else {
        return Collections.<T>unmodifiableList(CollectionLiterals.<T>newArrayList(connectionObject));
      }
    } else {
      if ((connectionMappingCall instanceof MultiConnectionMappingCall<?, ?>)) {
        final MultiConnectionMappingCall<T, U> connectionMappingCasted_1 = ((MultiConnectionMappingCall<T, U>) connectionMappingCall);
        Function1<? super U, ? extends Iterable<? extends T>> _selector_1 = connectionMappingCasted_1.getSelector();
        Iterable<T> _apply = ((Function1<? super Object, ? extends Iterable<T>>) ((Function1<? super Object, ? extends Iterable<T>>)_selector_1)).apply(domainArgument);
        return IterableExtensions.<T>filterNull(_apply);
      }
    }
    return null;
  }
  
  public <T extends Object, U extends Object> Iterable<XConnection> execute(final AbstractConnectionMappingCall<T, U> connectionMappingCall, final U domainArgument, final Procedure1<? super XConnection> initializer, final InterpreterContext context) {
    final Iterable<T> connectionObjects = this.<T, U>select(connectionMappingCall, domainArgument);
    final ArrayList<XConnection> result = CollectionLiterals.<XConnection>newArrayList();
    for (final T connectionObject : connectionObjects) {
      {
        ConnectionMapping<T> _connectionMapping = connectionMappingCall.getConnectionMapping();
        final XConnection connection = this.<T>createConnection(connectionObject, _connectionMapping, initializer, context);
        result.add(connection);
      }
    }
    return result;
  }
  
  protected <T extends Object> void createEndpoints(final ConnectionMapping<T> connectionMapping, final T connectionObject, final XConnection connection, final InterpreterContext context) {
    if ((Objects.equal(connection.getSource(), null) && (!Objects.equal(connectionMapping.getSource(), null)))) {
      NodeMappingCall<?, T> _source = connectionMapping.getSource();
      Iterable<XNode> _execute = null;
      if (_source!=null) {
        _execute=this.execute(_source, connectionObject, context);
      }
      XNode _head = IterableExtensions.<XNode>head(_execute);
      connection.setSource(_head);
    }
    if ((Objects.equal(connection.getTarget(), null) && (!Objects.equal(connectionMapping.getTarget(), null)))) {
      NodeMappingCall<?, T> _target = connectionMapping.getTarget();
      Iterable<XNode> _execute_1 = null;
      if (_target!=null) {
        _execute_1=this.execute(_target, connectionObject, context);
      }
      XNode _head_1 = IterableExtensions.<XNode>head(_execute_1);
      connection.setTarget(_head_1);
    }
  }
  
  public <T extends Object, U extends Object> XDiagram execute(final DiagramMappingCall<T, U> diagramMappingCall, final U domainArgument, final InterpreterContext context) {
    Function1<? super U, ? extends T> _selector = diagramMappingCall.getSelector();
    final T diagramObject = _selector.apply(domainArgument);
    boolean _equals = Objects.equal(diagramObject, null);
    if (_equals) {
      return null;
    }
    AbstractMapping<T> _mapping = diagramMappingCall.getMapping();
    boolean _isOnDemand = diagramMappingCall.isOnDemand();
    final XDiagram result = this.<T, Object>createDiagram(diagramObject, ((DiagramMapping<T>) _mapping), _isOnDemand, context);
    return result;
  }
  
  public <T extends Object> IMappedElementDescriptor<T> getDescriptor(final T domainObject, final AbstractMapping<T> mapping) {
    XDiagramConfig _config = mapping.getConfig();
    IMappedElementDescriptorProvider _domainObjectProvider = _config.getDomainObjectProvider();
    return _domainObjectProvider.<T>createMappedElementDescriptor(domainObject, mapping);
  }
}
