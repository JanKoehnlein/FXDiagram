package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import de.fxdiagram.xtext.glue.mapping.NodeMappingCall;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import de.fxdiagram.xtext.glue.shapes.BaseConnection;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class ConnectionMapping<T extends Object> extends AbstractMapping<T> {
  private NodeMappingCall<?, T> source;
  
  private NodeMappingCall<?, T> target;
  
  public ConnectionMapping(final XDiagramConfig config, final String id) {
    super(config, id);
  }
  
  public XConnection createConnection(final IMappedElementDescriptor<T> descriptor) {
    return new BaseConnection<T>(descriptor);
  }
  
  public NodeMappingCall<?, T> getSource() {
    NodeMappingCall<?, T> _xblockexpression = null;
    {
      this.initialize();
      _xblockexpression = this.source;
    }
    return _xblockexpression;
  }
  
  public NodeMappingCall<?, T> getTarget() {
    NodeMappingCall<?, T> _xblockexpression = null;
    {
      this.initialize();
      _xblockexpression = this.target;
    }
    return _xblockexpression;
  }
  
  public <U extends Object> NodeMappingCall<?, T> source(final NodeMapping<U> nodeMapping, final Function1<? super T, ? extends U> selector) {
    NodeMappingCall<U, T> _nodeMappingCall = new NodeMappingCall<U, T>(selector, nodeMapping);
    return this.source = _nodeMappingCall;
  }
  
  public <U extends Object> NodeMappingCall<?, T> target(final NodeMapping<U> nodeMapping, final Function1<? super T, ? extends U> selector) {
    NodeMappingCall<U, T> _nodeMappingCall = new NodeMappingCall<U, T>(selector, nodeMapping);
    return this.target = _nodeMappingCall;
  }
}
