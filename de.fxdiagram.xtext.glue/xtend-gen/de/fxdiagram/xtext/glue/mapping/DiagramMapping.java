package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.AbstractNodeMappingCall;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public abstract class DiagramMapping<T extends Object> extends AbstractMapping<T> {
  private List<AbstractNodeMappingCall<?, T>> nodes = CollectionLiterals.<AbstractNodeMappingCall<?, T>>newArrayList();
  
  private List<AbstractConnectionMappingCall<?, T>> connections = CollectionLiterals.<AbstractConnectionMappingCall<?, T>>newArrayList();
  
  public DiagramMapping(final XDiagramConfig config, final String id) {
    super(config, id);
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
  
  public XDiagram createDiagram(final XtextDomainObjectDescriptor<T> descriptor) {
    return new XDiagram();
  }
  
  public <U extends Object> boolean nodeFor(final NodeMapping<U> nodeMapping, final Function1<? super T, ? extends U> selector) {
    throw new Error("Unresolved compilation problems:"
      + "\nInvalid number of arguments. The constructor NodeMappingCall() is not applicable for the arguments ((T)=>U,NodeMapping<U>)");
  }
  
  public <U extends Object> boolean connectionFor(final ConnectionMapping<U> connectionMapping, final Function1<? super T, ? extends U> selector) {
    throw new Error("Unresolved compilation problems:"
      + "\nInvalid number of arguments. The constructor ConnectionMappingCall() is not applicable for the arguments ((T)=>U,ConnectionMapping<U>)");
  }
  
  public <U extends Object> boolean nodeForEach(final NodeMapping<U> nodeMapping, final Function1<? super T, ? extends Iterable<? extends U>> selector) {
    throw new Error("Unresolved compilation problems:"
      + "\nInvalid number of arguments. The constructor MultiNodeMappingCall() is not applicable for the arguments ((T)=>Iterable<? extends U>,NodeMapping<U>)");
  }
  
  public <U extends Object> boolean connectionForEach(final ConnectionMapping<U> connectionMapping, final Function1<? super T, ? extends Iterable<? extends U>> selector) {
    throw new Error("Unresolved compilation problems:"
      + "\nInvalid number of arguments. The constructor MultiConnectionMappingCall() is not applicable for the arguments ((T)=>Iterable<? extends U>,ConnectionMapping<U>)");
  }
}
