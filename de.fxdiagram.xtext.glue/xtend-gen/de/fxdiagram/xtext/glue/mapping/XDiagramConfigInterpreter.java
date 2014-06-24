package de.fxdiagram.xtext.glue.mapping;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.XtextDomainObjectProvider;
import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.AbstractNodeMappingCall;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.DiagramMapping;
import de.fxdiagram.xtext.glue.mapping.DiagramMappingCall;
import de.fxdiagram.xtext.glue.mapping.InterpreterContext;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import de.fxdiagram.xtext.glue.mapping.NodeMappingCall;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
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
      XtextDomainObjectDescriptor<T> _descriptor = this.<T>getDescriptor(diagramObject, diagramMapping);
      final XDiagram diagram = diagramMapping.createDiagram(_descriptor);
      context.diagram = diagram;
      List<AbstractNodeMappingCall<?, T>> _nodes = diagramMapping.getNodes();
      final Procedure1<AbstractNodeMappingCall<?, T>> _function = new Procedure1<AbstractNodeMappingCall<?, T>>() {
        public void apply(final AbstractNodeMappingCall<?, T> it) {
          XDiagramConfigInterpreter.this.execute(it, diagramObject, context);
        }
      };
      IterableExtensions.<AbstractNodeMappingCall<?, T>>forEach(_nodes, _function);
      List<AbstractConnectionMappingCall<?, T>> _connections = diagramMapping.getConnections();
      final Procedure1<AbstractConnectionMappingCall<?, T>> _function_1 = new Procedure1<AbstractConnectionMappingCall<?, T>>() {
        public void apply(final AbstractConnectionMappingCall<?, T> it) {
          final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
            public void apply(final XConnection it) {
            }
          };
          XDiagramConfigInterpreter.this.execute(it, diagramObject, _function, context);
        }
      };
      IterableExtensions.<AbstractConnectionMappingCall<?, T>>forEach(_connections, _function_1);
      _xblockexpression = diagram;
    }
    return _xblockexpression;
  }
  
  protected <T extends Object> XNode createNode(final T nodeObject, final NodeMapping<T> nodeMapping, final InterpreterContext context) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe field isIgnoreLazy is not visible"
      + "\nThe field isIgnoreLazy is not visible");
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
      final XConnection connection = connectionMappingCasted.createConnection(descriptor);
      final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
        public void apply(final XConnection it) {
          final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
            public void handle(final MouseEvent it) {
              int _clickCount = it.getClickCount();
              boolean _equals = (_clickCount == 2);
              if (_equals) {
                descriptor.openInEditor(true);
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
  
  public <T extends Object, U extends Object> Iterable<T> select(final AbstractNodeMappingCall<T, U> nodeMappingCall, final U domainArgument) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe field selector is not visible"
      + "\nThe field selector is not visible");
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
    throw new Error("Unresolved compilation problems:"
      + "\nThe field selector is not visible"
      + "\nThe field selector is not visible");
  }
  
  protected <T extends Object, U extends Object> Iterable<XConnection> execute(final AbstractConnectionMappingCall<T, U> connectionMappingCall, final U domainArgument, final Procedure1<? super XConnection> initializer, final InterpreterContext context) {
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
      NodeMappingCall<?, T> _source_1 = connectionMapping.getSource();
      boolean _notEquals = (!Objects.equal(_source_1, null));
      _and = _notEquals;
    }
    if (_and) {
      NodeMappingCall<?, T> _source_2 = connectionMapping.getSource();
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
      NodeMappingCall<?, T> _target_1 = connectionMapping.getTarget();
      boolean _notEquals_1 = (!Objects.equal(_target_1, null));
      _and_1 = _notEquals_1;
    }
    if (_and_1) {
      NodeMappingCall<?, T> _target_2 = connectionMapping.getTarget();
      Iterable<XNode> _execute_1 = null;
      if (_target_2!=null) {
        _execute_1=this.execute(_target_2, connectionObject, context);
      }
      XNode _head_1 = IterableExtensions.<XNode>head(_execute_1);
      connection.setTarget(_head_1);
    }
  }
  
  public <T extends Object, U extends Object> XDiagram execute(final DiagramMappingCall<T, U> diagramMappingCall, final U domainArgument, final InterpreterContext context) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe field selector is not visible"
      + "\nThe field diagramMapping is not visible"
      + "\nThe field isIgnoreLazy is not visible");
  }
  
  public <T extends Object> XtextDomainObjectDescriptor<T> getDescriptor(final T domainObject, final AbstractMapping<T> mapping) {
    return this.domainObjectProvider.<T, EObject>createDescriptor(domainObject, mapping);
  }
}
