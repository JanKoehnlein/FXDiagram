package de.fxdiagram.mapping;

import de.fxdiagram.core.XNode;
import de.fxdiagram.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.ConnectionMappingCall;
import de.fxdiagram.mapping.DiagramMapping;
import de.fxdiagram.mapping.DiagramMappingCall;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.MultiConnectionMappingCall;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.shapes.BaseNode;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

/**
 * A fixed mapping from a domain object represented by a {@link IMappedElementDescriptor}
 * to an {@link XNode}.
 * 
 * @see AbstractMapping
 */
@SuppressWarnings("all")
public class NodeMapping<T extends Object> extends AbstractMapping<T> {
  private List<AbstractConnectionMappingCall<?, T>> outgoing = CollectionLiterals.<AbstractConnectionMappingCall<?, T>>newArrayList();
  
  private List<AbstractConnectionMappingCall<?, T>> incoming = CollectionLiterals.<AbstractConnectionMappingCall<?, T>>newArrayList();
  
  private DiagramMappingCall<?, T> nestedDiagram = null;
  
  public NodeMapping(final XDiagramConfig config, final String id, final String displayName) {
    super(config, id, displayName);
  }
  
  public List<AbstractConnectionMappingCall<?, T>> getOutgoing() {
    List<AbstractConnectionMappingCall<?, T>> _xblockexpression = null;
    {
      this.initialize();
      _xblockexpression = this.outgoing;
    }
    return _xblockexpression;
  }
  
  public List<AbstractConnectionMappingCall<?, T>> getIncoming() {
    List<AbstractConnectionMappingCall<?, T>> _xblockexpression = null;
    {
      this.initialize();
      _xblockexpression = this.incoming;
    }
    return _xblockexpression;
  }
  
  public DiagramMappingCall<?, T> getNestedDiagram() {
    DiagramMappingCall<?, T> _xblockexpression = null;
    {
      this.initialize();
      _xblockexpression = this.nestedDiagram;
    }
    return _xblockexpression;
  }
  
  public XNode createNode(final IMappedElementDescriptor<T> descriptor) {
    return new BaseNode<T>(descriptor);
  }
  
  public <U extends Object> ConnectionMappingCall<U, T> outConnectionFor(final ConnectionMapping<U> connectionMapping, final Function1<? super T, ? extends U> selector) {
    ConnectionMappingCall<U, T> _xblockexpression = null;
    {
      final ConnectionMappingCall<U, T> call = new ConnectionMappingCall<U, T>(selector, connectionMapping);
      this.outgoing.add(call);
      _xblockexpression = call;
    }
    return _xblockexpression;
  }
  
  public <U extends Object> ConnectionMappingCall<U, T> inConnectionFor(final ConnectionMapping<U> connectionMapping, final Function1<? super T, ? extends U> selector) {
    ConnectionMappingCall<U, T> _xblockexpression = null;
    {
      final ConnectionMappingCall<U, T> call = new ConnectionMappingCall<U, T>(selector, connectionMapping);
      this.incoming.add(call);
      _xblockexpression = call;
    }
    return _xblockexpression;
  }
  
  public <U extends Object> DiagramMappingCall<?, T> nestedDiagramFor(final DiagramMapping<U> connectionMapping, final Function1<? super T, ? extends U> selector) {
    DiagramMappingCall<U, T> _diagramMappingCall = new DiagramMappingCall<U, T>(selector, connectionMapping);
    return this.nestedDiagram = _diagramMappingCall;
  }
  
  public <U extends Object> MultiConnectionMappingCall<U, T> outConnectionForEach(final ConnectionMapping<U> connectionMapping, final Function1<? super T, ? extends Iterable<? extends U>> selector) {
    MultiConnectionMappingCall<U, T> _xblockexpression = null;
    {
      final MultiConnectionMappingCall<U, T> call = new MultiConnectionMappingCall<U, T>(selector, connectionMapping);
      this.outgoing.add(call);
      _xblockexpression = call;
    }
    return _xblockexpression;
  }
  
  public <U extends Object> MultiConnectionMappingCall<U, T> inConnectionForEach(final ConnectionMapping<U> connectionMapping, final Function1<? super T, ? extends Iterable<? extends U>> selector) {
    MultiConnectionMappingCall<U, T> _xblockexpression = null;
    {
      final MultiConnectionMappingCall<U, T> call = new MultiConnectionMappingCall<U, T>(selector, connectionMapping);
      this.incoming.add(call);
      _xblockexpression = call;
    }
    return _xblockexpression;
  }
}
