package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.DiagramMapping;
import de.fxdiagram.xtext.glue.mapping.MappingCall;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

/* @Data
 */@SuppressWarnings("all")
public class DiagramMappingCall<T extends Object, ARG extends Object> implements MappingCall<T, ARG> {
  private Function1<? super ARG, ? extends T> selector;
  
  private DiagramMapping<T> diagramMapping;
  
  public AbstractMapping<T> getMapping() {
    return this.diagramMapping;
  }
}
