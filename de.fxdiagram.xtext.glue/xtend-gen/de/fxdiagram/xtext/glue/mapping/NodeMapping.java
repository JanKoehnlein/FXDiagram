package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.core.XNode;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.ConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.DiagramMapping;
import de.fxdiagram.xtext.glue.mapping.DiagramMappingCall;
import de.fxdiagram.xtext.glue.mapping.MultiConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import de.fxdiagram.xtext.glue.shapes.BaseNode;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class NodeMapping<T extends Object> extends AbstractMapping<T> {
  private List<AbstractConnectionMappingCall<?, T>> outgoing = CollectionLiterals.<AbstractConnectionMappingCall<?, T>>newArrayList();
  
  private List<AbstractConnectionMappingCall<?, T>> incoming = CollectionLiterals.<AbstractConnectionMappingCall<?, T>>newArrayList();
  
  private DiagramMappingCall<?, T> nestedDiagram = null;
  
  public NodeMapping(final XDiagramConfig config, final String id) {
    super(config, id);
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
  
  public XNode createNode(final XtextDomainObjectDescriptor<T> descriptor) {
    return new BaseNode<T>(descriptor);
  }
  
  public <U extends Object> ConnectionMappingCall<?, T> outConnectionFor(final ConnectionMapping<U> connectionMapping, final Function1<? super T, ? extends U> selector) {
    throw new Error("Unresolved compilation problems:"
      + "\nInvalid number of arguments. The constructor ConnectionMappingCall() is not applicable for the arguments ((T)=>U,ConnectionMapping<U>)");
  }
  
  public <U extends Object> ConnectionMappingCall<?, T> inConnectionFor(final ConnectionMapping<U> connectionMapping, final Function1<? super T, ? extends U> selector) {
    throw new Error("Unresolved compilation problems:"
      + "\nInvalid number of arguments. The constructor ConnectionMappingCall() is not applicable for the arguments ((T)=>U,ConnectionMapping<U>)");
  }
  
  public <U extends Object> DiagramMappingCall<?, T> nestedDiagramFor(final DiagramMapping<U> connectionMapping, final Function1<? super T, ? extends U> selector) {
    throw new Error("Unresolved compilation problems:"
      + "\nInvalid number of arguments. The constructor DiagramMappingCall() is not applicable for the arguments ((T)=>U,DiagramMapping<U>)");
  }
  
  public <U extends Object> MultiConnectionMappingCall<?, T> outConnectionForEach(final ConnectionMapping<U> connectionMapping, final Function1<? super T, ? extends Iterable<? extends U>> selector) {
    throw new Error("Unresolved compilation problems:"
      + "\nInvalid number of arguments. The constructor MultiConnectionMappingCall() is not applicable for the arguments ((T)=>Iterable<? extends U>,ConnectionMapping<U>)");
  }
  
  public <U extends Object> MultiConnectionMappingCall<?, T> inConnectionForEach(final ConnectionMapping<U> connectionMapping, final Function1<? super T, ? extends Iterable<? extends U>> selector) {
    throw new Error("Unresolved compilation problems:"
      + "\nInvalid number of arguments. The constructor MultiConnectionMappingCall() is not applicable for the arguments ((T)=>Iterable<? extends U>,ConnectionMapping<U>)");
  }
}
