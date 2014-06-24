package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.MappingAcceptor;
import de.fxdiagram.xtext.glue.mapping.MappingCall;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import java.util.List;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@Logging
@SuppressWarnings("all")
public abstract class AbstractDiagramConfig implements XDiagramConfig {
  private Map<String, AbstractMapping<?>> mappings = CollectionLiterals.<String, AbstractMapping<?>>newHashMap();
  
  /* @Property
   */private String ID;
  
  public AbstractMapping<?> getMappingByID(final String mappingID) {
    return this.mappings.get(mappingID);
  }
  
  protected abstract <ARG extends Object> void entryCalls(final ARG domainArgument, final MappingAcceptor<ARG> acceptor);
  
  public <ARG extends Object> Iterable<? extends MappingCall<?, ARG>> getEntryCalls(final ARG domainArgument) {
    List<MappingCall<?, ARG>> _xblockexpression = null;
    {
      final MappingAcceptor<ARG> acceptor = new MappingAcceptor<ARG>();
      this.<ARG>entryCalls(domainArgument, acceptor);
      _xblockexpression = acceptor.getMappingCalls();
    }
    return _xblockexpression;
  }
  
  public <ARG extends Object> void addMapping(final AbstractMapping<ARG> mapping) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field LOG is undefined for the type AbstractDiagramConfig"
      + "\nsevere cannot be resolved");
  }
}
