package de.fxdiagram.mapping.execution;

import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.DiagramMapping;
import de.fxdiagram.mapping.DiagramMappingCall;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.EntryCall;
import de.fxdiagram.mapping.execution.InterpreterContext;
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class DiagramEntryCall<RESULT extends Object, ARG extends Object> implements EntryCall<ARG> {
  private DiagramMappingCall<RESULT, ARG> mappingCall;
  
  public DiagramEntryCall(final Function1<? super ARG, ? extends RESULT> selector, final DiagramMapping<RESULT> mapping) {
    DiagramMappingCall<RESULT, ARG> _diagramMappingCall = new DiagramMappingCall<RESULT, ARG>(selector, mapping);
    this.mappingCall = _diagramMappingCall;
  }
  
  @Override
  public XDiagramConfig getConfig() {
    AbstractMapping<RESULT> _mapping = this.mappingCall.getMapping();
    return _mapping.getConfig();
  }
  
  @Override
  public String getText() {
    AbstractMapping<RESULT> _mapping = this.mappingCall.getMapping();
    String _displayName = _mapping.getDisplayName();
    String _plus = (_displayName + " (");
    AbstractMapping<RESULT> _mapping_1 = this.mappingCall.getMapping();
    XDiagramConfig _config = _mapping_1.getConfig();
    String _label = _config.getLabel();
    String _plus_1 = (_plus + _label);
    return (_plus_1 + ")");
  }
  
  @Override
  public void execute(final ARG domainObject, final XDiagramConfigInterpreter interpreter, final InterpreterContext context) {
    context.setIsReplaceRootDiagram(true);
    interpreter.<RESULT, ARG>execute(this.mappingCall, domainObject, context);
  }
}
