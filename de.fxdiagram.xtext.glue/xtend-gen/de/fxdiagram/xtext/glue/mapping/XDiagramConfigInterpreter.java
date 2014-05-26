package de.fxdiagram.xtext.glue.mapping;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.XtextDomainObjectProvider;
import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.AbstractNodeMappingCall;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.ConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.DiagramMapping;
import de.fxdiagram.xtext.glue.mapping.DiagramMappingCall;
import de.fxdiagram.xtext.glue.mapping.InterpreterContext;
import de.fxdiagram.xtext.glue.mapping.MultiConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.MultiNodeMappingCall;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import de.fxdiagram.xtext.glue.mapping.NodeMappingCall;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XDiagramConfigInterpreter {
  private XtextDomainObjectProvider domainObjectProvider;
  
  public XDiagramConfigInterpreter(final XtextDomainObjectProvider domainObjectProvider) {
    this.domainObjectProvider = domainObjectProvider;
  }
  
  public <T extends Object> XDiagram createDiagram(final T diagramObject, final DiagramMapping<T> diagramMapping, final InterpreterContext context) {
    XDiagram _xblockexpression = null;
    {
      boolean _isApplicable = diagramMapping.isApplicable(diagramObject);
      boolean _not = (!_isApplicable);
      if (_not) {
        return null;
      }
      Function1<? super XtextDomainObjectDescriptor<T>,? extends XDiagram> _createDiagram = diagramMapping.getCreateDiagram();
      XtextDomainObjectDescriptor<T> _descriptor = this.<T>getDescriptor(diagramObject, diagramMapping);
      final XDiagram diagram = _createDiagram.apply(_descriptor);
      context.diagram = diagram;
      List<AbstractNodeMappingCall<?,T>> _nodes = diagramMapping.getNodes();
      final Procedure1<AbstractNodeMappingCall<?,T>> _function = new Procedure1<AbstractNodeMappingCall<?,T>>() {
        public void apply(final AbstractNodeMappingCall<?,T> it) {
          XDiagramConfigInterpreter.this.execute(it, diagramObject, context);
        }
      };
      IterableExtensions.<AbstractNodeMappingCall<?,T>>forEach(_nodes, _function);
      List<AbstractConnectionMappingCall<?,T>> _connections = diagramMapping.getConnections();
      final Procedure1<AbstractConnectionMappingCall<?,T>> _function_1 = new Procedure1<AbstractConnectionMappingCall<?,T>>() {
        public void apply(final AbstractConnectionMappingCall<?,T> it) {
          final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
            public void apply(final XConnection it) {
            }
          };
          XDiagramConfigInterpreter.this.execute(it, diagramObject, _function, context);
        }
      };
      IterableExtensions.<AbstractConnectionMappingCall<?,T>>forEach(_connections, _function_1);
      _xblockexpression = diagram;
    }
    return _xblockexpression;
  }
  
  public <T extends Object> XNode createNode(final T nodeObject, final NodeMapping<T> nodeMapping, final InterpreterContext context) {
    boolean _isApplicable = nodeMapping.isApplicable(nodeObject);
    if (_isApplicable) {
      final XtextDomainObjectDescriptor<T> descriptor = this.<T>getDescriptor(nodeObject, nodeMapping);
      final XNode existingNode = context.<Object>getNode(descriptor);
      boolean _notEquals = (!Objects.equal(existingNode, null));
      if (_notEquals) {
        return existingNode;
      }
      Function1<? super XtextDomainObjectDescriptor<T>,? extends XNode> _createNode = nodeMapping.getCreateNode();
      final XNode node = _createNode.apply(descriptor);
      context.addNode(node);
      List<AbstractConnectionMappingCall<?,T>> _incoming = nodeMapping.getIncoming();
      final Procedure1<AbstractConnectionMappingCall<?,T>> _function = new Procedure1<AbstractConnectionMappingCall<?,T>>() {
        public void apply(final AbstractConnectionMappingCall<?,T> it) {
          boolean _or = false;
          boolean _isLazy = it.isLazy();
          boolean _not = (!_isLazy);
          if (_not) {
            _or = true;
          } else {
            boolean _isIsIgnoreLazy = context.isIsIgnoreLazy();
            _or = _isIsIgnoreLazy;
          }
          if (_or) {
            final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
              public void apply(final XConnection it) {
                it.setTarget(node);
              }
            };
            XDiagramConfigInterpreter.this.execute(it, nodeObject, _function, context);
          }
        }
      };
      IterableExtensions.<AbstractConnectionMappingCall<?,T>>forEach(_incoming, _function);
      List<AbstractConnectionMappingCall<?,T>> _outgoing = nodeMapping.getOutgoing();
      final Procedure1<AbstractConnectionMappingCall<?,T>> _function_1 = new Procedure1<AbstractConnectionMappingCall<?,T>>() {
        public void apply(final AbstractConnectionMappingCall<?,T> it) {
          boolean _or = false;
          boolean _isLazy = it.isLazy();
          boolean _not = (!_isLazy);
          if (_not) {
            _or = true;
          } else {
            boolean _isIsIgnoreLazy = context.isIsIgnoreLazy();
            _or = _isIsIgnoreLazy;
          }
          if (_or) {
            final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
              public void apply(final XConnection it) {
                it.setSource(node);
              }
            };
            XDiagramConfigInterpreter.this.execute(it, nodeObject, _function, context);
          }
        }
      };
      IterableExtensions.<AbstractConnectionMappingCall<?,T>>forEach(_outgoing, _function_1);
      if ((node instanceof OpenableDiagramNode)) {
        DiagramMappingCall<?,T> _nestedDiagram = nodeMapping.getNestedDiagram();
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
      final XtextDomainObjectDescriptor<T> descriptor = this.<T>getDescriptor(connectionObject, connectionMappingCasted);
      final XConnection existingConnection = context.<Object>getConnection(descriptor);
      boolean _notEquals = (!Objects.equal(existingConnection, null));
      if (_notEquals) {
        return existingConnection;
      }
      Function1<? super XtextDomainObjectDescriptor<T>,? extends XConnection> _createConnection = connectionMappingCasted.getCreateConnection();
      final XConnection connection = _createConnection.apply(descriptor);
      final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
        public void apply(final XConnection it) {
          final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
            public void handle(final MouseEvent it) {
              int _clickCount = it.getClickCount();
              boolean _equals = (_clickCount == 2);
              if (_equals) {
                descriptor.revealInEditor();
              }
            }
          };
          it.setOnMouseClicked(_function);
        }
      };
      ObjectExtensions.<XConnection>operator_doubleArrow(connection, _function);
      return connection;
    } else {
      return null;
    }
  }
  
  public <T extends Object, U extends Object> Iterable<T> select(final AbstractNodeMappingCall<T,U> nodeMappingCall, final U domainArgument) {
    if ((nodeMappingCall instanceof NodeMappingCall<?,?>)) {
      final NodeMappingCall<T,U> nodeMappingCallCasted = ((NodeMappingCall<T,U>) nodeMappingCall);
      Function1<? super U,? extends T> _selector = nodeMappingCallCasted.getSelector();
      final T nodeObject = ((Function1<? super Object,? extends T>) (Function1<? super Object,? extends T>)_selector).apply(domainArgument);
      return Collections.<T>unmodifiableList(Lists.<T>newArrayList(nodeObject));
    } else {
      if ((nodeMappingCall instanceof MultiNodeMappingCall<?,?>)) {
        final MultiNodeMappingCall<T,U> nodeMappingCallCasted_1 = ((MultiNodeMappingCall<T,U>) nodeMappingCall);
        Function1<? super U,? extends Iterable<? extends T>> _selector_1 = nodeMappingCallCasted_1.getSelector();
        return ((Function1<? super Object,? extends Iterable<T>>) (Function1<? super Object,? extends Iterable<T>>)_selector_1).apply(domainArgument);
      }
    }
    return null;
  }
  
  protected <T extends Object, U extends Object> Iterable<XNode> execute(final AbstractNodeMappingCall<T,U> nodeMappingCall, final U domainArgument, final InterpreterContext context) {
    final Iterable<T> nodeObjects = this.<T, U>select(nodeMappingCall, domainArgument);
    final ArrayList<XNode> result = CollectionLiterals.<XNode>newArrayList();
    for (final T nodeObject : nodeObjects) {
      NodeMapping<T> _nodeMapping = nodeMappingCall.getNodeMapping();
      XNode _createNode = this.<T>createNode(nodeObject, _nodeMapping, context);
      result.add(_createNode);
    }
    return result;
  }
  
  public <T extends Object, U extends Object> Iterable<T> select(final AbstractConnectionMappingCall<T,U> connectionMappingCall, final U domainArgument) {
    if ((connectionMappingCall instanceof ConnectionMappingCall<?,?>)) {
      final ConnectionMappingCall<T,U> connectionMappingCasted = ((ConnectionMappingCall<T,U>) connectionMappingCall);
      Function1<? super U,? extends T> _selector = connectionMappingCasted.getSelector();
      final T connectionObject = ((Function1<? super Object,? extends T>) (Function1<? super Object,? extends T>)_selector).apply(domainArgument);
      return Collections.<T>unmodifiableList(Lists.<T>newArrayList(connectionObject));
    } else {
      if ((connectionMappingCall instanceof MultiConnectionMappingCall<?,?>)) {
        final MultiConnectionMappingCall<T,U> connectionMappingCasted_1 = ((MultiConnectionMappingCall<T,U>) connectionMappingCall);
        Function1<? super U,? extends Iterable<? extends T>> _selector_1 = connectionMappingCasted_1.getSelector();
        return ((Function1<? super Object,? extends Iterable<T>>) (Function1<? super Object,? extends Iterable<T>>)_selector_1).apply(domainArgument);
      }
    }
    return null;
  }
  
  protected <T extends Object, U extends Object> Iterable<XConnection> execute(final AbstractConnectionMappingCall<T,U> connectionMappingCall, final U domainArgument, final Procedure1<? super XConnection> initializer, final InterpreterContext context) {
    final Iterable<T> connectionObjects = this.<T, U>select(connectionMappingCall, domainArgument);
    final ArrayList<XConnection> result = CollectionLiterals.<XConnection>newArrayList();
    for (final T connectionObject : connectionObjects) {
      {
        ConnectionMapping<T> _connectionMapping = connectionMappingCall.getConnectionMapping();
        final XConnection connection = this.<T>createConnection(connectionObject, _connectionMapping, context);
        result.add(connection);
        initializer.apply(connection);
        ConnectionMapping<T> _connectionMapping_1 = connectionMappingCall.getConnectionMapping();
        this.<T>createEndpoints(_connectionMapping_1, connectionObject, connection, context);
        context.addConnection(connection);
      }
    }
    return result;
  }
  
  protected <T extends Object> void createEndpoints(final ConnectionMapping<T> connectionMapping, final T connectionObject, final XConnection connection, final InterpreterContext context) {
    boolean _and = false;
    XNode _source = connection.getSource();
    boolean _equals = Objects.equal(_source, null);
    if (!_equals) {
      _and = false;
    } else {
      NodeMappingCall<?,T> _source_1 = connectionMapping.getSource();
      boolean _notEquals = (!Objects.equal(_source_1, null));
      _and = _notEquals;
    }
    if (_and) {
      NodeMappingCall<?,T> _source_2 = connectionMapping.getSource();
      Iterable<XNode> _execute = null;
      if (_source_2!=null) {
        _execute=this.execute(_source_2, connectionObject, context);
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
      NodeMappingCall<?,T> _target_1 = connectionMapping.getTarget();
      boolean _notEquals_1 = (!Objects.equal(_target_1, null));
      _and_1 = _notEquals_1;
    }
    if (_and_1) {
      NodeMappingCall<?,T> _target_2 = connectionMapping.getTarget();
      Iterable<XNode> _execute_1 = null;
      if (_target_2!=null) {
        _execute_1=this.execute(_target_2, connectionObject, context);
      }
      XNode _head_1 = IterableExtensions.<XNode>head(_execute_1);
      connection.setTarget(_head_1);
    }
  }
  
  protected <T extends Object, U extends Object> XDiagram execute(final DiagramMappingCall<T,U> diagramMappingCall, final U domainArgument, final InterpreterContext context) {
    Function1<? super U,? extends T> _selector = diagramMappingCall.getSelector();
    final T diagramObject = _selector.apply(domainArgument);
    DiagramMapping<T> _diagramMapping = diagramMappingCall.getDiagramMapping();
    InterpreterContext _interpreterContext = new InterpreterContext();
    final Procedure1<InterpreterContext> _function = new Procedure1<InterpreterContext>() {
      public void apply(final InterpreterContext it) {
        it.setIsIgnoreLazy(true);
      }
    };
    InterpreterContext _doubleArrow = ObjectExtensions.<InterpreterContext>operator_doubleArrow(_interpreterContext, _function);
    final XDiagram result = this.<T>createDiagram(diagramObject, _diagramMapping, _doubleArrow);
    return result;
  }
  
  public <T extends Object> XtextDomainObjectDescriptor<T> getDescriptor(final T domainObject, final AbstractMapping<T> mapping) {
    return this.domainObjectProvider.<T, EObject>createDescriptor(domainObject, mapping);
  }
}
