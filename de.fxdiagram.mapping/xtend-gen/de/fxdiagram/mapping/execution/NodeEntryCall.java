package de.fxdiagram.mapping.execution;

import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.NodeMappingCall;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.EntryCall;
import de.fxdiagram.mapping.execution.InterpreterContext;
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class NodeEntryCall<RESULT extends Object, ARG extends Object> implements EntryCall<ARG> {
  @Accessors(AccessorType.PUBLIC_GETTER)
  private NodeMappingCall<RESULT, ARG> mappingCall;
  
  public NodeEntryCall(final Function1<? super ARG, ? extends RESULT> selector, final NodeMapping<RESULT> mapping) {
    NodeMappingCall<RESULT, ARG> _nodeMappingCall = new NodeMappingCall<RESULT, ARG>(selector, mapping);
    this.mappingCall = _nodeMappingCall;
  }
  
  @Override
  public XDiagramConfig getConfig() {
    return this.mappingCall.getMapping().getConfig();
  }
  
  @Override
  public String getText() {
    String _displayName = this.mappingCall.getMapping().getDisplayName();
    String _plus = (_displayName + " (");
    String _label = this.mappingCall.getMapping().getConfig().getLabel();
    String _plus_1 = (_plus + _label);
    return (_plus_1 + ")");
  }
  
  @Override
  public void execute(final ARG domainObject, final XDiagramConfigInterpreter interpreter, final InterpreterContext context) {
    interpreter.<RESULT, ARG>execute(this.mappingCall, domainObject, context);
  }
  
  @Pure
  public NodeMappingCall<RESULT, ARG> getMappingCall() {
    return this.mappingCall;
  }
}
