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
  private List<AbstractConnectionMappingCall<T>> outgoing = CollectionLiterals.<AbstractConnectionMappingCall<T>>newArrayList();
  
  private List<AbstractConnectionMappingCall<T>> incoming = CollectionLiterals.<AbstractConnectionMappingCall<T>>newArrayList();
  
  private Function1<? super XtextDomainObjectDescriptor<T>,? extends XNode> createNode = new Function1<XtextDomainObjectDescriptor<T>,SimpleNode>() {
    public SimpleNode apply(final XtextDomainObjectDescriptor<T> it) {
      return new SimpleNode(it);
    }
  };
  
  public NodeMapping(final Class<T> typeGuard) {
    super(typeGuard);
  }
  
  public List<AbstractConnectionMappingCall<T>> getOutgoing() {
    return this.outgoing;
  }
  
  public List<AbstractConnectionMappingCall<T>> getIncoming() {
    return this.incoming;
  }
  
  public Function1<? super XtextDomainObjectDescriptor<T>,? extends XNode> getCreateNode() {
    return this.createNode;
  }
  
  public Function1<? super XtextDomainObjectDescriptor<T>,? extends XNode> setCreateNode(final Function1<? super XtextDomainObjectDescriptor<T>,? extends XNode> createNode) {
    return this.createNode = createNode;
  }
  
  public <U extends Object> boolean addOutgoingFor(final ConnectionMapping<U> connectionMapping, final Function1<? super T,? extends U> selector) {
    ConnectionMappingCall _connectionMappingCall = new ConnectionMappingCall(selector, connectionMapping);
    return this.outgoing.add(_connectionMappingCall);
  }
  
  public <U extends Object> boolean addIncomingFor(final ConnectionMapping<U> connectionMapping, final Function1<? super T,? extends U> selector) {
    ConnectionMappingCall _connectionMappingCall = new ConnectionMappingCall(selector, connectionMapping);
    return this.incoming.add(_connectionMappingCall);
  }
  
  public <U extends Object> boolean addOutgoingForEach(final ConnectionMapping<U> connectionMapping, final Function1<? super T,? extends List<? extends U>> selector) {
    MultiConnectionMappingCall _multiConnectionMappingCall = new MultiConnectionMappingCall(selector, connectionMapping);
    return this.outgoing.add(_multiConnectionMappingCall);
  }
  
  public <U extends Object> boolean addIncomingForEach(final ConnectionMapping<U> connectionMapping, final Function1<? super T,? extends List<? extends U>> selector) {
    MultiConnectionMappingCall _multiConnectionMappingCall = new MultiConnectionMappingCall(selector, connectionMapping);
    return this.incoming.add(_multiConnectionMappingCall);
  }
}
