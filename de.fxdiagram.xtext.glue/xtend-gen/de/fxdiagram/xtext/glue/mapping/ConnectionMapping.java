package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.mapping.BaseMapping;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import de.fxdiagram.xtext.glue.mapping.NodeMappingCall;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class ConnectionMapping<T extends Object> extends BaseMapping<T> {
  private NodeMappingCall<?,T> source;
  
  private NodeMappingCall<?,T> target;
  
  private Function1<? super XtextDomainObjectDescriptor<T>,? extends XConnection> createConnection = new Function1<XtextDomainObjectDescriptor<T>,XConnection>() {
    public XConnection apply(final XtextDomainObjectDescriptor<T> it) {
      return new XConnection(it);
    }
  };
  
  public ConnectionMapping(final Class<T> typeGuard) {
    super(typeGuard);
  }
  
  public Function1<? super XtextDomainObjectDescriptor<T>,? extends XConnection> getCreateConnection() {
    return this.createConnection;
  }
  
  public Function1<? super XtextDomainObjectDescriptor<T>,? extends XConnection> setCreateConnection(final Function1<? super XtextDomainObjectDescriptor<T>,? extends XConnection> createConnection) {
    return this.createConnection = createConnection;
  }
  
  public NodeMappingCall<?,T> getSource() {
    return this.source;
  }
  
  public NodeMappingCall<?,T> getTarget() {
    return this.target;
  }
  
  public <U extends Object> NodeMappingCall<?,T> source(final NodeMapping<U> nodeMapping, final Function1<? super T,? extends U> selector) {
    NodeMappingCall<U,T> _nodeMappingCall = new NodeMappingCall<U, T>(selector, nodeMapping);
    return this.source = _nodeMappingCall;
  }
  
  public <U extends Object> NodeMappingCall<?,T> target(final NodeMapping<U> nodeMapping, final Function1<? super T,? extends U> selector) {
    NodeMappingCall<U,T> _nodeMappingCall = new NodeMappingCall<U, T>(selector, nodeMapping);
    return this.target = _nodeMappingCall;
  }
}
