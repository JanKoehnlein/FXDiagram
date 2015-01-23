package de.fxdiagram.eclipse.mapping;

import de.fxdiagram.eclipse.mapping.AbstractMapping;
import de.fxdiagram.eclipse.mapping.ConnectionMapping;
import de.fxdiagram.eclipse.mapping.ConnectionMappingCall;
import de.fxdiagram.eclipse.mapping.DiagramMapping;
import de.fxdiagram.eclipse.mapping.DiagramMappingCall;
import de.fxdiagram.eclipse.mapping.MappingCall;
import de.fxdiagram.eclipse.mapping.NodeMapping;
import de.fxdiagram.eclipse.mapping.NodeMappingCall;
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
    MappingCall<T, ARG> _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (mapping instanceof NodeMapping) {
        _matched=true;
        _switchResult = new NodeMappingCall<T, ARG>(selector, ((NodeMapping<T>)mapping));
      }
    }
    if (!_matched) {
      if (mapping instanceof DiagramMapping) {
        _matched=true;
        _switchResult = new DiagramMappingCall<T, ARG>(selector, ((DiagramMapping<T>)mapping));
      }
    }
    if (!_matched) {
      if (mapping instanceof ConnectionMapping) {
        _matched=true;
        _switchResult = new ConnectionMappingCall<T, ARG>(selector, ((ConnectionMapping<T>)mapping));
      }
    }
    return this.mappingCalls.add(_switchResult);
  }
  
  public List<MappingCall<?, ARG>> getMappingCalls() {
    return this.mappingCalls;
  }
}
