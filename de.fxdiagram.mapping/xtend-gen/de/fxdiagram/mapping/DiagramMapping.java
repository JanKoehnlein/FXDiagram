package de.fxdiagram.mapping;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.AbstractNodeMappingCall;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.ConnectionMappingCall;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.MultiConnectionMappingCall;
import de.fxdiagram.mapping.MultiNodeMappingCall;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.NodeMappingCall;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.shapes.BaseDiagram;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * A fixed mapping from a domain object represented by a {@link IMappedElementDescriptor}
 * to an {@link XDiagram}.
 * 
 * @see AbstractMapping
 */
@SuppressWarnings("all")
public abstract class DiagramMapping<T extends Object> extends AbstractMapping<T> {
  private List<AbstractNodeMappingCall<?, T>> nodes = CollectionLiterals.<AbstractNodeMappingCall<?, T>>newArrayList();
  
  private List<AbstractConnectionMappingCall<?, T>> connections = CollectionLiterals.<AbstractConnectionMappingCall<?, T>>newArrayList();
  
  private List<ConnectionMapping<?>> eagerConnections = CollectionLiterals.<ConnectionMapping<?>>newArrayList();
  
  public DiagramMapping(final XDiagramConfig config, final String id, final String displayName) {
    super(config, id, displayName);
  }
  
  public List<AbstractNodeMappingCall<?, T>> getNodes() {
    List<AbstractNodeMappingCall<?, T>> _xblockexpression = null;
    {
      this.initialize();
      _xblockexpression = this.nodes;
    }
    return _xblockexpression;
  }
  
  public List<AbstractConnectionMappingCall<?, T>> getConnections() {
    List<AbstractConnectionMappingCall<?, T>> _xblockexpression = null;
    {
      this.initialize();
      _xblockexpression = this.connections;
    }
    return _xblockexpression;
  }
  
  public List<ConnectionMapping<?>> getEagerConnections() {
    List<ConnectionMapping<?>> _xblockexpression = null;
    {
      this.initialize();
      _xblockexpression = this.eagerConnections;
    }
    return _xblockexpression;
  }
  
  public XDiagram createDiagram(final IMappedElementDescriptor<T> descriptor) {
    BaseDiagram<T> _baseDiagram = new BaseDiagram<T>(descriptor);
    final Procedure1<BaseDiagram<T>> _function = (BaseDiagram<T> it) -> {
      it.setLayoutOnActivate(LayoutType.DOT);
    };
    return ObjectExtensions.<BaseDiagram<T>>operator_doubleArrow(_baseDiagram, _function);
  }
  
  public <U extends Object> boolean nodeFor(final NodeMapping<U> nodeMapping, final Function1<? super T, ? extends U> selector) {
    NodeMappingCall<U, T> _nodeMappingCall = new NodeMappingCall<U, T>(selector, nodeMapping);
    return this.nodes.add(_nodeMappingCall);
  }
  
  public <U extends Object> boolean connectionFor(final ConnectionMapping<U> connectionMapping, final Function1<? super T, ? extends U> selector) {
    ConnectionMappingCall<U, T> _connectionMappingCall = new ConnectionMappingCall<U, T>(selector, connectionMapping);
    return this.connections.add(_connectionMappingCall);
  }
  
  public <U extends Object> boolean nodeForEach(final NodeMapping<U> nodeMapping, final Function1<? super T, ? extends Iterable<? extends U>> selector) {
    MultiNodeMappingCall<U, T> _multiNodeMappingCall = new MultiNodeMappingCall<U, T>(selector, nodeMapping);
    return this.nodes.add(_multiNodeMappingCall);
  }
  
  public <U extends Object> boolean connectionForEach(final ConnectionMapping<U> connectionMapping, final Function1<? super T, ? extends Iterable<? extends U>> selector) {
    MultiConnectionMappingCall<U, T> _multiConnectionMappingCall = new MultiConnectionMappingCall<U, T>(selector, connectionMapping);
    return this.connections.add(_multiConnectionMappingCall);
  }
  
  public boolean eagerly(final ConnectionMapping<?>... connectionMapping) {
    return Iterables.<ConnectionMapping<?>>addAll(this.eagerConnections, ((Iterable<? extends ConnectionMapping<?>>)Conversions.doWrapArray(connectionMapping)));
  }
  
  public String getDefaultFilePath(final T element) {
    return null;
  }
}
