package de.fxdiagram.mapping;

import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.MappingAcceptor;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.EntryCall;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pure;

@Logging
@SuppressWarnings("all")
public abstract class AbstractDiagramConfig implements XDiagramConfig {
  private Map<String, AbstractMapping<?>> mappings = CollectionLiterals.<String, AbstractMapping<?>>newHashMap();
  
  @Accessors
  private String ID;
  
  @Accessors
  private String label;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private IMappedElementDescriptorProvider domainObjectProvider = this.createDomainObjectProvider();
  
  protected abstract IMappedElementDescriptorProvider createDomainObjectProvider();
  
  @Override
  public AbstractMapping<?> getMappingByID(final String mappingID) {
    return this.mappings.get(mappingID);
  }
  
  protected abstract <ARG extends Object> void entryCalls(final ARG domainArgument, final MappingAcceptor<ARG> acceptor);
  
  @Override
  public <ARG extends Object> Iterable<? extends AbstractMapping<ARG>> getMappings(final ARG domainObject) {
    Collection<AbstractMapping<?>> _values = this.mappings.values();
    final Function1<AbstractMapping<?>, Boolean> _function = (AbstractMapping<?> it) -> {
      return Boolean.valueOf(it.isApplicable(domainObject));
    };
    Iterable<AbstractMapping<?>> _filter = IterableExtensions.<AbstractMapping<?>>filter(_values, _function);
    final Function1<AbstractMapping<?>, AbstractMapping<ARG>> _function_1 = (AbstractMapping<?> it) -> {
      return ((AbstractMapping<ARG>) it);
    };
    return IterableExtensions.<AbstractMapping<?>, AbstractMapping<ARG>>map(_filter, _function_1);
  }
  
  @Override
  public <ARG extends Object> Iterable<? extends EntryCall<ARG>> getEntryCalls(final ARG domainArgument) {
    List<EntryCall<ARG>> _xblockexpression = null;
    {
      final MappingAcceptor<ARG> acceptor = new MappingAcceptor<ARG>();
      this.<ARG>entryCalls(domainArgument, acceptor);
      _xblockexpression = acceptor.getEntryCalls();
    }
    return _xblockexpression;
  }
  
  @Override
  public <ARG extends Object> void addMapping(final AbstractMapping<ARG> mapping) {
    String _iD = mapping.getID();
    boolean _containsKey = this.mappings.containsKey(_iD);
    if (_containsKey) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Duplicate mapping id=");
      String _iD_1 = mapping.getID();
      _builder.append(_iD_1, "");
      _builder.append(" in ");
      _builder.append(this.ID, "");
      AbstractDiagramConfig.LOG.severe(_builder.toString());
    } else {
      String _iD_2 = mapping.getID();
      this.mappings.put(_iD_2, mapping);
    }
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.mapping.AbstractDiagramConfig");
    ;
  
  @Pure
  public String getID() {
    return this.ID;
  }
  
  public void setID(final String ID) {
    this.ID = ID;
  }
  
  @Pure
  public String getLabel() {
    return this.label;
  }
  
  public void setLabel(final String label) {
    this.label = label;
  }
  
  @Pure
  public IMappedElementDescriptorProvider getDomainObjectProvider() {
    return this.domainObjectProvider;
  }
}
