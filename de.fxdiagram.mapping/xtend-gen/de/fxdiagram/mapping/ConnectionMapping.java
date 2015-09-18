package de.fxdiagram.mapping;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.mapping.AbstractLabelMappingCall;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.ConnectionLabelMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.LabelMappingCall;
import de.fxdiagram.mapping.MultiLabelMappingCall;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.NodeMappingCall;
import de.fxdiagram.mapping.execution.XDiagramConfig;
import de.fxdiagram.mapping.shapes.BaseConnection;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

/**
 * A fixed mapping from a domain object represented by a {@link IMappedElementDescriptor}
 * to an {@link XConnection}.
 * 
 * @see AbstractMapping
 */
@SuppressWarnings("all")
public class ConnectionMapping<T extends Object> extends AbstractMapping<T> {
  private NodeMappingCall<?, T> source;
  
  private NodeMappingCall<?, T> target;
  
  private List<AbstractLabelMappingCall<?, T>> labels = CollectionLiterals.<AbstractLabelMappingCall<?, T>>newArrayList();
  
  public ConnectionMapping(final XDiagramConfig config, final String id, final String displayName) {
    super(config, id, displayName);
  }
  
  /**
   * Called to instantiate the {@link XConnection} of this mapping. Override
   * if you want to change the way the {@link XConnection} should look like,
   * e.g. add a label or arrow heads.
   */
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
  
  public List<AbstractLabelMappingCall<?, T>> getLabels() {
    List<AbstractLabelMappingCall<?, T>> _xblockexpression = null;
    {
      this.initialize();
      _xblockexpression = this.labels;
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
  
  public <U extends Object> LabelMappingCall<U, T> labelFor(final ConnectionLabelMapping<U> labelMapping, final Function1<? super T, ? extends U> selector) {
    LabelMappingCall<U, T> _xblockexpression = null;
    {
      final LabelMappingCall<U, T> call = new LabelMappingCall<U, T>(selector, labelMapping);
      this.labels.add(call);
      _xblockexpression = call;
    }
    return _xblockexpression;
  }
  
  public <U extends Object> MultiLabelMappingCall<U, T> labelForEach(final ConnectionLabelMapping<U> labelMapping, final Function1<? super T, ? extends Iterable<? extends U>> selector) {
    MultiLabelMappingCall<U, T> _xblockexpression = null;
    {
      final MultiLabelMappingCall<U, T> call = new MultiLabelMappingCall<U, T>(selector, labelMapping);
      this.labels.add(call);
      _xblockexpression = call;
    }
    return _xblockexpression;
  }
}
