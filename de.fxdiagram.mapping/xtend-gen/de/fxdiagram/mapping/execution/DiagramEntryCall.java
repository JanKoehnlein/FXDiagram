package de.fxdiagram.mapping.execution;

import de.fxdiagram.mapping.DiagramMapping;
import de.fxdiagram.mapping.DiagramMappingCall;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.EntryCall;
import de.fxdiagram.mapping.execution.InterpreterContext;
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class DiagramEntryCall<RESULT extends Object, ARG extends Object> implements EntryCall<ARG> {
  @Accessors(AccessorType.PUBLIC_GETTER)
  private DiagramMappingCall<RESULT, ARG> mappingCall;
  
  public DiagramEntryCall(final Function1<? super ARG, ? extends RESULT> selector, final DiagramMapping<RESULT> mapping) {
    DiagramMappingCall<RESULT, ARG> _diagramMappingCall = new DiagramMappingCall<RESULT, ARG>(selector, mapping);
    this.mappingCall = _diagramMappingCall;
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
  public DiagramMappingCall<RESULT, ARG> getMappingCall() {
    return this.mappingCall;
  }
}
