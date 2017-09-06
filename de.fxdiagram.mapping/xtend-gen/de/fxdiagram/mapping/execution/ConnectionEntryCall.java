package de.fxdiagram.mapping.execution;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.ConnectionMappingCall;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.EntryCall;
import de.fxdiagram.mapping.execution.InterpreterContext;
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class ConnectionEntryCall<RESULT extends Object, ARG extends Object> implements EntryCall<ARG> {
  @Accessors(AccessorType.PUBLIC_GETTER)
  private ConnectionMappingCall<RESULT, ARG> mappingCall;
  
  private ConnectionMapping<RESULT> mapping;
  
  public ConnectionEntryCall(final Function1<? super ARG, ? extends RESULT> selector, final ConnectionMapping<RESULT> mapping) {
    ConnectionMappingCall<RESULT, ARG> _connectionMappingCall = new ConnectionMappingCall<RESULT, ARG>(selector, mapping);
    this.mappingCall = _connectionMappingCall;
    this.mapping = mapping;
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
    if (((!Objects.equal(this.mapping.getSource(), null)) && (!Objects.equal(this.mapping.getTarget(), null)))) {
      final Procedure1<XConnection> _function = (XConnection it) -> {
      };
      interpreter.<RESULT, ARG>execute(this.mappingCall, domainObject, _function, context);
    }
  }
  
  @Pure
  public ConnectionMappingCall<RESULT, ARG> getMappingCall() {
    return this.mappingCall;
  }
}
