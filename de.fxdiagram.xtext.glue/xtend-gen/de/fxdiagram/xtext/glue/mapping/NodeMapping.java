package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.core.XNode;
import de.fxdiagram.lib.simple.SimpleNode;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.BaseMapping;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.ConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.MultiConnectionMappingCall;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class NodeMapping<T extends Object> extends BaseMapping<T> {
  private List<AbstractConnectionMappingCall<?,T>> outgoing = CollectionLiterals.<AbstractConnectionMappingCall<?,T>>newArrayList();
  
  private List<AbstractConnectionMappingCall<?,T>> incoming = CollectionLiterals.<AbstractConnectionMappingCall<?,T>>newArrayList();
  
  private Function1<? super XtextDomainObjectDescriptor<T>,? extends XNode> createNode = new Function1<XtextDomainObjectDescriptor<T>,SimpleNode>() {
    public SimpleNode apply(final XtextDomainObjectDescriptor<T> it) {
      return new SimpleNode(it);
    }
  };
  
  public NodeMapping(final Class<T> typeGuard) {
    super(typeGuard);
  }
  
  public List<AbstractConnectionMappingCall<?,T>> getOutgoing() {
    return this.outgoing;
  }
  
  public List<AbstractConnectionMappingCall<?,T>> getIncoming() {
    return this.incoming;
  }
  
  public Function1<? super XtextDomainObjectDescriptor<T>,? extends XNode> getCreateNode() {
    return this.createNode;
  }
  
  public Function1<? super XtextDomainObjectDescriptor<T>,? extends XNode> setCreateNode(final Function1<? super XtextDomainObjectDescriptor<T>,? extends XNode> createNode) {
    return this.createNode = createNode;
  }
  
  public <U extends Object> ConnectionMappingCall<U,T> outConnectionFor(final ConnectionMapping<U> connectionMapping, final Function1<? super T,? extends U> selector) {
    ConnectionMappingCall<U,T> _xblockexpression = null;
    {
      final ConnectionMappingCall<U,T> call = new ConnectionMappingCall<U, T>(selector, connectionMapping);
      this.outgoing.add(call);
      _xblockexpression = call;
    }
    return _xblockexpression;
  }
  
  public <U extends Object> ConnectionMappingCall<U,T> inConnectionFor(final ConnectionMapping<U> connectionMapping, final Function1<? super T,? extends U> selector) {
    ConnectionMappingCall<U,T> _xblockexpression = null;
    {
      final ConnectionMappingCall<U,T> call = new ConnectionMappingCall<U, T>(selector, connectionMapping);
      this.incoming.add(call);
      _xblockexpression = call;
    }
    return _xblockexpression;
  }
  
  public <U extends Object> MultiConnectionMappingCall<U,T> outConnectionForEach(final ConnectionMapping<U> connectionMapping, final Function1<? super T,? extends List<? extends U>> selector) {
    MultiConnectionMappingCall<U,T> _xblockexpression = null;
    {
      final MultiConnectionMappingCall<U,T> call = new MultiConnectionMappingCall<U, T>(selector, connectionMapping);
      this.outgoing.add(call);
      _xblockexpression = call;
    }
    return _xblockexpression;
  }
  
  public <U extends Object> MultiConnectionMappingCall<U,T> inConnectionForEach(final ConnectionMapping<U> connectionMapping, final Function1<? super T,? extends List<? extends U>> selector) {
    MultiConnectionMappingCall<U,T> _xblockexpression = null;
    {
      final MultiConnectionMappingCall<U,T> call = new MultiConnectionMappingCall<U, T>(selector, connectionMapping);
      this.incoming.add(call);
      _xblockexpression = call;
    }
    return _xblockexpression;
  }
}
