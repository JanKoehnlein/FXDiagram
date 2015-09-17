package de.fxdiagram.mapping.behavior;

import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.behavior.UpdateAcceptor;
import de.fxdiagram.mapping.AbstractLabelMappingCall;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.behavior.AbstractLabelOwnerReconcileBehavior;
import de.fxdiagram.mapping.behavior.NodeLabelMorphCommand;

@SuppressWarnings("all")
public class NodeReconcileBehavior<T extends Object> extends AbstractLabelOwnerReconcileBehavior<T, XNode> {
  public NodeReconcileBehavior(final XNode host) {
    super(host);
  }
  
  @Override
  protected Iterable<? extends XLabel> getExistingLabels() {
    XNode _host = this.getHost();
    return _host.getLabels();
  }
  
  @Override
  protected Iterable<? extends AbstractLabelMappingCall<?, T>> getLabelMappingCalls(final AbstractMapping<T> mapping) {
    return ((NodeMapping<T>) mapping).getLabels();
  }
  
  @Override
  protected void reconcile(final AbstractMapping<T> mapping, final T domainObject, final UpdateAcceptor acceptor) {
    XNode _host = this.getHost();
    final NodeLabelMorphCommand nodeMorphCommand = new NodeLabelMorphCommand(_host);
    this.compareLabels(mapping, domainObject, nodeMorphCommand);
    boolean _isEmpty = nodeMorphCommand.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      acceptor.morph(nodeMorphCommand);
    }
  }
}
