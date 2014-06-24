package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

/* @Data
 */@SuppressWarnings("all")
public class ConnectionMappingCall<T extends Object, ARG extends Object> extends AbstractConnectionMappingCall<T, ARG> {
  private Function1<? super ARG, ? extends T> selector;
  
  private ConnectionMapping<T> connectionMapping;
}
