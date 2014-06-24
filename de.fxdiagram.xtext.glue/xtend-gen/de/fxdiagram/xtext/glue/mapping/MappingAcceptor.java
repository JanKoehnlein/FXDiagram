package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.MappingCall;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class MappingAcceptor<ARG extends Object> {
  private final List<MappingCall<?, ARG>> mappingCalls = CollectionLiterals.<MappingCall<?, ARG>>newArrayList();
  
  public boolean add(final AbstractMapping<?> mapping) {
    final Function1<ARG, ARG> _function = new Function1<ARG, ARG>() {
      public ARG apply(final ARG it) {
        return it;
      }
    };
    return this.<ARG>add(((AbstractMapping<ARG>) mapping), _function);
  }
  
  public <T extends Object> boolean add(final AbstractMapping<T> mapping, final Function1<? super ARG, ? extends T> selector) {
    throw new Error("Unresolved compilation problems:"
      + "\nInvalid number of arguments. The constructor NodeMappingCall() is not applicable for the arguments ((ARG)=>T,NodeMapping<T>)"
      + "\nInvalid number of arguments. The constructor DiagramMappingCall() is not applicable for the arguments ((ARG)=>T,DiagramMapping<T>)"
      + "\nInvalid number of arguments. The constructor ConnectionMappingCall() is not applicable for the arguments ((ARG)=>T,ConnectionMapping<T>)");
  }
  
  public List<MappingCall<?, ARG>> getMappingCalls() {
    return this.mappingCalls;
  }
}
