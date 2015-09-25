package de.fxdiagram.mapping;

import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.DiagramMapping;
import de.fxdiagram.mapping.NodeMapping;
import de.fxdiagram.mapping.execution.ConnectionEntryCall;
import de.fxdiagram.mapping.execution.DiagramEntryCall;
import de.fxdiagram.mapping.execution.EntryCall;
import de.fxdiagram.mapping.execution.NodeEntryCall;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class MappingAcceptor<ARG extends Object> {
  private final List<EntryCall<ARG>> entryCalls = CollectionLiterals.<EntryCall<ARG>>newArrayList();
  
  public boolean add(final AbstractMapping<?> mapping) {
    final Function1<ARG, ARG> _function = (ARG it) -> {
      return it;
    };
    return this.<ARG>add(((AbstractMapping<ARG>) mapping), _function);
  }
  
  public <T extends Object> boolean add(final AbstractMapping<T> mapping, final Function1<? super ARG, ? extends T> selector) {
    EntryCall<ARG> _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (mapping instanceof NodeMapping) {
        _matched=true;
        _switchResult = new NodeEntryCall<T, ARG>(selector, ((NodeMapping<T>)mapping));
      }
    }
    if (!_matched) {
      if (mapping instanceof DiagramMapping) {
        _matched=true;
        _switchResult = new DiagramEntryCall<T, ARG>(selector, ((DiagramMapping<T>)mapping));
      }
    }
    if (!_matched) {
      if (mapping instanceof ConnectionMapping) {
        _matched=true;
        _switchResult = new ConnectionEntryCall<T, ARG>(selector, ((ConnectionMapping<T>)mapping));
      }
    }
    return this.entryCalls.add(_switchResult);
  }
  
  public boolean add(final EntryCall<ARG> execution) {
    return this.entryCalls.add(execution);
  }
  
  public List<EntryCall<ARG>> getEntryCalls() {
    return this.entryCalls;
  }
}
