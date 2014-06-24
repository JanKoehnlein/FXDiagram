package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractNodeMappingCall;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

/* @Data
 */@SuppressWarnings("all")
public class NodeMappingCall<T extends Object, ARG extends Object> extends AbstractNodeMappingCall<T, ARG> {
  private Function1<? super ARG, ? extends T> selector;
  
  private NodeMapping<T> nodeMapping;
}
